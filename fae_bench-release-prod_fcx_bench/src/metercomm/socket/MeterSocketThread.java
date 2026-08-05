//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MeterSocketThread.java
*    @author marcos
*    @date 22 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package metercomm.socket;


import org.slf4j.Logger;

import util.ProcessLog;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.CompletableFuture;

import controller.BenchDataController;
import enumerations.MeterCmdEnum;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import si.dbcomm.model.MeterDataModel;
import util.MeterCommStateEnum;
import util.ViewStatesUtil;

/**
 * @author marcos
 *
 */
public class MeterSocketThread extends Observable implements Runnable {

	/** Logger SLF4J desta classe (infraestrutura de logs da bancada). */
	private static final Logger LOG = ProcessLog.get(MeterSocketThread.class);
	
	private MeterSocketComm meterSocketComm;
	
	private BenchDataController benchDataController = BenchDataController.getInstance();

	private PackageHandler pckHdl;
	
	private ArrayList<MeterDataModel> commPackages;
	
	private MeterDataModel lastPack;
	
	private ByteBuffer dataToSend;
	
	private boolean succefullDataSend = false;
	
	private MeterDataModel pack;

	private boolean stop = false;
	
	private boolean saveData = false;
	
	private boolean enableRead = false;
	
	private boolean checkForAckOrNack = false;
	
	boolean notFoundAckorNack = true;
	
	private boolean sendDataToMeter = false;
	
	private int timeouCheckTime = 525;
	
	private boolean timeouCheckTimeFlag = false;
	
	private boolean hasReachedSampleCount = false;
	
	private int sampleCounter = 0;
	
	private int timeoutCounter = 0;
	
	private int numOfSamples = 45;
	
	private boolean isAcked = false;
	
	private BooleanProperty isWorking;
	
	private BooleanProperty timeoutExceededProperty;

	private BooleanProperty checkingDataProperty;
	
	private BooleanProperty ack;
	
	private ByteBuffer receivedData;

	private BooleanProperty dataSentCorrectlyProperty;
	
	private int timeToSleepForResponse = 50; //ms
	
	private volatile MeterCmdEnum lastSentCommand;
			
//	private byte dataCommandToSend;
	
	private volatile MeterCmdEnum dataCommandToSend;
	
	MeterCommStateEnum meterCommStateEnum;
	
//	HashMap<MeterCommStateEnum, Boolean> operationSuccessStateHashMap;
	
	private boolean isTrasmiting = false;
	
	private boolean forceStopAckChecking = false;
	
	private int maxTimeout = 5;
	
	/**
	 * Creates a object for class MeterSocketThread.java
	 */
	public MeterSocketThread(MeterSocketComm meterSocket) {
		this.meterSocketComm = meterSocket;
		pckHdl = new PackageHandler();
		commPackages = new ArrayList<MeterDataModel>();
//		operationSuccessStateHashMap = new HashMap<>();
		dataSentCorrectlyProperty = new SimpleBooleanProperty();
		checkingDataProperty = new SimpleBooleanProperty();
		isWorking = new SimpleBooleanProperty();
		timeoutExceededProperty = new SimpleBooleanProperty(false);
//		this.initializeOpStateHash();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// Marca esta thread com o medidor que ela atende, para que todas as linhas
		// de log emitidas aqui identifiquem exatamente qual medidor falhou.
		ProcessLog.setMedidorTag(meterSocketComm != null ? meterSocketComm.getIp() : "SOCKET-?");
		ProcessLog.setEtapa("COMM_MEDIDOR");
		LOG.info("ETAPA INICIO: thread de comunicacao do medidor iniciada (IP {})",
				(meterSocketComm != null ? meterSocketComm.getIp() : "desconhecido"));
		long inicioThreadComm = ProcessLog.iniciarCronometro();
		try{
			byte readData;
			while(!stop){
				if (meterSocketComm.isConnected()) {
					if (checkForAckOrNack) {
						//System.out.println("checkForAckOrNack");
						setIsWorking(true);
						isAcked = false;
						try {
							if (checkForAck(lastSentCommand)) {
								checkForAckOrNack = false;
								isAcked = true;
								setDataSentCorrectly(true);
							} else {
								checkForAckOrNack = false;
								isAcked = false;
								setDataSentCorrectly(false);
							}
						} catch (SocketTimeoutException stex) {
							// Registra o timeout com stack trace; o fluxo original (seguir adiante) e mantido.
							ProcessLog.erro(LOG, "MeterSocketThread.run(): TIMEOUT aguardando ACK/NACK do comando "
									+ lastSentCommand + " no medidor " + meterSocketComm.getIp(), stex);
//							System.out.println("TIMEOUT on MeterSocketThread.checkForAck(): " + stex.getMessage());
						} finally {
//							System.out.println("FINALLY sets checkingData to false");
							setCheckingData(false);
							sendDataToMeter = false;
							setIsWorking(false);
						}
					} else {
						if (sendDataToMeter) {
							setCheckingData(true);
							setIsWorking(true);
							boolean verfiedAck = false;
							succefullDataSend = false;
							setDataSentCorrectly(false);

							timeouCheckTimeFlag = true;

							meterSocketComm.sendDataByte(dataCommandToSend.getValue());
							Thread.sleep(20);
							meterSocketComm.sendDataByte(dataCommandToSend.getValue());

							notFoundAckorNack = true;
							
							CompletableFuture cpf = CompletableFuture.runAsync(() -> {
								try {
									Thread.currentThread().setName("Timeout Check Data - "+this.meterSocketComm.getIp());
									Thread.currentThread().sleep(timeouCheckTime);
								} catch (InterruptedException e) {
									System.err.println("ERROR: InterruptedException of assync thread BenchController.assertStableFlow()");
									// e.printStackTrace();
								}
							}).thenAccept((timer) -> timeouCheckTimeFlag = true);

							int idVectorCounter = 0;
							while (notFoundAckorNack) {
								try {
									readData = meterSocketComm.readByte();
									if (readData == meterSocketComm.getUfoId()[idVectorCounter]) {
										idVectorCounter++;
										if (idVectorCounter == 4) {
											readData = meterSocketComm.readByte();
											if (readData == dataCommandToSend.getValue()) {
												readData = meterSocketComm.readByte();
												if (readData == MeterCmdEnum.CMD_ACK.getValue()) {
													notFoundAckorNack = false;
													verfiedAck = true;
													timeoutCounter = 0;
													break;
												} else {
													if (readData == MeterCmdEnum.CMD_NACK.getValue()) {
														verfiedAck = false;
														timeoutCounter = 0;
														break;
													}
												}
											}
											idVectorCounter = 0;
										}
									} else {
										idVectorCounter = 0;
									}
									if (cpf.isDone()) {
										if (meterSocketComm.sendDataByte(dataCommandToSend.getValue())) {
											Thread.currentThread().sleep(20);
											meterSocketComm.sendDataByte(dataCommandToSend.getValue());
										}
										cpf = CompletableFuture.runAsync(() -> {
											try {
												
												Thread.currentThread().setName("Timeout Check Data 2- "+this.meterSocketComm.getIp());
												Thread.currentThread().sleep(timeouCheckTime+25);
											} catch (InterruptedException e) {
												System.err.println("ERROR: InterruptedException of assync thread BenchController.assertStableFlow()");
											}
										}).thenAccept((timer) -> timeouCheckTimeFlag = true);
									}
									continue;
								} catch (SocketTimeoutException e1) {
									System.err.println("ERROR: SocketTimeoutException MeterSocketThread for "+dataCommandToSend.getValue()+" while reading data to check for ack on MeterSocketThread: " + Thread.currentThread().getName());
									timeoutCounter++;
									if (timeoutCounter >= maxTimeout) {
										this.stop = true;
										timeoutExceededProperty.set(true);
										setDataSentCorrectly(false);
										meterSocketComm.disconnect();
										Thread.currentThread().interrupt();
									}
									continue;
								}
							}
							cpf.cancel(true);
							if (verfiedAck) {
								if (dataToSend != null) {
									// This needs to be done to update the value of flag for next data that will be sent
									if (meterSocketComm.sendData(dataToSend)) {
										succefullDataSend = true;
										dataToSend.flip();
										byte dataSent;
										byte dataRead;
										setDataSentCorrectly(false);
										int numOfSentBytes = dataToSend.limit();

										/****************************************
										 * fw version related flow ************************************************** As of version 2.0.0 fw will not send the calib lut back for checking
										 */
										// System.out.println("Fw version from "+meterSocketComm.getIp()+" fwVersionToLong "+fwVersionToLong(this.meterSocketComm.getFwVersion())+ " -
										// "+fwVersionToLong(new byte[]{2,0,0,0,0,0}));
//										if (fwVersionToLong(this.meterSocketComm.getFwVersion()) < fwVersionToLong(new byte[] { 2, 0, 0, 0, 0, 0 })) {// version of
										if (fwVersionToLong(this.meterSocketComm.getFwVersion()) < fwVersionToLong(ViewStatesUtil.arrayMinVersion)) {// version of
											// caso a checagem de dados seja a para tabela de calibra��o
											if (dataCommandToSend.equals(MeterCmdEnum.CMD_LOAD_CALIB_PARAMETERS)) {
												for (int i = 0; i < numOfSentBytes; i++) {
													dataSent = dataToSend.get(i);
													try {
														dataRead = meterSocketComm.readByte();
														if (dataSent != dataRead) {
															setDataSentCorrectly(false);
															break;
														}
														timeoutCounter = 0;

													} catch (SocketTimeoutException so) {
														System.err.println(
																"ERROR: SocketTimeoutException MeterSocketThread " + meterSocketComm.getIp() + " while reading data to check constants download");
														verfiedAck = false;
														setDataSentCorrectly(false);
														timeoutCounter++;
														if (timeoutCounter >= maxTimeout) {
															this.stop = true;
															timeoutExceededProperty.set(true);
															meterSocketComm.disconnect();
															Thread.currentThread().interrupt();
														}
													}
													// if in the last iteration and passed the break
													if (i == numOfSentBytes - 1) {
														setDataSentCorrectly(true);
													}
												}
											} else {
												configureAckOrNackCheck();
											}
										} else {
											configureAckOrNackCheck();
//											System.out.println("Configured to check for ack or nack: MeterSocketThread() 286 " + Thread.currentThread().getName());
										}
										/**************************************** END of fw version related flow **************************************************/
									} else {
										System.err.println("ERROR: Data NOT sent to MeterSocketThread " + meterSocketComm.getIp() + " when trying " + dataCommandToSend.name());
									}
								}
								verfiedAck = false;
							}

							sendDataToMeter = false;
							setCheckingData(false);
							setIsWorking(false);

						} else {
							if (enableRead) {
								if ((receivedData = meterSocketComm.readDataLine()) != null) {
									timeoutCounter = 0;
									pack = pckHdl.unpackData(receivedData);
									lastPack = pack;
									if (pack != null) {
										if (saveData) {
											pack.setIp(this.meterSocketComm.getIp());
											pack.setReadAt(new Date());
											pack.setExpectedFlowRate(benchDataController.getExpectedFlowRate());
											commPackages.add(pack);
											sampleCounter++;
											if (sampleCounter >= numOfSamples) {
												hasReachedSampleCount = true;
											}
										}

										if (!Double.isNaN(pack.getTtstd()) && !Double.isNaN(pack.getTtrev()) && !Double.isNaN(pack.getVel())) {
											if (pack.getTtstd() != 0 || pack.getTtrev() != 0 || pack.getVel() != 0) {
												setChanged();
												notifyObservers(pack);
											}
										}
										// pack.printData();
									} else {
										System.err.println("ERROR: Unable to umpack null package for meter in position " + this.meterSocketComm.getIp());
									}
								} else {
									timeoutCounter++;
									System.err.println("ERROR: Received Package was null on " + this.meterSocketComm.getIp()+" COUNTER: "+timeoutCounter);
									
									// SAving data, meter must reprove in case loses a sample
									if(saveData){
										this.stop = true;
										timeoutExceededProperty.set(true);
										meterSocketComm.disconnect();
										Thread.currentThread().interrupt();
									}else{
										if (timeoutCounter >= maxTimeout) {
											this.stop = true;
											timeoutExceededProperty.set(true);
											meterSocketComm.disconnect();
											Thread.currentThread().interrupt();
										}
									}
								}
							}
						}
					}
				}
				
				try {
					Thread.currentThread().sleep(10);
					//System.out.println("notFoundAckorNack");

				} catch (InterruptedException e) {
					System.err.println("ERROR: InterruptedException MeterSocketThread.run() Thread.currentThread().sleep(5)");
				}
			}
			meterSocketComm.disconnect();
			Thread.currentThread().interrupt();
			if(Thread.interrupted()){
				throw new InterruptedException();
			}
		}catch(NullPointerException e1){
			// Registra com stack trace completo; o tratamento original e mantido.
			ProcessLog.critico(LOG, "MeterSocketThread.run(): NullPointerException na comunicacao do medidor "
					+ this.meterSocketComm.getIp() + " (amostras lidas: " + sampleCounter + ")", e1);
			System.err.println("ERROR: NullPointerException on MeterSocketThread.run() for meter: "+this.meterSocketComm.getIp());
		}catch(InterruptedException ex){
			ProcessLog.erro(LOG, "MeterSocketThread.run(): thread do medidor " + this.meterSocketComm.getIp()
					+ " interrompida (amostras lidas: " + sampleCounter + ")", ex);
			meterSocketComm.disconnect();
			System.err.println("ERROR: Thread for meter "+this.meterSocketComm.getIp()+" is interrupted. Stopping.");
		}
		LOG.info("ETAPA FIM: thread de comunicacao do medidor encerrada apos {} ms. Amostras lidas: {} | timeouts: {}",
				ProcessLog.duracaoMs(inicioThreadComm), sampleCounter, timeoutCounter);
		ProcessLog.limparContexto();
	}

	/**
	 * This function sets the thread state machine to check for ack or nack response once
	 * data parameters are sent to meter
	 * this is to be used when the bench needs to send data (date, wmbus config...) to meter
	 */
	private void configureAckOrNackCheck(){
		lastSentCommand = dataCommandToSend; //sets to check ack for the command which the data was send for
		this.setCheckForAckOrNack(true);
		setIsWorking(true);
		sendDataToMeter = false;
		setCheckingData(true);
	}
	
	/**
	 * Function returns the value of attribute stop
	 * @return the stop
	 */
	public boolean isStop() {
		return stop;
	}

	int i = 0;
	
	/**
	 * Function sets the value for attribute stop
	 * @param stop the stop to set
	 */
	public void setStop(boolean stop) {
//		System.out.println("SETSTOP "+i+": "+stop);
		this.stop = stop;
		i++;
	}

	/**
	 * Function returns the value of attribute stop
	 * @return the stop
	 */
	public void stop() {
		setStop(true);
		
	}

	/**
	 * Function returns the value of attribute commPackages
	 * @return the commPackages
	 */
	public ArrayList<MeterDataModel> getCommPackages() {
		return commPackages;
	}
	
	
	/**
	 * Function sets the value for attribute commPackages
	 * @param commPackages the commPackages to set
	 */
	public void setCommPackages(ArrayList<MeterDataModel> commPackages) {
		this.commPackages = commPackages;
	}

	/**
	 * Function returns the value of attribute saveData
	 * @return the saveData
	 */
	public boolean isSaveData() {
		return saveData;
	}

	/**
	 * Function sets the value for attribute saveData
	 * @param saveData the saveData to set
	 */
	public void setSaveData(boolean saveData) {
		this.saveData = saveData;
	}
	
	/**
	 * Function returns the value of attribute sendDataToMeter
	 * @return the sendDataToMeter
	 */
	public boolean isSendDataToMeter() {
		return sendDataToMeter;
	}
	
	/**
	 * Function sets the value for attribute sendDataToMeter
	 * @param sendDataToMeter the sendDataToMeter to set
	 */
	public void setSendDataToMeter(boolean sendDataToMeter) {
		this.sendDataToMeter = sendDataToMeter;
	}

	/**
	 * Function returns the value of attribute enableRead
	 * @return the enableRead
	 */
	public boolean isEnableRead() {
		return enableRead;
	}

	/**
	 * Function sets the value for attribute enableRead
	 * @param enableRead the enableRead to set
	 */
	public void setEnableRead(boolean enableRead) {
		this.enableRead = enableRead;
	}

	/**
	 * Function returns the value of attribute dataToSend
	 * @return the dataToSend
	 */
	public ByteBuffer getDataToSend() {
		return dataToSend;
	}

	/**
	 * Function sets the value for attribute dataToSend
	 * @param dataToSend the dataToSend to set
	 */
	public void setDataToSend(ByteBuffer dataToSend) {
		this.dataToSend = dataToSend;
	}

	/**
	 * Function returns the value of attribute succefullDataSend
	 * @return the succefullDataSend
	 */
	public boolean isSuccefullDataSend() {
		return succefullDataSend;
	}

	/**
	 * Function sets the value for attribute succefullDataSend
	 * @param succefullDataSend the succefullDataSend to set
	 */
	public void setSuccefullDataSend(boolean succefullDataSend) {
		this.succefullDataSend = succefullDataSend;
	}

	/**
	 * Function returns the value of attribute lastPack
	 * @return the lastPack
	 */
	public MeterDataModel getLastPack() {
		return lastPack;
	}

	/**
	 * Function sets the value for attribute lastPack
	 * @param lastPack the lastPack to set
	 */
	public void setLastPack(MeterDataModel lastPack) {
		this.lastPack = lastPack;
	}

	/**
	 * Function returns the value of attribute forceStopAckChecking
	 * @return the forceStopAckChecking
	 */
	public boolean isForceStopAckChecking() {
		return forceStopAckChecking;
	}

	/**
	 * Function sets the value for attribute forceStopAckChecking
	 * @param forceStopAckChecking the forceStopAckChecking to set
	 */
	public void setForceStopAckChecking(boolean forceStopAckChecking) {
		this.forceStopAckChecking = forceStopAckChecking;
	}

	
	/**
	 * Function returns the value of attribute timeoutExceededProperty
	 * @return the timeoutExceededProperty
	 */
	public BooleanProperty getTimeoutExceededProperty() {
		return timeoutExceededProperty;
	}

	/**
	 * Function sets the value for attribute timeoutExceededProperty
	 * @param timeoutExceededProperty the timeoutExceededProperty to set
	 */
	public void setTimeoutExceededProperty(BooleanProperty timeoutExceededProperty) {
		this.timeoutExceededProperty = timeoutExceededProperty;
	}

	/**
	 * Function returns the value of attribute meterCommStateEnum
	 * @return the meterCommStateEnum
	 */
	public MeterCommStateEnum getMeterCommStateEnum() {
		return meterCommStateEnum;
	}

	/**
	 * Function sets the value for attribute meterCommStateEnum
	 * @param meterCommStateEnum the meterCommStateEnum to set
	 */
	public void setMeterCommStateEnum(MeterCommStateEnum meterCommStateEnum) {
		this.meterCommStateEnum = meterCommStateEnum;
	}

//	/**
//	 * Function returns the value of attribute operationSuccessStateHashMap
//	 * @return the operationSuccessStateHashMap
//	 */
//	public HashMap<MeterCommStateEnum, Boolean> getOperationSuccessStateHashMap() {
//		return operationSuccessStateHashMap;
//	}
//
//	/**
//	 * Function sets the value for attribute operationSuccessStateHashMap
//	 * @param operationSuccessStateHashMap the operationSuccessStateHashMap to set
//	 */
//	public void setOperationSuccessStateHashMap(
//			HashMap<MeterCommStateEnum, Boolean> operationSuccessStateHashMap) {
//		this.operationSuccessStateHashMap = operationSuccessStateHashMap;
//	}
	
	/**
	 * Function returns the value of attribute isTrasmiting
	 * @return the isTrasmiting
	 */
	public boolean isTrasmiting() {
		return isTrasmiting;
	}

	/**
	 * Function sets the value for attribute isTrasmiting
	 * @param isTrasmiting the isTrasmiting to set
	 */
	public void setTrasmiting(boolean isTrasmiting) {
		this.isTrasmiting = isTrasmiting;
	}
	
	/**
	 * Function returns the value of attribute isAcked
	 * @return the isAcked
	 */
	public boolean isAcked() {
		return isAcked;
	}

	/**
	 * Function sets the value for attribute isAcked
	 * @param isAcked the isAcked to set
	 */
	public void setAcked(boolean isAcked) {
		this.isAcked = isAcked;
	}

	/**
	 * Function returns the value of attribute checkForAckOrNack
	 * @return the checkForAckOrNack
	 */
	public boolean isCheckForAckOrNack() {
		return checkForAckOrNack;
	}

	/**
	 * Function sets the value for attribute checkForAckOrNack
	 * @param checkForAckOrNack the checkForAckOrNack to set
	 */
	public void setCheckForAckOrNack(boolean checkForAckOrNack) {
		this.checkForAckOrNack = checkForAckOrNack;
	}
	
	/**
	 * Function returns the value of attribute hasReachedSampleCount
	 * @return the hasReachedSampleCount
	 */
	public boolean isHasReachedSampleCount() {
		return hasReachedSampleCount;
	}

	/**
	 * Function sets the value for attribute hasReachedSampleCount
	 * @param hasReachedSampleCount the hasReachedSampleCount to set
	 */
	public void setHasReachedSampleCount(boolean hasReachedSampleCount) {
		this.hasReachedSampleCount = hasReachedSampleCount;
	}

	/**
	 * Function returns the value of attribute sampleCounter
	 * @return the sampleCounter
	 */
	public int getSampleCounter() {
		return sampleCounter;
	}

	/**
	 * Function sets the value for attribute sampleCounter
	 * @param sampleCounter the sampleCounter to set
	 */
	public void setSampleCounter(int sampleCounter) {
		this.sampleCounter = sampleCounter;
	}
	
	public BooleanProperty getPropertyAck(){
		return ack;
	}
	
	public void setAck(boolean value){
		this.ack.set(value);
	}
	
	public boolean isAck(){
		return ack.get();
	}
	
	public BooleanProperty getPropertyDataSentCorrectly(){
		return dataSentCorrectlyProperty;
	}
	
	public void setDataSentCorrectly(boolean value){
		this.dataSentCorrectlyProperty.set(value);
	}
	
	public boolean isDataSentCorrectly(){
		return dataSentCorrectlyProperty.get();
	}
	
	public BooleanProperty getCheckingDataProperty(){
		return checkingDataProperty;
	}
	
	public void setCheckingData(boolean value){
		this.checkingDataProperty.set(value);
	}
	
	public boolean isCheckingData(){
		return checkingDataProperty.get();
	}
	
	public BooleanProperty getIsWorkingPorperty(){
		return isWorking;
	}
	
	public void setIsWorking(boolean value){
		this.isWorking.set(value);
	}
	
	public boolean IsWorking(){
		return isWorking.get();
	}


	public void clearCommPacks(){
		this.commPackages.clear();
		System.gc();
	}
	
	public void sendData(ByteBuffer buffer){
		meterSocketComm.sendData(buffer);
	}


	/**
	 * 
	 * Method checks if an ack command has been received to requested command
	 * @return true if ACK, false if NACK
	 */
	private boolean checkForAck(MeterCmdEnum expectedCommand) throws SocketTimeoutException{
		byte ackOrNack;
		MeterCmdEnum receivedCommand;
		boolean retorno = false;
		int[] receivedId = new int[4];
		
		int i = 0;
		while(i<6){
			if(forceStopAckChecking){
				forceStopAckChecking = false;
				break;
			}
			try{
 				receivedId[i] = meterSocketComm.readByte();
// 				System.out.println("Received byte for "+meterSocketComm.getIp()+"  BYTE : "+receivedId[i]+" expected command "+expectedCommand.toString());
			}catch(SocketTimeoutException so){
				System.err.println("ERROR: SocketTimeoutException when in MeterSocketThread.checkForAck() for meter: "+Thread.currentThread().getName());
				retorno = false;
				break; // leaves loop
			}
//			System.out.println("ID for position "+meterSocketComm.getIp()+"  expected : "+meterSocketComm.getUfoId()[i]+" received: "+receivedId[i]);
			if(receivedId[i] == meterSocketComm.getUfoId()[i]){
				i++;
				if(i == 4){
					
					try{
						receivedCommand = MeterCmdEnum.toEnum(meterSocketComm.readByte());
						if(receivedCommand == expectedCommand){
							i++;
							ackOrNack = meterSocketComm.readByte();
							if(ackOrNack == MeterCmdEnum.CMD_ACK.getValue()){
//								System.out.println("MeterSocketThread().checkForAck() ACK: "+receivedCommand);
								retorno = true;
							}else{
								if(ackOrNack == MeterCmdEnum.CMD_NACK.getValue()){
//									System.out.println("MeterSocketThread().checkForAck() NACK: "+receivedCommand);
									retorno = false;
								}
							}
//							System.out.println("MeterSocketThread.checkForAck() checked "+receivedCommand+" conf: "+(MeterCmdEnum.toEnum(ackOrNack))+" position: "+meterSocketComm.getIp());
							i++;
						}else{
//							System.out.println("Received different command then expected loop i="+i+" received: "+receivedCommand+". expected: "+expectedCommand+" position: "+meterSocketComm.getIp());
							i = 0;
						}
					}catch(SocketTimeoutException so){
						System.err.println("ERROR: SocketTimeoutException when reading received command for position "+meterSocketComm.getIp());
						break;
					}
				}
			}else{
				//System.out.println("ERROR: Wrong ID for position "+meterSocketComm.getIp()+"  expected : "+meterSocketComm.getUfoId()[i]+" received: "+receivedId[i]);
				i = 0;
			}
		}
		return retorno;
	}
	
	/**
	 * This method puts the thread to sleep to wait for a response from the meter
	 */
	private void sleepForResponse(){
		try {
			Thread.currentThread().sleep(timeToSleepForResponse);
		} catch (InterruptedException e) {
			ProcessLog.erro(LOG, "MeterSocketThread.sleepForResponse()", e);
			e.printStackTrace();
		}
	}
	
	public boolean sendCommandToMeter(MeterCmdEnum cmdToSend){
		boolean retorno = false;
		isAcked = false;
		lastSentCommand = cmdToSend;
		setDataSentCorrectly(false);
		//TESTESSS
		/*****************/
		setDataCmdType(cmdToSend);
		/*****************/

		
		if(meterSocketComm.sendDataByte(cmdToSend.getValue())){
			sleepForResponse();
			if(meterSocketComm.sendDataByte(cmdToSend.getValue())){
				retorno = true;
			}
		}
		return retorno;
	}
	
//	private boolean sendAndCheckCommand(MeterCmdEnum cmd, MeterCommStateEnum state){
//		boolean retorno = false;
//		if(sendCommandToMeter(cmd)){
//			try {
//				if(checkForAck(cmd)){
//					operationSuccessStateHashMap.put(state, true);
//					retorno = true;
//				}else{
//					operationSuccessStateHashMap.put(state, false);
//					retorno = false;
//				}
//			} catch (SocketTimeoutException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return retorno;
//	}
	
//	private boolean checkIfTransmiting(){
//		boolean retorno = false;
//		if((receivedData = meterSocketComm.readDataLine()) != null){
//			pack = pckHdl.unpackData(receivedData);
//			lastPack = pack;
//			if(pack != null){
//				retorno = true;
//			}else{
//				System.err.println("ERROR: Unable to umpack null package for meter in position "+this.meterSocketComm.getIp());
//			}
//		}else{
//			System.err.println("ERROR: Received Package was null on "+this.meterSocketComm.getIp());
//		}
//		return retorno;
//	}
	
	
	public void removeFlowRate(double expectedFlowRate){
		int size = commPackages.size()-1;
		
		for(int i = size; i >=0; i--){
			if(commPackages.get(i).getExpectedFlowRate() == expectedFlowRate){
				commPackages.remove(i);
			}
		}
	}
	
	public boolean checkFlowRateDataConsistency(){
		boolean retorno  = false;
		int size = commPackages.size()-1;
		double currentFlow;

		if(commPackages != null){
			double previousExpectedFlow = commPackages.get(0).getExpectedFlowRate();
			double nextExpectedFlow = commPackages.get(1).getExpectedFlowRate();
			
			for(int i = size; i >= 0; i--){
				currentFlow = commPackages.get(i).getExpectedFlowRate();
				
				if(i < size){
					nextExpectedFlow = commPackages.get(i+1).getExpectedFlowRate();
					if(nextExpectedFlow == previousExpectedFlow){
						if(previousExpectedFlow == currentFlow){
							retorno = true;
							continue;
						}
					}else{
						previousExpectedFlow = currentFlow;
						if(nextExpectedFlow == currentFlow){
							retorno = true;
							continue;
						}else{
							// this is the case when prev and next are equal but the current is different
							// this means that a flow rate sample is out of place
							retorno = false;
							break;
						}
					}
				}
				if(previousExpectedFlow != currentFlow){
					previousExpectedFlow = currentFlow;
				}
			}
		}
		return retorno;
	}
	
	public void setDataCmdType(MeterCmdEnum cmd){
		this.dataCommandToSend = cmd;
		
	}
	
	public String fwVersionToString(){
		String retorno = "";
		byte[] fwVersion;
		if(this.meterSocketComm != null){
			fwVersion = meterSocketComm.getFwVersion();
			retorno = new String (fwVersion);
//			for(int i = 0; i < fwVersion.length; i++){
//				retorno.a
//			}
		}
		return retorno;
	}
	
	public long fwVersionToLong(byte dado[]){
		byte[] dadosFull = new byte[8];
//		if(dado.length < 8){
//			for(int i = 0; i < dado.length; i++){
//				dadosFull[i] = dado[i];
//			}
//			for(int i = dado.length-1; i < 8; i++){
//				dadosFull[i] = 0;
//			}
//		}
		for (int i = 0; i < dado.length; i++) {
			dadosFull[i] = dado[i];
		}
		
		long result = 0;
	    for (int i = 0; i < 8; i++) {
	        result <<= 8;
	        result |= (dadosFull[i] & 0xFF);
	    }
//	    System.out.println(" "+result);
	    return result;
	}
	
	public long fwVersionToLong(){
		return fwVersionToLong(this.meterSocketComm.getFwVersion());
	}
}
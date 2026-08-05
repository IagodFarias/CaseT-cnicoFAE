//=============================================================================
/*!
*    All rights reserved
*    Fae Tecnologia
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BenchControlImpl.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.impl;

import java.util.ArrayList;
import java.util.Date;

import bciapi.command.model.Ack;
import bciapi.command.model.CloseLine;
import bciapi.command.model.CloseValve;
import bciapi.command.model.GetAlarms;
import bciapi.command.model.GetCounter;
import bciapi.command.model.GetLineState;
import bciapi.command.model.GetPidParameters;
import bciapi.command.model.GetPumpState;
import bciapi.command.model.GetScaleWeight;
import bciapi.command.model.GetSensorHumidity;
import bciapi.command.model.GetSensorLevel;
import bciapi.command.model.GetSensorPressure;
import bciapi.command.model.GetSensorTemp;
import bciapi.command.model.GetValveState;
import bciapi.command.model.Nack;
import bciapi.command.model.OpenLine;
import bciapi.command.model.OpenValve;
import bciapi.command.model.ReadRefMeter;
import bciapi.command.model.Reset;
import bciapi.command.model.ResponseSetAutoFlowRate;
import bciapi.command.model.SetAutoFlowRate;
import bciapi.command.model.SetEnableBuffer;
import bciapi.command.model.SetFlowRate;
import bciapi.command.model.SetPidParameters;
import bciapi.command.model.SetPumpLoad;
import bciapi.command.model.StartCounter;
import bciapi.command.model.StartPump;
import bciapi.command.model.Stop;
import bciapi.command.model.StopCounter;
import bciapi.command.model.StopPump;
import bciapi.command.parent.CommandParent;
import bciapi.interfaces.ApiInterface;
import bciapi.socket.BCISocketComm;
import enumerations.BenchAlarmEnum;
import json.JSonParser;
import si.dbcomm.enumerations.FlowRateStateEnum;
import si.dbcomm.enumerations.LineStateEnum;
import si.dbcomm.enumerations.PumpStateEnum;
import si.dbcomm.enumerations.ValveStateEnum;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.HumiditySensorModel;
import si.dbcomm.model.LevelSensorModel;
import si.dbcomm.model.PidConfigModel;
import si.dbcomm.model.PressureSensorModel;
import si.dbcomm.model.PumpModel;
import si.dbcomm.model.RefMeterModel;
import si.dbcomm.model.ScaleModel;
import si.dbcomm.model.TempSensorModel;
import si.dbcomm.model.ValveModel;
import si.dbcomm.model.WaterLineModel;
import util.PropertiesReaderUtil;
import view.bean.BenchBean;

/**
 * @author Marcos Oliveira
 */
public class BenchControlImpl implements  ApiInterface{

	/** Logger SLF4J dos comandos enviados a BCI (controle da bancada). */
	private static final org.slf4j.Logger LOG = util.ProcessLog.get(BenchControlImpl.class);
 
	
	private BCISocketComm socket = BCISocketComm.getInstance();
	
	private static BenchControlImpl INSTANCE = new BenchControlImpl();
	
	private final String ackResponse = PropertiesReaderUtil.getProperty("field.ack.val");
	
	private final String nackResponse = PropertiesReaderUtil.getProperty("field.nack.val");
	
	 /**
	 * Creates a object for class BenchControlImpl.java
	 */
	public BenchControlImpl() {
		if(INSTANCE == null){
			socket = BCISocketComm.getInstance();
		}
//		socket.connect();
	}
	
	/**
	 * Function returns the instance of object BenchControlImpl
	 * @return
	 */
	public static BenchControlImpl getInstance(){
		if(INSTANCE == null ){
			INSTANCE = new BenchControlImpl();
		}
		return INSTANCE;
	}

	/**
	 * Sends the object command to the JSON parser to prepare for send
	 * @param command
	 */
	public String sendToJSon(CommandParent command){
		JSonParser parser = new JSonParser();
		return parser.parse(command);
	}
	
	/**
	 * Function transforms the JSON read from socket to an command object
	 * @param <T> the class type the json must fit in
	 * @param data - read from socket
	 * @return the object 
	 */
	private <T extends CommandParent> T readFromJson(String data, Class<T> type){
		T retorno = null;
		if(data != null){
			JSonParser parser = new JSonParser();
			retorno = parser.toObject(data, type);
		}else{
			System.err.println("ERROR: data is NULL BenchControlImpl.readFromJson()!");
		}
		return retorno;
	}
	
	/**
	 * 
	 * @param json
	 * @return
	 */
	private String identifyCommand(String json){
		JSonParser parser = new JSonParser();
		return parser.getCommandName(json);
	}
	
	public synchronized boolean checkConnectionWithBci(){
		boolean retorno = false;
		LOG.debug("ENTRADA checkConnectionWithBci(): socket conectado={}", socket.isConnected());
		long inicioCheck = util.ProcessLog.iniciarCronometro();
		if(socket.isConnected()){
			socket.sendData("{\"command\":\"getAlarms\", \"alarmSel\": 0}");
			String command = identifyCommand(socket.readData());
			if(command != null){
				if(command.equals(PropertiesReaderUtil.getProperty("field.getalarms.val"))){
					retorno = true;
				}else{
					LOG.warn("checkConnectionWithBci(): resposta inesperada da BCI: '{}'", command);
					retorno = false;
				}
			}else{
				LOG.error("checkConnectionWithBci(): a BCI nao respondeu ao comando getAlarms");
				retorno = false;
			}
		}else{
			LOG.error("checkConnectionWithBci(): o socket com a BCI NAO esta conectado");
		}
		LOG.info("SAIDA checkConnectionWithBci(): BCI {} - verificado em {} ms",
				(retorno ? "RESPONDENDO" : "SEM RESPOSTA"), util.ProcessLog.duracaoMs(inicioCheck));
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#ack()
	 */
	@Override
	public void ack() {
		Ack ack = new Ack();
		sendToJSon(ack);
	}
	
	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#reset()
	 */
	@Override
	public synchronized boolean reset() {
		boolean retorno = false;
		LOG.info("Enviando comando RESET para a BCI");
		Reset reset = new Reset();
		socket.sendData(sendToJSon(reset));
		String readData = socket.readData();
		LOG.debug("Resposta da BCI ao RESET: {}", readData);
		if(readData != null){
			if(identifyCommand(readData).equals(ackResponse)){
				Ack ack = (Ack) readFromJson(readData, Ack.class);
				if(ack != null){
					if(ack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.reset.val"))){
						retorno = true;
					}else{
						//Else sets the value as -1 to indicate unknown
						System.err.println("ERROR: reset returned NULL BenchControlImpl.setFlowRate()!");
						retorno = false;
					}
				}else{
					System.err.println("ERROR: ack is NULL BenchControlImpl.reset()!");
					retorno = false;
				}
			}else{
				if(identifyCommand(readData).equals(nackResponse)){
					Nack nack = (Nack) readFromJson(readData, Nack.class);
					if(nack != null){
						System.err.println("MESSAGE: NACK on BenchControlImpl.reset()");
						retorno = false;
						setNack();
					}else{
						System.err.println("ERROR: nack is NULL BenchControlImpl.reset()!");
						retorno = false;
					}
				}
			}
		}
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#stop()
	 */
	@Override
	public synchronized boolean stop() {
		boolean retorno = false;
		LOG.info("Enviando comando STOP para a BCI");
		Stop stop = new Stop();
//				stop.setResponseTo(command);
		socket.sendData(sendToJSon(stop));
		String readData = socket.readData();
		LOG.debug("Resposta da BCI ao STOP: {}", readData);

		if(identifyCommand(readData).equals(ackResponse)){
			Ack ack = (Ack) readFromJson(readData, Ack.class);
			if(ack != null){
					retorno = true;
			}else{
				retorno = false;
				System.err.println("ERROR: ack is NULL BenchControlImpl.stop()!");
			}
		}else{
			if(identifyCommand(readData).equals(nackResponse)){
				Nack nack = (Nack) readFromJson(readData, Nack.class);
				if(nack != null){
					System.err.println("MESSAGE: NACK on BenchControlImpl.stop()");
					retorno = false;
					setNack();
				}else{
					retorno = false;
					System.err.println("ERROR: nack is NULL BenchControlImpl.stop()!");
				}
			}
		}
		return retorno;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getAlarms()
	 */
	public int getAlarms(){
		int retorno = 0;
		GetAlarms getAlarms = new GetAlarms();
		socket.sendData(sendToJSon(getAlarms));
		String readData = socket.readData();
		String receivedCommand = identifyCommand(readData);
		if(receivedCommand.equals(getAlarms.getCommand())){
			getAlarms =(GetAlarms) readFromJson(readData, GetAlarms.class);
			retorno = getAlarms.getAlarmSel();
		}else{
			System.err.println("ERROR: Command not identified at BenchControlImpl.getAlarms(). Expected: "+getAlarms.getCommand()+" Received: "+receivedCommand);
		}
		return retorno;
	}
	
	
	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getSensorTemp(model.TempSensorModel)
	 */
	@Override
	public synchronized TempSensorModel getSensorTemp(TempSensorModel sensor) {
		if(sensor != null){
			if(sensor.getTag() != null){
				if(!sensor.getTag().equals("")){
					
					GetSensorTemp getTempSensor = new GetSensorTemp();
					getTempSensor.setSensorSel(sensor.getTag());
					socket.sendData(sendToJSon(getTempSensor));
					String readData = socket.readData();
					String receivedCommand = identifyCommand(readData);
					if(receivedCommand != null){
						if(receivedCommand.equals(getTempSensor.getCommand())){
							getTempSensor = (GetSensorTemp) readFromJson(readData, GetSensorTemp.class);
							if(getTempSensor != null){
								sensor.setTemperature(getTempSensor.getTempRead());
							}else{
								System.err.println("ERROR: sensor returned NULL BenchControlImpl.getSensorTemp()!");
								sensor = null;
							}
						}else{
							System.err.println("ERROR: Command not identified at BenchControlImpl.getSensorTemp(). Expected: "+getTempSensor.getCommand()+" Received: "+receivedCommand);
						}
					}else{
						System.err.println("ERROR:String command is null on BenchControlImpl.getSensorTemp()!");
						
					}
				}else{
					System.err.println("ERROR: sensor.tag is empty string BenchControlImpl.getSensorTemp()!");
					sensor = null;
				}
			}else{
				System.err.println("ERROR: sensor.tag is NULL string BenchControlImpl.getSensorTemp()!");
				sensor = null;
			}
		}else{
			System.err.println("ERROR: parameter sensor is NULL BenchControlImpl.getSensorTemp()!");
			sensor = null;
		}
		return sensor;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getSensorPressure(model.PressureSensorModel)
	 */
	@Override
	public synchronized PressureSensorModel getSensorPressure(PressureSensorModel sensor) {
		if(sensor != null){
			if(sensor.getTag() != null){
				if(!sensor.getTag().equals("")){
					GetSensorPressure getPressureSensor = new GetSensorPressure();
					getPressureSensor.setSensorSel(sensor.getTag());
					socket.sendData(sendToJSon(getPressureSensor));
					String readData = socket.readData();
					String receivedCommand = identifyCommand(readData);
					if(receivedCommand.equals(getPressureSensor.getCommand())){
						getPressureSensor = (GetSensorPressure) readFromJson(readData, GetSensorPressure.class);
						if(getPressureSensor != null){
							sensor.setPressureRead(getPressureSensor.getPressureRead());
						}else{
							System.err.println("ERROR: sensor returned NULL BenchControlImpl.getSensorPressure()!");
							sensor = null;
						}
					}else{
						System.err.println("ERROR: Command not identified at BenchControlImpl.getSensorPressure(). Expected: "+getPressureSensor.getCommand()+" Received: "+receivedCommand);
					}
				}else{
					System.err.println("ERROR: sensor.tag is empty string BenchControlImpl.getSensorPressure()!");
					sensor = null;
				}
			}else{
				System.err.println("ERROR: sensor.tag is NULL string BenchControlImpl.getSensorPressure()!");
				sensor = null;
			}
		}else{
			System.err.println("ERROR: parameter sensor is NULL BenchControlImpl.getSensorPressure()!");
			sensor = null;
			
		}
		return sensor;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getSensorLevel(model.LevelSensorModel)
	 */
	@Override
	public synchronized LevelSensorModel getSensorLevel(LevelSensorModel sensor) {
		
		if(sensor != null){
			if(sensor.getTag() != null){
				if(!sensor.getTag().equals("")){
					GetSensorLevel getLevelSensor = new GetSensorLevel();
					getLevelSensor.setSensorSel(sensor.getTag());
					socket.sendData(sendToJSon(getLevelSensor));
					String readData = socket.readData();
					String receivedCommand = identifyCommand(readData);
					if(receivedCommand.equals(getLevelSensor.getCommand())){
						getLevelSensor = (GetSensorLevel) readFromJson(readData, GetSensorLevel.class);
						if(getLevelSensor != null){
							sensor.setLevel(getLevelSensor.isLevelState());
						}else{
							System.err.println("ERROR: sensor returned NULL BenchControlImpl.getSensorLevel()!");
							sensor = null;
						}
					}else{
						System.err.println("ERROR: Command not identified at BenchControlImpl.getSensorLevel(). Expected: "+getLevelSensor.getCommand()+" Received: "+receivedCommand);
					}
				}else{
					System.err.println("ERROR: sensor.tag is empty string BenchControlImpl.getSensorLevel()!");
					sensor = null;
				}
			}else{
				System.err.println("ERROR: sensor.tag is NULL string BenchControlImpl.getSensorLevel()!");
				sensor = null;
			}
		}else{
			System.err.println("ERROR: parameter sensor is NULL BenchControlImpl.getSensorLevel()!");
			sensor = null;
		}
		return sensor;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getSensorHumidity(model.HumiditySensorModel)
	 */
	@Override
	public synchronized HumiditySensorModel getSensorHumidity(HumiditySensorModel sensor) {
		if(sensor != null){
			if(sensor.getTag() != null){
				if(!sensor.getTag().equals("")){
					GetSensorHumidity getSensorHumidity = new GetSensorHumidity();
					getSensorHumidity.setSensorSel(sensor.getTag());
					socket.sendData(sendToJSon(getSensorHumidity));
					String readData = socket.readData();
					String receivedCommand = identifyCommand(readData);
					if(receivedCommand.equals(getSensorHumidity.getCommand())){
						getSensorHumidity = (GetSensorHumidity) readFromJson(readData, GetSensorHumidity.class);
						//ANalise the possibility of removing the creation of a new object
						if(getSensorHumidity != null){
							sensor.setHumidity(getSensorHumidity.getHumidityRead());
							sensor.setTag(getSensorHumidity.getSensorSel());
						}else{
							System.err.println("ERROR: sensor returned NULL BenchControlImpl.getSensorHumidity()!");
							sensor = null;
						}
					}else{
						System.err.println("ERROR: Command not identified at BenchControlImpl.getSensorHumidity(). Expected: "+getSensorHumidity.getCommand()+" Received: "+receivedCommand);
					}
				}else{
					System.err.println("ERROR: sensor.tag is empty string BenchControlImpl.getSensorHumidity()!");
					sensor = null;
				}
			}else{
				System.err.println("ERROR: sensor.tag is NULL string BenchControlImpl.getSensorHumidity()!");
				sensor = null;
			}
		}else{
			System.err.println("ERROR: parameter sensor is NULL BenchControlImpl.getSensorHumidity()!");
			sensor = null;
		}		
		return sensor;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#openLine(model.WaterLineModel)
	 */
	@Override
	public synchronized WaterLineModel openLine(WaterLineModel line) {
		if(line != null){
			if(line.getTag() != null){
				if(!line.getTag().equals("")){
					OpenLine openLine = new OpenLine();
					socket.sendData(sendToJSon(openLine));
					String readData = socket.readData();
					String receivedCommand = identifyCommand(readData); 
					if(receivedCommand.equals(ackResponse)){
						Ack ack = (Ack) readFromJson(readData, Ack.class);
						if(ack != null){
							//Read to check if ack from BCI is received
							//In case it is received, request the setted value
							if(ack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.openline.val"))){
								line.setState(getLineState(line).getState());
							}else{
								//Else sets the value as -1 to indicate unknown
								line.setState(LineStateEnum.UNKNOWN);
							}
						}else{
							System.err.println("ERROR: ack is NULL BenchControlImpl.openLine()!");
						}
					}else{
						if(receivedCommand.equals(nackResponse)){
							Nack nack = (Nack) readFromJson(readData, Nack.class);
							if(nack != null){
								System.err.println("MESSAGE: NACK on BenchControlImpl.openLine()");
								setNack();
							}else{
								System.err.println("ERROR: sensor response returned NULL BenchControlImpl.openLine()");
								line = null;
							}
						}else{
							System.err.println("ERROR: Command not identified at BenchControlImpl.openLine(). Expected: ACK or NACK Received: "+receivedCommand);
						}
					}
				}else{
					System.err.println("ERROR: sensor.tag is empty string BenchControlImpl.openLine()");
					line = null;
				}
			}else{
				System.err.println("ERROR: sensor.tag is NULL string BenchControlImpl.openLine()");
				line = null;
			}
		}else{
			System.err.println("ERROR: parameter sensor is NULL BenchControlImpl.openLine()");
			line = null;
		}
		return line;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#closeLine(model.WaterLineModel)
	 */
	@Override
	public synchronized WaterLineModel closeLine(WaterLineModel line) {
		if(line != null){
			if(line.getTag() != null){
				if(!line.getTag().equals("")){
			 		CloseLine closeLine = new CloseLine();
					socket.sendData(sendToJSon(closeLine));
					String readData = socket.readData();
					String receivedCommand = identifyCommand(readData);
					if(readData != null){
						if(receivedCommand.equals(ackResponse)){
							Ack ack = (Ack) readFromJson(readData, Ack.class);
							if(ack != null){
								if(ack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.closeline.val"))){
									line.setState(getLineState(line).getState());
								}else{
									//Else sets the value as -1 to indicate unknown
									line.setState(LineStateEnum.UNKNOWN);
								}
							}
						}else{
							if(identifyCommand(readData).equals(nackResponse)){
								Nack nack = (Nack) readFromJson(readData, Nack.class);
								if(nack != null){
									System.err.println("MESSAGE: NACK on BenchControlImpl.closeLine()");
									line.setState(LineStateEnum.UNKNOWN);
									setNack();
								}else{
									System.err.println("ERROR: sensor response returned NULL BenchControlImpl.closeLine()");
									line = null;
								}
							}else{
								System.err.println("ERROR: Command not identified at BenchControlImpl.closeLine(). Expected: ACK or NACK Received: "+receivedCommand);
							}
						}
					}
				}else{
					System.err.println("ERROR: sensor.tag is empty string BenchControlImpl.closeLine()!");
					line = null;
				}
			}else{
				System.err.println("ERROR: sensor.tag is NULL string BenchControlImpl.closeLine()!");
				line = null;
			}
		}else{
			System.err.println("ERROR: parameter sensor is NULL BenchControlImpl.closeLine()!");
			line = null;
		}
		return line;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getLineState(model.WaterLineModel)
	 */
	@Override
	public synchronized WaterLineModel getLineState(WaterLineModel line) {
		if(line != null){
			if(line.getTag() != null){
				if(!line.getTag().equals("")){
					GetLineState getLineState = new GetLineState();
					socket.sendData(sendToJSon(getLineState));
					String readData = socket.readData();
					String receivedCommand = identifyCommand(readData);
					if(receivedCommand.equals(getLineState.getCommand())){
						getLineState = (GetLineState) readFromJson(readData, GetLineState.class);
						if(getLineState != null){
							line.setState(getLineState.getLineState());
						}else{
							line.setState(LineStateEnum.UNKNOWN);
							System.err.println("ERROR: sensor returned NULL BenchControlImpl.getLineState()!");
							line = null;
						}
					}else{
						System.err.println("ERROR: Command not identified at BenchControlImpl.getLineState(). Expected: "+getLineState.getCommand()+" Received: "+receivedCommand);
					}
				}else{
					System.err.println("ERROR: sensor.tag is empty string BenchControlImpl.getLineState()!");
					line = null;
				}
			}else{
				System.err.println("ERROR: sensor.tag is NULL string BenchControlImpl.getLineState()!");
				line = null;
			}
		}else{
			System.err.println("ERROR: parameter sensor is NULL BenchControlImpl.getLineState()!");
			line = null;
		}
		return line;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#setFlowRate(model.FlowRateModel)
	 */
	@Override
	public synchronized FlowRateModel setFlowRate(FlowRateModel flowRate) {
		if(flowRate != null){
			
				SetFlowRate setFlowRate = new SetFlowRate();
				setFlowRate.setFlowRateSel(flowRate.getFlowRate());
				setFlowRate.setLowerLimit(flowRate.getLowerLimit());
				setFlowRate.setUpperLimit(flowRate.getUpperLimit());
				setFlowRate.setPumpSel(flowRate.getPump().getTag());
				setFlowRate.setRefMeterSel(flowRate.getRefMeter().getTag());
				socket.sendData(sendToJSon(setFlowRate));
				String readData = socket.readData();
				String receivedCommand = identifyCommand(readData);
				if(receivedCommand.equals(ackResponse)){
					Ack ack = (Ack) readFromJson(readData, Ack.class);
					//Read to check if ack from BCI is received
					//In case it is received, request the setted value
					if(ack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.setflowrate.val"))){
						flowRate.setState(FlowRateStateEnum.STABLE);
					}else{
						//Else sets the value as -1 to indicate unknown
						flowRate.setState(FlowRateStateEnum.UNKNOWN);
						System.err.println("ERROR: flowRate returned NULL BenchControlImpl.setFlowRate()!");
					}
				}else{
					if(receivedCommand.equals(nackResponse)){
						Nack nack = (Nack) readFromJson(readData, Nack.class);
						if(nack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.setflowrate.val"))){
							System.err.println("MESSAGE: Could not setFlowRate: "+flowRate.getFlowRate()+", NACK received BenchControlImpl.setFlowRate()!");
							setNack();
						}else{
							System.err.println("MESSAGE: Response not expected! JSON received by BenchControlImpl.setFlowRate(): "+readData+"");
							flowRate.setState(FlowRateStateEnum.UNKNOWN);
						}
					}else{
						System.err.println("ERROR: Command not identified at BenchControlImpl.setFlowRate(). Expected: ACK or NACK Received: "+receivedCommand);
					}
				}
					
		}else{
			System.err.println("ERROR: parameter flowRate is NULL BenchControlImpl.setFlowRate()!");
		}
		return flowRate;
	}

	
	

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#setEnableBuffer(boolean, long)
	 */
	@Override
	public synchronized boolean setEnableBuffer(boolean enable, long timeVal, int buffSize) {
		boolean retorno = false;
		SetEnableBuffer setEnableBuff = new SetEnableBuffer();
		if(enable){
			setEnableBuff.setBufferEnable(1);
		}else{
			setEnableBuff.setBufferEnable(0);
		}
		setEnableBuff.setWindowTime(timeVal);
		setEnableBuff.setBufferSize(buffSize);
		socket.sendData(sendToJSon(setEnableBuff));
		String readData = socket.readData();
		String commandName = identifyCommand(readData);
		if(commandName.equals(ackResponse)){
			Ack ack = (Ack) readFromJson(readData, Ack.class);
			//Read to check if ack from BCI is received
			//In case it is received, request the setted value
			if(ack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.setenablebuffer.val"))){
				retorno = true;
			}else{
				//Else sets the value as -1 to indicate unknown
				System.err.println("ERROR: Ack response wrong BenchControlImpl.setEnableBuffer()!");
			}
		}else{
			if(commandName.equals(nackResponse)){
				Nack nack = (Nack) readFromJson(readData, Nack.class);
				if(nack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.startcounter.val"))){
					System.err.println("MESSAGE: Could not enable buffer, NACK received BenchControlImpl.setEnableBuffer()!");
					setNack();
				}else{
					System.err.println("MESSAGE: Response not expected! JSON received by BenchControlImpl.setEnableBuffer(): "+readData+"");
				}
				retorno = false;
			}else{
				System.err.println("ERROR: Command not identified at BenchControlImpl.setEnableBuffer(). Expected: ACK or NACK Received: "+commandName);
			}
		}
		return retorno;
		
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#startCounter()
	 */
	@Override
	public synchronized boolean startCounter(RefMeterModel refMeter) {
		boolean retorno = false;
		StartCounter startCounter = new StartCounter();
		startCounter.setRefMeterSel(refMeter.getTag());
		socket.sendData(sendToJSon(startCounter));
		String readData = socket.readData();
		if(readData!=null){
			String commandName = identifyCommand(readData);
			if(commandName.equals(ackResponse)){
				Ack ack = (Ack) readFromJson(readData, Ack.class);
				//Read to check if ack from BCI is received
				//In case it is received, request the setted value
				if(ack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.startcounter.val"))){
					retorno = true;
				}else{
					//Else sets the value as -1 to indicate unknown
					System.err.println("ERROR: Ack response wrong BenchControlImpl.startCounter()!");
				}
			}else{
				if(commandName.equals(nackResponse)){
					Nack nack = (Nack) readFromJson(readData, Nack.class);
					if(nack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.startcounter.val"))){
						System.err.println("MESSAGE: Could not start counter for: ref meter "+refMeter.getTag()+", NACK received BenchControlImpl.startCounter()!");
						setNack();
					}else{
						System.err.println("MESSAGE: Response not expected! JSON received by BenchControlImpl.startCounter(): "+readData+"");
					}
					retorno = false;
				}else{
					System.err.println("ERROR: Command not identified at BenchControlImpl.startCounter(). Expected: ACK or NACK Received: "+commandName);
				}
			}
		}else{
			System.err.println("ERROR: Command NULL identified at BenchControlImpl.startCounter(). ");
			
		}
		
		return retorno;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#stopCounter()
	 */
	@Override
	public synchronized boolean stopCounter(RefMeterModel refMeter) {
		boolean retorno = false;
		StopCounter stopCounter = new StopCounter();
		stopCounter.setRefMeterSel(refMeter.getTag());
		socket.sendData(sendToJSon(stopCounter));
		String readData = socket.readData();
		String commandName = identifyCommand(readData);
		if(commandName.equals(ackResponse)){
			Ack ack = (Ack) readFromJson(readData, Ack.class);
			//Read to check if ack from BCI is received
			//In case it is received, request the setted value
			if(ack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.stopcounter.val"))){
				retorno = true;
			}else{
				//Else sets the value as -1 to indicate unknown
				retorno = true;
				System.err.println("ERROR: Ack response wrong BenchControlImpl.stopCounter()!");
			}
		}else{
			if(commandName.equals(nackResponse)){
				Nack nack = (Nack) readFromJson(readData, Nack.class);
				if(nack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.stopcounter.val"))){
					System.err.println("MESSAGE: Could not start counter for: ref meter "+refMeter.getTag()+", NACK received BenchControlImpl.stopCounter()!");
					setNack();
				}else{
					System.err.println("MESSAGE: Response not expected! JSON received by BenchControlImpl.stopCounter(): "+readData+"");
				}
				retorno = false;
			}else{
				System.err.println("ERROR: Command not identified at BenchControlImpl.stopCounter(). Expected: ACK or NACK Received: "+commandName);
			}
		}
		return retorno;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getCounter()
	 */
	@Override
	public synchronized int[] getCounter(RefMeterModel refMeter) {
		int[] retorno = new int[2];
		GetCounter getCounter = new GetCounter();
		getCounter.setRefMeterSel(refMeter.getTag());
		socket.sendData(sendToJSon(getCounter));
		String readData = socket.readData();
		String receivedCommand = identifyCommand(readData);
		if(receivedCommand != null){
			if(receivedCommand.equals(getCounter.getCommand())){
				getCounter = (GetCounter) readFromJson(readData, GetCounter.class); 
				if(getCounter != null){
					retorno[0] = getCounter.getCounterRead();
					retorno[1] = getCounter.getTimeRead();
				}
			}else{
				System.err.println("ERROR: Command not identified at BenchControlImpl.getCounter(). Expected: "+getCounter.getCommand()+" Received: "+receivedCommand);
			}
		}else{
			System.err.println("ERROR: Received null command on BenchControlImpl.getCounter(). Expected: "+getCounter.getCommand());
		}
		return retorno;	
	}
	
	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#openValve(model.ValveModel)
	 */
	@Override
	public synchronized ValveModel openValve(ValveModel valve) {
		if(valve != null){
			if(valve.getTag() != null){
				if(!valve.getTag().equals("")){
					OpenValve openValve = new OpenValve();
					openValve.setValveSel(valve.getTag());
					socket.sendData(sendToJSon(openValve));
					String readData = socket.readData();
					String commandName = identifyCommand(readData);
					if(commandName != null){
						if(commandName.equals(ackResponse)){
							Ack ack = (Ack) readFromJson(readData, Ack.class);
							if(ack != null){
								//Read to check if ack from BCI is received
								//In case it is received, request the setted value
								if(ack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.openvalve.val"))){
									valve.setState(getValveState(valve).getState());
								}else{
									//Else sets the value as -1 to indicate unknown
									valve.setState(ValveStateEnum.UNKNOWN);
								}
							}else{
								valve.setState(ValveStateEnum.UNKNOWN);
								System.err.println("ERROR: valve returned NULL BenchControlImpl.openValve()!");
								valve = null;
							}
						}else{
							if(commandName.equals(nackResponse)){
								Nack nack = (Nack) readFromJson(readData, Nack.class);
								if(nack != null){
									if(nack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.openvalve.val"))){
										System.err.println("MESSAGE: Could not open valve: "+valve.getTag()+", NACK received BenchControlImpl.openValve()!");
										setNack();
									}
								}else{
									System.err.println("MESSAGE: Response not expected! JSON received by BenchControlImpl.openValve(): "+readData+"");
									valve.setState(ValveStateEnum.UNKNOWN);
								}
							}else{
								System.err.println("ERROR: Command not identified at BenchControlImpl.openValve(). Expected: ACK or NACK Received: "+commandName);
								valve.setState(ValveStateEnum.UNKNOWN);
							}
						}
					}else{
						System.err.println("ERROR: could not retrieve commandName. It was NULL on BenchControlImpl.openValve()!");
						valve = null;
					}
			}else{
				System.err.println("ERROR: valve.tag is empty string BenchControlImpl.openValve()!");
				valve = null;
			}
		}else{
			System.err.println("ERROR: valve.tag is NULL string BenchControlImpl.openValve()!");
			valve = null;
		}
	}else{
		System.err.println("ERROR: parameter valve is NULL BenchControlImpl.openValve()!");
		valve = null;
	}
		return valve;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#closeValve(model.ValveModel)
	 */
	@Override
	public synchronized ValveModel closeValve(ValveModel valve) {
		if(valve != null){
			if(valve.getTag() != null){
				if(!valve.getTag().equals("")){
					CloseValve closeValve = new CloseValve();
					closeValve.setValveSel(valve.getTag());
					socket.sendData(sendToJSon(closeValve));
					String readData = socket.readData();
					String commandName = identifyCommand(readData);
					if(commandName != null){
						if(commandName.equals(ackResponse)){
							Ack ack = (Ack) readFromJson(readData, Ack.class);
							if(ack != null){
								//Read to check if ack from BCI is received
								//In case it is received, request the setted value
								if(ack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.closevalve.val"))){
									valve.setState(getValveState(valve).getState());
								}else{
									//Else sets the value as -1 to indicate unknown
									valve.setState(ValveStateEnum.UNKNOWN);
								}
							}else{
								valve.setState(ValveStateEnum.UNKNOWN);
								System.err.println("ERROR: valve returned NULL BenchControlImpl.closeValve()!");
								valve = null;
							}
						}else{
							if(commandName.equals(nackResponse)){
								Nack nack = (Nack) readFromJson(readData, Nack.class);
								if(nack != null){
									if(nack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.close.val"))){
										System.err.println("MESSAGE: Could not close valve: "+valve.getTag()+", NACK received BenchControlImpl.closeValve()!");
										setNack();
									}
								}else{
									valve.setState(ValveStateEnum.UNKNOWN);
								}
							}else{
								System.err.println("ERROR: Command not identified at BenchControlImpl.closeValve(). Expected: ACK or NACK Received: "+commandName);
								valve.setState(ValveStateEnum.UNKNOWN);
							}
						}
					}else{
						System.err.println("ERROR: could not retrieve commandName. It was NULL on BenchControlImpl.closeValve()!");
						valve = null;
					}
				}else{
					System.err.println("ERROR: valve.tag is empty string BenchControlImpl.closeValve()!");
					valve = null;
				}
			}else{
				System.err.println("ERROR: valve.tag is NULL string BenchControlImpl.closeValve()!");
				valve = null;
			}
		}else{
			System.err.println("ERROR: parameter valve is NULL BenchControlImpl.closeValve()!");
			valve = null;
		}
		return valve;
	}
    
	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getValveState(model.ValveModel)
	 */
	@Override
	public synchronized ValveModel getValveState(ValveModel valve) {
		if(valve != null){
			if(valve.getTag() != null){
				if(!valve.getTag().equals("")){
					GetValveState getValveState = new GetValveState();
					getValveState.setValveSel(valve.getTag());
					getValveState.setValveState(valve.getState());
					socket.sendData(sendToJSon(getValveState));
					String readData = socket.readData();
					String receivedCommand = identifyCommand(readData);
					if(receivedCommand.equals(getValveState.getCommand())){
						getValveState = (GetValveState) readFromJson(readData, GetValveState.class);
						if(getValveState != null){
							valve.setState(getValveState.getValveState());
						}else{
							valve.setState(ValveStateEnum.UNKNOWN);
							System.err.println("ERROR: valve returned NULL BenchControlImpl.getValveState()!");
							valve = null;
						}
					}else{
						System.err.println("ERROR: Command not identified at BenchControlImpl.getValveState(). Expected: "+getValveState.getCommand()+" Received: "+receivedCommand);
					}
				
				}else{
					System.err.println("ERROR: valve.tag is empty string BenchControlImpl.getValveState()!");
					valve = null;
				}
			}else{
				System.err.println("ERROR: valve.tag is NULL string BenchControlImpl.getValveState()!");
				valve = null;
			}
		}else{
			System.err.println("ERROR: parameter valve is NULL BenchControlImpl.getValveState()!");
			valve = null;
		}
		return valve;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#setPumpLoad(model.PumpModel)
	 */
	@Override
	public synchronized PumpModel setPumpLoad(PumpModel pump) {
		if(pump != null){
			if(pump.getTag() != null){
				if(!pump.getTag().equals("")){
					SetPumpLoad setPumpLoad = new SetPumpLoad();
					setPumpLoad.setPumpSel(pump.getTag());
					setPumpLoad.setPercentSel(pump.getLoad());
					socket.sendData(sendToJSon(setPumpLoad));
					String readData = socket.readData();
					String commandName = identifyCommand(readData);
					if(commandName.equals(ackResponse)){
						
						Ack ack = (Ack) readFromJson(readData, Ack.class);
						if(ack != null){
							//Read to check if ack from BCI is received
							//In case it is received, request the setted value
							if(ack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.setpumpload.val"))){
								pump.setState(getPumpState(pump).getState());
							}else{
								//Else sets the value as -1 to indicate unknown
								pump.setLoad(-1);
								pump.setState(PumpStateEnum.UNKNOWN);
							}
						}else{
							pump.setState(PumpStateEnum.UNKNOWN);
							System.err.println("ERROR: pump returned NULL BenchControlImpl.setPumpLoad()!");
							pump = null;
						}
					}else{
						if(commandName.equals(nackResponse)){
							Nack nack = (Nack) readFromJson(readData, Nack.class);
							if(nack != null){
								if(nack.getResponseTo().equals(PropertiesReaderUtil.getProperty("field.setpumpload.val"))){
									System.err.println("MESSAGE: Could not setLoadPump for pump: "+pump.getTag()+", NACK received BenchControlImpl.setPumpLoad()!");
									setNack();
								}
							}else{
								pump.setState(PumpStateEnum.UNKNOWN);
							}
						}else{
							System.err.println("ERROR: Command not identified at BenchControlImpl.setPumpLoad(). Expected: "+setPumpLoad.getCommand()+" Received: "+commandName);
							pump.setState(PumpStateEnum.UNKNOWN);
						}
					}		
				}else{
					System.err.println("ERROR: pump.tag is empty string BenchControlImpl.setPumpLoad()!");
					pump = null;
				}
			}else{
				System.err.println("ERROR: pump.tag is NULL string BenchControlImpl.setPumpLoad()!");
				pump = null;
			}
		}else{
			System.err.println("ERROR: parameter pump is NULL BenchControlImpl.setPumpLoad()!");
			pump = null;
		}	
		return pump;
	}

	@Override
	public synchronized boolean startPump(PumpModel pump) {
		boolean retorno = false;
		StartPump startPump = new StartPump();
		startPump.setPumpSel(pump.getTag());
		socket.sendData(sendToJSon(startPump));
		String readData = socket.readData();
		String commandName = identifyCommand(readData);
		if(commandName.equals(ackResponse)){
			Ack ack = (Ack) readFromJson(readData, Ack.class);
			if(ack.getResponseTo().equals(startPump.getCommand())){
				pump.setState(PumpStateEnum.ON);
				retorno = true;
			}else{
				retorno = false;
			}
		}else{
			if(commandName.equals(nackResponse)){
				Nack nack = (Nack) readFromJson(readData, Nack.class);
				if(nack.getResponseTo().equals(startPump.getCommand())){
					retorno = false;
					setNack();
				}else{
					System.err.println("MESSAGE: Response not expected! JSON received by BenchControlImpl.startPump(): "+readData+"");
					retorno = false;
					pump.setState(PumpStateEnum.UNKNOWN);
				}
			}else{
				System.err.println("ERROR: Command not identified at BenchControlImpl.startPump(). Expected: "+startPump.getCommand()+" Received: "+commandName);
			}
		}
		return retorno;
	}
	
	@Override
	public synchronized boolean stopPump(PumpModel pump) {
		boolean retorno = false;
		StopPump stopPump = new StopPump();
		stopPump.setPumpSel(pump.getTag());
		socket.sendData(sendToJSon(stopPump));
		String readData = socket.readData();
		String commandName = identifyCommand(readData);
		if(commandName.equals(ackResponse)){
			Ack ack = (Ack) readFromJson(readData, Ack.class);
			if(ack.getResponseTo().equals(stopPump.getCommand())){
				retorno = true;
			}else{
				retorno = false;
			}
		}else{
			if(commandName.equals(nackResponse)){
				Nack nack = (Nack) readFromJson(readData, Nack.class);
				if(nack.getResponseTo().equals(stopPump.getCommand())){
					retorno = false;
					setNack();
				}else{
					System.err.println("MESSAGE: Response not expected! JSON received by BenchControlImpl.stopPump(): "+readData+"");
					retorno = false;
					pump.setState(PumpStateEnum.UNKNOWN);
				}
			}else{
				System.err.println("ERROR: Command not identified at BenchControlImpl.stopPump(). Expected: "+stopPump.getCommand()+" Received: "+commandName);
			}
		}
		return retorno;
	}
	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getPumpLoad(model.PumpModel)
	 */
	//@Override
	/*	public PumpModel getPumpLoad(PumpModel pump) {
		GetPumpLoad getPumpLoad = new GetPumpLoad();
		getPumpLoad.setPumpSel(pump.getId());
		socket.sendData(sendToJSon(getPumpLoad));
		getPumpLoad = (GetPumpLoad) readFromJson(socket.readData(), GetPumpLoad.class);
		pump.setLoad(getPumpLoad.getPercenSel());
		return pump;
	}*/

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getPumpState(model.PumpModel)
	 */
	@Override
	public synchronized PumpModel getPumpState(PumpModel pump) {
		if(pump != null){
			if(pump.getTag() != null){
				if(!pump.getTag().equals("")){
					GetPumpState getPumpState = new GetPumpState();
					getPumpState.setPumpSel(pump.getTag());
					socket.sendData(sendToJSon(getPumpState));
					String readData = socket.readData();
					String receivedCommand = identifyCommand(readData);
					if(receivedCommand.equals(getPumpState.getCommand())){
						getPumpState = (GetPumpState) readFromJson(readData, GetPumpState.class);
						if(getPumpState != null){
							pump.setState(getPumpState.getPumpState());
						}else{
							//Else sets the value as -1 to indicate unknown
							pump.setLoad(-1);
							pump.setState(PumpStateEnum.UNKNOWN);
							System.err.println("ERROR: pump returned NULL BenchControlImpl.getPumpState()!");
						}
					}else{
						System.err.println("ERROR: Command not identified at BenchControlImpl.getPumpState(). Expected: "+getPumpState.getCommand()+" Received: "+receivedCommand);
					}
				}else{
					System.err.println("ERROR: pump.tag is empty string BenchControlImpl.getPumpState()!");
					pump = null;
				}
			}else{
				System.err.println("ERROR: pump.tag is NULL string BenchControlImpl.getPumpState()!");
				pump = null;
			}
		}else{
			System.err.println("ERROR: parameter pump is NULL BenchControlImpl.getPumpState()!");
			pump = null;
		}	
		return pump;
	}

//	
//	These following blocks have been removed and substituted in one command
	/* (non-Javadoc)
//	 * @see bciapi.interfaces.ApiInterface#readFlowRateRefMeter(model.RefMeterModel)
//	 */
//	@Override
//	public RefMeterModel readFlowRateRefMeter(RefMeterModel refMeter) {
//		ReadRefMeter readFlowRateRefMeter = new ReadRefMeter();
//		readFlowRateRefMeter.setRefMeterSel(refMeter.getId());
//		socket.sendData(sendToJSon(readFlowRateRefMeter));
//		readFlowRateRefMeter = (ReadRefMeter) readFromJson(socket.readData(), ReadRefMeter.class);
//		FlowRateModel receivedFlow = new FlowRateModel();
//		receivedFlow.setFlowRate(readFlowRateRefMeter.getFlowRate());
//		refMeter.setFlowRate(receivedFlow.getFlowRate());
//		return refMeter;
//	}
//	/* (non-Javadoc)
//	 * @see bciapi.interfaces.ApiInterface#readVolumeRefMeter(model.RefMeterModel)
//	 */
//	@Override
//	public RefMeterModel readVolumeRefMeter(RefMeterModel refMeter) {
//		ReadVolumeRefMeter readVolumeRefMeter = new ReadVolumeRefMeter();
//		readVolumeRefMeter.setRefMeterSel(refMeter.getId());
//		socket.sendData(sendToJSon(readVolumeRefMeter));
//		readVolumeRefMeter = (ReadVolumeRefMeter) readFromJson(socket.readData(), ReadVolumeRefMeter.class);
//		VolumeModel receivedVolume = new VolumeModel();
//		receivedVolume.setVolume(readVolumeRefMeter.getVolumeVal());
//		refMeter.setVolume(receivedVolume.getVolume());
//		return refMeter;
//	}
	
	
	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#readVolumeRefMeter(model.RefMeterModel)
	 */
	@Override
	public synchronized RefMeterModel readRefMeter(RefMeterModel refMeter) {
		
		if(refMeter != null){
			if(refMeter.getTag() != null){
				if(!refMeter.getTag().equals("")){
					ReadRefMeter readRefMeter = new ReadRefMeter();
					readRefMeter.setRefMeterSel(refMeter.getTag());
					socket.sendData(sendToJSon(readRefMeter));
					String readData = socket.readData();
					String receivedCommand = identifyCommand(readData);
					if(receivedCommand != null){
						if(receivedCommand.equals(readRefMeter.getCommand())){
							readRefMeter = (ReadRefMeter) readFromJson(readData, ReadRefMeter.class);
							if(readRefMeter!=null){
								refMeter.setFlowRate(readRefMeter.getFlowRate());
								refMeter.setVolume(readRefMeter.getVolume());
								refMeter.setFlowStability(readRefMeter.getFlowStability());
								refMeter.setReadtime(new Date());
								refMeter.setTimeread(readRefMeter.getTimeRead());
								
							}else{
								System.err.println("ERROR: refMeter returned NULL BenchControlImpl.readRefMeter()!");
							}
						}else{
							System.err.println("ERROR: Command not identified at BenchControlImpl.readRefMeter(). Expected: "+readRefMeter.getCommand()+" Received: "+receivedCommand);
						}
					}else{
						System.err.println("ERROR: Received command is null at BenchControlImpl.readRefMeter(). Expected: "+readRefMeter.getCommand());
					}
				}else{
					System.err.println("ERROR: refMeter.tag is empty string BenchControlImpl.readRefMeter()!");
					//refMeter = null;
				}
			}else{
				System.err.println("ERROR: refMeter.tag is NULL string BenchControlImpl.readRefMeter()!");
				//refMeter = null;
			}
		}else{
			System.err.println("ERROR: parameter refMeter is NULL BenchControlImpl.readRefMeter()!");
			//refMeter = null;
		}	
		
		return refMeter;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getScaleWeight(model.ScaleModel)
	 */
	@Override
	public synchronized ScaleModel getScaleWeight(ScaleModel scale) {
		if(scale != null){
			if(scale.getTag() != null){
				if(!scale.getTag().equals("")){
					GetScaleWeight getScaleWeight = new GetScaleWeight();
					socket.sendData(sendToJSon(getScaleWeight));
					String readData = socket.readData();
					String receivedCommand = identifyCommand(readData);
					if(receivedCommand.equals(getScaleWeight.getCommand())){
						//TODO - change from comma to decimal point
						getScaleWeight = (GetScaleWeight) readFromJson(readData, GetScaleWeight.class);
						if(getScaleWeight != null){
							scale.setWeight(getScaleWeight.getWeight());
						}else{
							System.err.println("ERROR: scale returned NULL BenchControlImpl.getScaleWeight()!");
						}
					}else{
						System.err.println("ERROR: Command not identified at BenchControlImpl.getScaleWeight(). Expected: "+getScaleWeight.getCommand()+" Received: "+receivedCommand);
					}
				}else{
					System.err.println("ERROR: scale.tag is empty string BenchControlImpl.getScaleWeight()!");
					scale = null;
				}
			}else{
				System.err.println("ERROR: scale.tag is NULL string BenchControlImpl.getScaleWeight()!");
				scale = null;
			}
		}else{
			System.err.println("ERROR: parameter scale is NULL BenchControlImpl.getScaleWeight()!");
			scale = null;
		}	
		return scale;
	}

	/**
	 * Sets a flowrate to run for the duration of secSel seconds repeating rep times. The "period" parameter 
	 * indicates the number time in wich the BCI will send the read data on ResponseSetAutoFlowRate();
	 * @return
	 */
	@Override
	public synchronized ResponseSetAutoFlowRate setAutoFlowRate(FlowRateModel flowrate, int secSel, int rep, int interval,	int period,	boolean onChange){
		ResponseSetAutoFlowRate response = new ResponseSetAutoFlowRate();
		SetAutoFlowRate setAutoFlowRate = new SetAutoFlowRate();
		int totalTime = (secSel+interval)*rep;
		int numberOfResponses = totalTime/period;
		numberOfResponses++;
		setAutoFlowRate.setFlowRateSel(flowrate.getFlowRate());
		setAutoFlowRate.setSecSel(secSel);
		setAutoFlowRate.setRep(rep);
		setAutoFlowRate.setInterval(interval);
		setAutoFlowRate.setPeriod(period);
		setAutoFlowRate.setOnChange(onChange);
		socket.sendData(sendToJSon(setAutoFlowRate));
		for(int i = 0; i < numberOfResponses; i++){
			String responseString = socket.readData();
//			System.out.println("Resposta recebida |"+i+"| : "+responseString);
			response = (ResponseSetAutoFlowRate) readFromJson(responseString, ResponseSetAutoFlowRate.class);
		}
		//TODO add code to receive response from BCI
		return response;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#setSeqFlowRates(java.util.ArrayList, int, int, int, int, boolean)
	 */
	@Override
	public synchronized ResponseSetAutoFlowRate setSeqFlowRates(
			ArrayList<FlowRateModel> flowRates, int secSel, int rep,
			int interval, int period, boolean onChange) {
		// TODO Auto-generated method stub
		return null;
	}

	private void setNack() {
		int alarms = this.getAlarms();
		System.out.println("NACK de alarme: " + alarms);

		// Emergency Activated alarm
		if (BenchAlarmEnum.isEmergencyActivated(alarms)) {
			BenchBean.getInstance().getEmergencyLed().turnLedRed();
		} else {
			BenchBean.getInstance().getEmergencyLed().turnLedOff();
		}

		// Low Air Pressure alarm
		if (BenchAlarmEnum.isLowAirPressure(alarms)) {
			BenchBean.getInstance().getPressLed().turnLedRed();
		} else {
			BenchBean.getInstance().getPressLed().turnLedOff();
		}

		// Low level inferior reservoir alarm
		if (BenchAlarmEnum.isLowLevelInferiorReservoir(alarms)) {
			BenchBean.getInstance().getInferiorLevel().turnLedRed();
		} else {
			BenchBean.getInstance().getInferiorLevel().turnLedOff();
		}

		// Thermal BP1 alarm
		if (BenchAlarmEnum.isThermalBp1(alarms)) {
			BenchBean.getInstance().getBp1Led().turnLedRed();
		} else {
			BenchBean.getInstance().getBp1Led().turnLedOff();
		}

		// Thermal BP2 alarm
		if (BenchAlarmEnum.isThermalBp2(alarms)) {
			BenchBean.getInstance().getBp2Led().turnLedRed();
		} else {
			BenchBean.getInstance().getBp2Led().turnLedOff();
		}

		// Thermal BREP alarm
		if (BenchAlarmEnum.isThermalBrep(alarms)) {
			BenchBean.getInstance().getBrepLed().turnLedRed();
		} else {
			BenchBean.getInstance().getBrepLed().turnLedOff();
		}
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#setPidParameters(model.PumpModel, double, double, double)
	 */
	@Override
	public boolean setPidParameters(PumpModel pumpVal, double kpVal, double tmVal, double tvVal) {
		boolean retorno = false;
		SetPidParameters setPidParameters = new SetPidParameters();
		setPidParameters.setPumpSel(pumpVal.getTag());
		setPidParameters.setKpSel(kpVal);
		setPidParameters.setTmSel(tmVal);
		setPidParameters.setTvSel(tvVal);
		
		socket.sendData(sendToJSon(setPidParameters));
		String readData = socket.readData();
		String commandName = identifyCommand(readData);
		if(commandName.equals(ackResponse)){
			Ack ack = (Ack) readFromJson(readData, Ack.class);
			if(ack.getResponseTo().equals(setPidParameters.getCommand())){
				retorno = true;
			}else{
				retorno = false;
			}
		}else{
			if(commandName.equals(nackResponse)){
				Nack nack = (Nack) readFromJson(readData, Nack.class);
				if(nack.getResponseTo().equals(setPidParameters.getCommand())){
					retorno = false;
					setNack();
				}else{
					System.err.println("MESSAGE: Response not expected! JSON received by BenchControlImpl.setPidParameters(): "+readData+"");
					retorno = false;
				}
			}else{
				System.err.println("ERROR: Command not identified at BenchControlImpl.setPidParameters(). Expected: "+setPidParameters.getCommand()+" Received: "+commandName);
			}
		}
		return retorno;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getPidParameters(model.PumpModel, double, double, double, double)
	 */
	@Override
	public PidConfigModel getPidParameters(PumpModel pumpVal) {
		PidConfigModel pidConfig = new PidConfigModel();
		GetPidParameters getPidParameters = new GetPidParameters();
		getPidParameters.setPumpSel(pumpVal.getTag());
		socket.sendData(sendToJSon(getPidParameters));
		String readData = socket.readData();
		String commandName = identifyCommand(readData);
		if(commandName.equals(getPidParameters.getCommand())){
			getPidParameters = (GetPidParameters) readFromJson(readData, GetPidParameters.class);
			pidConfig.setDerivativeTv(getPidParameters.getTvSel());
			pidConfig.setIntegrativeTm(getPidParameters.getTmSel());
			pidConfig.setProportionalKp(getPidParameters.getKpSel());
			pidConfig.setErrorE(getPidParameters.getE());
		}else{
			System.err.println("ERROR: Command not identified at BenchControlImpl.getPidParameters(). Expected: "+getPidParameters.getCommand()+" Received: "+commandName);
		}
		
		return pidConfig;
	}
	
	
		
}

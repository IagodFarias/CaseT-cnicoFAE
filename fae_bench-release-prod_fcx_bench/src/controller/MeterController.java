//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MeterController.java
*    @author marcos
*    @date 25 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;


import org.slf4j.Logger;

import util.ProcessLog;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import enumerations.MeterCmdEnum;
import exceptions.CryptoMd5Digest;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import metercomm.socket.MeterSocketComm;
import metercomm.socket.MeterSocketThread;
import model.EstimatedDistanceModel;
import services.CalibrationService;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.CalculatedFixedConstModel;
import si.dbcomm.model.CalibConstantsModel;
import si.dbcomm.model.CriticalPointModel;
import si.dbcomm.model.DistancePointsModel;
import si.dbcomm.model.FlowRateMeanStdModel;
import si.dbcomm.model.LogChangeProcessBatchModel;
import si.dbcomm.model.MeterDataModel;
import si.dbcomm.model.MeterModel;
import si.dbcomm.model.MeterTypeModel;
import si.dbcomm.model.RadioWmBusConfigModel;
import si.dbcomm.model.VerificationErrorModel;
import si.dbcomm.service.CalibConstantsService;
import si.dbcomm.service.CriticalPointService;
import si.dbcomm.service.DistancePointsService;
import si.dbcomm.service.FlowRateMeanStdService;
import si.dbcomm.service.LogChangeProcessBatchService;
import si.dbcomm.service.MeterService;
import si.dbcomm.service.RadioWmBusConfigService;
import si.dbcomm.service.VerificationErrorService;
import util.TypeConverterUtil;
import util.ViewStatesUtil;
import view.bean.MeterBean;

/**
 * @author marcos
 *
 */
public class MeterController {

	/** Logger SLF4J desta classe (infraestrutura de logs da bancada). */
	private static final Logger LOG = ProcessLog.get(MeterController.class);

	private MeterSocketThread meterSocketThread;

	private boolean readStatus = false;

	private MeterBean meterBean;

	private boolean isTransmiting = false;

	private boolean isInCalibMod = false;

	private boolean isStandByMode = false;

	private boolean isNormalMode = false;

	private boolean isPostCalibMode = false;

	private boolean isVerifMode = false;
	
	public boolean isReading = false;

	// private boolean isQa = false;

	// private boolean isAvailableSN = true;

	private boolean isReproved = false;

	private BooleanProperty isCommDownProperty; // COMM

	// private boolean isAgcTrimmed = false; // TRIM
	//
	// private boolean isZeroFlowApproved = false; // DESV
	//
	// private boolean isConstantsCalculated = false; // CALC
	//
	// private boolean isConstantsDownloaded = false; // DOWN

	// private boolean isVerifAproved = false; // VERIF

	// private boolean isWmBusConfigured = false; // RFCONF
	//
	// private boolean isMeterSystemDateConfigured = false; // SYSDATE
	//
	// private boolean isMeterReplaceDateConfigured = false; // BATDATE

	private Thread readThread;

	private MeterSocketComm socketComm;

	private String matFileName;

	//
	private String serialMeter;

	private String serialWmBus;

	//
	private MeterModel meter;

	private MeterService meterService;

	private CalibConstantsService calibConstService;

	private String ip;

	private int port;

	private ArrayList<MeterDataModel> meterData;

	private ArrayList<MeterDataModel> meterDataZeroFlow;

	private ArrayList<MeterDataModel> meterDataVerif;

	private ArrayList<CalibConstantsModel> calibConstants;

	private CalibrationService calibService;

	// private boolean hasReceivedInitCalibParam = false;

	private ByteBuffer response;

	private ArrayList<VerificationErrorModel> verifErrors;

	private boolean commInitiated = false;

	private boolean isCommVerified = false;

	private boolean isFwVersionVerified = false;

	private VerificationErrorService verifErrorService;

	private BooleanProperty dataSentCorrectlyProperty;

	private BooleanProperty checkingDataProperty;

	private BooleanProperty isThreadWorking;

	private byte[] meterUfoId = new byte[4];

	private boolean isCalculatedEstimatedFixedConstants = false;

	private ArrayList<CalibConstantsModel> calibConstsAux;

	// private byte[] fwVersion = new byte[6];
	private byte[] fwVersion = new byte[8];

	private FlowRateMeanStdService flowRateMeanStdService;
	
	private LogChangeProcessBatchService logChangeProcessBatchService;
	
	private CriticalPointService criticalPointService;
	
	private DistancePointsService distancePointsService;
	
	/**
	 * Creates a object for class MeterController.java
	 */
	public MeterController() {
		meter = new MeterModel();
		commonInit();
	}

	/**
	 * Creates a object for class MeterController.java
	 */
	public MeterController(MeterModel meter) {
		this.meter = meter;
		commonInit();
	}

	private ChangeListener<Boolean> listenerCommDownProperty = new ChangeListener<Boolean>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			// if (!isVerifAproved) {
			if (!meter.isApprovedVerification()) {
				if (meterBean != null) {
					meterBean.setLedToRed();
					meterBean.setStatusLabelPos("COMM");
				}
				meter.setCommDown(true);
			}
			isCommVerified = false;
			isFwVersionVerified = false;
		}
	};

	public void commonInit() {
		calibConstants = new ArrayList<>();
		calibService = new CalibrationService();
		meterService = new MeterService();
		verifErrorService = new VerificationErrorService();
		calibConstService = new CalibConstantsService();
		// meterCommStateStack = new ArrayList<>();
		dataSentCorrectlyProperty = new SimpleBooleanProperty();
		checkingDataProperty = new SimpleBooleanProperty();
		isThreadWorking = new SimpleBooleanProperty();
		isCommDownProperty = new SimpleBooleanProperty(false);
		ip = "";
		port = 0;
		isCommDownProperty.addListener(listenerCommDownProperty);
		flowRateMeanStdService = new FlowRateMeanStdService();
		logChangeProcessBatchService = new LogChangeProcessBatchService();
		criticalPointService = new CriticalPointService();
		distancePointsService = new DistancePointsService();
	}

	/**
	 * 
	 */
	public void resetStructure() {
		isTransmiting = false;
		isInCalibMod = false;
		isStandByMode = false;
		isNormalMode = false;
		isReproved = false;

		// isAgcTrimmed = false; // TRIM
		// isZeroFlowApproved = false; // DESV
		// isConstantsCalculated = false; // CALC
		// isConstantsDownloaded = false; // DOWN
		// isVerifAproved = false; // VERIF
		// isWmBusConfigured = false; // RFCONF
		// isMeterSystemDateConfigured = false; // MDATE

		// hasReceivedInitCalibParam = false;

		if (meterData != null)
			meterData.clear();

		if (meterDataZeroFlow != null)
			meterDataZeroFlow.clear();

		if (meterDataVerif != null)
			meterDataVerif.clear();

		if (calibConstants != null)
			calibConstants.clear();

		if (verifErrors != null)
			verifErrors.clear();
	}

	/**
	 * Function returns the value of attribute matFileName
	 * 
	 * @return the matFileName
	 */
	public String getMatFileName() {
		return matFileName;
	}

	/**
	 * Function sets the value for attribute matFileName
	 * 
	 * @param matFileName
	 *            the matFileName to set
	 */
	public void setMatFileName(String matFileName) {
		this.matFileName = matFileName;
	}

	/**
	 * Function returns the value of attribute serialMeter
	 * 
	 * @return the serialMeter
	 */
	public String getSerialMeter() {
		return serialMeter;
	}

	/**
	 * Function sets the value for attribute serialMeter
	 * 
	 * @param serialMeter
	 *            the serialMeter to set
	 */
	public void setSerialMeter(String serialMeter) {
		this.serialMeter = serialMeter;
	}

	/**
	 * Function returns the value of attribute serialWmBus
	 * 
	 * @return the serialWmBus
	 */
	public String getSerialWmBus() {
		return serialWmBus;
	}

	/**
	 * Function sets the value for attribute serialWmBus
	 * 
	 * @param serialWmBus
	 *            the serialWmBus to set
	 */
	public void setSerialWmBus(String serialWmBus) {
		this.serialWmBus = serialWmBus;
	}

	/**
	 * Function returns the value of attribute meter
	 * 
	 * @return the meter
	 */
	public MeterModel getMeter() {
		return meter;
	}

	/**
	 * Function sets the value for attribute meter
	 * 
	 * @param meter
	 *            the meter to set
	 */
	public void setMeter(MeterModel meter) {
		this.meter = meter;
	}

	/**
	 * Function returns the value of attribute meterData
	 * 
	 * @return the meterData
	 */
	public ArrayList<MeterDataModel> getMeterData() {
		return meterData;
	}

	/**
	 * Function sets the value for attribute meterData
	 * 
	 * @param meterData
	 *            the meterData to set
	 */
	public void setMeterData(ArrayList<MeterDataModel> meterData) {
		this.meterData = meterData;
	}

	/**
	 * Function returns the value of attribute meterDataZeroFlow
	 * 
	 * @return the meterDataZeroFlow
	 */
	public ArrayList<MeterDataModel> getMeterDataZeroFlow() {
		return meterDataZeroFlow;
	}

	/**
	 * Function sets the value for attribute meterDataZeroFlow
	 * 
	 * @param meterDataZeroFlow
	 *            the meterDataZeroFlow to set
	 */
	public void setMeterDataZeroFlow(ArrayList<MeterDataModel> meterDataZeroFlow) {
		this.meterDataZeroFlow = meterDataZeroFlow;
	}

	/**
	 * Function returns the value of attribute calibConstants
	 * 
	 * @return the calibConstants
	 */
	public ArrayList<CalibConstantsModel> getCalibConstants() {
		return calibConstants;
	}

	/**
	 * Function sets the value for attribute calibConstants
	 * 
	 * @param calibConstants
	 *            the calibConstants to set
	 */
	public void setCalibConstants(ArrayList<CalibConstantsModel> calibConstants) {
		this.calibConstants = calibConstants;
	}

	// /**
	// * Function returns the value of attribute hasReceivedInitCalibParam
	// *
	// * @return the hasReceivedInitCalibParam
	// */
	// public boolean hasReceivedInitCalibParam() {
	// return hasReceivedInitCalibParam;
	// }
	//
	// /**
	// * Function sets the value for attribute hasReceivedInitCalibParam
	// *
	// * @param hasReceivedInitCalibParam
	// * the hasReceivedInitCalibParam to set
	// */
	// public void setHasReceivedInitCalibParam(boolean hasReceivedInitCalibParam) {
	// this.hasReceivedInitCalibParam = hasReceivedInitCalibParam;
	// }

	/**
	 * Function returns the value of attribute isInCalibMod
	 * 
	 * @return the isInCalibMod
	 */
	public boolean isInCalibMod() {
		return isInCalibMod;
	}

	/**
	 * Function sets the value for attribute isInCalibMod
	 * 
	 * @param isInCalibMod
	 *            the isInCalibMod to set
	 */
	public void setInCalibMod(boolean isInCalibMod) {
		this.isInCalibMod = isInCalibMod;
	}

	/**
	 * Function returns the value of attribute isAgcTrimmed
	 * 
	 * @return the isAgcTrimmed
	 */
	public boolean isAgcTrimmed() {
		// return isAgcTrimmed;
		return meter.isApprovedAgcTrim();
	}

	/**
	 * Function sets the value for attribute isAgcTrimmed
	 * 
	 * @param isAgcTrimmed
	 *            the isAgcTrimmed to set
	 */
	public void setAgcTrimmed(boolean isAgcTrimmed) {
		// this.isAgcTrimmed = isAgcTrimmed;
		meter.setApprovedAgcTrim(isAgcTrimmed);
	}

	/**
	 * Function returns the value of attribute isCommDownProperty
	 * 
	 * @return the isCommDownProperty
	 */
	public BooleanProperty getIsCommDownProperty() {
		return isCommDownProperty;
	}

	/**
	 * Function sets the value for attribute isCommDownProperty
	 * 
	 * @param isCommDownProperty
	 *            the isCommDownProperty to set
	 */
	public void setIsCommDownProperty(BooleanProperty isCommDownProperty) {
		this.isCommDownProperty = isCommDownProperty;
	}

	public boolean isCommDown() {
		return isCommDownProperty.getValue();
	}

	public boolean isDownloadedInitContants() {
		return meter.isDownloadedInitContants();
	}

	public void setDownloadedInitContants(boolean isDownloadedInitContants) {
		meter.setDownloadedInitContants(isDownloadedInitContants);
	}

	/**
	 * Function returns the value of attribute isVerifAproved
	 * 
	 * @return the isVerifAproved
	 */
	public boolean isVerifAproved() {
		// return isVerifAproved;
		return meter.isApprovedVerification();
	}

	/**
	 * Function sets the value for attribute isVerifAproved
	 * 
	 * @param isVerifAproved
	 *            the isVerifAproved to set
	 */
	public void setVerifAproved(boolean isVerifAproved) {
		// this.isVerifAproved = isVerifAproved;
		meter.setApprovedVerification(isVerifAproved);
	}

	/**
	 * Function returns the value of attribute isWmBusConfigured
	 * 
	 * @return the isWmBusConfigured
	 */
	public boolean isWmBusConfigured() {
		// return isWmBusConfigured;
		return meter.isWmBusConfigured();
	}

	/**
	 * Function sets the value for attribute isWmBusConfigured
	 * 
	 * @param isWmBusConfigured
	 *            the isWmBusConfigured to set
	 */
	public void setWmBusConfigured(boolean isWmBusConfigured) {
		// this.isWmBusConfigured = isWmBusConfigured;
		meter.setWmBusConfigured(isWmBusConfigured);
	}

	/**
	 * Function returns the value of attribute isMeterDateConfigured
	 * 
	 * @return the isMeterDateConfigured
	 */
	public boolean isMeterSystemDateConfigured() {
		// return isMeterSystemDateConfigured;
		return meter.isMeterSystemDateConfigured();
	}

	/**
	 * Function sets the value for attribute isMeterDateConfigured
	 * 
	 * @param isMeterDateConfigured
	 *            the isMeterDateConfigured to set
	 */
	public void setMeterSystemDateConfigured(boolean isMeterSystemDateConfigured) {
		// this.isMeterSystemDateConfigured = isMeterDateConfigured;
		meter.setMeterSystemDateConfigured(isMeterSystemDateConfigured);
	}

	/**
	 * Function returns the value of attribute isMeterReplaceDateConfigured
	 * 
	 * @return the isMeterReplaceDateConfigured
	 */
	public boolean isMeterReplaceDateConfigured() {
		// return isMeterReplaceDateConfigured;
		return meter.isMeterReplaceDateConfigured();
	}

	/**
	 * Function sets the value for attribute isMeterReplaceDateConfigured
	 * 
	 * @param isMeterReplaceDateConfigured
	 *            the isMeterReplaceDateConfigured to set
	 */
	public void setMeterReplaceDateConfigured(boolean isMeterReplaceDateConfigured) {
		// this.isMeterReplaceDateConfigured = isMeterReplaceDateConfigured;
		meter.setMeterReplaceDateConfigured(isMeterReplaceDateConfigured);
	}

	/**
	 * Function returns the value of attribute isNormalMode
	 * 
	 * @return the isNormalMode
	 */
	public boolean isNormalMode() {
		return isNormalMode;
	}

	/**
	 * Function sets the value for attribute isNormalMode
	 * 
	 * @param isNormalMode
	 *            the isNormalMode to set
	 */
	public void setNormalMode(boolean isNormalMode) {
		this.isNormalMode = isNormalMode;
	}

	/**
	 * Function returns the value of attribute isPostCalibMode
	 * 
	 * @return the isPostCalibMode
	 */
	public boolean isPostCalibMode() {
		return isPostCalibMode;
	}

	/**
	 * Function sets the value for attribute isPostCalibMode
	 * 
	 * @param isPostCalibMode
	 *            the isPostCalibMode to set
	 */
	public void setPostCalibMode(boolean isPostCalibMode) {
		this.isPostCalibMode = isPostCalibMode;
	}

	/**
	 * Function returns the value of attribute isVerifMode
	 * 
	 * @return the isVerifMode
	 */
	public boolean isVerifMode() {
		return isVerifMode;
	}

	/**
	 * Function sets the value for attribute isVerifMode
	 * 
	 * @param isVerifMode
	 *            the isVerifMode to set
	 */
	public void setVerifMode(boolean isVerifMode) {
		this.isVerifMode = isVerifMode;
	}

	/**
	 * Function returns the value of attribute isQa
	 * 
	 * @return the isQa
	 */
	public boolean isQa() {
		// return isQa;
		return meter.isQa();
	}

	/**
	 * Function sets the value for attribute isQa
	 * 
	 * @param isQa
	 *            the isQa to set
	 */
	public void setQa(boolean isQa) {
		// this.isQa = isQa;
		meter.setQa(isQa);
	}

	// /**
	// * Function returns the value of attribute isMeterSystemDateConfigured
	// * @return the isMeterSystemDateConfigured
	// */
	// public boolean isMeterSystemDateConfigured() {
	// return isMeterSystemDateConfigured;
	// }

	/**
	 * Function returns the value of attribute meterBean
	 * 
	 * @return the meterBean
	 */
	public MeterBean getMeterBean() {
		return meterBean;
	}

	/**
	 * Function sets the value for attribute meterBean
	 * 
	 * @param meterBean
	 *            the meterBean to set
	 */
	public void setMeterBean(MeterBean meterBean) {
		this.meterBean = meterBean;
	}

	/**
	 * Function returns the value of attribute isZeroFlowApproved
	 * 
	 * @return the isZeroFlowApproved
	 */
	public boolean isCalculatedZeroFlow() {
		// public boolean isZeroFlowApproved() {
		// return isZeroFlowApproved;
		// return meter.isApprovedZeroFlow();
		return meter.isCalculatedZeroFlow();
	}

	/**
	 * @return the isDeviationZeroFlow
	 */
	public boolean isDeviationZeroFlow() {
		return meter.isDeviationZeroFlow();
	}

	/**
	 * @param isDeviationZeroFlow
	 *            the isDeviationZeroFlow to set
	 */
	public void setDeviationZeroFlow(boolean isDeviationZeroFlow) {
		meter.setDeviationZeroFlow(isDeviationZeroFlow);
	}

	/**
	 * Function sets the value for attribute isZeroFlowApproved
	 * 
	 * @param isZeroFlowApproved
	 *            the isZeroFlowApproved to set
	 */
	public void setCalculatedZeroFlow(boolean isCalculatedZeroFlow) {
		// public void setApprovedZeroFlow(boolean isZeroFlowApproved) {
		// this.isZeroFlowApproved = isZeroFlowApproved;
		// meter.setApprovedZeroFlow(isZeroFlowApproved);
		meter.setCalculatedZeroFlow(isCalculatedZeroFlow);
	}

	public boolean isDownloadedZeroFlow() {
		return meter.isDownloadedZeroFlow();
	}

	public void setDownloadedZeroFlow(boolean isDownloadedZeroFlow) {
		meter.setDownloadedZeroFlow(isDownloadedZeroFlow);
	}

	//
	public boolean isCalculatedErrors() {
		return meter.isCalculatedErrors();
	}

	public void setCalculatedErrors(boolean isCalculatedErrors) {
		meter.setCalculatedErrors(isCalculatedErrors);
	}

	/**
	 * Function returns the value of attribute isConstantsDownloaded
	 * 
	 * @return the isConstantsDownloaded
	 */
	public boolean isDownloadedConstants() {
		// public boolean isConstantsDownloaded() {
		// return isConstantsDownloaded;
		// return meter.isConstantsDownloaded();
		return meter.isDownloadedConstants();
	}

	/**
	 * Function sets the value for attribute isConstantsDownloaded
	 * 
	 * @param isConstantsDownloaded
	 *            the isConstantsDownloaded to set
	 */
	public void setDownloadedConstants(boolean isDownloadedConstants) {
		// public void setConstantsDownloaded(boolean isConstantsDownloaded) {
		// this.isConstantsDownloaded = isConstantsDownloaded;
		// meter.setConstantsDownloaded(isConstantsDownloaded);
		meter.setDownloadedConstants(isDownloadedConstants);
	}

	public BooleanProperty getPropertyDataSentCorrectly() {
		return dataSentCorrectlyProperty;
	}

	public void setDataSentCorrectly(boolean value) {
		this.dataSentCorrectlyProperty.set(value);
	}

	public boolean isDataSentCorrectly() {
		return dataSentCorrectlyProperty.get();
	}

	public BooleanProperty getCheckingDataProperty() {
		return checkingDataProperty;
	}

	public void setCheckingData(boolean value) {
		this.checkingDataProperty.set(value);
	}

	public boolean isCheckingData() {
		return checkingDataProperty.get();
	}

	public BooleanProperty getIsThreadWorking() {
		return isThreadWorking;
	}

	public void setIsThreadWorking(boolean value) {
		this.isThreadWorking.set(value);
	}

	public boolean isThreadWorking() {
		return isThreadWorking.get();
	}

	/**
	 * Function returns the value of attribute isStandByMode
	 * 
	 * @return the isStandByMode
	 */
	public boolean isStandByMode() {
		return isStandByMode;
	}

	/**
	 * Function sets the value for attribute isStandByMode
	 * 
	 * @param isStandByMode
	 *            the isStandByMode to set
	 */
	public void setStandByMode(boolean isStandByMode) {
		this.isStandByMode = isStandByMode;
	}

	/**
	 * Function returns the value of attribute isConstantsCalculated
	 * 
	 * @return the isConstantsCalculated
	 */
	public boolean isCalculatedEstimatedFixedConstants() {
		return isCalculatedEstimatedFixedConstants;
	}

	/**
	 * Function sets the value for attribute isConstantsCalculated
	 * 
	 * @param isConstantsCalculated
	 *            the isConstantsCalculated to set
	 */
	public void setCalculatedEstimatedFixedConstants(boolean isCalculatedFixedConstants) {
		this.isCalculatedEstimatedFixedConstants = isCalculatedFixedConstants;
	}

	/**
	 * Function returns the value of attribute isConstantsCalculated
	 * 
	 * @return the isConstantsCalculated
	 */
	public boolean isCalculatedConstants() {
		// return isConstantsCalculated;
		return meter.isCalculatedConstants();
	}

	/**
	 * Function sets the value for attribute isConstantsCalculated
	 * 
	 * @param isConstantsCalculated
	 *            the isConstantsCalculated to set
	 */
	public void setCalculatedConstants(boolean isConstantsCalculated) {
		// this.isConstantsCalculated = isConstantsCalculated;
		meter.setCalculatedConstants(isConstantsCalculated);
	}

	/**
	 * This method starts reading a meter throu the meterSocketThread istantiated for it. Even if the meter is been read by the thread. It still needs to be controlled to save the data. To save data
	 * please check method MeterController.saveMeterData();
	 * 
	 * @return true if started read meter
	 */
	public boolean startReading() {
		boolean retorno = false;
		marcarMedidorNoLog();
		LOG.debug("ENTRADA startReading(): socketComm={} | commInitiated={}", (socketComm != null), commInitiated);
		if (socketComm != null) {
			if (commInitiated) {
				if (socketComm.isConnected()) {
					// socketThread = new MeterSocketThread(socketComm);
					// Socket thread is instantiated when opening the comm
					if (meterSocketThread != null) {
						readThread = new Thread(meterSocketThread);
						readThread.setName("MeterThread-" + this.meter.getConversor().getIp());
						readThread.setPriority(Thread.MAX_PRIORITY);
						LOG.info("Iniciando a thread de leitura '{}' (prioridade MAX) para o medidor no IP {}",
								readThread.getName(), this.meter.getConversor().getIp());
						readThread.start();
						meterSocketThread.getPropertyDataSentCorrectly().bindBidirectional(dataSentCorrectlyProperty);
						meterSocketThread.getCheckingDataProperty().bindBidirectional(checkingDataProperty);
						meterSocketThread.getIsWorkingPorperty().bindBidirectional(isThreadWorking);
						meterSocketThread.getTimeoutExceededProperty().bindBidirectional(isCommDownProperty);

						retorno = true;
					} else {
						retorno = false;
						System.err.println("ERROR: Could not start reading meter position: " + meter.getConversor().getName() + ". Socket THREAD is null at MeterController.startReading() ");
						LOG.error("FALHA em startReading(): a thread de socket esta NULA para o medidor {}", meter.getConversor().getName());
					}
				} else {
					retorno = false;
					System.err.println("ERROR: Could not start reading meter " + meter.getConversor().getName() + ". Socket not conected at MeterController.startReading()");
					LOG.error("FALHA em startReading(): socket NAO conectado para o medidor {} (IP {})",
							meter.getConversor().getName(), meter.getConversor().getIp());
				}
			} else {
				retorno = false;
				System.err.println("ERROR: Could not start reading meter " + meter.getConversor().getName() + ". COMM not initiated. at MeterController.startReading()");
				LOG.error("FALHA em startReading(): comunicacao NAO iniciada para o medidor {} (IP {})",
						meter.getConversor().getName(), meter.getConversor().getIp());
			}
		} else {
			LOG.error("FALHA em startReading(): socketComm esta NULO");
		}
		readStatus = retorno;
		LOG.debug("SAIDA startReading(): retorno={}", retorno);
		return retorno;
	}

	// /**
	// *
	// */
	// public void stopReading() {
	// if (meterSocketThread != null) {
	// meterSocketThread.stop();
	// readThread.interrupt();
	// }
	// }
	//
	// public void stopReading() {
	//
	// }

	// /**
	// *
	// */
	// public void clear() {
	// meterSocketThread.stop();
	// readThread.interrupt();
	// meterSocketThread = null;
	// readThread = null;
	// meter = null;
	// meterBean = null;
	//
	// calibConstants = null;
	// calibService = null;
	// meterService = null;
	// verifErrorService = null;
	// // meterCommStateStack = new ArrayList<>();
	//// dataSentCorrectlyProperty = null;
	//// checkingDataProperty = null;
	//// isThreadWorking = null;
	// isCommDownProperty.removeListener(listenerCommDownProperty);
	// isCommDownProperty = null;
	// ip = "";
	// port = 0;
	// }

	/**
	 * Sets the flag for to save data in METERSOCKETTHREAD
	 */
	public void saveMeterData() {
		meterSocketThread.setSaveData(true);
	}

	public void stopSaveMeterData() {
		meterSocketThread.setSaveData(false);
	}

	/**
	 * Sets the flag for to save data in METERSOCKETTHREAD
	 */
	public void saveMeterVerifData() {
		meterSocketThread.setSaveData(true);
	}

	public void stopSaveMeterVerifData() {
		meterSocketThread.setSaveData(false);
	}

	/**
	 * Sends the calibration data back to the meter
	 */
	public boolean sendNonCalcCalibrationData() {
		boolean retorno = false;
		if (calibConstants != null) {
			if (!calibConstants.isEmpty()) {
				if (sendCalibDataToMeter(this.calibConstants, this.meter)) {
					retorno = true;
				} else {
					retorno = false;
				}
			} else {
				System.err.println("ERROR: Could not SEND calibration data to meter at position " + meter.getConversor().getName() + ". MeterController.calibConstants is EMPTY");
			}
		} else {
			System.err.println("ERROR: Could not SEND calibration data to meter at position " + meter.getConversor().getName() + ". COMM not initiated. MeterController.calibConstants is NULL");
		}

		return retorno;
	}

	/**
	 * Sends the calibration data back to the meter
	 */
	public boolean sendCalibrationData() {
		boolean retorno = false;
		if (calibConstants != null) {
			if (!calibConstants.isEmpty()) {

				orderCrescentByFlowRate(this.calibConstants);

				if (this.sendCalibDataToMeter(this.calibConstants, this.meter)) {
					System.err.println(calibConstants.size() + " Constants were sent to meter at position " + meter.getConversor().getName());
					retorno = true;
				} else {
					retorno = false;
				}
			} else {
				System.err.println("ERROR: Could not SEND calibration data to meter at position " + meter.getConversor().getName() + ". MeterController.calibConstants is EMPTY");
			}
		} else {
			System.err.println("ERROR: Could not SEND calibration data to meter at position " + meter.getConversor().getName() + ". COMM not initiated. MeterController.calibConstants is NULL");
		}

		return retorno;
	}

	/**
	 * Sends the wM-Bus configuration to the meter
	 * 
	 * @param serial
	 * @param criptoKey
	 * @param transInterval
	 * @return true if data was successfully sent
	 */
	public boolean sendWmBusConfig(String serial, String criptoKey, short transInterval) {
		boolean retorno = false;

		// if (this.fwVersionLong() >= fwVersionLong(new byte[] { 2, 0, 0, 0, 0, 0 })) {
		if (this.fwVersionLong() >= fwVersionLong(ViewStatesUtil.arrayMinVersion)) {
			ByteBuffer sn = ByteBuffer.allocate(16);
			sn = ByteBuffer.wrap(charLiteralToByteHexArray(serial));

			ByteBuffer key = ByteBuffer.allocate(16);
			key = ByteBuffer.wrap(criptoKey.getBytes());

			response = ByteBuffer.allocate(sn.capacity() + key.capacity() + 2);
			response.order(ByteOrder.LITTLE_ENDIAN);

			response.put(sn);
			response.put(key);

			// //Placing interval 2 bytes LSB
			byte intervalByte = (byte) (transInterval & 0xFF);
			response.put(intervalByte);
			intervalByte = (byte) (transInterval >>> 8);
			response.put(intervalByte);

			// Placing interval 2 bytes LSB
			// byte intervalByte1 = (byte) (transInterval & 0xFF);
			// byte intervalByte2 = (byte) (transInterval >>> 8) ;
			// response.put(intervalByte2);
			// response.put(intervalByte1);

			meterSocketThread.setDataToSend(response);
			meterSocketThread.setDataCmdType(MeterCmdEnum.CMD_WRITE_WMBUS_CONFIG);
			meterSocketThread.setSendDataToMeter(true);

			setCheckingData(true);
			// while (isCheckingData());
			while (isCheckingData() && !isCommDown());
			// while (isCheckingData()){
			// Thread.currentThread().yield();
			// }

			if (isDataSentCorrectly()) {
				retorno = true;
			} else {
				retorno = false;
			}
		} else {
			// case version is prior to 2.0.0 set return to True
			retorno = true;
			System.err.println("Did not send WM-BUS CONFIG to Meter in position " + this.meter.getConversor().getIp() + " firmware version: " + this.fwVersionLong());
		}
		return retorno;
	}

	/**
	 * Sends initial data to the meter to assure that it will calculate correct compensations with flow
	 * 
	 * @param meanTemp
	 * @param meter
	 */
	public boolean sendInitCalibrationData(double meanTemp, MeterModel meter) {

		boolean retorno = false;

		if (calibConstsAux == null) {
			calibConstsAux = new ArrayList<CalibConstantsModel>();
			calibConstsAux = calculateInitConstants(meanTemp, meter);
		}

		if (this.sendCalibDataToMeter(calibConstsAux, meter)) {
			retorno = true;
		} else {
			retorno = false;
		}
		return retorno;
	}

	public ArrayList<CalibConstantsModel> calculateInitConstants(double meanTemp, MeterModel meter) {

		ArrayList<CalibConstantsModel> calibConstsAux = new ArrayList<CalibConstantsModel>();

		double initLowCutOffFlow = meter.getMeterTypeModel().getLowCutoff() * 0.99;
		double initLowCutOffReynolds = calibService.flowToReynolds(initLowCutOffFlow, meanTemp, meter);
		double intiLowCutOffK = 0;

		CalibConstantsModel initLowCutOff = new CalibConstantsModel();
		initLowCutOff.setFlowRate(initLowCutOffFlow);
		initLowCutOff.setAvgFlowRate(initLowCutOffFlow);
		initLowCutOff.setReynolds(initLowCutOffReynolds);
		initLowCutOff.setConstantK(intiLowCutOffK);

		// ----------
		double lowCutOffFlow = meter.getMeterTypeModel().getLowCutoff();
		double lowCutOffReynolds = calibService.flowToReynolds(lowCutOffFlow, meanTemp, meter);
		double lowCutOffK = 1;

		CalibConstantsModel lowCutOff = new CalibConstantsModel();
		lowCutOff.setFlowRate(lowCutOffFlow);
		lowCutOff.setAvgFlowRate(lowCutOffFlow);
		lowCutOff.setReynolds(lowCutOffReynolds);
		lowCutOff.setConstantK(lowCutOffK);
		// ----------

		double stdReynoldsQ1 = calibService.flowToReynolds(meter.getMeterTypeModel().getMinFlowrate().getFlowRate(), meanTemp, meter);
		double KQ1 = 1;

		CalibConstantsModel q1 = new CalibConstantsModel();
		q1.setFlowRate(meter.getMeterTypeModel().getMinFlowrate().getFlowRate());
		q1.setAvgFlowRate(meter.getMeterTypeModel().getMinFlowrate().getFlowRate());
		q1.setReynolds(stdReynoldsQ1);
		q1.setConstantK(KQ1);
		// ----------

		double stdReynoldsQ2 = calibService.flowToReynolds(meter.getMeterTypeModel().getTransitionFlowrate().getFlowRate(), meanTemp, meter);
		double KQ2 = 1;

		CalibConstantsModel q2 = new CalibConstantsModel();
		q2.setFlowRate(meter.getMeterTypeModel().getTransitionFlowrate().getFlowRate());
		q2.setAvgFlowRate(meter.getMeterTypeModel().getTransitionFlowrate().getFlowRate());
		q2.setReynolds(stdReynoldsQ2);
		q2.setConstantK(KQ2);
		// ----------

		double stdReynoldsQ3 = calibService.flowToReynolds(meter.getMeterTypeModel().getNominalFlowrate().getFlowRate(), meanTemp, meter);
		double KQ3 = 1;

		CalibConstantsModel q3 = new CalibConstantsModel();
		q3.setFlowRate(meter.getMeterTypeModel().getNominalFlowrate().getFlowRate());
		q3.setAvgFlowRate(meter.getMeterTypeModel().getNominalFlowrate().getFlowRate());
		q3.setReynolds(stdReynoldsQ3);
		q3.setConstantK(KQ3);
		// ----------

		double stdReynoldsQ4 = calibService.flowToReynolds(meter.getMeterTypeModel().getMaxFlowrate().getFlowRate(), meanTemp, meter);
		double KQ4 = 1;

		CalibConstantsModel q4 = new CalibConstantsModel();
		q4.setFlowRate(meter.getMeterTypeModel().getMaxFlowrate().getFlowRate());
		q4.setAvgFlowRate(meter.getMeterTypeModel().getMaxFlowrate().getFlowRate());
		q4.setReynolds(stdReynoldsQ4);
		q4.setConstantK(KQ4);
		// ----------

		double endHighCutOffFlow = meter.getMeterTypeModel().getHighCutoff() * 1.01;
		double endHighCutOffReynolds = calibService.flowToReynolds(endHighCutOffFlow, meanTemp, meter);
		double endHighCutOffK = 0;

		CalibConstantsModel endHighCutOff = new CalibConstantsModel();
		endHighCutOff.setFlowRate(endHighCutOffFlow);
		endHighCutOff.setAvgFlowRate(endHighCutOffFlow);
		endHighCutOff.setReynolds(endHighCutOffReynolds);
		endHighCutOff.setConstantK(endHighCutOffK);
		// -----------

		double highCutOffFlow = meter.getMeterTypeModel().getHighCutoff();
		double highCutOffReynolds = calibService.flowToReynolds(highCutOffFlow, meanTemp, meter);
		double highCutOffK = 1;

		CalibConstantsModel highCutOff = new CalibConstantsModel();
		highCutOff.setFlowRate(highCutOffFlow);
		highCutOff.setAvgFlowRate(highCutOffFlow);
		highCutOff.setReynolds(highCutOffReynolds);
		highCutOff.setConstantK(highCutOffK);
		// ------------

		calibConstsAux.add(initLowCutOff);
		calibConstsAux.add(lowCutOff);
		calibConstsAux.add(q1);
		calibConstsAux.add(q2);
		calibConstsAux.add(q3);
		calibConstsAux.add(q4);
		calibConstsAux.add(highCutOff);
		calibConstsAux.add(endHighCutOff);

		return calibConstsAux;
	}

	/**
	 * Sends initial data to the meter to assure that it will calculate correct compensations with flow
	 * 
	 * @param meanTemp
	 * @param meter
	 */
	public boolean sendInitFixedCalibrationData(double meanTemp, MeterModel meter) {

		boolean retorno = false;

		ArrayList<CalibConstantsModel> calibConstsAux = new ArrayList<CalibConstantsModel>();

		calibConstsAux = getFixedCalibConst2_5m();
		//calibConstsAux = getFixedCalibConst();
		// calibConstsAux = getFixedCalibConst3m();

		if (this.sendCalibDataToMeter(calibConstsAux, meter)) {
			retorno = true;
		} else {
			retorno = false;
		}
		return retorno;
	}

	public ArrayList<CalibConstantsModel> getFixedCalibConst() {
		ArrayList<CalibConstantsModel> calibConstsAux = new ArrayList<CalibConstantsModel>();

		// --------------------------------------------
		// Q1 = 1.485
		CalibConstantsModel calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(1.485);
		calibConstant.setAvgFlowRate(1.485);
		calibConstant.setReynolds(54.3697008743456);
		calibConstant.setConstantK(0);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q2 = 1.5
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(1.5);
		calibConstant.setAvgFlowRate(1.5);
		calibConstant.setReynolds(54.9188897720663);
		calibConstant.setConstantK(1);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q3 = 2.5
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(2.5);
		calibConstant.setAvgFlowRate(2.5);
		calibConstant.setReynolds(91.5314829534439);
		calibConstant.setConstantK(1.027);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q4 = 5
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(5);
		calibConstant.setAvgFlowRate(5);
		calibConstant.setReynolds(183.062965906888);
		// calibConstant.setConstantK(1.0907);
		calibConstant.setConstantK(1.0907 * 0.993);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q5 = 10
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(10);
		calibConstant.setAvgFlowRate(10);
		calibConstant.setReynolds(299.024936639297);
		// calibConstant.setConstantK(1.16427273005354);
		calibConstant.setConstantK(1.16427273005354 * 1.01);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q6 = 16
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(16);
		calibConstant.setAvgFlowRate(16);
		calibConstant.setReynolds(446.504405751102);
		// calibConstant.setConstantK(/* 1.24503028969069 */1.25748059258760);
		calibConstant.setConstantK(1.25748059258760 * 1.004);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q7 = 25
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(25);
		calibConstant.setAvgFlowRate(25);
		calibConstant.setReynolds(682.230987633162);
		// calibConstant.setConstantK(/* 1.32493057405344 */1.34480453266424);
		calibConstant.setConstantK(1.34480453266424 * 1.01 * 0.98);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q8 = 37.5
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(37.5);
		calibConstant.setAvgFlowRate(37.5);
		calibConstant.setReynolds(943.247560185362);
		// calibConstant.setConstantK(/* 1.37749741927099 *//* 1.39127239346370 *//* 1.32170877379051 */1.41909784133297);
		// calibConstant.setConstantK(1.41909784133297 * 0.98);
		calibConstant.setConstantK(1.40462304335137 * 0.98);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q9 = 80
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(80);
		calibConstant.setAvgFlowRate(80);
		calibConstant.setReynolds(1825.61394080918);
		// calibConstant.setConstantK(/* 1.46270556091234 *//* 1.47733261652146 *//* 1.47733261652146 */1.50687926885189);
		calibConstant.setConstantK(1.50687926885189 * 0.98);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q10 = 500
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(500);
		calibConstant.setAvgFlowRate(500);
		calibConstant.setReynolds(11812.6539871358);
		calibConstant.setConstantK(1.47764843806934);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q11 = 1000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(1000);
		calibConstant.setAvgFlowRate(1000);
		calibConstant.setReynolds(23653.4128183095);
		calibConstant.setConstantK(1.47083468665536);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q12 = 2000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(2000);
		calibConstant.setAvgFlowRate(2000);
		calibConstant.setReynolds(46920.783398296);
		calibConstant.setConstantK(1.48419787709557);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q13 = 2500
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(2500);
		calibConstant.setAvgFlowRate(2500);
		calibConstant.setReynolds(58634.1534349704);
		// calibConstant.setConstantK(/* 1.48329835182043 */1.50554782709774);
		calibConstant.setConstantK(1.50554782709774 * 0.995);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q14 = 3000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(3000);
		calibConstant.setAvgFlowRate(3000);
		calibConstant.setReynolds(70286.0788654379);
		calibConstant.setConstantK(/* 1.4835981503416 */1.50585212259672);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q15 = 4000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(4000);
		calibConstant.setAvgFlowRate(4000);
		calibConstant.setReynolds(93930.1193578724);
		calibConstant.setConstantK(1.48244759892732);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q16 = 5000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(5000);
		calibConstant.setAvgFlowRate(5000);
		calibConstant.setReynolds(117320.526688861);
		calibConstant.setConstantK(1.48369261639831);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q17 = 8000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(8000);
		calibConstant.setAvgFlowRate(8000);
		calibConstant.setReynolds(292900.74545102);
		calibConstant.setConstantK(1.48369261639831);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q18 = 8080
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(8080);
		calibConstant.setAvgFlowRate(8080);
		calibConstant.setReynolds(295829.752905531);
		calibConstant.setConstantK(0);
		//
		calibConstsAux.add(calibConstant);
		return calibConstsAux;
	}

	public ArrayList<CalibConstantsModel> getFixedCalibConst10m() {
		ArrayList<CalibConstantsModel> calibConstsAux = new ArrayList<CalibConstantsModel>();

		// --------------------------------------------
		// Q1 = 5
		CalibConstantsModel calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(5);
		calibConstant.setAvgFlowRate(5);
		calibConstant.setReynolds(153.06296590688805);
		calibConstant.setConstantK(0);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q2 = 10
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(10);
		calibConstant.setAvgFlowRate(10);
		calibConstant.setReynolds(182.41147171957707);
		calibConstant.setConstantK(1.1374925501358104);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q3 = 20
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(25);
		calibConstant.setAvgFlowRate(25);
		calibConstant.setReynolds(455.6622);
		calibConstant.setConstantK(1.567556);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q4 = 35
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(35);
		calibConstant.setAvgFlowRate(35);
		calibConstant.setReynolds(628.9366);
		// calibConstant.setConstantK(/* 1.24503028969069 */1.25748059258760);
		calibConstant.setConstantK(1.6744033223634787);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q5 = 50
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(50);
		calibConstant.setAvgFlowRate(50);
		calibConstant.setReynolds(827.2783);
		// calibConstant.setConstantK(/* 1.32493057405344 */1.34480453266424);
		calibConstant.setConstantK(1.769728);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q6 = 75
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(75);
		calibConstant.setAvgFlowRate(75);
		calibConstant.setReynolds(1144.1756);
		// calibConstant.setConstantK(/* 1.37749741927099 *//* 1.39127239346370 *//* 1.32170877379051 */1.41909784133297);
		// calibConstant.setConstantK(1.41909784133297 * 0.98);
		calibConstant.setConstantK(1.835783);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q7 = 100
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(100);
		calibConstant.setAvgFlowRate(100);
		calibConstant.setReynolds(1616.7150);
		// calibConstant.setConstantK(/* 1.46270556091234 *//* 1.47733261652146 *//* 1.47733261652146 */1.50687926885189);
		calibConstant.setConstantK(1.826236);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q8 = 200
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(200);
		calibConstant.setAvgFlowRate(200);
		calibConstant.setReynolds(3309.0602902252217);
		calibConstant.setConstantK(1.5881564405791977);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q9 = 1000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(1000);
		calibConstant.setAvgFlowRate(1000);
		calibConstant.setReynolds(17607.423439721573);
		calibConstant.setConstantK(1.4946963193767762);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q12 = 2500
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(2500);
		calibConstant.setAvgFlowRate(2500);
		calibConstant.setReynolds(44874.5248);
		calibConstant.setConstantK(1.571813);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q13 = 3500
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(3500);
		calibConstant.setAvgFlowRate(3500);
		calibConstant.setReynolds(56665.47750493575);
		// calibConstant.setConstantK(/* 1.48329835182043 */1.50554782709774);
		calibConstant.setConstantK(1.571813);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q14 = 5000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(5000);
		calibConstant.setAvgFlowRate(5000);
		calibConstant.setReynolds(92439.9790);
		calibConstant.setConstantK(1.539715);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q15 = 7000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(7000);
		calibConstant.setAvgFlowRate(7000);
		calibConstant.setReynolds(118892.88048394427);
		calibConstant.setConstantK(1.707381316768446);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q16 = 10000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(9900);
		calibConstant.setAvgFlowRate(9900);
		calibConstant.setReynolds(185604.6630);
		calibConstant.setConstantK(1.545325);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q17 = 12000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(12000);
		calibConstant.setAvgFlowRate(12000);
		calibConstant.setReynolds(329513.3386323981);
		calibConstant.setConstantK(1.5681002128839736);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q18 = 12120
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(12120);
		calibConstant.setAvgFlowRate(12120);
		calibConstant.setReynolds(332808.4720187222);
		calibConstant.setConstantK(0);
		//
		calibConstsAux.add(calibConstant);
		return calibConstsAux;
	}

	public ArrayList<CalibConstantsModel> getFixedCalibConst3m() {
		ArrayList<CalibConstantsModel> calibConstsAux = new ArrayList<CalibConstantsModel>();

		// --------------------------------------------
		// Q1 = 1.485
		CalibConstantsModel calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(1.485);
		calibConstant.setAvgFlowRate(1.485);
		calibConstant.setReynolds(54.3697008743456);
		calibConstant.setConstantK(0);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q2 = 1.5
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(1.5);
		calibConstant.setAvgFlowRate(1.5);
		calibConstant.setReynolds(54.9188897720663);
		calibConstant.setConstantK(1);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q3 = 2.5
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(2.5);
		calibConstant.setAvgFlowRate(2.5);
		calibConstant.setReynolds(91.5314829534439);
		calibConstant.setConstantK(1.027);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q4 = 6.25
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(6.25);
		calibConstant.setAvgFlowRate(6.25);
		calibConstant.setReynolds(187.29924141213);
		calibConstant.setConstantK(1.04437340807505);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q5 = 10
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(10);
		calibConstant.setAvgFlowRate(10);
		calibConstant.setReynolds(279.220131846336);
		calibConstant.setConstantK(/* 1.16252675164736 */1.13927621661441);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q6 = 15
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(15);
		calibConstant.setAvgFlowRate(15);
		calibConstant.setReynolds(398.382339563282);
		calibConstant.setConstantK(/* 1.24503028969069 *//* 1.25748059258760 *//* 1.24539658789193 *//* 1.20803469025517 *//* 1.18991416990134 */ /* 1.23751073669739 */1.21276052196344);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q7 = 22.5
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(22.5);
		calibConstant.setAvgFlowRate(22.5);
		calibConstant.setReynolds(593.852224877975);
		calibConstant.setConstantK(
				/* 1.32493057405344 *//* 1.34480453266424 *//* 1.33437245985083 *//* 1.29434128605530 */ /* 1.27492616676447 *//* 1.30679932093358 *//* 1.31986731414292 */1.29346996786006);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q8 = 37.5
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(37.5);
		calibConstant.setAvgFlowRate(37.5);
		calibConstant.setReynolds(923.171169843122);
		calibConstant.setConstantK(/* 1.37749741927099 *//* 1.39127239346370 *//* 1.32170877379051 *//* 1.4148245027925 *//* 1.39360213525061 */1.36573009254560);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q9 = 100
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(100);
		calibConstant.setAvgFlowRate(100);
		calibConstant.setReynolds(2252.70347203278);
		calibConstant.setConstantK(/* 1.46270556091234 *//* 1.47733261652146 *//* 1.47733261652146 *//* 1.49985142848055 */1.46985439991094/* 1.44045731191272 */);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q10 = 250
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(250);
		calibConstant.setAvgFlowRate(250);
		calibConstant.setReynolds(5587.94601293715);
		calibConstant.setConstantK(/* 1.50843001445437 *//* 1.47826141416528 *//* 1.44130487881115 */1.47733750078143);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q11 = 450
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(450);
		calibConstant.setAvgFlowRate(450);
		calibConstant.setReynolds(9868.62466085262);
		calibConstant.setConstantK(/* 1.50182342454749 *//* 1.47178695605654 *//* 1.43499228215513 */ 1.47086708920901);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q12 = 700
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(700);
		calibConstant.setAvgFlowRate(700);
		calibConstant.setReynolds(15769.8357886383);
		calibConstant.setConstantK(/* 1.49496688490255 *//* 1.46506754720450 *//* 1.42844085852439 */1.46415187998750);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q13 = 1325
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(1325);
		calibConstant.setAvgFlowRate(1325);
		calibConstant.setReynolds(29926.8556650444);
		calibConstant.setConstantK(/* 1.49371150512607 *//* 1.50864862017733 *//* 1.46383727502355 */ /* 1.42724134314796 *//* 1.48433099687388 */1.49917430684262);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q14 = 1500
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(1500);
		calibConstant.setAvgFlowRate(1500);
		calibConstant.setReynolds(33841.1419735323);
		calibConstant.setConstantK(/* 1.49473650445916 *//* 1.50968386950375 *//* 1.46484177436998 *//* 1.42822073001073 */1.45678514461094);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q15 = 2000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(2000);
		calibConstant.setAvgFlowRate(2000);
		calibConstant.setReynolds(44915.258525354);
		calibConstant.setConstantK(/* 1.5035398228832 */1.47346902642554/* 1.51857522111203 */);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q16 = 3000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(3000);
		calibConstant.setAvgFlowRate(3000);
		calibConstant.setReynolds(67144.9492182501);
		calibConstant.setConstantK(1.50356095836446);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q17 = 8000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(8000);
		calibConstant.setAvgFlowRate(8000);
		calibConstant.setReynolds(292900.74545102);
		calibConstant.setConstantK(1.503560958364461);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q18 = 8080
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(8080);
		calibConstant.setAvgFlowRate(8080);
		calibConstant.setReynolds(295829.752905531);
		calibConstant.setConstantK(0);
		//
		calibConstsAux.add(calibConstant);
		return calibConstsAux;
	}

	public ArrayList<CalibConstantsModel> getFixedCalibConst2_5m(){
		
		ArrayList<CalibConstantsModel> calibConstsAux = new ArrayList<CalibConstantsModel>();
		
		LogChangeProcessBatchModel logChangeProcessBatchModel = logChangeProcessBatchService.findByBatch(meter.getBatch());
		List<FlowRateMeanStdModel> flowRateMeanStdModelList = flowRateMeanStdService.findAllByBatchAndBatelada(
				logChangeProcessBatchModel.getBatch(), logChangeProcessBatchModel.getBateladaInit(), logChangeProcessBatchModel.getBateladaEnd());
		
		// Q1 = 1.485
		CalibConstantsModel calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(1.485);
		calibConstant.setAvgFlowRate(1.485);
		calibConstant.setReynolds(54.3697008743456);
		calibConstant.setConstantK(0);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q2 = 1.5
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(1.5);
		calibConstant.setAvgFlowRate(1.5);
		calibConstant.setReynolds(54.9188897720663);
		calibConstant.setConstantK(1);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q3 = 2.5
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(2.5);
		calibConstant.setAvgFlowRate(2.5);
		calibConstant.setReynolds(91.5314829534439);
		calibConstant.setConstantK(1.027);
		//
		calibConstsAux.add(calibConstant);
			
		if(flowRateMeanStdModelList.isEmpty()){
			return null;
		}else{
			for(FlowRateMeanStdModel flowRateMeanStdModel: flowRateMeanStdModelList){
				
				calibConstant = new CalibConstantsModel();
				calibConstant.setFlowRate(flowRateMeanStdModel.getFlowRate());
				calibConstant.setAvgFlowRate(flowRateMeanStdModel.getFlowRate());
				calibConstant.setReynolds(flowRateMeanStdModel.getAvgReynolds());
				calibConstant.setConstantK(flowRateMeanStdModel.getAvgConstantK());
				
				calibConstsAux.add(calibConstant);
			}
		}
		
		
		// --------------------------------------------
		// Q17 = 8000
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(8000);
		calibConstant.setAvgFlowRate(8000);
		calibConstant.setReynolds(292900.74545102);
		calibConstant.setConstantK(1.48369261639831);
		//
		calibConstsAux.add(calibConstant);

		// --------------------------------------------
		// Q18 = 8080
		calibConstant = new CalibConstantsModel();
		calibConstant.setFlowRate(8080);
		calibConstant.setAvgFlowRate(8080);
		calibConstant.setReynolds(295829.752905531);
		calibConstant.setConstantK(0);
		//
		calibConstsAux.add(calibConstant);
		
		return calibConstsAux;
		
	}
	
	
	public boolean calculateEstimatedDistanceConst() {
		boolean retorno = false;
		ArrayList<EstimatedDistanceModel> distances25 = getEstimatedDistances5m(25.0);
		ArrayList<EstimatedDistanceModel> distances37 = getEstimatedDistances5m(37.5);
		ArrayList<EstimatedDistanceModel> distances2500 = getEstimatedDistances5m(2500.0);

		CalibConstantsModel const25 = null;
		CalibConstantsModel const37 = null;
		CalibConstantsModel const2500 = null;

		for (CalibConstantsModel calibConst : this.getCalibService().getCalibConstants()) {
			if (calibConst.getFlowRate() == 25.0) {
				const25 = calibConst;
			} else {
				if (calibConst.getFlowRate() == 37.5) {
					const37 = calibConst;
				} else {
					if (calibConst.getFlowRate() == 2500.0) {
						const2500 = calibConst;
					}
				}
			}

		}

		if (const25 != null) {
			if (const37 != null) {
				if (const2500 != null) {

					for (int i = 0; i < distances25.size(); i++) {
						CalibConstantsModel auxConst = new CalibConstantsModel();

						System.err.println("MeterController.calculateEstimatedDistanceConst(): calculating const for " + distances25.get(i).getDesiredFlow());

						auxConst.setConstantK(((const25.getConstantK() - distances25.get(i).getkDistValue()) + (const37.getConstantK() - distances37.get(i).getkDistValue())
								+ (const2500.getConstantK() - distances2500.get(i).getkDistValue())) / 3);

						// auxConst.setReynolds(((const25.getReynolds() - distances25.get(i).getReDistValue()) +
						// (const37.getReynolds() - distances37.get(i).getReDistValue()) +
						// (const2500.getReynolds() - distances2500.get(i).getReDistValue())) / 3);

						auxConst.setReynolds(distances25.get(i).getReDistValue());

						auxConst.setAvgFlowRate(distances25.get(i).getDesiredFlow());
						auxConst.setFlowRate(distances25.get(i).getDesiredFlow());
						auxConst.setAvgTemp(29.0);
						auxConst.setAvgPressure(2);

						if (auxConst.getFlowRate() == 100.0) {
							auxConst.setConstantK(auxConst.getConstantK() * 1.034);
						}
						// Osmenio - 19.09.09
						if (auxConst.getFlowRate() == 250.0) {
							auxConst.setConstantK(auxConst.getConstantK() * 1.004);
						}
						if (auxConst.getFlowRate() == 450.0) {
							auxConst.setConstantK(auxConst.getConstantK() * 1.011);
						}
						if (auxConst.getFlowRate() == 700.0) {
							auxConst.setConstantK(auxConst.getConstantK() * 1.017);
						}
						if (auxConst.getFlowRate() == 1000.0) {
							auxConst.setConstantK(auxConst.getConstantK() * 1.019);
						}
						if (auxConst.getFlowRate() == 1325.0) {
							auxConst.setConstantK(auxConst.getConstantK() * 1.019);
						}
						if (auxConst.getFlowRate() == 1500.0) {
							auxConst.setConstantK(auxConst.getConstantK() * 1.019);
						}
						// if(auxConst.getFlowRate() == 2000.0){
						// auxConst.setConstantK(auxConst.getConstantK()*1.019);
						// }
						// if (auxConst.getFlowRate() == 3500.0) {
						// auxConst.setConstantK(auxConst.getConstantK() * 1.019);
						// }

						calibConstants.add(auxConst);
					}
					retorno = true;
					orderCrescentByFlowRate(calibConstants);
				} else {
					retorno = false;
					System.err.println("MeterController.calculateEstimatedDistanceConst(): ERROR const for 2500.0 is NULL  ");
				}
			} else {
				retorno = false;
				System.err.println("MeterController.calculateEstimatedDistanceConst(): ERROR const for 37.5 is NULL  ");
			}
		} else {
			retorno = false;
			System.err.println("MeterController.calculateEstimatedDistanceConst(): ERROR const for 25.0 is NULL  ");
		}

		return retorno;
	}

	
	public boolean calculateEstimatedDistanceConstIdm2_5m(){

		boolean result = false;

		List<CriticalPointModel> criticalPoints = criticalPointService.findAllByBatch(meter.getBatch());
		List<List<DistancePointsModel>> distancePointsRefList = new ArrayList<>();
		List<CalibConstantsModel> calibConstantList = new ArrayList<>();
		
		for(CriticalPointModel criticalPoint : criticalPoints){			
			List<DistancePointsModel> distancePoints = distancePointsService.findAllByBatchAndFlowRef(meter.getBatch(), criticalPoint.getFlowRate().getFlowRate());		
			distancePointsRefList.add(distancePoints);
			for (CalibConstantsModel calibConst : this.getCalibService().getCalibConstants()) {
				if (calibConst.getFlowRate() == criticalPoint.getFlowRate().getFlowRate()) {
					calibConstantList.add(calibConst);
				}
			}
		}

		
		double k = 0;
		double reynolds = 0;
		double flowRateDesired = 0;
		for (int i = 0; i < distancePointsRefList.get(0).size(); i++) {
			k = 0;
			for (int j = 0; j < distancePointsRefList.size(); j++) {
				List<DistancePointsModel> distancePoints = distancePointsRefList.get(j);
					k += (calibConstantList.get(j).getConstantK() - distancePoints.get(i).getyDistance());
					flowRateDesired = distancePoints.get(i).getFlowRateTo();
			}
			
			List<FlowRateMeanStdModel> flowRateMeanStdList = flowRateMeanStdService.findAllByBatchAndFlowRate(meter.getBatch(), flowRateDesired);
			reynolds = flowRateMeanStdList.get(flowRateMeanStdList.size()-1).getAvgReynolds();
			k = k / distancePointsRefList.size();
			
			CalibConstantsModel calibConst = new CalibConstantsModel();
			calibConst.setReynolds(reynolds);
			calibConst.setConstantK(k);
			calibConst.setAvgFlowRate(flowRateDesired);
			calibConst.setFlowRate(flowRateDesired);
			calibConst.setAvgTemp(30);
			calibConst.setAvgPressure(2);
			
			calibConstants.add(calibConst);
			result = true;
		}
			
		orderCrescentByFlowRate(calibConstants);
		
		return result;
	}
	
	public boolean calculateEstimatedDistanceConstIdm3m() {
		boolean retorno = false;
		ArrayList<EstimatedDistanceModel> distances15 = getEstimatedDistances3mIdm(15.0);
		ArrayList<EstimatedDistanceModel> distances22 = getEstimatedDistances3mIdm(22.5);
		ArrayList<EstimatedDistanceModel> distances1500 = getEstimatedDistances3mIdm(1500.0);

		CalibConstantsModel const15 = null;
		CalibConstantsModel const22 = null;
		CalibConstantsModel const1500 = null;

		for (CalibConstantsModel calibConst : this.getCalibService().getCalibConstants()) {
			if (calibConst.getFlowRate() == 15.0) {
				const15 = calibConst;
			} else {
				if (calibConst.getFlowRate() == 22.5) {
					const22 = calibConst;
				} else {
					if (calibConst.getFlowRate() == 1500.0) {
						const1500 = calibConst;
					}
				}
			}

		}

		if (const15 != null) {
			if (const22 != null) {
				if (const1500 != null) {

					for (int i = 0; i < distances15.size(); i++) {
						CalibConstantsModel auxConst = new CalibConstantsModel();

						System.err.println("MeterController.calculateEstimatedDistanceConst(): calculating const for " + distances15.get(i).getDesiredFlow());

						auxConst.setConstantK(((const15.getConstantK() - distances15.get(i).getkDistValue()) + (const22.getConstantK() - distances22.get(i).getkDistValue())
								+ (const1500.getConstantK() - distances1500.get(i).getkDistValue())) / 3);

						auxConst.setReynolds(distances15.get(i).getReDistValue());

						auxConst.setAvgFlowRate(distances15.get(i).getDesiredFlow());
						auxConst.setFlowRate(distances15.get(i).getDesiredFlow());
						auxConst.setAvgTemp(28.0);
						auxConst.setAvgPressure(2);

						// AJUSTE PONTUAL VAZOES
						if (auxConst.getFlowRate() == 100.0) {
							auxConst.setConstantK(auxConst.getConstantK() * 0.975);
						}

						calibConstants.add(auxConst);

					}
					retorno = true;
					orderCrescentByFlowRate(calibConstants);
				} else {
					retorno = false;
					System.err.println("MeterController.calculateEstimatedDistanceConst(): ERROR const for 1500.0 is NULL  ");
				}
			} else {
				retorno = false;
				System.err.println("MeterController.calculateEstimatedDistanceConst(): ERROR const for 22.5 is NULL  ");
			}
		} else {
			retorno = false;
			System.err.println("MeterController.calculateEstimatedDistanceConst(): ERROR const for 15.0 is NULL  ");
		}

		return retorno;
	}

	public boolean calculateEstimatedDistanceConstIdm3m_NUMERO2() {
		boolean retorno = false;
		ArrayList<EstimatedDistanceModel> distances10 = getEstimatedDistances3mIdm2(10.0);
		ArrayList<EstimatedDistanceModel> distances22 = getEstimatedDistances3mIdm2(22.5);
		ArrayList<EstimatedDistanceModel> distances1325 = getEstimatedDistances3mIdm2(1325.0);

		CalibConstantsModel const10 = null;
		CalibConstantsModel const22 = null;
		CalibConstantsModel const1325 = null;

		for (CalibConstantsModel calibConst : this.getCalibService().getCalibConstants()) {
			if (calibConst.getFlowRate() == 10.0) {
				const10 = calibConst;
				System.out.println("********* 10.0 l/H - " + const10.getConstantK());
			} else {
				if (calibConst.getFlowRate() == 22.5) {
					const22 = calibConst;
					System.out.println("********* 22.5 l/H - " + const22.getConstantK());
				} else {
					if (calibConst.getFlowRate() == 1325.0) {
						const1325 = calibConst;
						System.out.println("********* 1325.0 l/H - " + const1325.getConstantK());
					}
				}
			}

		}

		if (const10 != null) {
			if (const22 != null) {
				if (const1325 != null) {
					double k, re, flow;

					for (int i = 0; i < distances10.size(); i++) {
						CalibConstantsModel auxConst = new CalibConstantsModel();

						System.err.println("MeterController.calculateEstimatedDistanceConst(): calculating const for " + distances10.get(i).getDesiredFlow());

						k = (const10.getConstantK() + distances10.get(i).getkDistValue() + const22.getConstantK() + distances22.get(i).getkDistValue() + const1325.getConstantK()
								+ distances1325.get(i).getkDistValue()) / 3;
						re = (const10.getReynolds() + distances10.get(i).getReDistValue() + const22.getReynolds() + distances22.get(i).getReDistValue() + const1325.getReynolds()
								+ distances1325.get(i).getReDistValue()) / 3;
						flow = distances10.get(i).getDesiredFlow();
						auxConst.setConstantK(k);
						auxConst.setReynolds(re);
						auxConst.setAvgFlowRate(flow);
						auxConst.setFlowRate(flow);
						auxConst.setAvgTemp(24.0);
						auxConst.setAvgPressure(2);
						System.out.println();
						System.out.println("        CALULATED for " + flow + " K :" + k + " RE: " + re);
						System.out.println();
						//
						// if (auxConst.getFlowRate() == 10.0) {
						// auxConst.setConstantK(auxConst.getConstantK() * 1.14);
						// }
						// if (auxConst.getFlowRate() == 40.0) {
						// auxConst.setConstantK(auxConst.getConstantK() * 1.06);
						// }
						// if (auxConst.getFlowRate() == 100.0) {
						// auxConst.setConstantK(auxConst.getConstantK() * 1.04);
						// }
						// if (auxConst.getFlowRate() == 250.0) {
						// auxConst.setConstantK(auxConst.getConstantK() * 1.04);
						// }
						// if (auxConst.getFlowRate() == 450.0) {
						// auxConst.setConstantK(auxConst.getConstantK() * 1.025);
						// }
						// if (auxConst.getFlowRate() == 700.0) {
						// auxConst.setConstantK(auxConst.getConstantK() * 1.007);
						// }
						// if (auxConst.getFlowRate() == 1325.0) {
						// auxConst.setConstantK(auxConst.getConstantK() * 1.01);
						// }
						calibConstants.add(auxConst);

					}
					retorno = true;
					orderCrescentByFlowRate(calibConstants);
				} else {
					retorno = false;
					System.err.println("MeterController.calculateEstimatedDistanceConst(): ERROR const for 1325.0 is NULL  ");
				}
			} else {
				retorno = false;
				System.err.println("MeterController.calculateEstimatedDistanceConst(): ERROR const for 22.5 is NULL  ");
			}
		} else {
			retorno = false;
			System.err.println("MeterController.calculateEstimatedDistanceConst(): ERROR const for 10.0 is NULL  ");
		}

		return retorno;
	}

	public boolean calculateEstimatedDistanceConstIdm3mTWOPOSISITON() {
		boolean retorno = false;

		ArrayList<EstimatedDistanceModel> distances22 = getEstimatedDistances3mIdm2_doublePosition(22.5);
		ArrayList<EstimatedDistanceModel> distances1325 = getEstimatedDistances3mIdm2_doublePosition(1325.0);

		CalibConstantsModel const22 = null;
		CalibConstantsModel const1325 = null;

		for (CalibConstantsModel calibConst : this.getCalibService().getCalibConstants()) {
			if (calibConst.getFlowRate() == 22.5) {
				const22 = calibConst;
				System.out.println("********* 22.5 l/H - k " + const22.getConstantK() + " RE: " + const22.getReynolds());
			} else {
				if (calibConst.getFlowRate() == 1325.0) {
					const1325 = calibConst;
					System.out.println("********* 1325.0 l/H - k " + const1325.getConstantK() + " RE: " + const1325.getReynolds());
				}
			}
		}

		if (const22 != null) {
			if (const1325 != null) {
				double k, re, flow;

				for (int i = 0; i < distances22.size(); i++) {
					CalibConstantsModel auxConst = new CalibConstantsModel();

					System.err.println("MeterController.calculateEstimatedDistanceConst(): calculating const for " + distances22.get(i).getDesiredFlow());

					k = (const22.getConstantK() + distances22.get(i).getkDistValue() + const1325.getConstantK() + distances1325.get(i).getkDistValue()) / 2;
					re = (const22.getReynolds() + distances22.get(i).getReDistValue() + const1325.getReynolds() + distances1325.get(i).getReDistValue()) / 2;
					flow = distances22.get(i).getDesiredFlow();
					auxConst.setConstantK(k);
					auxConst.setReynolds(re);
					auxConst.setAvgFlowRate(flow);
					auxConst.setFlowRate(flow);
					auxConst.setAvgTemp(24.0);
					auxConst.setAvgPressure(2);
					System.out.println();
					System.out.println("        CALULATED for " + flow + " K :" + auxConst.getConstantK() + " RE: " + re);
					System.out.println();

					if (auxConst.getFlowRate() == 2.5) {
						auxConst.setConstantK(k * 0.90);
						System.out.println();
						System.out.println("        COMPENSATED for " + flow + " K :" + auxConst.getConstantK() + " RE: " + re);
						System.out.println();
					}
					//
					if (auxConst.getFlowRate() == 5.0) {
						auxConst.setConstantK(k * 0.90);
						System.out.println();
						System.out.println("        COMPENSATED for " + flow + " K :" + auxConst.getConstantK() + " RE: " + re);
						System.out.println();
					}

					if (auxConst.getFlowRate() == 10.0) {
						auxConst.setConstantK(k * 0.55);
						System.out.println();
						System.out.println("        COMPENSATED for " + flow + " K :" + auxConst.getConstantK() + " RE: " + re);
						System.out.println();
					}
					if (auxConst.getFlowRate() == 40.0) {
						auxConst.setConstantK(k * .99);
						System.out.println();
						System.out.println("        COMPENSATED for " + flow + " K :" + auxConst.getConstantK() + " RE: " + re);
						System.out.println();
					}
					if (auxConst.getFlowRate() == 100.0) {
						auxConst.setConstantK(k * 1.01);
					}
					if (auxConst.getFlowRate() == 250.0) {
						auxConst.setConstantK(k * 1.01);
					}
					if (auxConst.getFlowRate() == 700.0) {
						auxConst.setConstantK(k * 1.01);
					}
					// if (auxConst.getFlowRate() == 400.0) {
					// auxConst.setConstantK(auxConst.getConstantK() * 1.06);
					// }
					// if (auxConst.getFlowRate() == 700.0) {
					// auxConst.setConstantK(auxConst.getConstantK() * 1.06);
					// }
					calibConstants.add(auxConst);

				}
				retorno = true;
				orderCrescentByFlowRate(calibConstants);
			} else {
				retorno = false;
				System.err.println("MeterController.calculateEstimatedDistanceConst(): ERROR const for 1325.0 is NULL  ");
			}
		} else {
			retorno = false;
			System.err.println("MeterController.calculateEstimatedDistanceConst(): ERROR const for 22.5 is NULL  ");
		}

		return retorno;
	}

	/**
	 * Method prepares the header and send the calibration data to the meter. the array is ordered crecently before sending to meter
	 * 
	 * @param caliConsts
	 *            - ArrayLst with the constants models to be sent
	 * @param meter
	 *            - meter to receive the data
	 * @return true if send is successfull
	 */
	public boolean sendCalibDataToMeter(ArrayList<CalibConstantsModel> caliConsts, MeterModel meter) {

		orderCrescentByFlowRate(caliConsts);
		boolean retorno = false;
		double dsos = meter.getDsos();
		double dzc = meter.getDzc();
		int numberOfCalcConstants = caliConsts.size();
		marcarMedidorNoLog();
		LOG.debug("ENTRADA sendCalibDataToMeter(): IP={} | constantes={} | DSOS={} | DZC={} | pathLength={} | diametro reducao={}",
				meter.getConversor().getIp(), numberOfCalcConstants, dsos, dzc,
				meter.getMeterTypeModel().getUltraSoundPathLengh(), meter.getMeterTypeModel().getReductionDiameter());
		long inicioEnvioCalib = ProcessLog.iniciarCronometro();
		// Here we allocate the size of the ByteBuffer we are going to transmit
		// 2 * because we transfer Re and then K constans. 8 is the number of bytes for each double value. 42 is number
		// of bytes of configuration parameters
		response = ByteBuffer.allocate((numberOfCalcConstants * 8 * 2) + 42/* header bytes */);
		response.order(ByteOrder.LITTLE_ENDIAN);
		// 22 bytes is fixed with the following attributes:
		response.putDouble(meter.getMeterTypeModel().getUltraSoundPathLengh()); // length +8 Bytes
		response.putDouble(meter.getMeterTypeModel().getReductionDiameter()); // Diameter in flowpath reduction +8 Bytes
		response.putDouble(calibService.calculateMeterReductionArea(meter.getMeterTypeModel())); // flowpath area m�
																									// +8bytes
		response.putDouble(dsos); // +8 bytes
		response.putDouble(dzc);// +8 bytes2 40
		response.putShort((short) (numberOfCalcConstants));// number of calculated constants + 4{init cutoff, low
															// cutoff, end high cutoff, high cutoff

		// in order from lowest flow rate to highest
		for (int i = 0; i < caliConsts.size(); i++) {
			response.putDouble(caliConsts.get(i).getReynolds());
		}

		// in order from lowest flow rate to highest
		for (int j = 0; j < caliConsts.size(); j++) {
			response.putDouble(caliConsts.get(j).getConstantK());
		}

		// TODO - remove
		// System.out.println("SIZE RESPONSE: "+response.limit());

		// Sets data on to be sent on thread and switches the thread to the load cali params state
		meterSocketThread.setDataCmdType(MeterCmdEnum.CMD_LOAD_CALIB_PARAMETERS);
		meterSocketThread.setDataToSend(response);
		meterSocketThread.setSendDataToMeter(true);

		CompletableFuture cpf = CompletableFuture.runAsync(() -> {
			try {
				Thread.currentThread().setName("CPF|Download Calib Data: " + meter.getConversor().getIp());
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// Registra com stack trace completo, sem alterar o tratamento original.
				ProcessLog.erro(LOG, "sendCalibDataToMeter(): timer assincrono de 2 s interrompido (IP "
						+ meter.getConversor().getIp() + ")", e);
				System.err.println("ERROR: InterruptedException of assync thread at MeterController.sendCalibDataToMeter()");
			}
		});

		while (isCheckingData() && !isCommDown()) {
			if (cpf.isDone()) {
				meterSocketThread.setForceStopAckChecking(true);
				break;
			}
		}
		cpf.cancel(true);

		//
		// while(isThreadWorking());

		// setCheckingData(true);

		// while (isCheckingData() && !isCommDown());
		// while (isCheckingData()){
		// Thread.currentThread().yield();
		// }

		if (isDataSentCorrectly()) {
			LOG.info("SUCESSO: constantes de calibracao ({} pontos) enviadas e confirmadas (ACK) pelo medidor no IP {} em {} ms",
					numberOfCalcConstants, meter.getConversor().getIp(), ProcessLog.duracaoMs(inicioEnvioCalib));
			retorno = true;
		} else {
			LOG.error("FALHA: constantes de calibracao NAO confirmadas pelo medidor no IP {} apos {} ms (comunicacao caida: {})",
					meter.getConversor().getIp(), ProcessLog.duracaoMs(inicioEnvioCalib), isCommDown());
			retorno = false;
		}

		LOG.debug("SAIDA sendCalibDataToMeter(): retorno={}", retorno);
		return retorno;
	}

	/**
	 * Retrieves the data that was read on the socket connection and sets is as zeroflow data
	 */
	public void fetchZeroFlowData() {
		this.meterDataZeroFlow = new ArrayList<>(fetchDataBuffer());
		marcarMedidorNoLog();
		LOG.debug("fetchZeroFlowData(): {} amostras de vazao zero recuperadas do buffer do socket", this.meterDataZeroFlow.size());
	}

	/**
	 * Retrieves the data that was read on the socket connection and sets is as flow data
	 */
	public void fetchFlowData() {
		this.meterData = new ArrayList<>(fetchDataBuffer());
		marcarMedidorNoLog();
		LOG.debug("fetchFlowData(): {} amostras de vazao recuperadas do buffer do socket", this.meterData.size());
	}

	/**
	 * Retrieves the data that was read on the socket connection and sets is as flow data
	 */
	public void fetchVerifFlowData() {
		this.meterDataVerif = new ArrayList<>(fetchDataBuffer());
	}

	/**
	 * Fetches the data buffer from the thread that communicates with meter; Must call clearDataBuffer() for every change of flow rate to assure no mix of data between the flowrates
	 */
	public ArrayList<MeterDataModel> fetchDataBuffer() {
		ArrayList<MeterDataModel> auxList = meterSocketThread.getCommPackages();
		return auxList;
	}

	/**
	 * Clears the data buffer. This must happen when data for different flow rate will be stored
	 */
	public void clearDataBuffer() {
		meterSocketThread.clearCommPacks();
	}

	/**
	 * 
	 */
	public void resetComm() {
		commInitiated = false;
		isCommVerified = false;
		if (socketComm != null) {
			if (socketComm.isConnected()) {
				socketComm.disconnect();
			}
		}
		socketComm = null;

		//
		if (meterSocketThread != null) {
			meterSocketThread.stop();
		}
		meterSocketThread = null;

		//
		if (readThread != null) {
			readThread.stop();
		}
		readThread = null;
	}

	/**
	 * 
	 */
	public boolean initiateComm() {
		commInitiated = false;
		marcarMedidorNoLog();
		ip = meter.getConversor().getIp();
		port = meter.getConversor().getPort();
		LOG.debug("ENTRADA initiateComm(): abrindo socket TCP para {}:{}", ip, port);
		long inicioComm = ProcessLog.iniciarCronometro();
		if (ip != null && port != 0) {
			socketComm = new MeterSocketComm(ip, port);
			if (socketComm.isConnected()) {
				commInitiated = true;
				meterSocketThread = new MeterSocketThread(socketComm);
				LOG.info("Socket conectado ao medidor em {}:{} em {} ms", ip, port, ProcessLog.duracaoMs(inicioComm));
			} else {
				// System.err.println("ERROR: COMM not connected at MeterController.initiateComm");
				LOG.error("FALHA: socket NAO conectou ao medidor em {}:{} apos {} ms", ip, port, ProcessLog.duracaoMs(inicioComm));
				commInitiated = false;
			}
		} else {
			LOG.error("FALHA em initiateComm(): IP ou porta invalidos (ip={}, porta={})", ip, port);
		}
		LOG.debug("SAIDA initiateComm(): retorno={}", commInitiated);
		return commInitiated;
	}

	public boolean disconnectComm() {
		boolean retorno = false;
		marcarMedidorNoLog();
		LOG.debug("ENTRADA disconnectComm(): socketComm={} | socketThread={}", (socketComm != null), (meterSocketThread != null));
		if (socketComm != null) {
			if (meterSocketThread != null) {
				if (meterSocketThread.isStop()) {
					retorno = socketComm.disconnect();
				} else {
					meterSocketThread.stop();
					retorno = true;
				}
			} else {
				retorno = socketComm.disconnect();
			}
		} else {
			// If comm is null. It is considered disconnected
			retorno = true;
		}
		LOG.info("FINALIZACAO: desconexao do medidor concluida. Resultado: {}", retorno);
		return retorno;
	}

	/**
	 * Marca a identificacao deste medidor ([MEDIDOR-NN]) no contexto de log da
	 * thread corrente, para que as linhas seguintes indiquem exatamente a qual
	 * medidor se referem. Apenas afeta o log; nao altera estado do processo.
	 */
	private void marcarMedidorNoLog() {
		if (meter != null && meter.getConversor() != null && meter.getConversor().getName() != null) {
			ProcessLog.setMedidor(meter.getConversor().getName());
		}
	}

	public double calculateVerifError() {
		return 0;
	}

	/**
	 * 
	 * @return
	 */
	public boolean verifiConnection2() {
		boolean retorno = false;
		if (meterSocketThread != null) {
			if (this.socketComm.verifiConnection2()) {
				setMeterUfoId(socketComm.getUfoId());
				retorno = true;
			} else {
				retorno = false;
			}
		}
		return retorno;
	}

	/**
	 * 
	 * @return
	 */
	public boolean verifiConnection() {
		isCommVerified = false;
		if (meterSocketThread != null) {
			// if (!readThread.isInterrupted()) {
			if (this.socketComm.verifiConnection()) {
				setMeterUfoId(socketComm.getUfoId());
				// if(this.socketComm.readFwVersion()){
				// setFwVersion(socketComm.getFwVersion());
				isCommVerified = true;
				// }
			} else {
				isCommVerified = false;
			}
			// } else {
			// // TODO: aqui
			// commInitiated = false;
			// isCommVerified = false;
			// isFwVersionVerified = false;
			// }
		}
		return isCommVerified;
	}

	// /**
	// *
	// */
	// public byte[] readMeterConfigData() {
	// byte[] data = null;
	//
	// if (this.fwVersionLong() >= fwVersionLong(ViewStatesUtil.arrayMinVersion)) {
	// if(meterSocketThread.sendCommandToMeter(MeterCmdEnum.CMD_READ_CONFIG_PARAMETERS)){
	// data = meterSocketThread.receiveMeterConfig();
	// //Criar arquivo e salvar arquivo
	// }
	//
	//
	//// data = socketComm.readMeterConfigData();
	//
	// } else {
	// // case version is prior to 2.0.0 set return to True
	// retorno = true;
	// System.err.println("MeterController:readMeterConfigData: Did not READ CONFIG DATA to Meter in position " + this.meter.getConversor().getIp() + " firmware version: " + this.fwVersionLong());
	// }
	// return data;
	// }

	/**
	 * Method sends the system date to the meter.
	 * 
	 * @return true - case successful in case the fw version is prior to 2.0.0 the return of the method is always true
	 */
	public boolean sendMeterSystemDate(Calendar currentDate) {
		boolean retorno = false;

		if (this.fwVersionLong() >= fwVersionLong(ViewStatesUtil.arrayMinVersion)) {
			// if (this.fwVersionLong() >= fwVersionLong(new byte[] { 2, 0, 0, 0, 0, 0 })) {
			ByteBuffer data = ByteBuffer.allocate(6);
			data.put((byte) currentDate.get(Calendar.SECOND));
			data.put((byte) currentDate.get(Calendar.MINUTE));
			data.put((byte) currentDate.get(Calendar.HOUR));
			data.put((byte) currentDate.get(Calendar.DAY_OF_MONTH));
			data.put((byte) (currentDate.get(Calendar.MONTH))); // +1 = The first month of the year is 0
			data.put((byte) (currentDate.get(Calendar.YEAR) - 1900));

			response = data;
			response.order(ByteOrder.LITTLE_ENDIAN);

			meterSocketThread.setDataToSend(response);
			meterSocketThread.setDataCmdType(MeterCmdEnum.CMD_UPDATE_DATE);
			meterSocketThread.setSendDataToMeter(true);

			setCheckingData(true);

			// while (isCheckingData());
			while (isCheckingData() && !isCommDown());
			// while (isCheckingData()){
			// Thread.currentThread().yield();
			// }

			if (isDataSentCorrectly()) {
				retorno = true;
			} else {
				retorno = false;
			}
		} else {
			// case version is prior to 2.0.0 set return to True
			retorno = true;
			System.err.println("Did not send SYSTEM DATE to Meter in position " + this.meter.getConversor().getIp() + " firmware version: " + this.fwVersionLong());

		}

		return retorno;
	}

	/**
	 * 
	 * @return
	 */
	public boolean sendMeterReplaceDate(Calendar currentDate, int warrantyYears) {

		boolean retorno = false;

		if (this.fwVersionLong() >= fwVersionLong(ViewStatesUtil.arrayMinVersion)) {
			// if (this.fwVersionLong() >= fwVersionLong(new byte[] { 2, 0, 0, 0, 0, 0 })) {

			SimpleDateFormat dtFormat = new SimpleDateFormat("ddMMyyyy");
			currentDate.setTime(new Date());
			currentDate.add(Calendar.YEAR, warrantyYears);

			// This is to register the replace date on the database
			meter.setDateOfReplace(currentDate.getTime());

			String dataString = dtFormat.format(currentDate.getTime());

			// System.out.println(dataString);

			ByteBuffer data = ByteBuffer.allocate(8);
			char dado;
			for (int i = dataString.length() - 1; i >= 0; i--) {
				dado = dataString.charAt(i);
				data.put((byte) dado);
			}

			response = data;

			meterSocketThread.setDataToSend(response);
			meterSocketThread.setDataCmdType(MeterCmdEnum.CMD_WRITE_REPLACEMENT_DATE);
			meterSocketThread.setSendDataToMeter(true);

			setCheckingData(true);

			// while (isCheckingData());
			while (isCheckingData() && !isCommDown());
			// while (isCheckingData()){
			// Thread.currentThread().yield();
			// }

			if (isDataSentCorrectly()) {
				retorno = true;
			} else {
				retorno = false;
			}
		} else {
			// case version is prior to 2.0.0 set return to True
			retorno = true;
			System.err.println("Did not send REPLACE DATE to Meter in position " + this.meter.getConversor().getIp() + " firmware version: " + this.fwVersionLong());

		}

		return retorno;
	}

	/**
	 * 
	 * @return
	 */
	public boolean sendMeterSerialNumber(String serialNumber) {
		boolean retorno = false;
		// Fw version as of 2.0.0 has Serial number reception
		if (this.fwVersionLong() >= fwVersionLong(ViewStatesUtil.arrayMinVersion)) {
			// if (this.fwVersionLong() >= fwVersionLong(new byte[] { 2, 0, 0, 0, 0, 0 })) {
			ByteBuffer data = ByteBuffer.allocate(12);

			char dado;
			for (int i = serialNumber.length() - 1; i >= 0; i--) {
				dado = serialNumber.charAt(i);
				data.put((byte) dado);
			}
			data.flip();
			response = data;

			meterSocketThread.setDataToSend(data);
			meterSocketThread.setDataCmdType(MeterCmdEnum.CMD_WRITE_SERIAL_NUMBER);
			meterSocketThread.setSendDataToMeter(true);

			setCheckingData(true);
			// while (isCheckingData());

			// try{
			// Thread.currentThread().sleep(300);
			// }catch(InterruptedException ie){
			// ie.printStackTrace();
			// }

			while (isCheckingData() && !isCommDown());
			// while (isCheckingData()){
			// Thread.currentThread().yield();
			// }

			if (isDataSentCorrectly()) {
				retorno = true;
			} else {
				retorno = false;
			}
		} else {
			// case version is prior to 2.0.0 set return to True
			retorno = true;
			System.err.println("Did not send SN to Meter in position " + this.meter.getConversor().getIp() + " firmware version: " + this.fwVersionLong() + " only "
					+ fwVersionLong(ViewStatesUtil.arrayMinVersion));
		}

		return retorno;
	}

	/**
	 * @return
	 */
	public void updateMeterUfoIdAndFwVersion() {
		int ufoId = ByteBuffer.wrap(meterUfoId).order(ByteOrder.LITTLE_ENDIAN).getInt();
		this.meter.setMeterUfoId(ufoId);
//		this.meter.setFwVersion(Utilities.parseVersionToString(fwVersion));
	}

	public long fwVersionLong() {
		return meterSocketThread.fwVersionToLong();
	}

	public long fwVersionLong(byte dado[]) {
		return meterSocketThread.fwVersionToLong(dado);
	}

	public boolean successfullDataSend() {
		return meterSocketThread.isSuccefullDataSend();
	}

	public boolean readFwVersion() {
		// return socketComm.readFwVersion();
		isFwVersionVerified = false;
		// if (meterSocketThread != null) {
		// if (!readThread.isInterrupted()) {
		if (socketComm.readFwVersion()) {
			fwVersion = socketComm.getFwVersion();
			isFwVersionVerified = true;
		} else {
			isFwVersionVerified = false;
		}
		// } else {
		// commInitiated = false;
		// isCommVerified = false;
		// isFwVersionVerified = false;
		// }
		// }
		return true;//isFwVersionVerified;
	}

	/**
	 * Function returns the value of attribute socketThread
	 * 
	 * @return the socketThread
	 */
	public MeterSocketThread getSocketThread() {
		return meterSocketThread;
	}

	/**
	 * Function sets the value for attribute socketThread
	 * 
	 * @param socketThread
	 *            the socketThread to set
	 */
	public void setSocketThread(MeterSocketThread socketThread) {
		this.meterSocketThread = socketThread;
	}

	public void assignSerialNumber(String serial) {
		this.meter.setSerialNumber(serial);
	}

	/**
	 * Function returns the value of attribute readStatus
	 * 
	 * @return the readStatus
	 */
	public boolean isReadStatus() {
		return readStatus;
	}

	/**
	 * Function sets the value for attribute readStatus
	 * 
	 * @param readStatus
	 *            the readStatus to set
	 */
	public void setReadStatus(boolean readStatus) {
		this.readStatus = readStatus;
	}

	/**
	 * Function returns the value of attribute readThread
	 * 
	 * @return the readThread
	 */
	public Thread getReadThread() {
		return readThread;
	}

	/**
	 * Function sets the value for attribute readThread
	 * 
	 * @param readThread
	 *            the readThread to set
	 */
	public void setReadThread(Thread readThread) {
		this.readThread = readThread;
	}

	/**
	 * Function returns the value of attribute isTransmiting
	 * 
	 * @return the isTransmiting
	 */
	public boolean isTransmiting() {
		return isTransmiting;
	}

	/**
	 * Function sets the value for attribute isTransmiting
	 * 
	 * @param isTransmiting
	 *            the isTransmiting to set
	 */
	public void setTransmiting(boolean isTransmiting) {
		this.isTransmiting = isTransmiting;
	}

	/**
	 * Function returns the value of attribute calibService
	 * 
	 * @return the calibService
	 */
	public CalibrationService getCalibService() {
		return calibService;
	}

	/**
	 * Function sets the value for attribute calibService
	 * 
	 * @param calibService
	 *            the calibService to set
	 */
	public void setCalibService(CalibrationService calibService) {
		this.calibService = calibService;
	}

	/**
	 * Function returns the value of attribute commInitiated
	 * 
	 * @return the commInitiated
	 */
	public boolean isCommInitiated() {
		return commInitiated;
	}

	/**
	 * Function sets the value for attribute commInitiated
	 * 
	 * @param commInitiated
	 *            the commInitiated to set
	 */
	public void setCommInitiated(boolean commInitiated) {
		this.commInitiated = commInitiated;
	}

	/**
	 * Function returns the value of attribute isCommVerified
	 * 
	 * @return the isCommVerified
	 */
	public boolean isCommVerified() {
		return isCommVerified;
	}

	/**
	 * Function sets the value for attribute isCommVerified
	 * 
	 * @param isCommVerified
	 *            the isCommVerified to set
	 */
	public void setCommVerified(boolean isCommVerified) {
		this.isCommVerified = isCommVerified;
	}

	/**
	 * Function returns the value of attribute isFwVersionVerified
	 * 
	 * @return the isFwVersionVerified
	 */
	public boolean isFwVersionVerified() {
		return isFwVersionVerified;
	}

	/**
	 * Function sets the value for attribute isFwVersionVerified
	 * 
	 * @param isFwVersionVerified
	 *            the isFwVersionVerified to set
	 */
	public void setFwVersionVerified(boolean isFwVersionVerified) {
		this.isFwVersionVerified = isFwVersionVerified;
	}

	/**
	 * Function returns the value of attribute meterDataVerif
	 * 
	 * @return the meterDataVerif
	 */
	public ArrayList<MeterDataModel> getMeterDataVerif() {
		return meterDataVerif;
	}

	/**
	 * Function sets the value for attribute meterDataVerif
	 * 
	 * @param meterDataVerif
	 *            the meterDataVerif to set
	 */
	public void setMeterDataVerif(ArrayList<MeterDataModel> meterDataVerif) {
		this.meterDataVerif = meterDataVerif;
	}

	public boolean isDataCorectlySent() {
		return meterSocketThread.isDataSentCorrectly();
	}

	/**
	 * Function returns the value of attribute calcError
	 * 
	 * @return the calcError
	 */
	public ArrayList<VerificationErrorModel> getCalcErrors() {
		return verifErrors;
	}

	/**
	 * Function sets the value for attribute calcError
	 * 
	 * @param calcError
	 *            the calcError to set
	 */
	public void setCalcErrors(ArrayList<VerificationErrorModel> calcError) {
		this.verifErrors = calcError;
	}

	/**
	 * Function returns the value of attribute meterUfoId
	 * 
	 * @return the meterUfoId
	 */
	public byte[] getMeterUfoId() {
		return meterUfoId;
	}

	/**
	 * Function sets the value for attribute meterUfoId
	 * 
	 * @param meterUfoId
	 *            the meterUfoId to set
	 */
	public void setMeterUfoId(byte[] meterUfoId) {
		this.meterUfoId = meterUfoId;
	}

	/**
	 * Function returns the value of attribute fwVersion
	 * 
	 * @return the fwVersion
	 */
	public byte[] getFwVersion() {
		return fwVersion;
	}

	/**
	 * Function sets the value for attribute fwVersion
	 * 
	 * @param fwVersion
	 *            the fwVersion to set
	 */
	public void setFwVersion(byte[] fwVersion) {
		this.fwVersion = fwVersion;
	}

	@SuppressWarnings("static-access")
	public void switchStateToPostCalibMode() {
		if (this.fwVersionLong() >= fwVersionLong(ViewStatesUtil.arrayMinVersion)) {
			// if (this.fwVersionLong() >= fwVersionLong(new byte[] { 2, 0, 0, 0, 0, 0 })) {
			if (meterSocketThread.sendCommandToMeter(MeterCmdEnum.CMD_LOAD_POST_CALIB_OPMODE)) {
				meterSocketThread.setCheckForAckOrNack(true);
				try {
					Thread.currentThread().sleep(20);
				} catch (InterruptedException e) {
					ProcessLog.erro(LOG, "MeterController.switchStateToPostCalibMode()", e);
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	public void switchStateToCalibMode() {
		if (meterSocketThread.sendCommandToMeter(MeterCmdEnum.CMD_LOAD_CALIB_OPMODE)) {
			meterSocketThread.setCheckForAckOrNack(true);
			try {
				Thread.currentThread().sleep(20);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterController.switchStateToCalibMode()", e);
				e.printStackTrace();
			}
			// while (isThreadWorking());
		}
	}

	@SuppressWarnings("static-access")
	public void switchStateToStandByMode() {
		if (meterSocketThread.sendCommandToMeter(MeterCmdEnum.CMD_LOAD_STAND_BY_OPMODE)) {
			meterSocketThread.setCheckForAckOrNack(true);
			try {
				Thread.currentThread().sleep(50);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterController.switchStateToStandByMode()", e);
				e.printStackTrace();
			}
			while (isThreadWorking());
		}
	}

	@SuppressWarnings("static-access")
	public void switchStateToNormalMode() {
		if (meterSocketThread.sendCommandToMeter(MeterCmdEnum.CMD_LOAD_NORMAL_OPMODE)) {
			meterSocketThread.setCheckForAckOrNack(true);
			try {
				Thread.currentThread().sleep(50);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterController.switchStateToNormalMode()", e);
				e.printStackTrace();
			}
			// while (isThreadWorking());
		}
	}

	@SuppressWarnings("static-access")
	public void runTrimAgcStage2() {
		if (meterSocketThread.sendCommandToMeter(MeterCmdEnum.CMD_TRIM_AG_STAGE_2)) {
			meterSocketThread.setCheckForAckOrNack(true);
			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterController.runTrimAgcStage2()", e);
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("static-access")
	public void switchStateToVerifMode() {
		meterSocketThread.setDataCmdType(MeterCmdEnum.CMD_LOAD_VERIF_OPMODE);
		if (meterSocketThread.sendCommandToMeter(MeterCmdEnum.CMD_LOAD_VERIF_OPMODE)) {
			meterSocketThread.setCheckForAckOrNack(true);
			try {
				Thread.currentThread().sleep(20);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterController.switchStateToVerifMode()", e);
				e.printStackTrace();
			}
		}
	}

	public void stopMeterReading() {

	}

	public void switchStatePostCalibMode() {
		meterSocketThread.setDataCmdType(MeterCmdEnum.CMD_LOAD_POST_CALIB_OPMODE);
		if (meterSocketThread.sendCommandToMeter(MeterCmdEnum.CMD_LOAD_POST_CALIB_OPMODE)) {
			meterSocketThread.setCheckForAckOrNack(true);
			try {
				Thread.currentThread().sleep(20);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterController.switchStatePostCalibMode()", e);
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("static-access")
	public void enableTransmitData() {
		if (meterSocketThread.sendCommandToMeter(MeterCmdEnum.CMD_ENABLE_DATA_TRANSMIT)) {
			meterSocketThread.setCheckForAckOrNack(true);
			try {
				Thread.currentThread().sleep(20);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterController.enableTransmitData()", e);
				e.printStackTrace();
			}
			// while (isThreadWorking());
		}
	}

	// public boolean disableTransmitData() {
	// return socketComm.sendMeterCmd(MeterCmdEnum.CMD_DISABLE_DATA_TRANSMIT);
	// }

	@SuppressWarnings("static-access")
	public void disableTransmitData() {
		if (meterSocketThread.sendCommandToMeter(MeterCmdEnum.CMD_DISABLE_DATA_TRANSMIT)) {
			meterSocketThread.setCheckForAckOrNack(true);
			try {
				Thread.currentThread().sleep(20);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterController.disableTransmitData()", e);
				e.printStackTrace();
			}
			// while (isThreadWorking());
		}
	}

	@SuppressWarnings("static-access")
	public void readMeterConfigData() {
		if (meterSocketThread.sendCommandToMeter(MeterCmdEnum.CMD_READ_CONFIG_PARAMETERS)) {
			meterSocketThread.setCheckForAckOrNack(true);
			try {
				Thread.currentThread().sleep(20);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "MeterController.readMeterConfigData()", e);
				e.printStackTrace();
			}
			// while (isThreadWorking());
		}
	}

	/**
	 * 
	 */
	public byte[] receiverData(int count) {
		//
		CompletableFuture cpf = CompletableFuture.runAsync(() -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				System.err.println("ERROR: InterruptedException of assync thread at ProcessController.receiverData()");
			}
		});

		byte[] array = new byte[count];
		byte aux;
		int available = socketComm.availableBytes();

		while (available < count) {
			if (cpf.isDone()) {
				available = socketComm.availableBytes();
				break;
			}
		}
		
		try {
			socketComm.readData(array, count);
		} catch (IOException e) {
			array = null;
			ProcessLog.erro(LOG, "MeterController.receiverData()", e);
			e.printStackTrace();
		}

//		int idx = 0;
//		while (idx < count) {
//			try {
//				aux = socketComm.readByte();
//				if (aux != -1) {
//					array[idx++] = (byte) aux;
//				}
//			} catch (SocketTimeoutException so) {
//				System.err.println("ERROR: SocketTimeoutException on MeterSocketComm.receiverData() for " + Thread.currentThread().getName());
//				array = null;
//				break;
//			}
//		}
//		System.err.println("MeterSocketComm.receiverData() - idx: " + idx);
		return array;
	}

	// public boolean isOpSuccessfull(MeterCommStateEnum stateToCheck) {
	// return meterSocketThread.getOperationSuccessStateHashMap().get(stateToCheck);
	// }
	//
	// public boolean isLoadPreCalibModeSuccesfull() {
	// return isOpSuccessfull(MeterCommStateEnum.LOAD_PRE_CALIB_OPMODE);
	// }
	//
	// public boolean isLoadCalibParamsModeSuccesfull() {
	// return isOpSuccessfull(MeterCommStateEnum.LOAD_CALIB_PARAM);
	// }
	//
	// public boolean isLoadCalibOpModeSuccesfull() {
	// return isOpSuccessfull(MeterCommStateEnum.LOAD_CALIB_OPMODE);
	// }
	//
	// public boolean isLoadPostCalibOpModeSuccesfull() {
	// return isOpSuccessfull(MeterCommStateEnum.LOAD_POST_CALIB_OPMODE);
	// }
	//
	// public boolean isLoadStandbyOpModeSuccesfull() {
	// return isOpSuccessfull(MeterCommStateEnum.LOAD_STANDBY_OPMODE);
	// }
	//
	// public boolean isReadMeterStateSuccesfull() {
	// return isOpSuccessfull(MeterCommStateEnum.READ_METER);
	// }
	//
	// public boolean isLoadTrimAGCStage2OpModeSuccesfull() {
	// return isOpSuccessfull(MeterCommStateEnum.TRIM_AG_STAGE2);
	// }
	//
	// public boolean isEnableDataTransmitOpModeSuccesfull() {
	// return isOpSuccessfull(MeterCommStateEnum.ENABLE_DATA_TRANSMIT);
	// }
	//
	// public boolean isDisableDataTransmitOpModeSuccesfull() {
	// return isOpSuccessfull(MeterCommStateEnum.DISABLE_DATA_TRANSMIT);
	// }

	public void orderCrescentByFlowRate(ArrayList<CalibConstantsModel> calibConstants) {
		Collections.sort(calibConstants, new Comparator<CalibConstantsModel>() {
			public int compare(CalibConstantsModel const1, CalibConstantsModel const2) {
				return const1.getFlowRate() < const2.getFlowRate() ? -1 : (const1.getFlowRate() > const2.getFlowRate()) ? 1 : 0;
			}
		});

	}
	
	public void orderDecrescentByFlowRate(ArrayList<CalibConstantsModel> calibConstants) {
		Collections.sort(calibConstants, new Comparator<CalibConstantsModel>() {
			public int compare(CalibConstantsModel const1, CalibConstantsModel const2) {
				return const1.getFlowRate() > const2.getFlowRate() ? -1 : (const1.getFlowRate() < const2.getFlowRate()) ? 1 : 0;
			}
		});

	}

	public ArrayList<VerificationErrorModel> fetchCalcErrors() {
		this.verifErrors = calibService.getErrorConstants();
		return getCalcErrors();
	}

	public boolean saveVerificationErrorsInDb() {
		boolean retorno = false;
		if (verifErrors != null) {
			this.getMeter().getErrors().addAll(verifErrors);
			for (VerificationErrorModel verifError : verifErrors) {
				try {
					verifError.setMeter(this.getMeter());
					verifErrorService.persist(verifError);
					retorno = true;
				} catch (CrudDatabaseException e) {
					ProcessLog.erro(LOG, "MeterController.saveVerificationErrorsInDb()", e);
					e.printStackTrace();
					retorno = false;
					break;
				}
			}
		} else {
			System.err.println("MESSAGE: verifErrors of meter in position: " + this.getMeter().getConversor().getIp() + " is NULL");
		}
		return retorno;
	}

	boolean retorno = false;

	public boolean saveCalibConstantsInDb() {
		if (meter.getCalibConstants() != null) {
			meter.getCalibConstants().addAll(this.calibConstants);
			if (!meter.getCalibConstants().isEmpty()) {
				for (CalibConstantsModel calibModel : calibConstants) {
					try {
						calibModel.setMeter(meter);
						calibConstService.persist(calibModel);
						retorno = true;
					} catch (CrudDatabaseException e) {
						ProcessLog.erro(LOG, "MeterController.saveCalibConstantsInDb()", e);
						e.printStackTrace();
						retorno = false;
						break;
					}
				}
			}
		}
		return retorno;
	}

	public boolean checkMeterApproval() {
		boolean retorno = false;

		MeterTypeModel meterType = meter.getMeterTypeModel();
		if (verifErrors != null) {
			if (!verifErrors.isEmpty()) {
				double error;
				double expectedFlowRate;
				double minFlowRate;
				double transtionFlowRate;
				double maxFlowRate;

				double minFlowRatePermissileError = meterType.getMinFlowRatePermissibleError();
				// minFlowRatePermissileError = minFlowRatePermissileError - 0.5;
				// minFlowRatePermissileError = 100; // prod

				double nomFlowRatePermissileError = meterType.getNominalFlowRatePermissibleError();

				// nomFlowRatePermissileError = nomFlowRatePermissileError - 0.5;
				// nomFlowRatePermissileError = 100; // prod

				for (VerificationErrorModel verifError : verifErrors) {
					// q < q1
					error = verifError.getError();
					error = Math.abs(error);
					expectedFlowRate = verifError.getExpectedFlowRate();
					minFlowRate = meterType.getMinFlowrate().getFlowRate();
					transtionFlowRate = meterType.getTransitionFlowrate().getFlowRate();

					if (expectedFlowRate < transtionFlowRate) {
						if (error <= minFlowRatePermissileError) {
							retorno = true;
						} else {
							retorno = false;
							break;
						}
					} else {
						// q >= qt
						if (expectedFlowRate >= transtionFlowRate) {
							if (error <= nomFlowRatePermissileError) {
								retorno = true;
							} else {
								retorno = false;
								break;
							}
						}
					}
				}
			}

		}
		return retorno;
	}

	public boolean checkMeterForNaN() {
		boolean retorno = false;
		if (verifiConnection()) {
			if (this.socketComm.verifiConnection()) {
				retorno = true;
			}
		}

		return retorno;
	}

	public boolean saveMeterInDb() {
		boolean retorno = false;

		try {
			if (meterService.persist(this.meter).equals(this.meter)) {

				retorno = true;
			}
		} catch (CrudDatabaseException e) {
			ProcessLog.erro(LOG, "MeterController.saveMeterInDb()", e);
			e.printStackTrace();
		}

		return retorno;
	}

	public boolean updateMeterInDb() {
		boolean retorno = false;

		try {
			// if (meterService.update(this.meter).equals(this.meter)) {
			// retorno = true;
			// }
			if (meter != null) {
				if (meter.getId() != null) { // To update meter id has to be pulled from DB
					if (meter.getId() != 0) { // To update meter id has to be pulled from DB
						meterService.update(this.meter);
					}
				}
			}
			retorno = true;
		} catch (CrudDatabaseException e) {
			ProcessLog.erro(LOG, "MeterController.updateMeterInDb()", e);
			e.printStackTrace();
		}
		return retorno;
	}

	public boolean checkAckForCommand() {
		boolean retorno = false;

		if (meterSocketThread.isAcked()) {
			retorno = true;
		}

		return retorno;
	}

	public void setAcked(boolean value) {
		meterSocketThread.setAcked(value);
	}

	public void setEnableRead(boolean value) {
		meterSocketThread.setEnableRead(value);
		this.isReading = true;
	}

	public void setCheckForAckOrNack(boolean value) {
		meterSocketThread.setCheckForAckOrNack(value);
	}

	public void setForceStopCheckAckOrNack(boolean value) {
		meterSocketThread.setForceStopAckChecking(value);
	}

	public void setHasReachedSampleCount(boolean value) {
		this.meterSocketThread.setHasReachedSampleCount(value);
	}

	public boolean getHasReachedSampleCount() {
		return this.meterSocketThread.isHasReachedSampleCount();
	}

	public void clearSampleCounter() {
		this.meterSocketThread.setSampleCounter(0);
	}

	public boolean isTimedOut() {
		return meterSocketThread.getTimeoutExceededProperty().get();
	}

	public void removeFlowRate(double flowRate) {
		meterSocketThread.removeFlowRate(flowRate);
	}

	public void clearMeterData() {
		this.meterData.clear();
	}

	public void clearMeterZeroFlowData() {
		this.meterDataZeroFlow.clear();
	}

	public boolean isReproved() {
		return isReproved;
	}

	public void setIsReproved(boolean value) {
		this.isReproved = value;
	}

	public boolean isMeterSerialNumberConfigured() {
		return meter.isMeterSerialNumberConfigured();
	}

	public void setMeterSerialNumberConfigured(boolean serialNumberConfigured) {
		meter.setMeterSerialNumberConfigured(serialNumberConfigured);
	}

	/**
	 * Function returns the value of attribute isAvailableSN
	 * 
	 * @return the isAvailableSN
	 */
	public boolean isAvailableSN() {
		// return isAvailableSN;
		return meter.isAvailableSN();
	}

	/**
	 * Function sets the value for attribute isAvailableSN
	 * 
	 * @param isAvailableSN
	 *            the isAvailableSN to set
	 */
	public void setAvailableSN(boolean isAvailableSN) {
		// this.isAvailableSN = isAvailableSN;
		meter.setAvailableSN(isAvailableSN);
	}

	public MeterSocketComm getMeterSocketComm() {
		return this.socketComm;
	}

	/**
	 * Tranforms a literal string to byte array
	 * 
	 * @return
	 */
	private byte[] charLiteralToByteHexArray(String dado) {
		byte[] retorno = new byte[dado.length() / 2];
		byte nippleLsb = 0;
		byte nippleMsb = 0;
		byte auxByte = 0;
		boolean first = true;
		if (!dado.contains(" ")) {
			for (int i = 0; i < dado.length(); i++) {
				if (first) {
					nippleMsb = 0;
					nippleMsb = (byte) (nippleMsb | TypeConverterUtil.stringDecToHex(dado.charAt(i)));
					nippleMsb = (byte) (nippleMsb << 4);
					first = false;
				} else {
					nippleLsb = 0;
					nippleLsb = (byte) (nippleLsb | TypeConverterUtil.stringDecToHex(dado.charAt(i)));
					first = true;
					nippleLsb = (byte) (0x0f & nippleLsb);
					nippleMsb = (byte) (0xf0 & nippleMsb);
					auxByte = (byte) (nippleMsb + nippleLsb);
					retorno[i / 2] = auxByte;
					continue;
				}
			}
		}
		return retorno;
	}

	/**
	 * Method generates the crypto key used to decipher packages. the serial number is used
	 * 
	 * @param fullSerialNumber
	 * @return
	 * @throws Exception
	 */
	public String generateCryptoKey(String fullSerialNumber) throws CryptoMd5Digest {
		byte keyByte[] = new byte[16];
		Random random = new Random();
		byte serialByte[] = charLiteralToByteHexArray(fullSerialNumber);
		int indexSn = 0;

		for (int i = 0; i < serialByte.length * 2; i++) {
			if (i % 2 == 0) {
				keyByte[i] = serialByte[indexSn++];
			} else {
				keyByte[i] = (byte) random.nextInt(255);
			}
		}
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoMd5Digest("Could not digest key: " + e.getMessage());
		}
		byte[] thedigest = md.digest(keyByte);
		// Remove '0'.
		for (int i = 0; i < thedigest.length; i++) {
			if (thedigest[i] == 0) {
				thedigest[i] = 1;
			}
		}

		// // Osmenio - 28.09.18
		// System.err.println(".......................................................................................................");
		// System.err.println("MeterController.generateCryptoKey");
		// for (int i = 0; i < thedigest.length; i++) {
		// System.err.print("" + thedigest[i] + ",");
		// }
		// System.err.println("");
		// System.err.println(".......................................................................................................");
		String key = new String(thedigest);
		return key;
	}

	public void addCalculatedFixedConsts(Set<CalculatedFixedConstModel> consts) {
		if (consts != null) {
			ArrayList<CalculatedFixedConstModel> calcConsts = new ArrayList<>(consts);// (ArrayList<CalculatedFixedConstModel>) consts;
			// calcConsts
			CalibConstantsModel constAux;
			for (int i = 0; i < calcConsts.size(); i++) {
				constAux = new CalibConstantsModel();
				constAux.setFlowRate(calcConsts.get(i).getFlowrate());
				constAux.setAvgFlowRate(calcConsts.get(i).getFlowrate());
				constAux.setAvgTemp(calcConsts.get(i).getTemp());
				constAux.setConstantK(calcConsts.get(i).getK());
				constAux.setReynolds(calcConsts.get(i).getRe());
				this.calibConstants.add(constAux);
			}

			orderCrescentByFlowRate(this.calibConstants);

			// ------------------***************************************************************************
			CalibConstantsModel lastFLowConstAux = calibConstants.get(calibConstants.size() - 1);
			CalibConstantsModel highCutOffConst = new CalibConstantsModel();
			double highCutOffFlow = this.meter.getMeterTypeModel().getHighCutoff();
			double highCutOffReynolds = calibService.flowToReynolds(highCutOffFlow, 30, meter);
			double highCutOffK = lastFLowConstAux.getConstantK(); // Set the same constant to assure flowrates are
																	// near estimated values

			highCutOffConst.setAvgFlowRate(highCutOffFlow);
			highCutOffConst.setFlowRate(highCutOffFlow);
			highCutOffConst.setAvgPressure(0);
			highCutOffConst.setAvgTemp(27);
			highCutOffConst.setReynolds(highCutOffReynolds);
			highCutOffConst.setConstantK(highCutOffK);

			// ------------------

			CalibConstantsModel endHighCutOffConst = new CalibConstantsModel();
			double endHighCutOffFlow = this.meter.getMeterTypeModel().getHighCutoff() * 1.01;
			double endHighCutOffReynolds = calibService.flowToReynolds(endHighCutOffFlow, 30, meter);
			double endHighCutOffK = 0;

			endHighCutOffConst.setAvgFlowRate(endHighCutOffFlow);
			endHighCutOffConst.setFlowRate(endHighCutOffFlow);
			endHighCutOffConst.setAvgPressure(0);
			endHighCutOffConst.setAvgTemp(27);
			endHighCutOffConst.setReynolds(endHighCutOffReynolds);
			endHighCutOffConst.setConstantK(endHighCutOffK);
			// //------------------

			calibConstants.add(highCutOffConst);
			calibConstants.add(endHighCutOffConst);
		}
	}

	public boolean saveMeterWmBusData(String serial, String criptoKey, short transInterval) {
		boolean retorno = false;

		RadioWmBusConfigModel radioConfig = new RadioWmBusConfigModel();
		radioConfig.setMeter(this.meter);
		radioConfig.setTransmitInterval(transInterval);
		radioConfig.setWmBusCryptoKey(criptoKey);
		radioConfig.setWmBusSerialNumber(serial);

		this.meter.setRadioWmBusConfig(radioConfig);

		// This configuration is realtive to the meter
		RadioWmBusConfigService radioConfigService = new RadioWmBusConfigService();

		try {
			radioConfigService.persist(radioConfig);
			retorno = true;
		} catch (CrudDatabaseException e) {
			retorno = false;
			ProcessLog.erro(LOG, "MeterController.saveMeterWmBusData()", e);
			e.printStackTrace();
		}

		return retorno;
	}

	public ArrayList<EstimatedDistanceModel> getEstimatedDistances5m(double flowrate) {
		ArrayList<EstimatedDistanceModel> estimatedDistances = new ArrayList<>();

		if (flowrate == 25.0) {

			EstimatedDistanceModel est25to2_5 = new EstimatedDistanceModel();
			est25to2_5.setDesiredFlow(2.5);
			est25to2_5.setRefFlow(25.0);
			est25to2_5.setkDistValue(0.360361543816380525129261513939);
			est25to2_5.setReDistValue(48.76309328279337052);

			EstimatedDistanceModel est25to10 = new EstimatedDistanceModel();
			est25to10.setDesiredFlow(10.0);
			est25to10.setRefFlow(25.0);
			est25to10.setkDistValue(0.172692528682891310864988554385);
			est25to10.setReDistValue(310.6811379720985542);

			EstimatedDistanceModel est25to15 = new EstimatedDistanceModel();
			est25to15.setDesiredFlow(15.0);
			est25to15.setRefFlow(25.0);
			est25to15.setkDistValue(0.106260181363397876452836499084);
			est25to15.setReDistValue(436.43598986782018301);

			EstimatedDistanceModel est25to100 = new EstimatedDistanceModel();
			est25to100.setDesiredFlow(100.0);
			est25to100.setRefFlow(25.0);
			est25to100.setkDistValue(-0.0778662058632768605548335472122);
			est25to100.setReDistValue(2358.3737819648235927);

			EstimatedDistanceModel est25to250 = new EstimatedDistanceModel();
			est25to250.setDesiredFlow(250.0);
			est25to250.setRefFlow(25.0);
			est25to250.setkDistValue(-0.145608026823143665851034711523);
			est25to250.setReDistValue(5868.2901034742362754);

			EstimatedDistanceModel est25to450 = new EstimatedDistanceModel();
			est25to450.setDesiredFlow(450.0);
			est25to450.setRefFlow(25.0);
			est25to450.setkDistValue(-0.133632812457793903604397200979);
			est25to450.setReDistValue(10703.34160979707849);

			EstimatedDistanceModel est25to700 = new EstimatedDistanceModel();
			est25to700.setDesiredFlow(700.0);
			est25to700.setRefFlow(25.0);
			est25to700.setkDistValue(-0.126289858508062069475386124395);
			est25to700.setReDistValue(16789.037180540333793);

			EstimatedDistanceModel est25to1000 = new EstimatedDistanceModel();
			est25to1000.setDesiredFlow(1000.0);
			est25to1000.setRefFlow(25.0);
			est25to1000.setkDistValue(-0.12446984650008530159936981363);
			est25to1000.setReDistValue(24004.853622014219582);

			EstimatedDistanceModel est25to1325 = new EstimatedDistanceModel();
			est25to1325.setDesiredFlow(1325.0);
			est25to1325.setRefFlow(25.0);
			est25to1325.setkDistValue(-0.126182356522527072684169979766);
			est25to1325.setReDistValue(31794.354736745055561);

			EstimatedDistanceModel est25to1500 = new EstimatedDistanceModel();
			est25to1500.setDesiredFlow(1500.0);
			est25to1500.setRefFlow(25.0);
			est25to1500.setkDistValue(-0.127559837181759450075446693518);
			est25to1500.setReDistValue(35992.237158046598779);

			EstimatedDistanceModel est25to2000 = new EstimatedDistanceModel();
			est25to2000.setDesiredFlow(2000.0);
			est25to2000.setRefFlow(25.0);
			est25to2000.setkDistValue(-0.143066322617566354935547678906);
			est25to2000.setReDistValue(47695.672876650736725);

			EstimatedDistanceModel est25to3500 = new EstimatedDistanceModel();
			est25to3500.setDesiredFlow(3500.0);
			est25to3500.setRefFlow(25.0);
			est25to3500.setkDistValue(-0.172541602035660490699342517473);
			est25to3500.setReDistValue(83013.847642659486155);

			EstimatedDistanceModel est25to4000 = new EstimatedDistanceModel();
			est25to4000.setDesiredFlow(4000.0);
			est25to4000.setRefFlow(25.0);
			est25to4000.setkDistValue(-0.173491116566285352718068679678);
			est25to4000.setReDistValue(94804.089451618187013);

			EstimatedDistanceModel est25to4500 = new EstimatedDistanceModel();
			est25to4500.setDesiredFlow(4500.0);
			est25to4500.setRefFlow(25.0);
			est25to4500.setkDistValue(-0.167639093916163162134580488782);
			est25to4500.setReDistValue(106484.32788489172526);

			EstimatedDistanceModel est25to5000 = new EstimatedDistanceModel();
			est25to5000.setDesiredFlow(5000.0);
			est25to5000.setRefFlow(25.0);
			est25to5000.setkDistValue(-0.166750766419965668063696284662);
			est25to5000.setReDistValue(118305.28548919390596);

			estimatedDistances.add(est25to2_5);
			estimatedDistances.add(est25to10);
			estimatedDistances.add(est25to15);
			estimatedDistances.add(est25to100);
			estimatedDistances.add(est25to250);
			estimatedDistances.add(est25to450);
			estimatedDistances.add(est25to700);
			estimatedDistances.add(est25to1000);
			estimatedDistances.add(est25to1325);
			estimatedDistances.add(est25to1500);
			estimatedDistances.add(est25to2000);
			estimatedDistances.add(est25to3500);
			estimatedDistances.add(est25to4500);
			estimatedDistances.add(est25to5000);

		} else {
			if (flowrate == 37.5) {
				/*****************************************************************************/
				EstimatedDistanceModel est37to2_5 = new EstimatedDistanceModel();
				est37to2_5.setDesiredFlow(2.5);
				est37to2_5.setRefFlow(37.5);
				est37to2_5.setkDistValue(0.409487072170475974175474220829);
				est37to2_5.setReDistValue(880.368773363556215372227597982);

				EstimatedDistanceModel est37to10 = new EstimatedDistanceModel();
				est37to10.setDesiredFlow(10.0);
				est37to10.setRefFlow(37.5);
				est37to10.setkDistValue(0.221818057036986759911201261275);
				est37to10.setReDistValue(618.45072867425108142924727872);

				EstimatedDistanceModel est37to15 = new EstimatedDistanceModel();
				est37to15.setDesiredFlow(15.0);
				est37to15.setRefFlow(37.5);
				est37to15.setkDistValue(0.155385709717493325499049205973);
				est37to15.setReDistValue(492.695876778529452622024109587);

				EstimatedDistanceModel est37to100 = new EstimatedDistanceModel();
				est37to100.setDesiredFlow(100.0);
				est37to100.setRefFlow(37.5);
				est37to100.setkDistValue(-0.0287406775091814115086208403227);
				est37to100.setReDistValue(-1429.24191531847395708609838039);

				EstimatedDistanceModel est37to250 = new EstimatedDistanceModel();
				est37to250.setDesiredFlow(250.0);
				est37to250.setRefFlow(37.5);
				est37to250.setkDistValue(-0.096482498469048216804822004633);
				est37to250.setReDistValue(-4939.1582368278868671040982008);

				EstimatedDistanceModel est37to450 = new EstimatedDistanceModel();
				est37to450.setDesiredFlow(450.0);
				est37to450.setRefFlow(37.5);
				est37to450.setkDistValue(-0.0845072841036984545581844940898);
				est37to450.setReDistValue(-9774.20974315072817262262105942);

				EstimatedDistanceModel est37to700 = new EstimatedDistanceModel();
				est37to700.setDesiredFlow(700.0);
				est37to700.setRefFlow(37.5);
				est37to700.setkDistValue(-0.0771643301539666204291734175058);
				est37to700.setReDistValue(-15859.9053138939834752818569541);

				EstimatedDistanceModel est37to1000 = new EstimatedDistanceModel();
				est37to1000.setDesiredFlow(1000.0);
				est37to1000.setRefFlow(37.5);
				est37to1000.setkDistValue(-0.0753443181459898525531571067404);
				est37to1000.setReDistValue(-23075.721755367871082853525877);

				EstimatedDistanceModel est37to1325 = new EstimatedDistanceModel();
				est37to1325.setDesiredFlow(1325.0);
				est37to1325.setRefFlow(37.5);
				est37to1325.setkDistValue(-0.0770568281684316236379572728765);
				est37to1325.setReDistValue(-30865.2228700987070624250918627);

				EstimatedDistanceModel est37to1500 = new EstimatedDistanceModel();
				est37to1500.setDesiredFlow(1500.0);
				est37to1500.setRefFlow(37.5);
				est37to1500.setkDistValue(-0.0784343088276640010292339866282);
				est37to1500.setReDistValue(-35063.1052914002502802759408951);

				EstimatedDistanceModel est37to2000 = new EstimatedDistanceModel();
				est37to2000.setDesiredFlow(2000.0);
				est37to2000.setRefFlow(37.5);
				est37to2000.setkDistValue(-0.0939407942634709058893349720165);
				est37to2000.setReDistValue(-46766.5410100043882266618311405);

				EstimatedDistanceModel est37to3500 = new EstimatedDistanceModel();
				est37to3500.setDesiredFlow(3500.0);
				est37to3500.setRefFlow(37.5);
				est37to3500.setkDistValue(-0.123416073681565041653129810584);
				est37to3500.setReDistValue(-82084.7157760131376562640070915);

				EstimatedDistanceModel est37to4000 = new EstimatedDistanceModel();
				est37to4000.setDesiredFlow(4000.0);
				est37to4000.setRefFlow(37.5);
				est37to4000.setkDistValue(-0.124365588212189903671855972789);
				est37to4000.setReDistValue(-93874.9575849718385143205523491);

				EstimatedDistanceModel est37to4500 = new EstimatedDistanceModel();
				est37to4500.setDesiredFlow(4500.0);
				est37to4500.setRefFlow(37.5);
				est37to4500.setkDistValue(-0.118513565562067713088367781893);
				est37to4500.setReDistValue(-105555.196018245376762934029102);

				EstimatedDistanceModel est37to5000 = new EstimatedDistanceModel();
				est37to5000.setDesiredFlow(5000.0);
				est37to5000.setRefFlow(37.5);
				est37to5000.setkDistValue(-0.117625238065870219017483577773);
				est37to5000.setReDistValue(-117376.153622547557461075484753);

				estimatedDistances.add(est37to2_5);
				estimatedDistances.add(est37to10);
				estimatedDistances.add(est37to15);
				estimatedDistances.add(est37to100);
				estimatedDistances.add(est37to250);
				estimatedDistances.add(est37to450);
				estimatedDistances.add(est37to700);
				estimatedDistances.add(est37to1000);
				estimatedDistances.add(est37to1325);
				estimatedDistances.add(est37to1500);
				estimatedDistances.add(est37to2000);
				estimatedDistances.add(est37to3500);
				estimatedDistances.add(est37to4500);
				estimatedDistances.add(est37to5000);

			} else {
				if (flowrate == 2500.0) {
					/*******************************************************************************/

					EstimatedDistanceModel est2500to2_5 = new EstimatedDistanceModel();
					est2500to2_5.setDesiredFlow(2.5);
					est2500to2_5.setRefFlow(2500);
					est2500to2_5.setkDistValue(0.503715679259109538890015755896);
					est2500to2_5.setReDistValue(59295.4652789259562268853187561);

					EstimatedDistanceModel est2500to10 = new EstimatedDistanceModel();
					est2500to10.setDesiredFlow(10.0);
					est2500to10.setRefFlow(2500);
					est2500to10.setkDistValue(0.316046664125620324625742796343);
					est2500to10.setReDistValue(59033.5472342366556404158473015);

					EstimatedDistanceModel est2500to15 = new EstimatedDistanceModel();
					est2500to15.setDesiredFlow(15.0);
					est2500to15.setRefFlow(2500);
					est2500to15.setkDistValue(0.249614316806126890213590741041);
					est2500to15.setReDistValue(58907.7923823409291799180209637);

					EstimatedDistanceModel est2500to100 = new EstimatedDistanceModel();
					est2500to100.setDesiredFlow(100.0);
					est2500to100.setRefFlow(2500);
					est2500to100.setkDistValue(0.0654879295794521532059206947451);
					est2500to100.setReDistValue(56985.8545902439291239716112614);

					EstimatedDistanceModel est2500to250 = new EstimatedDistanceModel();
					est2500to250.setDesiredFlow(250.0);
					est2500to250.setRefFlow(2500);
					est2500to250.setkDistValue(-0.00225389138041465209028046956519);
					est2500to250.setReDistValue(53475.9382687345132580958306789);

					EstimatedDistanceModel est2500to450 = new EstimatedDistanceModel();
					est2500to450.setDesiredFlow(450.0);
					est2500to450.setRefFlow(2500);
					est2500to450.setkDistValue(0.00972132298493511015635704097804);
					est2500to450.setReDistValue(48640.886762411675590556114912);

					EstimatedDistanceModel est2500to700 = new EstimatedDistanceModel();
					est2500to700.setDesiredFlow(700.0);
					est2500to700.setRefFlow(2500);
					est2500to700.setkDistValue(0.017064276934666944285368117562);
					est2500to700.setReDistValue(42555.1911916684184689074754715);

					EstimatedDistanceModel est2500to1000 = new EstimatedDistanceModel();
					est2500to1000.setDesiredFlow(1000.0);
					est2500to1000.setRefFlow(2500);
					est2500to1000.setkDistValue(0.0188842889426437121613844283274);
					est2500to1000.setReDistValue(35339.3747501945326803252100945);

					EstimatedDistanceModel est2500to1325 = new EstimatedDistanceModel();
					est2500to1325.setDesiredFlow(1325.0);
					est2500to1325.setRefFlow(2500);
					est2500to1325.setkDistValue(0.0171717789202019410765842621913);
					est2500to1325.setReDistValue(27549.8736354636967007536441088);

					EstimatedDistanceModel est2500to1500 = new EstimatedDistanceModel();
					est2500to1500.setDesiredFlow(1500.0);
					est2500to1500.setRefFlow(2500);
					est2500to1500.setkDistValue(0.0157942982609695636853075484396);
					est2500to1500.setReDistValue(23351.9912141621534829027950764);

					EstimatedDistanceModel est2500to2000 = new EstimatedDistanceModel();
					est2500to2000.setDesiredFlow(2000.0);
					est2500to2000.setRefFlow(2500);
					est2500to2000.setkDistValue(0.000287812825162658825206563051324);
					est2500to2000.setReDistValue(11648.5554955580155365169048309);

					EstimatedDistanceModel est2500to3500 = new EstimatedDistanceModel();
					est2500to3500.setDesiredFlow(3500.0);
					est2500to3500.setRefFlow(2500);
					est2500to3500.setkDistValue(-0.0291874665929314769385882755159);
					est2500to3500.setReDistValue(-23669.6192704507338930852711201);

					EstimatedDistanceModel est2500to4000 = new EstimatedDistanceModel();
					est2500to4000.setDesiredFlow(4000.0);
					est2500to4000.setRefFlow(2500);
					est2500to4000.setkDistValue(-0.0301369811235563389573144377209);
					est2500to4000.setReDistValue(-35459.8610794094347511418163776);

					EstimatedDistanceModel est2500to4500 = new EstimatedDistanceModel();
					est2500to4500.setDesiredFlow(4500.0);
					est2500to4500.setRefFlow(2500);
					est2500to4500.setkDistValue(-0.024284958473434148373826246825);
					est2500to4500.setReDistValue(-47140.0995126829729997552931309);

					EstimatedDistanceModel est2500to5000 = new EstimatedDistanceModel();
					est2500to5000.setDesiredFlow(5000.0);
					est2500to5000.setRefFlow(37.5);
					est2500to5000.setkDistValue(-0.0233966309772366543029420427047);
					est2500to5000.setReDistValue(-58961.0571169851536978967487812);

					estimatedDistances.add(est2500to2_5);
					estimatedDistances.add(est2500to10);
					estimatedDistances.add(est2500to15);
					estimatedDistances.add(est2500to100);
					estimatedDistances.add(est2500to250);
					estimatedDistances.add(est2500to450);
					estimatedDistances.add(est2500to700);
					estimatedDistances.add(est2500to1000);
					estimatedDistances.add(est2500to1325);
					estimatedDistances.add(est2500to1500);
					estimatedDistances.add(est2500to2000);
					estimatedDistances.add(est2500to3500);
					estimatedDistances.add(est2500to4500);
					estimatedDistances.add(est2500to5000);
				}
			}
		}
		return estimatedDistances;
	}

	public ArrayList<EstimatedDistanceModel> getEstimatedDistances3mIdm(double flowrate) {

		ArrayList<EstimatedDistanceModel> estimatedDistances = new ArrayList<>();

		if (flowrate == 15.0) {

			// 6.25
			EstimatedDistanceModel est15to6_25 = new EstimatedDistanceModel();
			est15to6_25.setDesiredFlow(6.25);
			est15to6_25.setRefFlow(15.0);
			est15to6_25.setkDistValue(0.163945790359805831926109931374);
			est15to6_25.setReDistValue(196.1833630389647567);

			// 10
			EstimatedDistanceModel est15to10 = new EstimatedDistanceModel();
			est15to10.setDesiredFlow(10.0);
			est15to10.setRefFlow(15.0);
			est15to10.setkDistValue(0.0771361617627874807112675625831);
			est15to10.setReDistValue(294.98978697099664714);

			// 40
			EstimatedDistanceModel est15to40 = new EstimatedDistanceModel();
			est15to40.setDesiredFlow(40.0);
			est15to40.setRefFlow(15.0);
			est15to40.setkDistValue(-0.17711382689895938113977535977);
			est15to40.setReDistValue(978.22514489600087018);

			// 100
			EstimatedDistanceModel est15to100 = new EstimatedDistanceModel();
			est15to100.setDesiredFlow(100.0);
			est15to100.setRefFlow(15.0);
			est15to100.setkDistValue(-0.262760819854100047265887951653);
			est15to100.setReDistValue(2272.1970743889105506);

			// 250
			EstimatedDistanceModel est15to250 = new EstimatedDistanceModel();
			est15to250.setDesiredFlow(250.0);
			est15to250.setRefFlow(15.0);
			est15to250.setkDistValue(-0.258309102700400083918452764919);
			est15to250.setReDistValue(5710.9502738315331953);

			// 450
			EstimatedDistanceModel est15to450 = new EstimatedDistanceModel();
			est15to450.setDesiredFlow(450.0);
			est15to450.setRefFlow(15.0);
			est15to450.setkDistValue(-0.257514249580558018948295284645);
			est15to450.setReDistValue(10268.75773583784212);

			// 700
			EstimatedDistanceModel est15to700 = new EstimatedDistanceModel();
			est15to700.setDesiredFlow(700.0);
			est15to700.setRefFlow(15.0);
			est15to700.setkDistValue(-0.256554602821133626022742646455);
			est15to700.setReDistValue(15947.857196147748255);

			// 1325
			EstimatedDistanceModel est15to1325 = new EstimatedDistanceModel();
			est15to1325.setDesiredFlow(1325);
			est15to1325.setRefFlow(15.0);
			est15to1325.setkDistValue(-0.257589448934897546905631315894);
			est15to1325.setReDistValue(30156.852255765526934);

			// 2000
			EstimatedDistanceModel est15to2000 = new EstimatedDistanceModel();
			est15to2000.setDesiredFlow(2000.0);
			est15to2000.setRefFlow(15.0);
			est15to2000.setkDistValue(-0.271927718538504725742654954956);
			est15to2000.setReDistValue(45373.136403139578761);

			// 3000
			EstimatedDistanceModel est15to3000 = new EstimatedDistanceModel();
			est15to3000.setDesiredFlow(3000.0);
			est15to3000.setRefFlow(15.0);
			est15to3000.setkDistValue(-0.26808681355660612943836440536);
			est15to3000.setReDistValue(67886.370147382040159);

			estimatedDistances.add(est15to6_25);
			estimatedDistances.add(est15to10);
			estimatedDistances.add(est15to40);
			estimatedDistances.add(est15to100);
			estimatedDistances.add(est15to250);
			estimatedDistances.add(est15to450);
			estimatedDistances.add(est15to700);
			estimatedDistances.add(est15to1325);
			estimatedDistances.add(est15to2000);
			estimatedDistances.add(est15to3000);

		} else {
			if (flowrate == 22.5) {
				/*****************************************************************************/

				// 2.5
				EstimatedDistanceModel est22to6_25 = new EstimatedDistanceModel();
				est22to6_25.setDesiredFlow(6.25);
				est22to6_25.setRefFlow(22.5);
				est22to6_25.setkDistValue(0.261147775703859608142920478713);

				// 10
				EstimatedDistanceModel est22to10 = new EstimatedDistanceModel();
				est22to10.setDesiredFlow(10.0);
				est22to10.setRefFlow(22.5);
				est22to10.setkDistValue(0.174338147106841256928078109922);

				// 40
				EstimatedDistanceModel est22to40 = new EstimatedDistanceModel();
				est22to40.setDesiredFlow(37.5);
				est22to40.setRefFlow(22.5);
				est22to40.setkDistValue(-0.079911841554905604922964812431);

				// 100
				EstimatedDistanceModel est22to100 = new EstimatedDistanceModel();
				est22to100.setDesiredFlow(100.0);
				est22to100.setRefFlow(22.5);
				est22to100.setkDistValue(-0.165558834510046271049077404314);

				// 250
				EstimatedDistanceModel est22to250 = new EstimatedDistanceModel();
				est22to250.setDesiredFlow(250.0);
				est22to250.setRefFlow(22.5);
				est22to250.setkDistValue(-0.16110711735634630770164221758);

				// 450
				EstimatedDistanceModel est22to450 = new EstimatedDistanceModel();
				est22to450.setDesiredFlow(450.0);
				est22to450.setRefFlow(22.5);
				est22to450.setkDistValue(-0.160312264236504242731484737305);

				// 700
				EstimatedDistanceModel est22to700 = new EstimatedDistanceModel();
				est22to700.setDesiredFlow(700.0);
				est22to700.setRefFlow(22.5);
				est22to700.setkDistValue(-0.159352617477079849805932099116);

				// 1325
				EstimatedDistanceModel est22to1325 = new EstimatedDistanceModel();
				est22to1325.setDesiredFlow(1325);
				est22to1325.setRefFlow(22.5);
				est22to1325.setkDistValue(-0.160387463590843770688820768555);

				// 2000
				EstimatedDistanceModel est22to2000 = new EstimatedDistanceModel();
				est22to2000.setDesiredFlow(2000.0);
				est22to2000.setRefFlow(22.5);
				est22to2000.setkDistValue(-0.174725733194450949525844407617);

				// 3000
				EstimatedDistanceModel est22to3000 = new EstimatedDistanceModel();
				est22to3000.setDesiredFlow(3000.0);
				est22to3000.setRefFlow(22.5);
				est22to3000.setkDistValue(-0.17088482821255235322155385802);

				estimatedDistances.add(est22to6_25);
				estimatedDistances.add(est22to10);
				estimatedDistances.add(est22to40);
				estimatedDistances.add(est22to100);
				estimatedDistances.add(est22to250);
				estimatedDistances.add(est22to450);
				estimatedDistances.add(est22to700);
				estimatedDistances.add(est22to1325);
				estimatedDistances.add(est22to2000);
				estimatedDistances.add(est22to3000);

			} else {
				if (flowrate == 1500.0) {
					/*******************************************************************************/

					// 6.25
					EstimatedDistanceModel est1500to6_25 = new EstimatedDistanceModel();
					est1500to6_25.setDesiredFlow(6.25);
					est1500to6_25.setRefFlow(1500.0);
					est1500to6_25.setkDistValue(0.421492253093355362381089435075);

					// 10
					EstimatedDistanceModel est1500to10 = new EstimatedDistanceModel();
					est1500to10.setDesiredFlow(10.0);
					est1500to10.setRefFlow(1500.0);
					est1500to10.setkDistValue(0.334682624496337011166247066285);

					// 40
					EstimatedDistanceModel est1500to40 = new EstimatedDistanceModel();
					est1500to40.setDesiredFlow(37.5);
					est1500to40.setRefFlow(1500.0);
					est1500to40.setkDistValue(0.0804326358345901493152041439316);

					// 100
					EstimatedDistanceModel est1500to100 = new EstimatedDistanceModel();
					est1500to100.setDesiredFlow(100.0);
					est1500to100.setRefFlow(1500.0);
					est1500to100.setkDistValue(-0.00521435712055051681090844795108);

					// 250
					EstimatedDistanceModel est1500to250 = new EstimatedDistanceModel();
					est1500to250.setDesiredFlow(250.0);
					est1500to250.setRefFlow(1500.0);
					est1500to250.setkDistValue(-0.000762639966850553463473261217587);

					// 450
					EstimatedDistanceModel est1500to450 = new EstimatedDistanceModel();
					est1500to450.setDesiredFlow(450.0);
					est1500to450.setRefFlow(1500.0);
					est1500to450.setkDistValue(3.22131529915115066842190572061e-05);

					// 700
					EstimatedDistanceModel est1500to700 = new EstimatedDistanceModel();
					est1500to700.setDesiredFlow(700.0);
					est1500to700.setRefFlow(1500.0);
					est1500to700.setkDistValue(0.000991859912415904432236857246608);

					// 1325
					EstimatedDistanceModel est1500to1325 = new EstimatedDistanceModel();
					est1500to1325.setDesiredFlow(1325);
					est1500to1325.setRefFlow(1500.0);
					est1500to1325.setkDistValue(-4.29862013480164506518121925183e-05);

					// 2000
					EstimatedDistanceModel est1500to2000 = new EstimatedDistanceModel();
					est1500to2000.setDesiredFlow(2000.0);
					est1500to2000.setRefFlow(1500.0);
					est1500to2000.setkDistValue(-0.0143812558049551952876754512545);

					// 3000
					EstimatedDistanceModel est1500to3000 = new EstimatedDistanceModel();
					est1500to3000.setDesiredFlow(3000.0);
					est1500to3000.setRefFlow(1500.0);
					est1500to3000.setkDistValue(-0.0105403508230565989833849016577);

					estimatedDistances.add(est1500to6_25);
					estimatedDistances.add(est1500to10);
					estimatedDistances.add(est1500to40);
					estimatedDistances.add(est1500to100);
					estimatedDistances.add(est1500to250);
					estimatedDistances.add(est1500to450);
					estimatedDistances.add(est1500to700);
					estimatedDistances.add(est1500to1325);
					estimatedDistances.add(est1500to2000);
					estimatedDistances.add(est1500to3000);
				}
			}
		}
		return estimatedDistances;
	}

	public ArrayList<EstimatedDistanceModel> getEstimatedDistances3mIdm2(double flowrate) {

		ArrayList<EstimatedDistanceModel> estimatedDistances = new ArrayList<>();

		if (flowrate == 10.0) {
			// 2.5
			EstimatedDistanceModel est10to2_5 = new EstimatedDistanceModel();
			est10to2_5.setDesiredFlow(2.5);
			est10to2_5.setRefFlow(10.0);
			est10to2_5.setkDistValue(-0.1492736486611983);
			est10to2_5.setReDistValue(-200.87866482115908);
			estimatedDistances.add(est10to2_5);
			// 5
			EstimatedDistanceModel est10to5 = new EstimatedDistanceModel();
			est10to5.setDesiredFlow(5.0);
			est10to5.setRefFlow(10.0);
			est10to5.setkDistValue(-0.08238667122301035);
			est10to5.setReDistValue(-130.2349466894281);
			estimatedDistances.add(est10to5);
			// 10
			// EstimatedDistanceModel est10to15 = new EstimatedDistanceModel();
			// est10to15.setDesiredFlow(15.0);
			// est10to15.setRefFlow(10.0);
			// est10to15.setkDistValue(0.059507545925707033829);
			// est10to15.setReDistValue(102.99204902878454959);

			// 40
			EstimatedDistanceModel est10to40 = new EstimatedDistanceModel();
			est10to40.setDesiredFlow(40.0);
			est10to40.setRefFlow(10.0);
			est10to40.setkDistValue(0.18503861971778268);
			est10to40.setReDistValue(647.965443696404);
			estimatedDistances.add(est10to40);

			// 100
			EstimatedDistanceModel est10to100 = new EstimatedDistanceModel();
			est10to100.setDesiredFlow(100.0);
			est10to100.setRefFlow(10.0);
			est10to100.setkDistValue(0.2745570451630772);
			est10to100.setReDistValue(1807.5150834638046);
			estimatedDistances.add(est10to100);

			// 250
			EstimatedDistanceModel est10to250 = new EstimatedDistanceModel();
			est10to250.setDesiredFlow(250.0);
			est10to250.setRefFlow(10.0);
			est10to250.setkDistValue(0.2965440691277177);
			est10to250.setReDistValue(5099.371398930744);
			estimatedDistances.add(est10to250);

			// 450
			EstimatedDistanceModel est10to450 = new EstimatedDistanceModel();
			est10to450.setDesiredFlow(450.0);
			est10to450.setRefFlow(10.0);
			est10to450.setkDistValue(0.31605533278878206);
			est10to450.setReDistValue(9444.448849097238);
			estimatedDistances.add(est10to450);

			// 700
			EstimatedDistanceModel est10to700 = new EstimatedDistanceModel();
			est10to700.setDesiredFlow(700.0);
			est10to700.setRefFlow(10.0);
			est10to700.setkDistValue(0.3280587149250167);
			est10to700.setReDistValue(14870.457060726942);
			estimatedDistances.add(est10to700);

			// 1000
			EstimatedDistanceModel est10to1000 = new EstimatedDistanceModel();
			est10to1000.setDesiredFlow(1000);
			est10to1000.setRefFlow(10.0);
			est10to1000.setkDistValue(0.35099403008475454913);
			est10to1000.setReDistValue(22954.002263653019327);
			estimatedDistances.add(est10to1000);

			// 1500
			EstimatedDistanceModel est10to1500 = new EstimatedDistanceModel();
			est10to1500.setDesiredFlow(1500);
			est10to1500.setRefFlow(10.0);
			est10to1500.setkDistValue(0.33341182976459427);
			est10to1500.setReDistValue(32217.208138621976);
			estimatedDistances.add(est10to1500);

			// 2000
			EstimatedDistanceModel est10to2000 = new EstimatedDistanceModel();
			est10to2000.setDesiredFlow(2000.0);
			est10to2000.setRefFlow(10.0);
			est10to2000.setkDistValue(0.3531121291405055);
			est10to2000.setReDistValue(42858.54936613275);
			estimatedDistances.add(est10to2000);

			// 2500
			EstimatedDistanceModel est10to2500 = new EstimatedDistanceModel();
			est10to2500.setDesiredFlow(2500.0);
			est10to2500.setRefFlow(10.0);
			est10to2500.setkDistValue(0.3508026508066413);
			est10to2500.setReDistValue(53536.146706877764);
			estimatedDistances.add(est10to2500);

			// 3000
			EstimatedDistanceModel est10to3000 = new EstimatedDistanceModel();
			est10to3000.setDesiredFlow(3000.0);
			est10to3000.setRefFlow(10.0);
			est10to3000.setkDistValue(0.37049027308146987);
			est10to3000.setReDistValue(63587.09578022174);
			estimatedDistances.add(est10to3000);

		} else {
			if (flowrate == 22.5) {
				/*****************************************************************************/

				// 2.5
				EstimatedDistanceModel est22to2_5 = new EstimatedDistanceModel();
				est22to2_5.setDesiredFlow(2.5);
				est22to2_5.setRefFlow(22.5);
				est22to2_5.setkDistValue(-0.3285481247080506);
				est22to2_5.setReDistValue(-474.1179314920669);
				estimatedDistances.add(est22to2_5);

				// 5
				EstimatedDistanceModel est22to5 = new EstimatedDistanceModel();
				est22to5.setDesiredFlow(5.0);
				est22to5.setRefFlow(22.5);
				est22to5.setkDistValue(-0.26166114726986267);
				est22to5.setReDistValue(-403.4742133603359);
				estimatedDistances.add(est22to5);

				// //10
				// EstimatedDistanceModel est22to15 = new EstimatedDistanceModel();
				// est22to15.setDesiredFlow(15.0);
				// est22to15.setRefFlow(22.5);
				// est22to15.setkDistValue(0.26257644120452683367);
				// est22to15.setReDistValue(-168.38010510303666933);

				// 40
				EstimatedDistanceModel est22to40 = new EstimatedDistanceModel();
				est22to40.setDesiredFlow(40.0);
				est22to40.setRefFlow(22.5);
				est22to40.setkDistValue(0.005764143670930366);
				est22to40.setReDistValue(374.7261770254962);
				estimatedDistances.add(est22to40);

				// 100
				EstimatedDistanceModel est22to100 = new EstimatedDistanceModel();
				est22to100.setDesiredFlow(100.0);
				est22to100.setRefFlow(22.5);
				est22to100.setkDistValue(0.0952825691162249);
				est22to100.setReDistValue(1534.2758167928969);
				estimatedDistances.add(est22to100);

				// 250
				EstimatedDistanceModel est22to250 = new EstimatedDistanceModel();
				est22to250.setDesiredFlow(250.0);
				est22to250.setRefFlow(22.5);
				est22to250.setkDistValue(0.11726959308086538);
				est22to250.setReDistValue(4826.132132259836);
				estimatedDistances.add(est22to250);

				// 450
				EstimatedDistanceModel est22to450 = new EstimatedDistanceModel();
				est22to450.setDesiredFlow(450.0);
				est22to450.setRefFlow(22.5);
				est22to450.setkDistValue(0.13678085674192975);
				est22to450.setReDistValue(9171.20958242633);
				estimatedDistances.add(est22to450);

				// 700
				EstimatedDistanceModel est22to700 = new EstimatedDistanceModel();
				est22to700.setDesiredFlow(700.0);
				est22to700.setRefFlow(22.5);
				est22to700.setkDistValue(0.1487842388781644);
				est22to700.setReDistValue(14597.217794056034);
				estimatedDistances.add(est22to700);

				// 1000
				EstimatedDistanceModel est22to1000 = new EstimatedDistanceModel();
				est22to1000.setDesiredFlow(1000);
				est22to1000.setRefFlow(22.5);
				est22to1000.setkDistValue(0.14792518847629748668);
				est22to1000.setReDistValue(22682.630109521200211);
				estimatedDistances.add(est22to1000);

				// 1500
				EstimatedDistanceModel est22to1500 = new EstimatedDistanceModel();
				est22to1500.setDesiredFlow(1500);
				est22to1500.setRefFlow(22.5);
				est22to1500.setkDistValue(0.15413735371774195);
				est22to1500.setReDistValue(31943.96887195107);
				estimatedDistances.add(est22to1500);

				// 2000
				EstimatedDistanceModel est22to2000 = new EstimatedDistanceModel();
				est22to2000.setDesiredFlow(2000.0);
				est22to2000.setRefFlow(22.5);
				est22to2000.setkDistValue(0.17383759352247349361);
				est22to2000.setReDistValue(42585.310099461836217);
				estimatedDistances.add(est22to2000);

				// 2500
				EstimatedDistanceModel est22to2500 = new EstimatedDistanceModel();
				est22to2500.setDesiredFlow(2500.0);
				est22to2500.setRefFlow(22.5);
				est22to2500.setkDistValue(0.171528174759789);
				est22to2500.setReDistValue(53262.90744020685);
				estimatedDistances.add(est22to2500);

				// 3000
				EstimatedDistanceModel est22to3000 = new EstimatedDistanceModel();
				est22to3000.setDesiredFlow(3000.0);
				est22to3000.setRefFlow(22.5);
				est22to3000.setkDistValue(0.19121579703461755);
				est22to3000.setReDistValue(63313.856513550825);
				estimatedDistances.add(est22to3000);

			} else {
				if (flowrate == 1325.0) {
					/*******************************************************************************/

					// 2.5
					EstimatedDistanceModel est1325to2_5 = new EstimatedDistanceModel();
					est1325to2_5.setDesiredFlow(2.5);
					est1325to2_5.setRefFlow(1325.0);
					est1325to2_5.setkDistValue(-0.4851228538955732);
					est1325to2_5.setReDistValue(-28407.36336600489);
					estimatedDistances.add(est1325to2_5);

					// 5
					EstimatedDistanceModel est1325to5 = new EstimatedDistanceModel();
					est1325to5.setDesiredFlow(5.0);
					est1325to5.setRefFlow(1325.0);
					est1325to5.setkDistValue(-0.41823587645738525);
					est1325to5.setReDistValue(-28336.71964787316);
					estimatedDistances.add(est1325to5);

					// //10
					// EstimatedDistanceModel est1325to15 = new EstimatedDistanceModel();
					// est1325to15.setDesiredFlow(15.0);
					// est1325to15.setRefFlow(1325.0);
					// est1325to15.setkDistValue(0.41915117038900628899);
					// est1325to15.setReDistValue(-28101.625539615859452);
					// estimatedDistances.add(est22to3000);

					// 40
					EstimatedDistanceModel est1325to40 = new EstimatedDistanceModel();
					est1325to40.setDesiredFlow(40.0);
					est1325to40.setRefFlow(1325.0);
					est1325to40.setkDistValue(-0.1508105855165922);
					est1325to40.setReDistValue(-27558.51925748733);
					estimatedDistances.add(est1325to40);

					// 100
					EstimatedDistanceModel est1325to100 = new EstimatedDistanceModel();
					est1325to100.setDesiredFlow(100.0);
					est1325to100.setRefFlow(1325.0);
					est1325to100.setkDistValue(-0.061292160071297674);
					est1325to100.setReDistValue(-26398.969617719926);
					estimatedDistances.add(est1325to100);

					// 250
					EstimatedDistanceModel est1325to250 = new EstimatedDistanceModel();
					est1325to250.setDesiredFlow(250.0);
					est1325to250.setRefFlow(1325.0);
					est1325to250.setkDistValue(-0.0393051361066572);
					est1325to250.setReDistValue(-23107.113302252987);
					estimatedDistances.add(est1325to250);

					// 450
					EstimatedDistanceModel est1325to450 = new EstimatedDistanceModel();
					est1325to450.setDesiredFlow(450.0);
					est1325to450.setRefFlow(1325.0);
					est1325to450.setkDistValue(-0.01979387244559283);
					est1325to450.setReDistValue(-18762.035852086494);
					estimatedDistances.add(est1325to450);

					// 700
					EstimatedDistanceModel est1325to700 = new EstimatedDistanceModel();
					est1325to700.setDesiredFlow(700.0);
					est1325to700.setRefFlow(1325.0);
					est1325to700.setkDistValue(-0.007790490309358189);
					est1325to700.setReDistValue(-13336.02764045679);
					estimatedDistances.add(est1325to700);

					// 1000
					EstimatedDistanceModel est1325to1000 = new EstimatedDistanceModel();
					est1325to1000.setDesiredFlow(1000);
					est1325to1000.setRefFlow(1325);
					est1325to1000.setkDistValue(0.0086495254421204773032);
					est1325to1000.setReDistValue(-5250.6153249916242203);
					estimatedDistances.add(est1325to1000);

					// 1500
					EstimatedDistanceModel est1325to1500 = new EstimatedDistanceModel();
					est1325to1500.setDesiredFlow(1500);
					est1325to1500.setRefFlow(1325);
					est1325to1500.setkDistValue(-0.0024373754697806227);
					est1325to1500.setReDistValue(4010.723437438246);
					estimatedDistances.add(est1325to1500);

					// 2000
					EstimatedDistanceModel est1325to2000 = new EstimatedDistanceModel();
					est1325to2000.setDesiredFlow(2000.0);
					est1325to2000.setRefFlow(1325.0);
					est1325to2000.setkDistValue(0.0172629239061306);
					est1325to2000.setReDistValue(14652.064664949015);
					estimatedDistances.add(est1325to2000);

					// 2500
					EstimatedDistanceModel est1325to2500 = new EstimatedDistanceModel();
					est1325to2500.setDesiredFlow(2500.0);
					est1325to2500.setRefFlow(1325);
					est1325to2500.setkDistValue(0.01495344557226641);
					est1325to2500.setReDistValue(25329.66200569403);
					estimatedDistances.add(est1325to2500);

					// 3000
					EstimatedDistanceModel est1325to3000 = new EstimatedDistanceModel();
					est1325to3000.setDesiredFlow(3000.0);
					est1325to3000.setRefFlow(1325);
					est1325to3000.setkDistValue(0.034641067847094975);
					est1325to3000.setReDistValue(35380.611079038004);
					estimatedDistances.add(est1325to3000);

				}
			}
		}
		return estimatedDistances;
	}

	public ArrayList<EstimatedDistanceModel> getEstimatedDistances3mIdm2_doublePosition(double flowrate) {

		ArrayList<EstimatedDistanceModel> estimatedDistances = new ArrayList<>();

		if (flowrate == 22.5) {
			/*****************************************************************************/

			// 2.5
			EstimatedDistanceModel est22to2_5 = new EstimatedDistanceModel();
			est22to2_5.setDesiredFlow(2.5);
			est22to2_5.setRefFlow(22.5);
			est22to2_5.setkDistValue(0.32854812470802785551);
			est22to2_5.setReDistValue(-474.11793149206687303);
			estimatedDistances.add(est22to2_5);

			// 5
			EstimatedDistanceModel est22to5 = new EstimatedDistanceModel();
			est22to5.setDesiredFlow(5.0);
			est22to5.setRefFlow(22.5);
			est22to5.setkDistValue(0.26166114726997669049);
			est22to5.setReDistValue(-403.47421336033596617);
			estimatedDistances.add(est22to5);

			// 10
			EstimatedDistanceModel est22to10 = new EstimatedDistanceModel();
			est22to10.setDesiredFlow(10.0);
			est22to10.setRefFlow(22.5);
			est22to10.setkDistValue(0.17927447604681792659);
			est22to10.setReDistValue(-273.23926667090785259);
			estimatedDistances.add(est22to10);

			// 40
			EstimatedDistanceModel est22to40 = new EstimatedDistanceModel();
			est22to40.setDesiredFlow(40.0);
			est22to40.setRefFlow(22.5);
			est22to40.setkDistValue(0.0057641425862468998603);
			est22to40.setReDistValue(374.72617702549621299);
			estimatedDistances.add(est22to40);

			// 100
			EstimatedDistanceModel est22to100 = new EstimatedDistanceModel();
			est22to100.setDesiredFlow(100.0);
			est22to100.setRefFlow(22.5);
			est22to100.setkDistValue(0.095282569669405930979);
			est22to100.setReDistValue(1534.275816792896876);
			estimatedDistances.add(est22to100);

			// 250
			EstimatedDistanceModel est22to250 = new EstimatedDistanceModel();
			est22to250.setDesiredFlow(250.0);
			est22to250.setRefFlow(22.5);
			est22to250.setkDistValue(0.11726959653813427698);
			est22to250.setReDistValue(4826.1321322598359984);
			estimatedDistances.add(est22to250);

			// 450
			EstimatedDistanceModel est22to450 = new EstimatedDistanceModel();
			est22to450.setDesiredFlow(450.0);
			est22to450.setRefFlow(22.5);
			est22to450.setkDistValue(0.13678085767887190882);
			est22to450.setReDistValue(9171.2095824263305985);
			estimatedDistances.add(est22to450);

			// 700
			EstimatedDistanceModel est22to700 = new EstimatedDistanceModel();
			est22to700.setDesiredFlow(700.0);
			est22to700.setRefFlow(22.5);
			est22to700.setkDistValue(0.1487842558259747372);
			est22to700.setReDistValue(14597.217794056034109);
			estimatedDistances.add(est22to700);

			// 1000
			EstimatedDistanceModel est22to1000 = new EstimatedDistanceModel();
			est22to1000.setDesiredFlow(1000);
			est22to1000.setRefFlow(22.5);
			est22to1000.setkDistValue(0.14792518847629748668);
			est22to1000.setReDistValue(22682.630109521200211);
			estimatedDistances.add(est22to1000);

			// 1500
			EstimatedDistanceModel est22to1500 = new EstimatedDistanceModel();
			est22to1500.setDesiredFlow(1500);
			est22to1500.setRefFlow(22.5);
			est22to1500.setkDistValue(0.15413728352832037349);
			est22to1500.setReDistValue(31943.968871951070469);
			estimatedDistances.add(est22to1500);

			// 2000
			EstimatedDistanceModel est22to2000 = new EstimatedDistanceModel();
			est22to2000.setDesiredFlow(2000.0);
			est22to2000.setRefFlow(22.5);
			est22to2000.setkDistValue(0.17383759352247349361);
			est22to2000.setReDistValue(42585.310099461836217);
			estimatedDistances.add(est22to2000);

			// 2500
			EstimatedDistanceModel est22to2500 = new EstimatedDistanceModel();
			est22to2500.setDesiredFlow(2500.0);
			est22to2500.setRefFlow(22.5);
			est22to2500.setkDistValue(0.17152837323899605693);
			est22to2500.setReDistValue(53262.907440206850879);
			estimatedDistances.add(est22to2500);

			// 3000
			EstimatedDistanceModel est22to3000 = new EstimatedDistanceModel();
			est22to3000.setDesiredFlow(3000.0);
			est22to3000.setRefFlow(22.5);
			est22to3000.setkDistValue(0.19121569031249607828);
			est22to3000.setReDistValue(63313.856513550825184);
			estimatedDistances.add(est22to3000);

		} else {
			if (flowrate == 1325.0) {
				/*******************************************************************************/

				// 2.5
				EstimatedDistanceModel est1325to2_5 = new EstimatedDistanceModel();
				est1325to2_5.setDesiredFlow(2.5);
				est1325to2_5.setRefFlow(1325.0);
				est1325to2_5.setkDistValue(0.48512285389647991085);
				est1325to2_5.setReDistValue(-28407.36336600489085);
				estimatedDistances.add(est1325to2_5);

				// 5
				EstimatedDistanceModel est1325to5 = new EstimatedDistanceModel();
				est1325to5.setDesiredFlow(5.0);
				est1325to5.setRefFlow(1325.0);
				est1325to5.setkDistValue(0.4182358764572567944);
				est1325to5.setReDistValue(-28336.719647873160284);
				estimatedDistances.add(est1325to5);

				// 10
				EstimatedDistanceModel est1325to10 = new EstimatedDistanceModel();
				est1325to10.setDesiredFlow(10.0);
				est1325to10.setRefFlow(1325.0);
				est1325to10.setkDistValue(0.33584920522715006053);
				est1325to10.setReDistValue(-28206.484701183726429);
				estimatedDistances.add(est1325to10);

				// 40
				EstimatedDistanceModel est1325to40 = new EstimatedDistanceModel();
				est1325to40.setDesiredFlow(40.0);
				est1325to40.setRefFlow(1325.0);
				est1325to40.setkDistValue(0.15081058551391193778);
				est1325to40.setReDistValue(-27558.519257487329014);
				estimatedDistances.add(est1325to40);

				// 100
				EstimatedDistanceModel est1325to100 = new EstimatedDistanceModel();
				est1325to100.setDesiredFlow(100.0);
				est1325to100.setRefFlow(1325.0);
				est1325to100.setkDistValue(0.06129216008148288819);
				est1325to100.setReDistValue(-26398.969617719925736);
				estimatedDistances.add(est1325to100);

				// 250
				EstimatedDistanceModel est1325to250 = new EstimatedDistanceModel();
				est1325to250.setDesiredFlow(250.0);
				est1325to250.setRefFlow(1325.0);
				est1325to250.setkDistValue(0.039305136107151340819);
				est1325to250.setReDistValue(-23107.113302252986614);
				estimatedDistances.add(est1325to250);

				// 450
				EstimatedDistanceModel est1325to450 = new EstimatedDistanceModel();
				est1325to450.setDesiredFlow(450.0);
				est1325to450.setRefFlow(1325.0);
				est1325to450.setkDistValue(0.019793872442843463838);
				est1325to450.setReDistValue(-18762.035852086493833);
				estimatedDistances.add(est1325to450);

				// 700
				EstimatedDistanceModel est1325to700 = new EstimatedDistanceModel();
				est1325to700.setDesiredFlow(700.0);
				est1325to700.setRefFlow(1325.0);
				est1325to700.setkDistValue(0.0077904903116180583555);
				est1325to700.setReDistValue(-13336.027640456790323);
				estimatedDistances.add(est1325to700);

				// 1000
				EstimatedDistanceModel est1325to1000 = new EstimatedDistanceModel();
				est1325to1000.setDesiredFlow(1000);
				est1325to1000.setRefFlow(1325);
				est1325to1000.setkDistValue(0.0086495254421204773032);
				est1325to1000.setReDistValue(-5250.6153249916242203);
				estimatedDistances.add(est1325to1000);

				// 1500
				EstimatedDistanceModel est1325to1500 = new EstimatedDistanceModel();
				est1325to1500.setDesiredFlow(1500);
				est1325to1500.setRefFlow(1325);
				est1325to1500.setkDistValue(0.0024371875629895585653);
				est1325to1500.setReDistValue(4010.7234374382460373);
				estimatedDistances.add(est1325to1500);

				// 2000
				EstimatedDistanceModel est1325to2000 = new EstimatedDistanceModel();
				est1325to2000.setDesiredFlow(2000.0);
				est1325to2000.setRefFlow(1325.0);
				est1325to2000.setkDistValue(0.017263483105539653012);
				est1325to2000.setReDistValue(14652.064664949015423);
				estimatedDistances.add(est1325to2000);

				// 2500
				EstimatedDistanceModel est1325to2500 = new EstimatedDistanceModel();
				est1325to2500.setDesiredFlow(2500.0);
				est1325to2500.setRefFlow(1325);
				est1325to2500.setkDistValue(0.014950681140864606825);
				est1325to2500.setReDistValue(25329.662005694030086);
				estimatedDistances.add(est1325to2500);

				// 3000
				EstimatedDistanceModel est1325to3000 = new EstimatedDistanceModel();
				est1325to3000.setDesiredFlow(3000.0);
				est1325to3000.setRefFlow(1325);
				est1325to3000.setkDistValue(0.034643898802770432821);
				est1325to3000.setReDistValue(35380.611079038004391);
				estimatedDistances.add(est1325to3000);

			}
		}

		return estimatedDistances;
	}

	
	public ArrayList<EstimatedDistanceModel> getEstimatedDistances2_5m(double flowrate){
		
		List<DistancePointsModel> distancePoints = new ArrayList<>();
		
		
		
		
		ArrayList<EstimatedDistanceModel> estimatedDistances = new ArrayList<>();

		if (flowrate == 6.25) {
			/*****************************************************************************/

//			// 2.5
//			EstimatedDistanceModel est6to2_5 = new EstimatedDistanceModel();
//			est6to2_5.setDesiredFlow(2.5);
//			est6to2_5.setRefFlow(6.25);
//			est6to2_5.setkDistValue(0.32854812470802785551);
//			est6to2_5.setReDistValue(-474.11793149206687303);
//			estimatedDistances.add(est6to2_5);

			// 15
			EstimatedDistanceModel est6to15 = new EstimatedDistanceModel();
			est6to15.setDesiredFlow(15.0);
			est6to15.setRefFlow(6.25);
			est6to15.setkDistValue(-0.145309140114765478);
			est6to15.setReDistValue(-235.426934950085979);
			est6to15.setAvgReynolds(450.1368063119168);
			estimatedDistances.add(est6to15);
			
			// 20
			EstimatedDistanceModel est6to20 = new EstimatedDistanceModel();
			est6to20.setDesiredFlow(20.0);
			est6to20.setRefFlow(6.25);
			est6to20.setkDistValue(-0.195854749031608044);
			est6to20.setReDistValue(-359.501778009972725);
			est6to20.setAvgReynolds(574.2116493718036);
			estimatedDistances.add(est6to20);

			// 35
			EstimatedDistanceModel est6to35 = new EstimatedDistanceModel();
			est6to35.setDesiredFlow(35.0);
			est6to35.setRefFlow(6.25);
			est6to35.setkDistValue(-0.28776840425405581);
			est6to35.setReDistValue(-715.428482112977917);
			est6to35.setAvgReynolds(930.1383534748088);
			estimatedDistances.add(est6to35);
			
			// 40
			EstimatedDistanceModel est6to40 = new EstimatedDistanceModel();
			est6to40.setDesiredFlow(40.0);
			est6to40.setRefFlow(6.25);
			est6to40.setkDistValue(-0.304809506197633251);
			est6to40.setReDistValue(-845.670946598427463);
			est6to40.setAvgReynolds(1060.3808179602584);
			estimatedDistances.add(est6to40);
			
			// 50
//			EstimatedDistanceModel est6to50 = new EstimatedDistanceModel();
//			est6to50.setDesiredFlow(50.0);
//			est6to50.setRefFlow(6.25);
//			est6to50.setkDistValue(-0.331106424722368731);
//			est6to50.setReDistValue(-1075.32420522503708);
//			est6to50.setAvgReynolds(1290.034076586868);
//			estimatedDistances.add(est6to50);


			// 100
			EstimatedDistanceModel est6to100 = new EstimatedDistanceModel();
			est6to100.setDesiredFlow(100.0);
			est6to100.setRefFlow(6.25);
			est6to100.setkDistValue(-0.380623516837192533);
			est6to100.setReDistValue(-2303.25267377327373);
			est6to100.setAvgReynolds(2517.9625451351044);
			estimatedDistances.add(est6to100);

			// 200
			EstimatedDistanceModel est6to200 = new EstimatedDistanceModel();
			est6to200.setDesiredFlow(200.0);
			est6to200.setRefFlow(6.25);
			est6to200.setkDistValue(-0.397430899309819674);
			est6to200.setReDistValue(-4743.68898790615731);
			est6to200.setAvgReynolds(4958.398859267988);
			estimatedDistances.add(est6to200);

			// 400
			EstimatedDistanceModel est6to400 = new EstimatedDistanceModel();
			est6to400.setDesiredFlow(400.0);
			est6to400.setRefFlow(6.25);
			est6to400.setkDistValue(-0.38497897008913684);
			est6to400.setReDistValue(-9777.39269945465094);
			est6to400.setAvgReynolds(9992.102570816482);
			estimatedDistances.add(est6to400);

			// 878.5
			EstimatedDistanceModel est6to878 = new EstimatedDistanceModel();
			est6to878.setDesiredFlow(878.5);
			est6to878.setRefFlow(6.25);
			est6to878.setkDistValue(-0.374701737482512121);
			est6to878.setReDistValue(-21845.3638803349822);
			est6to878.setAvgReynolds(22060.07375169681);
			estimatedDistances.add(est6to878);

			// 1757
			EstimatedDistanceModel est6to1757 = new EstimatedDistanceModel();
			est6to1757.setDesiredFlow(1757.0);
			est6to1757.setRefFlow(6.25);
			est6to1757.setkDistValue(-0.387441185960773593);
			est6to1757.setReDistValue(-43515.5570456625792);
			est6to1757.setAvgReynolds(43730.26691702441);
			estimatedDistances.add(est6to1757);

			// 3125
			EstimatedDistanceModel est6to3125 = new EstimatedDistanceModel();
			est6to3125.setDesiredFlow(3125.0);
			est6to3125.setRefFlow(6.25);
			est6to3125.setkDistValue(-0.386325455813783192);
			est6to3125.setReDistValue(-77609.1507339418167);
			est6to3125.setAvgReynolds(77823.86060530365);
			estimatedDistances.add(est6to3125);

		} else if(flowrate == 10.0){
			// 2.5
//			EstimatedDistanceModel est10to2_5 = new EstimatedDistanceModel();
//			est10to2_5.setDesiredFlow(2.5);
//			est10to2_5.setRefFlow(10.0);
//			est10to2_5.setkDistValue(0.32854812470802785551);
//			est10to2_5.setReDistValue(-474.11793149206687303);
//			estimatedDistances.add(est10to2_5);

			// 15
			EstimatedDistanceModel est10to15 = new EstimatedDistanceModel();
			est10to15.setDesiredFlow(15.0);
			est10to15.setRefFlow(10.0);
			est10to15.setkDistValue(-0.0701919747596397858);
			est10to15.setReDistValue(-128.819385416682962);
			est10to15.setAvgReynolds(450.1368063119168);
			estimatedDistances.add(est10to15);
			
			// 20
			EstimatedDistanceModel est10to20 = new EstimatedDistanceModel();
			est10to20.setDesiredFlow(20.0);
			est10to20.setRefFlow(6.25);
			est10to20.setkDistValue(-0.195854749031608044);
			est10to20.setReDistValue(-359.501778009972725);
			est10to20.setAvgReynolds(574.2116493718036);
			estimatedDistances.add(est10to20);
			
			// 35
			EstimatedDistanceModel est10to35 = new EstimatedDistanceModel();
			est10to35.setDesiredFlow(35.0);
			est10to35.setRefFlow(10.0);
			est10to35.setkDistValue(-0.212651238898930117);
			est10to35.setReDistValue(-608.820932579574901);
			est10to35.setAvgReynolds(930.1383534748088);
			estimatedDistances.add(est10to35);
			
			// 40
			EstimatedDistanceModel est10to40 = new EstimatedDistanceModel();
			est10to40.setDesiredFlow(40.0);
			est10to40.setRefFlow(10.0);
			est10to40.setkDistValue(-0.229692340842507559);
			est10to40.setReDistValue(-739.063397065024446);
			est10to40.setAvgReynolds(1060.3808179602584);
			estimatedDistances.add(est10to40);
						

			// 50
//			EstimatedDistanceModel est10to50 = new EstimatedDistanceModel();
//			est10to50.setDesiredFlow(50.0);
//			est10to50.setRefFlow(10.0);
//			est10to50.setkDistValue(-0.255989259367243038);
//			est10to50.setReDistValue(-968.71665569163406);
//			est10to50.setAvgReynolds(1290.034076586868);
//			estimatedDistances.add(est10to50);


			// 100
			EstimatedDistanceModel est10to100 = new EstimatedDistanceModel();
			est10to100.setDesiredFlow(100.0);
			est10to100.setRefFlow(10.0);
			est10to100.setkDistValue(-0.30550635148206684);
			est10to100.setReDistValue(-2196.64512423987071);
			est10to100.setAvgReynolds(2517.9625451351044);
			estimatedDistances.add(est10to100);

			// 200
			EstimatedDistanceModel est10to200 = new EstimatedDistanceModel();
			est10to200.setDesiredFlow(200.0);
			est10to200.setRefFlow(10.0);
			est10to200.setkDistValue(-0.322313733954693982);
			est10to200.setReDistValue(-4637.08143837275384);
			est10to200.setAvgReynolds(4958.398859267988);
			estimatedDistances.add(est10to200);

			// 400
			EstimatedDistanceModel est10to400 = new EstimatedDistanceModel();
			est10to400.setDesiredFlow(400.0);
			est10to400.setRefFlow(10.0);
			est10to400.setkDistValue(-0.309861804734011148);
			est10to400.setReDistValue(-9670.78514992124838);
			est10to400.setAvgReynolds(9992.102570816482);
			estimatedDistances.add(est10to400);

			// 878.5
			EstimatedDistanceModel est10to878 = new EstimatedDistanceModel();
			est10to878.setDesiredFlow(878.5);
			est10to878.setRefFlow(10.0);
			est10to878.setkDistValue(-0.299584572127386428);
			est10to878.setReDistValue(-21738.7563308015815);
			est10to878.setAvgReynolds(22060.073751696815);
			estimatedDistances.add(est10to878);

			// 1757
			EstimatedDistanceModel est10to1757 = new EstimatedDistanceModel();
			est10to1757.setDesiredFlow(1757.0);
			est10to1757.setRefFlow(10.0);
			est10to1757.setkDistValue(-0.312324020605647901);
			est10to1757.setReDistValue(-43408.9494961291784);
			est10to1757.setAvgReynolds(43730.26691702441);
			estimatedDistances.add(est10to1757);

			// 3125
			EstimatedDistanceModel est10to3125 = new EstimatedDistanceModel();
			est10to3125.setDesiredFlow(3125.0);
			est10to3125.setRefFlow(10.0);
			est10to3125.setkDistValue(-0.311208290458657499);
			est10to3125.setReDistValue(-77502.5431844084087);
			est10to3125.setAvgReynolds(77823.86060530365);
			estimatedDistances.add(est10to3125);

		}else if(flowrate == 20.0){

			// 2.5
//			EstimatedDistanceModel est20to2_5 = new EstimatedDistanceModel();
//			est20to2_5.setDesiredFlow(2.5);
//			est20to2_5.setRefFlow(20.0);
//			est20to2_5.setkDistValue(0.32854812470802785551);
//			est20to2_5.setReDistValue(-474.11793149206687303);
//			estimatedDistances.add(est20to2_5);

			// 15
			EstimatedDistanceModel est20to15 = new EstimatedDistanceModel();
			est20to15.setDesiredFlow(15.0);
			est20to15.setRefFlow(20.0);
			est20to15.setkDistValue(0.050545608916842566);
			est20to15.setReDistValue(124.074843059886746);
			est20to15.setAvgReynolds(450.1368063119168);
			estimatedDistances.add(est20to15);
						
			// 35
			EstimatedDistanceModel est20to35 = new EstimatedDistanceModel();
			est20to35.setDesiredFlow(35.0);
			est20to35.setRefFlow(20.0);
			est20to35.setkDistValue(-0.0919136552224477654);
			est20to35.setReDistValue(-355.92670410300525);
			est20to35.setAvgReynolds(930.1383534748088);
			estimatedDistances.add(est20to35);
			
			// 40
			EstimatedDistanceModel est20to40 = new EstimatedDistanceModel();
			est20to40.setDesiredFlow(40.0);
			est20to40.setRefFlow(20.0);
			est20to40.setkDistValue(-0.108954757166025207);
			est20to40.setReDistValue(-486.169168588454795);
			est20to40.setAvgReynolds(1060.3808179602584);
			estimatedDistances.add(est20to40);

			// 50
			EstimatedDistanceModel est20to50 = new EstimatedDistanceModel();
			est20to50.setDesiredFlow(50.0);
			est20to50.setRefFlow(20.0);
			est20to50.setkDistValue(-0.135251675690760687);
			est20to50.setReDistValue(-715.822427215064408);
			est20to50.setAvgReynolds(1290.034076586868);
			estimatedDistances.add(est20to50);


			// 100
			EstimatedDistanceModel est20to100 = new EstimatedDistanceModel();
			est20to100.setDesiredFlow(100.0);
			est20to100.setRefFlow(20.0);
			est20to100.setkDistValue(-0.184768767805584488);
			est20to100.setReDistValue(-1943.75089576330083);
			est20to100.setAvgReynolds(2517.9625451351044);
			estimatedDistances.add(est20to100);

			// 200
			EstimatedDistanceModel est20to200 = new EstimatedDistanceModel();
			est20to200.setDesiredFlow(200.0);
			est20to200.setRefFlow(20.0);
			est20to200.setkDistValue(-0.20157615027821163);
			est20to200.setReDistValue(-4384.18720989618487);
			est20to200.setAvgReynolds(4958.398859267988);
			estimatedDistances.add(est20to200);

			// 400
			EstimatedDistanceModel est20to400 = new EstimatedDistanceModel();
			est20to400.setDesiredFlow(400.0);
			est20to400.setRefFlow(20.0);
			est20to400.setkDistValue(-0.189124221057528796);
			est20to400.setReDistValue(-9417.89092144467759);
			est20to400.setAvgReynolds(9992.102570816482);
			estimatedDistances.add(est20to400);

			// 878.5
			EstimatedDistanceModel est20to878 = new EstimatedDistanceModel();
			est20to878.setDesiredFlow(878.5);
			est20to878.setRefFlow(20.0);
			est20to878.setkDistValue(-0.178846988450904076);
			est20to878.setReDistValue(-21485.8621023250125);
			est20to878.setAvgReynolds(22060.073751696815);
			estimatedDistances.add(est20to878);

			// 1757
			EstimatedDistanceModel est20to1757 = new EstimatedDistanceModel();
			est20to1757.setDesiredFlow(1757.0);
			est20to1757.setRefFlow(20.0);
			est20to1757.setkDistValue(-0.0563347612384048624);
			est20to1757.setReDistValue(-42440.2328404375439);
			est20to1757.setAvgReynolds(43730.26691702441);
			estimatedDistances.add(est20to1757);

			// 3125
			EstimatedDistanceModel est20to3125 = new EstimatedDistanceModel();
			est20to3125.setDesiredFlow(3125.0);
			est20to3125.setRefFlow(20.0);
			est20to3125.setkDistValue(-0.190470706782175148);
			est20to3125.setReDistValue(-77249.6489559318434);
			est20to3125.setAvgReynolds(77823.86060530365);
			estimatedDistances.add(est20to3125);

		}else if(flowrate == 50.0){
			// 15
			EstimatedDistanceModel est50to15 = new EstimatedDistanceModel();
			est50to15.setDesiredFlow(15.0);
			est50to15.setRefFlow(50.0);
			est50to15.setkDistValue(0.185797284607603252);
			est50to15.setReDistValue(839.897270274951097);
			est50to15.setAvgReynolds(450.1368063119168);
			estimatedDistances.add(est50to15);
			
			// 20
			EstimatedDistanceModel est50to20 = new EstimatedDistanceModel();
			est50to20.setDesiredFlow(20.0);
			est50to20.setRefFlow(50.0);
			est50to20.setkDistValue(0.135251675690760687);
			est50to20.setReDistValue(715.822427215064408);
			est50to20.setAvgReynolds(1290.034076586868);
			estimatedDistances.add(est50to20);
			
			// 35
			EstimatedDistanceModel est50to35 = new EstimatedDistanceModel();
			est50to35.setDesiredFlow(35.0);
			est50to35.setRefFlow(50.0);
			est50to35.setkDistValue(0.0433380204683129211);
			est50to35.setReDistValue(715.822427215064408);
			est50to35.setAvgReynolds(359.895723112059159);
			estimatedDistances.add(est50to35);
			
			// 40
			EstimatedDistanceModel est50to40 = new EstimatedDistanceModel();
			est50to40.setDesiredFlow(40.0);
			est50to40.setRefFlow(50.0);
			est50to40.setkDistValue(0.0262969185247354797);
			est50to40.setReDistValue(229.653258626609613);
			est50to40.setAvgReynolds(1060.3808179602584);
			estimatedDistances.add(est50to40);


			// 100
			EstimatedDistanceModel est50to100 = new EstimatedDistanceModel();
			est50to100.setDesiredFlow(100.0);
			est50to100.setRefFlow(50.0);
			est50to100.setkDistValue(-0.0495170921148238019);
			est50to100.setReDistValue(-1227.92846854823642);
			est50to100.setAvgReynolds(2517.9625451351044);
			estimatedDistances.add(est50to100);

			// 200
			EstimatedDistanceModel est50to200 = new EstimatedDistanceModel();
			est50to200.setDesiredFlow(200.0);
			est50to200.setRefFlow(20.0);
			est50to200.setkDistValue(-0.0663244745874509434);
			est50to200.setReDistValue(-3668.36478268112023);
			est50to200.setAvgReynolds(4958.398859267988);
			estimatedDistances.add(est50to200);

			// 400
			EstimatedDistanceModel est50to400 = new EstimatedDistanceModel();
			est50to400.setDesiredFlow(400.0);
			est50to400.setRefFlow(50.0);
			est50to400.setkDistValue(-0.0538725453667681098);
			est50to400.setReDistValue(-8702.06849422961386);
			est50to400.setAvgReynolds(9992.102570816482);
			estimatedDistances.add(est50to400);

			// 878.5
			EstimatedDistanceModel est50to878 = new EstimatedDistanceModel();
			est50to878.setDesiredFlow(878.5);
			est50to878.setRefFlow(50.0);
			est50to878.setkDistValue(-0.0435953127601433899);
			est50to878.setReDistValue(-20770.039675109947);
			est50to878.setAvgReynolds(22060.073751696815);
			estimatedDistances.add(est50to878);

			// 1757
			EstimatedDistanceModel est50to1757 = new EstimatedDistanceModel();
			est50to1757.setDesiredFlow(1757.0);
			est50to1757.setRefFlow(50.0);
			est50to1757.setkDistValue(-0.0563347612384048624);
			est50to1757.setReDistValue(-42440.2328404375439);
			est50to1757.setAvgReynolds(43730.26691702441);
			estimatedDistances.add(est50to1757);

			// 3125
			EstimatedDistanceModel est50to3125 = new EstimatedDistanceModel();
			est50to3125.setDesiredFlow(3125.0);
			est50to3125.setRefFlow(50.0);
			est50to3125.setkDistValue(-0.190470706782175148);
			est50to3125.setReDistValue(-77249.6489559318434);
			est50to3125.setAvgReynolds(77823.86060530365);
			estimatedDistances.add(est50to3125);
			
		}else if (flowrate == 1757.0) {
			
			// 15
			EstimatedDistanceModel est15to1757 = new EstimatedDistanceModel();
			est15to1757.setDesiredFlow(15.0);
			est15to1757.setRefFlow(1757.0);
			est15to1757.setkDistValue(0.242132045846008115);
			est15to1757.setReDistValue(43280.1301107124964);
			est15to1757.setAvgReynolds(450.1368063119168);
			estimatedDistances.add(est15to1757);
			
			// 20
//			EstimatedDistanceModel est20to1757 = new EstimatedDistanceModel();
//			est20to1757.setDesiredFlow(20.0);
//			est20to1757.setRefFlow(1757.0);
//			est20to1757.setkDistValue();
//			est20to1757.setReDistValue();
//			est20to1757.setAvgReynolds(43730.26691702441);
//			estimatedDistances.add(est20to1757);
			
			// 35
			EstimatedDistanceModel est35to1757 = new EstimatedDistanceModel();
			est35to1757.setDesiredFlow(35.0);
			est35to1757.setRefFlow(1757.0);
			est35to1757.setkDistValue(0.0996727817067177835);
			est35to1757.setReDistValue(42800.1285635496024);
			est35to1757.setAvgReynolds(930.1383534748088);
			estimatedDistances.add(est35to1757);
			
			// 40
			EstimatedDistanceModel est40to1757= new EstimatedDistanceModel();
			est40to1757.setDesiredFlow(40.0);
			est40to1757.setRefFlow(1757.0);
			est40to1757.setkDistValue(0.0826316797631403421);
			est40to1757.setReDistValue(42669.8860990641551);
			est40to1757.setAvgReynolds(1060.3808179602584);
			estimatedDistances.add(est40to1757);

			// 50
//			EstimatedDistanceModel est50to1757 = new EstimatedDistanceModel();
//			est50to1757.setDesiredFlow(50.0);
//			est50to1757.setRefFlow(1757.0);
//			est50to1757.setkDistValue(0.0563347612384048624);
//			est50to1757.setReDistValue(42440.2328404375439);
//			est50to1757.setAvgReynolds(1290.034076586868);
//			estimatedDistances.add(est50to1757);


			// 100
			EstimatedDistanceModel est100to1757 = new EstimatedDistanceModel();
			est100to1757.setDesiredFlow(100.0);
			est100to1757.setRefFlow(1757.0);
			est100to1757.setkDistValue(0.00681766912358106048);
			est100to1757.setReDistValue(41212.3043718893096);
			est100to1757.setAvgReynolds(2517.9625451351044);
			estimatedDistances.add(est100to1757);

			// 200
			EstimatedDistanceModel est200to1757 = new EstimatedDistanceModel();
			est200to1757.setDesiredFlow(200.0);
			est200to1757.setRefFlow(1757.0);
			est200to1757.setkDistValue(-0.00998971334904608099);
			est200to1757.setReDistValue(38771.8680577564228);
			est200to1757.setAvgReynolds(4958.398859267988);
			estimatedDistances.add(est200to1757);

			// 400
			EstimatedDistanceModel est20to400 = new EstimatedDistanceModel();
			est20to400.setDesiredFlow(400.0);
			est20to400.setRefFlow(1757.0);
			est20to400.setkDistValue(0.0024622158716367526);
			est20to400.setReDistValue(33738.1643462079301);
			est20to400.setAvgReynolds(9992.102570816482);
			estimatedDistances.add(est20to400);

			// 878.5
			EstimatedDistanceModel est20to878 = new EstimatedDistanceModel();
			est20to878.setDesiredFlow(878.5);
			est20to878.setRefFlow(1757.0);
			est20to878.setkDistValue(0.0127394484782614725);
			est20to878.setReDistValue(21670.1931653275969);
			est20to878.setAvgReynolds(22060.073751696815);
			estimatedDistances.add(est20to878);

			// 3125
			EstimatedDistanceModel est20to3125 = new EstimatedDistanceModel();
			est20to3125.setDesiredFlow(3125.0);
			est20to3125.setRefFlow(1757.0);
			est20to3125.setkDistValue(0.00111573014699040129);
			est20to3125.setReDistValue(-34093.5936882792375);
			est20to3125.setAvgReynolds(77823.86060530365);
			estimatedDistances.add(est20to3125);
			
		}else if(flowrate == 2500.0){
			// 15
			EstimatedDistanceModel est15to2500 = new EstimatedDistanceModel();
			est15to2500.setDesiredFlow(15.0);
			est15to2500.setRefFlow(2500.0);
			est15to2500.setkDistValue(0.242087004014592599);
			est15to2500.setReDistValue(61791.6066710680971);
			est15to2500.setAvgReynolds(450.1368063119168);
			estimatedDistances.add(est15to2500);
			
			// 20
			EstimatedDistanceModel est20to1757 = new EstimatedDistanceModel();
			est20to1757.setDesiredFlow(20.0);
			est20to1757.setRefFlow(2500.0);
			est20to1757.setkDistValue(0.191541395097750033);
			est20to1757.setReDistValue(61667.5318280082065);
			est20to1757.setAvgReynolds(574.2116493718036);
			estimatedDistances.add(est20to1757);
			
			// 35
			EstimatedDistanceModel est35to2500 = new EstimatedDistanceModel();
			est35to2500.setDesiredFlow(35.0);
			est35to2500.setRefFlow(2500.0);
			est35to2500.setkDistValue(0.099627739875302268);
			est35to2500.setReDistValue(61311.6051239052031);
			est35to2500.setAvgReynolds(930.1383534748088);
			estimatedDistances.add(est35to2500);
			
			// 40
			EstimatedDistanceModel est40to2500= new EstimatedDistanceModel();
			est40to2500.setDesiredFlow(40.0);
			est40to2500.setRefFlow(2500.0);
			est40to2500.setkDistValue(0.0825866379317248267);
			est40to2500.setReDistValue(61181.3626594197558);
			est40to2500.setAvgReynolds(1060.3808179602584);
			estimatedDistances.add(est40to2500);

			// 50
//			EstimatedDistanceModel est50to2500 = new EstimatedDistanceModel();
//			est50to2500.setDesiredFlow(50.0);
//			est50to2500.setRefFlow(2500.0);
//			est50to2500.setkDistValue();
//			est50to2500.setReDistValue();
//			est50to2500.setAvgReynolds(1290.034076586868);
//			estimatedDistances.add(est50to2500);


			// 100
			EstimatedDistanceModel est100to2500 = new EstimatedDistanceModel();
			est100to2500.setDesiredFlow(100.0);
			est100to2500.setRefFlow(2500.0);
			est100to2500.setkDistValue(0.00681766912358106048);
			est100to2500.setReDistValue(59723.7809322449102);
			est100to2500.setAvgReynolds(0.00677262729216554504);
			estimatedDistances.add(est100to2500);

			// 200
			EstimatedDistanceModel est200to2500 = new EstimatedDistanceModel();
			est200to2500.setDesiredFlow(200.0);
			est200to2500.setRefFlow(2500.0);
			est200to2500.setkDistValue(-0.0100347551804615964);
			est200to2500.setReDistValue(57283.3446181120235);
			est200to2500.setAvgReynolds(4958.398859267988);
			estimatedDistances.add(est200to2500);

			// 400
			EstimatedDistanceModel est400to2500 = new EstimatedDistanceModel();
			est400to2500.setDesiredFlow(400.0);
			est400to2500.setRefFlow(2500.0);
			est400to2500.setkDistValue(0.00241717404022123716);
			est400to2500.setReDistValue(52249.6409065635307);
			est400to2500.setAvgReynolds(9992.102570816482);
			estimatedDistances.add(est400to2500);

			// 878.5
			EstimatedDistanceModel est878to2500 = new EstimatedDistanceModel();
			est878to2500.setDesiredFlow(878.5);
			est878to2500.setRefFlow(2500.0);
			est878to2500.setkDistValue(0.0126944066468459571);
			est878to2500.setReDistValue(40181.6697256831976);
			est878to2500.setAvgReynolds(22060.073751696815);
			estimatedDistances.add(est878to2500);
			
			// 1757
			EstimatedDistanceModel est1757to2500 = new EstimatedDistanceModel();
			est1757to2500.setDesiredFlow(2500.0);
			est1757to2500.setRefFlow(50.0);
			est1757to2500.setkDistValue(-0.000045041831415515432);
			est1757to2500.setReDistValue(18511.4765603556007);
			est1757to2500.setAvgReynolds(43730.26691702441);
			estimatedDistances.add(est1757to2500);

			 //3125
			EstimatedDistanceModel est3125to2500 = new EstimatedDistanceModel();
			est3125to2500.setDesiredFlow(3125.0);
			est3125to2500.setRefFlow(2500.0);
			est3125to2500.setkDistValue(0.00107068831557488586);
			est3125to2500.setReDistValue(-15582.1171279236369);
			est3125to2500.setAvgReynolds(77823.86060530365);
			estimatedDistances.add(est3125to2500);
		}else if(flowrate == 3125){
			
		}

		return estimatedDistances;
	}
}

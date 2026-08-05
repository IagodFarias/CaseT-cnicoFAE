//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BatchController.java
*    @author marcos
*    @date 29 de nov de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.hibernate.Hibernate;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BatchRadioWmBusConfigModel;
import si.dbcomm.model.BateladaModel;
import si.dbcomm.model.MeterModel;
import si.dbcomm.model.ProcessConfigModel;
import si.dbcomm.service.BatchRadioWmBusConfigService;
import si.dbcomm.service.BatchService;
import si.dbcomm.service.BateladaService;
import si.dbcomm.service.ProcessConfigService;
import view.bean.BenchBean;

/**
 * @author marcos
 *
 */
public class BatchController {
	
	private static BatchController INSTANCE;

	private BatchModel batch;
	
	private BateladaModel lastBateladaStored;

	private List<BatchModel> batches;

	private BatchService batchService;

	private BatchRadioWmBusConfigService batchRadioConfigService;

	private BateladaService bateladaService;

	private BateladaController bateladaController;

	private SimpleDateFormat manufacYearDateFormat;

	private static Semaphore mutexSerialHid;

	private static Semaphore mutexSerialRad;

	{
		mutexSerialHid = new Semaphore(1);
		mutexSerialRad = new Semaphore(1);
	}

	/**
	 * Creates a object for class BatchController.java
	 */
	public BatchController() {
		this.batch = new BatchModel();
		this.batches = new ArrayList<>();
		this.batchService = new BatchService();
		this.batchRadioConfigService = new BatchRadioWmBusConfigService();
		this.bateladaService = new BateladaService();
		this.bateladaController = new BateladaController();
		this.manufacYearDateFormat = new SimpleDateFormat("yy");
	}
	
	
	
	/**
	 * Function returns the value of attribute lastBateladaStored. This is previously stored by incRunCount() method
	 * @return the lastBateladaStored
	 */
	public BateladaModel getLastBateladaStored() {
		return lastBateladaStored;
	}



	/**
	 * Function sets the value for attribute lastBateladaStored
	 * @param lastBateladaStored the lastBateladaStored to set
	 */
	public void setLastBateladaStored(BateladaModel lastBateladaStored) {
		this.lastBateladaStored = lastBateladaStored;
	}



	/**
	 * Function returns the value of attribute batch
	 * 
	 * @return the batch
	 */
	public BatchModel getBatch() {
		return batch;
	}

	/**
	 * Function sets the value for attribute batch
	 * 
	 * @param batch
	 *            the batch to set
	 */
	public void setBatch(BatchModel batch) {
		this.batch = batch;
	}

	/**
	 * Function returns the value of attribute batches
	 * 
	 * @return the batches
	 */
	public List<BatchModel> getBatches() {
		return batches;
	}

	/**
	 * Function sets the value for attribute batches
	 * 
	 * @param batches
	 *            the batches to set
	 */
	public void setBatches(List<BatchModel> batches) {
		this.batches = batches;
	}

	/**
	 * Finds the batch by the determined ID
	 * 
	 * @param batchId
	 * @return
	 */
	public BatchModel findByBatchId(String batchId) {
		BatchModel aux = null;
		if (batchId != null) {
			if (!batchId.isEmpty()) {
				aux = batchService.findByBatchId(batchId);
			}
		}
		return aux;
	}

	public String getBatchManufatureYear(BatchModel batch) {
		String retorno = "";
		BatchModel auxBatch = findByBatchId(batch.getBatchId());
		if (auxBatch != null) {
			Date date = auxBatch.getYearOfManufacture();
			if (date != null) {
				retorno = manufacYearDateFormat.format(date);
			}
		}

		return retorno;
	}

	/**
	 * 
	 * @param batchId
	 * @return
	 */
	public String findLastMeterSN(String batchId) {
		String retorno = null;
		if (batchId != null) {
			if (!batchId.isEmpty()) {
				retorno = batchService.findLastSerialInBatch(batchId);
			}
		}
		return retorno;
	}

	// /**
	// * checks the last serial number for the specified batch and retrieves the next attributable Serial Number
	// *
	// * @return
	// */
	// public String getNextAvailableSN(String batchId) {
	// String completeSerialNumber = null;
	// if (batchId != null) {
	// String qnId = null;
	// String manufacCod = null;
	// String serialSeqString = null;
	// String manufacYear = null;
	// // aux = findLastMeterSN(batchId);
	// try {
	// mutexSerialHid.acquire();
	// } catch (InterruptedException e) {
	// System.err.println("ERROR: InterruptedException Could not acquire mutex for transaction BatchController.getNextAvailableSN()");
	// }
	// BatchModel batchAux = batchService.findByBatchId(batchId);
	// int lastSerial = 0;
	// lastSerial = batchAux.getLastAssignedSerialSeq();
	// qnId = batchAux.getMeterType().getInmetroQnId();
	// manufacCod = batchAux.getManufacturerCode();
	// manufacYear = manufacYearDateFormat.format(batchAux.getYearOfManufacture());
	//
	// if (lastSerial == -1) {
	// lastSerial = batchAux.getStartSerialSeq();
	// } else {
	// lastSerial++;// incs the series counter for the serial number
	//
	// // Check max serial number
	// if (){
	//
	// }
	//
	// }
	//
	// serialSeqString = serialIntToString(6, lastSerial);
	// completeSerialNumber = qnId + manufacYear + manufacCod + serialSeqString;
	// mutexSerialHid.release();
	// }
	// return completeSerialNumber;
	// }

	// /**
	// *
	// */
	// private QaAssuredModel getInactiveSerialNumber(BatchModel batch) {
	// // int lostSeq = -1;
	// QaAssuredModel qaAssuredSelected = null;
	// MeterService meterService = new MeterService();
	//
	// QaAssuredService qaAssuredService = new QaAssuredService();
	// for (QaAssuredModel qaAssured : qaAssuredService.findAll()) {
	// if (qaAssured.getMeter().getBatch().getId() == batch.getId()) {
	// if (qaAssured.getMeter().getSerialNumber().isEmpty()) {
	// String serial = qaAssured.getOldSerialNumber();
	// MeterModel meter = meterService.findBySerialNumber(serial);
	// if (meter == null) {
	// qaAssuredSelected = qaAssured;
	// // serial = serial.substring(serial.length() - 6, serial.length());
	// // lostSeq = Integer.valueOf(serial);
	// break;
	// }
	// }
	// }
	// }
	// return qaAssuredSelected;
	// }

	/**
	 * checks the last serial number for the specified batch and retrieves the next attributable Serial Number
	 * 
	 * @return
	 */
	public String getNextAvailableSN(BatchModel batch) {

		boolean available = false;
		String completeSerialNumber = null;
		if (batch != null) {
			String qnId = null;
			String manufacCod = null;
			String serialSeqString = null;
			String manufacYear = null;
			// aux = findLastMeterSN(batchId);
			try {
				mutexSerialHid.acquire();
			} catch (InterruptedException e) {
				System.err.println("ERROR: InterruptedException Could not acquire mutex for transaction BatchController.getNextAvailableSN()");
			}

			// Serial number meter
			qnId = batch.getMeterType().getInmetroQnId();
			manufacCod = batch.getManufacturerCode();
			manufacYear = manufacYearDateFormat.format(batch.getYearOfManufacture());

			//
			int lastSerial = batch.getLastAssignedSerialSeq();
			if (lastSerial == -1) {
				lastSerial = batch.getStartSerialSeq();
				available = true;
			} else {
				lastSerial++;// incs the series counter for the serial number
				// Check max serial number
				if ((lastSerial - batch.getStartSerialSeq()) > batch.getNumMeters()) {
					available = false;
					batch.setFinished(true);
				} else {
					available = true;
				}
			}
			//
			if (available) {
				serialSeqString = serialIntToString(6, lastSerial);
				completeSerialNumber = qnId + manufacYear + manufacCod + serialSeqString;
			}
			mutexSerialHid.release();
		}
		return completeSerialNumber;
	}

	/**
	 * Returns the next avaliable radio serial number
	 * 
	 * @param batchId
	 * @return
	 */
	public String getNextAvailableRadioSN(String batchId) {
		String completeSerialNumber = null;
		if (batchId != null) {
			String manufacNumber;
			String prodCod;
			String versao;
			String serialSeqString = null;
			// aux = findLastMeterSN(batchId);
			try {
				mutexSerialRad.acquire();
			} catch (InterruptedException e) {
				System.err.println("ERROR: InterruptedException Could not acquire mutex for transaction BatchController.getNextAvailableRadioSN()");
			}
			BatchModel batchAux = batchService.findByBatchId(batchId);
			BatchRadioWmBusConfigModel batchRadioConfig = batchRadioConfigService.findByBatchId(batchAux.getId());
			if (batchRadioConfig != null) {

				int lastSerial = 0;
				lastSerial = batchRadioConfig.getLastAssignedSerial();
				manufacNumber = batchRadioConfig.getManufacNumber();
				prodCod = batchRadioConfig.getProductCode();
				versao = batchRadioConfig.getVersion();

				if (lastSerial == -1) {
					lastSerial = batchRadioConfig.getStartSerialSeq();
				} else {

					lastSerial++;// incs the series counter for the serial number
					// lastSerial = serialSeqToLsb(lastSerial);
				}

				serialSeqString = serialIntToStringRadio(8, lastSerial);
				// 25 18 %02d %02d %02d %02d 00 07
				completeSerialNumber = manufacNumber + serialSeqString + versao + prodCod;
			} else {
				System.err.println("ERROR: batchRadioConfig is null on BatchController.getNextAvailableRadioSN()");
			}
			mutexSerialRad.release();
		}
		return completeSerialNumber;
	}

	// public String serialSeqToLsb(String serialSeq){
	// String retorno = 0;
	// byte byteArray[];
	// byte byteAux;
	//
	//// ByteBuffer array = ByteBuffer.allocate(4);
	//// array.putInt(serialSeq);
	//// byteArray = array.array();
	////
	//// byteAux = byteArray[3];
	//// byteArray[3] = byteArray[0];
	//// byteArray[0] = byteAux;
	//// byteAux = byteArray[2];
	//// byteArray[2] = byteArray[1];
	//// byteArray[1] = byteAux;
	//// array = ByteBuffer.wrap(byteArray);
	//// retorno = array.getInt();
	// return retorno;
	// }

	// public boolean allocateWmBusConfigToBatch(BatchModel batchModel) {
	// boolean retorno = false;
	// BatchRadioWmBusConfigModel batchRadioConfig = batchRadioConfigService.findByBatchId(batchModel.getId());
	// batchRadioConfig.setLastAssignedSerial(batchRadioConfig.getLastAssignedSerial() + 1);
	//
	// try {
	// batchRadioConfigService.update(batchRadioConfig);
	// retorno = true;
	// } catch (CrudDatabaseException e) {
	// e.printStackTrace();
	// }
	//
	// return retorno;
	// }

	public boolean allocateWmBusConfigToBatch(BatchModel batchModel, String serial) {
		boolean retorno = false;
		BatchRadioWmBusConfigModel batchRadioConfig = batchRadioConfigService.findByBatchId(batchModel.getId());
		// batchRadioConfig.setLastAssignedSerial(batchRadioConfig.getLastAssignedSerial() + 1);
		batchRadioConfig.setLastAssignedSerial(getLastSerialSeqFromStringRadio(serial));

		try {
			batchRadioConfigService.update(batchRadioConfig);
			retorno = true;
		} catch (CrudDatabaseException e) {
			e.printStackTrace();
		}

		return retorno;
	}

	/**
	 * Receive sequence and formats it as as wM-Bus serial number
	 * 
	 * @param numberOfDigits
	 * @param data
	 * @return
	 */
	public String serialIntToStringRadio(int numberOfDigits, int data) {
		String retorno = "";
		String serial = Integer.toString(data);
		int length = serial.length();
		while (length < numberOfDigits) {
			serial = 0 + serial;
			length = serial.length();
		}

		retorno = serial.substring(6, 8) + serial.substring(4, 6) + serial.substring(2, 4) + serial.substring(0, 2);
		return retorno;
	}

	/**
	 * 
	 */
	public int getLastSerialSeqFromStringRadio(String serial) {
		int result = 0;
		String lastAssigned = serial.substring(4, 13);

		String str = lastAssigned.substring(6, 8) + lastAssigned.substring(4, 6) + lastAssigned.substring(2, 4) + lastAssigned.substring(0, 2);
		result = Integer.valueOf(str);
		return result;
	}

	private String serialIntToString(int numberOfDigits, int data) {
		String retorno = "";
		int length = String.valueOf(data).length();
		retorno = String.valueOf(data);
		while (length < numberOfDigits) {
			retorno = '0' + retorno;
			length = retorno.length();
		}

		return retorno;
	}

	public int getBatchRunCount(String batchId) {
		return batchService.getBatchRunCount(batchId);
	}

	// /**
	// * Increments the run counter of the batch
	// * @param batchId
	// * @return int - the incremented run count = -1 if not able to calculate
	// */
	// public int incBatchRunCount(String batchId){
	// int count = -1;
	// if(!batchId.isEmpty()){
	// BatchModel batchAux = batchService.findByBatchId(batchId);
	// BateladaModel lastBatelada = batchService.getBatchLastBatelada(batchId);
	// BateladaModel newBatelada = new BateladaModel();
	// newBatelada.setBatch(batchAux);
	// newBatelada.setProcessesConfigModel(lastBatelada.getProcessesConfigModel());
	// count = lastBatelada.getRunCount();
	// count++;
	// newBatelada.setRunCount(count);
	// newBatelada.setInitTime(new Date());
	// try {
	// if(bateladaService.persist(newBatelada).equals(newBatelada)){
	// }
	// } catch (CrudDatabaseException e) {
	// e.printStackTrace();
	// }
	// }
	// return count;
	// }

	/**
	 * Increments the run counter of the batch
	 * 
	 * @param batchId
	 * @return int - the incremented run count = -1 if not able to calculate
	 */
	public int incBatchRunCount(String batchId) {
		int count = -1;
		if (!batchId.isEmpty()) {
			BatchModel batchAux = batchService.findByBatchId(batchId);
			BateladaModel lastBatelada = batchService.getBatchLastBatelada(batchId);
			count = lastBatelada.getRunCount();

			// ProcessConfigModel processConfig;
			// if (ViewDataUtil.getInstance().getProcessConfig() != null) { // processConfig was edited by user
			// processConfig = ViewDataUtil.getInstance().getProcessConfig();
			// } else {
			// processConfig = lastBatelada.getProcessesConfigModel();
			// }
			//
			// ProcessConfigModel processConfig = lastBatelada.getProcessesConfigModel();
			//
			ProcessConfigService processConfigService = new ProcessConfigService();
			ProcessConfigModel processConfig = processConfigService.findById(batchAux.getIdxProcessConfig());
			
			BateladaModel batAux;
			if (count == 0) {
				count++;
				lastBatelada.setRunCount(count);
				lastBatelada.setInitTime(new Date());
				lastBatelada.setProcessesConfigModel(processConfig);
				try {
					if (bateladaService.update(lastBatelada).equals(lastBatelada)) {
						System.err.println("Update batelada on BatchController.incBatchRunCount().");
					} else {
						System.err.println("ERROR: Failed to update batelada on BatchController.incBatchRunCount().");
					}
				} catch (CrudDatabaseException e) {
					e.printStackTrace();
				}
				batAux = lastBatelada;
			} else {
				BateladaModel newBatelada = new BateladaModel();
				newBatelada.setBatch(batchAux);
				newBatelada.setProcessesConfigModel(processConfig);
				count++;
				newBatelada.setRunCount(count);
				newBatelada.setInitTime(new Date());
				newBatelada.setCalibrationType(lastBatelada.getCalibrationType());
				
				try {
					if (bateladaService.persist(newBatelada).equals(newBatelada)) {
						System.err.println("Persist new batelada on BatchController.incBatchRunCount().");
					} else {
						System.err.println("ERROR: Failed to persist new batelada on BatchController.incBatchRunCount().");
					}
				} catch (CrudDatabaseException e) {
					e.printStackTrace();
				}
				batAux = newBatelada;
			}
			
			
			this.lastBateladaStored = batAux;
			this.batch = batchAux;
		}
		return count;
	}

	/**
	 * Persists in Database the serial number related to the run. This method is to be called after the getNextAvailableSN() that increments the serial number
	 * 
	 * @param meterController
	 * @param batch
	 * @return true in case
	 */
	public boolean allocateSerialNumberToBatch(MeterController meterController, BatchModel batch) {
		boolean retorno = false;
		if (batch != null) {
			if (meterController != null) {
				MeterModel meter = meterController.getMeter();
				// BatchModel batchAux = batchService.findByBatchId(batch.getBatchId());
				BateladaModel currentBatelada = batchService.getBatchLastBatelada(batch.getBatchId());
				currentBatelada.setApprovedMeters(1 + currentBatelada.getApprovedMeters());
				currentBatelada.getMeters().add(meter);
				meter.setBatelada(currentBatelada);
				if (meter.getSerialNumber() != null) {
					batch.setLastAssignedSerialSeq(extractSeqFromSerialNumber(meter.getSerialNumber(), batch));
				}

				try {
					if (batchService.update(batch).equals(batch)) {
						currentBatelada.setBatch(batch);
						if (bateladaService.update(currentBatelada).equals(currentBatelada)) {
							retorno = true;
						}
					} else {
						retorno = false;
					}
				} catch (CrudDatabaseException e) {
					e.printStackTrace();
				}
			}
		}

		return retorno;
	}

	/**
	 * Checks if batch has any bateladas
	 * 
	 * @param batch
	 * @return
	 */
	public boolean checkForBateladas(BatchModel batch) {
		boolean retorno = false;
		Set<BateladaModel> bateladas = batch.getBateladas();
		
		if(!Hibernate.isInitialized(bateladas)){
			BateladaModel currentBatelada = batchService.getBatchLastBatelada(batch);
			if (currentBatelada != null) {
				retorno = true;
			}
		}
		return retorno;
	}

	public boolean createInitalBatelada(BatchModel batch) {
		boolean retorno = false;
		if (bateladaController.createInitBatelada(batch)) {
			retorno = true;
		}
		return retorno;
	}

	public BateladaModel getLastBatelada(BatchModel batch) {
		return batchService.getBatchLastBatelada(batch.getBatchId());
	}

	public int extractSeqFromSerialNumber(String serialNumber, BatchModel batch) {
		int retorno = 0;
		String aux = serialNumber.substring(serialNumber.lastIndexOf(batch.getManufacturerCode()) + 1);
		retorno = Integer.parseInt(aux);

		return retorno;
	}

	public int extractSeqFromRFSerialNumber(String serialNumber) {
		int retorno = 0;
		String aux = serialNumber.substring(serialNumber.lastIndexOf(batch.getManufacturerCode()) + 1);
		retorno = Integer.parseInt(aux);

		return retorno;
	}

	public ProcessConfigModel getLastUsedProcessConfig(BatchModel batch) {
		BateladaModel batelada = batchService.getBatchLastBatelada(batch.getBatchId());
		return batelada.getProcessesConfigModel();

	}

	public short getWmBusTransmitInterval(BatchModel batch) {
		short retorno = 0;
		if (batch.getId() != 0) {
			BatchRadioWmBusConfigModel batchRadioConfig = batchRadioConfigService.findByBatchId(batch.getId());
			if (batchRadioConfig != null) {
				retorno = batchRadioConfig.getTransmitInterval();
			}
		}
		return retorno;
	}
}

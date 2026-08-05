//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MatFileHandlerUtil.java
*    @author marcos
*    @date 30 de jan de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package util;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jmatio.io.MatFileReader;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;

import enumerations.MeterConfigV108Enum;
import si.dbcomm.model.BenchDataModel;
import si.dbcomm.model.CalibConstantsModel;
import si.dbcomm.model.MeterDataModel;
import si.dbcomm.model.MeterModel;
import si.dbcomm.model.MeterTypeModel;
import si.dbcomm.model.VerificationErrorModel;

/**
 * @author marcos
 *
 */
public class MatFileHandlerUtil {
	
	@SuppressWarnings("unused")
	private MatFileReader matReader;
	
	@SuppressWarnings("unused")
	private MatFileWriter matWriter;
	
	private SimpleDateFormat spdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	
	private String benchDataFileName;
	
	private String benchVerifDataFileName;
	
	private String folderName;
	
	private String zeroFlowFileName;
	
	private String meterFileName;
	
	private String date;
	
	private String rootDir = "../matFiles/";
	
	private String fullDir;
	
	private String meterDir;
	
	
	/**
	 * Function returns the value of attribute fullDir
	 * @return the fullDir
	 */
	public String getFullDir() {
		return fullDir;
	}

		/**
	 * Function sets the value for attribute fullDir
	 * @param fullDir the fullDir to set
	 */
	public void setFullDir(String fullDir) {
		this.fullDir = fullDir;
	}
	
	/**
	 * Function returns the value of attribute meterDir
	 * @return the meterDir
	 */
	public String getMeterDir() {
		return meterDir;
	}

	/**
	 * Function sets the value for attribute meterDir
	 * @param meterDir the meterDir to set
	 */
	public void setMeterDir(String meterDir) {
		this.meterDir = meterDir;
	}

	/**
	 * Function returns the value of attribute benchDataFileName
	 * @return the benchDataFileName
	 */
	public String getBenchDataFileName() {
		return benchDataFileName;
	}

	/**
	 * Function sets the value for attribute benchDataFileName
	 * @param benchDataFileName the benchDataFileName to set
	 */
	public void setBenchDataFileName(String filename) {
		this.benchDataFileName = filename;
	}
	
	/**
	 * Function returns the value of attribute benchVerifDataFileName
	 * @return the benchVerifDataFileName
	 */
	public String getBenchVerifDataFileName() {
		return benchVerifDataFileName;
	}

	/**
	 * Function sets the value for attribute benchVerifDataFileName
	 * @param benchVerifDataFileName the benchVerifDataFileName to set
	 */
	public void setBenchVerifDataFileName(String benchVerifDataFileName) {
		this.benchVerifDataFileName = benchVerifDataFileName;
	}

	/**
	 * Function returns the value of attribute zeroFlowFileName
	 * @return the zeroFlowFileName
	 */
	public String getZeroFlowFileName() {
		return zeroFlowFileName;
	}

	/**
	 * Function sets the value for attribute zeroFlowFileName
	 * @param zeroFlowFileName the zeroFlowFileName to set
	 */
	public void setZeroFlowFileName(String zeroFlowFileName) {
		this.zeroFlowFileName = zeroFlowFileName;
	}

	/**
	 * Function returns the value of attribute folderName
	 * @return the folderName
	 */
	public String getFolderName() {
		return folderName;
	}

	/**
	 * Function sets the value for attribute folderName
	 * @param folderName the folderName to set
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	/**
	 * Function returns the value of attribute meterFileName
	 * @return the meterFileName
	 */
	public String getMeterFileName() {
		return meterFileName;
	}

	/**
	 * Function sets the value for attribute meterFileName
	 * @param meterFileName the meterFileName to set
	 */
	public void setMeterFileName(String meterFileName) {
		this.meterFileName = meterFileName;
	}

	/**
	 * Function returns the value of attribute rootDir
	 * @return the rootDir
	 */
	public String getRootDir() {
		return rootDir;
	}

	/**
	 * Function sets the value for attribute rootDir
	 * @param rootDir the rootDir to set
	 */
	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	public void generateBenchZeroFlowDataFileName(){
		zeroFlowFileName = generateFileName("bench_data_zero_flow");
	}
	
	public void generateBenchDataFileName(){
		benchDataFileName = generateFileName("bench_data");
	}
	
	public void generateBenchVerifDataFileName(){
		benchVerifDataFileName = generateFileName("bench_verif_data");
	}
	
	public String generateFileName(String prefix){
		date = spdf.format(new Date());
		String retorno = prefix.replace('.', '_')+"_"+date+".mat";
		return retorno;
	}
	
	/**
	 * Checks if the directory for the batch exists. Returns true if it exists or was created.
	 * Also sets the object parameter that holds the current full directory being used.
	 * @param bacthName - the batch number in production.
	 * @param run - the run that is to save the data for
	 * @return true if directory already exists or if it was created
	 */
	public boolean createDir(String bacthName, int run){
		fullDir = this.rootDir+bacthName+"/"+run;
		boolean retorno = false;
		if(dirExists(fullDir)){
			retorno = true;
		}else{
			File dir = new File(fullDir);
			retorno = dir.mkdirs();
		}
		return retorno;
	}
	
	public boolean createMeterDir( String meterDir){
		boolean retorno = false;
		if(dirExists(meterDir)){
			retorno = true;
		}else{
			File dir = new File(meterDir);
			retorno = dir.mkdirs();
		}
		return retorno;
	}
	
	public boolean f(String dirString){
		
		boolean retorno = false;
		if(dirExists(dirString)){
			retorno = true;
		}else{
			File dir = new File(dirString);
			retorno = dir.mkdirs();
		}
		return retorno;
	}
	
	public boolean dirExists(String dirString){
		File dir = new File(dirString);
		return dir.exists();
	}
	
	public MeterModel retrieveConfigFromMatFile(String filePath){
		MeterModel retorno = null;
//		String fileName = filePath.substring(filePath.lastIndexOf('\\')+1, filePath.lastIndexOf(".mat")+4);
		if(dirExists(filePath)){
			MatFileReader matfilereader;
			MLDouble dataMl = null;
			try {
				matfilereader = new MatFileReader(filePath);
				dataMl = (MLDouble) matfilereader.getMLArray("config");
				
				if(dataMl != null){
					double[][] auxDataArray = dataMl.getArray();
					retorno = new MeterModel();
					MeterTypeModel meterType = new MeterTypeModel();
					int j = 0;
					int i = 0;
					retorno.setDsos(auxDataArray[i][j]);
					retorno.setDzc(auxDataArray[i][++j]);
					meterType.setReductionDiameter(auxDataArray[i][++j]);
					meterType.setUltraSoundPathLengh(auxDataArray[i][++j]);
					meterType.setCarcassLength(auxDataArray[i][++j]);
					retorno.setMeterTypeModel(meterType);
				}
				

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}
			
		}
		
		return retorno;
	}
	
	public ArrayList<CalibConstantsModel> retrieveConstantsFromMatFile(String filePath){
		ArrayList<CalibConstantsModel> retorno = null;
//		String fileName = filePath.substring(filePath.lastIndexOf('\\')+1, filePath.lastIndexOf(".mat")+4);
		if(dirExists(filePath)){
			MatFileReader matfilereader;
			MLDouble dataMl = null;
			try {
				matfilereader = new MatFileReader(filePath);
				dataMl = (MLDouble) matfilereader.getMLArray("calibLut");
				
				if(dataMl != null){
					double[][] auxDataArray = dataMl.getArray();
					CalibConstantsModel calibConst;
					 retorno = new ArrayList<CalibConstantsModel>();
					int j=0;
					for(int i = 0; i< auxDataArray.length; i++){
						calibConst = new CalibConstantsModel();
						calibConst.setFlowRate(auxDataArray[i][j]);
						calibConst.setAvgFlowRate(auxDataArray[i][++j]);
						calibConst.setConstantK(auxDataArray[i][++j]);
						calibConst.setReynolds(auxDataArray[i][++j]);
						calibConst.setAvgTemp(auxDataArray[i][++j]);
						calibConst.setAvgPressure(auxDataArray[i][++j]);
						retorno.add(calibConst);
						j = 0;
					}
				}
				

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}
			
		}
		
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public boolean saveBenchZeroData(ArrayList<BenchDataModel> benchDatas){
		boolean retorno = false;
		
		if(!benchDatas.isEmpty()){
			double[][] data = new double[benchDatas.size()][8];
			int i = 0;
			int j = 0;
			
			for(BenchDataModel benchData : benchDatas){
				data[i][j++] = benchData.getExpectedFlowRate(); 
				data[i][j++] = benchData.getFlowRate();
				data[i][j++] = benchData.getPulseCalcFlowRate(); 
				data[i][j++] = benchData.getTemperatureLi();
				data[i][j++] = benchData.getTemperatureLo();
				data[i][j++] = benchData.getPressure();
				data[i][j++] = benchData.getCounter();
				data[i][j++] = benchData.getTimeVal();
				i++;
				j=0;
			}
			MLDouble dataMl = new MLDouble("dataZeroBenchBuff", data);
			@SuppressWarnings("rawtypes")
			ArrayList list = new ArrayList<MLDouble>();
			list.add(dataMl);
			
			try {
				matWriter = new MatFileWriter(fullDir+"/"+this.zeroFlowFileName, list);
			} catch (IOException e) {
				e.printStackTrace();
			}
			retorno = true;
		}else{
			retorno = false;
			System.err.println("ERROR: Calib Constants array is empty on MatFileHandlerUtil.saveMeterConstantsData().");
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public boolean saveBenchData(ArrayList<BenchDataModel> benchDatas){
		boolean retorno = false;
		if(!benchDatas.isEmpty()){
			double[][] data = new double[benchDatas.size()][8];
			int i = 0;
			int j = 0;
			for(BenchDataModel benchData : benchDatas){
				data[i][j++] = benchData.getExpectedFlowRate(); 
				data[i][j++] = benchData.getPulseCalcFlowRate(); 
				data[i][j++] = benchData.getFlowRate();
				data[i][j++] = benchData.getTemperatureLi();
				data[i][j++] = benchData.getTemperatureLo();
				data[i][j++] = benchData.getPressure();
				data[i][j++] = benchData.getCounter();
				data[i][j++] = benchData.getTimeVal();
				i++;
				j=0;
			}
			MLDouble dataMl = new MLDouble("dataBenchbuff", data);
			@SuppressWarnings("rawtypes")
			ArrayList list = new ArrayList<MLDouble>();
			list.add(dataMl);
			
			try {
				matWriter = new MatFileWriter(fullDir+"/"+this.benchDataFileName, list);
			} catch (IOException e) {
				System.err.println("ERROR: IOException on MatFileHandlerUtil.saveMeterConstantsData(). Error "+e.getMessage());
			}
			retorno = true;
		}else{
			retorno = false;
			System.err.println("ERROR: Calib Constants array is empty on MatFileHandlerUtil.saveMeterConstantsData().");
		}
		return retorno;
	}	
	
	@SuppressWarnings("unchecked")
	public boolean saveBenchVerifData(ArrayList<BenchDataModel> benchDatas){
		boolean retorno = false;
		if(!benchDatas.isEmpty()){
			double[][] data = new double[benchDatas.size()][8];
			int i = 0;
			int j = 0;
			for(BenchDataModel benchData : benchDatas){
				data[i][j++] = benchData.getExpectedFlowRate(); 
				data[i][j++] = benchData.getPulseCalcFlowRate(); 
				data[i][j++] = benchData.getFlowRate();
				data[i][j++] = benchData.getTemperatureLi();
				data[i][j++] = benchData.getTemperatureLo();
				data[i][j++] = benchData.getPressure();
				data[i][j++] = benchData.getCounter();
				data[i][j++] = benchData.getTimeVal();
				i++;
				j=0;
			}
			MLDouble dataMl = new MLDouble("dataBenchbuff", data);
			@SuppressWarnings("rawtypes")
			ArrayList list = new ArrayList<MLDouble>();
			list.add(dataMl);
			
			try {
				matWriter = new MatFileWriter(fullDir+"/"+this.benchVerifDataFileName, list);
			} catch (IOException e) {
				System.err.println("ERROR: IOException on MatFileHandlerUtil.saveMeterConstantsData(). Error "+e.getMessage());
			}
			retorno = true;
		}else{
			retorno = false;
			System.err.println("ERROR: benchDatas array is empty on MatFileHandlerUtil.saveMeterConstantsData().");
		}
		return retorno;
	}	
	
	/**
	 * This method considers the bencha data is created first to attribute the date value
	 * @param prefix
	 * @return
	 */
	public String generateMeterFileName(String prefix){
		String retorno = prefix.replace('.', '_')+"/"+date+".mat";
		this.meterFileName = retorno;
		return retorno;
				
	}
	
	
	public String generateMeterDir(String prefix){
		meterDir = fullDir+"/"+prefix.replace('.', '_')+"/";
		return meterDir;
	}
	
	public String generateMeterFileName(String prefix, Date date){
		this.date = spdf.format(date);
		meterDir = fullDir+"/"+prefix.replace('.', '_')+"/";
		String retorno = this.date+".mat";
		this.meterFileName = retorno;
		return retorno;
	}
	
	public void saveMeterData(ArrayList<MeterDataModel> meterDatas, String fileName){
		this.meterFileName = fileName;
		saveMeterData(meterDatas);
	}
	
	@SuppressWarnings("unchecked")
	public boolean saveMeterConstantsData(ArrayList<CalibConstantsModel> calibConsts, String meterFileName){
		boolean retorno = false;
		if(!calibConsts.isEmpty()){
			int dataColums = 6;
			
			double[][] data = new double[calibConsts.size()][dataColums];
			int i = 0;
			int j = 0;
			for(CalibConstantsModel calibConst: calibConsts){
				data[i][j++] = calibConst.getFlowRate();
				data[i][j++] = calibConst.getAvgFlowRate();
				data[i][j++] = calibConst.getConstantK();
				data[i][j++] = calibConst.getReynolds();
				data[i][j++] = calibConst.getAvgTemp();
				data[i][j++] = calibConst.getAvgPressure();
				i++;
				j=0;
			}
			MLDouble dataMl = new MLDouble("calibLut", data);
			@SuppressWarnings("rawtypes")
			ArrayList list = new ArrayList<MLDouble>();
			list.add(dataMl);
			try {
				matWriter = new MatFileWriter(meterDir+"calib_lut_consts_"+meterFileName, list);
			} catch (IOException e) {
				System.err.println("ERROR: IOException on MatFileHandlerUtil.saveMeterConstantsData(). Error "+e.getMessage());
			}
			retorno = true;
		}else{
			retorno = false;
			System.err.println("ERROR: Calib Constants array is empty on MatFileHandlerUtil.saveMeterConstantsData().");
		}
		return retorno;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean saveMeterVerifErrors(ArrayList<VerificationErrorModel> errors, String meterFileName){
		boolean retorno = false;
		if(errors != null){
			
			if(!errors.isEmpty()){
				int dataColums = 3;
				
				double[][] data = new double[errors.size()][dataColums];
				int i = 0;
				int j = 0;
				for(VerificationErrorModel error : errors){
					data[i][j++] = error.getError();
					data[i][j++] = error.getMeanMeterFlowRate();
					data[i][j++] = error.getMeanRefFlowRate();
					i++;
					j=0;
				}
				MLDouble dataMl = new MLDouble("errors", data);
				
				@SuppressWarnings("rawtypes")
				ArrayList list = new ArrayList<MLDouble>();
				list.add(dataMl);
				try {
					matWriter = new MatFileWriter(meterDir+"verif_errors_"+meterFileName, list);
					retorno = true;
				} catch (IOException e) {
					System.err.println("ERROR: IOException on MatFileHandlerUtil.saveMeterVerifErrors() "+meterDir+". Error "+e.getMessage());
					retorno = false;
				}
			}else{
				retorno = false;
				System.err.println("ERROR: Calib Constants array is empty on MatFileHandlerUtil.saveMeterVerifErrors() "+meterDir+".");
			}
		}
		return retorno;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean saveMeterConfig(MeterModel meter, String meterFileName){
		boolean retorno = false;
		
		if(meter != null){
			double[][] data = new double[1][5];
			int i = 0;
			int j = 0;
			data[i][j++] = meter.getDsos();
			data[i][j++] = meter.getDzc();
			data[i][j++] = meter.getMeterTypeModel().getReductionDiameter();
			data[i][j++] = meter.getMeterTypeModel().getUltraSoundPathLengh();
			data[i][j++] = meter.getMeterTypeModel().getCarcassLength();
			i++;
			j=0;
			MLDouble dataMl = new MLDouble("config", data);
			@SuppressWarnings("rawtypes")
			ArrayList list = new ArrayList<MLDouble>();
			list.add(dataMl);
			try {
				matWriter = new MatFileWriter(meterDir+"meter_config_"+meterFileName, list);
			} catch (IOException e) {
				System.err.println("ERROR: IOException on MatFileHandlerUtil.saveMeterConstantsData(). Error "+e.getMessage());
			}
			retorno = true;
		}else{
			retorno = false;
			System.err.println("ERROR: Calib Constants array is empty on MatFileHandlerUtil.saveMeterConstantsData().");
		}
		return retorno;
	}

	/**
	 * @throws IOException 
	 * 
	 */
	public boolean saveFullMeterConfig(String dir, String meterFileName,  byte[] arrayFile) throws IOException{

		// int id = Utils.arrayByteToInt(meterId);

		int idxLine = 0;
		int idxColumn = 0;
		ByteBuffer buff = ByteBuffer.allocate(arrayFile.length).order(ByteOrder.LITTLE_ENDIAN);
		buff.put(arrayFile);

		double[][] meterConfigMatriz = new double[1][115]; // 30 k + 30 Reynolds + 55 Var
//		double[][] meterConfigMatriz = new double[1][125]; // 30 k + 30 Reynolds + 55 Var

		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_MAJOR.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_MINOR.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_PATCH.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_BUILD.getMeterConfigEnum());

		meterConfigMatriz[idxLine][idxColumn++] = buff.getDouble(MeterConfigV108Enum.METER_CONF_CALIB_DATE.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getInt(MeterConfigV108Enum.METER_CONF_SERIAL_NUM_1.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getInt(MeterConfigV108Enum.METER_CONF_SERIAL_NUM_2.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getInt(MeterConfigV108Enum.METER_CONF_SERIAL_NUM_3.getMeterConfigEnum());

		meterConfigMatriz[idxLine][idxColumn++] = buff.getDouble(MeterConfigV108Enum.METER_CONF_LEN.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getDouble(MeterConfigV108Enum.METER_CONF_DIA.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getDouble(MeterConfigV108Enum.METER_CONF_AREA.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getDouble(MeterConfigV108Enum.METER_CONF_DSOS.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getDouble(MeterConfigV108Enum.METER_CONF_DZC.getMeterConfigEnum());

		int lutIndx = MeterConfigV108Enum.METER_CONF_LUT.getMeterConfigEnum();
		for (int i = 0; i < 30; i++) {
			meterConfigMatriz[idxLine][idxColumn++] = buff.getDouble(lutIndx);
			lutIndx = lutIndx + MeterConfigV108Enum.METER_CONF_LUT_k.getNumByte();
			meterConfigMatriz[idxLine][idxColumn++] = buff.getDouble(lutIndx);
			lutIndx = lutIndx + MeterConfigV108Enum.METER_CONF_LUT_R.getNumByte();
		}

		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_SIZE.getMeterConfigEnum());

		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_LOOPC.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_SMARK.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_SPACE.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_ANA_SAMPLE.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_PRE_MEAS_D_CAL.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_PRE_MEAS_D0.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_PRE_MEAS_D1.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_INIT_PULSE_D.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_LAST_PULSE_D.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_REF_CONFIG.getMeterConfigEnum());

		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_STAGE1_AUTO_R1.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_STAGE1_AUTO_GM1.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_STAGE1_CONF_MIN_R1.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_STAGE1_CONF_MIN_GM1.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_STAGE1_CONF_LOWEST.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_STAGE1_CONF_R1_SEARCH.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_STAGE1_CONF_GM1_SEARCH.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_STAGE2_A2.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_STAGE2_R2.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_OTHER_SEL_HP.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_OTHER_SEL_PAD_BP.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_PTRIG_LEV.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_PTRIG_LEV_LOW.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_PTRIG_LEV_HIGH.getMeterConfigEnum());

		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_PCONFIG.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_PCONFIG0.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_PCONFIG1.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_PCONFIG2.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_PCONFIG3.getMeterConfigEnum());

		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_EXC_LEN.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_EXC_CONFIG_SEL_N70.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_MSEQ_LEVEL.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_MSEQ_ENABLE.getMeterConfigEnum());

		meterConfigMatriz[idxLine][idxColumn++] = buff.getDouble(MeterConfigV108Enum.METER_CONF_PULSE_K.getMeterConfigEnum());

		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_RESOLUTION.getMeterConfigEnum());

		meterConfigMatriz[idxLine][idxColumn++] = buff.get(MeterConfigV108Enum.METER_CONF_LOG_INTERVAL.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.get(MeterConfigV108Enum.METER_CONF_WEEKDAY.getMeterConfigEnum());

		meterConfigMatriz[idxLine][idxColumn++] = buff.getDouble(MeterConfigV108Enum.METER_CONF_WMBUS_SERIAL_NUM.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getDouble(MeterConfigV108Enum.METER_CONF_WMBUS_CIPHERKEY_1.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getDouble(MeterConfigV108Enum.METER_CONF_WMBUS_CIPHERKEY_2.getMeterConfigEnum());
		meterConfigMatriz[idxLine][idxColumn++] = buff.getShort(MeterConfigV108Enum.METER_CONF_WMBUS_INTERVAL.getMeterConfigEnum());

		MLDouble dataMlMeterConf = new MLDouble("meter_conf", meterConfigMatriz);
		// MLInt8 dataMlMeterConf = new MLInt8("meter_conf", arrayFile, 1); // Number rows = 1
		List<MLArray> towrite = new ArrayList<MLArray>();
		towrite.add(dataMlMeterConf);

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "meter_conf_" + meterFileName + "_" + format.format(new Date()) + ".mat";
		String directoryName = dir;

		File directory = new File(directoryName);
		if (!directory.exists()) {
			directory.mkdir();
		}

		File file = new File(directoryName + "/" + fileName);
		new MatFileWriter(file, towrite);
		return true;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean saveMeterVerifData(ArrayList<MeterDataModel> meterDatas, String meterFileName){
		boolean retorno = false;
		if(!meterDatas.isEmpty()){
			int dataColums = 12;
			
			double[][] data = new double[meterDatas.size()][dataColums];
			int i = 0;
			int j = 0;
			for(MeterDataModel meterData : meterDatas){
				data[i][j++]= meterData.getExpectedFlowRate(); 
				data[i][j++] = meterData.getHfc();
				data[i][j++] = meterData.getAgStatus();
				data[i][j++] = meterData.getGm1();
				data[i][j++] = meterData.getR1();
				data[i][j++] = meterData.getTtstd();
				data[i][j++] = meterData.getTtrev();
				data[i][j++] = meterData.getVel();
				data[i][j++] = meterData.getVelRe();
				data[i][j++] = meterData.getVolFlowRe();
				data[i][j++] = meterData.getAccReVolume();
				data[i][j++] = meterData.getAccReVolumeRev();
				i++;
				j=0;
			}
			MLDouble dataMl = new MLDouble("dataBuff", data);
			@SuppressWarnings("rawtypes")
			ArrayList list = new ArrayList<MLDouble>();
			list.add(dataMl);
			
			try {
//			dir+meterName+".mat"
				matWriter = new MatFileWriter(meterDir+"verif_flowrate_data_"+meterFileName, list);
			} catch (IOException e) {
				System.err.println("ERROR: IOException on MatFileHandlerUtil.saveMeterVerifData(). Error "+e.getMessage());
			}
			retorno = true;
		}else{
			retorno = false;
			System.err.println("ERROR: Calib Constants array is empty on MatFileHandlerUtil.saveMeterVerifData().");
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public boolean saveMeterData(ArrayList<MeterDataModel> meterDatas){
		boolean retorno = false;
		if(!meterDatas.isEmpty()){
			int dataColums = 12;
			
			double[][] data = new double[meterDatas.size()][dataColums];
			int i = 0;
			int j = 0;
			for(MeterDataModel meterData : meterDatas){
				data[i][j++]= meterData.getExpectedFlowRate(); 
				data[i][j++] = meterData.getHfc();
				data[i][j++] = meterData.getAgStatus();
				data[i][j++] = meterData.getGm1();
				data[i][j++] = meterData.getR1();
				data[i][j++] = meterData.getTtstd();
				data[i][j++] = meterData.getTtrev();
				data[i][j++] = meterData.getVel();
				data[i][j++] = meterData.getVelRe();
				data[i][j++] = meterData.getVolFlowRe();
				data[i][j++] = meterData.getAccReVolume();
				data[i][j++] = meterData.getAccReVolumeRev();
				i++;
				j=0;
			}
			MLDouble dataMl = new MLDouble("dataBuff", data);
			@SuppressWarnings("rawtypes")
			ArrayList list = new ArrayList<MLDouble>();
			list.add(dataMl);
			
			try {
//			dir+meterName+".mat"
				matWriter = new MatFileWriter(meterDir+"flowrate_data_"+meterFileName, list);
			} catch (IOException e) {
				System.err.println("ERROR: IOException on MatFileHandlerUtil.saveMeterData(). Error "+e.getMessage());
			}
			retorno = true;
		}else{
			retorno = false;
			System.err.println("ERROR: Calib Constants array is empty on MatFileHandlerUtil.saveMeterData().");
		}
		return retorno;
	}
	
	public void saveMeterDataZeroFlow(ArrayList<MeterDataModel> meterDatas, String filename){
		this.meterFileName = filename;
		saveMeterDataZeroFlow(meterDatas);
	}
	
	@SuppressWarnings("unchecked")
	public boolean saveMeterDataZeroFlow(ArrayList<MeterDataModel> meterDatas){
			boolean retorno = false;
			if(!meterDatas.isEmpty()){
				int dataColums = 12;
				
				double[][] data = new double[meterDatas.size()][dataColums];
				int i = 0;
				int j = 0;
				for(MeterDataModel meterData : meterDatas){
					data[i][j++] = 0; 
					data[i][j++] = meterData.getHfc();
					data[i][j++] = meterData.getAgStatus();
					data[i][j++] = meterData.getGm1();
					data[i][j++] = meterData.getR1();
					data[i][j++] = meterData.getTtstd();
					data[i][j++] = meterData.getTtrev();
					data[i][j++] = meterData.getVel();
					data[i][j++] = meterData.getVelRe();
					data[i][j++] = meterData.getVolFlowRe();
					data[i][j++] = meterData.getAccReVolume();
					data[i][j++] = meterData.getAccReVolumeRev();
					i++;
					j=0;
				}
				
				MLDouble dataMl = new MLDouble("dataZerobuff", data);
				@SuppressWarnings("rawtypes")
				ArrayList list = new ArrayList<MLDouble>();
				list.add(dataMl);
				
				if(Files.notExists(Paths.get(meterDir), LinkOption.NOFOLLOW_LINKS)){
					new File(meterDir).mkdirs();
				}
				try {
					matWriter = new MatFileWriter(meterDir+"/"+"zeroflow_data_"+this.meterFileName, list);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				retorno = true;
			}else{
				retorno = false;
				System.err.println("ERROR: Meter ZEROFLOW data array is empty on MatFileHandlerUtil.saveMeterDataZeroFlow()");
			}
		return retorno;
	}
	
}

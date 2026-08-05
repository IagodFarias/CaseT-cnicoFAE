//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ViewDataUtil.java
*    @author marcos
*    @date 24 de out de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.util;

import java.util.ArrayList;
import java.util.HashMap;

import si.dbcomm.model.BenchDataModel;
import si.dbcomm.util.ConversorNumbers;
import view.bean.MeterBean;

/**
 * @author marcos
 *
 */
public class ViewDataUtil {

	private static ViewDataUtil INSTANCE = new ViewDataUtil();

	private ArrayList<BenchDataModel> benchData = new ArrayList<>();

	public static HashMap<ConversorNumbers, MeterBean> meterBeans = new HashMap<>();

//	private ProcessConfigModel processConfig;
//	private ProcessConfigModel processConfig = new ProcessConfigModel();

	public ViewDataUtil() {

	}

	/**
	 * Function returns the value of attribute iNSTANCE
	 * 
	 * @return the INSTANCE
	 */
	public static ViewDataUtil getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ViewDataUtil();
		}
		return INSTANCE;
	}

	/**
	 * Function returns the value of attribute benchData
	 * 
	 * @return the benchData
	 */
	public ArrayList<BenchDataModel> getBenchData() {
		return benchData;
	}

	/**
	 * Function sets the value for attribute benchData
	 * 
	 * @param benchData
	 *            the benchData to set
	 */
	public void setBenchData(ArrayList<BenchDataModel> benchData) {
		this.benchData = benchData;
	}

//	/**
//	 * Function returns the value of attribute processConfig
//	 * @return the processConfig
//	 */
//	public ProcessConfigModel getProcessConfig() {
//		return processConfig;
//	}
//
//	/**
//	 * Function sets the value for attribute processConfig
//	 * @param processConfig the processConfig to set
//	 */
//	public void setProcessConfig(ProcessConfigModel processConfig) {
//		this.processConfig = processConfig;
//	}	
}

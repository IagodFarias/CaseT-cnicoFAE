//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ProcessConfigController.java
*    @author marcos
*    @date 10 de nov de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.ProcessConfigModel;
import si.dbcomm.service.ProcessConfigService;

/**
 * @author marcos
 *
 */
public class ProcessConfigController {
	
	private ProcessConfigModel processConfig;
	
	private List<ProcessConfigModel> processes;
	
	private ProcessConfigService processConfigService;
	
	private BatchController batchController;
	
	/**
	 * Creates a object for class ProcessConfigController.java
	 */
	public ProcessConfigController() {
		this.processConfig = new ProcessConfigModel();
		this.processes = new ArrayList<>();
		this.processConfigService = new ProcessConfigService();
		this.batchController = new BatchController();
	}

	/**
	 * Function returns the value of attribute processConfig
	 * @return the processConfig
	 */
	public ProcessConfigModel getProcessConfig() {
		return processConfig;
	}

	/**
	 * Function sets the value for attribute processConfig
	 * @param processConfig the processConfig to set
	 */
	public void setProcessConfig(ProcessConfigModel processConfig) {
		this.processConfig = processConfig;
	}

	/**
	 * Function returns the value of attribute processes
	 * @return the processes
	 */
	public List<ProcessConfigModel> getProcesses() {
		return processes;
	}

	/**
	 * Function sets the value for attribute processes
	 * @param processes the processes to set
	 */
	public void setProcesses(List<ProcessConfigModel> processes) {
		this.processes = processes;
	}

	/**
	 * Function returns the value of attribute processConfigService
	 * @return the processConfigService
	 */
	public ProcessConfigService getProcessConfigService() {
		return processConfigService;
	}

	/**
	 * Function sets the value for attribute processConfigService
	 * @param processConfigService the processConfigService to set
	 */
	public void setProcessConfigService(ProcessConfigService processConfigService) {
		this.processConfigService = processConfigService;
	}
	
	public Set<FlowRateModel> findCalibFlowRatesByProcessConfig(ProcessConfigModel processConfig){
		return processConfigService.findFlowRatesByProcessConfig(processConfig);
	}
	
	public ArrayList<FlowRateModel> findVerifFlowRatesByProcessConfig(ProcessConfigModel processConfig){
		return (ArrayList<FlowRateModel>) processConfigService.findVerifFlowRatesByProcessConfig(processConfig);
	}
	
}

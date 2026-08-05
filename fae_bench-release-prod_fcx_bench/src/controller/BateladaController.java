//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BateladaController.java
*    @author marcos
*    @date 22 de mar de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;

import java.util.ArrayList;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BateladaModel;
import si.dbcomm.model.ProcessConfigModel;
import si.dbcomm.service.BateladaService;
import si.dbcomm.service.ProcessConfigService;

/**
 * @author marcos
 *
 */
public class BateladaController {

	private BateladaService bateladaService;

	private ArrayList<BateladaModel> bateladas;

	public BateladaController() {
		bateladaService = new BateladaService();
		bateladas = new ArrayList<>();
	}

	/**
	 * Function returns the value of attribute bateladaService
	 * 
	 * @return the bateladaService
	 */
	public BateladaService getBateladaService() {
		return bateladaService;
	}

	/**
	 * Function sets the value for attribute bateladaService
	 * 
	 * @param bateladaService
	 *            the bateladaService to set
	 */
	public void setBateladaService(BateladaService bateladaService) {
		this.bateladaService = bateladaService;
	}

	/**
	 * Function returns the value of attribute bateladas
	 * 
	 * @return the bateladas
	 */
	public ArrayList<BateladaModel> getBateladas() {
		return bateladas;
	}

	/**
	 * Function sets the value for attribute bateladas
	 * 
	 * @param bateladas
	 *            the bateladas to set
	 */
	public void setBateladas(ArrayList<BateladaModel> bateladas) {
		this.bateladas = bateladas;
	}

	// public boolean createInitBatelada(BatchModel batch) {
	// boolean retorno = false;
	// BateladaModel newBatelada = new BateladaModel();
	// newBatelada.setApprovedMeters(0);
	// newBatelada.setBatch(batch);
	// newBatelada.setRunCount(0);
	// newBatelada.setProcessesConfigModel(ViewDataUtil.getInstance().getProcessConfig());
	//
	// try {
	// if (bateladaService.persist(newBatelada).getId() != null) {
	// retorno = true;
	// }
	// } catch (CrudDatabaseException e) {
	// e.printStackTrace();
	// }
	// return retorno;
	// }

	public boolean createInitBatelada(BatchModel batch) {
		boolean retorno = false;
		// TODO: Osmenio
		ProcessConfigService processConfigService = new ProcessConfigService();
		ProcessConfigModel processConfig = processConfigService.findById(batch.getIdxProcessConfig());

		BateladaModel newBatelada = new BateladaModel();
		newBatelada.setApprovedMeters(0);
		newBatelada.setBatch(batch);
		newBatelada.setRunCount(0);
		newBatelada.setProcessesConfigModel(processConfig);

		try {
			if (bateladaService.persist(newBatelada).getId() != null) {
				retorno = true;
			}
		} catch (CrudDatabaseException e) {
			e.printStackTrace();
		}
		return retorno;
	}
}

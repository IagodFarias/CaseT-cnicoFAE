//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ConversorController.java
*    @author marcos
*    @date 31 de mar de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;

import java.util.ArrayList;
import java.util.List;

import si.dbcomm.model.ConversorModel;
import si.dbcomm.service.ConversorService;

/**
 * @author marcos
 *
 */
public class ConversorController  {

	ConversorModel conversor;
	
	ConversorService conversorService;
	
	List<ConversorModel> conversors;
	
	public ConversorController(){
		conversor = new ConversorModel();
		conversorService = new ConversorService();
		conversors = new ArrayList<ConversorModel>();
	}
	
	
	/**
	 * Retrieves lis with all conversors
	 * @return List with all conversors
	 */
	public List<ConversorModel> getAllConversors(){
		List<ConversorModel> retorno = new ArrayList<ConversorModel>();
		if(conversors.isEmpty()){
			retorno = conversorService.findAll();
			conversors = new ArrayList<>(retorno);
		}
		return retorno;
	}
	
	public ConversorModel getConversorFormDbByIp(String ip){
		return conversorService.findByIp(ip);
	}
}

//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file RamalController.java
*    @author marcos
*    @date 18 de nov de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;

import java.util.ArrayList;
import java.util.List;

import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.RamalModel;
import si.dbcomm.model.ValveModel;
import si.dbcomm.service.RamalService;
import util.DeviceTagReaderUtil;

/**
 * @author marcos
 *
 */
public class RamalController {
	private RamalModel ramal;
	
	private List<RamalModel> ramais;
	
	private RamalService ramalService;
	
	private ValveController valveController;
	
	public static final String ramal1Tag = DeviceTagReaderUtil.getProperty("ramal1");
	
	public static final String ramal2Tag = DeviceTagReaderUtil.getProperty("ramal2");
	
	public static final String ramal3Tag = DeviceTagReaderUtil.getProperty("ramal3");
	
	public static final String ramal4Tag = DeviceTagReaderUtil.getProperty("ramal4");
	
	public static final String ramal5Tag = DeviceTagReaderUtil.getProperty("ramal5");
	
	public static final String ramal6Tag = DeviceTagReaderUtil.getProperty("ramal6");
	
	public static final String ramal7Tag = DeviceTagReaderUtil.getProperty("ramal7");
	
	public static final String ramal8Tag = DeviceTagReaderUtil.getProperty("ramal8");
	
	public static final String ramal9Tag = DeviceTagReaderUtil.getProperty("ramal9");
	
	public static final String ramal10Tag = DeviceTagReaderUtil.getProperty("ramal10");
	
	/**
	 * Creates a object for class RamalController.java
	 */
	public RamalController() {
		this.ramal = new RamalModel();
		this.ramais = new ArrayList<>();
		this.ramalService = new RamalService();
		this.ramais = ramalService.findAll();
		this.valveController = new ValveController();
	}

	/**
	 * Function returns the value of attribute ramal
	 * @return the ramal
	 */
	public RamalModel getRamal() {
		return ramal;
	}

	/**
	 * Function sets the value for attribute ramal
	 * @param ramal the ramal to set
	 */
	public void setRamal(RamalModel ramal) {
		this.ramal = ramal;
	}

	/**
	 * Function returns the value of attribute ramais
	 * @return the ramais
	 */
	public List<RamalModel> getRamais() {
		return ramais;
	}

	/**
	 * Function sets the value for attribute ramais
	 * @param ramais the ramais to set
	 */
	public void setRamais(List<RamalModel> ramais) {
		this.ramais = ramais;
	}
	
	/**
	 * Closes the ramal valve 
	 * @param ramal
	 * @return true if ramal has been closed. false case operation has not been executed
	 */
	public boolean closeRamal(String ramalTag){
		boolean retorno = false;
		if(ramalTag != null){
			if(!ramalTag.isEmpty()){
				RamalModel ramalAux = ramalService.findByTag(ramalTag);
				if(valveController.closeValve(ramalAux.getValve())){
					retorno = true;
				}else{
					retorno = false;
				}
			}
		}
		return retorno;
	}
	
	/**
	 * Closes the ramal valve 
	 * @param ramal
	 * @return true if ramal has been closed. false case operation has not been executed
	 */
	public boolean closeRamal(RamalModel ramal){
		boolean retorno = false;
		if(ramal != null){
			ValveModel valveAux;
			if((valveAux = ramal.getValve())!= null){
				if(valveController.closeValve(valveAux)){
					retorno = true;
				}else{
					retorno = false;
				}
			}
		}
		return retorno;
	}

	/**
	 * Opens the ramal valve to allow flow or pressure control
	 * @param ramal
	 * @return true if ramal has been opened. false case operation has not been executed
	 */
	public boolean openRamal(RamalModel ramal){
		boolean retorno = false;
		if(ramal != null){
			ValveModel valveAux;
			if((valveAux = ramal.getValve())!= null){
				if(valveController.openValve(valveAux)){
					retorno = true;
					
				}else{
					retorno = false;
				}
			}
		}
		return retorno;
	}
	
	public boolean openRamal(String ramalTag){
		boolean retorno = false;
		if(ramal != null){
			ramal =  ramalService.findByTag(ramalTag);
			ValveModel valveAux;
			if((valveAux = ramal.getValve())!= null){
				if(valveController.openValve(valveAux)){
					retorno = true;
					
				}else{
					retorno = false;
				}
			}
		}
		return retorno;
	}
	
	/**
	 * This method opens a ramal according to the default maxFlowRate and minflowRate values set to the model
	 * It is a default setting. The ramal can be choosen dinamicaly for each flowrate. In case they are not determined, this will be used
	 * @param flowrate
	 * @return
	 */
	public boolean openDefaultRamal(FlowRateModel flowrate){
		boolean retorno = false;
		if(flowrate != null){
			ramais = ramalService.findAll();
			double auxFlowRate = 0.0;
			for(RamalModel ramal : ramais){
				auxFlowRate = flowrate.getFlowRate();
				if(auxFlowRate < ramal.getMaxFlowRate() && auxFlowRate >= ramal.getMinFlowRate()){
					if(openRamal(ramal)){
						flowrate.setRamal(ramal);
						retorno = true;
						break;
					}else{
						retorno = false;
					}
				}
			}
			
		}
		return retorno;
	}
	
	public boolean closeAllRamal(){
		boolean retorno = true;
		if(!closeRamal(ramal1Tag)){
			retorno = false;
		}
		if(!closeRamal(ramal2Tag)){
			retorno = false;
		}
		if(!closeRamal(ramal3Tag)){
			retorno = false;
		}
		if(!closeRamal(ramal4Tag)){
			retorno = false;
		}
		if(!closeRamal(ramal5Tag)){
			retorno = false;
		}
		if(!closeRamal(ramal6Tag)){
			retorno = false;
		}
		if(!closeRamal(ramal7Tag)){
			retorno = false;
		}
		if(!closeRamal(ramal8Tag)){
			retorno = false;
		}
		if(!closeRamal(ramal9Tag)){
			retorno = false;
		}
		if(!closeRamal(ramal10Tag)){
			retorno = false;
		}
		return retorno;
	}
	
	public boolean openAllRamal(){
		boolean retorno = true;
		if(!openRamal(ramal1Tag)){
			retorno = false;
		}
		if(!openRamal(ramal2Tag)){
			retorno = false;
		}
		if(!openRamal(ramal3Tag)){
			retorno = false;
		}
		if(!openRamal(ramal4Tag)){
			retorno = false;
		}
		if(!openRamal(ramal5Tag)){
			retorno = false;
		}
		if(!openRamal(ramal6Tag)){
			retorno = false;
		}
		if(!openRamal(ramal7Tag)){
			retorno = false;
		}
		if(!openRamal(ramal8Tag)){
			retorno = false;
		}
		if(!openRamal(ramal9Tag)){
			retorno = false;
		}
		if(!openRamal(ramal10Tag)){
			retorno = false;
		}
		return retorno;
	}
	
	public boolean closeAllRamalExcept(RamalModel ramal){
		boolean retorno = false;
		if(ramal.getTag().equals(ramal1Tag)){
			if(closeRamal(ramal2Tag) &&
					closeRamal(ramal3Tag) &&	
					closeRamal(ramal4Tag) &&	
					closeRamal(ramal5Tag) &&	
					closeRamal(ramal6Tag) &&	
					closeRamal(ramal7Tag) &&	
					closeRamal(ramal8Tag) &&	
					closeRamal(ramal9Tag) &&	
					closeRamal(ramal10Tag) 	
					){
				retorno = true;
			}
		}else{
			if(ramal.getTag().equals(ramal2Tag)){
				if(closeRamal(ramal1Tag) &&
						closeRamal(ramal3Tag) &&	
						closeRamal(ramal4Tag) &&	
						closeRamal(ramal5Tag) &&	
						closeRamal(ramal6Tag) &&	
						closeRamal(ramal7Tag) &&	
						closeRamal(ramal8Tag) &&	
						closeRamal(ramal9Tag) &&	
						closeRamal(ramal10Tag) 	
						){
					retorno = true;
				}
			}else{
				if(ramal.getTag().equals(ramal3Tag)){
					if(closeRamal(ramal1Tag) &&
							closeRamal(ramal2Tag) &&	
							closeRamal(ramal4Tag) &&	
							closeRamal(ramal5Tag) &&	
							closeRamal(ramal6Tag) &&	
							closeRamal(ramal7Tag) &&	
							closeRamal(ramal8Tag) &&	
							closeRamal(ramal9Tag) &&	
							closeRamal(ramal10Tag) 	
							){
						retorno = true;
					}
				}else{
					if(ramal.getTag().equals(ramal4Tag)){
						if(closeRamal(ramal1Tag) &&
								closeRamal(ramal2Tag) &&	
								closeRamal(ramal3Tag) &&	
								closeRamal(ramal5Tag) &&	
								closeRamal(ramal6Tag) &&	
								closeRamal(ramal7Tag) &&	
								closeRamal(ramal8Tag) &&	
								closeRamal(ramal9Tag) &&	
								closeRamal(ramal10Tag) 	
								){
							retorno = true;
						}
					}else{
						if(ramal.getTag().equals(ramal5Tag)){
							if(closeRamal(ramal1Tag) &&
									closeRamal(ramal2Tag) &&	
									closeRamal(ramal3Tag) &&	
									closeRamal(ramal4Tag) &&	
									closeRamal(ramal6Tag) &&	
									closeRamal(ramal7Tag) &&	
									closeRamal(ramal8Tag) &&	
									closeRamal(ramal9Tag) &&	
									closeRamal(ramal10Tag) 	
									){
								retorno = true;
							}
						}else{
							if(ramal.getTag().equals(ramal6Tag)){
								if(closeRamal(ramal1Tag) &&
										closeRamal(ramal2Tag) &&	
										closeRamal(ramal3Tag) &&	
										closeRamal(ramal4Tag) &&	
										closeRamal(ramal5Tag) &&	
										closeRamal(ramal7Tag) &&	
										closeRamal(ramal8Tag) &&	
										closeRamal(ramal9Tag) &&	
										closeRamal(ramal10Tag) 	
										){
									retorno = true;
								}
							}else{
								if(ramal.getTag().equals(ramal7Tag)){
									if(closeRamal(ramal1Tag) &&
											closeRamal(ramal2Tag) &&	
											closeRamal(ramal3Tag) &&	
											closeRamal(ramal4Tag) &&	
											closeRamal(ramal5Tag) &&	
											closeRamal(ramal6Tag) &&	
											closeRamal(ramal8Tag) &&	
											closeRamal(ramal9Tag) &&	
											closeRamal(ramal10Tag) 	
											){
										retorno = true;
									}
								}else{
									if(ramal.getTag().equals(ramal8Tag)){
										if(closeRamal(ramal1Tag) &&
												closeRamal(ramal2Tag) &&	
												closeRamal(ramal3Tag) &&	
												closeRamal(ramal4Tag) &&	
												closeRamal(ramal5Tag) &&	
												closeRamal(ramal6Tag) &&	
												closeRamal(ramal7Tag) &&	
												closeRamal(ramal9Tag) &&	
												closeRamal(ramal10Tag) 	
												){
											retorno = true;
										}
									}else{
										if(ramal.getTag().equals(ramal9Tag)){
											if(closeRamal(ramal1Tag) &&
													closeRamal(ramal2Tag) &&	
													closeRamal(ramal3Tag) &&	
													closeRamal(ramal4Tag) &&	
													closeRamal(ramal5Tag) &&	
													closeRamal(ramal6Tag) &&	
													closeRamal(ramal7Tag) &&	
													closeRamal(ramal8Tag) &&	
													closeRamal(ramal10Tag) 	
													){
												retorno = true;
											}
										}else{
											if(ramal.getTag().equals(ramal10Tag)){
												if(closeRamal(ramal1Tag) &&
														closeRamal(ramal2Tag) &&	
														closeRamal(ramal3Tag) &&	
														closeRamal(ramal4Tag) &&	
														closeRamal(ramal5Tag) &&	
														closeRamal(ramal6Tag) &&	
														closeRamal(ramal7Tag) &&	
														closeRamal(ramal8Tag) &&	
														closeRamal(ramal9Tag) 	
														){
													retorno = true;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return retorno;
	}
	
}

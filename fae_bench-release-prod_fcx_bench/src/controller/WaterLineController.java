//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file WaterLineBean.java
*    @author Marcos
*    @date 17 de jun de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;

import bciapi.impl.BenchControlImpl;
import si.dbcomm.enumerations.LineStateEnum;
import si.dbcomm.model.WaterLineModel;
import si.dbcomm.service.WaterLineService;
import util.DeviceTagReaderUtil;

/**
 * @author Marcos
 *
 */
public class WaterLineController extends ActionParent {
	
	private WaterLineModel waterLine;
	
	private WaterLineService lineService;
	
	static final String LINE_TAG = DeviceTagReaderUtil.getProperty("line.tag");
	
	
	/**
	 * Creates a object for class WaterLineBean.java
	 */
	public WaterLineController(WaterLineModel waterLine) {
		this.waterLine = waterLine;
		lineService = new WaterLineService();
	}
	
	/**
	 * Creates a object for class WaterLineBean.java
	 */
	public WaterLineController() {
		this.waterLine = new WaterLineModel();
		this.lineService = new WaterLineService();
	}
	
	/**
	 * Function returns the value of attribute waterLine
	 * @return the waterLine
	 */
	public WaterLineModel getWaterLine() {
		return waterLine;
	}

	/**
	 * Function sets the value for attribute waterLine
	 * @param waterLine the waterLine to set
	 */
	public void setWaterLine(WaterLineModel waterLine) {
		this.waterLine = waterLine;
	}

	/* (non-Javadoc)
	 * @see interfaces.ActionInterface#executeProcedure()
	 */
	@Override
	public void executeAction() {
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		waterLine = bcimpl.getLineState(waterLine);
	}
	
	/**
	 * Opens the bench's water line
	 */
	public boolean openLine(){
		boolean retorno = false;
		if(waterLine==null){
			waterLine = lineService.findByTag(LINE_TAG);
		}else{
			waterLine.setTag(LINE_TAG);
		}
		if(waterLine != null){
			BenchControlImpl bcimpl = BenchControlImpl.getInstance();
			waterLine = bcimpl.openLine(waterLine);
			if(waterLine.getState() == LineStateEnum.OPENED){
				retorno = true;
			}else{
				retorno = false;
			}
		}
		return retorno;
	}
	
	/**
	 * Closes the bench's water line
	 */
	public boolean closeLine(){
		boolean retorno = false;
		if(waterLine==null){
			waterLine = lineService.findByTag(LINE_TAG);
		}else{
			waterLine.setTag(LINE_TAG);
		}
		if(waterLine != null){
			BenchControlImpl bcimpl = BenchControlImpl.getInstance();
			waterLine = bcimpl.closeLine(waterLine);
			if(waterLine.getState() == LineStateEnum.CLOSED){
				retorno = true;
			}else{
				retorno = false;
			}
		}
		return retorno;
	}
	
	/**
	 * Retrieves the line state from the CLP to verify it is closed.
	 * @return true if line is closed.
	 */
	public boolean isLineClosed(){
		boolean retorno = false;
		waterLine = lineService.findByTag(LINE_TAG);
		if(waterLine != null){
			BenchControlImpl bcimpl = BenchControlImpl.getInstance();
			waterLine = bcimpl.getLineState(waterLine);
			if(waterLine.getState() == LineStateEnum.CLOSED){
				retorno = true;
			}else{
				retorno = false;
			}
		}
		return retorno;
	}


}

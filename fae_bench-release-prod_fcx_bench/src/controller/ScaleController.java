//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ScaleBean.java
*    @author Marcos
*    @date 17 de jun de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;

import bciapi.impl.BenchControlImpl;
import si.dbcomm.model.ScaleModel;

/**
 * @author Marcos
 *
 */
public class ScaleController extends ActionParent {

	private ScaleModel scale;
	
	
	/**
	 * Creates a object for class ScaleBean.java
	 */
	public ScaleController(ScaleModel scale) {
		this.scale = scale;
	}

	/**
	 * Function returns the value of attribute scale
	 * @return the scale
	 */
	public ScaleModel getScale() {
		return scale;
	}

	/**
	 * Function sets the value for attribute scale
	 * @param scale the scale to set
	 */
	public void setScale(ScaleModel scale) {
		this.scale = scale;
	}
	
	/* (non-Javadoc)
	 * @see model.ActionParent#executeAction()
	 */
	@Override
	public void executeAction() {
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		scale = bcimpl.getScaleWeight(scale);
		
	}


	public ScaleModel readScale(){
		if(scale != null){
			if(scale.getTag() != null){
				if(!scale.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					scale = bcimpl.getScaleWeight(scale);
				}
			}
		}
		return scale;
	}
	
	/**
	 * Reads the weight on scale 
	 * @param scale to read the weight from
	 * @return object scale with the weight
	 */
	public ScaleModel readScale(ScaleModel scale){
		if(scale != null){
			if(scale.getTag() != null){
				if(!scale.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					scale = bcimpl.getScaleWeight(scale);
				}
			}
		}
		return scale;
	}
	
	
}

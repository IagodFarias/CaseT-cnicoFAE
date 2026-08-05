//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file PumpBean.java
*    @author marcos
*    @date 23 de dez de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.bean;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * @author marcos
 *
 */
public class PumpBean {
	DoubleProperty loadProperty = new SimpleDoubleProperty();
	BooleanProperty stateProperty = new SimpleBooleanProperty();
	
	/**
	 * Function returns the value of attribute loadProperty
	 * @return the loadProperty
	 */
	public DoubleProperty getLoadProperty() {
		return loadProperty;
	}
	/**
	 * Function sets the value for attribute loadProperty
	 * @param loadProperty the loadProperty to set
	 */
	public void setLoadProperty(DoubleProperty loadProperty) {
		this.loadProperty = loadProperty;
	}
	
	public double getLoad(){
		return loadProperty.get();
	}
	
	public void setLoad(double load){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				loadProperty.set(load);
			};
		});
	}
	
	/**
	 * Function returns the value of attribute stateProperty
	 * @return the stateProperty
	 */
	public BooleanProperty getStateProperty() {
		return stateProperty;
	}
	/**
	 * Function sets the value for attribute stateProperty
	 * @param stateProperty the stateProperty to set
	 */
	public void setStateProperty(BooleanProperty stateProperty) {
		this.stateProperty = stateProperty;
	}
	
	public void setState(boolean state){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				stateProperty.set(state);
			};
		});
	}
	public boolean getState(){
		return stateProperty.get();
	}
	
}

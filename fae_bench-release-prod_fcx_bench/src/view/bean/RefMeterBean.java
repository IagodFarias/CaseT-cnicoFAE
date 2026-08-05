//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file RefMeterBean.java
*    @author marcos
*    @date 23 de dez de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.bean;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import view.util.GraphControl;

/**
 * @author marcos
 *
 */
public class RefMeterBean {
	
	private DoubleProperty flowRateProperty = new SimpleDoubleProperty();
	private DoubleProperty volumeProperty = new SimpleDoubleProperty();
	private IntegerProperty pulsesProperty = new SimpleIntegerProperty();
	private IntegerProperty timeReadProperty = new SimpleIntegerProperty();
	private StringProperty stabilityProperty = new SimpleStringProperty();
	private DoubleProperty stdDevRefFlowRateProperty = new SimpleDoubleProperty();
	
	GraphControl  viewGraph;
	
	GraphControl  stdGraph;
	
	/**
	 * Function returns the value of attribute flowRateProperty
	 * @return the flowRateProperty
	 */
	public DoubleProperty getFlowRateProperty() {
		return flowRateProperty;
	}
	
	/**
	 * Function sets the value for attribute flowRateProperty
	 * @param flowRateProperty the flowRateProperty to set
	 */
	public void setFlowRateProperty(DoubleProperty flowRateProperty) {
		this.flowRateProperty = flowRateProperty;
	}
	
	public double getflowRate() {
		return flowRateProperty.get();
	}
	

	public void setFlowRate(double flowRate){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					flowRateProperty.set(flowRate);
				};
			});
	}
	
	/**
	 * Function returns the value of attribute volumeProperty
	 * @return the volumeProperty
	 */
	public DoubleProperty getVolumeProperty() {
		return volumeProperty;
	}
	/**
	 * Function sets the value for attribute volumeProperty
	 * @param volumeProperty the volumeProperty to set
	 */
	public void setVolumeProperty(DoubleProperty volumeProperty) {
		this.volumeProperty = volumeProperty;
	}
	
	public double getVolume() {
		return volumeProperty.get();
	}
	
	public void setVolume(double volume){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				volumeProperty.set(volume);
			};
		});
	}
	
	/**
	 * Function returns the value of attribute pulsesProperty
	 * @return the pulsesProperty
	 */
	public IntegerProperty getPulsesProperty() {
		return pulsesProperty;
	}
	/**
	 * Function sets the value for attribute pulsesProperty
	 * @param pulsesProperty the pulsesProperty to set
	 */
	public void setPulsesProperty(IntegerProperty pulsesProperty) {

		this.pulsesProperty = pulsesProperty;
	}
	
	public int getPulses() {
		return pulsesProperty.get();
	}
	
	public void setPulses(int pulses){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				pulsesProperty.set(pulses);
			};
		});
	}
	
	/**
	 * Function returns the value of attribute timeReadProperty
	 * @return the timeReadProperty
	 */
	public IntegerProperty getTimeReadProperty() {
		return timeReadProperty;
	}
	
	/**
	 * Function sets the value for attribute timeReadProperty
	 * @param timeReadProperty the timeReadProperty to set
	 */
	public void setTimeReadProperty(IntegerProperty timeReadProperty) {
		this.timeReadProperty = timeReadProperty;
	}
	
	public int getTimeRead() {
		return timeReadProperty.get();
	}
	
	public void setTimeRead(int timeRead){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				timeReadProperty.set(timeRead);
			};
		});
	}
	
	
	/**
	 * Function returns the value of attribute stabilityProperty
	 * @return the stabilityProperty
	 */
	public StringProperty getStabilityProperty() {
		return stabilityProperty;
	}

	/**
	 * Function sets the value for attribute stabilityProperty
	 * @param stabilityProperty the stabilityProperty to set
	 */
	public void setStabilityProperty(StringProperty stabilityProperty) {
		this.stabilityProperty = stabilityProperty;
	}

	public String getStability(){
		return stabilityProperty.get();
	}
	
	public void setStability(String stability){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				stabilityProperty.set(stability);
			};
		});
	}

	/**
	 * Function returns the value of attribute viewGraph
	 * @return the viewGraph
	 */
	public GraphControl getViewGraph() {
		return viewGraph;
	}

	/**
	 * Function sets the value for attribute viewGraph
	 * @param viewGraph the viewGraph to set
	 */
	public void setViewGraph(GraphControl viewGraph) {
		this.viewGraph = viewGraph;
	}

	/**
	 * Function returns the value of attribute stdGraph
	 * @return the stdGraph
	 */
	public GraphControl getStdGraph() {
		return stdGraph;
	}

	/**
	 * Function sets the value for attribute stdGraph
	 * @param stdGraph the stdGraph to set
	 */
	public void setStdGraph(GraphControl stdGraph) {
		this.stdGraph = stdGraph;
	}
	
	/**
	 * Function returns the value of attribute stdDevRefFlowRateProperty
	 * @return the stdDevRefFlowRateProperty
	 */
	public DoubleProperty getStdDevRefFlowRateProperty() {
		return stdDevRefFlowRateProperty;
	}

	/**
	 * Function sets the value for attribute stdDevRefFlowRateProperty
	 * @param stdDevRefFlowRateProperty the stdDevRefFlowRateProperty to set
	 */
	public void setStdDevRefFlowRateProperty(
			DoubleProperty stdDevRefFlowRateProperty) {
		this.stdDevRefFlowRateProperty = stdDevRefFlowRateProperty;
	}


	public double getFlowRateStdDeviation(){
		return stdDevRefFlowRateProperty.get();
	}
	
	public void setFlowRateStdDeviation(double value){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				stdDevRefFlowRateProperty.set(value);
			};
		});
	}
	
	
}

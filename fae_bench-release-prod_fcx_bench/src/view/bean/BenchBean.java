//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BenchBean.java
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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import si.dbcomm.enumerations.CalibrationTypeEnum;
import si.dbcomm.model.BateladaModel;
import view.util.LedViewUtil;

/**
 * @author marcos
 *
 */
public class BenchBean {

	private static BenchBean INSTANCE;

	private DoubleProperty runningFlowRateProperty = new SimpleDoubleProperty();

	private DoubleProperty runningLowerFlowRateProperty = new SimpleDoubleProperty();

	private DoubleProperty runningUpperFlowRateProperty = new SimpleDoubleProperty();

	private IntegerProperty bateladaCountProperty = new SimpleIntegerProperty();

	private LedViewUtil isSampling;

	private LedViewUtil isAdjustingFlow;

	private LedViewUtil isLeaking;

	private LedViewUtil bciConnectLed;

	private LedViewUtil emergencyLed;

	private LedViewUtil pressLed;

	private LedViewUtil inferiorLevel;

	private LedViewUtil bp1Led;

	private LedViewUtil bp2Led;

	private LedViewUtil brepLed;
	
	private LedViewUtil fullProdLed;
	
	private LedViewUtil pointEstimatedLed;

	private StringProperty runningStateProperty = new SimpleStringProperty();
	
	private Property<String> calibrationTypeProperty = new SimpleStringProperty();

	// Scale
	private DoubleProperty weightWTProperty = new SimpleDoubleProperty();

	// Pressure sensors
	private DoubleProperty pressPTLIProperty = new SimpleDoubleProperty();
	private DoubleProperty pressPTLOProperty = new SimpleDoubleProperty();
	private DoubleProperty pressPTBARProperty = new SimpleDoubleProperty();
	private DoubleProperty pressPTDIFProperty = new SimpleDoubleProperty();
	// END Pressure sensors

	// Temperature sensors
	private DoubleProperty tempTTLIProperty = new SimpleDoubleProperty();
	private DoubleProperty tempTTLOProperty = new SimpleDoubleProperty();
	private DoubleProperty tempTTRIProperty = new SimpleDoubleProperty();
	private DoubleProperty tempTTRSProperty = new SimpleDoubleProperty();
	private DoubleProperty tempTTWTProperty = new SimpleDoubleProperty();
	// END Temperature sensors

	// Humidity sensor
	private DoubleProperty humTRHProperty = new SimpleDoubleProperty();

	// Valves
	private BooleanProperty valveVX1Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX2Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX3Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX4Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX5Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX6Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX7Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX8Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX9Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX10Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX11Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX12Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX13Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX14Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX15Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX16Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX17Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX18Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX19Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX20Property = new SimpleBooleanProperty();
	private BooleanProperty valveVX21Property = new SimpleBooleanProperty();
	// END Valves

	// Level Sensor
	private BooleanProperty levelSN1Property = new SimpleBooleanProperty();
	private BooleanProperty levelSN2Property = new SimpleBooleanProperty();
	private BooleanProperty levelSN3Property = new SimpleBooleanProperty();
	private BooleanProperty levelSN4Property = new SimpleBooleanProperty();
	// END Level Sensor

	// Line
	private BooleanProperty lineLINHAFProperty = new SimpleBooleanProperty();

	// Reference Meters
	private RefMeterBean refMeterDN02 = new RefMeterBean();
	private RefMeterBean refMeterDN08 = new RefMeterBean();
	private RefMeterBean refMeterDN32 = new RefMeterBean();
	// END Reference Meters

	// Pumps
	private PumpBean pumpBP1 = new PumpBean();
	private PumpBean pumpBP2 = new PumpBean();
	private PumpBean pumpBREP = new PumpBean();
	// END Pumps

	private BooleanProperty repeatFlowUncertaintyProperty = new SimpleBooleanProperty();

	private BooleanProperty onlyVerificationProperty = new SimpleBooleanProperty();

	// // State process
	// private IntegerProperty stateProcess = new SimpleIntegerProperty();
	
	//
	private BateladaModel batelada = new BateladaModel();

	/**
	 * 
	 */
	public static void benchBeanStart() {
		if (INSTANCE == null) {
			INSTANCE = new BenchBean();
		}
	}

	public static BenchBean getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BenchBean();
		}
		return INSTANCE;
	}

	/**
	 * Function returns the value of attribute runningStateProperty
	 * 
	 * @return the runningStateProperty
	 */
	public StringProperty getRunningStateProperty() {
		return runningStateProperty;
	}

	/**
	 * Function sets the value for attribute runningStateProperty
	 * 
	 * @param runningStateProperty
	 *            the runningStateProperty to set
	 */
	public void setRunningStateProperty(StringProperty runningStateProperty) {
		this.runningStateProperty = runningStateProperty;
	}

	public void setRunningState(String value) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				runningStateProperty.set(value);
			};
		});
	}

	/**
	 * Function returns the value of attribute runningFlowRateProperty
	 * 
	 * @return the runningFlowRateProperty
	 */
	public DoubleProperty getRunningFlowRateProperty() {
		return runningFlowRateProperty;
	}

	/**
	 * Function sets the value for attribute runningFlowRateProperty
	 * 
	 * @param runningFlowRateProperty
	 *            the runningFlowRateProperty to set
	 */
	public void setRunningFlowRateProperty(DoubleProperty runningFlowRateProperty) {
		this.runningFlowRateProperty = runningFlowRateProperty;
	}

	public void setRunningFlowRate(double value) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				runningFlowRateProperty.set(value);
			};
		});
	}

	/**
	 * Function returns the value of attribute runningLowerFlowRateProperty
	 * 
	 * @return the runningLowerFlowRateProperty
	 */
	public DoubleProperty getRunningLowerFlowRateProperty() {
		return runningLowerFlowRateProperty;
	}

	/**
	 * Function sets the value for attribute runningLowerFlowRateProperty
	 * 
	 * @param runningLowerFlowRateProperty
	 *            the runningLowerFlowRateProperty to set
	 */
	public void setRunningLowerFlowRateProperty(DoubleProperty runningLowerFlowRateProperty) {
		this.runningLowerFlowRateProperty = runningLowerFlowRateProperty;
	}

	public void setRunningLowerFlowRate(double value) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				runningLowerFlowRateProperty.set(value);
			};
		});
	}

	/**
	 * Function returns the value of attribute runningUpperFlowRateProperty
	 * 
	 * @return the runningUpperFlowRateProperty
	 */
	public DoubleProperty getRunningUpperFlowRateProperty() {
		return runningUpperFlowRateProperty;
	}

	/**
	 * Function sets the value for attribute runningUpperFlowRateProperty
	 * 
	 * @param runningUpperFlowRateProperty
	 *            the runningUpperFlowRateProperty to set
	 */
	public void setRunningUpperFlowRateProperty(DoubleProperty runningUpperFlowRateProperty) {
		this.runningUpperFlowRateProperty = runningUpperFlowRateProperty;
	}

	public void setRunningUpperFlowRate(double value) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				runningUpperFlowRateProperty.set(value);
			};
		});
	}

	public String getRunningState() {
		return runningStateProperty.get();
	}

	/**
	 * Function returns the value of attribute weightWTProperty
	 * 
	 * @return the weightWTProperty
	 */
	public DoubleProperty getWeightWTProperty() {
		return weightWTProperty;
	}

	/**
	 * Function returns the value of attribute iNSTANCE
	 * 
	 * @return the iNSTANCE
	 */
	public BenchBean getINSTANCE() {
		return INSTANCE;
	}

	/**
	 * Function sets the value for attribute iNSTANCE
	 * 
	 * @param iNSTANCE
	 *            the iNSTANCE to set
	 */
	public void setINSTANCE(BenchBean iNSTANCE) {
		INSTANCE = iNSTANCE;
	}

	/**
	 * Function sets the value for attribute weightWTProperty
	 * 
	 * @param weightWTProperty
	 *            the weightWTProperty to set
	 */
	public void setWeightWTProperty(DoubleProperty weightWTProperty) {
		this.weightWTProperty = weightWTProperty;
	}

	public void setWeightWT(double weight) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				weightWTProperty.set(weight);
			};
		});
	}

	public double getWeightWT() {
		return weightWTProperty.get();
	}

	/**
	 * Function returns the value of attribute pressPTLIProperty
	 * 
	 * @return the pressPTLIProperty
	 */
	public DoubleProperty getPressPTLIProperty() {
		return pressPTLIProperty;
	}

	/**
	 * Function sets the value for attribute pressPTLIProperty
	 * 
	 * @param pressPTLIProperty
	 *            the pressPTLIProperty to set
	 */
	public void setPressPTLIProperty(DoubleProperty pressPTLIProperty) {
		this.pressPTLIProperty = pressPTLIProperty;
	}

	public double getPessPTLI() {

		return pressPTLIProperty.get();
	}

	public void setPressPTLI(double pressPTLI) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				pressPTLIProperty.set(pressPTLI);
			};
		});
	}

	/**
	 * Function returns the value of attribute pressPTLOProperty
	 * 
	 * @return the pressPTLOProperty
	 */
	public DoubleProperty getPressPTLOProperty() {
		return pressPTLOProperty;
	}

	/**
	 * Function sets the value for attribute pressPTLOProperty
	 * 
	 * @param pressPTLOProperty
	 *            the pressPTLOProperty to set
	 */
	public void setPressPTLOProperty(DoubleProperty pressPTLOProperty) {
		this.pressPTLOProperty = pressPTLOProperty;
	}

	public double getPressPTLO() {
		return pressPTLOProperty.get();
	}

	public void setPressPTLO(double pressPTLO) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				pressPTLOProperty.set(pressPTLO);
			};
		});
	}

	/**
	 * Function returns the value of attribute pressPTBARProperty
	 * 
	 * @return the pressPTBARProperty
	 */
	public DoubleProperty getPressPTBARProperty() {
		return pressPTBARProperty;
	}

	/**
	 * Function sets the value for attribute pressPTBARProperty
	 * 
	 * @param pressPTBARProperty
	 *            the pressPTBARProperty to set
	 */
	public void setPressPTBARProperty(DoubleProperty pressPTBARProperty) {
		this.pressPTBARProperty = pressPTBARProperty;
	}

	public double getPressPTBAR() {
		return pressPTBARProperty.get();
	}

	public void setPressPTBAR(double pressBAR) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				pressPTBARProperty.set(pressBAR);
			};
		});
	}

	/**
	 * Function returns the value of attribute pressPTDIFProperty
	 * 
	 * @return the pressPTDIFProperty
	 */
	public DoubleProperty getPressPTDIFProperty() {
		return pressPTDIFProperty;
	}

	/**
	 * Function sets the value for attribute pressPTDIFProperty
	 * 
	 * @param pressPTDIFProperty
	 *            the pressPTDIFProperty to set
	 */
	public void setPressPTDIFProperty(DoubleProperty pressPTDIFProperty) {
		this.pressPTDIFProperty = pressPTDIFProperty;
	}

	public double getPressPTDIF() {
		return pressPTDIFProperty.get();
	}

	public void setPressDiff(double pressDiff) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				pressPTDIFProperty.set(pressDiff);

			}
		});
	}

	/**
	 * Function returns the value of attribute tempTTLIProperty
	 * 
	 * @return the tempTTLIProperty
	 */
	public DoubleProperty getTempTTLIProperty() {
		return tempTTLIProperty;
	}

	/**
	 * Function sets the value for attribute tempTTLIProperty
	 * 
	 * @param tempTTLIProperty
	 *            the tempTTLIProperty to set
	 */
	public void setTempTTLIProperty(DoubleProperty tempTTLIProperty) {
		this.tempTTLIProperty = tempTTLIProperty;
	}

	public double getTempTTLI() {
		return tempTTLIProperty.get();
	}

	public void setTempTTLI(double tempTTLI) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tempTTLIProperty.set(tempTTLI);
			};
		});
	}

	/**
	 * Function returns the value of attribute tempTTLOProperty
	 * 
	 * @return the tempTTLOProperty
	 */
	public DoubleProperty getTempTTLOProperty() {
		return tempTTLOProperty;
	}

	/**
	 * Function sets the value for attribute tempTTLOProperty
	 * 
	 * @param tempTTLOProperty
	 *            the tempTTLOProperty to set
	 */
	public void setTempTTLOProperty(DoubleProperty tempTTLOProperty) {
		this.tempTTLOProperty = tempTTLOProperty;
	}

	public double getTempTTLO() {
		return tempTTLOProperty.get();
	}

	public void setTempTTLO(double tempTTLO) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tempTTLOProperty.set(tempTTLO);
			};
		});
	}

	/**
	 * Function returns the value of attribute tempTTRIProperty
	 * 
	 * @return the tempTTRIProperty
	 */
	public DoubleProperty getTempTTRIProperty() {
		return tempTTRIProperty;
	}

	/**
	 * Function sets the value for attribute tempTTRIProperty
	 * 
	 * @param tempTTRIProperty
	 *            the tempTTRIProperty to set
	 */
	public void setTempTTRIProperty(DoubleProperty tempTTRIProperty) {
		this.tempTTRIProperty = tempTTRIProperty;
	}

	public double getTempTTRI() {
		return tempTTLIProperty.get();
	}

	public void setTempTTRI(double tempTTRI) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tempTTRIProperty.set(tempTTRI);
			};
		});
	}

	/**
	 * Function returns the value of attribute tempTTRSProperty
	 * 
	 * @return the tempTTRSProperty
	 */
	public DoubleProperty getTempTTRSProperty() {
		return tempTTRSProperty;
	}

	/**
	 * Function sets the value for attribute tempTTRSProperty
	 * 
	 * @param tempTTRSProperty
	 *            the tempTTRSProperty to set
	 */
	public void setTempTTRSProperty(DoubleProperty tempTTRSProperty) {
		this.tempTTRSProperty = tempTTRSProperty;
	}

	public double getTempTTRS() {
		return tempTTRSProperty.get();
	}

	public void setTempTTRS(double tempTTRS) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tempTTRSProperty.set(tempTTRS);
			};
		});
	}

	/**
	 * Function returns the value of attribute tempTTWTProperty
	 * 
	 * @return the tempTTWTProperty
	 */
	public DoubleProperty getTempTTWTProperty() {
		return tempTTWTProperty;
	}

	/**
	 * Function sets the value for attribute tempTTWTProperty
	 * 
	 * @param tempTTWTProperty
	 *            the tempTTWTProperty to set
	 */
	public void setTempTTWTProperty(DoubleProperty tempTTWTProperty) {
		this.tempTTWTProperty = tempTTWTProperty;
	}

	public double getTempTTWT() {
		return tempTTWTProperty.get();
	}

	public void setTempTTWT(double tempTTWT) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tempTTWTProperty.set(tempTTWT);
			};
		});
	}

	/**
	 * Function returns the value of attribute humTRHProperty
	 * 
	 * @return the humTRHProperty
	 */
	public DoubleProperty getHumTRHProperty() {
		return humTRHProperty;
	}

	/**
	 * Function sets the value for attribute humTRHProperty
	 * 
	 * @param humTRHProperty
	 *            the humTRHProperty to set
	 */
	public void setHumTRHProperty(DoubleProperty humTRHProperty) {
		this.humTRHProperty = humTRHProperty;
	}

	public double getHumTRH() {
		return humTRHProperty.get();
	}

	public void setHumTRH(double humTRH) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				humTRHProperty.set(humTRH);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX1Property
	 * 
	 * @return the valveVX1Property
	 */
	public BooleanProperty getValveVX1Property() {
		return valveVX1Property;
	}

	/**
	 * Function sets the value for attribute valveVX1Property
	 * 
	 * @param valveVX1Property
	 *            the valveVX1Property to set
	 */
	public void setValveVX1Property(BooleanProperty valveVX1Property) {
		this.valveVX1Property = valveVX1Property;
	}

	public boolean getValveVX1() {
		return valveVX1Property.get();
	}

	public void setValveVX1(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX1Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX2Property
	 * 
	 * @return the valveVX2Property
	 */
	public BooleanProperty getValveVX2Property() {
		return valveVX2Property;
	}

	/**
	 * Function sets the value for attribute valveVX2Property
	 * 
	 * @param valveVX2Property
	 *            the valveVX2Property to set
	 */
	public void setValveVX2Property(BooleanProperty valveVX2Property) {
		this.valveVX2Property = valveVX2Property;
	}

	public boolean getValveVX2() {
		return valveVX2Property.get();
	}

	public void setValveVX2(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX2Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX3Property
	 * 
	 * @return the valveVX3Property
	 */
	public BooleanProperty getValveVX3Property() {
		return valveVX3Property;
	}

	/**
	 * Function sets the value for attribute valveVX3Property
	 * 
	 * @param valveVX3Property
	 *            the valveVX3Property to set
	 */
	public void setValveVX3Property(BooleanProperty valveVX3Property) {
		this.valveVX3Property = valveVX3Property;
	}

	public boolean getValveVX3() {
		return valveVX3Property.get();
	}

	public void setValveVX3(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX3Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX4Property
	 * 
	 * @return the valveVX4Property
	 */
	public BooleanProperty getValveVX4Property() {
		return valveVX4Property;
	}

	/**
	 * Function sets the value for attribute valveVX4Property
	 * 
	 * @param valveVX4Property
	 *            the valveVX4Property to set
	 */
	public void setValveVX4Property(BooleanProperty valveVX4Property) {
		this.valveVX4Property = valveVX4Property;
	}

	public boolean getValveVX4() {
		return valveVX4Property.get();
	}

	public void setValveVX4(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX4Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX5Property
	 * 
	 * @return the valveVX5Property
	 */
	public BooleanProperty getValveVX5Property() {
		return valveVX5Property;
	}

	/**
	 * Function sets the value for attribute valveVX5Property
	 * 
	 * @param valveVX5Property
	 *            the valveVX5Property to set
	 */
	public void setValveVX5Property(BooleanProperty valveVX5Property) {
		this.valveVX5Property = valveVX5Property;
	}

	public boolean getValveVX5() {
		return valveVX5Property.get();
	}

	public void setValveVX5(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX5Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX6Property
	 * 
	 * @return the valveVX6Property
	 */
	public BooleanProperty getValveVX6Property() {
		return valveVX6Property;
	}

	/**
	 * Function sets the value for attribute valveVX6Property
	 * 
	 * @param valveVX6Property
	 *            the valveVX6Property to set
	 */
	public void setValveVX6Property(BooleanProperty valveVX6Property) {
		this.valveVX6Property = valveVX6Property;
	}

	public boolean getValveVX6() {
		return valveVX6Property.get();
	}

	public void setValveVX6(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX6Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX7Property
	 * 
	 * @return the valveVX7Property
	 */
	public BooleanProperty getValveVX7Property() {
		return valveVX7Property;
	}

	/**
	 * Function sets the value for attribute valveVX7Property
	 * 
	 * @param valveVX7Property
	 *            the valveVX7Property to set
	 */
	public void setValveVX7Property(BooleanProperty valveVX7Property) {
		this.valveVX7Property = valveVX7Property;
	}

	public boolean getValveVX7() {
		return valveVX7Property.get();
	}

	public void setValveVX7(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX7Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX8Property
	 * 
	 * @return the valveVX8Property
	 */
	public BooleanProperty getValveVX8Property() {
		return valveVX8Property;
	}

	/**
	 * Function sets the value for attribute valveVX8Property
	 * 
	 * @param valveVX8Property
	 *            the valveVX8Property to set
	 */
	public void setValveVX8Property(BooleanProperty valveVX8Property) {
		this.valveVX8Property = valveVX8Property;
	}

	public boolean getValveVX8() {
		return valveVX8Property.get();
	}

	public void setValveVX8(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX8Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX9Property
	 * 
	 * @return the valveVX9Property
	 */
	public BooleanProperty getValveVX9Property() {
		return valveVX9Property;
	}

	/**
	 * Function sets the value for attribute valveVX9Property
	 * 
	 * @param valveVX9Property
	 *            the valveVX9Property to set
	 */
	public void setValveVX9Property(BooleanProperty valveVX9Property) {
		this.valveVX9Property = valveVX9Property;
	}

	public boolean getValveVX9() {
		return valveVX9Property.get();
	}

	public void setValveVX9(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX9Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX10Property
	 * 
	 * @return the valveVX10Property
	 */
	public BooleanProperty getValveVX10Property() {
		return valveVX10Property;
	}

	/**
	 * Function sets the value for attribute valveVX10Property
	 * 
	 * @param valveVX10Property
	 *            the valveVX10Property to set
	 */
	public void setValveVX10Property(BooleanProperty valveVX10Property) {
		this.valveVX10Property = valveVX10Property;
	}

	public boolean getValveVX10() {
		return valveVX10Property.get();
	}

	public void setValveVX10(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX10Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX11Property
	 * 
	 * @return the valveVX11Property
	 */
	public BooleanProperty getValveVX11Property() {
		return valveVX11Property;
	}

	/**
	 * Function sets the value for attribute valveVX11Property
	 * 
	 * @param valveVX11Property
	 *            the valveVX11Property to set
	 */
	public void setValveVX11Property(BooleanProperty valveVX11Property) {
		this.valveVX11Property = valveVX11Property;
	}

	public boolean getValveVX11() {
		return valveVX11Property.get();
	}

	public void setValveVX11(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX11Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX12Property
	 * 
	 * @return the valveVX12Property
	 */
	public BooleanProperty getValveVX12Property() {
		return valveVX12Property;
	}

	/**
	 * Function sets the value for attribute valveVX12Property
	 * 
	 * @param valveVX12Property
	 *            the valveVX12Property to set
	 */
	public void setValveVX12Property(BooleanProperty valveVX12Property) {
		this.valveVX12Property = valveVX12Property;
	}

	public boolean getValveVX12() {
		return valveVX12Property.get();
	}

	public void setValveVX12(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX12Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX13Property
	 * 
	 * @return the valveVX13Property
	 */
	public BooleanProperty getValveVX13Property() {
		return valveVX13Property;
	}

	/**
	 * Function sets the value for attribute valveVX13Property
	 * 
	 * @param valveVX13Property
	 *            the valveVX13Property to set
	 */
	public void setValveVX13Property(BooleanProperty valveVX13Property) {
		this.valveVX13Property = valveVX13Property;
	}

	public boolean getValveVX13() {
		return valveVX13Property.get();
	}

	public void setValveVX13(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX13Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX14Property
	 * 
	 * @return the valveVX14Property
	 */
	public BooleanProperty getValveVX14Property() {
		return valveVX14Property;
	}

	/**
	 * Function sets the value for attribute valveVX14Property
	 * 
	 * @param valveVX14Property
	 *            the valveVX14Property to set
	 */
	public void setValveVX14Property(BooleanProperty valveVX14Property) {
		this.valveVX14Property = valveVX14Property;
	}

	public boolean getValveVX14() {
		return valveVX14Property.get();
	}

	public void setValveVX14(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX14Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX15Property
	 * 
	 * @return the valveVX15Property
	 */
	public BooleanProperty getValveVX15Property() {
		return valveVX15Property;
	}

	/**
	 * Function sets the value for attribute valveVX15Property
	 * 
	 * @param valveVX15Property
	 *            the valveVX15Property to set
	 */
	public void setValveVX15Property(BooleanProperty valveVX15Property) {
		this.valveVX15Property = valveVX15Property;
	}

	public boolean getValveVX15() {
		return valveVX15Property.get();
	}

	public void setValveVX15(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX15Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX16Property
	 * 
	 * @return the valveVX16Property
	 */
	public BooleanProperty getValveVX16Property() {
		return valveVX16Property;
	}

	/**
	 * Function sets the value for attribute valveVX16Property
	 * 
	 * @param valveVX16Property
	 *            the valveVX16Property to set
	 */
	public void setValveVX16Property(BooleanProperty valveVX16Property) {
		this.valveVX16Property = valveVX16Property;
	}

	public boolean getValveVX16() {
		return valveVX16Property.get();
	}

	public void setValveVX16(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX16Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX17Property
	 * 
	 * @return the valveVX17Property
	 */
	public BooleanProperty getValveVX17Property() {
		return valveVX17Property;
	}

	/**
	 * Function sets the value for attribute valveVX17Property
	 * 
	 * @param valveVX17Property
	 *            the valveVX17Property to set
	 */
	public void setValveVX17Property(BooleanProperty valveVX17Property) {
		this.valveVX17Property = valveVX17Property;
	}

	public boolean getValveVX17() {
		return valveVX17Property.get();
	}

	public void setValveVX17(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX17Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX18Property
	 * 
	 * @return the valveVX18Property
	 */
	public BooleanProperty getValveVX18Property() {
		return valveVX18Property;
	}

	/**
	 * Function sets the value for attribute valveVX18Property
	 * 
	 * @param valveVX18Property
	 *            the valveVX18Property to set
	 */
	public void setValveVX18Property(BooleanProperty valveVX18Property) {
		this.valveVX18Property = valveVX18Property;
	}

	public boolean getValveVX18() {
		return valveVX18Property.get();
	}

	public void setValveVX18(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX18Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX19Property
	 * 
	 * @return the valveVX19Property
	 */
	public BooleanProperty getValveVX19Property() {
		return valveVX19Property;
	}

	/**
	 * Function sets the value for attribute valveVX19Property
	 * 
	 * @param valveVX19Property
	 *            the valveVX19Property to set
	 */
	public void setValveVX19Property(BooleanProperty valveVX19Property) {
		this.valveVX19Property = valveVX19Property;
	}

	public boolean getValveVX19() {
		return valveVX19Property.get();
	}

	public void setValveVX19(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX19Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX20Property
	 * 
	 * @return the valveVX20Property
	 */
	public BooleanProperty getValveVX20Property() {
		return valveVX20Property;
	}

	/**
	 * Function sets the value for attribute valveVX20Property
	 * 
	 * @param valveVX20Property
	 *            the valveVX20Property to set
	 */
	public void setValveVX20Property(BooleanProperty valveVX20Property) {
		this.valveVX20Property = valveVX20Property;
	}

	public boolean getValveVX20() {
		return valveVX20Property.get();
	}

	public void setValveVX20(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX20Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute valveVX21Property
	 * 
	 * @return the valveVX21Property
	 */
	public BooleanProperty getValveVX21Property() {
		return valveVX21Property;
	}

	/**
	 * Function sets the value for attribute valveVX21Property
	 * 
	 * @param valveVX21Property
	 *            the valveVX21Property to set
	 */
	public void setValveVX21Property(BooleanProperty valveVX21Property) {
		this.valveVX21Property = valveVX21Property;
	}

	public boolean getValveVX21() {
		return valveVX21Property.get();
	}

	public void setValveVX21(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				valveVX21Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute levelSN1Property
	 * 
	 * @return the levelSN1Property
	 */
	public BooleanProperty getLevelSN1Property() {
		return levelSN1Property;
	}

	/**
	 * Function sets the value for attribute levelSN1Property
	 * 
	 * @param levelSN1Property
	 *            the levelSN1Property to set
	 */
	public void setLevelSN1Property(BooleanProperty levelSN1Property) {
		this.levelSN1Property = levelSN1Property;
	}

	public boolean getLevelSN1() {
		return levelSN1Property.get();
	}

	public void setLevelSN1(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				levelSN1Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute levelSN2Property
	 * 
	 * @return the levelSN2Property
	 */
	public BooleanProperty getLevelSN2Property() {
		return levelSN2Property;
	}

	/**
	 * Function sets the value for attribute levelSN2Property
	 * 
	 * @param levelSN2Property
	 *            the levelSN2Property to set
	 */
	public void setLevelSN2Property(BooleanProperty levelSN2Property) {
		this.levelSN2Property = levelSN2Property;
	}

	public boolean getLevelSN2() {
		return levelSN2Property.get();
	}

	public void setLevelSN2(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				levelSN2Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute levelSN3Property
	 * 
	 * @return the levelSN3Property
	 */
	public BooleanProperty getLevelSN3Property() {
		return levelSN3Property;
	}

	/**
	 * Function sets the value for attribute levelSN3Property
	 * 
	 * @param levelSN3Property
	 *            the levelSN3Property to set
	 */
	public void setLevelSN3Property(BooleanProperty levelSN3Property) {
		this.levelSN3Property = levelSN3Property;
	}

	public boolean getLevelSN3() {
		return levelSN3Property.get();
	}

	public void setLevelSN3(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				levelSN3Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute levelSN4Property
	 * 
	 * @return the levelSN4Property
	 */
	public BooleanProperty getLevelSN4Property() {
		return levelSN4Property;
	}

	/**
	 * Function sets the value for attribute levelSN4Property
	 * 
	 * @param levelSN4Property
	 *            the levelSN4Property to set
	 */
	public void setLevelSN4Property(BooleanProperty levelSN4Property) {
		this.levelSN4Property = levelSN4Property;
	}

	public boolean getLevelSN4() {
		return levelSN4Property.get();
	}

	public void setLevelSN4(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				levelSN4Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute lineLINHAFProperty
	 * 
	 * @return the lineLINHAFProperty
	 */
	public BooleanProperty getLineLINHAFProperty() {
		return lineLINHAFProperty;
	}

	/**
	 * Function sets the value for attribute lineLINHAFProperty
	 * 
	 * @param lineLINHAFProperty
	 *            the lineLINHAFProperty to set
	 */
	public void setLineLINHAFProperty(BooleanProperty lineLINHAFProperty) {
		this.lineLINHAFProperty = lineLINHAFProperty;
	}

	public boolean getLineLINHAF() {
		return levelSN4Property.get();
	}

	public void setLineLINHAF(boolean val) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				levelSN4Property.set(val);
			};
		});
	}

	/**
	 * Function returns the value of attribute refMeterDN02
	 * 
	 * @return the refMeterDN02
	 */
	public RefMeterBean getRefMeterDN02() {
		return refMeterDN02;
	}

	/**
	 * Function sets the value for attribute refMeterDN02
	 * 
	 * @param refMeterDN02
	 *            the refMeterDN02 to set
	 */
	public void setRefMeterDN02(RefMeterBean refMeterDN02) {
		this.refMeterDN02 = refMeterDN02;
	}

	/**
	 * Function returns the value of attribute refMeterDN08
	 * 
	 * @return the refMeterDN08
	 */
	public RefMeterBean getRefMeterDN08() {
		return refMeterDN08;
	}

	/**
	 * Function sets the value for attribute refMeterDN08
	 * 
	 * @param refMeterDN08
	 *            the refMeterDN08 to set
	 */
	public void setRefMeterDN08(RefMeterBean refMeterDN08) {
		this.refMeterDN08 = refMeterDN08;
	}

	/**
	 * Function returns the value of attribute refMeterDN32
	 * 
	 * @return the refMeterDN32
	 */
	public RefMeterBean getRefMeterDN32() {
		return refMeterDN32;
	}

	/**
	 * Function sets the value for attribute refMeterDN32
	 * 
	 * @param refMeterDN32
	 *            the refMeterDN32 to set
	 */
	public void setRefMeterDN32(RefMeterBean refMeterDN32) {
		this.refMeterDN32 = refMeterDN32;
	}

	/**
	 * Function returns the value of attribute pumpBP1
	 * 
	 * @return the pumpBP1
	 */
	public PumpBean getPumpBP1() {
		return pumpBP1;
	}

	/**
	 * Function sets the value for attribute pumpBP1
	 * 
	 * @param pumpBP1
	 *            the pumpBP1 to set
	 */
	public void setPumpBP1(PumpBean pumpBP1) {
		this.pumpBP1 = pumpBP1;
	}

	/**
	 * Function returns the value of attribute pumpBP2
	 * 
	 * @return the pumpBP2
	 */
	public PumpBean getPumpBP2() {
		return pumpBP2;
	}

	/**
	 * Function sets the value for attribute pumpBP2
	 * 
	 * @param pumpBP2
	 *            the pumpBP2 to set
	 */
	public void setPumpBP2(PumpBean pumpBP2) {
		this.pumpBP2 = pumpBP2;
	}

	/**
	 * Function returns the value of attribute pumpBREP
	 * 
	 * @return the pumpBREP
	 */
	public PumpBean getPumpBREP() {
		return pumpBREP;
	}

	/**
	 * Function sets the value for attribute pumpBREP
	 * 
	 * @param pumpBREP
	 *            the pumpBREP to set
	 */
	public void setPumpBREP(PumpBean pumpBREP) {
		this.pumpBREP = pumpBREP;
	}

	/**
	 * Function returns the value of attribute bciConnectLed
	 * 
	 * @return the bciConnectLed
	 */
	public LedViewUtil getBciConnectLed() {
		return bciConnectLed;
	}

	/**
	 * Function sets the value for attribute bciConnectLed
	 * 
	 * @param bciConnectLed
	 *            the bciConnectLed to set
	 */
	public void setBciConnectLed(LedViewUtil bciConnectLed) {
		this.bciConnectLed = bciConnectLed;
	}

	/**
	 * Function returns the value of attribute bateladaCountProperty
	 * 
	 * @return the bateladaCountProperty
	 */
	public IntegerProperty getBateladaCountProperty() {
		return bateladaCountProperty;
	}

	/**
	 * Function sets the value for attribute bateladaCountProperty
	 * 
	 * @param bateladaCountProperty
	 *            the bateladaCountProperty to set
	 */
	public void setBateladaCountProperty(IntegerProperty bateladaCountProperty) {
		this.bateladaCountProperty = bateladaCountProperty;
	}

	public int getBateladaCount() {
		return bateladaCountProperty.get();
	}

	public void setBateladaCount(int value) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				bateladaCountProperty.set(value);
			};
		});
	}

	/**
	 * Function returns the value of attribute isSampling
	 * 
	 * @return the isSampling
	 */
	public LedViewUtil getIsSampling() {
		return isSampling;
	}

	/**
	 * Function sets the value for attribute isSampling
	 * 
	 * @param isSampling
	 *            the isSampling to set
	 */
	public void setIsSampling(LedViewUtil isSampling) {
		this.isSampling = isSampling;
	}

	/**
	 * Function returns the value of attribute isAdjustingFlow
	 * 
	 * @return the isAdjustingFlow
	 */
	public LedViewUtil getIsAdjustingFlow() {
		return isAdjustingFlow;
	}

	/**
	 * Function sets the value for attribute isAdjustingFlow
	 * 
	 * @param isAdjustingFlow
	 *            the isAdjustingFlow to set
	 */
	public void setIsAdjustingFlow(LedViewUtil isAdjustingFlow) {
		this.isAdjustingFlow = isAdjustingFlow;
	}

	/**
	 * Function returns the value of attribute isLeaking
	 * 
	 * @return the isLeaking
	 */
	public LedViewUtil getIsLeaking() {
		return isLeaking;
	}

	/**
	 * Function sets the value for attribute isLeaking
	 * 
	 * @param isLeaking
	 *            the isLeaking to set
	 */
	public void setIsLeaking(LedViewUtil isLeaking) {
		this.isLeaking = isLeaking;
	}

	/**
	 * Function returns the value of attribute emergencyLed
	 * 
	 * @return the emergencyLed
	 */
	public LedViewUtil getEmergencyLed() {
		return emergencyLed;
	}

	/**
	 * Function sets the value for attribute emergencyLed
	 * 
	 * @param emergencyLed
	 *            the emergencyLed to set
	 */
	public void setEmergencyLed(LedViewUtil emergencyLed) {
		this.emergencyLed = emergencyLed;
	}

	/**
	 * Function returns the value of attribute pressLed
	 * 
	 * @return the pressLed
	 */
	public LedViewUtil getPressLed() {
		return pressLed;
	}

	/**
	 * Function sets the value for attribute pressLed
	 * 
	 * @param pressLed
	 *            the pressLed to set
	 */
	public void setPressLed(LedViewUtil pressLed) {
		this.pressLed = pressLed;
	}

	/**
	 * Function returns the value of attribute inferiorLevel
	 * 
	 * @return the inferiorLevel
	 */
	public LedViewUtil getInferiorLevel() {
		return inferiorLevel;
	}

	/**
	 * Function sets the value for attribute inferiorLevel
	 * 
	 * @param inferiorLevel
	 *            the inferiorLevel to set
	 */
	public void setInferiorLevel(LedViewUtil inferiorLevel) {
		this.inferiorLevel = inferiorLevel;
	}

	/**
	 * Function returns the value of attribute bp1Led
	 * 
	 * @return the bp1Led
	 */
	public LedViewUtil getBp1Led() {
		return bp1Led;
	}

	/**
	 * Function sets the value for attribute bp1Led
	 * 
	 * @param bp1Led
	 *            the bp1Led to set
	 */
	public void setBp1Led(LedViewUtil bp1Led) {
		this.bp1Led = bp1Led;
	}

	/**
	 * Function returns the value of attribute bp2Led
	 * 
	 * @return the bp2Led
	 */
	public LedViewUtil getBp2Led() {
		return bp2Led;
	}

	/**
	 * Function sets the value for attribute bp2Led
	 * 
	 * @param bp2Led
	 *            the bp2Led to set
	 */
	public void setBp2Led(LedViewUtil bp2Led) {
		this.bp2Led = bp2Led;
	}

	/**
	 * Function returns the value of attribute brepLed
	 * 
	 * @return the brepLed
	 */
	public LedViewUtil getBrepLed() {
		return brepLed;
	}

	/**
	 * Function sets the value for attribute brepLed
	 * 
	 * @param brepLed
	 *            the brepLed to set
	 */
	public void setBrepLed(LedViewUtil brepLed) {
		this.brepLed = brepLed;
	}

	/**
	 * Function returns the value of attribute repeatFlowUncertainty
	 * 
	 * @return the repeatFlowUncertainty
	 */
	public BooleanProperty getRepeatFlowUncertaintyProperty() {
		return repeatFlowUncertaintyProperty;
	}

	/**
	 * Function sets the value for attribute repeatFlowUncertainty
	 * 
	 * @param repeatFlowUncertainty
	 *            the repeatFlowUncertainty to set
	 */
	public void setRepeatFlowUncertaintyProperty(BooleanProperty repeatFlowUncertainty) {
		this.repeatFlowUncertaintyProperty = repeatFlowUncertainty;
	}

	public void setRepeatFlowUncertainty(boolean value) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				repeatFlowUncertaintyProperty.set(value);
			};
		});
	}
	//---------------------------
	/**
	 * Function returns the value of attribute repeatFlowUncertainty
	 * 
	 * @return the repeatFlowUncertainty
	 */
	public BooleanProperty getOnlyVerificationProperty() {
		return onlyVerificationProperty;
	}
	
	/**
	 * Function sets the value for attribute repeatFlowUncertainty
	 * 
	 * @param repeatFlowUncertainty
	 *            the repeatFlowUncertainty to set
	 */
	public void setOnlyVerificationProperty(BooleanProperty onlyVerification) {
		this.onlyVerificationProperty = onlyVerification;
	}
	
	public void setOnlyVerification(boolean value) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				onlyVerificationProperty.set(value);
			};
		});
	}

	/**
	 * Function returns the value of attribute batelada
	 * @return the batelada
	 */
	public BateladaModel getBatelada() {
		return batelada;
	}

	/**
	 * Function sets the value for attribute batelada
	 * @param batelada the batelada to set
	 */
	public void setBatelada(BateladaModel batelada) {
		this.batelada = batelada;
	}

	/**
	 * Function returns the value of attribute fullProdLed
	 * @return the fullProdLed
	 */
	public LedViewUtil getFullProdLed() {
		return fullProdLed;
	}

	/**
	 * Function sets the value for attribute fullProdLed
	 * @param fullProdLed the fullProdLed to set
	 */
	public void setFullProdLed(LedViewUtil fullProdLed) {
		this.fullProdLed = fullProdLed;
	}

	/**
	 * Function returns the value of attribute pointEstimatedLed
	 * @return the pointEstimatedLed
	 */
	public LedViewUtil getPointEstimatedLed() {
		return pointEstimatedLed;
	}

	/**
	 * Function sets the value for attribute pointEstimatedLed
	 * @param pointEstimatedLed the pointEstimatedLed to set
	 */
	public void setPointEstimatedLed(LedViewUtil pointEstimatedLed) {
		this.pointEstimatedLed = pointEstimatedLed;
	}

	/**
	 * Function returns the value of attribute calibrationTypeProperty
	 * @return the calibrationTypeProperty
	 */
	public Property<String> getCalibrationTypeProperty() {
		return calibrationTypeProperty;
	}

	/**
	 * Function sets the value for attribute calibrationTypeProperty
	 * @param calibrationTypeProperty the calibrationTypeProperty to set
	 */
	public void setCalibrationTypeProperty(StringProperty calibrationTypeProperty) {
		this.calibrationTypeProperty = calibrationTypeProperty;
	}

	
}

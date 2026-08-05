//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief Interface defines the methods the api must implement to communic
*    @file ApiInterface.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details All methods to command the bench are described here. these methods
*    allow the Service Interface to control and request the Bench to execute
*    actions
* 
*/
//=============================================================================
package bciapi.interfaces;

import java.util.ArrayList;

import bciapi.command.model.ResponseSetAutoFlowRate;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.HumiditySensorModel;
import si.dbcomm.model.LevelSensorModel;
import si.dbcomm.model.PidConfigModel;
import si.dbcomm.model.PressureSensorModel;
import si.dbcomm.model.PumpModel;
import si.dbcomm.model.RefMeterModel;
import si.dbcomm.model.ScaleModel;
import si.dbcomm.model.TempSensorModel;
import si.dbcomm.model.ValveModel;
import si.dbcomm.model.WaterLineModel;

/**
 * @author Marcos Oliveira
 *
 */
public interface ApiInterface {
	
	/**
	 * This is a response command. Acknwoledge any process is done.
	 */
	public void ack();
	
	/**
	 * This method resets the alarms running on PLC
	 */
	public boolean reset();
	
	/**
	 * Function sends a stop command to the bench. This command stops the bench and sets it to rest state
	 */
	public boolean stop();
	
	/**
	 * Method retrieves the alarms from the api. See BCI specification for definition.
	 * @return int indicating the alarm
	 */
	public int getAlarms();
	
	/**
	 * Retrieves the value read by the temperature sensor designated in parameter
	 * @param sensor with the id of the sensor to read
	 * @return An TempSensorModel Object with the temperature read 
	 */
	public TempSensorModel getSensorTemp(final TempSensorModel sensor);
	
	/**
	 * Returns the value of pressure indicated by selected sensor defined on 
	 * parameter.
	 * @param sensor with the id of the sensor to read
	 * @return An PressureSensorModel Object with the value of the pressure read
	 */
	public PressureSensorModel getSensorPressure(final PressureSensorModel sensor);
	
	/**
	 * Returns the state indicated by water level sensor designated by parameter.
	 * @param sensor with the id of the sensor to read
	 * @return An LevelSensorModel Object indicating if the level at sensor 
	 * has been reached
	 */
	public LevelSensorModel getSensorLevel(final LevelSensorModel sensor);
	
	/**
	 * Returns the state indicated by humidity sensor designated by parameter.
	 * @param sensor with the id of the sensor to read
	 * @returnAn HumiditySensorModel Object indicating if the humidity at sensor 
	 * has been reached
	 */
	public HumiditySensorModel getSensorHumidity(final HumiditySensorModel sensor);

	/**
	 * Opens hydraulics on line to install or remove meters. This action must 
	 * be verified to prevent the opening of the line with test running. Also 
	 * the pressure in the line must be relieved..
	 * @param line object with id of the line be opened
	 * @returnAn WaterLineModel Object with the state of the line 
	 */
	public WaterLineModel openLine(final WaterLineModel line);
	
	/**
	 * Closes the hydraulics on line. This action can be instantly cancelled in 
	 * case a something that is not to be in path of closing the bench’s line 
	 * gets stuck or caught.
	 * @param line object with id of the line be closed
	 * @returnAn WaterLineModel Object with the state of the line 
	 */
	public WaterLineModel closeLine(final WaterLineModel line);
	
	/**
	 * Returns the state of  the hydraulics of the benches line. 
	 * @param line object with id of the line to retrieve state
	 * @returnAn WaterLineModel Object with the state of the line 
	 */
	public WaterLineModel getLineState(final WaterLineModel line);

	/**
	 * Sets an specific flow rate to run in the bench. The flowrate
	 * will run until a stop command is sent. 
	 * @param flowRate object with the value from 1.50 to 16000.00
	 * @returnAn FlowRateModel object indicating the flow is stable Object 
	 * with the state of the flow
	 */
	public FlowRateModel setFlowRate(final FlowRateModel flowRate);
	
	/**
	 * Enables the buffer used for the flowrate average calculation. 
	 * This is because when adjusting the flow rate, t
	 * he averages will have a greater error that must not be used for the calibration process.
	 * Also sets the mili seconds of the window that captures volume pulse samples used to calculate the flowrate 
	 * @param enable
	 * @param timeVal
	 */
	public boolean setEnableBuffer(final boolean enable, final long timeVal, final int buffSize);
	
	public boolean startCounter(final RefMeterModel refMeter);
	
	public boolean stopCounter(final RefMeterModel refMeter);
	
	public int[] getCounter(final RefMeterModel refMeter);
	
	
	
	/**
	 * Opens the specified valve
	 * @param valve to open
	 * @return valve with the state of the valve
	 */
	public ValveModel openValve(final ValveModel valve);
	
	/**
	 * Closes the specified valve
	 * @param valve to close
	 * @return valve with the state of the valve
	 */
	public ValveModel closeValve(final ValveModel valve);
	
	/**
	 * Returns the specified valve state
	 * @param valve to retrieve state
	 * @return valve with the state of the valve
	 */
	public ValveModel getValveState(final ValveModel valve);
	
	/**
	 * Sets an specific load to run the selected pump. 
	 * @param pump object with the load percentage value from 0.0 to 100.0
	 * and the id of the pump to control
	 * @returnAn PumpModel object with the state of the pump
	 */
	public PumpModel setPumpLoad(final PumpModel pump);
	
	/**
	 * Returns the load of the selected pump. 
	 * @param pump object with the id of the pump to read load
	 * @returnAn PumpModel object with the load of the pump
	 */
	//public PumpModel getPumpLoad(final PumpModel pump);
	
	
	public boolean startPump(final PumpModel pump);
	
	public boolean stopPump(final PumpModel pump);
	
	/**
	 * Returns the state of the selected pump. 
	 * @param pump object with the id of the pump to read state
	 * @returnAn PumpModel object with the state of the pump
	 */
	public PumpModel getPumpState(final PumpModel pump);
	
//	/**
//	 * Reads the flowrate running at the specified reference meter 
//	 * @param refMeter object with the id of the reference meter to read flow
//	 * rate
//	 * @returnAn RefMeterModel object with the flow rate value
//	 */
//	public RefMeterModel readFlowRateRefMeter(final RefMeterModel refMeter);
//	
//	/**
//	 * Reads the volume indicated at the specified reference meter 
//	 * @param refMeter object with the id of the reference meter to read volume
//	 * @returnAn RefMeterModel object with the volume value
//	 */
//	public RefMeterModel readVolumeRefMeter(final RefMeterModel refMeter);

	/**
	 *	Reads the volume and flow rate at the specified reference meter 
	 * @param refMeter object with the id of the reference meter to read flow
	 * rate
	 * @return
	 */
	public RefMeterModel readRefMeter(RefMeterModel refMeter);
	
	/**
	 * Reads the weight indicated at the specified weight scale 
	 * @param scale object with the id of the reference scale to read the weight
	 * @returnAn ScaleModel object with the weight value
	 */
	public ScaleModel getScaleWeight(final ScaleModel scale);
	
	//TODO add automatic control signatures.
	/**
	 * Configures and runs a specific flowRateSel, for the duration of secSel, times a number of rep with an interval between each execution. The response from the bench is requested at a given frequency.
	 * In case the duration is not specified, the bench only stops (Command stop) in case a stop command is received. 
	 * Depending on command configuration several commands may result in a sequence of flow rates without stop.
	 * The period of the response is determined by parameter period and onChange. When onChange is set to True, the period parameter is overridden and the bench sends the raw data when it changes.
	 */
	public ResponseSetAutoFlowRate setAutoFlowRate(final FlowRateModel flowrate, int secSel, int rep, int interval, int period,	boolean onChange);

	
	public ResponseSetAutoFlowRate setSeqFlowRates(ArrayList<FlowRateModel> flowRates, int secSel, int rep, int interval, int period,	boolean onChange);

	public boolean setPidParameters(PumpModel pumpVal, double kpVal, double tmVal, double tvVal);
	
	public PidConfigModel getPidParameters(PumpModel pumpVal);
	
}

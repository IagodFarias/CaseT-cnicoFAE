//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BciapiService.java
*    @author marcos
*    @date 19 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.service;
import bciapi.impl.BenchControlImpl;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.HumiditySensorModel;
import si.dbcomm.model.LevelSensorModel;
import si.dbcomm.model.PressureSensorModel;
import si.dbcomm.model.PumpModel;
import si.dbcomm.model.RefMeterModel;
import si.dbcomm.model.ScaleModel;
import si.dbcomm.model.TempSensorModel;
import si.dbcomm.model.ValveModel;
import si.dbcomm.model.WaterLineModel;

/**
 * this class persists the data on the database. It uses the bench control implementation to 
 * send commands to the Bench and then, on its turn, it saves the data needed
 * 
 * @author marcos
 *
 */
public class BenchControlService  {
	BenchControlImpl bcimpl = BenchControlImpl.getInstance();
	

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#ack()
	 */
	public void ack() {
		bcimpl.ack();
		
	}

//	/* (non-Javadoc)
//	 * @see bciapi.interfaces.ApiInterface#stop(java.lang.String)
//	 */
//	public boolean stop(String command) {
//		return bcimpl.stop(command);
//	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getSensorTemp(model.TempSensorModel)
	 */
	public TempSensorModel getSensorTemp(TempSensorModel sensor) {
		return getSensorTemp(sensor);
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getSensorPressure(model.PressureSensorModel)
	 */
	public PressureSensorModel getSensorPressure(PressureSensorModel sensor) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getSensorLevel(model.LevelSensorModel)
	 */
	public LevelSensorModel getSensorLevel(LevelSensorModel sensor) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getSensorHumidity(model.HumiditySensorModel)
	 */
	public HumiditySensorModel getSensorHumidity(HumiditySensorModel sensor) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#openLine(model.WaterLineModel)
	 */
	public WaterLineModel openLine(WaterLineModel line) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#closeLine(model.WaterLineModel)
	 */
	public WaterLineModel closeLine(WaterLineModel line) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getLineState(model.WaterLineModel)
	 */
	public WaterLineModel getLineState(WaterLineModel line) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#setFlowRate(model.FlowRateModel)
	 */
	public FlowRateModel setFlowRate(FlowRateModel flowRate) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#openValve(model.ValveModel)
	 */
	public ValveModel openValve(ValveModel valve) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#closeValve(model.ValveModel)
	 */
	public ValveModel closeValve(ValveModel valve) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getValveState(model.ValveModel)
	 */
	public ValveModel getValveState(ValveModel valve) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#setPumpLoad(model.PumpModel)
	 */
	public PumpModel setPumpLoad(PumpModel pump) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getPumpState(model.PumpModel)
	 */
	public PumpModel getPumpState(PumpModel pump) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#readRefMeter(model.RefMeterModel)
	 */
	public RefMeterModel readRefMeter(RefMeterModel refMeter) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#getScaleWeight(model.ScaleModel)
	 */
	public ScaleModel getScaleWeight(ScaleModel scale) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bciapi.interfaces.ApiInterface#setAutoFlowRate(model.FlowRateModel, int, int, int, int, boolean)
	 */
	public FlowRateModel setAutoFlowRate(FlowRateModel flowrate, int secSel,
			int rep, int interval, int period, boolean onChange) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

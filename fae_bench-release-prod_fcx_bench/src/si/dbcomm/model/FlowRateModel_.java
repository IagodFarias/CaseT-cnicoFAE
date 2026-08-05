package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import si.dbcomm.enumerations.FlowRateStateEnum;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FlowRateModel.class)
public abstract class FlowRateModel_ {

	public static volatile SingularAttribute<FlowRateModel, RefMeterModel> refMeter;
	public static volatile SingularAttribute<FlowRateModel, String> description;
	public static volatile SingularAttribute<FlowRateModel, Double> lowerLimit;
	public static volatile SingularAttribute<FlowRateModel, Date> flowRateReadTime;
	public static volatile SingularAttribute<FlowRateModel, Boolean> running;
	public static volatile SingularAttribute<FlowRateModel, PidConfigModel> pidConfig;
	public static volatile SingularAttribute<FlowRateModel, Double> flowRate;
	public static volatile SingularAttribute<FlowRateModel, Double> upperLimit;
	public static volatile SingularAttribute<FlowRateModel, Long> id;
	public static volatile SingularAttribute<FlowRateModel, FlowRateStateEnum> state;
	public static volatile SingularAttribute<FlowRateModel, PumpModel> pump;
	public static volatile SingularAttribute<FlowRateModel, RamalModel> ramal;
	public static volatile SingularAttribute<FlowRateModel, Double> uncertantyLimit;

}


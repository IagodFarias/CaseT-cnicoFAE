package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import si.dbcomm.enumerations.FlowRateStateEnum;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RefMeterModel.class)
public abstract class RefMeterModel_ {

	public static volatile SetAttribute<RefMeterModel, BenchDataModel> data;
	public static volatile SingularAttribute<RefMeterModel, FlowRateStateEnum> flowStability;
	public static volatile SingularAttribute<RefMeterModel, String> description;
	public static volatile SingularAttribute<RefMeterModel, Double> maxFlowRate;
	public static volatile SingularAttribute<RefMeterModel, Double> volume;
	public static volatile SingularAttribute<RefMeterModel, Double> freqMaxFlowRate;
	public static volatile SingularAttribute<RefMeterModel, Double> flowRate;
	public static volatile SingularAttribute<RefMeterModel, Integer> pulses;
	public static volatile SingularAttribute<RefMeterModel, Integer> timeread;
	public static volatile SingularAttribute<RefMeterModel, Double> minFlowRate;
	public static volatile SingularAttribute<RefMeterModel, Long> id;
	public static volatile SingularAttribute<RefMeterModel, String> tag;
	public static volatile SingularAttribute<RefMeterModel, Date> readtime;
	public static volatile SingularAttribute<RefMeterModel, ValveModel> valve;
	public static volatile SetAttribute<RefMeterModel, FlowRateModel> flowRates;

}


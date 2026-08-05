package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MeterTypeModel.class)
public abstract class MeterTypeModel_ {

	public static volatile SingularAttribute<MeterTypeModel, Double> lowCutoff;
	public static volatile SingularAttribute<MeterTypeModel, FlowRateModel> nominalFlowrate;
	public static volatile SingularAttribute<MeterTypeModel, FlowRateModel> minFlowrate;
	public static volatile SingularAttribute<MeterTypeModel, Integer> range;
	public static volatile SingularAttribute<MeterTypeModel, Double> highCutoff;
	public static volatile SingularAttribute<MeterTypeModel, Double> nominalDiameter;
	public static volatile SingularAttribute<MeterTypeModel, Boolean> isRadioWmBus;
	public static volatile SingularAttribute<MeterTypeModel, Double> maxFlowRatePermissibleError;
	public static volatile SingularAttribute<MeterTypeModel, Double> transitionFlowRatePermissibleError;
	public static volatile SingularAttribute<MeterTypeModel, String> manufacturer;
	public static volatile SingularAttribute<MeterTypeModel, Double> reductionDiameter;
	public static volatile SingularAttribute<MeterTypeModel, String> codProduto;
	public static volatile SetAttribute<MeterTypeModel, MeterModel> meters;
	public static volatile SetAttribute<MeterTypeModel, BatchModel> batches;
	public static volatile SingularAttribute<MeterTypeModel, Double> nominalFlowRatePermissibleError;
	public static volatile SingularAttribute<MeterTypeModel, String> meterModelDescription;
	public static volatile SingularAttribute<MeterTypeModel, Double> minFlowRatePermissibleError;
	public static volatile SingularAttribute<MeterTypeModel, FlowRateModel> maxFlowrate;
	public static volatile SingularAttribute<MeterTypeModel, FlowRateModel> transitionFlowrate;
	public static volatile SingularAttribute<MeterTypeModel, Double> carcassLength;
	public static volatile SingularAttribute<MeterTypeModel, Double> ultraSoundPathLengh;
	public static volatile SingularAttribute<MeterTypeModel, Long> id;
	public static volatile SingularAttribute<MeterTypeModel, String> meteringClass;
	public static volatile SingularAttribute<MeterTypeModel, String> inmetroQnId;

}


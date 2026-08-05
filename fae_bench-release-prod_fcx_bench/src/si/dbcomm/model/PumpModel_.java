package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import si.dbcomm.enumerations.PumpStateEnum;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PumpModel.class)
public abstract class PumpModel_ {

	public static volatile SingularAttribute<PumpModel, Double> load;
	public static volatile SingularAttribute<PumpModel, Double> maxFlowrate;
	public static volatile SingularAttribute<PumpModel, Double> minFlowRate;
	public static volatile SingularAttribute<PumpModel, String> description;
	public static volatile SingularAttribute<PumpModel, Long> id;
	public static volatile SingularAttribute<PumpModel, PumpStateEnum> state;
	public static volatile SingularAttribute<PumpModel, String> tag;
	public static volatile SetAttribute<PumpModel, FlowRateModel> flowRates;

}


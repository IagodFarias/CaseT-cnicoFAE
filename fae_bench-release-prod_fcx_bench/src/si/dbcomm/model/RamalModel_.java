package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RamalModel.class)
public abstract class RamalModel_ {

	public static volatile SingularAttribute<RamalModel, Double> minFlowRate;
	public static volatile SingularAttribute<RamalModel, String> description;
	public static volatile SingularAttribute<RamalModel, Double> maxFlowRate;
	public static volatile SingularAttribute<RamalModel, Long> id;
	public static volatile SingularAttribute<RamalModel, String> tag;
	public static volatile SingularAttribute<RamalModel, ValveModel> valve;
	public static volatile SetAttribute<RamalModel, FlowRateModel> flowRates;

}


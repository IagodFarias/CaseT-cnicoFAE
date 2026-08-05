package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FlowRateMeanStdModel.class)
public abstract class FlowRateMeanStdModel_ {

	public static volatile SingularAttribute<FlowRateMeanStdModel, Double> stdConstantK;
	public static volatile SingularAttribute<FlowRateMeanStdModel, BateladaModel> bateladaEnd;
	public static volatile SingularAttribute<FlowRateMeanStdModel, Double> avgConstantK;
	public static volatile SingularAttribute<FlowRateMeanStdModel, BatchModel> batch;
	public static volatile SingularAttribute<FlowRateMeanStdModel, Double> flowRate;
	public static volatile SingularAttribute<FlowRateMeanStdModel, Double> avgReynolds;
	public static volatile SingularAttribute<FlowRateMeanStdModel, Long> id;
	public static volatile SingularAttribute<FlowRateMeanStdModel, BateladaModel> bateladaInit;
	public static volatile SingularAttribute<FlowRateMeanStdModel, Double> avgTemperature;
	public static volatile SingularAttribute<FlowRateMeanStdModel, Boolean> isApproved;

}


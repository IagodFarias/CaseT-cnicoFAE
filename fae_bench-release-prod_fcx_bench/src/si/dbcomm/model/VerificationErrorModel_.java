package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(VerificationErrorModel.class)
public abstract class VerificationErrorModel_ {

	public static volatile SingularAttribute<VerificationErrorModel, Double> meanMeterFlowRate;
	public static volatile SingularAttribute<VerificationErrorModel, MeterModel> meter;
	public static volatile SingularAttribute<VerificationErrorModel, Long> id;
	public static volatile SingularAttribute<VerificationErrorModel, Double> meanRefFlowRate;
	public static volatile SingularAttribute<VerificationErrorModel, Double> error;
	public static volatile SingularAttribute<VerificationErrorModel, Double> expectedFlowRate;

}


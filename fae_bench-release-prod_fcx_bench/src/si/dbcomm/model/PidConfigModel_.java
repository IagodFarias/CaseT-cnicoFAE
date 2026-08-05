package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PidConfigModel.class)
public abstract class PidConfigModel_ {

	public static volatile SingularAttribute<PidConfigModel, Double> integrativeTm;
	public static volatile SingularAttribute<PidConfigModel, Double> derivativeTv;
	public static volatile SingularAttribute<PidConfigModel, FlowRateModel> flowRate;
	public static volatile SingularAttribute<PidConfigModel, Double> errorE;
	public static volatile SingularAttribute<PidConfigModel, Double> proportionalKp;
	public static volatile SingularAttribute<PidConfigModel, Long> id;

}


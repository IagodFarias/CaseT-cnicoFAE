package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PressureSensorModel.class)
public abstract class PressureSensorModel_ {

	public static volatile SingularAttribute<PressureSensorModel, String> description;
	public static volatile SingularAttribute<PressureSensorModel, Long> id;
	public static volatile SingularAttribute<PressureSensorModel, String> tag;
	public static volatile SingularAttribute<PressureSensorModel, Double> pressureRead;

}


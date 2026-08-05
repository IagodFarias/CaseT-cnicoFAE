package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import si.dbcomm.enumerations.ValveStateEnum;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ValveModel.class)
public abstract class ValveModel_ {

	public static volatile SingularAttribute<ValveModel, String> description;
	public static volatile SingularAttribute<ValveModel, Long> id;
	public static volatile SingularAttribute<ValveModel, ValveStateEnum> state;
	public static volatile SingularAttribute<ValveModel, String> tag;

}


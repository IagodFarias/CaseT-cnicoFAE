package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import si.dbcomm.enumerations.LineStateEnum;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(WaterLineModel.class)
public abstract class WaterLineModel_ {

	public static volatile SingularAttribute<WaterLineModel, String> description;
	public static volatile SingularAttribute<WaterLineModel, Long> id;
	public static volatile SingularAttribute<WaterLineModel, LineStateEnum> state;
	public static volatile SingularAttribute<WaterLineModel, String> tag;

}


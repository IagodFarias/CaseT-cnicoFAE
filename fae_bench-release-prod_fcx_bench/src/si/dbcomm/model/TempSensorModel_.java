package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TempSensorModel.class)
public abstract class TempSensorModel_ {

	public static volatile SingularAttribute<TempSensorModel, Double> temperature;
	public static volatile SingularAttribute<TempSensorModel, String> description;
	public static volatile SingularAttribute<TempSensorModel, Date> readTime;
	public static volatile SingularAttribute<TempSensorModel, Long> id;
	public static volatile SingularAttribute<TempSensorModel, String> tag;

}


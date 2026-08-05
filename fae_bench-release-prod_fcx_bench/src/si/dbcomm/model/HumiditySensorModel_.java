package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(HumiditySensorModel.class)
public abstract class HumiditySensorModel_ {

	public static volatile SingularAttribute<HumiditySensorModel, String> description;
	public static volatile SingularAttribute<HumiditySensorModel, Double> humidity;
	public static volatile SingularAttribute<HumiditySensorModel, Date> readTime;
	public static volatile SingularAttribute<HumiditySensorModel, Long> id;
	public static volatile SingularAttribute<HumiditySensorModel, String> tag;

}


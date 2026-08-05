package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import si.dbcomm.enumerations.QaAssuredStatusEnum;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(QaAssuredModel.class)
public abstract class QaAssuredModel_ {

	public static volatile SingularAttribute<QaAssuredModel, String> serialNumberWmBus;
	public static volatile SingularAttribute<QaAssuredModel, String> serialNumberMeter;
	public static volatile SingularAttribute<QaAssuredModel, Date> dateOfReprove;
	public static volatile SingularAttribute<QaAssuredModel, MeterModel> meter;
	public static volatile SingularAttribute<QaAssuredModel, QaAssuredStatusEnum> qaAssuredStatusEnum;
	public static volatile SingularAttribute<QaAssuredModel, Long> id;

}


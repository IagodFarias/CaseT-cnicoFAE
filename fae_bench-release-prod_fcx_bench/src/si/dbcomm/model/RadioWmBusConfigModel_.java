package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RadioWmBusConfigModel.class)
public abstract class RadioWmBusConfigModel_ {

	public static volatile SingularAttribute<RadioWmBusConfigModel, String> wmBusSerialNumber;
	public static volatile SingularAttribute<RadioWmBusConfigModel, MeterModel> meter;
	public static volatile SingularAttribute<RadioWmBusConfigModel, Integer> transmitInterval;
	public static volatile SingularAttribute<RadioWmBusConfigModel, Long> id;
	public static volatile SingularAttribute<RadioWmBusConfigModel, String> wmBusCryptoKey;

}


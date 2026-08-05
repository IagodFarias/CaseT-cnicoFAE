package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BatchRadioWmBusConfigModel.class)
public abstract class BatchRadioWmBusConfigModel_ {

	public static volatile SingularAttribute<BatchRadioWmBusConfigModel, String> productCode;
	public static volatile SingularAttribute<BatchRadioWmBusConfigModel, Short> transmitInterval;
	public static volatile SingularAttribute<BatchRadioWmBusConfigModel, BatchModel> batch;
	public static volatile SingularAttribute<BatchRadioWmBusConfigModel, Long> id;
	public static volatile SingularAttribute<BatchRadioWmBusConfigModel, Integer> lastAssignedSerial;
	public static volatile SingularAttribute<BatchRadioWmBusConfigModel, Integer> startSerialSeq;
	public static volatile SingularAttribute<BatchRadioWmBusConfigModel, String> manufacNumber;
	public static volatile SingularAttribute<BatchRadioWmBusConfigModel, String> version;

}


package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BatchModel.class)
public abstract class BatchModel_ {

	public static volatile SetAttribute<BatchModel, BateladaModel> bateladas;
	public static volatile SingularAttribute<BatchModel, Long> idxProcessConfig;
	public static volatile SingularAttribute<BatchModel, Date> yearOfManufacture;
	public static volatile SingularAttribute<BatchModel, String> description;
	public static volatile SingularAttribute<BatchModel, Boolean> finished;
	public static volatile SingularAttribute<BatchModel, String> manufacturerCode;
	public static volatile SingularAttribute<BatchModel, String> batchId;
	public static volatile SingularAttribute<BatchModel, Integer> lastAssignedSerialSeq;
	public static volatile SetAttribute<BatchModel, MeterModel> meters;
	public static volatile SingularAttribute<BatchModel, Integer> numMeters;
	public static volatile SingularAttribute<BatchModel, MeterTypeModel> meterType;
	public static volatile SingularAttribute<BatchModel, ClientModel> client;
	public static volatile SingularAttribute<BatchModel, Long> id;
	public static volatile SingularAttribute<BatchModel, Integer> startSerialSeq;
	public static volatile SingularAttribute<BatchModel, FirmwareModel> firmware;

}


package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LogChangeBatchModel.class)
public abstract class LogChangeBatchModel_ {

	public static volatile SingularAttribute<LogChangeBatchModel, Date> dateOfChange;
	public static volatile SingularAttribute<LogChangeBatchModel, String> serialNumber;
	public static volatile SingularAttribute<LogChangeBatchModel, BatchModel> batchNew;
	public static volatile SingularAttribute<LogChangeBatchModel, MeterModel> meter;
	public static volatile SingularAttribute<LogChangeBatchModel, MeterTypeModel> meterTypeNew;
	public static volatile SingularAttribute<LogChangeBatchModel, Integer> meterUfoId;
	public static volatile SingularAttribute<LogChangeBatchModel, BatchModel> batchOld;
	public static volatile SingularAttribute<LogChangeBatchModel, Long> id;
	public static volatile SingularAttribute<LogChangeBatchModel, MeterTypeModel> meterTypeOld;

}


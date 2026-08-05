package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import si.dbcomm.enumerations.CalibrationTypeEnum;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LogChangeProcessBatchModel.class)
public abstract class LogChangeProcessBatchModel_ {

	public static volatile SingularAttribute<LogChangeProcessBatchModel, BateladaModel> bateladaEnd;
	public static volatile SingularAttribute<LogChangeProcessBatchModel, BatchModel> batch;
	public static volatile SingularAttribute<LogChangeProcessBatchModel, Boolean> useBatelada;
	public static volatile SingularAttribute<LogChangeProcessBatchModel, CalibrationTypeEnum> calibrationTypeEnum;
	public static volatile SingularAttribute<LogChangeProcessBatchModel, Long> id;
	public static volatile SingularAttribute<LogChangeProcessBatchModel, BateladaModel> bateladaInit;

}


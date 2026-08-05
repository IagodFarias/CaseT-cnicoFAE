package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import si.dbcomm.enumerations.CalibrationTypeEnum;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BateladaModel.class)
public abstract class BateladaModel_ {

	public static volatile SingularAttribute<BateladaModel, Integer> reprovedMetersOnCalcConst;
	public static volatile SingularAttribute<BateladaModel, Boolean> isReWork;
	public static volatile SingularAttribute<BateladaModel, Integer> reprovedMetersOnDownInitConst;
	public static volatile SingularAttribute<BateladaModel, Integer> reprovedMetersVerif;
	public static volatile SingularAttribute<BateladaModel, BatchModel> batch;
	public static volatile SingularAttribute<BateladaModel, Integer> connectedMeters;
	public static volatile SingularAttribute<BateladaModel, Integer> reprovedMetersOnCalcErrors;
	public static volatile SingularAttribute<BateladaModel, Double> maxFlowRatePermissibleError;
	public static volatile SetAttribute<BateladaModel, MeterModel> meters;
	public static volatile SingularAttribute<BateladaModel, Double> nominalFlowRatePermissibleError;
	public static volatile SingularAttribute<BateladaModel, Integer> reprovedMetersOnCalcZeroFlow;
	public static volatile SingularAttribute<BateladaModel, Double> minFlowRatePermissibleError;
	public static volatile SingularAttribute<BateladaModel, Integer> reprovedMetersOnDownZeroFlow;
	public static volatile SingularAttribute<BateladaModel, Integer> reprovedMetersOnDownConst;
	public static volatile SingularAttribute<BateladaModel, CalibrationTypeEnum> calibrationTypeEnum;
	public static volatile SingularAttribute<BateladaModel, Long> id;
	public static volatile SingularAttribute<BateladaModel, Date> endTime;
	public static volatile SingularAttribute<BateladaModel, ProcessConfigModel> processesConfigModel;
	public static volatile SingularAttribute<BateladaModel, Integer> runCount;
	public static volatile SingularAttribute<BateladaModel, Integer> reprovedMetersOnTrimAgc;
	public static volatile SingularAttribute<BateladaModel, Date> initTime;
	public static volatile SingularAttribute<BateladaModel, Integer> approvedMeters;

}


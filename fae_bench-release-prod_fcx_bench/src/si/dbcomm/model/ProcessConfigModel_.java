package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProcessConfigModel.class)
public abstract class ProcessConfigModel_ {

	public static volatile SetAttribute<ProcessConfigModel, BateladaModel> batelada;
	public static volatile SingularAttribute<ProcessConfigModel, Long> flowStabilityCheckTime;
	public static volatile SingularAttribute<ProcessConfigModel, Integer> flowBuffSize;
	public static volatile SetAttribute<ProcessConfigModel, FlowRateModel> verifFlowRates;
	public static volatile SingularAttribute<ProcessConfigModel, Integer> trimAgcMaxTries;
	public static volatile SingularAttribute<ProcessConfigModel, Integer> timeCheckPressurePumpLoad;
	public static volatile SingularAttribute<ProcessConfigModel, Integer> meterDownloadConstantsTries;
	public static volatile SingularAttribute<ProcessConfigModel, Boolean> isOnlyVerification;
	public static volatile SetAttribute<ProcessConfigModel, FlowRateModel> calibFlowRates;
	public static volatile SingularAttribute<ProcessConfigModel, Integer> windowSize;
	public static volatile SingularAttribute<ProcessConfigModel, Long> preFlowStabilizationTime;
	public static volatile SingularAttribute<ProcessConfigModel, Double> linePressurePumpLoad;
	public static volatile SingularAttribute<ProcessConfigModel, Boolean> saveMatData;
	public static volatile SingularAttribute<ProcessConfigModel, Integer> initCalibTemp;
	public static volatile SingularAttribute<ProcessConfigModel, Boolean> saveMatDataReprovedMeters;
	public static volatile SingularAttribute<ProcessConfigModel, Integer> meterRadioConfigTries;
	public static volatile SingularAttribute<ProcessConfigModel, Double> percentageToVerif;
	public static volatile SingularAttribute<ProcessConfigModel, Long> id;
	public static volatile SingularAttribute<ProcessConfigModel, Boolean> chooseQaSamples;
	public static volatile SingularAttribute<ProcessConfigModel, Boolean> saveCalibLutMatData;
	public static volatile SingularAttribute<ProcessConfigModel, Integer> meterDateUpdateTries;
	public static volatile SetAttribute<ProcessConfigModel, CalculatedFixedConstModel> calcFixedConsts;
	public static volatile SingularAttribute<ProcessConfigModel, String> descricao;
	public static volatile SingularAttribute<ProcessConfigModel, Long> timeOutFlowRate;
	public static volatile SingularAttribute<ProcessConfigModel, Long> zeroFlowTime;
	public static volatile SingularAttribute<ProcessConfigModel, Boolean> isZeroFlowEnabled;

}


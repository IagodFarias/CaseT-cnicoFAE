package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MeterModel.class)
public abstract class MeterModel_ {

	public static volatile SingularAttribute<MeterModel, BateladaModel> batelada;
	public static volatile SetAttribute<MeterModel, MeterDataModel> data;
	public static volatile SingularAttribute<MeterModel, Boolean> isMeterReplaceDateConfigured;
	public static volatile SingularAttribute<MeterModel, Boolean> isWmBusConfigured;
	public static volatile SingularAttribute<MeterModel, Double> dzc;
	public static volatile SingularAttribute<MeterModel, Boolean> isDownloadedConstants;
	public static volatile SingularAttribute<MeterModel, Boolean> isApprovedAgcTrim;
	public static volatile SingularAttribute<MeterModel, Boolean> isCommDown;
	public static volatile SingularAttribute<MeterModel, Long> id;
	public static volatile SingularAttribute<MeterModel, QaAssuredModel> QaAssured;
	public static volatile SingularAttribute<MeterModel, Double> dsos;
	public static volatile SingularAttribute<MeterModel, FirmwareModel> firmware;
	public static volatile SingularAttribute<MeterModel, Boolean> isCalculatedErrors;
	public static volatile SingularAttribute<MeterModel, Date> dateOfCalibration;
	public static volatile SingularAttribute<MeterModel, String> serialNumber;
	public static volatile SetAttribute<MeterModel, CalibConstantsModel> calibConstants;
	public static volatile SingularAttribute<MeterModel, Boolean> isCalculatedConstants;
	public static volatile SingularAttribute<MeterModel, Boolean> isMeterSystemDateConfigured;
	public static volatile SingularAttribute<MeterModel, BatchModel> batch;
	public static volatile SingularAttribute<MeterModel, Double> estimatedPathlength;
	public static volatile SingularAttribute<MeterModel, Boolean> isDeviationZeroFlow;
	public static volatile SingularAttribute<MeterModel, Date> dateOfReplace;
	public static volatile SingularAttribute<MeterModel, Boolean> isQa;
	public static volatile SingularAttribute<MeterModel, RadioWmBusConfigModel> radioWmBusConfig;
	public static volatile SingularAttribute<MeterModel, Boolean> isDownloadedZeroFlow;
	public static volatile SingularAttribute<MeterModel, Boolean> isApprovedVerification;
	public static volatile SingularAttribute<MeterModel, Boolean> isMeterSerialNumberConfigured;
	public static volatile SingularAttribute<MeterModel, ConversorModel> conversor;
	public static volatile SetAttribute<MeterModel, VolumetricErrorModel> volumetricErrors;
	public static volatile SingularAttribute<MeterModel, MeterTypeModel> meterTypeModel;
	public static volatile SingularAttribute<MeterModel, Boolean> isCalculatedZeroFlow;
	public static volatile SingularAttribute<MeterModel, Integer> meterUfoId;
	public static volatile SingularAttribute<MeterModel, Boolean> isDownloadedInitContants;
	public static volatile SetAttribute<MeterModel, VerificationErrorModel> errors;

}


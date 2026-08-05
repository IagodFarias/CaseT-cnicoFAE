package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MeterDataModel.class)
public abstract class MeterDataModel_ {

	public static volatile SingularAttribute<MeterDataModel, Double> hfc;
	public static volatile SingularAttribute<MeterDataModel, Double> accReVolume;
	public static volatile SingularAttribute<MeterDataModel, MeterModel> meter;
	public static volatile SingularAttribute<MeterDataModel, String> ip;
	public static volatile SingularAttribute<MeterDataModel, Double> velSwCalc;
	public static volatile SingularAttribute<MeterDataModel, Short> gm1;
	public static volatile SingularAttribute<MeterDataModel, Double> accReVolumeRev;
	public static volatile SingularAttribute<MeterDataModel, Date> readAt;
	public static volatile SingularAttribute<MeterDataModel, Double> volRe;
	public static volatile SingularAttribute<MeterDataModel, Double> temperature;
	public static volatile SingularAttribute<MeterDataModel, Double> ttstd;
	public static volatile SingularAttribute<MeterDataModel, Double> ttrev;
	public static volatile SingularAttribute<MeterDataModel, Long> id;
	public static volatile SingularAttribute<MeterDataModel, Double> vel;
	public static volatile SingularAttribute<MeterDataModel, Double> volFlowRe;
	public static volatile SingularAttribute<MeterDataModel, Short> agStatus;
	public static volatile SingularAttribute<MeterDataModel, Double> velRe;
	public static volatile SingularAttribute<MeterDataModel, Short> r1;
	public static volatile SingularAttribute<MeterDataModel, Double> expectedFlowRate;

}


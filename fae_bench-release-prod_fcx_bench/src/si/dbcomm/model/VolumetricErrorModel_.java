package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(VolumetricErrorModel.class)
public abstract class VolumetricErrorModel_ {

	public static volatile SingularAttribute<VolumetricErrorModel, Double> duration;
	public static volatile SingularAttribute<VolumetricErrorModel, Double> tankVolume;
	public static volatile SingularAttribute<VolumetricErrorModel, Double> initialVolume;
	public static volatile SingularAttribute<VolumetricErrorModel, MeterModel> meter;
	public static volatile SingularAttribute<VolumetricErrorModel, FlowRateModel> flowRate;
	public static volatile SingularAttribute<VolumetricErrorModel, VolumetricBenchModel> volumetricBench;
	public static volatile SingularAttribute<VolumetricErrorModel, Date> dateOfVerification;
	public static volatile SingularAttribute<VolumetricErrorModel, Double> finalVolume;
	public static volatile SingularAttribute<VolumetricErrorModel, Long> id;

}


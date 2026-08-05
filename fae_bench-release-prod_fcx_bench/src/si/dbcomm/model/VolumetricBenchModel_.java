package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(VolumetricBenchModel.class)
public abstract class VolumetricBenchModel_ {

	public static volatile SingularAttribute<VolumetricBenchModel, Integer> numberPositions;
	public static volatile SingularAttribute<VolumetricBenchModel, String> idBench;
	public static volatile SetAttribute<VolumetricBenchModel, VolumetricErrorModel> volumetricErrors;
	public static volatile SingularAttribute<VolumetricBenchModel, String> description;
	public static volatile SingularAttribute<VolumetricBenchModel, Long> id;
	public static volatile SingularAttribute<VolumetricBenchModel, Double> uncertainty;

}


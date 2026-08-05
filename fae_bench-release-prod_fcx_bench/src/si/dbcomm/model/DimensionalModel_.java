package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DimensionalModel.class)
public abstract class DimensionalModel_ {

	public static volatile SingularAttribute<DimensionalModel, Date> dateOfSampling;
	public static volatile SingularAttribute<DimensionalModel, String> description;
	public static volatile SingularAttribute<DimensionalModel, CarcassBatchModel> carcassBatch;
	public static volatile SingularAttribute<DimensionalModel, Long> id;
	public static volatile SetAttribute<DimensionalModel, DimensionMeasureModel> dimensionMeasures;

}


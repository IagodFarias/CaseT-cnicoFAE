package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DimensionMeasureModel.class)
public abstract class DimensionMeasureModel_ {

	public static volatile SingularAttribute<DimensionMeasureModel, DimensionalModel> dimensional;
	public static volatile SingularAttribute<DimensionMeasureModel, QuotaModel> quota;
	public static volatile SingularAttribute<DimensionMeasureModel, Long> id;
	public static volatile SingularAttribute<DimensionMeasureModel, Double> value;
	public static volatile SingularAttribute<DimensionMeasureModel, Date> measurementDate;

}


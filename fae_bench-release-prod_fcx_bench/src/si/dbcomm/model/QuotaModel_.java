package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(QuotaModel.class)
public abstract class QuotaModel_ {

	public static volatile SingularAttribute<QuotaModel, Boolean> isRequired;
	public static volatile SetAttribute<QuotaModel, DimensionMeasureModel> measures;
	public static volatile SingularAttribute<QuotaModel, Integer> fieldPosY;
	public static volatile SingularAttribute<QuotaModel, DrawingModel> drawing;
	public static volatile SingularAttribute<QuotaModel, String> description;
	public static volatile SingularAttribute<QuotaModel, Integer> fieldPosX;
	public static volatile SingularAttribute<QuotaModel, Double> upperLimit;
	public static volatile SingularAttribute<QuotaModel, Long> id;
	public static volatile SingularAttribute<QuotaModel, Double> lowerLimit;
	public static volatile SingularAttribute<QuotaModel, Double> nominalValue;
	public static volatile SingularAttribute<QuotaModel, Integer> fieldLength;

}


package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CarcassBatchModel.class)
public abstract class CarcassBatchModel_ {

	public static volatile SingularAttribute<CarcassBatchModel, Date> dateOfManufacture;
	public static volatile SingularAttribute<CarcassBatchModel, Integer> numberCarcass;
	public static volatile SetAttribute<CarcassBatchModel, DimensionalModel> dimensional;
	public static volatile SingularAttribute<CarcassBatchModel, DrawingModel> drawing;
	public static volatile SingularAttribute<CarcassBatchModel, String> description;
	public static volatile SingularAttribute<CarcassBatchModel, Long> id;

}


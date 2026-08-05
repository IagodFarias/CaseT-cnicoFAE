package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CalibConstantsModel.class)
public abstract class CalibConstantsModel_ {

	public static volatile SingularAttribute<CalibConstantsModel, Double> reynolds;
	public static volatile SingularAttribute<CalibConstantsModel, Double> constantK;
	public static volatile SingularAttribute<CalibConstantsModel, Double> avgPressure;
	public static volatile SingularAttribute<CalibConstantsModel, MeterModel> meter;
	public static volatile SingularAttribute<CalibConstantsModel, Double> flowRate;
	public static volatile SingularAttribute<CalibConstantsModel, Long> id;
	public static volatile SingularAttribute<CalibConstantsModel, Double> avgFlowRate;
	public static volatile SingularAttribute<CalibConstantsModel, Date> calcDate;
	public static volatile SingularAttribute<CalibConstantsModel, Double> avgTemp;

}


package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BenchDataModel.class)
public abstract class BenchDataModel_ {

	public static volatile SingularAttribute<BenchDataModel, RefMeterModel> refMeter;
	public static volatile SingularAttribute<BenchDataModel, Double> temperatureLi;
	public static volatile SingularAttribute<BenchDataModel, Double> temperatureLo;
	public static volatile SingularAttribute<BenchDataModel, BatchModel> batch;
	public static volatile SingularAttribute<BenchDataModel, Double> pressure;
	public static volatile SingularAttribute<BenchDataModel, Integer> counter;
	public static volatile SingularAttribute<BenchDataModel, Date> readAt;
	public static volatile SingularAttribute<BenchDataModel, Double> volume;
	public static volatile SingularAttribute<BenchDataModel, Double> pulseCalcFlowRate;
	public static volatile SingularAttribute<BenchDataModel, Double> flowRate;
	public static volatile SingularAttribute<BenchDataModel, Integer> timeVal;
	public static volatile SingularAttribute<BenchDataModel, Long> id;
	public static volatile SingularAttribute<BenchDataModel, Double> expectedFlowRate;

}


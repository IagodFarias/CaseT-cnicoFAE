package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import si.dbcomm.util.ConversorNumbers;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ConversorModel.class)
public abstract class ConversorModel_ {

	public static volatile SingularAttribute<ConversorModel, Integer> port;
	public static volatile SingularAttribute<ConversorModel, String> ip;
	public static volatile SingularAttribute<ConversorModel, ConversorNumbers> name;
	public static volatile SingularAttribute<ConversorModel, String> colorCode;
	public static volatile SingularAttribute<ConversorModel, Long> id;
	public static volatile SingularAttribute<ConversorModel, Boolean> enabled;
	public static volatile SetAttribute<ConversorModel, MeterModel> meters;

}


package si.dbcomm.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(VolumeModel.class)
public abstract class VolumeModel_ {

	public static volatile SingularAttribute<VolumeModel, Double> volume;
	public static volatile SingularAttribute<VolumeModel, Date> readTime;
	public static volatile SingularAttribute<VolumeModel, String> description;
	public static volatile SingularAttribute<VolumeModel, Long> id;

}


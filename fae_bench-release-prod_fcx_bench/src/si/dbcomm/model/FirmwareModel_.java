package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FirmwareModel.class)
public abstract class FirmwareModel_ {

	public static volatile SingularAttribute<FirmwareModel, byte[]> file;
	public static volatile SingularAttribute<FirmwareModel, String> name;
	public static volatile SingularAttribute<FirmwareModel, String> description;
	public static volatile SingularAttribute<FirmwareModel, Long> id;
	public static volatile SingularAttribute<FirmwareModel, Boolean> isActive;
	public static volatile SingularAttribute<FirmwareModel, String> version;

}


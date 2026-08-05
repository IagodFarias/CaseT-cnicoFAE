package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import si.dbcomm.enumerations.DrawingEnum;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DrawingModel.class)
public abstract class DrawingModel_ {

	public static volatile SingularAttribute<DrawingModel, byte[]> pdfFile;
	public static volatile SetAttribute<DrawingModel, QuotaModel> quotas;
	public static volatile SingularAttribute<DrawingModel, byte[]> imageFile;
	public static volatile SingularAttribute<DrawingModel, Integer> numberQuotas;
	public static volatile SingularAttribute<DrawingModel, String> description;
	public static volatile SetAttribute<DrawingModel, CarcassBatchModel> carcassBatch;
	public static volatile SingularAttribute<DrawingModel, Long> id;
	public static volatile SingularAttribute<DrawingModel, DrawingEnum> drawingEnum;

}


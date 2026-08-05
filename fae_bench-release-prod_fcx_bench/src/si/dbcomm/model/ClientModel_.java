package si.dbcomm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClientModel.class)
public abstract class ClientModel_ {

	public static volatile SingularAttribute<ClientModel, String> cidade;
	public static volatile SingularAttribute<ClientModel, String> telefone;
	public static volatile SingularAttribute<ClientModel, String> bairro;
	public static volatile SingularAttribute<ClientModel, String> cnpj;
	public static volatile SingularAttribute<ClientModel, String> cep;
	public static volatile SingularAttribute<ClientModel, String> uf;
	public static volatile SetAttribute<ClientModel, BatchModel> batches;
	public static volatile SingularAttribute<ClientModel, String> nomeFantasia;
	public static volatile SingularAttribute<ClientModel, String> logradouro;
	public static volatile SingularAttribute<ClientModel, String> inscricaoEstadual;
	public static volatile SingularAttribute<ClientModel, String> celular;
	public static volatile SingularAttribute<ClientModel, Long> id;
	public static volatile SingularAttribute<ClientModel, String> razaoSocial;
	public static volatile SingularAttribute<ClientModel, String> contato;
	public static volatile SingularAttribute<ClientModel, String> email;

}


//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ClientModel.java
*    @author marcos
*    @date 19 de set de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author marcos
 *
 */
@Entity
@Table(name = "clientmodel")
public class ClientModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5546391632111221544L;

	@Id
	@SequenceGenerator(name = "client_seq", sequenceName = "client_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "client_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_client", nullable = false)
	private Long id;

	@Column(name = "string_razaoSocial")
	private String razaoSocial;

	@Column(name = "string_nomefantasia")
	private String nomeFantasia;

	@Column(name = "string_inscricaoestadual")
	private String inscricaoEstadual;

	@Column(name = "string_email")
	private String email;

	@Column(name = "string_logradouro")
	private String logradouro;

	@Column(name = "string_bairro")
	private String bairro;

	@Column(name = "string_cep")
	private String cep;

	@Column(name = "string_uf")
	private String uf;

	@Column(name = "string_cidade")
	private String cidade;

	@Column(name = "string_telefone")
	private String telefone;

	@Column(name = "string_celular")
	private String celular;

	@Column(name = "string_contato")
	private String contato;

	@Column(name = "string_cnpj")
	private String cnpj;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "client", targetEntity = BatchModel.class)
	private Set<BatchModel> batches = new HashSet<>();

	/**
	 * Function returns the value of attribute cidade
	 * 
	 * @return the cidade
	 */
	public String getCidade() {
		return cidade;
	}

	/**
	 * Function sets the value for attribute cidade
	 * 
	 * @param cidade
	 *            the cidade to set
	 */
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	/**
	 * Function returns the value of attribute id
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Function sets the value for attribute id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Function returns the value of attribute razaoSocial
	 * 
	 * @return the razaoSocial
	 */
	public String getRazaoSocial() {
		return razaoSocial;
	}

	/**
	 * Function sets the value for attribute razaoSocial
	 * 
	 * @param razaoSocial
	 *            the razaoSocial to set
	 */
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	/**
	 * Function returns the value of attribute cnpj
	 * 
	 * @return the cnpj
	 */
	public String getCnpj() {
		return cnpj;
	}

	/**
	 * Function sets the value for attribute cnpj
	 * 
	 * @param cnpj
	 *            the cnpj to set
	 */
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	/**
	 * Function returns the value of attribute batches
	 * 
	 * @return the batches
	 */
	public Set<BatchModel> getBatches() {
		return batches;
	}

	/**
	 * Function sets the value for attribute batches
	 * 
	 * @param batches
	 *            the batches to set
	 */
	public void setBatches(Set<BatchModel> batches) {
		this.batches = batches;
	}

	/**
	 * Function returns the value of attribute nomeFantasia
	 * 
	 * @return the nomeFantasia
	 */
	public String getNomeFantasia() {
		return nomeFantasia;
	}

	/**
	 * Function sets the value for attribute nomeFantasia
	 * 
	 * @param nomeFantasia
	 *            the nomeFantasia to set
	 */
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	/**
	 * Function returns the value of attribute inscricaoEstadual
	 * 
	 * @return the inscricaoEstadual
	 */
	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	/**
	 * Function sets the value for attribute inscricaoEstadual
	 * 
	 * @param inscricaoEstadual
	 *            the inscricaoEstadual to set
	 */
	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	/**
	 * Function returns the value of attribute email
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Function sets the value for attribute email
	 * 
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Function returns the value of attribute logradouro
	 * 
	 * @return the logradouro
	 */
	public String getLogradouro() {
		return logradouro;
	}

	/**
	 * Function sets the value for attribute logradouro
	 * 
	 * @param logradouro
	 *            the logradouro to set
	 */
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	/**
	 * Function returns the value of attribute bairro
	 * 
	 * @return the bairro
	 */
	public String getBairro() {
		return bairro;
	}

	/**
	 * Function sets the value for attribute bairro
	 * 
	 * @param bairro
	 *            the bairro to set
	 */
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	/**
	 * Function returns the value of attribute cep
	 * 
	 * @return the cep
	 */
	public String getCep() {
		return cep;
	}

	/**
	 * Function sets the value for attribute cep
	 * 
	 * @param cep
	 *            the cep to set
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}

	/**
	 * Function returns the value of attribute uf
	 * 
	 * @return the uf
	 */
	public String getUf() {
		return uf;
	}

	/**
	 * Function sets the value for attribute uf
	 * 
	 * @param uf
	 *            the uf to set
	 */
	public void setUf(String uf) {
		this.uf = uf;
	}

	/**
	 * Function returns the value of attribute telefone
	 * 
	 * @return the telefone
	 */
	public String getTelefone() {
		return telefone;
	}

	/**
	 * Function sets the value for attribute telefone
	 * 
	 * @param telefone
	 *            the telefone to set
	 */
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	/**
	 * Function returns the value of attribute contato
	 * 
	 * @return the contato
	 */
	public String getContato() {
		return contato;
	}

	/**
	 * Function sets the value for attribute contato
	 * 
	 * @param contato
	 *            the contato to set
	 */
	public void setContato(String contato) {
		this.contato = contato;
	}

	/**
	 * Function returns the value of attribute celular
	 * 
	 * @return the celular
	 */
	public String getCelular() {
		return celular;
	}

	/**
	 * Function sets the value for attribute celular
	 * 
	 * @param celular
	 *            the celular to set
	 */
	public void setCelular(String celular) {
		this.celular = celular;
	}

	@Override
	public String toString() {
		return nomeFantasia;
	}
}

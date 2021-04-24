package br.com.iwe.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "country")
public class Country {

	@DynamoDBHashKey(attributeName = "pais")
	private String pais;
	@DynamoDBRangeKey(attributeName = "cep")
	private String cep;

	@DynamoDBIndexRangeKey(attributeName = "cidade", localSecondaryIndexName = "cidadeIndex")
	private String cidade;

	@DynamoDBAttribute(attributeName = "nome")
	private String nome;
	@DynamoDBAttribute(attributeName = "logradouro")
	private String logradouro;

	@DynamoDBIndexRangeKey(attributeName = "consumed", localSecondaryIndexName = "consumedIndex")
	private String consumed;

	public Country(String pais, String cep, String cidade, String nome, String logradouro, String consumed) {
		super();
		this.pais = pais;
		this.cep = cep;
		this.cidade = cidade;
		this.nome = nome;
		this.logradouro = logradouro;
		this.consumed = consumed;
	}

	public Country() {
		super();
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getConsumed() {
		return consumed;
	}

	public void setConsumed(String consumed) {
		this.consumed = consumed;
	}

}

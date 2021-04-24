package br.com.iwe.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import br.com.iwe.model.Country;

public class CountryRepository {

	private static final DynamoDBMapper mapper = DynamoDBManager.mapper();

	public Country save(final Country country) {
		mapper.save(country);
		return country;
	}

	public List<Country> findByCep(final String pais, final String starts, final String ends) {

		final Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(pais));
		eav.put(":val2", new AttributeValue().withS(starts));
		eav.put(":val3", new AttributeValue().withS(ends));

		final DynamoDBQueryExpression<Country> queryExpression = new DynamoDBQueryExpression<Country>()
				.withKeyConditionExpression("pais = :val1 and cep between :val2 and :val3")
				.withExpressionAttributeValues(eav);

		final List<Country> countries = mapper.query(Country.class, queryExpression);

		return countries;
	}

	public List<Country> findByCidade(final String pais, final String cidade) {

		final Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(pais));
		eav.put(":val2", new AttributeValue().withS(cidade));

		final DynamoDBQueryExpression<Country> queryExpression = new DynamoDBQueryExpression<Country>()
				.withIndexName("cidadeIndex").withConsistentRead(false)
				.withKeyConditionExpression("pais = :val1 and cidade=:val2").withExpressionAttributeValues(eav);

		final List<Country> countries = mapper.query(Country.class, queryExpression);

		return countries;
	}

	public List<Country> findByIsConsumed(final String pais, final String isConsumed) {

		final Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(pais));
		eav.put(":val2", new AttributeValue().withS(isConsumed));

		final Map<String, String> expression = new HashMap<>();

		// consumed is a reserver word in DynamoDB
		expression.put("#consumed", "consumed");

		final DynamoDBQueryExpression<Country> queryExpression = new DynamoDBQueryExpression<Country>()
				.withIndexName("consumedIndex").withConsistentRead(false)
				.withKeyConditionExpression("pais = :val1 and #consumed=:val2").withExpressionAttributeValues(eav)
				.withExpressionAttributeNames(expression);

		final List<Country> countries = mapper.query(Country.class, queryExpression);

		return countries;
	}
}
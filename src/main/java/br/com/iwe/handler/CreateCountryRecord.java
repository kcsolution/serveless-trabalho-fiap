package br.com.iwe.handler;

import java.io.IOException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.iwe.dao.CountryRepository;
import br.com.iwe.model.HandlerRequest;
import br.com.iwe.model.HandlerResponse;
import br.com.iwe.model.Country;

public class CreateCountryRecord implements RequestHandler<HandlerRequest, HandlerResponse> {
	
	private final CountryRepository repository = new CountryRepository();

	@Override
	public HandlerResponse handleRequest(final HandlerRequest request, final Context context) {

		Country country = null;
		try {
			country = new ObjectMapper().readValue(request.getBody(), Country.class);
		} catch (IOException e) {
			return HandlerResponse.builder().setStatusCode(400).setRawBody("Este é um erro de Pais!").build();
		}
		context.getLogger().log("Criando um novo registro de pais " + country.getPais());
		final Country countryRecorded = repository.save(country);
		return HandlerResponse.builder().setStatusCode(201).setObjectBody(countryRecorded).build();
	}
}
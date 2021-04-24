package br.com.iwe.handler;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.iwe.dao.CountryRepository;
import br.com.iwe.model.HandlerRequest;
import br.com.iwe.model.HandlerResponse;
import br.com.iwe.model.Country;

public class GetCountryRecordsByCep implements RequestHandler<HandlerRequest, HandlerResponse> {

	private final CountryRepository repository = new CountryRepository();
	
	@Override
	public HandlerResponse handleRequest(HandlerRequest request, Context context) {

		final String pais = request.getPathParameters().get("pais");
		final String starts = request.getQueryStringParameters().get("starts");
		final String ends = request.getQueryStringParameters().get("ends");

		context.getLogger().log("Procurando paises por " + pais + " pais between " + starts + " and " + ends);

		final List<Country> countries = this.repository.findByCep(pais, starts, ends);
		
		if(countries == null || countries.isEmpty()) {
			return HandlerResponse.builder().setStatusCode(404).build();
		}
		
		return HandlerResponse.builder().setStatusCode(200).setObjectBody(countries).build();
	}
}
package br.com.iwe.handler;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.iwe.dao.CountryRepository;
import br.com.iwe.model.HandlerRequest;
import br.com.iwe.model.HandlerResponse;
import br.com.iwe.model.Country;

public class GetCountryRecordsByIsConsumed implements RequestHandler<HandlerRequest, HandlerResponse> {

	private final CountryRepository repository = new CountryRepository();

	@Override
	public HandlerResponse handleRequest(HandlerRequest request, Context context) {

		final String pais = request.getPathParameters().get("pais");
		final String isConsumed = request.getQueryStringParameters().get("isconsumed");

		context.getLogger()
				.log("Procurando registros de paises por " + pais + " isto é Consumed igual " + isConsumed);
		final List<Country> countries = this.repository.findByIsConsumed(pais, isConsumed);

		if (countries == null || countries.isEmpty()) {
			return HandlerResponse.builder().setStatusCode(404).build();
		}

		return HandlerResponse.builder().setStatusCode(200).setObjectBody(countries).build();
	}
}
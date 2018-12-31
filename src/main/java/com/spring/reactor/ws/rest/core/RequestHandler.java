package com.spring.reactor.ws.rest.core;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

import java.util.function.Function;

import javax.validation.Validator;

@Component
public class RequestHandler {

	private final Validator validator;

	public RequestHandler(Validator validator) {
		this.validator = validator;
	}

	public <BODY> Mono<ServerResponse> requireValidBody(Function<Mono<BODY>, Mono<ServerResponse>> block,
			ServerRequest request, Class<BODY> bodyClass) {

		return request.bodyToMono(bodyClass)
				.flatMap(body -> validator.validate(body).isEmpty() ? block.apply(Mono.just(body))
						: ServerResponse.unprocessableEntity().build());
	}

}

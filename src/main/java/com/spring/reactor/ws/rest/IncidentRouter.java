package com.spring.reactor.ws.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class IncidentRouter {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpWebClient.class);

	@Bean
	public RouterFunction<ServerResponse> incidentRoute(IncidentHandler incidentHandler) {
		LOGGER.info("Inside incidentRoute method");
		return route(GET("/incident/list").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
				incidentHandler::getIncidents)
						.and(route(GET("/incidents").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
								incidentHandler::getIncidents))
						.and(route(
								POST("/apptoapp/token/generate")
										.and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
								incidentHandler::getIncidents));
	}

}

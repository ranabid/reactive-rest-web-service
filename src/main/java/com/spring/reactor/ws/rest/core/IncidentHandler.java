package com.spring.reactor.ws.rest.core;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class IncidentHandler {
//	private RequestHandler requestHandler;
//	
//	public IncidentHandler(RequestHandler requestHandler) {
//		this.requestHandler = requestHandler;	
//	}
	
	public Mono<ServerResponse> getIncidents(ServerRequest request) {
//		String response = "{"
//				+ "\"INC12345\":"
//					+ "{"
//						+ "\"title\": \"Auto Alert\","
//						+ "\"configurtion\": \"genesys\" "
//					+ "}"
//				+ "}";
		
		String response = "{"
				+"\"token\": \"DBG&FRDT%$^_HYUJHGrfse453&^52\""
				+"}";
				
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(response));
	}

}



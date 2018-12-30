package com.spring.reactor.ws.rest;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Map;
import org.eclipse.jetty.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

public class HttpWebClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpWebClient.class);
	private final WebClient webClient;

	public HttpWebClient(final String baseUrl) {

		this.webClient = WebClient.builder().baseUrl(baseUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	public String wrappedGet(final String uri) throws Exception {
		try {
			return this.webClient.get().uri(uri).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class)
					.block();

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex);
		}
	}

	public String wrappedPost(final String uri, final Object requestBody) throws Exception {

		try {
			String response = this.webClient.post().uri(URI.create(uri)).contentType(MediaType.APPLICATION_JSON)
					.syncBody(requestBody).retrieve().bodyToMono(String.class).block();
			return response;

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex);
		}
	}

	public String wrappedPost(final String uri, final String requestBody, final Map<String, String> headerList)
			throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.setAll(headerList);
		String response = "";
		try {
			response = this.webClient.post().uri(URI.create(uri)).contentType(MediaType.APPLICATION_JSON)
					.headers(headers -> {
						headers.addAll(params);
					}).syncBody(requestBody).retrieve().bodyToMono(String.class).block();
			return response;

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex);
		}
	}

	public void setCookies() {
		HttpClient httpClient = new HttpClient();
		CookieStore cookieStore = new CookieManager().getCookieStore();
		HttpCookie cookie = new HttpCookie("role", "Admin");
		cookieStore.add(URI.create("/admin"), cookie);
		httpClient.setCookieStore(cookieStore);
		ClientHttpConnector connector = new JettyClientHttpConnector(httpClient);

		/*
		 * WebClient client = WebClient.builder() .baseUrl(baseUrl)
		 * .clientConnector(connector) .build();
		 */

	}

	// Client Filters
	public void exchangeFilterFunction() {
		WebClient client = WebClient.builder().filter((request, next) -> {

			ClientRequest filtered = ClientRequest.from(request).header("foo", "bar").build();

			return next.exchange(filtered);
		}).build();
	}

}

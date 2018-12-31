package com.spring.reactor.ws.rest.core;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.reactor.ws.rest.common.ApplicationProperties;

public class TokenManager {

	private static ApplicationProperties prop;

	private final LocalDateTime currentDateTimeUTC;
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenManager.class);
	private Map<String, String> tokenCache;
	private final DateTimeFormatter formatter;
	private final HttpWebClient httpWebClient;

	public TokenManager() {
		LOGGER.info("Inside a constructor TokenManager");
		this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.currentDateTimeUTC = LocalDateTime.now(Clock.systemUTC());
		this.tokenCache = new HashMap<String, String>();
		this.httpWebClient = new HttpWebClient(prop.getIdmsAuthEndpoints().getBaseUrl());
	}

	private Boolean isTokenvalid() {
		LOGGER.info("Inside isTokenvalid");
		boolean isValid = false;
		if (!tokenCache.get("ttl").isEmpty()) {
			LocalDateTime tokenTTL = LocalDateTime.parse(tokenCache.get("ttl"), formatter);
			if (tokenTTL.compareTo(currentDateTimeUTC) <= 0) {
				LOGGER.info("token is invalid");
				isValid = false;
			} else {
				isValid = true;
			}
		}

		return isValid;
	}

	private String generateNewTokenAndCache() throws Exception {
		LOGGER.info("inside generateNewTokenAndCache");
		try {

			LOGGER.info("Calling http post");

			String newTokenJson = this.httpWebClient.wrappedPost(prop.getIdmsAuthEndpoints().getGenerateUri(),
					this.new TokenRequestBody());

			LOGGER.info("Current time: " + getCurrentDateTimeUTC());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = new HashMap<String, Object>();

			map = mapper.readValue(newTokenJson, new TypeReference<Map<String, String>>() {
			});

			tokenCache.put("token", map.get("token").toString());
			tokenCache.put("ttl", this.formatter
					.format(this.currentDateTimeUTC.plusMinutes(Integer.parseInt(prop.getTimeToLive()) / 60)));

			LOGGER.info("ttl: " + tokenCache.get("ttl"));
			return tokenCache.get("token");
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		}
	}

	public String getCurrentDateTimeUTC() {
		return this.formatter.format(currentDateTimeUTC);
	}

	public String getToken() throws Exception {
		LOGGER.info("Inside getToken");
		try {
			if (!this.tokenCache.isEmpty() && isTokenvalid()) {
				LOGGER.info("token is not empty");
				return tokenCache.get("token");
			} else {
				LOGGER.info("getting new token");
				return this.generateNewTokenAndCache();
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		}
	}

	public static void setApplicationProperties(ApplicationProperties prop) {
		TokenManager.prop = prop;
	}

	private class TokenRequestBody {
		private String appId;
		private String appPassword;
		private String context;
		private String otherApp;
		private String contextVersion;
		private String oneTimeToken;
		private String timeToLive;

		public TokenRequestBody() {
			this.appId = prop.getAppId();
			this.appPassword = prop.getAppPassword();
			this.context = prop.getContext();
			this.otherApp = prop.getOtherApp();
			this.contextVersion = prop.getContextVersion();
			this.oneTimeToken = prop.getOneTimeToken();
			this.timeToLive = prop.getTimeToLive();
		}

		public String getAppId() {
			return appId;
		}

		public String getAppPassword() {
			return appPassword;
		}

		public String getContext() {
			return context;
		}

		public String getOtherApp() {
			return otherApp;
		}

		public String getContextVersion() {
			return contextVersion;
		}

		public String getOneTimeToken() {
			return oneTimeToken;
		}

		public String getTimeToLive() {
			return timeToLive;
		}

		@Override
		public String toString() {

			return "appId: " + appId + " appPassword: " + appPassword + " context: " + context + " otherApp" + otherApp
					+ " contextVesrion: " + contextVersion + " oneTimeToken: " + oneTimeToken + " timeToLive: "
					+ timeToLive;

		}

	}

}

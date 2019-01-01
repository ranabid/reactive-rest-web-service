package com.spring.reactor.ws.rest.core;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.reactor.ws.rest.common.ApplicationProperties;

public class TokenManager {

	private static ApplicationProperties prop;
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenManager.class);
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private Map<String, String> tokenCache;
	private final HttpWebClient httpWebClient;

	public TokenManager() {
		LOGGER.debug("Inside a constructor TokenManager");
		this.tokenCache = new HashMap<String, String>();
		this.httpWebClient = new HttpWebClient(prop.getIdmsAuthEndpoints().getBaseUrl());
	}

	private Boolean isTokenvalid() throws Exception {
		LOGGER.debug("Inside isTokenvalid");

		boolean isValid = false;
		if (!tokenCache.get("ttl").isEmpty()) {
			LocalDateTime currentDateTimeGMT = LocalDateTime.parse(getCurrentDateTimeGMT(), formatter);
			LocalDateTime tokenTTL = LocalDateTime.parse(tokenCache.get("ttl"), formatter);
			if (tokenTTL.compareTo(currentDateTimeGMT) <= 0) {
				LOGGER.info("token is invalid at - "+tokenTTL.format(formatter)+" GMT");
				isValid = false;
			} else {
				LOGGER.info("token is valid till - "+tokenTTL.format(formatter)+" GMT");
				isValid = true;
			}
		}

		return isValid;
	}

	private String generateNewTokenAndCache() throws Exception {
		LOGGER.debug("inside generateNewTokenAndCache");
		try {

			LOGGER.info("Calling http post");

			String newTokenJson = this.httpWebClient.wrappedPost(prop.getIdmsAuthEndpoints().getGenerateUri(),
					this.new TokenRequestBody());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = new HashMap<String, Object>();

			map = mapper.readValue(newTokenJson, new TypeReference<Map<String, String>>() {
			});
			LocalDateTime currentDateTimeGMT = LocalDateTime.parse(getCurrentDateTimeGMT(), formatter).plusMinutes(10);
			tokenCache.put("token", map.get("token").toString());
			tokenCache.put("ttl", currentDateTimeGMT.format(formatter));
			return tokenCache.get("token");
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		}
	}

	public String getCurrentDateTimeGMT() {
		Instant timestamp = Instant.now();
		ZonedDateTime timestampAtGMT = timestamp.atZone(ZoneId.of("GMT"));
		return timestampAtGMT.format(formatter);
	}

	public String getToken() throws Exception {
		LOGGER.debug("Inside getToken");
		LOGGER.info("Current time: "+getCurrentDateTimeGMT()+" GMT");
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

	@SuppressWarnings("unused")
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

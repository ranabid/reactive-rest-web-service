package com.spring.reactor.ws.rest.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="app")
public class ApplicationProperties {
	private String appId;
	private String appPassword;
	private String env;
	private String context;
	private String contextVersion;
	private String otherApp;
	private String authProvider;
	private String timeToLive;
	private String oneTimeToken;
	public String getOneTimeToken() {
		return oneTimeToken;
	}

	public void setOneTimeToken(String oneTimeToken) {
		this.oneTimeToken = oneTimeToken;
	}

	public String getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(String timeToLive) {
		this.timeToLive = timeToLive;
	}
	private IdmsAuthEndpoints idmsAuthEndpoints;
	
	public static class IdmsAuthEndpoints {
		private String baseUrl;
		private String generateUri;
		private String validateUri;
		private Map<String, String> httpHeaders = new HashMap<String, String>();
		public String getBaseUrl() {
			return baseUrl;
		}
		public void setBaseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
		}
		public String getGenerateUri() {
			return generateUri;
		}
		public void setGenerateUri(String generateUri) {
			this.generateUri = generateUri;
		}
		public String getValidateUri() {
			return validateUri;
		}
		public void setValidateUri(String validateUri) {
			this.validateUri = validateUri;
		}
		public Map<String, String> getHttpHeaders() {
			return httpHeaders;
		}
		public void setHttpHeaders(Map<String, String> httpHeaders) {
			this.httpHeaders = httpHeaders;
		}
		
	}
 	
	
	public String getAppId() {
		return appId;
	}
	
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public String getAppPassword() {
		return appPassword;
	}
	public void setAppPassword(String appPassword) {
		this.appPassword = appPassword;
	}
	public String getEnv() {
		return env;
	}
	public void setEnv(String env) {
		this.env = env;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getContextVersion() {
		return contextVersion;
	}
	public void setContextVersion(String contextVersion) {
		this.contextVersion = contextVersion;
	}
	public String getOtherApp() {
		return otherApp;
	}
	public void setOtherApp(String otherApp) {
		this.otherApp = otherApp;
	}
	public String getAuthProvider() {
		return authProvider;
	}
	public void setAuthProvider(String authProvider) {
		this.authProvider = authProvider;
	}
	public IdmsAuthEndpoints getIdmsAuthEndpoints() {
		return idmsAuthEndpoints;
	}
	public void setIdmsAuthEndpoints(IdmsAuthEndpoints idmsAuthEndpoints) {
		this.idmsAuthEndpoints = idmsAuthEndpoints;
	}
}

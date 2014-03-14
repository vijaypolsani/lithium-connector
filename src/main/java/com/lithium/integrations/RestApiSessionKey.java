package com.lithium.integrations;

public enum RestApiSessionKey {
	SESSION_KEY("");

	private String sessionKey;

	private RestApiSessionKey(String s) {
		sessionKey = s;
	}

	public String getSessionKey() {
		return sessionKey;
	}

}
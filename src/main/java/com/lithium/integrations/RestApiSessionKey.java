package com.lithium.integrations;

public class RestApiSessionKey {
	private RestApiSessionKey() {
		// private constructor
	}

	public static RestApiSessionKey myInstance;

	public static RestApiSessionKey getInstance() {
		if (myInstance == null) {
			synchronized (RestApiSessionKey.class) {
				myInstance = new RestApiSessionKey();
			}
		}
		return myInstance;
	}
}

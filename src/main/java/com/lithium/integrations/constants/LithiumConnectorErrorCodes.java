package com.lithium.integrations.constants;

public enum LithiumConnectorErrorCodes {
	CONNECTOR_1000(1000, "Exception in UnMarshalling of Rest Reponse Generic Response Data for STATUS code."), CONNECTOR_REST_CLIENT_2000(
			2000, "The session key is NULL. Need to login."), CONNECTOR_REST_CLIENT_2001(2001,
			"Invalid Session Key. Hence retry to get New Login Session Key."), CONNECTOR_REST_CLIENT_2002(2002,
			"In Jersey Client. Login did not succeed: "), CONNECTOR_10001(1001,
			"Exception in client connection of Rest Reponse Generic Response Data for STATUS code.");

	private final int code;
	private final String description;

	private LithiumConnectorErrorCodes(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return code + ": " + description;
	}
}

package com.lithium.integrations;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.lithium.integrations.model.LithiumLoginResponse;
import static com.lithium.integrations.constants.QueryParameterConstants.*;

public class LithiumSessionRestClient {
	private static String restApiSessionKey = null;
	private static Client client = Client.create();

	public static synchronized String getRestApiSessionKey() {
		return restApiSessionKey;
	}

	public static synchronized void setRestApiSessionKey(String restApiSessionKey) {
		LithiumSessionRestClient.restApiSessionKey = restApiSessionKey;
	}

	public static String invokeToGetRestSessionKey(String url, MultivaluedMap<String, String> queryParams) {
		boolean adminCall = false;
		if (url == null) {
			url = LOGIN_URL;
			adminCall = true;
		}
		System.out.println("In Jersey Client. URL: " + url);
		System.out.println("In Jersey Client. queryParams: " + queryParams);
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.queryParams(queryParams).accept("application/xml")
				.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			System.out.println("In Jersey Client. Login Response Data: " + response.getStatus());
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else if (String.valueOf(response.getStatus()).startsWith("3")) {
			return String.valueOf(response.getStatus());
		}
		if (adminCall) {
			System.out.println("In Jersey Client. Made Admin Call.");
			LithiumLoginResponse lithiumLoginResponse = response.getEntity(LithiumLoginResponse.class);
			System.out.println(lithiumLoginResponse);
			setRestApiSessionKey(lithiumLoginResponse.getValue());
			return lithiumLoginResponse.getValue();
		} else {
			System.out.println("In Jersey Client. Making genric restapi Call.");
			String restResponse = response.getEntity(String.class);
			System.out.println(restResponse);
			return restResponse;
		}

	}

	public static void main(String args[]) {
		LithiumSessionRestClient.invokeToGetRestSessionKey(null, null);
	}

}

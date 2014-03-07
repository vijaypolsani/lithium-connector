package com.lithium.integrations;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.lithium.integrations.model.LithiumLoginResponse;
import static com.lithium.integrations.constants.QueryParameterConstants.*;

public class LithiumSessionRestClient {
	private static final MultivaluedMap<String, String> adminQueryParams = new MultivaluedMapImpl();

	public static String invokeToGetRestSessionKey(String url, MultivaluedMap<String, String> queryParams) {
		boolean adminCall = false;
		if (url == null)
			url = LOGIN_URL;
		if (queryParams == null || queryParams.get(RESTAPI_SESSION_KEY) == null) {
			System.out.println("Query Params is null or restapi.session_key is null." + queryParams);
			queryParams = new MultivaluedMapImpl();
			adminQueryParams.add(LOGIN_USER_NAME_PARAM, LOGIN_USER_NAME_VALUE);
			adminQueryParams.add(LOGIN_PASSWORD_PARAM, LOGIN_PASSWORD_VALUE);
			queryParams.putAll(adminQueryParams);
			adminCall = true;
		}
		System.out.println("URL: " + url);
		System.out.println("queryParams: " + queryParams.toString());
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.queryParams(queryParams).accept("application/xml")
				.get(ClientResponse.class);
		System.out.println("Login Response Data: " + response);
		if (response.getStatus() != 200) {
			System.out.println("Login Response Data: " + response.getStatus());
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else if (String.valueOf(response.getStatus()).startsWith("3")) {
			return String.valueOf(response.getStatus());
		}
		if (adminCall) {
			System.out.println("Making Admin Call.");
			LithiumLoginResponse lithiumLoginResponse = response.getEntity(LithiumLoginResponse.class);
			System.out.println(lithiumLoginResponse);
			return lithiumLoginResponse.getValue();
		} else {
			System.out.println("Making genric restapi Call.");
			String restResponse = response.getEntity(String.class);
			System.out.println(restResponse);
			return restResponse;
		}

	}

	public static void main(String args[]) {
		LithiumSessionRestClient.invokeToGetRestSessionKey(null, null);
	}

}

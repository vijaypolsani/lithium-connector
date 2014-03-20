package com.lithium.integrations;

import java.io.ByteArrayInputStream;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.lithium.integrations.model.LithiumGenericResponse;
import com.lithium.integrations.model.LithiumLoginResponse;
import static com.lithium.integrations.constants.QueryParameterConstants.*;

public class LithiumSessionRestClient {
	private static String restApiSessionKey = null;
	private static Client client = Client.create();

	public static String getRestApiSessionKey() {
		return restApiSessionKey;
	}

	public static synchronized void setRestApiSessionKey(String restApiSessionKey) {
		LithiumSessionRestClient.restApiSessionKey = restApiSessionKey;
	}

	public static String invokeToGetRestSessionKey(String url, MultivaluedMap<String, String> queryParams) {
		if (url == null) {
			url = LOGIN_URL;
		}
		System.out.println("In Jersey Client. Login URL: " + url);
		System.out.println("In Jersey Client. Login queryParams: " + queryParams);
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.queryParams(queryParams).accept("application/xml")
				.get(ClientResponse.class);
		LithiumLoginResponse lithiumLoginResponse = response.getEntity(LithiumLoginResponse.class);
		if (lithiumLoginResponse.getStatus().equalsIgnoreCase("error")) {
			System.out.println("In Jersey Client. Login did not succeed: " + lithiumLoginResponse);
			return lithiumLoginResponse.getStatus();
		}
		System.out.println("In Jersey Client. Made Admin Call." + lithiumLoginResponse);
		setRestApiSessionKey(lithiumLoginResponse.getValue());
		return lithiumLoginResponse.getValue();
	}

	public static String invokeGenericRestCall(String url, MultivaluedMap<String, String> queryParams) {
		System.out.println("In Jersey Client. Generic URL: " + url);
		if (getRestApiSessionKey() != null)
			queryParams.add(RESTAPI_SESSION_KEY, getRestApiSessionKey());
		else
			return "399";
		System.out.println("In Jersey Client. Generic queryParams: " + queryParams);
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.queryParams(queryParams).accept("application/xml")
				.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			System.out.println("In Jersey Client. Rest Call is Not Successfull: " + response.getStatus());
			return String.valueOf(response.getStatus());
		}
		System.out.println("In Jersey Client. Making Genric restapi Call.");
		String restResponse = response.getEntity(String.class);

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(LithiumGenericResponse.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			LithiumGenericResponse lithiumGenericResponse = (LithiumGenericResponse) jaxbUnmarshaller
					.unmarshal(new ByteArrayInputStream(restResponse.getBytes()));
			if (lithiumGenericResponse.getStatus().equalsIgnoreCase(ERROR))
				return ERROR;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(restResponse);
		return restResponse;
	}

	public static void main(String args[]) {
		LithiumSessionRestClient.invokeToGetRestSessionKey(null, null);
	}

}

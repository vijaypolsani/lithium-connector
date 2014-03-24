package com.lithium.integrations;

import java.io.ByteArrayInputStream;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.lithium.integrations.constants.LithiumConnectorErrorCodes;
import com.lithium.integrations.model.LithiumGenericResponse;
import com.lithium.integrations.model.LithiumLoginResponse;
import static com.lithium.integrations.constants.QueryParameterConstants.*;

/**
 * Lithium Jersey Session Client used for invoking Rest API calls.
 * 
 * @author Vijay Polsani. Lithium Technologies Inc.
 */
public class LithiumSessionRestClient {
	public static Logger log = LoggerFactory.getLogger(LithiumSessionRestClient.class);
	private static String restApiSessionKey = null;
	private static Client client = Client.create();

	public static String getRestApiSessionKey() {
		return restApiSessionKey;
	}

	public static synchronized void setRestApiSessionKey(String restApiSessionKey) {
		LithiumSessionRestClient.restApiSessionKey = restApiSessionKey;
	}

	/** 
	  * Performs the LDN Login Operation and stores the SessionKey in the static variable.
	  * 
	  * This method is a static method and connects based on Community HostName, CommunityName, User, Password.
	  * Perform the HTTP login only. SSL based in not yet supported.
	  * @param url The nicely formed URL for loggin-in.
	  * @param MultivaluedMap The map for Jersey Client to populate Key/value parameters for GET/POST.
	  * @return String Response Session Key for caching and reuse.
	  * 
	  */
	public static String invokeToGetRestSessionKey(String url, MultivaluedMap<String, String> queryParams) {
		log.debug(String.format("In Lithium Client. Login URL: %s Login Query ParametersL %s ", url, queryParams));
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.queryParams(queryParams).accept("application/xml")
				.get(ClientResponse.class);
		LithiumLoginResponse lithiumLoginResponse = response.getEntity(LithiumLoginResponse.class);
		if (lithiumLoginResponse.getStatus().equalsIgnoreCase("error")) {
			log.debug(LithiumConnectorErrorCodes.CONNECTOR_REST_CLIENT_2002.getDescription() + lithiumLoginResponse);
			return lithiumLoginResponse.getStatus();
		}
		log.debug("In Jersey Client. Made Admin Call." + lithiumLoginResponse);
		setRestApiSessionKey(lithiumLoginResponse.getValue());
		return lithiumLoginResponse.getValue();
	}

	/** 
	  * Performs the LDN REST URL Operation and sends the response back to the caller. The initial response is
	  * unmarshlled to JAXB bindings to get the STATUS messages for finding error/success status. In V1 the HTTP
	  * return status does not conver the success or a failure of the URL invocation. In the next V2 version of the
	  * API calls, this will change.

	  * This method is a static method and connects based on Community HostName, CommunityName, QuerParameters.
	  * Perform the HTTP login only. SSL based in not yet supported.
	  * @param url The nicely formed URL for loggin-in.
	  * @param MultivaluedMap The map for Jersey Client to populate Key/value parameters for GET/POST.
	  * @return String Response Session Key for caching and reuse.
	  * @throws LithiumConnectorException This is a generic mule based Lithium Connector Exception
	  * 
	  */
	public static String invokeGenericRestCall(String url, MultivaluedMap<String, String> queryParams)
			throws LithiumConnectorException {
		if (getRestApiSessionKey() != null)
			queryParams.add(RESTAPI_SESSION_KEY, getRestApiSessionKey());
		else
			return "399";
		log.debug(String.format("In Lithium Client. Login URL: %s Login Query ParametersL %s ", url, queryParams));
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.queryParams(queryParams).accept("application/xml")
				.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			log.debug(LithiumConnectorErrorCodes.CONNECTOR_REST_CLIENT_2002.getDescription() + response.getStatus());
			return String.valueOf(response.getStatus());
		}
		log.debug("In Jersey Client. Making Genric restapi Call.");
		String restResponse = response.getEntity(String.class);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(LithiumGenericResponse.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			LithiumGenericResponse lithiumGenericResponse = (LithiumGenericResponse) jaxbUnmarshaller
					.unmarshal(new ByteArrayInputStream(restResponse.getBytes()));
			if (lithiumGenericResponse.getStatus().equalsIgnoreCase(ERROR))
				return ERROR;
		} catch (JAXBException e) {
			log.error("Exception in UnMarshalling of Rest Reponse Data: " + e.getLocalizedMessage());
			e.printStackTrace();
			throw new LithiumConnectorException(LithiumConnectorErrorCodes.CONNECTOR_10001.getDescription(), e);
		}
		log.debug(restResponse);
		return restResponse;
	}

	public static void main(String args[]) {
		LithiumSessionRestClient.invokeToGetRestSessionKey(null, null);
	}
}

package com.lithium.integrations;

import static com.lithium.integrations.constants.QueryParameterConstants.LABEL_LABELS;
import static com.lithium.integrations.constants.QueryParameterConstants.MESSAGE_ADD;
import static com.lithium.integrations.constants.QueryParameterConstants.MESSAGE_BODY;
import static com.lithium.integrations.constants.QueryParameterConstants.MESSAGE_IS_DRAFT;
import static com.lithium.integrations.constants.QueryParameterConstants.MESSAGE_SUBJECT;
import static com.lithium.integrations.constants.QueryParameterConstants.MESSAGE_TEASER;
import static com.lithium.integrations.constants.QueryParameterConstants.RESTAPI_SESSION_KEY;
import static com.lithium.integrations.constants.QueryParameterConstants.LOGIN_USER_NAME_PARAM;
import static com.lithium.integrations.constants.QueryParameterConstants.*;
import static org.junit.Assert.*;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.core.util.MultivaluedMapImpl;

public class LithiumSessionRestClientTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		MultivaluedMap<String, String> queryParam = new MultivaluedMapImpl();
		queryParam.add(LOGIN_USER_NAME_PARAM, LOGIN_USER_NAME_VALUE);
		queryParam.add(LOGIN_PASSWORD_PARAM, LOGIN_PASSWORD_VALUE);
		assertNotNull(LithiumSessionRestClient.invokeToGetRestSessionKey(null, queryParam));
	}

	@Test
	public void testNonAdmin() {
		MultivaluedMap<String, String> queryParam = new MultivaluedMapImpl();
		queryParam.add(RESTAPI_SESSION_KEY, "Y1SA29-Refx_GD6OUj0ae2saJtWT1zdLX0PB5dKjeRg.");
		queryParam.add(MESSAGE_SUBJECT, "JUNIT Testing.");
		queryParam.add(MESSAGE_TEASER, "Msg Teaser");
		queryParam.add(MESSAGE_BODY, "Mule Integration Testing Happenning Here...");
		queryParam.add(MESSAGE_ADD, "Message Add");
		queryParam.add(LABEL_LABELS, "lab1, lab2");
		queryParam.add(MESSAGE_IS_DRAFT, "false");
		assertNotNull(LithiumSessionRestClient.invokeToGetRestSessionKey(
				"http://ldn.qa.lithium.com/restapi/vc/blogs/id/scienceofsocial/messages/post", queryParam));
	}
}

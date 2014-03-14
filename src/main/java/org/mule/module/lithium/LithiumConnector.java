/**
 * This file was automatically generated by the Mule Development Kit
 */
package org.mule.module.lithium;

import java.io.IOException;

import javax.ws.rs.core.MultivaluedMap;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.rest.RestCall;
import org.mule.api.annotations.rest.RestHeaderParam;
import org.mule.api.annotations.rest.RestQueryParam;
import org.mule.api.annotations.rest.RestUriParam;
import org.mule.api.annotations.rest.RestExceptionOn;
import org.mule.api.ConnectionException;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Processor;
import static com.lithium.integrations.constants.QueryParameterConstants.*;

import com.lithium.integrations.LithiumSessionRestClient;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Cloud Connector
 * 
 * @author MuleSoft, Inc.
 */
@Connector(name = "Lithium", schemaVersion = "1.0.0", friendlyName = "Lithium", minMuleVersion = "3.4")
public abstract class LithiumConnector {

	private final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();

	public MultivaluedMap<String, String> getQueryParams() {
		return queryParams;
	}

	/**
	 * Configurable
	 */
	@RestUriParam(value = "communityHostname")
	@Configurable
	@Optional
	@Default("ldn.qa.lithium.com")
	private String communityHostname;

	/**
	 * get property
	 * 
	 * @return String Community Host Name
	 *            The session key used for Lithium connectivity
	 */
	public String getCommunityHostname() {
		return communityHostname;
	}

	public void setCommunityHostname(String communityHostname) {
		this.communityHostname = communityHostname;
	}

	/**
	 * Configurable
	 */
	@RestUriParam(value = "communityName")
	@Configurable
	@Optional
	@Default("")
	private String communityName;

	/**
	 * get property
	 * 
	 * @return String Session Key
	 *            The session key used for Lithium connectivity
	 */
	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	/**
	 * Configurable
	 */
	@Configurable
	private String lithiumUserName;

	public String getLithiumUserName() {
		return lithiumUserName;
	}

	public void setLithiumUserName(String lithiumUserName) {
		this.lithiumUserName = lithiumUserName;
	}

	/**
	 * Configurable
	 */
	@Configurable
	private String lithiumPassword;

	public String getLithiumPassword() {
		return lithiumPassword;
	}

	public void setLithiumPassword(String lithiumPassword) {
		this.lithiumPassword = lithiumPassword;
	}

	/**
	 * Configurable
	 */
	@RestUriParam(value = "restApiSessionKey")
	@Configurable
	@Optional
	private String restApiSessionKey;

	/**
	 * get property
	 * 
	 * @return String Session Key
	 *            The session key used for Lithium connectivity
	 */
	public String getRestApiSessionKey() {
		return restApiSessionKey;
	}

	/**
	 * Set property
	 * 
	 * @param restApiSessionKey
	 *            The session key used for Lithium connectivity
	 */
	public void setRestApiSessionKey(String restApiSessionKey) {
		this.restApiSessionKey = restApiSessionKey;
	}

	/**
	 * Configurable
	 */
	public void populateSessionKey() {
		System.out.println("**Start of populateSessionKey with User/Password: " + getLithiumUserName() + "/"
				+ getLithiumPassword() + " SessionKey: " + getRestApiSessionKey() + " Hostname: "
				+ getCommunityHostname());
		MultivaluedMap<String, String> adminQueryParams = new MultivaluedMapImpl();
		adminQueryParams.add(LOGIN_USER_NAME_PARAM, getLithiumUserName());
		adminQueryParams.add(LOGIN_PASSWORD_PARAM, getLithiumPassword());
		// Null implies login.
		LithiumSessionRestClient.invokeToGetRestSessionKey(null, adminQueryParams);
		System.out.println("** End of populateSessionKey Key: "
				+ LithiumSessionRestClient.invokeToGetRestSessionKey(null, adminQueryParams));

	}

	/**
	 * Connect
	 * 
	 * @param username The username for Lithim Login
	 * @param password A password for Lithium Password
	 * @throws ConnectionException
	 */
	@Connect
	public void connect(@ConnectionKey String username, String password) throws ConnectionException {
		System.out.println("**In Connect. username/password: " + username + "/" + password);
	}

	/**
	 * Disconnect
	 */
	@Disconnect
	public void disconnect() {
		/*
		 * CODE FOR CLOSING A CONNECTION GOES IN HERE
		 */
	}

	/**
	 * Are we connected
	 */
	@ValidateConnection
	public boolean isConnected() {
		return true;
	}

	/**
	 * Are we connected
	 */
	@ConnectionIdentifier
	public String connectionId() {
		return "001";
	}

	/**
	 * Custom processor for getting the latest message on the Board
	 * <p/>
	 * {@sample.xml ../../../doc/Lithium-connector.xml.sample Lithium:get-blog-latest}
	 * 
	 * @param boardIdOrBlogName A blog name to get latest from..
	 * @return Some string
	 * @throws java.io.IOException
	 *             throws the exception
	 */
	@Processor
	@RestCall(uri = ("http://{communityHostName}/restapi/vc/blogs/id/{boardName}/messages/latest"), method = org.mule.api.annotations.rest.HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract String getBlogLatest(
			@RestUriParam(value = "boardName") @Optional @Default("scienceofsocial") String boardIdOrBlogName)
			throws IOException;

	/**
	 * Custom processor for posting a message in the blog
	 * <p/>
	 * {@sample.xml ../../../doc/Lithium-connector.xml.sample Lithium:post-blog-message}
	 * message.author passing is not needed. THe logged in user is enough. If any other author name is kept. It thows an exception.
	 * @param boardIdOrBlogName The name of the boardID that need to be used in Rest Call 	
	 * @param messageSubject Subject of the blog message 	
	 * @param messageTeaser Message Teasor for display 	
	 * @param messageBody Message Body to display 	
	 * @param tagAdd Tag addition 	
	 * @param labels Labels that can be added to the Blog	
	 * @param messageIsDraft Checking whether message is a blog? true/false?	
	 * @return Response string from the WebService Call
	 * @throws java.io.IOException throws the exception
	 */
	@Processor
	public String postBlogMessage(@Optional @Default("scienceofsocial") String boardIdOrBlogName,
			@Optional @Default("Test From Mule connector calling LDN.") String messageSubject,
			@Optional @Default("Welcome to Lithium Integrations") String messageTeaser,
			@Optional @Default("Mule Lithium best practices.") String messageBody,
			@Optional @Default("Stocks,News,Events") String tagAdd,
			@Optional @Default("FIRST,TEST,GOAL") String labels, @Optional @Default("false") String messageIsDraft)
			throws IOException {

		if (LithiumSessionRestClient.getRestApiSessionKey() == null) {
			System.out.println("-- Session key is NULL. Trying to do admin call.");
			populateSessionKey();
		}
		MultivaluedMap<String, String> queryParam = new MultivaluedMapImpl();
		String url = "http://" + getCommunityHostname() + "/"
				+ (getCommunityName() == null ? "" : (getCommunityName() + "/")) + "restapi/vc/blogs/id/"
				+ boardIdOrBlogName + "/messages/post";
		queryParam.add(RESTAPI_SESSION_KEY, LithiumSessionRestClient.getRestApiSessionKey());
		queryParam.add(MESSAGE_SUBJECT, messageSubject);
		queryParam.add(MESSAGE_TEASER, messageTeaser);
		queryParam.add(MESSAGE_BODY, messageBody);
		queryParam.add(MESSAGE_ADD, tagAdd);
		queryParam.add(LABEL_LABELS, labels);
		queryParam.add(MESSAGE_IS_DRAFT, messageIsDraft);

		String reponseData = LithiumSessionRestClient.invokeToGetRestSessionKey(url, queryParam);
		if (reponseData.startsWith("3")) {
			// retry with new session key;
			System.out.println("--Invalid Session Key. Hence retry. ");
			populateSessionKey();
			reponseData = LithiumSessionRestClient.invokeToGetRestSessionKey(url, queryParam);
		}
		return reponseData;
	}

	/**
	 * Custom processor for Kudos Givers leaderboard
	 * <p/>
	 * {@sample.xml ../../../doc/Lithium-connector.xml.sample Lithium:get-kudos-leaderboard}
	 * message.author passing is not needed. THe logged in user is enough. If any other author name is kept. It thows an exception.
	 * @param boardIdOrBlogName The name of the boardID that need to be used in Rest Call 	
	 * @param maxAge Max age of the post for pull up	
	 * @param pageSize Max number of pages 	
	 * @return Response string from the WebService Call
	 * @throws java.io.IOException throws the exception
	 */
	@Processor
	public String getKudosLeaderboard(@Optional @Default("scienceofsocial") String boardIdOrBlogName,
			@Optional @Default("all") String maxAge, @Optional @Default("100") String pageSize) throws IOException {

		if (LithiumSessionRestClient.getRestApiSessionKey() == null) {
			System.out.println("-- Session key is NULL. Trying to do admin call.");
			populateSessionKey();
		}
		MultivaluedMap<String, String> queryParam = new MultivaluedMapImpl();
		String url = "http://" + getCommunityHostname() + "/"
				+ (getCommunityName() == null ? "" : (getCommunityName() + "/")) + "restapi/vc/blogs/id/"
				+ boardIdOrBlogName + "/kudos/givers/leaderboard";
		queryParam.add(RESTAPI_SESSION_KEY, LithiumSessionRestClient.getRestApiSessionKey());
		queryParam.add(MAX_AGE, maxAge);
		queryParam.add(PAGE_SIZE, pageSize);

		String reponseData = LithiumSessionRestClient.invokeToGetRestSessionKey(url, queryParam);
		if (reponseData.startsWith("3")) {
			// retry with new session key;
			System.out.println("--Invalid Session Key. Hence retry. ");
			populateSessionKey();
			reponseData = LithiumSessionRestClient.invokeToGetRestSessionKey(url, queryParam);
		}
		return reponseData;
	}

	/**
	 * Custom processor for Get Recent Topics in a Board
	 * <p/>
	 * {@sample.xml ../../../doc/Lithium-connector.xml.sample Lithium:get-recent-topics}
	 * message.author passing is not needed. THe logged in user is enough. If any other author name is kept. It thows an exception.
	 * @param boardIdOrBlogName The name of the boardID that need to be used in Rest Call 	
	 * @param moderationScope Moderation Scope for the user	
	 * @param visibilityScope Visibility scope of the user
	 * @return Response string from the WebService Call
	 * @throws java.io.IOException throws the exception
	 */
	@Processor
	public String getRecentTopics(@Optional @Default("scienceofsocial") String boardIdOrBlogName,
			@Optional @Default("approved") String moderationScope, @Optional @Default("public") String visibilityScope)
			throws IOException {

		if (LithiumSessionRestClient.getRestApiSessionKey() == null) {
			System.out.println("-- Session key is NULL. Trying to do admin call.");
			populateSessionKey();
		}
		MultivaluedMap<String, String> queryParam = new MultivaluedMapImpl();
		String url = "http://" + getCommunityHostname() + "/"
				+ (getCommunityName() == null ? "" : (getCommunityName() + "/")) + "restapi/vc/blogs/id/"
				+ boardIdOrBlogName + "/topics/recent";
		queryParam.add(RESTAPI_SESSION_KEY, LithiumSessionRestClient.getRestApiSessionKey());
		queryParam.add(MODERATION_SCOPE, moderationScope);
		queryParam.add(VISIBILITY_SCOPE, visibilityScope);
		queryParam.add(RESPONSE_FORMAT_PARAM, RESPONSE_FORMAT_VALUE);

		String reponseData = LithiumSessionRestClient.invokeToGetRestSessionKey(url, queryParam);
		if (reponseData.startsWith("3")) {
			// retry with new session key;
			System.out.println("--Invalid Session Key. Hence retry. ");
			populateSessionKey();
			reponseData = LithiumSessionRestClient.invokeToGetRestSessionKey(url, queryParam);
		}
		return reponseData;
	}

	// TODO: Try sending the object type to marshall response.
	/**
	 * Custom processor for getting the latest message on the Board
	 * <p/>
	 * {@sample.xml ../../../doc/Lithium-connector.xml.sample Lithium:get-topic-message}
	 * 
	 * @param boardIdOrBlogName A parameter to pass for board or blog name id.
	 * @param messageId A parameter to pass message id.
	 * @return Some string
	 * @throws java.io.IOException
	 *             throws the exception
	 */
	@Processor
	public String getTopicMessage(@Optional @Default("scienceofsocial") String boardIdOrBlogName,
			@Optional @Default("1267") String messageId) throws IOException {

		if (LithiumSessionRestClient.getRestApiSessionKey() == null) {
			System.out.println("-- Session key is NULL. Trying to do admin call.");
			populateSessionKey();
		}
		MultivaluedMap<String, String> queryParam = new MultivaluedMapImpl();
		String url = "http://" + getCommunityHostname() + "/"
				+ (getCommunityName() == null ? "" : (getCommunityName() + "/")) + "restapi/vc/blogs/id/"
				+ boardIdOrBlogName + "/messages/id/" + messageId;
		queryParam.add(RESTAPI_SESSION_KEY, LithiumSessionRestClient.getRestApiSessionKey());
		queryParam.add(RESPONSE_FORMAT_PARAM, RESPONSE_FORMAT_VALUE);

		String reponseData = LithiumSessionRestClient.invokeToGetRestSessionKey(url, queryParam);
		if (reponseData.startsWith("3")) {
			// retry with new session key;
			System.out.println("--Invalid Session Key. Hence retry. ");
			populateSessionKey();
			reponseData = LithiumSessionRestClient.invokeToGetRestSessionKey(url, queryParam);
		}
		return reponseData;
	}

	// TODO: Try sending the object type to marshall response.
	/**
	 * Custom processor for getting the latest message on the Board
	 * <p/>
	 * {@sample.xml ../../../doc/Lithium-connector.xml.sample Lithium:get-author}
	 * 
	 * @param userId A parameter to pass message id.
	 * @return Some string
	 * @throws java.io.IOException
	 *             throws the exception
	 */
	@Processor
	public String getAuthor(@Optional @Default("1") String userId) throws IOException {
		if (LithiumSessionRestClient.getRestApiSessionKey() == null) {
			System.out.println("-- Session key is NULL. Trying to do admin call.");
			populateSessionKey();
		}
		MultivaluedMap<String, String> queryParam = new MultivaluedMapImpl();
		String url = "http://" + getCommunityHostname() + "/"
				+ (getCommunityName() == null ? "" : (getCommunityName() + "/")) + "restapi/vc/users/id/" + userId;
		queryParam.add(RESTAPI_SESSION_KEY, LithiumSessionRestClient.getRestApiSessionKey());
		//queryParam.add(RESPONSE_FORMAT_PARAM, RESPONSE_FORMAT_VALUE);
		String reponseData = LithiumSessionRestClient.invokeToGetRestSessionKey(url, queryParam);
		if (reponseData.startsWith("3")) {
			// retry with new session key;
			System.out.println("--Invalid Session Key. Hence retry. ");
			populateSessionKey();
			reponseData = LithiumSessionRestClient.invokeToGetRestSessionKey(url, queryParam);
		}
		return reponseData;
	}

	// TODO: Try sending the object type to marshall response.
	/**
	 * Custom processor for getting the latest message on the Board
	 * <p/>
	 * {@sample.xml ../../../doc/Lithium-connector.xml.sample Lithium:get-author-avatar}
	 * 
	 * @param userId A parameter to pass message id.
	 * @return Some string
	 * @throws java.io.IOException
	 *             throws the exception
	 */
	@Processor
	public String getAuthorAvatar(@Optional @Default("1") String userId) throws IOException {

		if (LithiumSessionRestClient.getRestApiSessionKey() == null) {
			System.out.println("-- Session key is NULL. Trying to do admin call.");
			populateSessionKey();
		}
		MultivaluedMap<String, String> queryParam = new MultivaluedMapImpl();
		String url = "http://" + getCommunityHostname() + "/"
				+ (getCommunityName() == null ? "" : (getCommunityName() + "/")) + "restapi/vc/users/id/" + userId
				+ "/profiles/avatar";
		queryParam.add(RESTAPI_SESSION_KEY, LithiumSessionRestClient.getRestApiSessionKey());
		//queryParam.add(RESPONSE_FORMAT_PARAM, RESPONSE_FORMAT_VALUE);
		String reponseData = LithiumSessionRestClient.invokeToGetRestSessionKey(url, queryParam);
		if (reponseData.startsWith("3")) {
			// retry with new session key;
			System.out.println("--Invalid Session Key. Hence retry. ");
			populateSessionKey();
			reponseData = LithiumSessionRestClient.invokeToGetRestSessionKey(url, queryParam);
		}
		return reponseData;
	}
}

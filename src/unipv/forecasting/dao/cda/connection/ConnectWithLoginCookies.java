package unipv.forecasting.dao.cda.connection;

import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.dao.URLBuilder;
import unipv.forecasting.dao.cda.connection.connector.HttpConnector;
import unipv.forecasting.dao.cda.connection.connector.HttpConnectorWithHeaders;
import unipv.forecasting.dao.cda.connection.connector.SimpleHttpConnector;
import unipv.forecasting.dao.cda.url.CDAUrlBuilder;

public class ConnectWithLoginCookies implements ConnectStrategy {

	/**
	 * the URL of CDA file
	 */
	private String url;
	/**
	 * the parameters which you want to specific
	 */
	private HashMap<String, String> parameters;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            the id used to access a CDA file.
	 * @param parameters
	 *            parameters which will be delivered to CDA file.
	 */
	public ConnectWithLoginCookies(final String id,
			final HashMap<String, String> parameters) {
		// create a URL accessing to a CDA file by access id.
		URLBuilder urlBuilder = new CDAUrlBuilder();
		this.url = urlBuilder.getURL(id);
		this.parameters = parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forcasting.dao.cda.connection.ConnectStrategy#getJSONString()
	 */
	public final String getJSONString() {
		// Initiate HTTP client.
		HttpClient client = new DefaultHttpClient();
		// Header used to store cookies.
		Header cookies = null;
		// HTTP body.
		String body = "";
		// Initiate login information.
		HashMap<String, String> loginInfo = new HashMap<String, String>();
		loginInfo.put(CONFIGURATION.LOGIN_REPT_USERNAME,
				CONFIGURATION.LOGIN_USERNAME);
		loginInfo.put(CONFIGURATION.LOGIN_REPT_PASSWORD,
				CONFIGURATION.LOGIN_PASSWORD);
		// construct login connector, used to retrieve login cookies.
		HttpConnector login = new SimpleHttpConnector(CONFIGURATION.LOGIN_URL,
				loginInfo, CONFIGURATION.REPT_POST, client);
		// decide which login construction was success.
		if (login.getInitiateState().equals(CONFIGURATION.REPT_SUCCESS)) {
			// execute connection and decide whether it was success.
			if (login.execute().equals(CONFIGURATION.REPT_SUCCESS)) {
				// store cookies data.
				cookies = login.getFirstHeader(CONFIGURATION.COOKIE_NAME);
			} else {
				return null;
			}
		} else {
			return null;
		}
		// decide whether we really have cookies.
		if (cookies == null) {
			return null;
		}
		// if we failed in closing resources we create
		// , there is a chance of memory leak.
		if (login.closeAll().equals(CONFIGURATION.REPT_FAIL)) {
			// TODO memory leak.
		}
		// construct query connector with cookies, used to retrieve JSON data.
		HttpConnector query = new HttpConnectorWithHeaders(
				new SimpleHttpConnector(url, parameters,
						CONFIGURATION.REPT_GET, client), cookies);
		if (query.getInitiateState().equals(CONFIGURATION.REPT_SUCCESS)) {
			if (query.execute().equals(CONFIGURATION.REPT_SUCCESS)) {
				// retrieve the body of query.
				body = query.getBody();
			} else {
				return null;
			}
		} else {
			return null;
		}
		// close the client, release resources
		client.getConnectionManager().shutdown();
		return body;
	}
}

/**
 * A simple implementation of HttpConnector class.
 */
package unipv.forecasting.dao.cda.connection.connector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import unipv.forecasting.CONFIGURATION;

/**
 * @author Quest
 * 
 */
public class SimpleHttpConnector implements HttpConnector {

	/**
	 * HttpClient used to connect a URL.
	 */
	private HttpClient httpClient;
	/**
	 * HttpRequest used to request a URL.
	 */
	private HttpRequestBase httpRequest;
	/**
	 * A state used to represent the result of initiation.
	 */
	private String initiateState;
	/**
	 * HttpResponse used to get response from a URL.
	 */
	private HttpResponse response;
	/**
	 * An HttpEntity used to retrieve data from HttpResponse.
	 */
	private HttpEntity entity;

	/**
	 * Constructor of HttpConnector Class.
	 * 
	 * @param url
	 *            the URL which you want to connect.
	 * @param parameters
	 *            parameters which you want to pass to the URL.
	 * @param method
	 *            request method, can be 'POST' or 'GET'
	 * @param httpClient
	 *            the client used to connect a URL.
	 */
	@SuppressWarnings("deprecation")
	public SimpleHttpConnector(final String url,
			final HashMap<String, String> parameters, final String method,
			HttpClient httpClient) {
		this.httpClient = httpClient;
		// decide the connection method.
		if (method.equals(CONFIGURATION.REPT_POST)) {
			httpRequest = new HttpPost(url);
			// decide whether there are parameters, if not jump to next step.
			if ((parameters != null) && (!parameters.isEmpty())) {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				// add parameters to post method.
				for (String key : parameters.keySet()) {
					nvps.add(new BasicNameValuePair(key, parameters.get(key)));
				}
				try {
					((HttpPost) httpRequest)
							.setEntity(new UrlEncodedFormEntity(nvps));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.initiateState = CONFIGURATION.REPT_SUCCESS;
		} else if (method.equals(CONFIGURATION.REPT_GET)) {
			String urlGet = url;
			// add parameters to URL.
			for (String key : parameters.keySet()) {
				urlGet += "&param" + key + "="
						+ java.net.URLEncoder.encode(parameters.get(key));
			}
			// TODO test
			// System.out.println(urlGet);

			httpRequest = new HttpGet(urlGet);
			this.initiateState = CONFIGURATION.REPT_SUCCESS;
		} else {
			this.initiateState = CONFIGURATION.REPT_FAIL;
		}
	}

	/**
	 * Used to retrieve the state of initiation.
	 * 
	 * @return Representative String: 'Success' or 'Fail'
	 */
	public final String getInitiateState() {
		return initiateState;
	}

	/**
	 * Used to execute HTTP request.
	 * 
	 * @return Representative String: 'Success' or 'Fail'
	 */
	public final String execute() {
		try {
			this.response = httpClient.execute(httpRequest);
			return CONFIGURATION.REPT_SUCCESS;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CONFIGURATION.REPT_FAIL;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CONFIGURATION.REPT_FAIL;
		}
	}

	/**
	 * used to retrieve the state of HTTP request.
	 * 
	 * @return Representative String: 'Success' or 'Fail'
	 */
	public final String getState() {
		if (response.getStatusLine().getStatusCode() == CONFIGURATION.CODE_REQUEST_SUCCESS
				|| response.getStatusLine().getStatusCode() == CONFIGURATION.CODE_REQUEST_REDIRECTED
				|| response.getStatusLine().getStatusCode() == CONFIGURATION.CODE_REQUEST_REDIRECTED) {
			return CONFIGURATION.REPT_SUCCESS;
		} else {
			return CONFIGURATION.REPT_FAIL;
		}
	}

	/**
	 * Used to retrieve all headers from HTTP response.
	 * 
	 * @return A set of headers.
	 */
	public final Header[] getAllHeaders() {
		return response.getAllHeaders();
	}

	/**
	 * Used to retrieve a set of headers with same key.
	 * 
	 * @param key
	 *            the key of the header.
	 * @return a set of headers.
	 */
	public final Header[] getHeaders(final String key) {
		return response.getHeaders(key);
	}

	/**
	 * Used to retrieve the first header from HTTP response by key.
	 * 
	 * @param key
	 *            the key of the header.
	 * @return the header with specific key.
	 */
	public final Header getFirstHeader(final String key) {
		return response.getFirstHeader(key);
	}

	/**
	 * Used to retrieve the last header from HTTP response by key.
	 * 
	 * @param key
	 *            the key of the header.
	 * @return the header with specific key.
	 */
	public final Header getLastHeader(final String key) {
		return response.getLastHeader(key);
	}

	/**
	 * Used to retrieve the body of the HTTP response.
	 * 
	 * @return the body of the HTTP response in String format, if fail, return
	 *         'Fail'
	 */
	public final String getBody() {
		this.entity = response.getEntity();
		try {
			return EntityUtils.toString(entity);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CONFIGURATION.REPT_FAIL;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CONFIGURATION.REPT_FAIL;
		}
	}

	/**
	 * Used to release resources used by this connection.
	 * 
	 * @return String represent the result: 'Success' or 'Fail'
	 */
	public final String closeAll() {
		try {
			EntityUtils.consume(this.entity);
			httpRequest.releaseConnection();
			return CONFIGURATION.REPT_SUCCESS;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CONFIGURATION.REPT_FAIL;
		}
	}

	/**
	 * @return the httpRequest
	 */
	public final HttpRequestBase getHttpRequest() {
		return httpRequest;
	}

	/**
	 * @return the response
	 */
	public final HttpResponse getResponse() {
		return response;
	}

	/**
	 * @return the httpClient
	 */
	public final HttpClient getHttpClient() {
		return httpClient;
	}
}

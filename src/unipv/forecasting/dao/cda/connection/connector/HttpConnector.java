/**
 * Interface of HTTP connectors.
 */
package unipv.forecasting.dao.cda.connection.connector;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author Quest
 * 
 */
public interface HttpConnector {
	/**
	 * Used to retrieve the state of initiation.
	 * 
	 * @return Representative String: 'Success' or 'Fail'
	 */
	String getInitiateState();

	/**
	 * Used to get the HTTP request.
	 * 
	 * @return HttpRequestBase
	 */
	HttpRequestBase getHttpRequest();

	/**
	 * Used to execute HTTP request.
	 * 
	 * @return Representative String: 'Success' or 'Fail'
	 */
	String execute();

	/**
	 * used to retrieve the state of HTTP request.
	 * 
	 * @return Representative String: 'Success' or 'Fail'
	 */
	String getState();

	/**
	 * Used to retrieve all headers from HTTP response.
	 * 
	 * @return A set of headers.
	 */
	Header[] getAllHeaders();

	/**
	 * Used to retrieve a set of headers with same key.
	 * 
	 * @param key
	 *            the key of the header.
	 * @return a set of headers.
	 */
	Header[] getHeaders(final String key);

	/**
	 * Used to retrieve the first header from HTTP response by key.
	 * 
	 * @param key
	 *            the key of the header.
	 * @return the header with specific key.
	 */
	Header getFirstHeader(final String key);

	/**
	 * Used to retrieve the last header from HTTP response by key.
	 * 
	 * @param key
	 *            the key of the header.
	 * @return the header with specific key.
	 */
	Header getLastHeader(final String key);

	/**
	 * Used to retrieve the body of the HTTP response.
	 * 
	 * @return the body of the HTTP response in String format, if fail, return
	 *         'Fail'
	 */
	String getBody();

	/**
	 * Used to release resources used by this connection.
	 * 
	 * @return String represent the result: 'Success' or 'Fail'
	 */
	String closeAll();
}

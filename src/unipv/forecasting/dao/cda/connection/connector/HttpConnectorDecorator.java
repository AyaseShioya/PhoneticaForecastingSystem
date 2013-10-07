/**
 * The abstract class for decorators.
 */
package unipv.forecasting.dao.cda.connection.connector;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author Quest
 * 
 */
public class HttpConnectorDecorator implements HttpConnector {

	/**
	 * A httpConnector which will be decorated.
	 */
	protected HttpConnector decoratedHttpConnector;

	/**
	 * A httpConnector which will be decorated.
	 */
	public HttpConnectorDecorator(HttpConnector decoratedHttpConnector) {
		this.decoratedHttpConnector = decoratedHttpConnector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnector#getInitiateState
	 * ()
	 */
	@Override
	public String getInitiateState() {
		return decoratedHttpConnector.getInitiateState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnector#execute()
	 */
	@Override
	public String execute() {
		return decoratedHttpConnector.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnector#getState()
	 */
	@Override
	public String getState() {
		return decoratedHttpConnector.getState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnector#getAllHeaders
	 * ()
	 */
	@Override
	public Header[] getAllHeaders() {
		return decoratedHttpConnector.getAllHeaders();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnector#getHeaders
	 * (java.lang.String)
	 */
	@Override
	public Header[] getHeaders(String key) {
		return decoratedHttpConnector.getHeaders(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnector#getFirstHeader
	 * (java.lang.String)
	 */
	@Override
	public Header getFirstHeader(String key) {
		return decoratedHttpConnector.getFirstHeader(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnector#getLastHeader
	 * (java.lang.String)
	 */
	@Override
	public Header getLastHeader(String key) {
		return decoratedHttpConnector.getLastHeader(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnector#getBody()
	 */
	@Override
	public String getBody() {
		return decoratedHttpConnector.getBody();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnector#closeAll()
	 */
	@Override
	public String closeAll() {
		return decoratedHttpConnector.closeAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnector#getHttpRequest
	 * ()
	 */
	@Override
	public HttpRequestBase getHttpRequest() {
		// TODO Auto-generated method stub
		return decoratedHttpConnector.getHttpRequest();
	}
}

/**
 * Http connector with customized headers.
 */
package unipv.forecasting.dao.cda.connection.connector;

import org.apache.http.Header;

/**
 * @author Quest
 * 
 */
public class HttpConnectorWithHeaders extends HttpConnectorDecorator {
	/**
	 * @param decoratedHttpConnector
	 *            simple HTTP connector to be decorated.
	 * @param header
	 *            the customized header.
	 */
	public HttpConnectorWithHeaders(final HttpConnector decoratedHttpConnector,
			final Header header) {
		super(decoratedHttpConnector);
		// decorate HTTP connector with customized header.
		decoratedHttpConnector.getHttpRequest().setHeader(header);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnectorDecorator#
	 * getInitiateState()
	 */
	@Override
	public String getInitiateState() {
		return super.getInitiateState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnectorDecorator#
	 * execute()
	 */
	@Override
	public String execute() {
		return super.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnectorDecorator#
	 * getState()
	 */
	@Override
	public String getState() {
		return super.getState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnectorDecorator#
	 * getAllHeaders()
	 */
	@Override
	public Header[] getAllHeaders() {
		return super.getAllHeaders();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnectorDecorator#
	 * getHeaders(java.lang.String)
	 */
	@Override
	public Header[] getHeaders(String key) {
		return super.getHeaders(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnectorDecorator#
	 * getFirstHeader(java.lang.String)
	 */
	@Override
	public Header getFirstHeader(String key) {
		return super.getFirstHeader(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnectorDecorator#
	 * getLastHeader(java.lang.String)
	 */
	@Override
	public Header getLastHeader(String key) {
		return super.getLastHeader(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnectorDecorator#
	 * getBody()
	 */
	@Override
	public String getBody() {
		return super.getBody();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.cda.connection.connector.HttpConnectorDecorator#
	 * closeAll()
	 */
	@Override
	public String closeAll() {
		return super.closeAll();
	}

}

/**
 * The interface of URL builder used to build the URL to access database or CDA.
 */
package unipv.forecasting.dao;

/**
 * @author Quest
 * 
 */
public interface URLBuilder {

	/**
	 * . Used to generate a URL to access database or CDA
	 * 
	 * @param id
	 *            the access id of CDA file or database name
	 * @return URL in String format.
	 */
	String getURL(String id);
}

/**
 * The interface of different strategy to connect CDA using
 * different connectors.
 */
package unipv.forecasting.dao.cda.connection;

/**
 * @author Quest
 * 
 */
public interface ConnectStrategy {

	/**
	 * Used to retrieve JSON data in String format.
	 * 
	 * @return String format JSON data. If it is null, this method running
	 *         failed.
	 */
	String getJSONString();
}

/**
 * Factory engaging in creating different DAOs.
 */
package unipv.forecasting.dao;

import unipv.forecasting.CONFIGURATION.DAO_APPROACH;
import unipv.forecasting.dao.cda.CDAClient;
import unipv.forecasting.dao.database.DatabaseClient;

/**
 * @author Quest
 */
public class DAOSelector {
	/**
	 * Factory method.
	 * 
	 * @param accessApproach
	 *            the type of approach you want to access data.
	 * @return DataAccessInterface with specific method.
	 */
	public final DataAccessInterface getDAO(final DAO_APPROACH accessApproach) {
		switch (accessApproach) {
		case CDA:
			return new CDAClient();
		case DATABASE:
			return new DatabaseClient();
		default:
			return null;
		}
	}
}

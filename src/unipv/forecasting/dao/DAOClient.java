/**
 * Facade of dao package, used to hide implementation details.
 */
package unipv.forecasting.dao;

import java.util.HashMap;

import unipv.forecasting.CONFIGURATION.DAO_APPROACH;
import unipv.forecasting.CONFIGURATION.TRANSLATOR;
import unipv.forecasting.dao.instances.InstancesTranslator;
import unipv.forecasting.dao.instances.TranslatorSelector;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class DAOClient {
	/**
	 * DAO object used to query data.
	 */
	private DataAccessInterface dataPort;
	/**
	 * translator used to translate retrieved data into instances.
	 */
	private InstancesTranslator translator;

	/**
	 * Constructor
	 * 
	 * @param accessApproach
	 *            the type of approach to query data. can be:'CDA' or 'Database'
	 * @param translatorType
	 *            the type of translator. can be: 'Daily', 'Yearly' or 'Hourly'
	 */
	public DAOClient(final DAO_APPROACH accessApproach,
			final TRANSLATOR translator) {
		TranslatorSelector translatorSelector = new TranslatorSelector();
		DAOSelector daoSelector = new DAOSelector();
		this.dataPort = daoSelector.getDAO(accessApproach);
		this.translator = translatorSelector.getTranslator(translator);
	}

	/**
	 * query data on required conditions.
	 * 
	 * @param address
	 *            The address of the data sources, it is the database name to
	 *            access the database or the fileName of CDA file.
	 * @param parameters
	 *            Parameters that can customize the query on data sources.
	 * @return Instance of Class Instances that can be used in WEKA.
	 */
	public final Instances getInstances(final String address,
			final HashMap<String, String> parameters) {
		return dataPort.getInstances(address, parameters, translator);
	}

}

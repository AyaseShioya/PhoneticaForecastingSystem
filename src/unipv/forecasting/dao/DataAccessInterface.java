/**
 * Interface for different ways to access data.
 */
package unipv.forecasting.dao;

import java.util.HashMap;

import unipv.forecasting.dao.instances.InstancesTranslator;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public interface DataAccessInterface {

	/**
	 * @param address
	 *            The address of the data sources, it is the database name to
	 *            access the database or the fileName of CDA file.
	 * @param parameters
	 *            Parameters that can customize the query on data sources.
	 * @param instancesTranslator
	 *            Translator for creating instances following a specific method.
	 * @return Instance of Class Instances that can be used in WEKA.
	 */
	Instances getInstances(String address, HashMap<String, String> parameters,
			InstancesTranslator instancesTranslator);
}

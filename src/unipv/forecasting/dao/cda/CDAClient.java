/**
 * Facade of CDA package, used to hide implementation details.
 */
package unipv.forecasting.dao.cda;

import java.util.HashMap;

import unipv.forecasting.dao.DataAccessInterface;
import unipv.forecasting.dao.cda.connection.ConnectStrategy;
import unipv.forecasting.dao.cda.connection.ConnectWithLoginCookies;
import unipv.forecasting.dao.instances.InstancesTranslator;
import weka.core.Instances;

/**
 * @author Quest
 */
public class CDAClient implements DataAccessInterface {

	/**
	 * @param address
	 *            The address of the data sources, it is the fileName of CDA
	 *            file.
	 * @param parameters
	 *            Parameters that can customize the query on data sources.
	 * @param instancesTranslator
	 *            Translator for creating instances following a specific method.
	 * @return Instance of Class Instances that can be used in WEKA.
	 */
	public final Instances getInstances(final String address,
			final HashMap<String, String> parameters,
			final InstancesTranslator instancesTranslator) {
		// TODO Auto-generated method stub
		ConnectStrategy connection = new ConnectWithLoginCookies(address,
				parameters);
		String json = connection.getJSONString();
		// System.out.println(json);
		if (json.isEmpty()) {
			return null;
		}
		JSONAnalyzer analyzer = new JSONAnalyzer(json, instancesTranslator);
		return analyzer.translateToInstances();
	}
}

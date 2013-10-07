/**
 * Interface of how to translate meta data and result
 * sets to instances in a specific method.
 */
package unipv.forecasting.dao.instances;

import net.sf.json.JSONArray;
import weka.core.Instances;

/**
 * @author Quest
 */
public interface InstancesTranslator {
	/**
	 * translate meta data and result sets to instances in a specific method.
	 * 
	 * @param metaData
	 *            meta data of a CDA query result.
	 * @param resultSets
	 *            result set of a CDA query result.
	 * @param numberOfRows
	 *            number of rows in a CDA query result.
	 * @return instances created according to the translate method.
	 */
	Instances translate(final JSONArray metaData, final JSONArray resultSets,
			final int numberOfRows);
}

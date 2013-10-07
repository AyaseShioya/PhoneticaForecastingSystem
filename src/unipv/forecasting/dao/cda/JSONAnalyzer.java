/**
 * Class used for analysis JSON and create data container for it.
 */
package unipv.forecasting.dao.cda;

import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.dao.instances.InstancesTranslator;
import weka.core.Instances;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

/**
 * @author Quest
 */
public class JSONAnalyzer {

	/**
	 * query information
	 */
	private JSONObject queryInfos;
	/**
	 * meta data of results.
	 */
	private JSONArray metaData;
	/**
	 * set of results.
	 */
	private JSONArray resultSets;
	/**
	 * Translator for creating instances following a specific method.
	 */
	private InstancesTranslator instancesTranslator;

	/**
	 * . Constructor
	 * 
	 * @param jsonString
	 *            JSON data in String format.
	 * @param instancesTranslator
	 *            Translator for creating instances following a specific method.
	 */
	public JSONAnalyzer(final String jsonString,
			final InstancesTranslator instancesTranslator) {
		JSONObject rawData = JSONObject.fromObject(jsonString);
		// get query information
		this.queryInfos = rawData
				.getJSONObject(CONFIGURATION.CDA_JSON_REPT_QUERYINFO);
		// get meta data
		this.metaData = rawData
				.getJSONArray(CONFIGURATION.CDA_JSON_REPT_METADATA);
		// get result set
		this.resultSets = rawData
				.getJSONArray(CONFIGURATION.CDA_JSON_REPT_RESULTSET);
		this.instancesTranslator = instancesTranslator;
	}

	/**
	 * @return instances can be used in WEKA.
	 */
	public final Instances translateToInstances() {
		// translate result in translator.
		if (getNumberOfRows() == 0)
			return null;
		return instancesTranslator.translate(metaData, resultSets,
				getNumberOfRows());
	}

	/**
	 * @return number of rows.
	 */
	public final int getNumberOfRows() {
		return queryInfos.getInt(CONFIGURATION.CDA_JSON_REPT_RAW_COUNT);
	}

	/**
	 * @return number of columns.
	 */
	public final int getNumberOfColumns() {
		return metaData.size();
	}
}

/**
 * The class used to translate yearly data to instances.
 */
package unipv.forecasting.dao.instances;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import unipv.forecasting.CONFIGURATION;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

/**
 * @author Quest
 */
public class YearlyInstancesTranslator implements InstancesTranslator {

	/**
	 * translate yearly data to instances.
	 * 
	 * @param metaData
	 *            meta data of a CDA query result.
	 * @param resultSets
	 *            result set of a CDA query result.
	 * @param numberOfRows
	 *            number of rows in a CDA query result.
	 * @return instances created according to the translate method.
	 */
	public final Instances translate(final JSONArray metaData,
			final JSONArray resultSets, final int numberOfRows) {
		// initiation
		Instances data = null;
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		// translate JSON meta data to instance meta data.

		// first column is the date we need.
		JSONObject metaDate = metaData.getJSONObject(0);
		atts.add(new Attribute(metaDate
				.getString(CONFIGURATION.CDA_JSON_REPT_COLUMN_NAME)));
		// atts.add(new Attribute(metaDate
		// .getString(CONFIGURATION.CDA_JSON_REPT_COLUMN_NAME),
		// "yyyy"));

		// second column is number of calls, it is numeric.
		metaDate = metaData.getJSONObject(1);
		atts.add(new Attribute(metaDate
				.getString(CONFIGURATION.CDA_JSON_REPT_COLUMN_NAME)));

		// we have retrieved meta data, then we can create an instance.
		data = new Instances("YearlyData", atts, 0);

		// translate JSON result sets to instance data.
		double[] instanceValue = new double[data.numAttributes()];

		for (int i = 0; i < numberOfRows; i++) {
			JSONArray resultSet = resultSets.getJSONArray(i);
			// add first column date.
			instanceValue[0] = i + 1;
			// try {
			// instanceValue[0] = data.attribute(0).parseDate(
			// resultSet.getString(0));
			// } catch (ParseException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// // if there are parse errors, set date to 1970-01-01, then we
			// // can drop them.
			// instanceValue[0] = 0;
			// }
			// add second column date
			if (resultSet.getString(1).equals(CONFIGURATION.REPT_NULL)) {
				// if the column is null, set it to 0.
				instanceValue[1] = 0;
			} else {
				instanceValue[1] = resultSet.getDouble(1);
			}
			data.add(new DenseInstance(CONFIGURATION.WEKA_DEFAULTWEIGHT,
					instanceValue));
			// refresh instanceValue
			instanceValue = new double[data.numAttributes()];
		}
		return data;
	}
}

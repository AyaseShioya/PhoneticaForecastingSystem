/**
 * The class used to translate hourly data to instances.
 */
package unipv.forecasting.dao.instances;

import java.text.ParseException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import unipv.forecasting.CONFIGURATION;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

/**
 * @author Quest
 */
public class HourlyAVGInstancesTranslator implements InstancesTranslator {

	/**
	 * translate hourly data to instances.
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
		// JSONObject metaDate = metaData.getJSONObject(0);
		atts.add(new Attribute(CONFIGURATION.REPT_DATE,
				CONFIGURATION.DATE_FORMAT_HOURLY));
		// atts.add(new Attribute(metaDate
		// .getString(CONFIGURATION.CDA_JSON_REPT_COLUMN_NAME),
		// "yyyy-MM-dd HH"));

		// second column is the target, for instance, number of calls, it is
		// numeric.
		// metaDate = metaData.getJSONObject(2);
		atts.add(new Attribute(CONFIGURATION.REPT_TARGET));

		// we have retrieved meta data, then we can create an instance.
		data = new Instances("hourly", atts, 0);

		int hour = 0;
		String day = "";
		String oday = resultSets.getJSONArray(0).getString(0);
		double accumulator = 0;
		int number = 1;
		double[] instanceValue = new double[data.numAttributes()];
		double[] instanceValueAcc = new double[data.numAttributes()];
		for (int i = 0; i < numberOfRows; i++) {
			JSONArray resultSet = resultSets.getJSONArray(i);
			hour = Integer.parseInt(resultSet.getString(1));
			day = resultSet.getString(0);

			// if it is another day.
			// set the accumulated target attribute and reset accumulator.
			if (!day.equals(oday)) {
				// the first attribute.
				try {
					instanceValueAcc[0] = data.attribute(0).parseDate(
							oday + " " + (CONFIGURATION.TIME_END_WORKING + 1));
				} catch (ParseException e) {
					e.printStackTrace();
					// if there are parse errors, set date to 1970-01-01, then
					// we can drop them.
					instanceValueAcc[0] = 0;
				}
				// set target as the value of accumulator.
				instanceValueAcc[1] = accumulator / number;
				data.add(new DenseInstance(CONFIGURATION.WEKA_DEFAULTWEIGHT,
						instanceValueAcc));
				// reset accumulator and instanceValue.
				accumulator = 0;
				number = 1;
				instanceValueAcc = new double[data.numAttributes()];
				oday = day;
			}
			if ((hour >= CONFIGURATION.TIME_START_WORKING)
					&& (hour <= CONFIGURATION.TIME_END_WORKING)) {
				// the first attribute.
				try {
					instanceValue[0] = data.attribute(0).parseDate(
							day + " " + hour);
				} catch (ParseException e) {
					e.printStackTrace();
					// if there are parse errors, set date to 1970-01-01, then
					// we
					// can drop them.
					instanceValue[0] = 0;
				}
				// the second attribute.
				if (resultSet.getString(2).equals(CONFIGURATION.REPT_NULL)) {
					// if the column is null, set it to 0.
					instanceValue[1] = 0;
				} else {
					instanceValue[1] = resultSet.getDouble(2);
				}
				data.add(new DenseInstance(CONFIGURATION.WEKA_DEFAULTWEIGHT,
						instanceValue));
				// reset instanceValue.
				instanceValue = new double[data.numAttributes()];
			} else {
				// accumulate target values;
				if (resultSet.getString(2).equals(CONFIGURATION.REPT_NULL)) {
					// if the column is null, set it to 0.
				} else {
					accumulator += resultSet.getDouble(2);
					number++;
				}
			}
			if (i == (numberOfRows - 1)) {
				// if it is the last one in the result set.
				// save attribute and finish loop.
				try {
					instanceValueAcc[0] = data.attribute(0).parseDate(
							day + " " + (CONFIGURATION.TIME_END_WORKING + 1));
				} catch (ParseException e) {
					e.printStackTrace();
					// if there are parse errors, set date to 1970-01-01, then
					// we
					// can drop them.
					instanceValueAcc[0] = 0;
				}
				// set target as the value of accumulator.
				instanceValueAcc[1] = accumulator / number;
				data.add(new DenseInstance(CONFIGURATION.WEKA_DEFAULTWEIGHT,
						instanceValueAcc));
				// reset accumulator and instanceValue.
				accumulator = 0;
				number++;
				instanceValueAcc = new double[data.numAttributes()];
			}
		}
		return data;
	}
}

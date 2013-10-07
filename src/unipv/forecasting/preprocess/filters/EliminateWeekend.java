/**
 * 
 */
package unipv.forecasting.preprocess.filters;

import java.util.Calendar;
import java.util.GregorianCalendar;

import unipv.forecasting.CONFIGURATION;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class EliminateWeekend implements Filter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.preprocess.filters.Filter#doFilter(weka.core.Instances)
	 */
	@Override
	public Instances doFilter(Instances originalData) {
		// TODO Auto-generated method stub
		Instances data = originalData;
		Instances result = new Instances(data, 0);

		Calendar calendar = new GregorianCalendar();
		Double time = 0.0;
		// traversing instances
		for (int i = 0; i < data.numInstances(); i++) {
			time = data.get(i).value(CONFIGURATION.REPT_DATE_INDEX);
			calendar.setTimeInMillis(time.longValue());
			// if it is not weekend.
			if ((calendar.get(Calendar.DAY_OF_WEEK) != 7)
					&& (calendar.get(Calendar.DAY_OF_WEEK) != 1)) {
				result.add(data.get(i));
			}
		}
		return result;
	}

}

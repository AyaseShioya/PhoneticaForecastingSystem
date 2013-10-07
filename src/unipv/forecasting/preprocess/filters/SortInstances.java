/**
 * 
 */
package unipv.forecasting.preprocess.filters;

import unipv.forecasting.CONFIGURATION;
import weka.core.Instances;

/**
 * @author Quest
 *
 */
public class SortInstances implements Filter {

	/* (non-Javadoc)
	 * @see unipv.forecasting.preprocess.filters.Filter#doFilter(weka.core.Instances)
	 */
	@Override
	public Instances doFilter(Instances originalData) {
		Instances data = originalData;
		data.sort(CONFIGURATION.REPT_DATE_INDEX);
		return data;
	}

}

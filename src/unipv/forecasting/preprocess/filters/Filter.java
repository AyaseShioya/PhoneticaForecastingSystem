/**
 * Interface for filters
 */
package unipv.forecasting.preprocess.filters;

import weka.core.Instances;

/**
 * @author Quest
 */
public interface Filter {

	/**
	 * do filter process on the original data in order to get a new set of data
	 * with better quality.
	 * 
	 * @param originalData
	 *            original data in WEKA instances format.
	 * @return the data filtered by the filter.
	 */
	Instances doFilter(final Instances originalData);
}

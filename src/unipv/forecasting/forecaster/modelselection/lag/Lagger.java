/**
 * The interface for creating lags for given instances.
 */
package unipv.forecasting.forecaster.modelselection.lag;

import java.util.Calendar;

import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public interface Lagger {

	/**
	 * create a lagged instances
	 * 
	 * @return lagged data
	 */
	abstract Instances createLag();

	/**
	 * retrieve the length of lag
	 * 
	 * @return the length of lag
	 */
	abstract int getLagLength();

	/**
	 * set the minimum lag.
	 * 
	 * @param minLag
	 *            minimum lag.
	 */
	abstract void setMinLag(final int minLag);

	abstract int getMinLag();

	/**
	 * set the maximum lag
	 * 
	 * @param maxLag
	 *            maximum lag
	 */
	abstract void setMaxLag(final int maxLag);

	abstract int getMaxLag();

	/**
	 * set the data which will be lagged
	 * 
	 * @param data
	 */
	abstract void inputData(Instances data);

	/**
	 * retrieve the begin date of the lagged data.
	 * 
	 * @return calendar of the first instance.
	 */
	abstract Calendar getBeginDate();

	abstract Lagger copy();

}

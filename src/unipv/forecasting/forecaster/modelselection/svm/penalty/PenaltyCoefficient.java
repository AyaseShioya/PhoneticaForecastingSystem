/**
 * The interface of different methods used to calculate an optimal parameter C.
 */
package unipv.forecasting.forecaster.modelselection.svm.penalty;

import unipv.forecasting.forecaster.modelselection.svm.SVMConfiguration;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public interface PenaltyCoefficient {
	/**
	 * . Calculate a optimal parameter C
	 * 
	 * @param data
	 *            The sample data.
	 */
	void optimize(Instances data, SVMConfiguration configuration);
}

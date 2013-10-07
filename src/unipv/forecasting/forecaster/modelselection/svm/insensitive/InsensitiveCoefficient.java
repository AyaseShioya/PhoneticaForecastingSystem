package unipv.forecasting.forecaster.modelselection.svm.insensitive;

import unipv.forecasting.forecaster.modelselection.svm.SVMConfiguration;
import weka.core.Instances;

public interface InsensitiveCoefficient {
	/**
	 * . The method used to calculate a optimal epsilon
	 * 
	 * @param data
	 *            The sample data.
	 */
	void optimize(final Instances data, SVMConfiguration configuration);
}

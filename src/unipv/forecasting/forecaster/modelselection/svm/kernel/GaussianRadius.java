package unipv.forecasting.forecaster.modelselection.svm.kernel;

import weka.core.Instances;

public interface GaussianRadius {
	/**
	 * . Calculate a optimal Gaussian Radius.
	 * 
	 * @param data
	 *            The sample data.
	 * @return The optimal C for SVM.
	 */
	double optimize(Instances data);
}

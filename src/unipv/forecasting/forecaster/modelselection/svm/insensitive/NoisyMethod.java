/**
 * Optimize insensitive coefficient using noisy method, for detail, please check
 * Vladimir C, Yunqian M. Practical Selection of SVM Parameters and
 * Noise Estimation for SVM Regression[J]. Neural Networks, 2004,
 * 17(1): 113-126.
 */
package unipv.forecasting.forecaster.modelselection.svm.insensitive;

import unipv.forecasting.forecaster.modelselection.LearningModel;
import unipv.forecasting.forecaster.modelselection.svm.SVM;
import unipv.forecasting.forecaster.modelselection.svm.SVMConfiguration;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class NoisyMethod implements InsensitiveCoefficient {

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forcasting.preprocess.modelselection.svm.insensitive.
	 * InsensitiveCoefficient#optimize(weka.core.Instances)
	 */
	@Override
	public void optimize(final Instances data, SVMConfiguration configuration) {
		LearningModel learningModel = new SVM(configuration);
		learningModel.createTrainer();

		((SVM) learningModel).getTrainer().setTrainingSample(data);
		// retrieve training error.
		double error = ((SVM) learningModel).getTrainer().getTrainingError();
		configuration.setEpsilon(optimize(data.numInstances(), error));
	}

	/**
	 * optimize Epsilon
	 * 
	 * @param n
	 *            the size of training sample.
	 * @param estimates
	 *            the MSE of training process.
	 * @return the optimal epsilon.
	 */
	private double optimize(final Integer n, final double error) {
		double k = 3.0;
		double noise = (Math.pow(n, 0.2) * k) / (Math.pow(n, 0.2) * (k - 1.0))
				* error;
		return 3.0 * Math.sqrt(noise)
				* Math.sqrt(Math.log(n) / n.doubleValue());
	}

}

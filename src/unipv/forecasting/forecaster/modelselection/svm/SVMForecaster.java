/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm;

import unipv.forecasting.forecaster.modelselection.Forecaster;
import unipv.forecasting.forecaster.modelselection.lag.Lagger;
import weka.classifiers.functions.supportVector.Kernel;
import weka.core.Instance;
import weka.core.Instances;
import unipv.forecasting.utils.Normalizer;

/**
 * @author Quest
 * 
 */
public class SVMForecaster extends Forecaster {
	/** the essential parameters required by SVM **/
	private SVMConfiguration configuration;

	public SVMForecaster(SVMConfiguration configuration) {
		super();
		this.configuration = configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.Forecaster#doLagging()
	 */
	@Override
	public Lagger getLagger() {
		return configuration.getLagger();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.Forecaster#forecast(weka.
	 * core.Instance)
	 */
	@Override
	public double forecast(Instance pre) {
		// the forecasting result.
		double result = -configuration.getB();

		try {
			// Normalizer normalizer = configuration.getNormalizer();
			// normalizer.batchFinished();
			// normalizer.input(pre);
			// pre = normalizer.output();

			Normalizer normalizer = configuration.getNormalizer();
			normalizer.normalize(pre);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			double[] weight = configuration.getWeight();
			Kernel kernel = configuration.getKernel();
			Instances supportVectors = configuration.getSupportVectors();
			for (int i = 0; i < supportVectors.numInstances(); i++) {
				result += weight[i] * kernel.eval(-1, i, pre);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = result * configuration.getM_x1() + configuration.getM_x0();
		result = result < 0 ? 0 : result;
		return result;
	}

}

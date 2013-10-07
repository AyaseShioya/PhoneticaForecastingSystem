/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm;

import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.utils.MetheUtilities;
import unipv.forecasting.utils.Normalizer;
import weka.classifiers.functions.supportVector.Kernel;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class SMOReg {
	private Instances training;
	private SVMConfiguration configuration;

	public SMOReg(SVMConfiguration configuration, Instances training) {
		super();
		this.training = training;
		this.configuration = configuration;
	}

	public void train() throws Exception {

		// remove instances with missing class
		training = new Instances(training);
		training.deleteWithMissingClass();

		// Removes all the instances with weight equal to 0.
		// MUST be done since condition (8) of Keerthi's paper
		// is made with the assertion Ci > 0 (See equation (3a).
		Instances data = new Instances(training, 0);
		for (int i = 0; i < training.numInstances(); i++) {
			if (training.instance(i).weight() > 0) {
				data.add(training.instance(i));
			}
		}

		if (data.numInstances() == 0) {
			throw new Exception("No training instances left after removing "
					+ "instance with either a weight null or a missing class!");
		}
		// set instances as the one without 0 weight.
		training = data;

		// retrieve two different class values used to determine filter
		// transformation
		double y0 = training.instance(0).classValue();
		int index = 1;
		while (index < training.numInstances()
				&& training.instance(index).classValue() == y0) {
			index++;
		}
		if (index == training.numInstances()) {
			// degenerate case, all class values are equal
			// we don't want to deal with this, too much hassle
			throw new Exception(
					"All class values are the same. At least two class values should be different");
		}
		double y1 = training.instance(index).classValue();

		// apply filters
		// Normalize normalizer = new Normalize();
		// normalizer.setIgnoreClass(true);
		// normalizer.setInputFormat(training);
		// training = Filter.useFilter(training, normalizer);
		// System.out.println(training);
		Normalizer normalizer = new Normalizer();
		normalizer.normalize(training);
		configuration.setNormalizer(normalizer);

		// double summation = 0.0;
		// for(Instance instance: training) {
		// summation += instance.value(4);
		// }
		// System.out.println(summation);

		double z0 = training.instance(0).classValue();
		double z1 = training.instance(index).classValue();
		// no division by zero, since y0 != y1 guaranteed => z0 != z1 ???
		double m_x1 = (y0 - y1) / (z0 - z1);
		configuration.setM_x1(m_x1);
		// = y1 - m_x1 * z1
		double m_x0 = (y0 - m_x1 * z0);
		configuration.setM_x0(m_x0);
		// optimization begin.

		SMOOpt optimizer = new SMOOpt(this);
		optimizer.buildClassifier();
	}

	public void close() {

	}

	/**
	 * @return the training
	 */
	public Instances getTraining() {
		return training;
	}

	/**
	 * @param training
	 *            the training to set
	 */
	public void setTraining(Instances training) {
		this.training = training;
	}

	/**
	 * @return the configuration
	 */
	public SVMConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration
	 *            the configuration to set
	 */
	public void setConfiguration(SVMConfiguration configuration) {
		this.configuration = configuration;
	}

	public double getTrainingError() {
		// the forecasting result.
		double[] act = training
				.attributeToDoubleArray(CONFIGURATION.REPT_VALUE_INDEX);
		double[] pred = new double[training.numInstances()];
		for (int i = 0; i < training.numInstances(); i++) {
			pred[i] = -configuration.getB();

			try {
				double[] weight = configuration.getWeight();
				Kernel kernel = configuration.getKernel();
				Instances supportVectors = configuration.getSupportVectors();
				for (int j = 0; j < supportVectors.numInstances(); j++) {
					pred[i] += weight[j] * kernel.eval(-1, j, training.get(i));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pred[i] = pred[i] < 0 ? 0 : pred[i];
		}
		return MetheUtilities.calculateMSE(act, pred);
	}
}

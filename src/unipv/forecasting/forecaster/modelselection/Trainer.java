/**
 * The interface of trainers used to build the model for forecasting with
 * a set of training data.
 */
package unipv.forecasting.forecaster.modelselection;

import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public abstract class Trainer {
	/** the copy of original, will be processed with lagger **/
	protected Instances training;

	/**
	 * @return the training
	 */
	public Instances getTraining() {
		return training;
	}

	/**
	 * set original and copy
	 * 
	 * @param trainingSample
	 *            the original sample.
	 */
	public void setTrainingSample(Instances trainingSample) {
		training = new Instances(trainingSample);
		training = doLagging();
	}

	public void input(Instances trainingSample) {
		training = new Instances(trainingSample);
	}

	/**
	 * process the original data with lagger.
	 * 
	 * @return the copy of original data and has been processed by the lagger.
	 */
	protected abstract Instances doLagging();

	/**
	 * train the machine learning model with the training sample.
	 */
	public abstract void train();

	public Trainer copy(Trainer trainer) {
		trainer.setTraining(new Instances(training));
		return trainer;
	}

	/**
	 * @param training
	 *            the training to set
	 */
	public void setTraining(Instances training) {
		this.training = training;
	}

	public abstract double getTrainingError();
}

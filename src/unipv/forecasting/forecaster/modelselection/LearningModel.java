/**
 * The interface for model learning models.
 */
package unipv.forecasting.forecaster.modelselection;

import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public abstract class LearningModel {
	protected Trainer trainer;
	protected Forecaster forecaster;

	public abstract void createTrainer();

	public abstract void createForecaster();

	public abstract void train(final Instances trainingSample);

	public abstract Instances forecast(final int steps,
			final Instances forecastingSample);

	public abstract double[] forecast(final Instances data);

	/**
	 * @return the trainer
	 */
	public Trainer getTrainer() {
		return trainer;
	}

	/**
	 * @param trainer
	 *            the trainer to set
	 */
	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

	/**
	 * @return the forecaster
	 */
	public Forecaster getForecaster() {
		return forecaster;
	}

	/**
	 * @param forecaster
	 *            the forecaster to set
	 */
	public void setForecaster(Forecaster forecaster) {
		this.forecaster = forecaster;
	}

	/**
	 * @return the training training set.
	 */
	abstract public Instances getTraining();

	/**
	 * @return the training forecasting set.
	 */
	abstract public Instances getForecasting();

	abstract public LearningModel copy();
}

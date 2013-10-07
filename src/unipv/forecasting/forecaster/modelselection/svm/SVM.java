/**
 * Support Vector Machine.
 */
package unipv.forecasting.forecaster.modelselection.svm;

import unipv.forecasting.forecaster.modelselection.Forecaster;
import unipv.forecasting.forecaster.modelselection.LearningModel;
import unipv.forecasting.forecaster.modelselection.Trainer;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class SVM extends LearningModel {
	/** the essential parameters required by SVM **/
	private SVMConfiguration configuration;

	public SVM(SVMConfiguration configuration) {
		super();
		this.configuration = configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.LearningModel#createTrainer
	 * (weka.core.Instances)
	 */
	@Override
	public void createTrainer() {
		super.setTrainer(new SVMTrainer(configuration));
		super.setForecaster(new SVMForecaster(configuration));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.LearningModel#createForecaster
	 * (weka.core.Instances)
	 */
	@Override
	public void createForecaster() {
		super.setForecaster(new SVMForecaster(configuration));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.LearningModel#train()
	 */
	@Override
	public void train(Instances trainingSample) {
		trainer.setTrainingSample(trainingSample);
		trainer.train();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.LearningModel#forecast(int)
	 */
	@Override
	public Instances forecast(int steps, Instances forecastingSample) {
		forecaster.input(forecastingSample);
//		System.out.println(forecaster.forecast(steps));
		Instances result = forecaster.forecast(steps);
		return result;
	}

	public double[] forecast(final Instances data) {
		return forecaster.forecast(data);
	}

	/**
	 * @return the training
	 */
	public Instances getTraining() {
		return trainer.getTraining();
	}

	public Instances getForecasting() {
		return forecaster.getForecasting();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.LearningModel#copy()
	 */
	@Override
	public LearningModel copy() {
		SVMConfiguration configurationCopy = configuration == null ? null
				: configuration.copy();
		LearningModel learningModel = new SVM(configurationCopy);
		Trainer trainerCopy = trainer == null ? null : new SVMTrainer(
				configurationCopy);
		learningModel.setTrainer(trainerCopy);

		Forecaster forecasterCopy = forecaster == null ? null
				: new SVMForecaster(configurationCopy);
		learningModel.setForecaster(forecasterCopy);

		return learningModel;
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

}

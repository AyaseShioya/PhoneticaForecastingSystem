/**
 * The common context which machine learning model
 * will use them to train or forecast data.
 */
package unipv.forecasting.forecaster.modelselection;

import java.io.IOException;

import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.forecaster.modelselection.svm.SVM;
import unipv.forecasting.utils.MetheUtilities;
import weka.core.Instances;

;;
/**
 * @author Quest
 * 
 */
public class Context {
	/** the set of data used to train the machine learning model **/
	private Instances trainingSample;
	/** the set of data used to generate data for forecasting **/
	private Instances forecastingSample;
	/** the machine learning model used to train or forecast data **/
	private LearningModel learningModel;
	private double mse;
	private double rsd;
	private double relativeError;
	private double[] trainingResults;

	public Context(LearningModel learningModel) {
		this.learningModel = learningModel;
	}

	/**
	 * create a trainer with the machine learning model.
	 */
	public void createTrainer() {
		learningModel.createTrainer();
	}

	/**
	 * create a forecaster with the machine learning model.
	 */
	public void createForecaster() {
		learningModel.createForecaster();
	}

	/**
	 * train the model with trainingSample.
	 * 
	 * @return MSE (Mean Squared Error) of the generated model forecast on the
	 *         original data.
	 */
	public double train() {
		learningModel.train(trainingSample);
		Instances odata = learningModel.getTraining();
		double[] actuals = odata
				.attributeToDoubleArray(CONFIGURATION.REPT_VALUE_INDEX);
		trainingResults = learningModel.forecast(odata);
		// System.out.println(actuals.length + "," + trainingResults.length);
		mse = MetheUtilities.calculateMSE(actuals, trainingResults);
		rsd = MetheUtilities.calculateRSD(actuals, trainingResults);
		relativeError = MetheUtilities.calculateRelativeError(actuals,
				trainingResults);
		return rsd;
	}

	/**
	 * do cross validation on the training sample.
	 * 
	 * @param folds
	 *            folds of the cross validation.
	 * @return mean MSE of the cross validation. and the % of support vectors.
	 * @throws IOException
	 */
	public double[] crossValidation(final int folds,
			final Instances trainingSample) throws IOException {
		double numFolds = folds;
		/** preprocess for training. **/
		Instances data = new Instances(trainingSample);

		double numInstance = data.numInstances();
		double volumeDou = numInstance / numFolds;
		int volume = ((Long) Math.round(volumeDou)).intValue();
		double[] result = new double[2];
		result[0] = 0.0;

		Instances[] samples = new Instances[folds];
		/** training sample **/
		Instances tSample = null;
		/** forecasting sample **/
		Instances fSample = null;

		/** initialization **/
		for (int i = 0; i < folds; i++) {
			samples[i] = new Instances(data, 0);
		}
		/** split sample into pieces. **/
		for (int i = 0; i < data.numInstances(); i++) {
			int index = (i / volume) > (folds - 1) ? (folds - 1) : (i / volume);
			samples[index].add(data.get(i));
		}
		double numSVs = 0;

		/** outer loop for folds **/
		for (int i = 0; i < folds; i++) {
			LearningModel subModel = learningModel.copy();
			tSample = new Instances(data, 0);
			fSample = new Instances(data, 0);
			/**
			 * inner loop for constructing training sample and forecasting
			 * sample
			 */
			for (int j = 0; j < folds; j++) {
				if (j == i) {
					fSample = samples[j];
				} else {
					tSample.addAll(samples[j]);
				}
				if (folds == 1) {
					tSample = samples[j];
				}
			}
			/** training **/

			if (subModel.getTrainer() == null)
				subModel.createTrainer();
			// System.out.println(subModel.getTrainer());
			subModel.train(tSample);

			Integer sv = ((SVM) subModel).getConfiguration()
					.getSupportVectors().numInstances();
			Integer in = ((SVM) subModel).getTrainer().getTraining()
					.numInstances();

			numSVs += sv.doubleValue() / in.doubleValue();

			// double summation = 0.0;
			// if(i == 0) {
			// for(Instance instance: subModel.getTrainer().getTraining()) {
			// summation += instance.value(15);
			// }
			// System.out.println(summation);
			// }

			// if (i == 0)
			// System.out.println(((SVM)subModel).getConfiguration());

			// Instances odata = subModel.getTraining();
			// double[] act = odata
			// .attributeToDoubleArray(ForecasterConfiguration.REPT_VALUE_INDEX);
			// double[] pred = subModel.forecast(odata);
			// System.err.println(MetheUtilities.calculateMSE(act, pred));

			// if(i == 0) {
			// System.out.println(((SVM)subModel).getConfiguration().getLagger().getBeginDate());
			// }

			/** forecasting **/
			subModel.getForecaster().input(fSample);
			double[] predictions = subModel.getForecaster().forecast();

			double[] actuals = subModel.getForecaster().getForecasting()
					.attributeToDoubleArray(CONFIGURATION.REPT_VALUE_INDEX);
			// System.out.println(actuals.length + "," + predictions.length);
			switch (CONFIGURATION.SV_EVALUATER) {
			case MSE:
				result[0] += MetheUtilities.calculateMSE(actuals, predictions);
				break;
			case RSD:
				result[0] += MetheUtilities.calculateRSD(actuals, predictions);
				break;
			case RE:
				result[0] += MetheUtilities.calculateRelativeError(actuals,
						predictions);
				break;
			}

			// System.out.println("error:"
			// + MetheUtilities.calculateMSE(actuals, predictions) + ","
			// + result);
			// System.out.println("progress:" + ((i + 0.0)/ folds) * 100 + "%");
		}
		result[0] /= numFolds;
		result[1] = numSVs / folds;
		samples = null;
		return result;
	}

	/**
	 * make forecasting by the model.
	 * 
	 * @param steps
	 *            the steps after the final time point of forecasting model.
	 * @return the result of forecasting, along with error and actual data if
	 *         possible.
	 */
	public Instances forecast(final int steps) {
		return learningModel.forecast(steps, forecastingSample);
	}

	/**
	 * @return the trainingSample
	 */
	public Instances getTrainingSample() {
		return trainingSample;
	}

	/**
	 * @param trainingSample
	 *            the trainingSample to set
	 */
	public void setTrainingSample(Instances trainingSample) {
		this.trainingSample = trainingSample;
	}

	/**
	 * @return the forecastingSample
	 */
	public Instances getForecastingSample() {
		return forecastingSample;
	}

	/**
	 * @param forecastingSample
	 *            the forecastingSample to set
	 */
	public void setForecastingSample(Instances forecastingSample) {
		this.forecastingSample = forecastingSample;
	}

	/**
	 * @return the learningModel
	 */
	public LearningModel getLearningModel() {
		return learningModel;
	}

	/**
	 * @param learningModel
	 *            the learningModel to set
	 */
	public void setLearningModel(LearningModel learningModel) {
		this.learningModel = learningModel;
	}

	/**
	 * @return the mse
	 */
	public double getMse() {
		return mse;
	}

	/**
	 * @return the rsd
	 */
	public double getRsd() {
		return rsd;
	}

	/**
	 * @return the trainingResults
	 */
	public double[] getTrainingResults() {
		return trainingResults;
	}

	/**
	 * @return the relativeError
	 */
	public double getRelativeError() {
		return relativeError;
	}
}

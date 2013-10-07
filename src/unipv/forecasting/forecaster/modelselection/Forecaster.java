/**
 * The interface of forecaster which generate a
 * set of forecasting result by a model.
 */
package unipv.forecasting.forecaster.modelselection;

import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.forecaster.modelselection.lag.Lagger;
import unipv.forecasting.utils.MetheUtilities;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public abstract class Forecaster {
	// The copy of original data, will be processed by lagger.
	protected Instances forecasting;

	/**
	 * @return the forecasting
	 */
	public Instances getForecasting() {
		return forecasting;
	}

	/**
	 * @param forecasting
	 *            the forecasting to set
	 */
	public void input(Instances forecastingSample) {
		forecasting = new Instances(forecastingSample);
	}

	/**
	 * forecast the target value of a given instance.
	 * 
	 * @param pre
	 *            the instance will be forecasted.
	 * @return the forecasting result of this instance.
	 */
	public abstract double forecast(Instance pre);

	/**
	 * forecast the target values of the forecasting sample.
	 * 
	 * @return the forecasting results of the forecasting sample.
	 */
	public double[] forecast() {
		getLagger().inputData(forecasting);
		forecasting = getLagger().createLag();
		forecasting.setClassIndex(CONFIGURATION.REPT_LAGGEDVALUE_INDEX);
		double[] pred = new double[forecasting.numInstances()];
		for (int i = 0; i < forecasting.numInstances(); i++) {
			pred[i] = forecast(forecasting.get(i));
		}
		return pred;
	}

	public double[] forecast(Instances instances) {
		double[] pred = new double[instances.numInstances()];
		for (int i = 0; i < instances.numInstances(); i++) {
			pred[i] = forecast(instances.get(i));
		}
		return pred;
	}

	public Instances forecast(int steps) {
		Instances history = new Instances(forecasting);
//		System.out.println(forecasting);
		Instances results = new Instances(history, 0);
		Instance pred;
		Instance predLagged;
		Instances lag;
		double start = getLagger().getBeginDate().getTimeInMillis();
		for (int i = 0; i < steps; i++) {
			// retrieve the last instance.
			pred = new DenseInstance(history.get(history.numInstances() - 1));
			// get the time of the last instance.
			double timePred = pred.value(CONFIGURATION.REPT_DATE_INDEX);
			// get the start time of the training sample.
			// add one step on the time.
//			System.out.println(MetheUtilities.translateDateToIndex(timePred, start) + 1);
			timePred = MetheUtilities.translateIndexToDate_hourly(
					(MetheUtilities.translateDateToIndex(timePred, start) + 1),
					start);
//			System.out.println(timePred);
			pred.setValue(CONFIGURATION.REPT_DATE_INDEX, timePred);
			// retrieve enough history data.
			lag = new Instances(history, 0);
			for (int j = 2 * getLagger().getLagLength(); j > 0; j--) {
				lag.add(history.get(history.numInstances() - j));
			}
			lag.add(pred);
			// create lag.
			getLagger().inputData(lag);
			lag = getLagger().createLag();
			lag.setClassIndex(CONFIGURATION.REPT_LAGGEDVALUE_INDEX);
			// forecast
			predLagged = lag.get(0);
			pred.setValue(CONFIGURATION.REPT_VALUE_INDEX, forecast(predLagged));
			// save forecasting result;
			results.add(pred);
//			Calendar c = Calendar.getInstance();
//			Double time = timePred;
//			c.setTimeInMillis(time.longValue());
//			System.out.println(c.getTime());
			history.add(pred);
		}
//		System.out.println(history);
		return results;
	}

	public abstract Lagger getLagger();

	public Forecaster copy(Forecaster forecaster) {
		forecaster.setForecasting(forecasting);
		return forecaster;
	}

	/**
	 * @param forecasting
	 *            the forecasting to set
	 */
	public void setForecasting(Instances forecasting) {
		this.forecasting = forecasting;
	}

}

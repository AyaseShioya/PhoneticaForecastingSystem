/**
 * 
 */
package unipv.forecasting.forecaster.modelselection;

import unipv.forecasting.CONFIGURATION.FORECASTER_CONFIGURATOR;
import unipv.forecasting.CONFIGURATION.TRAINER_CONFIGURATOR;
import unipv.forecasting.forecaster.modelselection.lag.Lagger;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public interface Configuration {
	/**
	 * configure trainer relevant parameters by a specific set.
	 * 
	 * @param set
	 *            the set of predefined trainer configuration.
	 */
	void configureTrainer(final TRAINER_CONFIGURATOR set, final Instances data,
			final Context system);

	/**
	 * configure trainer relevant parameter by a default set.
	 */
	abstract void configureTrainer();

	/**
	 * configure forecaster relevant parameters by a specific set
	 * 
	 * @param set
	 *            the set of predefined forecaster configuration.
	 */
	abstract void configureForecaster(final FORECASTER_CONFIGURATOR set,
			final Instances data, final Context system);

	/**
	 * configure trainer relevant parameter by a default set.
	 */
	abstract void configureForecaster();

	/**
	 * save the configuration in database.
	 */
	abstract void saveModel();

	abstract Lagger getLagger();
}

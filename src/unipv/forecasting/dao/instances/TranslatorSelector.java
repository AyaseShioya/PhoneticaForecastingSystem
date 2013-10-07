/**
 * Factory for translators.
 */
package unipv.forecasting.dao.instances;

import unipv.forecasting.CONFIGURATION.TRANSLATOR;

/**
 * @author Quest
 */
public class TranslatorSelector {
	/**
	 * factory method
	 * 
	 * @param translatorType
	 *            type of translator you want to create
	 * @return specific translator
	 */
	public final InstancesTranslator getTranslator(final TRANSLATOR translator) {
		switch (translator) {
		case DAILY_SUM:
			return new DailyInstancesTranslator();
		case HOURLY_SUM:
			return new HourlyInstancesTranslator();
		case YEARLY_SUM:
			return new YearlyInstancesTranslator();
		case HOURLY_AVG:
			return new HourlyAVGInstancesTranslator();
		default:
			return null;
		}
	}
}

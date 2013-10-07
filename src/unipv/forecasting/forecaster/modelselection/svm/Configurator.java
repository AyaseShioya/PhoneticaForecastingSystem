/**
 * Configurator interface. Used to configure the SVMConfiguration.
 */
package unipv.forecasting.forecaster.modelselection.svm;

/**
 * @author Quest
 *
 */
public abstract class Configurator {
	/**
	 * The configuration will be configured.
	 */
	protected SVMConfiguration configuration;
	/**
	 * Set up the configuration.
	 * @param configuration
	 *   the configuraiton will be configured.
	 */
	public void setConfiguration(SVMConfiguration configuration){
		this.configuration = configuration;
	}
	/**
	 * configure all the essential parameters.
	 */
	abstract void configure();
}

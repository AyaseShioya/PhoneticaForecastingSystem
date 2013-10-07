/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm;

/**
 * @author Quest
 * 
 */
public abstract class TrainerConfigurator extends Configurator {

	public void setConfiguration(SVMConfiguration configuration) {
		super.setConfiguration(configuration);
	}

	/** parameters which is essential when build up the trainer. **/
	abstract void setKernel();

	abstract void setC();

	abstract void setEpsilon();

	abstract void setNSeed();

	abstract void isVariant1();

	abstract void setTolerance();

	abstract void setLagger();

	/**
	 * batch configuration for all the essential parameters when build up a
	 * trainer.
	 */
	public void configure() {
		setC();
		setKernel();
		setNSeed();
		isVariant1();
		setTolerance();
		setLagger();
		setEpsilon();
	}
}

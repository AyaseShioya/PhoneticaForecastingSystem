/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm;

/**
 * @author Quest
 * 
 */
public abstract class ForecasterConfigurator extends Configurator {

	/** parameters which is essential when build up the forecaster. **/
	abstract void setM_x1();

	abstract void setM_x0();

	abstract void setKernel();

	abstract void setB();

	abstract void setSupportVectors();

	abstract void setWeight();

	abstract void setLagger();

	abstract void setNormalizer();

	/**
	 * batch configuration for all the essential parameters when build up a
	 * forecaster.
	 */
	public void configure() {
		setM_x1();
		setM_x0();
		setKernel();
		setB();
		setSupportVectors();
		setWeight();
		setLagger();
		setNormalizer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.Configurator#setConfiguration
	 * (unipv.forecasting.forecaster.modelselection.svm.SVMConfiguration)
	 */
	@Override
	public void setConfiguration(SVMConfiguration configuration) {
		// TODO Auto-generated method stub
		super.setConfiguration(configuration);
	}
}
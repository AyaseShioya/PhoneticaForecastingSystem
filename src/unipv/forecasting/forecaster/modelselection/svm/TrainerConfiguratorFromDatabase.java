/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm;

import unipv.forecasting.dao.database.ParameterConnector;

/**
 * @author Quest
 * 
 */
public class TrainerConfiguratorFromDatabase extends TrainerConfigurator {
	private SVMConfiguration newCon;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#setKernel
	 * ()
	 */
	@Override
	void setKernel() {
		if (newCon != null)
			configuration.setKernel(newCon.getKernel());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#setC
	 * ()
	 */
	@Override
	void setC() {
		if (newCon != null)
			configuration.setC(newCon.getC());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * setEpsilon()
	 */
	@Override
	void setEpsilon() {
		if (newCon != null)
			configuration.setEpsilon(newCon.getEpsilon());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#setNSeed
	 * ()
	 */
	@Override
	void setNSeed() {
		if (newCon != null)
			configuration.setnSeed(newCon.getnSeed());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * isVariant1()
	 */
	@Override
	void isVariant1() {
		if (newCon != null)
			configuration.setVariant1(newCon.isVariant1());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * setTolerance()
	 */
	@Override
	void setTolerance() {
		if (newCon != null)
			configuration.setTolerance(newCon.getTolerance());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#setLagger
	 * ()
	 */
	@Override
	void setLagger() {
		if (newCon != null)
			configuration.setLagger(newCon.getLagger());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * setConfiguration
	 * (unipv.forecasting.forecaster.modelselection.svm.SVMConfiguration)
	 */
	@Override
	public void setConfiguration(SVMConfiguration configuration) {
		super.setConfiguration(configuration);
		ParameterConnector connector = new ParameterConnector();
		String jsonString = connector.selectParameter(configuration);
		if (jsonString != null)
			newCon = new SVMConfiguration(jsonString);
		else
			newCon = null;
	}

}

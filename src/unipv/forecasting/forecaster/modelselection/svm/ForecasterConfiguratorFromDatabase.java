/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm;

import unipv.forecasting.dao.database.ParameterConnector;

/**
 * @author Quest
 * 
 */
public class ForecasterConfiguratorFromDatabase extends ForecasterConfigurator {
	private SVMConfiguration newCon;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.ForecasterConfigurator
	 * #setM_x1()
	 */
	@Override
	void setM_x1() {
		if (newCon != null)
			configuration.setM_x1(newCon.getM_x1());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.ForecasterConfigurator
	 * #setM_x0()
	 */
	@Override
	void setM_x0() {
		if (newCon != null)
			configuration.setM_x0(newCon.getM_x0());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.ForecasterConfigurator
	 * #setKernel()
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
	 * unipv.forecasting.forecaster.modelselection.svm.ForecasterConfigurator
	 * #setB()
	 */
	@Override
	void setB() {
		if (newCon != null)
			configuration.setB(newCon.getB());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.ForecasterConfigurator
	 * #setSupportVectors()
	 */
	@Override
	void setSupportVectors() {
		if (newCon != null)
			configuration.setSupportVectors(newCon.getSupportVectors());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.ForecasterConfigurator
	 * #setLagger()
	 */
	@Override
	void setLagger() {
		if (newCon != null)
			configuration.setLagger(newCon.getLagger());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.ForecasterConfigurator
	 * #setWeight()
	 */
	@Override
	void setWeight() {
		if (newCon != null)
			configuration.setWeight(newCon.getWeight());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.ForecasterConfigurator
	 * #setNormalizer()
	 */
	@Override
	void setNormalizer() {
		if (newCon != null)
			configuration.setNormalizer(newCon.getNormalizer());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.ForecasterConfigurator
	 * #setConfiguration
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

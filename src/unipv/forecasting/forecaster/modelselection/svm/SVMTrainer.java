/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm;

import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.forecaster.modelselection.Trainer;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class SVMTrainer extends Trainer {
	/* data used by SVM */
	private SVMConfiguration configuration;

	public SVMTrainer(SVMConfiguration configuration) {
		super();
		this.configuration = configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.Trainer#doLagging()
	 */
	@Override
	protected Instances doLagging() {
		configuration.getLagger().inputData(training);
		Instances t = configuration.getLagger().createLag();
		t.setClassIndex(CONFIGURATION.REPT_LAGGEDVALUE_INDEX);
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.Trainer#train()
	 */
	@Override
	public void train() {
		SMOReg smo = new SMOReg(configuration, training);
		try {
			smo.train();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			smo.close();
		}
	}

	public double getTrainingError() {
		double error = -1;
		SMOReg smo = new SMOReg(configuration, training);
		try {
			smo.train();
			error = smo.getTrainingError();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			smo.close();
		}
		return error;
	}
}

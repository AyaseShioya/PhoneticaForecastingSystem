/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm;

import unipv.forecasting.forecaster.modelselection.lag.ArtificialHourIndex;
import unipv.forecasting.forecaster.modelselection.lag.BasicLagger;
import unipv.forecasting.forecaster.modelselection.lag.DayIndex;
import unipv.forecasting.forecaster.modelselection.lag.HourIndex;
import weka.classifiers.functions.supportVector.RBFKernel;

/**
 * @author Quest
 * 
 */
public class DefaultTrainerConfigurator extends TrainerConfigurator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#setKernel
	 * ()
	 */
	@Override
	void setKernel() {
		RBFKernel kernel = new RBFKernel();
		kernel.setGamma(0.9);
		configuration.setKernel(kernel);
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
		configuration.setC(1.0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * setEpsilon()
	 */
	@Override
	void setEpsilon() {
		configuration.setEpsilon(0.001);
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
		configuration.setnSeed(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * isVariant1()
	 */
	@Override
	void isVariant1() {
		configuration.setVariant1(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * setTolerance()
	 */
	@Override
	void setTolerance() {
		configuration.setTolerance(0.001);
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
		configuration.setLagger(new ArtificialHourIndex(new HourIndex(
				new DayIndex(new BasicLagger(1, 12)))));
	}

}

/**
 * optimizer for SVM parameter using hourly lagger.
 */
package unipv.forecasting.forecaster.modelselection.svm;

import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.forecaster.modelselection.lag.ArtificialHourIndex;
import unipv.forecasting.forecaster.modelselection.lag.BasicLagger;
import unipv.forecasting.forecaster.modelselection.lag.DayIndex;
import unipv.forecasting.forecaster.modelselection.lag.HourIndex;
import unipv.forecasting.forecaster.modelselection.svm.insensitive.InsensitiveCoefficient;
import unipv.forecasting.forecaster.modelselection.svm.insensitive.NoisyMethod;
import unipv.forecasting.forecaster.modelselection.svm.kernel.CombinationKernel;
import unipv.forecasting.forecaster.modelselection.svm.penalty.DirectDeterminationMethod;
import unipv.forecasting.forecaster.modelselection.svm.penalty.PenaltyCoefficient;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class TrainerConfiguratorHCom1 extends TrainerConfigurator {
	private Instances data;

	public TrainerConfiguratorHCom1(Instances data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#setKernel
	 * ()
	 */
	@Override
	void setKernel() {
		// RBFKernel kernel = new RBFKernel();
		CombinationKernel kernel = new CombinationKernel();
		kernel.setLowerOrder(true);
		kernel.setGamma(0.9);
		kernel.setExponent(1.07);
		kernel.setK(0.01);
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
		PenaltyCoefficient c = new DirectDeterminationMethod();
		configuration.setVariant1(true);
		c.optimize(data, configuration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * setEpsilon()
	 */
	@Override
	void setEpsilon() {
		InsensitiveCoefficient epsilon = new NoisyMethod();
		epsilon.optimize(data, configuration);
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
				new DayIndex(new BasicLagger(1, CONFIGURATION.TIME_PERIOD)))));
	}

}

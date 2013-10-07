/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm;

import java.io.IOException;

import weka.core.Instances;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.forecaster.modelselection.Context;
import unipv.forecasting.forecaster.modelselection.svm.kernel.CombinationKernel;

/**
 * @author Quest
 * 
 */
public class TrainerConfiturationGridOptimizer extends TrainerConfigurator {
	private double base;
	private double increaser;
	private Instances data;
	private Context system;

	public TrainerConfiturationGridOptimizer(Instances data, Context system) {
		this.data = data;
		this.system = system;
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

		if (configuration.getKernel() instanceof PolyKernel) {
			gridSearchPolyKernel();
		} else if (configuration.getKernel() instanceof RBFKernel) {
			gridSearchRBFKernel();
		} else if (configuration.getKernel() instanceof CombinationKernel) {
			gridSearchCombinationKernel();
		} else {
			System.err.println("Unknown Kernel");
		}
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
		gridSearchC();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * setEpsilon()
	 */
	@Override
	void setEpsilon() {
		gridSearchEpsilon();
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * isVariant1()
	 */
	@Override
	void isVariant1() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * setTolerance()
	 */
	@Override
	void setTolerance() {

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

	}

	private void gridSearchC() {
		/** initial grid **/
		initial(configuration.getC());
		double best = base;
		double result = 0;
		double error = 0;

		/** initial error **/
		try {
			result = system.crossValidation(
					CONFIGURATION.CROSS_VALIDATION_NUMFOLDS, data)[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** grid searching **/
		int loop = 1;
		while (loop < CONFIGURATION.GRID_LOOP_NUMBER) {
			base += increaser;
			configuration.setC(base);
			try {
				error = system.crossValidation(
						CONFIGURATION.CROSS_VALIDATION_NUMFOLDS, data)[0];
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/** update optimal parameter **/
			if (error < result) {
				result = error;
				best = base;
			}
			loop++;
		}
		configuration.setC(best);
	}

	private void gridSearchRBFKernel() {
		/** initial grid **/
		initial(((RBFKernel) configuration.getKernel()).getGamma());
		double best = base;
		double mse = 0;
		double error = 0;

		try {
			mse = system.crossValidation(
					CONFIGURATION.CROSS_VALIDATION_NUMFOLDS, data)[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int loop = 1;
		while (loop < CONFIGURATION.GRID_LOOP_NUMBER) {
			base += increaser;
			((RBFKernel) configuration.getKernel()).setGamma(base);
			try {
				error = system.crossValidation(
						CONFIGURATION.CROSS_VALIDATION_NUMFOLDS, data)[0];
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error < mse) {
				mse = error;
				best = base;
			}
			loop++;
		}
		((RBFKernel) configuration.getKernel()).setGamma(best);
	}

	private void gridSearchPolyKernel() {
		/** initial grid **/
		initial(((PolyKernel) configuration.getKernel()).getExponent());
		double best = base;
		double mse = 0;
		double error = 0;

		try {
			mse = system.crossValidation(
					CONFIGURATION.CROSS_VALIDATION_NUMFOLDS, data)[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int loop = 1;
		while (loop < CONFIGURATION.GRID_LOOP_NUMBER) {
			base += increaser;
			((PolyKernel) configuration.getKernel()).setExponent(base);
			try {
				error = system.crossValidation(
						CONFIGURATION.CROSS_VALIDATION_NUMFOLDS, data)[0];
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error < mse) {
				mse = error;
				best = base;
			}
			loop++;
		}
		((PolyKernel) configuration.getKernel()).setExponent(best);
	}

	private void gridSearchCombinationKernel() {
		/** optimize gamma **/
		initial(((CombinationKernel) configuration.getKernel()).getGamma());
		((CombinationKernel) configuration.getKernel()).setK(0);
		double best = base;
		double mse = 0;
		double error = 0;

		try {
			mse = system.crossValidation(
					CONFIGURATION.CROSS_VALIDATION_NUMFOLDS, data)[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int loop = 1;
		while (loop < CONFIGURATION.GRID_LOOP_NUMBER) {
			base += increaser;
			((CombinationKernel) configuration.getKernel()).setGamma(base);
			try {
				error = system.crossValidation(
						CONFIGURATION.CROSS_VALIDATION_NUMFOLDS, data)[0];
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error < mse) {
				mse = error;
				best = base;
			}
			loop++;
		}
		((CombinationKernel) configuration.getKernel()).setGamma(best);

		/** optimize exponent **/
		initial(((CombinationKernel) configuration.getKernel()).getExponent());
		((CombinationKernel) configuration.getKernel()).setK(1.0);
		best = base;
		mse = 0;
		error = 0;

		try {
			mse = system.crossValidation(
					CONFIGURATION.CROSS_VALIDATION_NUMFOLDS, data)[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		loop = 1;
		while (loop < CONFIGURATION.GRID_LOOP_NUMBER) {
			base += increaser;
			((CombinationKernel) configuration.getKernel()).setExponent(base);
			try {
				error = system.crossValidation(
						CONFIGURATION.CROSS_VALIDATION_NUMFOLDS, data)[0];
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (error < mse) {
				mse = error;
				best = base;
			}
			loop++;
		}
		((CombinationKernel) configuration.getKernel()).setExponent(best);
		/** set K **/
		// TODO optimize K
		((CombinationKernel) configuration.getKernel()).setK(0.01);
	}

	private void gridSearchEpsilon() {
		/** initial grid **/
		initial(configuration.getEpsilon());
		double best = base;
		double[] result = new double[2];
		double error[] = new double[2];

		/** initial error **/
		try {
			result = system.crossValidation(
					CONFIGURATION.CROSS_VALIDATION_NUMFOLDS, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** grid searching **/
		int loop = 1;
		while (loop < CONFIGURATION.GRID_LOOP_NUMBER) {
			base += increaser;
			configuration.setEpsilon(base);
			try {
				error = system.crossValidation(
						CONFIGURATION.CROSS_VALIDATION_NUMFOLDS, data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/** update optimal parameter **/
			if ((error[1] > CONFIGURATION.SV_LOWER_BOUND)
					&& (error[1] < CONFIGURATION.SV_UPPER_BOUND)) {
				if (evaluate(error) < evaluate(result)) {
					result = error;
					best = base;
				}
			}
			loop++;
		}
		configuration.setEpsilon(best);
	}

	/**
	 * initialize the parameters used in grid searching.
	 * 
	 * @param origin
	 *            the original parameter.
	 */
	private void initial(final double origin) {
		double lowerBound = (origin - (origin * CONFIGURATION.GRID_SCOPE_COEFFICIENT)) < 0 ? 0.0
				: (origin - (origin * CONFIGURATION.GRID_SCOPE_COEFFICIENT));
		double upperBound = origin
				+ (origin * CONFIGURATION.GRID_SCOPE_COEFFICIENT);
		increaser = (upperBound - lowerBound) / CONFIGURATION.GRID_LOOP_NUMBER;
		base = lowerBound + increaser;
	}

	/**
	 * evaluate error and percentage of support vectors
	 * 
	 * @param para
	 *            the array for error and percentage,0:error, 1:#SV in
	 *            percentage.
	 * @return evaluate result.
	 */
	private double evaluate(final double[] para) {
		return para[0] * para[1];
	}
}

/**
 * Calculate parameter C using Direct Determination Method
 */
package unipv.forecasting.forecaster.modelselection.svm.penalty;

import unipv.forecasting.utils.MetheUtilities;
import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.forecaster.modelselection.svm.SVMConfiguration;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

/**
 * @author Quest
 * 
 */
public class DirectDeterminationMethod implements PenaltyCoefficient {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.preprocess.modelselection.svm.ParameterC#calculate()
	 */
	@Override
	public void optimize(final Instances data, SVMConfiguration configuration) {
		Instances odata = new Instances(data);
		// ignore Date.
		odata.setClassIndex(CONFIGURATION.REPT_DATE_INDEX);

		try {
			Normalize normalizer = new Normalize();
			normalizer.setIgnoreClass(true);
			normalizer.setInputFormat(odata);
			odata = Filter.useFilter(odata, normalizer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		double[] values = odata
				.attributeToDoubleArray(CONFIGURATION.REPT_VALUE_INDEX);
		double mean = Utils.mean(values);
		double result = Math.max(
				Math.abs(mean + 3 * MetheUtilities.standardDeviation(values)),
				Math.abs(mean - 3 * MetheUtilities.standardDeviation(values)));
		configuration.setC(result);
	}

}

/**
 * Filter applying interquartile range(IQR), used to eliminate outliers.
 */
package unipv.forecasting.preprocess.filters;

import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.utils.MetheUtilities;
import weka.core.Instances;

/**
 * @author Quest
 */
public class InterquartileRangeFilter implements Filter {

	/**
	 * .
	 * 
	 * @param Instance
	 *            whose value above this is the extreme value.
	 */
	private double upperExtremeValueBound = 0.0;
	/**
	 * @param Instance
	 *            whose value between upperExtremeValueBound and
	 *            upperOurlierBound is the outlier.
	 */
	private double upperOutlierBound = 0.0;
	/**
	 * @param Third
	 *            quartile.
	 */
	private double q3 = 0.0;
	/**
	 * @param Interquartile
	 *            range.
	 */
	private double iqr = 0.0;
	/**
	 * @param First
	 *            quartile.
	 */
	private double q1 = 0.0;
	/**
	 * @param Instance
	 *            whose value between lowerExtremeValueBound and
	 *            lowerOurlierBound is the outlier.
	 */
	private double lowerOutlierBound = 0.0;
	/**
	 * @param Instance
	 *            whose value below this is the extreme value.
	 */
	private double lowerEvtremeValueBound = 0.0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.preprocess.filters.Filter#doFilter(weka.core.Instances)
	 */
	public Instances doFilter(Instances originalData) {
		Instances targetData = originalData;
		// retrieve the values.
		double[] values = targetData
				.attributeToDoubleArray(CONFIGURATION.REPT_VALUE_INDEX);
		// calculate parameters used in IQR.
		calculateParameters(values);
		// for testing
		// System.out.println("iqr: " + iqr);
		// System.out.println("upperExtremeValueBound: "
		// + upperExtremeValueBound);
		// System.out.println("upperOutlierBound: " + upperOutlierBound);
		// System.out.println("q3: " + q3);
		// System.out.println("q1: " + q1);
		// System.out.println("lowerOutlierBound: " + lowerOutlierBound);
		// System.out.println("lowerEvtremeValueBound: "
		// + lowerEvtremeValueBound);
		// System.out.println(MetheUtilities.standardDeviation(values));

		for (int n = 0; n < targetData.numInstances(); n++) {
			if (!isAccepted(targetData.instance(n).value(
					CONFIGURATION.REPT_VALUE_INDEX))) {
				// for testing
				// System.out.println("index: " + n);
				// System.out.println(targetData.instance(n).value(
				// PreprocessConfiguration.IQR_REPT_VALUE));
				targetData.instance(n).setValue(CONFIGURATION.REPT_VALUE_INDEX,
						replace(n, originalData));
			}
		}
		return targetData;
	}

	/**
	 * calculate the essential parameters for instances IQR.
	 * 
	 * @param values
	 *            the array on which calculation performed.
	 */
	protected void calculateParameters(double[] values) {
		double q1Index = 0.0;
		double q3Index = 0.0;
		double[] sortedValues = null;
		int length = values.length;
		// sort the array so that it will be ordered.
		sortedValues = MetheUtilities.mergeSort(values);
		// System.out.println("----------------");
		// for(double d: sortedValues) {
		// System.out.println(d);
		// }
		// System.out.println("----------------");
		q1Index = length / 4.0;
		q3Index = q1Index * 3.0;
		// for testing
		// System.out.println("Length: " + length);
		// System.out.println("q1Index: " + q1Index);
		// System.out.println("q3Index: " + q3Index);
		// if q1 and q3 are not integers
		// do a linear interolation to calculate the appropriate value.
		q1 = MetheUtilities.linearInterpolation(sortedValues, q1Index);
		q3 = MetheUtilities.linearInterpolation(sortedValues, q3Index);
		// make sure that q3 is the bigger one.
		if (q3 < q1) {
			double temp = q3;
			q3 = q1;
			q1 = temp;
		}
		iqr = q3 - q1;
		upperExtremeValueBound = q3 + CONFIGURATION.IQRMETHOD_EVF * iqr;
		upperOutlierBound = q3 + CONFIGURATION.IQRMETHOD_OF * iqr;
		lowerOutlierBound = q1 - CONFIGURATION.IQRMETHOD_OF * iqr;
		lowerEvtremeValueBound = q1 - CONFIGURATION.IQRMETHOD_EVF * iqr;
	}

	/**
	 * determine whether a value is outlier.
	 * 
	 * @param value
	 *            the value will be determined.
	 * @return Yes:true
	 */
	protected boolean isOutlier(double value) {
		boolean result = (value <= upperExtremeValueBound && value > upperOutlierBound)
				|| (value >= lowerEvtremeValueBound)
				&& (value < lowerOutlierBound);
		return result;
	}

	/**
	 * determine whether a value is extreme value.
	 * 
	 * @param value
	 *            the value will be determined.
	 * @return Yes:true
	 */
	protected boolean isExtreme(double value) {
		boolean result = value > upperExtremeValueBound
				|| value < lowerEvtremeValueBound;
		return result;
	}

	/**
	 * determine whether a value can be accepted. (not a outlier, nor a extreme
	 * value)
	 * 
	 * @param value
	 *            the value will be determined.
	 * @return Yes:true
	 */
	protected boolean isAccepted(double value) {
		boolean result = (value >= lowerOutlierBound && value <= upperOutlierBound);
		// for testing
		// if(!result)
		// System.out.println("lowerOutlierBound: " + lowerOutlierBound
		// + " value: " + value
		// +" upperOutlierBound: " + upperOutlierBound);
		return result;
	}

	/**
	 * replace the anomaly instance with a appropriate value.
	 * 
	 * @param index
	 *            the index of the instance;
	 * @param instances
	 *            the instance which can not accepted.
	 * @return a appropriate value that can replace the anomaly one.
	 */
	protected double replace(int index, Instances instances) {
		if (index == 0) {
			return instances.instance(index + 1).value(
					CONFIGURATION.REPT_VALUE_INDEX) < upperOutlierBound ? instances
					.instance(index + 1).value(CONFIGURATION.REPT_VALUE_INDEX)
					: upperOutlierBound;
		} else if (index == instances.numInstances() - 1) {
			return instances.instance(index - 2).value(
					CONFIGURATION.REPT_VALUE_INDEX) < upperOutlierBound ? instances
					.instance(index - 2).value(CONFIGURATION.REPT_VALUE_INDEX)
					: upperOutlierBound;
		} else {
			double result = 0.0;
			result = (instances.instance(index - 1).value(
					CONFIGURATION.REPT_VALUE_INDEX) + instances.instance(
					index + 1).value(CONFIGURATION.REPT_VALUE_INDEX)) / 2.0;
			return result < upperOutlierBound ? result : upperOutlierBound;
		}
	}
}

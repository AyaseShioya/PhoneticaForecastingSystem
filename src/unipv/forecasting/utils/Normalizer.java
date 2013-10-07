/**
 * 
 */
package unipv.forecasting.utils;

import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class Normalizer {

	private double[] minArray;
	private double[] maxArray;
	private boolean isInitialized;

	public Normalizer() {
		this.isInitialized = false;
	}

	public Instances normalize(Instances data) {
		if (!isInitialized) {
			initialize(data);
		}
		Instances result = data;
		for (int i = 0; i < data.numInstances(); i++) {
			if (normalize(data.get(i)) == null) {
				result = null;
				break;
			}
		}
		return result;
	}

	public Instance normalize(Instance data) {
		Instance result = data;
		if ((data.numAttributes() == minArray.length)
				&& (data.numAttributes() == maxArray.length)) {
			for (int i = 0; i < data.numAttributes(); i++) {
				data.setValue(i,
						convert(minArray[i], maxArray[i], data.value(i)));
			}
		} else {
			result = null;
		}
		return result;
	}

	public Instances denormalize(Instances data) {
		if (!isInitialized) {
			initialize(data);
		}
		Instances result = data;
		for (int i = 0; i < data.numInstances(); i++) {
			if (denormalize(data.get(i)) == null) {
				result = null;
				break;
			}
		}
		return result;
	}

	public Instance denormalize(Instance data) {
		Instance result = data;
		if ((data.numAttributes() == minArray.length)
				&& (data.numAttributes() == maxArray.length)) {
			for (int i = 0; i < data.numAttributes(); i++) {
				data.setValue(i,
						deconvert(minArray[i], maxArray[i], data.value(i)));
			}
		} else {
			result = null;
		}
		return result;
	}

	public void initialize(final Instances input) {
		minArray = new double[input.numAttributes()];
		maxArray = new double[input.numAttributes()];
		for (int i = 0; i < input.numAttributes(); i++) {
			minArray[i] = Double.MAX_VALUE;
			maxArray[i] = Double.MIN_VALUE;
		}

		for (int i = 0; i < input.numInstances(); i++) {
			double[] value = input.instance(i).toDoubleArray();
			for (int j = 0; j < input.numAttributes(); j++) {
				if (minArray[j] > value[j]) {
					minArray[j] = value[j];
				}
				if (maxArray[j] < value[j]) {
					maxArray[j] = value[j];
				}
			}
		}
		this.isInitialized = true;
	}

	public void initialize(final double[] minArray, final double[] maxArray) {
		this.minArray = minArray;
		this.maxArray = maxArray;
		this.isInitialized = true;
	}

	private double convert(final double min, final double max,
			final double original) {
		double result = 0;
		if (max != min)
			result = (original - min) / (max - min);
		else
			result = original / min;
		return result;
	}

	private double deconvert(final double min, final double max,
			final double original) {
		double result = 0;
		if (max != min)
			result = (original * (max - min)) + min;
		else
			result = original * min;
		return result;
	}

	/**
	 * @return the minArray
	 */
	public double[] getMinArray() {
		return minArray;
	}

	/**
	 * @param minArray
	 *            the minArray to set
	 */
	public void setMinArray(double[] minArray) {
		this.minArray = minArray;
	}

	/**
	 * @return the maxArray
	 */
	public double[] getMaxArray() {
		return maxArray;
	}

	/**
	 * @param maxArray
	 *            the maxArray to set
	 */
	public void setMaxArray(double[] maxArray) {
		this.maxArray = maxArray;
	}

	/**
	 * @return the isInitialized
	 */
	public boolean isInitialized() {
		return isInitialized;
	}

	/**
	 * @param isInitialized
	 *            the isInitialized to set
	 */
	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	public Normalizer copy() {
		Normalizer copy = new Normalizer();
		copy.setMinArray(minArray.clone());
		copy.setMaxArray(maxArray.clone());
		copy.setInitialized(isInitialized);
		return copy;
	}

}

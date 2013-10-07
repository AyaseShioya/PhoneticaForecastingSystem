/**
 * The package used for filtering zero.
 */
package unipv.forecasting.preprocess.filters;

import unipv.forecasting.CONFIGURATION;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class EliminateZero implements Filter {

	private double EPSINON = 0.000001;
	private int cycle = CONFIGURATION.TIME_PERIOD;
	private double[] average;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.preprocess.filters.Filter#doFilter(weka.core.Instances)
	 */
	@Override
	public Instances doFilter(Instances originalData) {
		Instances targetData = originalData;
		// System.out.println(originalData.numInstances());
		init(targetData);
		// for(int i = 0; i < targetData.numInstances(); i++) {
		// if(targetData.get(i).value(CONFIGURATION.REPT_VALUE_INDEX) < EPSINON)
		// {
		// targetData.get(i).setValue(CONFIGURATION.REPT_VALUE_INDEX, average[i
		// % cycle]);
		// }
		// }
		return targetData;
	}

	private void init(final Instances data) {
		average = new double[cycle];
		int[] count = new int[cycle];
		for (int i = 0; i < cycle; i++) {
			average[i] = 0.0;
			count[i] = 0;
		}
		double[] value = data
				.attributeToDoubleArray(CONFIGURATION.REPT_VALUE_INDEX);
		boolean allZero = true;
		boolean isRemoved = false;

		int numNotZero = 0;
		int numRemoved = 0;
		/** outer loop for traversal data **/
		for (int i = 0; i < value.length; i += cycle) {
			numNotZero = 0;
			allZero = true;
			if (isRemoved) {
				numRemoved += cycle;
				isRemoved = false;
			}
			/** inner loop for traversal cycle **/
			// System.out.println("i:" + i);
			for (int j = 0; (j < cycle) && ((i + j) < value.length); j++) {
				// System.out.println(data.get(i - numRemoved + j));
				if (value[i + j] > EPSINON) {
					average[j] += value[i + j];
					count[j] += 1;
					numNotZero++;
					// System.out.println("NOT ZERO!!");
					// System.out.println(i + j + ":" + value[i + j]);
				}
				// System.out.println("j:" + j);
			}
			allZero = numNotZero < (cycle / 2);
			// System.out.println(allZero);
			// System.out.println("----------------------------------------------------------------------------------");
			// for(int k = 0; k < cycle; k++) {
			// System.out.println("i:" + data.get(i - numRemoved + k).value(1));
			// }

			// System.out.println(data.numInstances());
			// System.out.println("allZero:" + allZero);
			if (allZero && data.numInstances() > 0) {
				for (int k = 0; k < cycle; k++) {
					// System.out.println((i - numRemoved) + ":" + data.get(i -
					// numRemoved + k).value(1));
					data.remove(i - numRemoved);
				}
				isRemoved = true;
			}
		}

		for (int i = 0; i < cycle; i++) {
			if (count[i] != 0) {
				average[i] /= count[i];
				// System.out.println(average[i]);
			}
		}

	}

}

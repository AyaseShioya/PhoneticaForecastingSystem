/**
 * Mathematical utilities
 */
package unipv.forecasting.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import unipv.forecasting.CONFIGURATION;
import weka.core.Utils;

/**
 * @author Quest
 * 
 */
public class MetheUtilities {

	/**
	 * @param values
	 *            Used to calculate the sample value using linear interpolation.
	 * @param index
	 *            The index of this value.
	 * @return
	 */
	static public double linearInterpolation(final double[] values,
			final double index) {
		int predecessor = (int) index;
		int successor = (int) index + 1;
		double result = values[successor] - (successor - index)
				* (values[successor] - values[predecessor]);
		return result;
	}

	static public double standardDeviation(final double[] values) {
		return Math.sqrt(Utils.variance(values));
	}

	/**
	 * translate index number into a date.
	 * 
	 * @param now
	 *            the index of the time.
	 * @param start
	 *            the time of the first instance of training sample.
	 * @return time in millis.
	 */
	static public Double translateIndexToDate_hourly(final int now,
			final Double start) {
		Long begin = start.longValue();
		Calendar translator = new GregorianCalendar();
		translator.setTimeInMillis(start.longValue());
		if (now > 0) {
			int index = now - 1;
			// day of week of the start time mapped as Mon:0 .....
			long dayStart = translator.get(Calendar.DAY_OF_WEEK) - 2;
			// used to calculate weekends past.
			long day = index / CONFIGURATION.TIME_PERIOD + dayStart;
			// how many weekends past.
			long weekends = day / CONFIGURATION.TIME_WORKDAY_PERIOD;
			// the hour indicator of now.
			long hours = index % CONFIGURATION.TIME_PERIOD;
			// how many days past.
			long days = index / CONFIGURATION.TIME_PERIOD;
			// add intervals.
//			System.out.println("weeks:" + weekends + ",days:" + days + ",hours:" + hours);
			begin += weekends * 2 * 24 * 60 * 60 * 1000;
			begin += days * 24 * 60 * 60 * 1000;
			begin += hours * 60 * 60 * 1000;
		} else {
			int index = now;
			// day of week of the start time mapped as Mon:0 .....
			long dayStart = translator.get(Calendar.DAY_OF_WEEK) - 2;
			// used to calculate weekends past.
			long day = index / CONFIGURATION.TIME_PERIOD + dayStart;
			// how many weekends past.
			long weekends = day / CONFIGURATION.TIME_WORKDAY_PERIOD;
			// the hour indicator of now.
			long hours = index % CONFIGURATION.TIME_PERIOD;
			// how many days past.
			long days = index / CONFIGURATION.TIME_PERIOD;
			// add intervals.

			begin += weekends * 2 * 24 * 60 * 60 * 1000;
			begin += days * 24 * 60 * 60 * 1000;
			begin += hours * 60 * 60 * 1000;
		}

		return begin.doubleValue();
	}

	/**
	 * translate date into time index.
	 * 
	 * @param now
	 *            the time will be translated.
	 * @param start
	 *            the time of the first instance of training sample.
	 * @return the index of the time.
	 */
	static public int translateDateToIndex(final Double now, final Double start) {
		Long interval = now.longValue() - start.longValue();

		Calendar translator = new GregorianCalendar();
//		translator.setTimeInMillis(now.longValue());
//		System.out.println(translator.getTime());
		translator.setTimeInMillis(start.longValue());

		// day of week of the start time mapped as Mon:0 .....
		long dayStart = translator.get(Calendar.DAY_OF_WEEK) - 2;

		// #weeks pasted.
		long weeks = (dayStart * 24 * 60 * 60 * 1000 + interval)
				/ (7 * 24 * 60 * 60 * 1000);
		// #days pasted.
		long days = interval / (24 * 60 * 60 * 1000);
		// the hour of now.
		long hours = (interval % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
//		System.out.println(hours);
		// add intervals.
		int index = 0;
		index += days * CONFIGURATION.TIME_PERIOD;
		index -= weeks * 2 * CONFIGURATION.TIME_PERIOD;
		index += hours;
		if (interval >= 0)
			index += 1;

		return index;
	}

	/**
	 * calculate MSE by two given sets of data.
	 * 
	 * @param actuals
	 *            the actual data of training sample.
	 * @param predictions
	 *            the predictions.
	 * @return MSE
	 */
	static public double calculateMSE(final double[] actuals,
			final double[] predictions) {
		double[] a = actuals.clone();
		double[] p = predictions.clone();
		int i = 0;
		// calculate difference between actuals and predictions
		for (double pred : p) {
			a[i++] -= pred;
		}
		double sum = 0.0;
		// calculate sum of squares.
		for (double act : a) {
			sum += act * act;
			// test
			// System.out.println("sum: " + sum + ", act: " + act);
		}
		sum /= i;
		return sum;
	}

	/**
	 * Calculate relative standard deviation of two given sets of data.
	 * 
	 * @param actuals
	 *            the actual data o training sample.
	 * @param predictions
	 *            the predictions
	 * @return RSD
	 */
	static public double calculateRSD(final double[] actuals,
			final double[] predictions) {
		double mse = calculateMSE(actuals, predictions);
		double mean = 0.0;
		for (int i = 0; i < actuals.length; i++) {
			mean += actuals[i];
		}
		mean /= actuals.length;
		return Math.sqrt(mse) / mean * 100.0;
	}

	static public double calculateRelativeError(final double[] actuals,
			final double[] predictions) {

		double sum = 0.0;
		double error = 0.0;
		for (int i = 0; i < actuals.length; i++) {
			sum += actuals[i];
			error += Math.abs(actuals[i] - predictions[i]);
			// System.out.println(actuals[i] + "," + predictions[i]);
		}
		// System.out.println(error);
		return error / sum * 100;
	}

	// public static double[] mergeSort(double[] oList) {
	// if (oList.length == 1) {
	// return oList;
	// } else {
	// double[] listL = new double[oList.length / 2];
	// double[] listR = new double[oList.length - oList.length / 2];
	// int center = oList.length / 2;
	// for (int i = 0; i < center; i++) {
	// listL[i] = oList[i];
	// }
	// for (int i = center, j = 0; i < oList.length; i++, j++) {
	// listR[j] = oList[i];
	// }
	//
	// double[] sortedListL = mergeSort(listL);
	// double[] sortedListR = mergeSort(listR);
	// double[] sortedlist = mergeTwoList(sortedListL, sortedListR);
	// return sortedlist;
	// }
	// }
	//
	// public static double[] mergeTwoList(double[] listL, double[] listR) {
	// int i = 0, j = 0;
	// double[] sortedList = new double[listL.length + listR.length];
	// int foot = 0;
	// while (i < listL.length && j < listR.length) {
	// if (listL[i] <= listR[j]) {
	// sortedList[foot] = listL[i];
	// i++;
	// } else {
	// sortedList[foot] = listR[j];
	// j++;
	// }
	// foot++;
	// }
	//
	// if (i == listL.length) {
	// while (j < listR.length) {
	// sortedList[foot++] = listR[j++];
	// }
	// } else { // j==listR.length
	// while (i < listL.length) {
	// sortedList[foot++] = listL[i++];
	// }
	// }
	// return sortedList;
	// }

	public static double[] mergeSort(double[] list) {
		int beforeLen;
		int afterLen = 1;

		for (beforeLen = 1; afterLen < list.length; beforeLen = afterLen) {
			int i = 0;
			afterLen = 2 * beforeLen;

			while (i + afterLen < list.length) {
				merge(list, i, i + beforeLen - 1, i + afterLen - 1, afterLen);
				i += afterLen;
			}

			if (i + beforeLen < list.length)
				merge(list, i, i + beforeLen - 1, list.length - 1, list.length);
		}
		return list;
	}

	public static void merge(double[] list, int left, int center, int right,
			int n) {
		double[] sortedList = new double[n];
		int i = left;
		int j = center + 1;
		int k = 0;

		while (i <= center && j <= right) {
			if (list[i] <= list[j])
				sortedList[k++] = list[i++];
			else
				sortedList[k++] = list[j++];
		}

		if (i == center + 1) {
			while (j <= right)
				sortedList[k++] = list[j++];
		} else {
			while (i <= center)
				sortedList[k++] = list[i++];
		}

		for (i = left, k = 0; i <= right; i++, k++)
			list[i] = sortedList[k];

		sortedList = null;
	}

	public static void main(String[] args) throws Exception {
		TimeZone tz = TimeZone.getTimeZone("CET");
		Calendar c = Calendar.getInstance();
		c.setTimeZone(tz);
		c.setTimeInMillis(1317967200000l);
//		System.out.println(c.getTime());
		for(int i = 100; i < 200; i++) {
			Double n = translateIndexToDate_hourly(i,1317967200000d);
			c.setTimeInMillis(n.longValue());
//			System.out.println(c.getTime() + "," + Calendar.DST_OFFSET);
//			if(i != translateDateToIndex(n,1317967200000d))
//				System.out.println(i);
//			if(c.get(Calendar.HOUR_OF_DAY) == 7)
//				System.out.println(i);
		}
		System.out.println("fi");
	}
}

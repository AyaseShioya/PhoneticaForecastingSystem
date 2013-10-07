/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.lag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

import unipv.forecasting.CONFIGURATION;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;

/**
 * @author Quest
 * 
 */
public class BasicLagger implements Lagger {

	private int minLag;
	private int maxLag;
	private Instances data;
	private Calendar beginCalendar;
	private boolean customized = false;

	public BasicLagger(final int minLag, final int maxLag) {
		this.minLag = minLag;
		this.maxLag = maxLag;
	}

	public BasicLagger(final int minLag, final int maxLag, Double millis) {
		this.minLag = minLag;
		this.maxLag = maxLag;
		beginCalendar = new GregorianCalendar();
		beginCalendar.setTimeInMillis(millis.longValue());
		customized = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.LagCreator#createLag(
	 * weka.core.Instances)
	 */
	@Override
	public Instances createLag() {
		createNormalLag();
		createAverageLag();
		eliminateNull();
		// System.out.println(beginCalendar.getTime());
		return data;
	}

	/**
	 * create normal lag according to the minLag and maxLag.
	 */
	private void createNormalLag() {
		// indexes for additional attributes.
		int[] indexes = new int[getLagLength()];
		// first create the attributes
		for (int i = minLag; i <= maxLag; i++) {
			// attribute name
			String label = "lag_" + i;
			Add a = new Add();
			a.setAttributeName(label);
			try {
				a.setInputFormat(data);
				// add new attribute.
				data = Filter.useFilter(data, a);
				// save the index of the additional attributes.
				indexes[i - minLag] = data.attribute(label).index();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			a = null;
		}
		// outer loop for traversing instances.
		for (int i = 2 * maxLag; i < data.numInstances(); i++) {
			// cache for lag.
			double lag = 0.0;
			// inner loop for traversing lags.
			for (int j = minLag; j <= maxLag; j++) {
				// retrieve the specific lag value.
				lag = data.instance(i - j)
						.value(CONFIGURATION.REPT_VALUE_INDEX);
				// fill the lag.
				data.get(i).setValue(indexes[j - minLag], lag);
			}
		}
	}

	/**
	 * create an average lags from maxLag+1 to 2*maxLag.
	 */
	private void createAverageLag() {
		// calculate average after normal lag
		// start from maxLag + 1 and end at 2 * maxLag.
		int from = maxLag + 1;
		int to = 2 * maxLag;
		// the index for AVG attribute.
		int index = 0;
		// add a AVG attribute.
		Add a = new Add();
		a.setAttributeName("avg");
		try {
			a.setInputFormat(data);
			// add new attribute.
			data = Filter.useFilter(data, a);
			// save the index of the additional attributes.
			index = data.attribute("avg").index();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		a = null;
		// summation and average for the value of instances.
		double sum = 0.0, avg = 0.0;
		int length = to - from + 1;
		// outer loop for traversing instances
		for (int i = to; i < data.numInstances(); i++) {
			// inner loop for calculating average.
			for (int j = from; j <= to; j++) {
				// TODO
				sum += data.get(i - j).value(CONFIGURATION.REPT_VALUE_INDEX);
			}
			avg = sum / length;
			data.get(i).setValue(index, avg);
			sum = 0.0;
			avg = 0.0;
		}
	}

	private void eliminateNull() {
		for (int i = 0; i < 2 * maxLag; i++) {
			data.remove(0);
		}
		if (!customized) {
			Double begin = data.get(0).value(CONFIGURATION.REPT_DATE_INDEX);
			// set beginCalendar.
			beginCalendar.setTimeInMillis(begin.longValue());
			// System.out.println(beginCalendar.getTime());
			customized = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.Lagger#getLagLength()
	 */
	@Override
	public int getLagLength() {
		return maxLag - minLag + 1;
	}

	/**
	 * @return the minLag
	 */
	public int getMinLag() {
		return minLag;
	}

	/**
	 * @param minLag
	 *            the minLag to set
	 */
	public void setMinLag(int minLag) {
		this.minLag = minLag;
	}

	/**
	 * @return the maxLag
	 */
	public int getMaxLag() {
		return maxLag;
	}

	/**
	 * @param maxLag
	 *            the maxLag to set
	 */
	public void setMaxLag(int maxLag) {
		this.maxLag = maxLag;
	}

	/**
	 * @return the data
	 */
	public Instances getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void inputData(Instances data) {
		// set data
		this.data = data;
		if (!customized) {
			Double begin = data.get(0).value(CONFIGURATION.REPT_DATE_INDEX);
			// set beginCalendar.
			beginCalendar = new GregorianCalendar();
			beginCalendar.setTimeInMillis(begin.longValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.Lagger#getBeginDate()
	 */
	@Override
	public Calendar getBeginDate() {
		return beginCalendar;
	}

	public void setBeginDate(Calendar beginCalendar) {
		this.beginCalendar = beginCalendar;
		customized = true;
	}

	public static void main(String[] args) throws Exception {
		String pathToWineData = weka.core.WekaPackageManager.PACKAGES_DIR
				.toString()
				+ File.separator
				+ "timeseriesForecasting"
				+ File.separator + "sample-data" + File.separator + "TEST.arff";
		Instances data = new Instances(new BufferedReader(new FileReader(
				pathToWineData)));
		int min = 1, max = 12;
		Lagger lagger = new BasicLagger(min, max);
		lagger.inputData(data);
		Instances newData = lagger.createLag();
		boolean flag = true;
		for (int index = 2 * max; index < data.numInstances(); index++) {
			double avg = newData.get(index).value(14);
			double sum = 0.0;
			for (int i = 13; i <= 24; i++) {
				sum += newData.get(index - i).value(1);
			}
			sum /= 12.0;
			flag &= Math.abs(avg - sum) < 0.0000001;
		}
		System.out.println(flag);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.lag.Lagger#copy()
	 */
	@Override
	public Lagger copy() {
		BasicLagger newLagger = new BasicLagger(minLag, maxLag);
		if (customized)
			newLagger.setBeginDate(beginCalendar);
		return newLagger;
	}

}

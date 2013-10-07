package unipv.forecasting.forecaster.modelselection.lag;

import java.util.Calendar;
import java.util.GregorianCalendar;

import unipv.forecasting.CONFIGURATION;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;

public class DayIndex extends LagDecorator {
	// The first time stamp of data.
	private int firstDay;

	public DayIndex(Lagger lagger) {
		super(lagger);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.LagDecorator#createLag()
	 */
	@Override
	public Instances createLag() {
		Instances data = super.createLag();
		Add a = new Add();
		a.setAttributeName("day");
		try {
			a.setInputFormat(data);
			data = Filter.useFilter(data, a);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		a = null;
		int index = data.attribute("day").index();

		// set the values for each instance in the data
		for (int i = 0; i < data.numInstances(); i++) {
			// Monday:0,.... weekend should not exists in these instances
			// devided by 8 is 12 is because the hour is composed into
			// 12:8...18, and
			// the summation of 0-7,19-23.
			data.get(i).setValue(
					index,
					(i / lagger.getLagLength() + firstDay)
							% CONFIGURATION.TIME_WORKDAY_PERIOD);
		}

		Calendar calendar = new GregorianCalendar();
		a = new Add();
		a.setAttributeName("month");
		try {
			a.setInputFormat(data);
			data = Filter.useFilter(data, a);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		a = null;
		index = data.attribute("month").index();

		for (int i = 0; i < data.numInstances(); i++) {
			// Monday:0,.... weekend should not exists in these instances
			// devided by 8 is 12 is because the hour is composed into
			// 12:8...18, and
			// the summation of 0-7,19-23.
			Double time = data.get(i).value(CONFIGURATION.REPT_DATE_INDEX);
			// set beginCalendar.
			calendar.setTimeInMillis(time.longValue());
			data.get(i).setValue(index, calendar.get(Calendar.MONTH));
			// System.out.println(data.get(i).value(index));
		}

		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.LagDecorator#getLagLength
	 * ()
	 */
	@Override
	public int getLagLength() {
		return super.getLagLength();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.LagDecorator#setMinLag
	 * (int)
	 */
	@Override
	public void setMinLag(int minLag) {
		super.setMinLag(minLag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.LagDecorator#setMaxLag
	 * (int)
	 */
	@Override
	public void setMaxLag(int maxLag) {
		super.setMaxLag(maxLag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.LagDecorator#setData(
	 * weka.core.Instances)
	 */
	@Override
	public void inputData(Instances data) {
		super.inputData(data);
		// get the first hour in the instances.
		this.firstDay = super.getBeginDate().get(Calendar.DAY_OF_WEEK);
		// System.out.println(super.getBeginDate().getTime());
		// System.out.println(firstDay);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.Lagger#getBeginDate()
	 */
	@Override
	public Calendar getBeginDate() {
		return super.getBeginDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.lag.LagDecorator#copy()
	 */
	@Override
	public Lagger copy() {
		Lagger newLagger = new DayIndex(super.copy());
		return newLagger;
	}

}

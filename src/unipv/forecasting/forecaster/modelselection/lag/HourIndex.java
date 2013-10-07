/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.lag;

import java.util.Calendar;

import unipv.forecasting.CONFIGURATION;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;

/**
 * @author Quest
 * 
 */
public class HourIndex extends LagDecorator {

	private int thisHour;

	public HourIndex(Lagger lagger) {
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
		a.setAttributeName("hour");
		try {
			a.setInputFormat(data);
			data = Filter.useFilter(data, a);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		a = null;
		// index for hour
		int index = data.attribute("hour").index();

		// set the values for each instance in the data
		for (int i = 0; i < data.numInstances(); i++) {
			data.get(i).setValue(
					index,
					(i + thisHour - CONFIGURATION.TIME_START_WORKING)
							% lagger.getLagLength());
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
		this.thisHour = super.getBeginDate().get(Calendar.HOUR_OF_DAY);
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
		Lagger newLagger = new HourIndex(super.copy());
		return newLagger;
	}

}

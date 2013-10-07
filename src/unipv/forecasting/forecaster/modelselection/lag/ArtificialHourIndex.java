/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.lag;

import java.util.Calendar;

import unipv.forecasting.utils.MetheUtilities;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.RemoveType;

/**
 * @author Quest
 * 
 */
public class ArtificialHourIndex extends LagDecorator {

	public ArtificialHourIndex(Lagger lagger) {
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
		int minLag = super.getMinLag();
		int maxLag = super.getMaxLag();
		int index = 0;
		int index2 = 0;
		int index3 = 0;
		// indexes for time*lag
		int[] indexes = new int[super.getLagLength()];
		// indexes for time^2*lag
		int[] indexes2 = new int[super.getLagLength()];
		// indexes for original lag.
		int[] oindexes = new int[super.getLagLength()];
		String label;

		Instances data = super.createLag();
		// initialize oindexes.
		for (int j = minLag; j <= maxLag; j++) {
			label = "lag_" + j;
			oindexes[j - minLag] = data.attribute(label).index();
		}
		// create a series of index that map the date attribute to them.
		// try {
		// AddID a = new AddID();
		// a.setAttributeName("time");
		// a.setIDIndex("first");
		// a.setInputFormat(data);
		// data = Filter.useFilter(data, a);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		try {
			label = "time";
			Add a = new Add();
			a.setAttributeIndex("first");
			a.setAttributeName(label);
			a.setInputFormat(data);
			// add new attribute.
			data = Filter.useFilter(data, a);
			// save the index of the additional attributes.
			index = data.attribute(label).index();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// prime values.
		// outer loop for traversing instances.
		for (int i = 0; i < data.numInstances(); i++) {
			double now = data.get(i).value(1);
			double start = super.getBeginDate().getTimeInMillis();
			data.get(i).setValue(index,
					MetheUtilities.translateDateToIndex(now, start));
		}

		// remove date attribute.
		RemoveType r = new RemoveType();
		try {
			r.setOptions(new String[] { "-T", "date" });
			r.setInputFormat(data);
			data = Filter.useFilter(data, r);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			r = null;
		}

		// add time^2 attribute.
		try {
			label = "time^2";
			Add a = new Add();
			a.setAttributeName(label);
			a.setInputFormat(data);
			// add new attribute.
			data = Filter.useFilter(data, a);
			// save the index of the additional attributes.
			index2 = data.attribute(label).index();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// prime values.
		// outer loop for traversing instances.
		for (int i = 0; i < data.numInstances(); i++) {
			double time = data.get(i).value(0);
			if (time >= 0) {
				data.get(i).setValue(index2, time * time);
			} else {
				data.get(i).setValue(index2, -time * time);
			}
		}

		// add time^3 attribute.
		try {
			label = "time^3";
			Add a = new Add();
			a.setAttributeName(label);
			a.setInputFormat(data);
			// add new attribute.
			data = Filter.useFilter(data, a);
			index3 = data.attribute(label).index();
			// save the index of the additional attributes.
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// prime values.
		// outer loop for traversing instances.
		for (int i = 0; i < data.numInstances(); i++) {
			double time = data.get(i).value(0);
			data.get(i).setValue(index3, time * time * time);
		}

		// add time*lag_i attribute.
		for (int i = minLag; i <= maxLag; i++) {
			// attribute name
			label = "time*lag_" + i;
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
		// prime values.
		// outer loop for traversing instances.
		double lag, time, tLag;
		for (int i = 0; i < data.numInstances(); i++) {
			// cache for lag.
			lag = 0.0;
			// inner loop for traversing lags.
			for (int j = minLag; j <= maxLag; j++) {
				time = data.get(i).value(0);
				lag = data.instance(i).value(oindexes[j - minLag]);
				tLag = time * lag;
				// fill the lag.
				data.get(i).setValue(indexes[j - minLag], tLag);
			}
		}
		// add time^2*lag_i attribute.
		for (int i = minLag; i <= maxLag; i++) {
			// attribute name
			label = "time^2*lag_" + i;
			Add a = new Add();
			a.setAttributeName(label);
			try {
				a.setInputFormat(data);
				// add new attribute.
				data = Filter.useFilter(data, a);
				// save the index of the additional attributes.
				indexes2[i - minLag] = data.attribute(label).index();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			a = null;
		}
		// prime values.
		// outer loop for traversing instances.
		for (int i = 0; i < data.numInstances(); i++) {
			// cache for lag.
			lag = 0.0;
			// inner loop for traversing lags.
			for (int j = minLag; j <= maxLag; j++) {
				time = data.get(i).value(index2);
				lag = data.instance(i).value(oindexes[j - minLag]);
				tLag = time * lag;
				// fill the lag.
				data.get(i).setValue(indexes2[j - minLag], tLag);
			}
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
		Lagger newLagger = new ArtificialHourIndex(super.copy());
		return newLagger;
	}

}

/**
 * An abstract class for decorate Lags.
 */
package unipv.forecasting.forecaster.modelselection.lag;

import java.util.Calendar;

import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public abstract class LagDecorator implements Lagger {
	// The lagger will be decorated.
	protected Lagger lagger;

	LagDecorator(Lagger lagger) {
		this.lagger = lagger;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.lag.Lagger#createLag()
	 */
	@Override
	public Instances createLag() {
		return lagger.createLag();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.Lagger#getLagLength()
	 */
	@Override
	public int getLagLength() {
		return lagger.getLagLength();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.Lagger#setMinLag(int)
	 */
	@Override
	public void setMinLag(int minLag) {
		lagger.setMinLag(minLag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.Lagger#setMaxLag(int)
	 */
	@Override
	public void setMaxLag(int maxLag) {
		lagger.setMaxLag(maxLag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.Lagger#setData(weka.core
	 * .Instances)
	 */
	@Override
	public void inputData(Instances data) {
		lagger.inputData(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.lag.Lagger#getBeginDate()
	 */
	@Override
	public Calendar getBeginDate() {
		return lagger.getBeginDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.lag.Lagger#getMinLag()
	 */
	@Override
	public int getMinLag() {
		return lagger.getMinLag();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.lag.Lagger#getMaxLag()
	 */
	@Override
	public int getMaxLag() {
		return lagger.getMaxLag();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.lag.Lagger#copy()
	 */
	@Override
	public Lagger copy() {
		return lagger.copy();
	}
}

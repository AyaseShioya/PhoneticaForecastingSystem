package unipv.forecasting.preprocess.filters;

import java.util.ArrayList;

public class Value {
	private double value;
	/** average pathc lendth **/
	private ArrayList<Double> pls;
	private ArrayList<Double> sps;
	public Value(double value) {
		this.value = value;
		pls = new ArrayList<Double>();
		sps = new ArrayList<Double>();
	}
		
	public void addPL(double pl) {
//		System.out.println("PL: " + pl + ",VALUE: " + value);
		pls.add(pl);
	}
	
	public void addSP(double sp) {
		sps.add(sp);
	}
	
	public double getAPL() {
		return calculateMean(pls);
	}
	
	public double getNewValue() {
		return calculateMean(sps);
	}
	
	public double calculateMean(ArrayList<Double> list) {
		double mean = 0;
		for(Double value : list) {
			mean += value;
		}
		mean /= list.size();		

		return mean;
	}

	/**
	 * @return the pls
	 */
	public ArrayList<Double> getPLS() {
		return pls;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
	
	
}

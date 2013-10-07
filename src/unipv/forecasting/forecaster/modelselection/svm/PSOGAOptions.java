/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm;

import unipv.forecasting.forecaster.modelselection.Context;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class PSOGAOptions {
	private Instances data;
	private Context system;
	private SVMConfiguration configuration;

	private int maxGeneration;
	private int popSize;

	/** g,c,ep **/
	private double[] base;
	private int[] granularity;
	private double[] maxV;
	private double[] increaser;

	private double w;
	private double c1;
	private double c2;

	private double crossoverProbability;
	private int crossoverPropotion;
	private int crossoverTimes;
	private double mutationProbability;
	private int mutationTimes;

	public PSOGAOptions(final Instances data, final Context system,
			final SVMConfiguration configuration, int maxGeneration,
			int popSize, double w, double c1, double c2,
			double crossoverProbability, double mutationProbability,
			int crossoverTimes, int mutationTimes, int crossoverPropotion,
			int[] granularity, double[] base, double[] scope) {
		this.data = data;
		this.system = system;
		this.configuration = configuration;
		this.maxGeneration = maxGeneration;
		this.popSize = popSize;
		this.w = w;
		this.c1 = c1;
		this.c2 = c2;
		this.crossoverProbability = crossoverProbability;
		this.mutationProbability = mutationProbability;
		this.crossoverTimes = crossoverTimes;
		this.mutationTimes = mutationTimes;
		this.granularity = granularity;
		this.base = base;
		this.crossoverPropotion = crossoverPropotion;

		maxV = new double[base.length];
		increaser = new double[base.length];
		for (int i = 0; i < granularity.length; i++) {
			if (scope[i] > 1) {
				scope[i] = 1;
			}

			// maxV[i] = (granularity[i] + 0.0) / maxGeneration / popSize * 2.0;
			maxV[i] = granularity[i] / 10;
			double max = base[i];
			max += base[i] * scope[i];
			double min = base[i];
			min = min - base[i] * scope[i];
			min = min > 0 ? min : 0;
			// System.out.println((max - min) / granularity[i]);
			increaser[i] = (max - min) / granularity[i];
		}
	}

	public double[] decode(final double code[]) {
		double[] result = new double[3];
		for (int i = 0; i < code.length; i++) {
			result[i] = (code[i] - (granularity[i] / 2)) * increaser[i]
					+ base[i];
		}
		return result;
	}

	public double decode(final double code, final int index) {
		double result = (code - (granularity[index] / 2)) * increaser[index]
				+ base[index];
		return result;
	}

	public double generatorRandomPop(final int index) {
		double result = Math.random() * granularity[index];
		return result;
	}

	public double[] generatorRandomPop() {
		double[] result = new double[3];
		for (int i = 0; i < 3; i++) {
			result[i] = Math.random() * granularity[i];
		}
		return result;
	}

	public double generatorRandomV(final int index) {
		double result = Math.random() * maxV[index];
		return result;
	}

	public double[] generatorRandomV() {
		double[] result = new double[3];
		for (int i = 0; i < 3; i++) {
			result[i] = Math.random() * maxV[i];
		}
		return result;
	}

	/**
	 * @return the maxGeneration
	 */
	public int getMaxGeneration() {
		return maxGeneration;
	}

	/**
	 * @param maxGeneration
	 *            the maxGeneration to set
	 */
	public void setMaxGeneration(int maxGeneration) {
		this.maxGeneration = maxGeneration;
	}

	/**
	 * @return the popSize
	 */
	public int getPopSize() {
		return popSize;
	}

	/**
	 * @param popSize
	 *            the popSize to set
	 */
	public void setPopSize(int popSize) {
		this.popSize = popSize;
	}

	/**
	 * @return the granularity
	 */
	public int[] getGranularity() {
		return granularity;
	}

	/**
	 * @param granularity
	 *            the granularity to set
	 */
	public void setGranularity(int[] granularity) {
		this.granularity = granularity;
	}

	/**
	 * @return the maxV
	 */
	public double[] getMaxV() {
		return maxV;
	}

	/**
	 * @param maxV
	 *            the maxV to set
	 */
	public void setMaxV(double[] maxV) {
		this.maxV = maxV;
	}

	/**
	 * @return the increaser
	 */
	public double[] getIncreaser() {
		return increaser;
	}

	/**
	 * @param increaser
	 *            the increaser to set
	 */
	public void setIncreaser(double[] increaser) {
		this.increaser = increaser;
	}

	/**
	 * @return the w
	 */
	public double getW() {
		return w;
	}

	/**
	 * @param w
	 *            the w to set
	 */
	public void setW(double w) {
		this.w = w;
	}

	/**
	 * @return the c1
	 */
	public double getC1() {
		return c1;
	}

	/**
	 * @param c1
	 *            the c1 to set
	 */
	public void setC1(double c1) {
		this.c1 = c1;
	}

	/**
	 * @return the c2
	 */
	public double getC2() {
		return c2;
	}

	/**
	 * @param c2
	 *            the c2 to set
	 */
	public void setC2(double c2) {
		this.c2 = c2;
	}

	/**
	 * @return the crossoverProbability
	 */
	public double getCrossoverProbability() {
		return crossoverProbability;
	}

	/**
	 * @param crossoverProbability
	 *            the crossoverProbability to set
	 */
	public void setCrossoverProbability(double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	/**
	 * @return the mutationProbability
	 */
	public double getMutationProbability() {
		return mutationProbability;
	}

	/**
	 * @param mutationProbability
	 *            the mutationProbability to set
	 */
	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	/**
	 * @return the base
	 */
	public double[] getBase() {
		return base;
	}

	/**
	 * @param base
	 *            the base to set
	 */
	public void setBase(double[] base) {
		this.base = base;
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
	public void setData(Instances data) {
		this.data = data;
	}

	/**
	 * @return the system
	 */
	public Context getSystem() {
		return system;
	}

	/**
	 * @param system
	 *            the system to set
	 */
	public void setSystem(Context system) {
		this.system = system;
	}

	/**
	 * @return the configuration
	 */
	public SVMConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration
	 *            the configuration to set
	 */
	public void setConfiguration(SVMConfiguration configuration) {
		this.configuration = configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String description = "Max Generation: " + maxGeneration;
		description += " Pop Size: " + popSize;
		description += " Base: [";
		for (int i = 0; i < base.length; i++) {
			description += base[i] + ",";
		}
		description += "] Granularity: [";
		for (int i = 0; i < granularity.length; i++) {
			description += granularity[i] + ",";
		}
		description += "] maxV: [";
		for (int i = 0; i < maxV.length; i++) {
			description += maxV[i] + ",";
		}
		description += "] increaser: [";
		for (int i = 0; i < increaser.length; i++) {
			description += increaser[i] + ",";
		}

		description += "] w: " + w;
		description += " c1: " + c1;
		description += " c2: " + c2;
		description += " crossoverProbability: " + crossoverProbability;
		description += " mutationProbability: " + mutationProbability;
		description += " crossoverTimes: " + crossoverTimes;
		description += " mutationTimes: " + mutationTimes;
		return description;
	}

	/**
	 * @return the crossoverTimes
	 */
	public int getCrossoverTimes() {
		return crossoverTimes;
	}

	/**
	 * @param crossoverTimes
	 *            the crossoverTimes to set
	 */
	public void setCrossoverTimes(int crossoverTimes) {
		this.crossoverTimes = crossoverTimes;
	}

	/**
	 * @return the mutationTimes
	 */
	public int getMutationTimes() {
		return mutationTimes;
	}

	/**
	 * @param mutationTimes
	 *            the mutationTimes to set
	 */
	public void setMutationTimes(int mutationTimes) {
		this.mutationTimes = mutationTimes;
	}

	/**
	 * @return the crossoverPropotion
	 */
	public int getCrossoverPropotion() {
		return crossoverPropotion;
	}

	/**
	 * @param crossoverPropotion
	 *            the crossoverPropotion to set
	 */
	public void setCrossoverPropotion(int crossoverPropotion) {
		this.crossoverPropotion = crossoverPropotion;
	}

}

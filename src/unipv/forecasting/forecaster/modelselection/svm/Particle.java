/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm;

import java.io.IOException;

import unipv.forecasting.CONFIGURATION;
import weka.classifiers.functions.supportVector.RBFKernel;

/**
 * @author Quest
 * 
 */
public class Particle implements Cloneable {
	private PSOGAOptions options;

	/** g,c,ep **/
	private double[] pop;
	private double[] velocity;
	private double fitness;

	public Particle(PSOGAOptions options, double[] pop, double[] velocity) {
		this.options = options;
		this.pop = pop;
		this.velocity = velocity;
	}

	public void migrate(final Particle pBest, final Particle gBest) {
		// update v
		for (int i = 0; i < pop.length; i++) {
			velocity[i] *= options.getW();
			velocity[i] += options.getC1() * Math.random()
					* (pBest.getPop()[i] - pop[i]);
			velocity[i] += options.getC2() * Math.random()
					* (gBest.getPop()[i] - pop[i]);

			if (velocity[i] > options.getMaxV()[i]) {
				velocity[i] = options.getMaxV()[i];
			}
			if (velocity[i] < -options.getMaxV()[i]) {
				velocity[i] = -options.getMaxV()[i];
			}
		}
		// update pop
		for (int i = 0; i < pop.length; i++) {
			pop[i] += velocity[i];
			if (pop[i] > options.getGranularity()[i]) {
				pop[i] = options.getGranularity()[i];
			}
			if (pop[i] < 0) {
				pop[i] = 0;
			}
		}
	}

	public void mutation() {
		for (int i = 0; i < options.getMutationTimes(); i++) {
			int dice = (int) (3 * Math.random());
			pop[dice] = options.generatorRandomPop(dice);
		}
	}

	public Particle[] crossover(Particle spouse) {
		Particle[] children = new Particle[2];
		children[0] = this.clone();
		children[1] = spouse.clone();
		for (int i = 0; i < options.getCrossoverTimes(); i++) {
			int dice = (int) (3 * Math.random());
			double temp = children[0].getPop()[dice];
			children[0].getPop()[dice] = children[1].getPop()[dice];
			children[1].getPop()[dice] = temp;
		}
		return children;
	}

	public void calculateFitness() {
		double result = -Double.MAX_VALUE;
		double[] parameters = options.decode(pop);

		// test
		// fitness = 0;
		// fitness += -Math.pow((parameters[0] - 120), 2);
		// fitness += -Math.pow((parameters[1] - 100), 2);
		// fitness += -Math.pow((parameters[2] - 80), 2);

		((RBFKernel) options.getConfiguration().getKernel())
				.setGamma(parameters[0]);
		options.getConfiguration().setC(parameters[1]);
		options.getConfiguration().setEpsilon(parameters[2]);
		double[] error = { 1000, 0.1 };
		try {
			error = options.getSystem().crossValidation(
					CONFIGURATION.CROSS_VALIDATION_NUMFOLDS, options.getData());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// if((error[1] > CONFIGURATION.SV_LOWER_BOUND) && (error[1] <
		// CONFIGURATION.SV_UPPER_BOUND)) {
		result = 100 / error[0];
		fitness = result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see weka.core.Copyable#copy()
	 */
	@Override
	public Particle clone() {
		Particle clone = new Particle(options, pop.clone(), velocity.clone());
		clone.setFitness(fitness);
		return clone;
	}

	public void copy(Particle original) {
		this.options = original.getOptions();
		this.pop = original.getPop().clone();
		this.velocity = original.getVelocity().clone();
		this.fitness = original.getFitness();
	}

	/**
	 * @return the pop
	 */
	public double[] getPop() {
		return pop;
	}

	/**
	 * @param pop
	 *            the pop to set
	 */
	public void setPop(double[] pop) {
		this.pop = pop;
	}

	/**
	 * @return the options
	 */
	public PSOGAOptions getOptions() {
		return options;
	}

	/**
	 * @param options
	 *            the options to set
	 */
	public void setOptions(PSOGAOptions options) {
		this.options = options;
	}

	/**
	 * @return the velocity
	 */
	public double[] getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity
	 *            the velocity to set
	 */
	public void setVelocity(double[] velocity) {
		this.velocity = velocity;
	}

	/**
	 * @return the fittness
	 */
	public double getFitness() {
		return fitness;
	}

	/**
	 * @param fittness
	 *            the fittness to set
	 */
	public void setFitness(double fittness) {
		this.fitness = fittness;
	}

	public String toString() {
		String description = "Pop: [";
		for (int i = 0; i < pop.length; i++) {
			description += pop[i] + ",";
		}
		description += "] Velocity: [";
		for (int i = 0; i < velocity.length; i++) {
			description += velocity[i] + ",";
		}
		description += "] Fittness: " + fitness;
		return description;
	}

}

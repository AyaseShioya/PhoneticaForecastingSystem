/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm;

import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.forecaster.modelselection.Context;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class TrainerConfiturationPSOGA extends TrainerConfigurator {
	/** g,c,e **/
	private double[] best;
	private PSOGAOptions options;
	private Particle[] particles;
	private Particle[] pBest;
	private Particle gBest;
	private Instances data;
	private Context system;

	public TrainerConfiturationPSOGA(Instances data, Context system) {
		this.data = data;
		this.system = system;
	}

	public void setConfiguration(SVMConfiguration configuration) {
		super.setConfiguration(configuration);
		System.out.println("Start!");
		System.out
				.println("---------------------------Initialization!---------------------------------");
		initial(data, system);
		System.out
				.println("-----------------------Initialization Finished!----------------------------");

		// test
		// System.out.println("Optinos");
		// System.out.println(options);
		// System.out.println("--------------------------------------------------------------");

		int noChange = 0;
		// loop end
		boolean stopCondition1 = false;
		// Homogenization
		boolean stopCondition2 = false;
		// no change
		boolean stopCondition3 = false;
		int loop = 0;
		while (!(stopCondition1 || stopCondition2 || stopCondition3)) {
			System.out.println("---------------------------Population:"
					+ +(loop + 1) + "/" + options.getMaxGeneration()
					+ "----------------------------------");

			// test
			// for(Particle par: particles) {
			// System.out.println(par);
			// }
			// System.out.println("--------------------------------------------------------------");

			// test
			// System.out.println("Best of Particle:");
			// for(Particle par: pBest) {
			// System.out.println(par);
			// }
			// System.out.println("--------------------------------------------------------------");
			// System.out.println("Best of Group:");
			// System.out.println(gBest);
			// System.out.println("--------------------------------------------------------------");

			// migrate and calculate fitness
			System.out
					.println("------------------------------migration!-----------------------------------");
			for (int i = 0; i < options.getPopSize(); i++) {
				particles[i].migrate(pBest[i], gBest);
				particles[i].calculateFitness();

				double complete = 100.0 * (i + 1.0) / options.getPopSize();
				System.err.println("Complete: " + complete + "%");
			}

			// test
			// for(Particle par: particles) {
			// System.out.println(par);
			// }
			// System.out.println("--------------------------------------------------------------");

			// roulette and crossover
			System.out
					.println("-------------------------Selecting Parents!--------------------------------");
			playRoulette();

			// mitation
			// for (int i = 0; i < options.getPopSize(); i++) {
			// if(Math.random() < options.getMutationProbability()){
			// particles[i].mutation();
			// }
			// }
			// calculate fitness
			System.out
					.println("--------------------------Updating!----------------------------------------");
			double average = 0;
			for (int i = 0; i < options.getPopSize(); i++) {
				particles[i].calculateFitness();
				average += particles[i].getFitness();
				double complete = 100.0 * (i + 1.0) / options.getPopSize();
				System.err.println("Complete: " + complete + "%");
			}
			average /= options.getPopSize();

			System.err.println("Average fitness: " + average);

			// update pBest
			for (int i = 0; i < options.getPopSize(); i++) {
				if (particles[i].getFitness() > pBest[i].getFitness()) {
					pBest[i] = particles[i].clone();
				}
			}
			// update gBest
			Particle maybeBest = mergeSort(pBest)[options.getPopSize() - 1];
			if (maybeBest.getFitness() > gBest.getFitness()) {
				noChange = 0;
				gBest = maybeBest.clone();
			} else {
				noChange++;
			}

			System.err.println("Group Best: " + -gBest.getFitness());

			// // test
			// for(Particle par: particles) {
			// System.out.println(par);
			// }
			// System.out.println("--------------------------------------------------------------");

			stopCondition3 = noChange > CONFIGURATION.PSOGA_NOCHANGE_TIMES;

			stopCondition2 = true;
			for (int i = 0; i < options.getPopSize(); i++) {
				boolean flag = Math.abs(particles[0].getFitness()
						- particles[i].getFitness()) < 1e-10;
				stopCondition2 = stopCondition2 && flag;
				// TODO test
				System.out.println(particles[i].getFitness());
			}

			loop++;
			stopCondition1 = loop >= options.getMaxGeneration();

			System.out.println("Loop Over?: " + stopCondition1
					+ ", Homogenization: " + stopCondition2
					+ ", No change within " + noChange + " loops: ");
			System.out
					.println("---------------------------------------------------------------------------");
		}
		best = options.decode(gBest.getPop());
	}

	private void initial(Instances data, Context system) {
		best = new double[3];
		int[] granularity = { 1000, 1000, 1000 };

		// test
		double[] base = { ((RBFKernel) configuration.getKernel()).getGamma(),
				configuration.getC(), configuration.getEpsilon() };
		// double[] base = {100, 100, 100};

		double[] scope = { 0.4, 0.4, 0.4 };
		options = new PSOGAOptions(data, system, configuration,
				CONFIGURATION.PSOGA_MAX_GENERATION,
				CONFIGURATION.PSOGA_POPULATION_SIZE,
				CONFIGURATION.PSOGA_INERTIA_COEFFICIENT,
				CONFIGURATION.PSOGA_LOCAL_COEFFICIENT,
				CONFIGURATION.PSOGA_GLOBAL_COEFFICIENT,
				CONFIGURATION.PSOGA_CROSSOVER_PROBABILITY,
				CONFIGURATION.PSOGA_MUTATION_PROBABILITY,
				CONFIGURATION.PSOGA_CROSSOVER_TIMES,
				CONFIGURATION.PSOGA_MUTATION_TIMES,
				CONFIGURATION.PSOGA_TOLERANCE, granularity, base, scope);

		particles = new Particle[options.getPopSize()];
		pBest = new Particle[options.getPopSize()];
		double[] ini = { granularity[0] / 2, granularity[1] / 2,
				granularity[2] / 2 };

		particles[0] = new Particle(options, ini, options.generatorRandomV());
		particles[0].calculateFitness();

		double complete = 100.0 * 1.0 / options.getPopSize();
		System.err.println("Complete: " + complete + "%");

		pBest[0] = particles[0].clone();
		for (int i = 1; i < options.getPopSize(); i++) {
			particles[i] = new Particle(options, options.generatorRandomPop(),
					options.generatorRandomV());
			particles[i].calculateFitness();

			complete = 100.0 * (i + 1.0) / options.getPopSize();
			System.err.println("Complete: " + complete + "%");
			pBest[i] = particles[i].clone();
		}

		gBest = mergeSort(pBest)[options.getPopSize() - 1];

	}

	private Particle[] mergeSort(Particle[] oList) {
		if (oList.length == 1) {
			return oList;
		} else {
			Particle[] listL = new Particle[oList.length / 2];
			Particle[] listR = new Particle[oList.length - oList.length / 2];
			int center = oList.length / 2;
			for (int i = 0; i < center; i++) {
				listL[i] = oList[i];
			}
			for (int i = center, j = 0; i < oList.length; i++, j++) {
				listR[j] = oList[i];
			}

			Particle[] sortedListL = mergeSort(listL);
			Particle[] sortedListR = mergeSort(listR);
			Particle[] sortedlist = MergeTwoList(sortedListL, sortedListR);
			return sortedlist;
		}
	}

	private Particle[] MergeTwoList(Particle[] listL, Particle[] listR) {
		int i = 0, j = 0;
		Particle[] sortedList = new Particle[listL.length + listR.length];
		int foot = 0;
		while (i < listL.length && j < listR.length) {
			if (listL[i].getFitness() <= listR[j].getFitness()) {
				sortedList[foot] = listL[i];
				i++;
			} else {
				sortedList[foot] = listR[j];
				j++;
			}
			foot++;
		}

		if (i == listL.length) {
			while (j < listR.length) {
				sortedList[foot++] = listR[j++];
			}
		} else { // j==listR.length
			while (i < listL.length) {
				sortedList[foot++] = listL[i++];
			}
		}
		return sortedList;
	}

	private void playRoulette() {
		double sum = 0;
		double[] propotions = calculatePropotions();

		for (double p : propotions) {
			sum += p;
		}

		double[] rouletteProbability = new double[options.getPopSize()];

		rouletteProbability[0] = propotions[0] / sum;
		for (int i = 1; i < options.getPopSize(); i++) {
			rouletteProbability[i] = propotions[i] / sum;
			rouletteProbability[i] += rouletteProbability[i - 1];
		}

		// for(double par: rouletteProbability) {
		// System.out.println(par);
		// }
		// System.out.println("--------------------------------------------------------------");
		int numParents = options.getPopSize() / options.getCrossoverPropotion();
		if ((numParents % 2) == 1) {
			numParents++;
		}
		int[] newParticlesIndex = new int[numParents];

		for (int i = 0; i < numParents; i++) {
			double dice = Math.random();
			if (dice <= rouletteProbability[0]) {
				newParticlesIndex[i] = 0;
			}
			for (int j = 1; j < options.getPopSize() - 1; j++) {
				if ((dice <= rouletteProbability[j])
						&& (dice > rouletteProbability[j - 1])) {
					newParticlesIndex[i] = j;
				}
			}
			if (dice > rouletteProbability[options.getPopSize() - 2]) {
				newParticlesIndex[i] = options.getPopSize() - 1;
			}
		}

		System.out
				.println("-------------------------CrossOver!----------------------------------------");

		Particle[] sorted = mergeSort(particles);
		for (int i = 1; i < numParents; i += 2) {
			Particle[] children = particles[newParticlesIndex[i - 1]]
					.crossover(particles[newParticlesIndex[i]]);
			sorted[i - 1].copy(children[0]);
			sorted[i].copy(children[1]);

			double complete = 100.0 * (i + 1.0) / numParents;
			System.err.println("Complete: " + complete + "%");
		}

		// ArrayList<Integer> temp = new ArrayList<Integer>();
		// for(int i = 0; i < options.getPopSize(); i++) {
		// if(newParticles[newParticlesIndex[i]] == null) {
		// newParticles[newParticlesIndex[i]] = particles[newParticlesIndex[i]];
		// } else {
		// temp.add(newParticlesIndex[i]);
		// }
		// }
		// for(int i = 0; i < temp.size(); i++) {
		// int j = 0;
		// while(newParticles[j] != null) {
		// j++;
		// }
		// newParticles[j] = particles[temp.get(i)];
		// }
		//
		// for (int i = 1; i < options.getPopSize(); i += 2){
		// if(Math.random() < options.getCrossoverProbability()){
		// particles[i - 1].crossover(particles[i]);
		// }
		// }
		// for(int o: newParticlesIndex) {
		// System.out.println(o);
		// }

	}

	private double[] calculatePropotions() {
		double[] propotions = new double[options.getPopSize()];
		for (int i = 0; i < options.getPopSize(); i++) {
			propotions[i] = particles[i].getFitness();
		}
		return propotions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#setKernel
	 * ()
	 */
	@Override
	void setKernel() {
		RBFKernel kernel = new RBFKernel();
		kernel.setGamma(best[0]);
		configuration.setKernel(kernel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#setC
	 * ()
	 */
	@Override
	void setC() {
		configuration.setC(best[1]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * setEpsilon()
	 */
	@Override
	void setEpsilon() {
		configuration.setEpsilon(best[2]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#setNSeed
	 * ()
	 */
	@Override
	void setNSeed() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * isVariant1()
	 */
	@Override
	void isVariant1() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#
	 * setTolerance()
	 */
	@Override
	void setTolerance() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.svm.TrainerConfigurator#setLagger
	 * ()
	 */
	@Override
	void setLagger() {

	}

}

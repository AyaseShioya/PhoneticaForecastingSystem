/**
 * This is a customization of RegSMOImproved in weka
 * in order to set all the essential parameters configurable
 * and make the process automatically done.
 */
package unipv.forecasting.forecaster.modelselection.svm;

//import java.util.Random;

import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.SMOset;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class SMOOpt {
	private final static int I0 = 3;
	private final static int I0a = 1;
	private final static int I0b = 2;
	private final static int I1 = 4;
	private final static int I2 = 8;
	private final static int I3 = 16;
	/** Precision constant for updating sets */
	private final static double m_Del = 1e-10; // 1000 * Double.MIN_VALUE;

	private SMOReg regression;

	private double b;
	private SMOset supportVectors;
	private double[] alpha, alphaStar;;

	private Instances data;
	private double epsilon;
	private double tolerance;
	private double c;
	private int nInstances;
	// private int classIndex;
	private Kernel kernel;
	private double[] target;
	// private Random random;
	private double[] error;
	/** The different sets used by the algorithm. */
	private SMOset m_I0;
	/** Index set {i: 0 < alpha[i] < C || 0 < alphaStar[i] < C}} */
	private int[] m_iSet;
	/** b.up and b.low boundaries used to determine stopping criterion */
	protected double bUp, bLow;

	/** index of the instance that gave us b.up and b.low */
	protected int iUp, iLow;

	public SMOOpt(SMOReg regression) {
		this.regression = regression;
	}

	/**
	 * learn SVM parameters from data using Keerthi's SMO algorithm. Subclasses
	 * should implement something more interesting.
	 * 
	 * @param instances
	 *            the data to work with
	 * @throws Exception
	 *             if something goes wrong
	 */
	public void buildClassifier() throws Exception {
		/** initialize variables **/
		data = regression.getTraining();
		init(data);

		/** solve optimization problem **/
		if (regression.getConfiguration().isVariant1()) {
			optimize1();
		} else {
			optimize2();
		}

		/** clean up **/
		wrapUp(data);
	}

	/**
	 * initialize various variables before starting the actual optimizer
	 * 
	 * @param data
	 *            data set used for learning
	 * @throws Exception
	 *             if something goes wrong
	 */
	final private void init(Instances data) throws Exception {
		epsilon = regression.getConfiguration().getEpsilon();
		tolerance = regression.getConfiguration().getTolerance();
		c = regression.getConfiguration().getC();
		b = 0.0;
		// classIndex = data.classIndex();
		nInstances = data.numInstances();

		// Initialize kernel
		kernel = Kernel.makeCopy(regression.getConfiguration().getKernel());

		kernel.buildKernel(data);

		// init target
		target = new double[nInstances];
		for (int i = 0; i < nInstances; i++) {
			target[i] = data.instance(i).classValue();
		}

		// random = new Random(regression.getConfiguration().getnSeed());

		// initialize alpha and alpha* array to all zero
		alpha = new double[target.length];
		alphaStar = new double[target.length];

		supportVectors = new SMOset(nInstances);

		// from Keerthi's pseudo code:
		// set alpha and alpha' to zero for every example set I.1 to contain all
		// the examples
		// Choose any example i from the training set.
		// set b.up = target[i]+epsilon
		// set b.low = target[i]-espilon
		// i.up = i.low = i;
		// Initialize sets
		m_I0 = new SMOset(data.numInstances());
		m_iSet = new int[data.numInstances()];
		for (int i = 0; i < nInstances; i++) {
			m_iSet[i] = I1;
		}
		// iUp = m_random.nextInt(nInstances);
		iUp = 0;
		bUp = target[iUp] + epsilon;
		iLow = iUp;
		bLow = target[iLow] - epsilon;
		// init error cache
		error = new double[nInstances];
		for (int i = 0; i < nInstances; i++) {
			error[i] = target[i];
		}
	}

	/**
	 * use variant 1 of Shevade's et al.s paper
	 * 
	 * @throws Exception
	 *             if something goes wrong
	 */
	final private void optimize1() throws Exception {
		// % main routine for modification 1 procedure main
		// while (numChanged > 0 || examineAll)
		// numChanged = 0;
		int nNumChanged = 0;
		boolean bExamineAll = true;
		// while (numChanged > 0 || examineAll)
		// numChanged = 0;
		while (nNumChanged > 0 || bExamineAll) {
			nNumChanged = 0;
			// if (examineAll)
			// loop I over all the training examples
			// numChanged += examineExample(I)
			// else
			// loop I over I.0
			// numChanged += examineExample(I)
			// % It is easy to check if optimality on I.0 is attained...
			// if (b.up > b.low - 2*tol) at any I
			// exit the loop after setting numChanged = 0
			// endif
			if (bExamineAll) {
				for (int i = 0; i < nInstances; i++) {
					nNumChanged += examineExample(i);
				}
			} else {
				for (int i = m_I0.getNext(-1); i != -1; i = m_I0.getNext(i)) {

					nNumChanged += examineExample(i);
					if (bLow - bUp < 2 * tolerance) {
						nNumChanged = 0;
						break;
					}
				}
			} // if (examineAll == 1)
				// examineAll = 0;
				// elseif (numChanged == 0)
				// examineAll = 1;
				// endif
				// endwhile
				// endprocedure
			if (bExamineAll) {
				bExamineAll = false;
			} else if (nNumChanged == 0) {
				bExamineAll = true;
			}
		}
	}

	/**
	 * use variant 2 of Shevade's et al.s paper
	 * 
	 * @throws Exception
	 *             if something goes wrong
	 */
	protected void optimize2() throws Exception {
		// % main routine for modification 2 procedure main
		int nNumChanged = 0;
		boolean bExamineAll = true;
		// while (numChanged > 0 || examineAll)
		// numChanged = 0;
		while (nNumChanged > 0 || bExamineAll) {
			nNumChanged = 0;
			// if (examineAll)
			// loop I over all the training examples
			// numChanged += examineExample(I)
			// else
			// % The following loop is the only difference between the two
			// % SMO modifications. Whereas, modification 1, the type II
			// % loop selects i2 fro I.0 sequentially, here i2 is always
			// % set to the current i.low and i1 is set to the current i.up;
			// % clearly, this corresponds to choosing the worst violating
			// % pair using members of I.0 and some other indices
			// inner.loop.success = 1;
			// do
			// i2 = i.low
			// alpha2, alpha2' = Lagrange multipliers for i2
			// F2 = f-cache[i2]
			// i1 = i.up
			// inner.loop.success = takeStep(i.up, i.low)
			// numChanged += inner.loop.success
			// until ( (b.up > b.low - 2*tol) || inner.loop.success == 0)
			// numChanged = 0;
			// endif
			if (bExamineAll) {
				for (int i = 0; i < nInstances; i++) {
					nNumChanged += examineExample(i);
				}
			} else {
				boolean bInnerLoopSuccess = true;
				do {
					if (takeStep(iUp, iLow, alpha[iLow], alphaStar[iLow],
							error[iLow]) > 0) {
						bInnerLoopSuccess = true;
						nNumChanged += 1;
					} else {
						bInnerLoopSuccess = false;
					}
				} while ((bUp <= bLow - 2 * tolerance) && bInnerLoopSuccess);
				nNumChanged = 0;
			} //
				// if (examineAll == 1)
				// examineAll = 0
				// elseif (numChanged == 0)
				// examineAll = 1
				// endif
				// endwhile
				// endprocedure
				//
			if (bExamineAll) {
				bExamineAll = false;
			} else if (nNumChanged == 0) {
				bExamineAll = true;
			}
		}
	}

	/**
	 * updates the index sets I0a, IOb, I1, I2 and I3 for vector i
	 * 
	 * @param i
	 *            index of vector
	 * @param C
	 *            capacity for vector i
	 * @throws Exception
	 */
	protected void updateIndexSetFor(int i, double C) throws Exception {
		/*
		 * m_I0a.delete(i); m_I0b.delete(i); m_I1.delete(i); m_I2.delete(i);
		 * m_I3.delete(i);
		 */
		if (alpha[i] == 0 && alphaStar[i] == 0) {
			// m_I1.insert(i);
			m_iSet[i] = I1;
			m_I0.delete(i);
		} else if (alpha[i] > 0) {
			if (alpha[i] < C) {
				if ((m_iSet[i] & I0) == 0) {
					// error[i] = -SVMOutput(i) - b + target[i];
					m_I0.insert(i);
				}
				// m_I0a.insert(i);
				m_iSet[i] = I0a;
			} else { // alpha[i] == C
				// m_I3.insert(i);
				m_iSet[i] = I3;
				m_I0.delete(i);
			}
		} else {// alphaStar[i] > 0
			if (alphaStar[i] < C) {
				if ((m_iSet[i] & I0) == 0) {
					// error[i] = -SVMOutput(i) - b + target[i];
					m_I0.insert(i);
				}
				// m_I0b.insert(i);
				m_iSet[i] = I0b;
			} else { // alpha[i] == C
				// m_I2.insert(i);
				m_iSet[i] = I2;
				m_I0.delete(i);
			}
		}
	}

	/**
	 * updates boundaries bLow and bHi and corresponding indexes
	 * 
	 * @param i2
	 *            index of vector
	 * @param F2
	 *            error of vector i2
	 */
	protected void updateBoundaries(int i2, double F2) {
		int iSet = m_iSet[i2];

		double FLow = bLow;
		if ((iSet & (I2 | I0b)) > 0) {
			FLow = F2 + epsilon;
		} else if ((iSet & (I1 | I0a)) > 0) {
			FLow = F2 - epsilon;
		}
		if (bLow < FLow) {
			bLow = FLow;
			iLow = i2;
		}
		double FUp = bUp;
		if ((iSet & (I3 | I0a)) > 0) {
			FUp = F2 - epsilon;
		} else if ((iSet & (I1 | I0b)) > 0) {
			FUp = F2 + epsilon;
		}
		if (bUp > FUp) {
			bUp = FUp;
			iUp = i2;
		}
	}

	final private int examineExample(int i2) throws Exception {
		// procedure examineExample(i2)
		//
		// alpha2, alpha2' = Lagrange multipliers for i2
		// double alpha2 = alpha[i2];
		// double alpha2Star = alphaStar[i2];

		// if (i2 is in I.0)
		// F2 = f-cache[i2]
		// else
		// compute F2 = F.i2 and set f-cache[i2] = F2
		// % Update (b.low, i.low) or (b.up, i.up) using (F2, i2)...
		// if (i2 is in I.1)
		// if (F2+epsilon < b.up)
		// b.up = F2+epsilon,
		// i.up = i2
		// elseif (F2-epsilon > b.low)
		// b.low = F2-epsilon,
		// i.low = i2
		// end if
		// elseif ( (i2 is in I.2) && (F2+epsilon > b.low) )
		// b.low = F2+epsilon,
		// i.low = i2
		// elseif ( (i2 is in I.3) && (F2-epsilon < b.up) )
		// b.up = F2-epsilon,
		// i.up = i2
		// endif
		// endif

		int iSet = m_iSet[i2];
		double F2 = error[i2];
		if (!m_I0.contains(i2)) {
			F2 = -SVMOutput(i2) - b + target[i2];
			error[i2] = F2;
			if (iSet == I1) {
				if (F2 + epsilon < bUp) {
					bUp = F2 + epsilon;
					iUp = i2;
				} else if (F2 - epsilon > bLow) {
					bLow = F2 - epsilon;
					iLow = i2;
				}
			} else if ((iSet == I2) && (F2 + epsilon > bLow)) {
				bLow = F2 + epsilon;
				iLow = i2;
			} else if ((iSet == I3) && (F2 - epsilon < bUp)) {
				bUp = F2 - epsilon;
				iUp = i2;
			}
		}

		// % Check optimality using current b.low and b.up and, if
		// % violated, find an index i1 to do joint optimization with i2...
		// optimality = 1;
		// case 1: i2 is in I.0a
		// if (b.low-(F2-epsilon) > 2 * tol)
		// optimality = 0;
		// i1 = i.low;
		// % For i2 in I.0a choose the better i1...
		// if ((F2-epsilon)-b.up > b.low-(F2-epsilon))
		// i1 = i.up;
		// endif
		// elseif ((F2-epsilon)-b.up > 2 * tol)
		// optimality = 0;
		// i1 = i.up;
		// % For i2 in I.0a choose the better i1...
		// if ((b.low-(F2-epsilon) > (F2-epsilon)-b.up)
		// i1 = i.low;
		// endif
		// endif
		// case 2: i2 is in I.0b
		// if (b.low-(F2+epsilon) > 2 * tol)
		// optimality = 0;
		// i1 = i.low;
		// % For i2 in I.0b choose the better i1...
		// if ((F2+epsilon)-b.up > b.low-(F2+epsilon))
		// i1 = i.up;
		// endif
		// elseif ((F2+epsilon)-b.up > 2 * tol)
		// optimality = 0;
		// i1 = i.up;
		// % For i2 in I.0b choose the better i1...
		// if ((b.low-(F2+epsilon) > (F2+epsilon)-b.up)
		// i1 = i.low;
		// endif
		// endif
		// case 3: i2 is in I.1
		// if (b.low-(F2+epsilon) > 2 * tol)
		// optimality = 0;
		// i1 = i.low;
		// % For i2 in I1 choose the better i1...
		// if ((F2+epsilon)-b.up > b.low-(F2+epsilon)
		// i1 = i.up;
		// endif
		// elseif ((F2-epsilon)-b.up > 2 * tol)
		// optimality = 0;
		// i1 = i.up;
		// % For i2 in I1 choose the better i1...
		// if (b.low-(F2-epsilon) > (F2-epsilon)-b.up)
		// i1 = i.low;
		// endif
		// endif
		// case 4: i2 is in I.2
		// if ((F2+epsilon)-b.up > 2*tol)
		// optimality = 0,
		// i1 = i.up
		// endif
		// case 5: i2 is in I.3
		// if ((b.low-(F2-epsilon) > 2*tol)
		// optimality = 0, i1 = i.low
		// endif

		int i1 = i2;
		boolean bOptimality = true;
		// case 1: i2 is in I.0a
		if (iSet == I0a) {
			if (bLow - (F2 - epsilon) > 2 * tolerance) {
				bOptimality = false;
				i1 = iLow;
				// % For i2 in I .0 a choose the better i1...
				if ((F2 - epsilon) - bUp > bLow - (F2 - epsilon)) {
					i1 = iUp;
				}
			} else if ((F2 - epsilon) - bUp > 2 * tolerance) {
				bOptimality = false;
				i1 = iUp;
				// % For i2 in I.0a choose the better i1...
				if (bLow - (F2 - epsilon) > (F2 - epsilon) - bUp) {
					i1 = iLow;
				}
			}
		} // case 2: i2 is in I.0b
		else if (iSet == I0b) {
			if (bLow - (F2 + epsilon) > 2 * tolerance) {
				bOptimality = false;
				i1 = iLow; // % For i2 in I.0b choose the better i1...
				if ((F2 + epsilon) - bUp > bLow - (F2 + epsilon)) {
					i1 = iUp;
				}
			} else if ((F2 + epsilon) - bUp > 2 * tolerance) {
				bOptimality = false;
				i1 = iUp; // % For i2 in I.0b choose the better i1...
				if (bLow - (F2 + epsilon) > (F2 + epsilon) - bUp) {
					i1 = iLow;
				}
			}
		} // case 3: i2 is in I.1
		else if (iSet == I1) {
			if (bLow - (F2 + epsilon) > 2 * tolerance) {
				bOptimality = false;
				i1 = iLow;
				// % For i2 in I1 choose the better i1...
				if ((F2 + epsilon) - bUp > bLow - (F2 + epsilon)) {
					i1 = iUp;
				}
			} else if ((F2 - epsilon) - bUp > 2 * tolerance) {
				bOptimality = false;
				i1 = iUp; // % For i2 in I1 choose the better i1...
				if (bLow - (F2 - epsilon) > (F2 - epsilon) - bUp) {
					i1 = iLow;
				}
			}
		} // case 4: i2 is in I.2
		else if (iSet == I2) {
			if ((F2 + epsilon) - bUp > 2 * tolerance) {
				bOptimality = false;
				i1 = iUp;
			}
		} // case 5: i2 is in I.3
		else if (iSet == I3) {
			if (bLow - (F2 - epsilon) > 2 * tolerance) {
				bOptimality = false;
				i1 = iLow;
			}
		}
		// if (optimality == 1)
		// return 0
		// if (takeStep(i1, i2))
		// return 1
		// else
		// return 0
		// endif
		// endprocedure
		if (bOptimality) {
			return 0;
		}
		return takeStep(i1, i2, alpha[i2], alphaStar[i2], F2);
	}

	/**
	 * takeStep method from Shevade et al.s paper. parameters correspond to
	 * pseudocode from paper.
	 * 
	 * @param i1
	 * @param i2
	 * @param alpha2
	 * @param alpha2Star
	 * @param phi2
	 * @return
	 * @throws Exception
	 */
	protected int takeStep(int i1, int i2, double alpha2, double alpha2Star,
			double phi2) throws Exception {
		// procedure takeStep(i1, i2)
		//
		// if (i1 == i2)
		// return 0
		if (i1 == i2) {
			return 0;
		}
		double C1 = c * data.instance(i1).weight();
		double C2 = c * data.instance(i2).weight();
		// alpha1, alpha1' = Lagrange multipliers for i1
		double alpha1 = alpha[i1];
		double alpha1Star = alphaStar[i1];
		// double y1 = target[i1];
		double phi1 = error[i1];
		// if ((m_iSet[i1] & I0)==0) {
		// phi1 = -SVMOutput(i1) - b + target[i1];
		// error[i1] = phi1;
		// }
		// k11 = kernel(point[i1], point[i1])
		// k12 = kernel(point[i1], point[i2])
		// k22 = kernel(point[i2], point[i2])
		// eta = -2*k12+k11+k22
		// gamma = alpha1-alpha1'+alpha2-alpha2'
		//
		double k11 = kernel.eval(i1, i1, data.instance(i1));
		double k12 = kernel.eval(i1, i2, data.instance(i1));
		double k22 = kernel.eval(i2, i2, data.instance(i2));
		double eta = -2 * k12 + k11 + k22;
		double gamma = alpha1 - alpha1Star + alpha2 - alpha2Star;
		// if (eta < 0) {
		// this may happen due to numeric instability
		// due to Mercer's condition, this should not happen, hence we give up
		// return 0;
		// }
		// % We assume that eta > 0. Otherwise one has to repeat the complete
		// % reasoning similarly (i.e. compute objective functions at L and H
		// % and decide which one is largest
		//
		// case1 = case2 = case3 = case4 = finished = 0
		// alpha1old = alpha1,
		// alpha1old' = alpha1'
		// alpha2old = alpha2,
		// alpha2old' = alpha2'
		// deltaphi = F1 - F2
		//

		// while !finished
		// % This loop is passed at most three times
		// % Case variables needed to avoid attempting small changes twice
		// if (case1 == 0) &&
		// (alpha1 > 0 || (alpha1' == 0 && deltaphi > 0)) &&
		// (alpha2 > 0 || (alpha2' == 0 && deltaphi < 0))
		// compute L, H (w.r.t. alpha1, alpha2)
		// if (L < H)
		// a2 = alpha2 - (deltaphi / eta ) a2 = min(a2, H) a2 = max(L, a2) a1 =
		// alpha1 - (a2 - alpha2)
		// update alpha1, alpha2 if change is larger than some eps
		// else
		// finished = 1
		// endif
		// case1 = 1
		// elseif (case2 == 0) &&
		// (alpha1 > 0 || (alpha1' == 0 && deltaphi > 2*epsilon)) &&
		// (alpha2' > 0 || (alpha2 == 0 && deltaphi > 2*epsilon))
		//
		// compute L, H (w.r.t. alpha1, alpha2')
		// if (L < H)
		// a2 = alpha2' + ((deltaphi - 2*epsilon)/eta)) a2 = min(a2, H) a2 =
		// max(L, a2) a1 = alpha1 + (a2-alpha2')
		// update alpha1, alpha2' if change is larger than some eps
		// else
		// finished = 1
		// endif
		// case2 = 1
		// elseif (case3 == 0) &&
		// (alpha1' > 0 || (alpha1 == 0 && deltaphi < -2*epsilon)) &&
		// (alpha2 > 0 || (alpha2' == 0 && deltaphi < -2*epsilon))
		// compute L, H (w.r.t. alpha1', alpha2)
		// if (L < H)
		// a2 = alpha2 - ((deltaphi + 2*epsilon)/eta) a2 = min(a2, H) a2 =
		// max(L, a2) a1 = alpha1' + (a2 - alpha2)
		// update alpha1', alpha2 if change is larger than some eps
		// else
		// finished = 1
		// endif
		// case3 = 1
		// elseif (case4 == 0) &&
		// (alpha1' > 0) || (alpha1 == 0 && deltaphi < 0)) &&
		// (alpha2' > 0) || (alpha2 == 0 && deltaphi > 0))
		// compute L, H (w.r.t. alpha1', alpha2')
		// if (L < H)
		// a2 = alpha2' + deltaphi/eta a2 = min(a2, H) a2 = max(L, a2) a1 =
		// alpha1' - (a2 - alpha2')
		// update alpha1, alpha2' if change is larger than some eps
		// else
		// finished = 1
		// endif
		// case4 = 1
		// else
		// finished = 1
		// endif
		// update deltaphi
		// endwhile

		double alpha1old = alpha1;
		double alpha1Starold = alpha1Star;
		double alpha2old = alpha2;
		double alpha2Starold = alpha2Star;
		double deltaPhi = phi1 - phi2;

		if (findOptimalPointOnLine(i1, alpha1, alpha1Star, C1, i2, alpha2,
				alpha2Star, C2, gamma, eta, deltaPhi)) {

			alpha1 = alpha[i1];
			alpha1Star = alphaStar[i1];
			alpha2 = alpha[i2];
			alpha2Star = alphaStar[i2];

			// if changes in alpha('), alpha2(') are larger than some eps
			// Update f-cache[i] for i in I.0 using new Lagrange multipliers
			// Store the changes in alpha, alpha' array
			// Update I.0, I.1, I.2, I.3
			// Compute (i.low, b.low) and (i.up, b.up) by applying the
			// conditions mentioned above, using only i1, i2 and indices in I.0
			// return 1
			// else
			// return 0
			// endif endprocedure

			// Update error cache using new Lagrange multipliers
			double dAlpha1 = alpha1 - alpha1old - (alpha1Star - alpha1Starold);
			double dAlpha2 = alpha2 - alpha2old - (alpha2Star - alpha2Starold);
			for (int j = m_I0.getNext(-1); j != -1; j = m_I0.getNext(j)) {
				if ((j != i1) && (j != i2)) {
					error[j] -= dAlpha1 * kernel.eval(i1, j, data.instance(i1))
							+ dAlpha2 * kernel.eval(i2, j, data.instance(i2));
				}
			}
			error[i1] -= dAlpha1 * k11 + dAlpha2 * k12;
			error[i2] -= dAlpha1 * k12 + dAlpha2 * k22;

			updateIndexSetFor(i1, C1);
			updateIndexSetFor(i2, C2);

			// Compute (i.low, b.low) and (i.up, b.up) by applying the
			// conditions mentioned above, using only i1, i2 and indices in I.0
			bUp = Double.MAX_VALUE;
			bLow = -Double.MAX_VALUE;
			for (int j = m_I0.getNext(-1); j != -1; j = m_I0.getNext(j)) {
				updateBoundaries(j, error[j]);
			}
			if (!m_I0.contains(i1)) {
				updateBoundaries(i1, error[i1]);
			}
			if (!m_I0.contains(i2)) {
				updateBoundaries(i2, error[i2]);
			}

			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Finds optimal point on line constrained by first (i1) and second (i2)
	 * candidate. Parameters correspond to pseudocode (see technicalinformation)
	 * 
	 * @param i1
	 * @param alpha1
	 * @param alpha1Star
	 * @param C1
	 * @param i2
	 * @param alpha2
	 * @param alpha2Star
	 * @param C2
	 * @param gamma
	 * @param eta
	 * @param deltaPhi
	 * @return
	 */
	protected boolean findOptimalPointOnLine(int i1, double alpha1,
			double alpha1Star, double C1, int i2, double alpha2,
			double alpha2Star, double C2, double gamma, double eta,
			double deltaPhi) {
		if (eta <= 0) {
			// this may happen due to numeric instability
			// due to Mercer's condition, this should not happen, hence we give
			// up
			return false;
		}

		boolean case1 = false;
		boolean case2 = false;
		boolean case3 = false;
		boolean case4 = false;
		boolean finished = false;

		// while !finished
		// % this loop is passed at most three times
		// % case variables needed to avoid attempting small changes twice
		while (!finished) {
			// if (case1 == 0) &&
			// (alpha1 > 0 || (alpha1* == 0 && deltaPhi > 0)) &&
			// (alpha2 > 0 || (alpha2* == 0 && deltaPhi < 0))
			// compute L, H (wrt. alpha1, alpha2)
			// if L < H
			// a2 = alpha2 ? - deltaPhi/eta
			// a2 = min(a2, H)
			// a2 = max(L, a2)
			// a1 = alpha1 ? - (a2 ? alpha2)
			// update alpha1, alpha2 if change is larger than some eps
			// else
			// finished = 1
			// endif
			// case1 = 1;

			if ((case1 == false)
					&& (alpha1 > 0 || (alpha1Star == 0 && deltaPhi > 0))
					&& (alpha2 > 0 || (alpha2Star == 0 && deltaPhi < 0))) {
				// compute L, H (wrt. alpha1, alpha2)
				double L = Math.max(0, gamma - C1);
				double H = Math.min(C2, gamma);
				if (L < H) {
					double a2 = alpha2 - deltaPhi / eta;
					a2 = Math.min(a2, H);
					a2 = Math.max(L, a2);
					// To prevent precision problems
					if (a2 > C2 - m_Del * C2) {
						a2 = C2;
					} else if (a2 <= m_Del * C2) {
						a2 = 0;
					}
					double a1 = alpha1 - (a2 - alpha2);
					if (a1 > C1 - m_Del * C1) {
						a1 = C1;
					} else if (a1 <= m_Del * C1) {
						a1 = 0;
					}
					// update alpha1, alpha2 if change is larger than some eps
					if (Math.abs(alpha1 - a1) > epsilon) {
						deltaPhi += eta * (a2 - alpha2);
						alpha1 = a1;
						alpha2 = a2;
					}
				} else {
					finished = true;
				}
				case1 = true;
			}

			// elseif (case2 == 0) &&
			// (alpha1 > 0 || (alpha1* == 0 && deltaPhi > 2 epsilon)) &&
			// (alpha2* > 0 || (alpha2 == 0 && deltaPhi > 2 epsilon))
			// compute L, H (wrt. alpha1, alpha2*)
			// if L < H
			// a2 = alpha2* + (deltaPhi ?- 2 epsilon)/eta
			// a2 = min(a2, H)
			// a2 = max(L, a2)
			// a1 = alpha1 + (a2 ? alpha2*)
			// update alpha1, alpha2* if change is larger than some eps
			// else
			// finished = 1
			// endif
			// case2 = 1;

			else if ((case2 == false)
					&& (alpha1 > 0 || (alpha1Star == 0 && deltaPhi > 2 * epsilon))
					&& (alpha2Star > 0 || (alpha2 == 0 && deltaPhi > 2 * epsilon))) {
				// compute L, H (wrt. alpha1, alpha2*)
				double L = Math.max(0, -gamma);
				double H = Math.min(C2, -gamma + C1);
				if (L < H) {
					double a2 = alpha2Star + (deltaPhi - 2 * epsilon) / eta;
					a2 = Math.min(a2, H);
					a2 = Math.max(L, a2);
					// To prevent precision problems
					if (a2 > C2 - m_Del * C2) {
						a2 = C2;
					} else if (a2 <= m_Del * C2) {
						a2 = 0;
					}
					double a1 = alpha1 + (a2 - alpha2Star);
					if (a1 > C1 - m_Del * C1) {
						a1 = C1;
					} else if (a1 <= m_Del * C1) {
						a1 = 0;
					}
					// update alpha1, alpha2* if change is larger than some eps
					if (Math.abs(alpha1 - a1) > epsilon) {
						deltaPhi += eta * (-a2 + alpha2Star);
						alpha1 = a1;
						alpha2Star = a2;
					}
				} else {
					finished = true;
				}
				case2 = true;
			}

			// elseif (case3 == 0) &&
			// (alpha1* > 0 || (alpha1 == 0 && deltaPhi < -2 epsilon)) &&
			// (alpha2 > 0 || (alpha2* == 0 && deltaPhi < -2 epsilon))
			// compute L, H (wrt. alpha1*, alpha2)
			// if L < H
			// a2 = alpha2 ?- (deltaPhi ?+ 2 epsilon)/eta
			// a2 = min(a2, H)
			// a2 = max(L, a2)
			// a1 = alpha1* + (a2 ? alpha2)
			// update alpha1*, alpha2 if change is larger than some eps
			// else
			// finished = 1
			// endif
			// case3 = 1;

			else if ((case3 == false)
					&& (alpha1Star > 0 || (alpha1 == 0 && deltaPhi < -2
							* epsilon))
					&& (alpha2 > 0 || (alpha2Star == 0 && deltaPhi < -2
							* epsilon))) {
				// compute L, H (wrt. alpha1*, alpha2)
				double L = Math.max(0, gamma);
				double H = Math.min(C2, C1 + gamma);
				if (L < H) {
					// note Smola's psuedocode has a minus, where there should
					// be a plus in the following line, Keerthi's is correct
					double a2 = alpha2 - (deltaPhi + 2 * epsilon) / eta;
					a2 = Math.min(a2, H);
					a2 = Math.max(L, a2);
					// To prevent precision problems
					if (a2 > C2 - m_Del * C2) {
						a2 = C2;
					} else if (a2 <= m_Del * C2) {
						a2 = 0;
					}
					double a1 = alpha1Star + (a2 - alpha2);
					if (a1 > C1 - m_Del * C1) {
						a1 = C1;
					} else if (a1 <= m_Del * C1) {
						a1 = 0;
					}
					// update alpha1*, alpha2 if change is larger than some eps
					if (Math.abs(alpha1Star - a1) > epsilon) {
						deltaPhi += eta * (a2 - alpha2);
						alpha1Star = a1;
						alpha2 = a2;
					}
				} else {
					finished = true;
				}
				case3 = true;
			}

			// elseif (case4 == 0) &&
			// (alpha1* > 0 || (alpha1 == 0 && deltaPhi < 0)) &&
			// (alpha2* > 0 || (alpha2 == 0 && deltaPhi > 0))
			// compute L, H (wrt. alpha1*, alpha2*)
			// if L < H
			// a2 = alpha2* + deltaPhi/eta
			// a2 = min(a2, H)
			// a2 = max(L, a2)
			// a1 = alpha1* ? (a2 ? alpha2*)
			// update alpha1*, alpha2* if change is larger than some eps
			// else
			// finished = 1
			// endif
			// case4 = 1;
			// else
			// finished = 1
			// endif

			else if ((case4 == false)
					&& (alpha1Star > 0 || (alpha1 == 0 && deltaPhi < 0))
					&& (alpha2Star > 0 || (alpha2 == 0 && deltaPhi > 0))) {
				// compute L, H (wrt. alpha1*, alpha2*)
				double L = Math.max(0, -gamma - C1);
				double H = Math.min(C2, -gamma);
				if (L < H) {
					double a2 = alpha2Star + deltaPhi / eta;
					a2 = Math.min(a2, H);
					a2 = Math.max(L, a2);
					// To prevent precision problems
					if (a2 > C2 - m_Del * C2) {
						a2 = C2;
					} else if (a2 <= m_Del * C2) {
						a2 = 0;
					}
					double a1 = alpha1Star - (a2 - alpha2Star);
					if (a1 > C1 - m_Del * C1) {
						a1 = C1;
					} else if (a1 <= m_Del * C1) {
						a1 = 0;
					}
					// update alpha1*, alpha2* if change is larger than some eps
					if (Math.abs(alpha1Star - a1) > epsilon) {
						deltaPhi += eta * (-a2 + alpha2Star);

						alpha1Star = a1;
						alpha2Star = a2;
					}
				} else {
					finished = true;
				}
				case4 = true;
			} else {
				finished = true;
			}

			// update deltaPhi
			// using 4.36 from Smola's thesis:
			// deltaPhi = deltaPhi - eta *
			// ((alpha1New-alpha1StarNew)-(alpha1-alpha1Star));
			// the update is done inside the loop, saving us to remember old
			// values of alpha1(*)
			// deltaPhi += eta * ((alpha2 - alpha2Star) - dAlpha2Old);
			// dAlpha2Old = (alpha2 - alpha2Star);

			// endwhile

		}

		if (Math.abs(alpha1 - alpha[i1]) > epsilon
				|| Math.abs(alpha1Star - alphaStar[i1]) > epsilon
				|| Math.abs(alpha2 - alpha[i2]) > epsilon
				|| Math.abs(alpha2Star - alphaStar[i2]) > epsilon) {

			if (alpha1 > C1 - m_Del * C1) {
				alpha1 = C1;
			} else if (alpha1 <= m_Del * C1) {
				alpha1 = 0;
			}
			if (alpha1Star > C1 - m_Del * C1) {
				alpha1Star = C1;
			} else if (alpha1Star <= m_Del * C1) {
				alpha1Star = 0;
			}
			if (alpha2 > C2 - m_Del * C2) {
				alpha2 = C2;
			} else if (alpha2 <= m_Del * C2) {
				alpha2 = 0;
			}
			if (alpha2Star > C2 - m_Del * C2) {
				alpha2Star = C2;
			} else if (alpha2Star <= m_Del * C2) {
				alpha2Star = 0;
			}

			// store new alpha's
			alpha[i1] = alpha1;
			alphaStar[i1] = alpha1Star;
			alpha[i2] = alpha2;
			alphaStar[i2] = alpha2Star;

			// update supportvector set
			if (alpha1 != 0 || alpha1Star != 0) {
				if (!supportVectors.contains(i1)) {
					supportVectors.insert(i1);
				}
			} else {
				supportVectors.delete(i1);
			}
			if (alpha2 != 0 || alpha2Star != 0) {
				if (!supportVectors.contains(i2)) {
					supportVectors.insert(i2);
				}
			} else {
				supportVectors.delete(i2);
			}
			return true;
		}

		return false;
	}

	/**
	 * SVMOutput of an instance in the training set, m_data This uses the cache,
	 * unlike SVMOutput(Instance)
	 * 
	 * @param index
	 *            index of the training instance in m_data
	 * @return the SVM output
	 * @throws Exception
	 *             if something goes wrong
	 */
	protected double SVMOutput(int index) throws Exception {
		double result = -b;
		for (int i = supportVectors.getNext(-1); i != -1; i = supportVectors
				.getNext(i)) {
			result += (alpha[i] - alphaStar[i])
					* kernel.eval(index, i, data.instance(index));
		}
		return result;
	}

	public void wrapUp(final Instances data) throws Exception {
		// System.out.println(bLow + " and " + bUp);
		b = -(bLow + bUp) / 2.0;
		regression.getConfiguration().setB(b);

		// int index = 0;
		// for (double value: alphaStar) {
		// alpha[index++] -= value;
		// }

		int index = 0;
		double[] weight;
		Instances svs = new Instances(data, 0);

		/** loop for support vectors **/
		if (supportVectors.numElements() != 0) {
			weight = new double[supportVectors.numElements()];
			for (int i = supportVectors.getNext(-1); i != -1; i = supportVectors
					.getNext(i)) {
				weight[index++] = alpha[i] - alphaStar[i];
				svs.add(data.get(i));
			}
		} else {
			weight = new double[1];
			weight[0] = 0;
			svs.add(data.get(0));
		}
		// svs.setClassIndex(ForecasterConfiguration.REPT_LAGGEDVALUE_INDEX);
		// System.out.println(svs.numInstances());
		regression.getConfiguration().setSupportVectors(svs);
		regression.getConfiguration().setWeight(weight);
		/** rebuild kernel and save it. **/

		// int hasClass = 0;
		// for (int i = 0; i < svs.numInstances(); i++) {
		// if (!svs.instance(i).classIsMissing())
		// hasClass++;
		// }System.out.println(hasClass + "," + svs.numInstances());

		kernel.buildKernel(svs);
		regression.getConfiguration().setKernel(kernel);
	}

	public void close() {
		alpha = null;
		alphaStar = null;
		kernel = null;
		target = null;
		error = null;
		m_iSet = null;
	}
}

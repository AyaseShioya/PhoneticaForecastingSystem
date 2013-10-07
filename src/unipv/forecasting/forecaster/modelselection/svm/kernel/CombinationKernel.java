/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm.kernel;

import weka.classifiers.functions.supportVector.CachedKernel;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Capabilities.Capability;

/**
 * @author Quest
 * 
 */
public class CombinationKernel extends CachedKernel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2859198505884923930L;

	/** Use lower-order terms? */
	protected boolean lowerOrder = false;

	/** The exponent for the polynomial kernel. */
	protected double exponent = 1.0;

	/** The precalculated dotproducts of &lt;inst_i,inst_i&gt; */
	protected double[] kernelPrecalc;

	/** Gamma for the RBF kernel. */
	protected double gamma = 0.01;

	/**
	 * The measure of how Polynomial kernel and RBF kernel combined. The bigger
	 * it is, the more the kernel is like a polynomial kernel.
	 **/
	protected double k = 0.5;

	public CombinationKernel() {
		super();
	}

	public CombinationKernel(Instances data, int cacheSize, boolean lowerOrder,
			double exponent, double[] kernelPrecalc, double gamma) {
		super();
		this.lowerOrder = lowerOrder;
		this.exponent = exponent;
		this.kernelPrecalc = kernelPrecalc;
		this.gamma = gamma;

		setCacheSize(cacheSize);
		setExponent(exponent);
		setLowerOrder(lowerOrder);
		setGamma(gamma);

		try {
			buildKernel(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see weka.classifiers.functions.supportVector.CachedKernel#evaluate(int,
	 * int, weka.core.Instance)
	 */
	@Override
	protected double evaluate(int id1, int id2, Instance inst1)
			throws Exception {
		double resultPoly = 0.0;
		double resultRBF = 0.0;
		if (id1 == id2) {
			/** calculating Polynomial Kernel **/
			resultPoly = dotProd(inst1, inst1);
			/** calculating RBF Kernel **/
			resultRBF = 1.0;
		} else {
			/** calculating Polynomial Kernel **/
			resultPoly = dotProd(inst1, m_data.instance(id2));
			/** calculating RBF Kernel **/
			double precalc1;
			if (id1 == -1)
				precalc1 = dotProd(inst1, inst1);
			else
				precalc1 = kernelPrecalc[id1];
			Instance inst2 = m_data.instance(id2);
			resultRBF = Math
					.exp(gamma
							* (2. * dotProd(inst1, inst2) - precalc1 - kernelPrecalc[id2]));
		}
		// Use lower order terms?
		if (lowerOrder) {
			resultPoly += 1.0;
		}
		if (exponent != 1.0) {
			resultPoly = Math.pow(resultPoly, exponent);
		}

		return ((k * resultPoly) + ((1.0 - k) * resultRBF));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see weka.classifiers.functions.supportVector.Kernel#globalInfo()
	 */
	@Override
	public String globalInfo() {
		return "The combination kernel : k * Kp(x,y) + (1 - k) * Kr(x,y)"
				+ "The Polynomial kernel: Kp(x, y) = <x, y>^p or Kp(x, y) = (<x, y>+1)^p"
				+ "The RBF kernel. Kr(x, y) = e^-(gamma * <x-y, x-y>^2)";
	}

	/**
	 * Returns the Capabilities of this kernel.
	 * 
	 * @return the capabilities of this object
	 * @see Capabilities
	 */
	public Capabilities getCapabilities() {
		Capabilities result = super.getCapabilities();
		result.disableAll();

		result.enable(Capability.NUMERIC_ATTRIBUTES);
		result.enableAllClasses();
		result.enable(Capability.MISSING_CLASS_VALUES);

		return result;
	}

	/**
	 * initializes variables etc.
	 * 
	 * @param data
	 *            the data to use
	 */
	protected void initVars(Instances data) {
		super.initVars(data);

		kernelPrecalc = new double[data.numInstances()];
	}

	/**
	 * builds the kernel with the given data. Initializes the kernel cache. The
	 * actual size of the cache in bytes is (64 * cacheSize).
	 * 
	 * @param data
	 *            the data to base the kernel on
	 * @throws Exception
	 *             if something goes wrong
	 */
	public void buildKernel(Instances data) throws Exception {
		// does kernel handle the data?
		// System.out.println(data);
		if (!getChecksTurnedOff())
			getCapabilities().testWithFail(data);

		initVars(data);

		for (int i = 0; i < data.numInstances(); i++) {
			kernelPrecalc[i] = dotProd(data.instance(i), data.instance(i));
		}
	}

	/**
	 * @return the lowerOrder
	 */
	public boolean isLowerOrder() {
		return lowerOrder;
	}

	/**
	 * @param lowerOrder
	 *            the lowerOrder to set
	 */
	public void setLowerOrder(boolean lowerOrder) {
		this.lowerOrder = lowerOrder;
	}

	/**
	 * @return the exponent
	 */
	public double getExponent() {
		return exponent;
	}

	/**
	 * @param exponent
	 *            the exponent to set
	 */
	public void setExponent(double exponent) {
		this.exponent = exponent;
	}

	/**
	 * @return the kernelPrecalc
	 */
	public double[] getKernelPrecalc() {
		return kernelPrecalc;
	}

	/**
	 * @param kernelPrecalc
	 *            the kernelPrecalc to set
	 */
	public void setKernelPrecalc(double[] kernelPrecalc) {
		this.kernelPrecalc = kernelPrecalc;
	}

	/**
	 * @return the gamma
	 */
	public double getGamma() {
		return gamma;
	}

	/**
	 * @param gamma
	 *            the gamma to set
	 */
	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	/**
	 * @return the k
	 */
	public double getK() {
		return k;
	}

	/**
	 * @param k
	 *            the k to set
	 */
	public void setK(double k) {
		this.k = k;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CombinationKernel [lowerOrder=" + lowerOrder + ", exponent="
				+ exponent + ", gamma=" + gamma + ", k=" + k + "]"
				+ "kernelPrecalc:"
				+ (kernelPrecalc == null ? null : kernelPrecalc[0]);
	}

}

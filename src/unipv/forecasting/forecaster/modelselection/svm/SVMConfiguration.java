/**
 * 
 */
package unipv.forecasting.forecaster.modelselection.svm;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.CONFIGURATION.FORECASTER_CONFIGURATOR;
import unipv.forecasting.CONFIGURATION.FORECASTING_KPI;
import unipv.forecasting.CONFIGURATION.FORECASTING_TYPE;
import unipv.forecasting.CONFIGURATION.TRAINER_CONFIGURATOR;
import unipv.forecasting.Service;
import unipv.forecasting.dao.database.ParameterConnector;
import unipv.forecasting.forecaster.modelselection.Configuration;
import unipv.forecasting.forecaster.modelselection.Context;
import unipv.forecasting.forecaster.modelselection.lag.ArtificialHourIndex;
import unipv.forecasting.forecaster.modelselection.lag.BasicLagger;
import unipv.forecasting.forecaster.modelselection.lag.DayIndex;
import unipv.forecasting.forecaster.modelselection.lag.HourIndex;
import unipv.forecasting.forecaster.modelselection.lag.Lagger;
import unipv.forecasting.utils.Normalizer;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class SVMConfiguration implements Configuration {
	private FORECASTING_TYPE type;
	private FORECASTING_KPI kpi;
	private Service service;

	/**
	 * coefficients used by normalization filter for doing its linear
	 * transformation so that result = svmoutput * m_x1 + m_x0
	 **/
	private double m_x1;
	private double m_x0;
	/** kernel function **/
	private Kernel kernel;
	/** penalty coefficient **/
	private double c;
	/** offset **/
	private double b;
	/** epsilon of epsilon-insensitive cost function **/
	private double epsilon;
	/** seed for initializing random number generator **/
	private int nSeed;
	/** set of support vectors, that is, vectors with alpha(*)!=0 **/
	private Instances supportVectors;
	/** the weight of support vectors **/
	private double[] weight;
	/** is the Improved SMO use variant 1? **/
	private boolean isVariant1;
	/**
	 * tolerance parameter used for checking stopping criterion b.up < b.low + 2
	 * tol
	 */
	private double tolerance;
	/** the lag creator used to create lag from original data **/
	private Lagger lagger;
	/** the normalizer used to normalize the instances **/
	private Normalizer normalizer;

	public SVMConfiguration(final FORECASTING_TYPE type,
			final FORECASTING_KPI kpi, final Service service) {
		this.type = type;
		this.kpi = kpi;
		this.service = service;
	}

	public SVMConfiguration(String jsonString) {
		JSONObject json = JSONObject.fromObject(jsonString);
		this.m_x0 = json.getDouble("m_x0");
		this.m_x1 = json.getDouble("m_x1");

		RBFKernel rKernel = new RBFKernel();
		rKernel.setGamma(json.getDouble("gamma"));

		this.kernel = rKernel;
		this.c = json.getDouble("c");
		this.b = json.getDouble("b");
		this.epsilon = json.getDouble("epsilon");
		this.nSeed = json.getInt("nSeed");

		Instances sv = null;
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		JSONArray row = null;
		JSONObject svs = json.getJSONObject("supportVectors");
		JSONArray metaData = svs.getJSONArray("metaData");
		JSONArray vectors = svs.getJSONArray("vectors");
		for (int i = 0; i < metaData.size(); i++) {
			atts.add(new Attribute(metaData.getString(i)));
		}
		sv = new Instances("SupportVectors", atts, 0);
		double[] instanceValue = new double[sv.numAttributes()];
		for (int i = 0; i < vectors.size(); i++) {
			row = vectors.getJSONArray(i);
			for (int j = 0; j < row.size(); j++) {
				instanceValue[j] = Double.parseDouble(row.getString(j));
			}
			sv.add(new DenseInstance(1, instanceValue));
			instanceValue = new double[sv.numAttributes()];
		}
		sv.setClassIndex(CONFIGURATION.REPT_LAGGEDVALUE_INDEX);

		this.supportVectors = sv;

		try {
			kernel.buildKernel(sv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONArray weightArray = json.getJSONArray("weight");
		double[] w = new double[weightArray.size()];
		for (int i = 0; i < weightArray.size(); i++) {
			w[i] = weightArray.getDouble(i);
		}

		this.weight = w;

		this.isVariant1 = json.getBoolean("isVariant1");
		this.tolerance = json.getDouble("tolerance");

		this.lagger = new ArtificialHourIndex(new HourIndex(new DayIndex(
				new BasicLagger(json.getInt("minLag"), json.getInt("maxLag"),
						json.getDouble("beginTimeMills")))));

		double[] min = new double[supportVectors.numAttributes()];
		double[] max = new double[supportVectors.numAttributes()];
		JSONArray minArray = json.getJSONArray("minArray");
		JSONArray maxArray = json.getJSONArray("maxArray");
		for (int i = 0; i < minArray.size(); i++) {
			min[i] = minArray.getDouble(i);
		}
		for (int i = 0; i < maxArray.size(); i++) {
			max[i] = maxArray.getDouble(i);
		}

		Normalizer nor = new Normalizer();
		nor.initialize(min, max);
		this.normalizer = nor;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.Configuration#configureTrainer
	 * (
	 * unipv.forecasting.forecaster.ForecasterConfiguration.TRAINER_CONFIGURATOR
	 * )
	 */
	@Override
	public void configureTrainer(final TRAINER_CONFIGURATOR set,
			final Instances data, final Context system) {
		Configurator configurator = null;
		switch (set) {
		case DEFAULT:
			configurator = new DefaultTrainerConfigurator();
			break;
		case HCOM_1:
			configurator = new TrainerConfiguratorHCom1(data);
			break;
		case GRID_SEARCH:
			configurator = new TrainerConfiturationGridOptimizer(data, system);
			break;
		case PSOGA:
			configurator = new TrainerConfiturationPSOGA(data, system);
			break;
		case HCOM_2:
			configurator = new TrainerConfiguratorHCom2(data);
			break;
		case DATABASE:
			configurator = new TrainerConfiguratorFromDatabase();
			break;
		case TEST:
			;
			break;
		}
		configurator.setConfiguration(this);
		configurator.configure();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.Configuration#configureTrainer
	 * ()
	 */
	@Override
	public void configureTrainer() {
		Configurator configurator = new DefaultTrainerConfigurator();
		configurator.setConfiguration(this);
		configurator.configure();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.Configuration#configureForecaster
	 * (
	 * unipv.forecasting.forecaster.ForecasterConfiguration.FORECASTER_CONFIGURATOR
	 * )
	 */
	@Override
	public void configureForecaster(final FORECASTER_CONFIGURATOR set,
			final Instances data, final Context system) {
		Configurator configurator = null;
		switch (set) {
		case DEFAULT:
			configurator = new DefaultForecastorConfigurator();
			break;
		case DATABASE:
			configurator = new ForecasterConfiguratorFromDatabase();
			break;
		case TEST:
			;
			break;
		}
		configurator.setConfiguration(this);
		configurator.configure();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.Configuration#configureForecaster
	 * ()
	 */
	@Override
	public void configureForecaster() {
		Configurator configurator = new DefaultForecastorConfigurator();
		configurator.setConfiguration(this);
		configurator.configure();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forecasting.forecaster.modelselection.Configuration#saveModel()
	 */
	@Override
	public void saveModel() {
		ParameterConnector connector = new ParameterConnector();
		connector.updateParameter(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		result += "x1: " + m_x1;
		result += ", x0: " + m_x0;
		result += ", kernel: " + kernel;
		result += ", c: " + c;
		result += ", b: " + b;
		result += ", epsilon: " + epsilon;
		result += ", seed: " + nSeed + "\n";
		result += ", isVariant1: " + isVariant1;
		result += ", tolerance: " + tolerance;
		result += ", lagger: " + lagger;
		result += ", normalizer: "
				+ (normalizer == null ? null : normalizer.toString()) + "\n";
		if (supportVectors != null) {
			result += "support vectors:" + "\n";
			for (int i = 0; i < 1; i++) {
				result += i + ": ";
				result += "weight: " + weight[i];
				result += "\n";
			}
		}
		return result;
	}

	/**
	 * @return the weight
	 */
	public double[] getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(double[] weight) {
		this.weight = weight;
	}

	public SVMConfiguration copy() {
		SVMConfiguration configuration = new SVMConfiguration(type, kpi,
				service);
		configuration.setM_x1(m_x1);
		configuration.setM_x0(m_x0);
		try {
			configuration.setKernel(Kernel.makeCopy(kernel));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			configuration.setKernel(null);
		}
		// System.out.println(configuration.getKernel());
		configuration.setC(c);
		configuration.setB(b);
		configuration.setEpsilon(epsilon);
		configuration.setnSeed(nSeed);
		configuration.setSupportVectors(supportVectors == null ? null
				: new Instances(supportVectors));
		configuration.setWeight(weight == null ? null : weight.clone());
		configuration.setVariant1(isVariant1);
		configuration.setTolerance(tolerance);
		configuration.setLagger(lagger.copy());
		configuration.setNormalizer(normalizer.copy());

		return configuration;
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("m_x0", m_x0);
		json.put("m_x1", m_x1);
		json.put("gamma", ((RBFKernel) kernel).getGamma());
		json.put("c", c);
		json.put("b", b);
		json.put("epsilon", epsilon);
		json.put("nSeed", nSeed);
		JSONObject svs = new JSONObject();
		JSONArray metaData = new JSONArray();
		for (int i = 0; i < supportVectors.numAttributes(); i++) {
			metaData.add(supportVectors.attribute(i).name());
		}
		JSONArray vectors = new JSONArray();
		for (int i = 0; i < supportVectors.numInstances(); i++) {
			JSONArray row = new JSONArray();
			for (int j = 0; j < supportVectors.numAttributes(); j++) {
				row.add(supportVectors.get(i).value(j));
			}
			vectors.add(row);
		}
		svs.put("metaData", metaData);
		svs.put("vectors", vectors);
		json.put("supportVectors", svs);

		JSONArray weightArray = new JSONArray();
		for (int i = 0; i < weight.length; i++) {
			weightArray.add(weight[i]);
		}
		json.put("weight", weightArray);
		json.put("isVariant1", isVariant1);
		json.put("tolerance", tolerance);
		json.put("minLag", lagger.getMinLag());
		json.put("maxLag", lagger.getMaxLag());
		json.put("beginTimeMills", lagger.getBeginDate().getTimeInMillis());
		double[] minArray = normalizer.getMinArray();
		double[] maxArray = normalizer.getMaxArray();
		JSONArray min = new JSONArray();
		JSONArray max = new JSONArray();
		for (int i = 0; i < minArray.length; i++) {
			min.add(minArray[i]);
		}
		for (int i = 0; i < maxArray.length; i++) {
			max.add(maxArray[i]);
		}

		json.put("minArray", minArray);
		json.put("maxArray", maxArray);

		return json.toString();
	}

	/**
	 * @return the m_x1
	 */
	public double getM_x1() {
		return m_x1;
	}

	/**
	 * @param m_x1
	 *            the m_x1 to set
	 */
	public void setM_x1(double m_x1) {
		this.m_x1 = m_x1;
	}

	/**
	 * @return the m_x0
	 */
	public double getM_x0() {
		return m_x0;
	}

	/**
	 * @param m_x0
	 *            the m_x0 to set
	 */
	public void setM_x0(double m_x0) {
		this.m_x0 = m_x0;
	}

	/**
	 * @return the kernel
	 */
	public Kernel getKernel() {
		return kernel;
	}

	/**
	 * @param kernel
	 *            the kernel to set
	 */
	public void setKernel(Kernel kernel) {
		this.kernel = kernel;
	}

	/**
	 * @return the c
	 */
	public double getC() {
		return c;
	}

	/**
	 * @param c
	 *            the c to set
	 */
	public void setC(double c) {
		this.c = c;
	}

	/**
	 * @return the b
	 */
	public double getB() {
		return b;
	}

	/**
	 * @param b
	 *            the b to set
	 */
	public void setB(double b) {
		this.b = b;
	}

	/**
	 * @return the epsilon
	 */
	public double getEpsilon() {
		return epsilon;
	}

	/**
	 * @param epsilon
	 *            the epsilon to set
	 */
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	/**
	 * @return the nSeed
	 */
	public int getnSeed() {
		return nSeed;
	}

	/**
	 * @param nSeed
	 *            the nSeed to set
	 */
	public void setnSeed(int nSeed) {
		this.nSeed = nSeed;
	}

	/**
	 * @return the supportVectors
	 */
	public Instances getSupportVectors() {
		return supportVectors;
	}

	/**
	 * @param supportVectors
	 *            the supportVectors to set
	 */
	public void setSupportVectors(Instances supportVectors) {
		this.supportVectors = supportVectors;
	}

	/**
	 * @return the isVariant1
	 */
	public boolean isVariant1() {
		return isVariant1;
	}

	/**
	 * @param isVariant1
	 *            the isVariant1 to set
	 */
	public void setVariant1(boolean isVariant1) {
		this.isVariant1 = isVariant1;
	}

	/**
	 * @return the tolerance
	 */
	public double getTolerance() {
		return tolerance;
	}

	/**
	 * @param tolerance
	 *            the tolerance to set
	 */
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	/**
	 * @return the lagger
	 */
	public Lagger getLagger() {
		return lagger;
	}

	/**
	 * @param lagger
	 *            the lagger to set
	 */
	public void setLagger(Lagger lagger) {
		this.lagger = lagger;
	}

	/**
	 * @return the normalizer
	 */
	public Normalizer getNormalizer() {
		return normalizer;
	}

	/**
	 * @param normalizer
	 *            the normalizer to set
	 */
	public void setNormalizer(Normalizer normalizer) {
		this.normalizer = normalizer;
	}

	/**
	 * @return the type
	 */
	public FORECASTING_TYPE getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(FORECASTING_TYPE type) {
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public Service getService() {
		return service;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * @return the kpi
	 */
	public FORECASTING_KPI getKpi() {
		return kpi;
	}

	/**
	 * @param kpi
	 *            the kpi to set
	 */
	public void setKpi(FORECASTING_KPI kpi) {
		this.kpi = kpi;
	}

}

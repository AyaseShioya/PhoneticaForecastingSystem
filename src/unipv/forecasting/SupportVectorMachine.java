/**
 * 
 */
package unipv.forecasting;

//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import unipv.forecasting.CONFIGURATION.FORECASTER_CONFIGURATOR;
import unipv.forecasting.CONFIGURATION.FORECASTING_KPI;
import unipv.forecasting.CONFIGURATION.FORECASTING_TYPE;
import unipv.forecasting.CONFIGURATION.TRAINER_CONFIGURATOR;
import unipv.forecasting.forecaster.modelselection.Context;
import unipv.forecasting.forecaster.modelselection.LearningModel;
import unipv.forecasting.forecaster.modelselection.svm.SVM;
import unipv.forecasting.forecaster.modelselection.svm.SVMConfiguration;
import unipv.forecasting.preprocess.filters.EliminateWeekend;
import unipv.forecasting.preprocess.filters.EliminateZero;
import unipv.forecasting.preprocess.filters.Filter;
import unipv.forecasting.preprocess.filters.InterquartileRangeFilter;
import unipv.forecasting.preprocess.filters.SortInstances;
//import unipv.forecasting.utils.MetheUtilities;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class SupportVectorMachine {
	private Context system;
	private SVMConfiguration configuration;
	private JSONArray forecast;
	private JSONArray comparison;

	private double mse;
	private double rsd;
	private double re;

	public SupportVectorMachine(final FORECASTING_TYPE type,
			final FORECASTING_KPI kpi, final Service service) {
		this.configuration = new SVMConfiguration(type, kpi, service);
		LearningModel learningModel = new SVM(configuration);
		this.system = new Context(learningModel);
	}

	public boolean optimizeParameter(Instances oData) {
		Instances pData = batchFilter(oData);
		System.out.println(pData.numInstances());
		if (pData.numInstances() > 150) {
			configuration.configureTrainer(TRAINER_CONFIGURATOR.HCOM_2, pData,
					system);
			configuration.configureTrainer(TRAINER_CONFIGURATOR.PSOGA, pData,
					system);
			simpleTrain(pData);
			return true;
		} else {
			return false;
		}
	}

	public boolean train(Instances oData) {
		Instances pData = batchFilter(oData);
		configuration.configureTrainer(TRAINER_CONFIGURATOR.DATABASE, pData,
				system);
		if (configuration.getKernel() != null && pData.numInstances() > 150) {
			simpleTrain(pData);
			return true;
		} else {
			return false;
		}
	}

	private void simpleTrain(Instances pData) {
		system.createTrainer();
		system.setTrainingSample(pData);
		rsd = system.train();
		re = system.getRelativeError();
		mse = system.getMse();
		configuration.saveModel();
	}

//	public JSONObject forecast(Instances oData, final int endNode) {
//		Instances pData = batchFilter(oData);
////		 System.out.println(pData);
//		configuration.configureForecaster(FORECASTER_CONFIGURATOR.DATABASE,
//				pData, system);
//		if (configuration.getSupportVectors() == null
//				|| pData.numInstances() < 36) {
//			return null;
//		}
//
//		Instances act = new Instances(pData, 0);
//		Instances pro = new Instances(pData, 0);
//		Instances pred = null;
//
//		Instances tempData = new Instances(oData);
//		Filter filter = new SortInstances();
//		filter = new SortInstances();
//		tempData = filter.doFilter(tempData);
//
//		filter = new EliminateWeekend();
//		tempData = filter.doFilter(tempData);
//
//		filter = new EliminateZero();
//		tempData = filter.doFilter(tempData);
//
//		act = cutLastCycle(tempData);
//		Instances history = batchFilter(pData);
//
//		system.createForecaster();
//		system.setForecastingSample(history);
//		int step = calculateStep(history, endNode);
////		System.out.println(step);
//		// System.out.println(history.numInstances());
//		Instances results = system.forecast(step);
//
//		forecast = foreTranslator(cutLastCycle(results));
//		// System.out.println(forecast);
//		pro = cutLastCycle(history);
//		for (int i = 0; i < CONFIGURATION.TIME_PERIOD; i++) {
//			history.remove(history.numInstances() - 1);
//		}
//
//		system.setForecastingSample(history);
//		step = CONFIGURATION.TIME_PERIOD;
//
//		pred = system.forecast(step);
////		System.out.println(pred);
//		pred = cutLastCycle(pred);
//
//		comparison = compTranslator(pred, act, pro);
//
//		JSONObject pack = new JSONObject();
//		pack.put("forecasting", forecast);
//		pack.put("comparison", comparison);
//
//		return pack;
//	}
	
	public JSONObject forecast(Instances oData, final int numberInDays) {
		Instances pData = batchFilter(oData);
//		 System.out.println(pData);
		configuration.configureForecaster(FORECASTER_CONFIGURATOR.DATABASE,
				pData, system);
		if (configuration.getSupportVectors() == null
				|| pData.numInstances() < 36) {
			return null;
		}

		Instances act = new Instances(pData, 0);
		Instances pro = new Instances(pData, 0);
		Instances pred = null;

		Instances tempData = new Instances(oData);
		Filter filter = new SortInstances();
		filter = new SortInstances();
		tempData = filter.doFilter(tempData);

		filter = new EliminateWeekend();
		tempData = filter.doFilter(tempData);

		filter = new EliminateZero();
		tempData = filter.doFilter(tempData);

		act = cutLastCycle(tempData);
		Instances history = batchFilter(pData);

		system.createForecaster();
		system.setForecastingSample(history);
		int step = (numberInDays + 1) * 12;
//		System.out.println(step);
		// System.out.println(history.numInstances());
		Instances results = system.forecast(step);

		forecast = foreTranslator(cutLastCycle(results));
		// System.out.println(forecast);
		pro = cutLastCycle(history);
		for (int i = 0; i < CONFIGURATION.TIME_PERIOD; i++) {
			history.remove(history.numInstances() - 1);
		}

		system.setForecastingSample(history);
		step = CONFIGURATION.TIME_PERIOD;

		pred = system.forecast(12);
//		System.out.println(pred);
		pred = cutLastCycle(pred);

		comparison = compTranslator(pred, act, pro);

		JSONObject pack = new JSONObject();
		pack.put("forecasting", forecast);
		pack.put("comparison", comparison);

		return pack;
	}

	private JSONArray foreTranslator(final Instances fore) {
		// String target = null;
		// String type = null;
		// switch(configuration.getKpi()) {
		// case TRAFFIC:
		// target = "Total Number of Calls";
		// break;
		// case ATT:
		// target = "Average Talking Time";
		// break;
		// }
		// switch (configuration.getType()) {
		// case SERVICE:
		// type = "Service";
		// break;
		// case SKILLSET:
		// type = "Skillset";
		// break;
		// case SERVICE_COM:
		// type = "Service Combination";
		// break;
		// case SKILLSET_COM:
		// type = "Skillset Combination";
		// break;
		// }
		// JSONObject resultSets = new JSONObject();
//		Calendar calendar = new GregorianCalendar();
		// Double timeMillis = fore.get(fore.numInstances() -
		// 1).value(CONFIGURATION.REPT_DATE_INDEX);
		// calendar.setTimeInMillis(timeMillis.longValue());
		// resultSets.put("date", calendar.get(Calendar.YEAR) + "-" +
		// (calendar.get(Calendar.MONTH) + 1) + "-" +
		// calendar.get(Calendar.DATE));
		// JSONObject json = new JSONObject();

		// JSONObject forecastingInfos = new JSONObject();
		// forecastingInfos.put("numRows", fore.numInstances());
		// forecastingInfos.put("date", calendar.get(Calendar.YEAR) + "-" +
		// (calendar.get(Calendar.MONTH) + 1) + "-" +
		// calendar.get(Calendar.DATE));
		// forecastingInfos.put("type", type);
		// forecastingInfos.put("target", target);
		// json.put("foreInfos", forecastingInfos);

		// JSONArray metaData = new JSONArray();
		// JSONObject time = new JSONObject();
		// time.put("index", 0);
		// time.put("type", "date");
		// time.put("name", "Time");
		// JSONObject forecasting = new JSONObject();
		// time.put("index", 1);
		// time.put("type", "numeric");
		// time.put("name", "Forecasting");
		// metaData.add(time);
		// metaData.add(forecasting);
		// json.put("metaData", metaData);

		JSONArray result = new JSONArray();

		int hour = 8;
		for (int i = 0; i < fore.numInstances(); i++) {
			JSONObject row = new JSONObject();
//			Double hour = fore.get(i).value(0);
//			calendar.setTimeInMillis(hour.longValue());
//
//			row.put("time", calendar.get(Calendar.HOUR_OF_DAY));
			row.put("time", hour++);
			row.put("fore", (fore.get(i).value(1)));
			row.put("aftercall", CONFIGURATION.TIME_AFTER_CALL);
			result.add(row);
		}
		// resultSets.put("resultSets", result);

		// json.put("resultSets", resultSets);

		return result;
	}

	private JSONArray compTranslator(final Instances fore, final Instances act,
			final Instances pro) {
		// String target = null;
		// String type = null;
		// switch(configuration.getKpi()) {
		// case TRAFFIC:
		// target = "Traffic";
		// break;
		// case ATT:
		// target = "Average Talking Time";
		// break;
		// }
		// switch (configuration.getType()) {
		// case SERVICE:
		// type = "Service";
		// break;
		// case SKILLSET:
		// type = "Skillset";
		// break;
		// case SERVICE_COM:
		// type = "Service Combination";
		// break;
		// case SKILLSET_COM:
		// type = "Skillset Combination";
		// break;
		// }
		// JSONObject resultSets = new JSONObject();
//		Calendar calendar = new GregorianCalendar();
		// Double timeMillis = act.get(act.numInstances() -
		// 1).value(CONFIGURATION.REPT_DATE_INDEX);
		// calendar.setTimeInMillis(timeMillis.longValue());
		// resultSets.put("date", calendar.get(Calendar.YEAR) + "-" +
		// (calendar.get(Calendar.MONTH) + 1) + "-" +
		// calendar.get(Calendar.DATE));
		// JSONObject json = new JSONObject();

		// JSONObject forecastingInfos = new JSONObject();
		// forecastingInfos.put("numRows", fore.numInstances());
		// forecastingInfos.put("date", calendar.get(Calendar.YEAR) + "-" +
		// (calendar.get(Calendar.MONTH) + 1) + "-" +
		// calendar.get(Calendar.DATE));
		// forecastingInfos.put("type", type);
		// forecastingInfos.put("target", target);
		// json.put("foreInfos", forecastingInfos);

		// JSONArray metaData = new JSONArray();
		// JSONObject time = new JSONObject();
		// time.put("index", 0);
		// time.put("type", "date");
		// time.put("name", "Time");
		// JSONObject actuals = new JSONObject();
		// time.put("index", 1);
		// time.put("type", "numeric");
		// time.put("name", "Actuals");
		// JSONObject processed = new JSONObject();
		// time.put("index", 2);
		// time.put("type", "numeric");
		// time.put("name", "Pocessed");
		// JSONObject forecasting = new JSONObject();
		// time.put("index", 3);
		// time.put("type", "numeric");
		// time.put("name", "Forecasting");
		// metaData.add(time);
		// metaData.add(actuals);
		// metaData.add(processed);
		// metaData.add(forecasting);
		// json.put("metaData", metaData);

		JSONArray result = new JSONArray();

		int hour = 8;
		for (int i = 0; i < fore.numInstances(); i++) {
			JSONObject row = new JSONObject();
//			Double hour = fore.get(i).value(0);
//			calendar.setTimeInMillis(hour.longValue());
//			row.put("time", calendar.get(Calendar.HOUR_OF_DAY));
			row.put("time", hour++);
			row.put("act", act.get(i).value(1));
			row.put("pro", pro.get(i).value(1));
			row.put("fore", fore.get(i).value(1));
			result.add(row);
		}
		// json.put("resultSets", resultSets);

		// resultSets.put("resultSets", result);
		return result;
	}

	private Instances batchFilter(final Instances oData) {
		Instances result = oData;
		Filter filter = null;
		if (result.numInstances() > 0) {
			filter = new SortInstances();
			result = filter.doFilter(result);
		}

		filter = new EliminateWeekend();
		result = filter.doFilter(result);

		filter = new EliminateZero();
		result = filter.doFilter(result);

		if (result.numInstances() > 0) {
			filter = new InterquartileRangeFilter();
			result = filter.doFilter(result);
		}
		// System.out.println(result.numInstances());
		return result;
	}

//	private int calculateStep(Instances history, final int endNode) {
//		SimpleDateFormat dateFormatter = new SimpleDateFormat(
//				CONFIGURATION.DATE_FORMAT_DAILY);
//		SimpleDateFormat hourFormatter = new SimpleDateFormat(
//				CONFIGURATION.DATE_FORMAT_HOURLY);
//		double start = configuration.getLagger().getBeginDate()
//				.getTimeInMillis();
//
//		Date now = new Date();
//		String endString = dateFormatter.format(now) + " " + endNode;
////		System.out.println(endString);
//		double end = 0;
//		try {
//			end = (hourFormatter.parse(endString)).getTime();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		int lastHistoricalIndex = MetheUtilities.translateDateToIndex(
//				history.get(history.numInstances() - 1).value(
//						CONFIGURATION.REPT_DATE_INDEX), start);
//		int endIndex = MetheUtilities.translateDateToIndex(end, start);
////		System.out.println(lastHistoricalIndex);
//		return endIndex - lastHistoricalIndex;
//	}

	private Instances cutLastCycle(Instances data) {
		Instances pData = new Instances(data, 0);
		for (int i = 0; i < CONFIGURATION.TIME_PERIOD; i++) {
			pData.add(data.get(data.numInstances() - CONFIGURATION.TIME_PERIOD
					+ i));
		}
		return pData;
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

	/**
	 * @return the mse
	 */
	public double getMse() {
		return mse;
	}

	/**
	 * @param mse
	 *            the mse to set
	 */
	public void setMse(double mse) {
		this.mse = mse;
	}

	/**
	 * @return the rsd
	 */
	public double getRsd() {
		return rsd;
	}

	/**
	 * @param rsd
	 *            the rsd to set
	 */
	public void setRsd(double rsd) {
		this.rsd = rsd;
	}

	/**
	 * @return the re
	 */
	public double getRe() {
		return re;
	}

	/**
	 * @param re
	 *            the re to set
	 */
	public void setRe(double re) {
		this.re = re;
	}

	/**
	 * @return the forecast
	 */
	public JSONArray getForecast() {
		return forecast;
	}

	/**
	 * @param forecast
	 *            the forecast to set
	 */
	public void setForecast(JSONArray forecast) {
		this.forecast = forecast;
	}

	/**
	 * @return the comparison
	 */
	public JSONArray getComparison() {
		return comparison;
	}

	/**
	 * @param comparison
	 *            the comparison to set
	 */
	public void setComparison(JSONArray comparison) {
		this.comparison = comparison;
	}

}

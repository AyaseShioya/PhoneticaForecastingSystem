/**
 * 
 */
package unipv.forecasting;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import net.sf.json.JSONObject;

import unipv.forecasting.CONFIGURATION.DAO_APPROACH;
import unipv.forecasting.CONFIGURATION.FORECASTING_KPI;
import unipv.forecasting.CONFIGURATION.FORECASTING_TYPE;
import unipv.forecasting.CONFIGURATION.TRANSLATOR;
import unipv.forecasting.dao.DAOClient;
import unipv.forecasting.dao.database.DatabaseInitializer;
import unipv.forecasting.dao.database.ListConnector;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class ForecastingClient {
	private List list;
	private SupportVectorMachine traffic;
	private SupportVectorMachine att;
	private Service selection;
	private FORECASTING_TYPE type;

	public ForecastingClient() {
		this.list = new List();
	}

	public ForecastingClient(final boolean needInitialize) {
		if (needInitialize) {
			DatabaseInitializer initializer = new DatabaseInitializer();
			initializer.initialize();
			this.list = new List();
			updateService();
		} else {
			this.list = new List();
		}
	}

	public HashMap<Integer, String> generateList(final FORECASTING_TYPE type) {
		this.type = type;
		return list.listNames(type);
	}

	public HashMap<Integer, String> generateAllList(final FORECASTING_TYPE type) {
		return list.listNames(type);
	}

	public HashMap<Integer, String> generateAvaliableList(
			final FORECASTING_TYPE type) {
		this.type = type;
		return list.listAvaliable(type);
	}

	public HashMap<Integer, String> generatePriorityList(
			final FORECASTING_TYPE type, final int priority) {
		this.type = type;
		return list.listPriority(type, priority);
	}

	public Service select(final int id) {
		this.selection = list.select(id, type);
		traffic = new SupportVectorMachine(type, FORECASTING_KPI.TRAFFIC,
				selection);
		att = new SupportVectorMachine(type, FORECASTING_KPI.ATT, selection);
		return selection;
	}

	public boolean updatePriority(final int priority) {
		ListConnector connector = new ListConnector();
		boolean result = connector.updatePriority(type, selection.getId(),
				priority);
		return result;
	}
	
	public boolean delete() {
		ListConnector connector = new ListConnector();
		boolean result = connector.delete(type, selection.getId());
		
		if(result) {
			HashMap<Integer, Service> serviceList = null;
			
			switch (type) {
			case SERVICE:
				serviceList = list.getServices();
				break;
			case SKILLSET:
				serviceList = list.getSkillsets();
				break;
			case SERVICE_COM:
				serviceList = list.getServiceComs();
				break;
			case SKILLSET_COM:
				serviceList = list.getSkillsetComs();
				break;
			}
			
			serviceList.remove(selection.getId());
		}
		return result;
	}

	public boolean train() {
		Instances trafficData = readTrainingData(FORECASTING_KPI.TRAFFIC);
		Instances attData = readTrainingData(FORECASTING_KPI.ATT);
		if (trafficData == null || attData == null) {
			return false;
		}
		traffic.train(trafficData);
		att.train(attData);
		list.load();
		return true;
	}

	public JSONObject forecast(final int numberInDays) {
		JSONObject pack = new JSONObject();
		Instances trafficData = readForecasintData(FORECASTING_KPI.TRAFFIC);
		Instances attData = readForecasintData(FORECASTING_KPI.ATT);

		// System.out.println(trafficData.numInstances() + "," +
		// attData.numInstances());

		if (trafficData == null || attData == null) {
			pack.put("success", false);
			return null;
		}
		JSONObject trafficPred = traffic.forecast(trafficData, numberInDays);
		JSONObject attPred = att.forecast(attData, numberInDays);
		pack.put("success", true);

		JSONObject result = new JSONObject();
		if (trafficPred != null && attPred != null) {
			result.put("trafficForecasting",
					trafficPred.getJSONArray("forecasting"));
			result.put("trafficComparison",
					trafficPred.getJSONArray("comparison"));
			result.put("attForecasting", attPred.getJSONArray("forecasting"));
			result.put("attComparison", attPred.getJSONArray("comparison"));
		} else {
			pack.put("success", false);
		}

		pack.put("result", result);
		return pack;
	}

	public boolean optimizeParameter() {
		Instances trafficData = readTrainingData(FORECASTING_KPI.TRAFFIC);
		Instances attData = readTrainingData(FORECASTING_KPI.ATT);
		// System.out.println(trafficData.numInstances());
		if (trafficData == null || attData == null
				|| trafficData.numInstances() == 0
				|| attData.numInstances() == 0) {
			return false;
		}
		boolean result_traffic = traffic.optimizeParameter(trafficData);
		boolean result_att = att.optimizeParameter(attData);
		if (result_traffic && result_att) {
			list.load();
			return true;
		} else {
			return false;
		}
	}

	public List optimizeAll() {
		HashMap<Integer, Service> services = optimizeOneType(
				FORECASTING_TYPE.SERVICE, true);
		HashMap<Integer, Service> skillsets = optimizeOneType(
				FORECASTING_TYPE.SKILLSET, true);
		HashMap<Integer, Service> serviceComs = optimizeOneType(
				FORECASTING_TYPE.SERVICE_COM, true);
		HashMap<Integer, Service> skillsetComs = optimizeOneType(
				FORECASTING_TYPE.SKILLSET_COM, true);
		return new List(services, skillsets, serviceComs, skillsetComs);
	}

	public HashMap<Integer, Service> optimizeOneType(
			final FORECASTING_TYPE type, final boolean optimizeAll) {
		HashMap<Integer, Service> result = new HashMap<Integer, Service>();
		HashMap<Integer, String> serviceList = generateList(type);
		for (int id : serviceList.keySet()) {
			select(id);
			// System.out.println(selection.getId() + ":" + selection.getName()
			// + " Finished!!");
			if (!selection.isInitialized() || optimizeAll) {
				boolean isSuccessful = optimizeParameter();
				if (!isSuccessful) {
					result.put(selection.getId(), selection);
					// System.out.println("Throw " + selection.getName() +
					// " !!!");
				}
				// System.out.println(selection.getName() + " Finished!!");
			}
		}
		return result;
	}

	private HashMap<Integer, Service> optimizeOnePriority(
			final FORECASTING_TYPE type, final int priority) {
		HashMap<Integer, Service> result = new HashMap<Integer, Service>();
		HashMap<Integer, String> serviceList = generatePriorityList(type,
				priority);
		for (int id : serviceList.keySet()) {
			select(id);
			// System.out.println(selection.getId() + ":" + selection.getName()
			// + " Finished!!");
			boolean isSuccessful = optimizeParameter();
			if (!isSuccessful) {
				result.put(selection.getId(), selection);
				// System.out.println("Throw " + selection.getName() + " !!!");
			}
			// System.out.println(selection.getName() + " Finished!!");
		}
		return result;
	}

	public List optimizeNew() {
		HashMap<Integer, Service> services = optimizeOneType(
				FORECASTING_TYPE.SERVICE, false);
		HashMap<Integer, Service> skillsets = optimizeOneType(
				FORECASTING_TYPE.SKILLSET, false);
		HashMap<Integer, Service> serviceComs = optimizeOneType(
				FORECASTING_TYPE.SERVICE_COM, false);
		HashMap<Integer, Service> skillsetComs = optimizeOneType(
				FORECASTING_TYPE.SKILLSET_COM, false);
		return new List(services, skillsets, serviceComs, skillsetComs);
	}

	public List optimizeByPriority(final int priority) {
		HashMap<Integer, Service> services = optimizeOnePriority(
				FORECASTING_TYPE.SERVICE, priority);
		HashMap<Integer, Service> skillsets = optimizeOnePriority(
				FORECASTING_TYPE.SKILLSET, priority);
		HashMap<Integer, Service> serviceComs = optimizeOnePriority(
				FORECASTING_TYPE.SERVICE_COM, priority);
		HashMap<Integer, Service> skillsetComs = optimizeOnePriority(
				FORECASTING_TYPE.SKILLSET_COM, priority);
		return new List(services, skillsets, serviceComs, skillsetComs);
	}

	public List trainAll() {
		HashMap<Integer, Service> services = trainOneType(FORECASTING_TYPE.SERVICE);
		HashMap<Integer, Service> skillsets = trainOneType(FORECASTING_TYPE.SKILLSET);
		HashMap<Integer, Service> serviceComs = trainOneType(FORECASTING_TYPE.SERVICE_COM);
		HashMap<Integer, Service> skillsetComs = trainOneType(FORECASTING_TYPE.SKILLSET_COM);
		return new List(services, skillsets, serviceComs, skillsetComs);
	}

	public HashMap<Integer, Service> trainOneType(final FORECASTING_TYPE type) {
		HashMap<Integer, Service> result = new HashMap<Integer, Service>();
		HashMap<Integer, String> serviceList = generateList(type);
		for (int id : serviceList.keySet()) {
			select(id);
			if (selection.isInitialized()) {
				boolean isSuccessful = train();
				if (!isSuccessful) {
					result.put(selection.getId(), selection);
				}
			}
		}
		return result;
	}

	private HashMap<Integer, Service> trainOnePriority(
			final FORECASTING_TYPE type, final int priority) {
		HashMap<Integer, Service> result = new HashMap<Integer, Service>();
		HashMap<Integer, String> serviceList = generatePriorityList(type,
				priority);
		for (int id : serviceList.keySet()) {
			select(id);
			if (selection.isInitialized()) {
				boolean isSuccessful = train();
				if (!isSuccessful) {
					result.put(selection.getId(), selection);
				}
			}
		}
		return result;
	}

	public List trainByPriority(final int priority) {
		HashMap<Integer, Service> services = trainOnePriority(
				FORECASTING_TYPE.SERVICE, priority);
		HashMap<Integer, Service> skillsets = trainOnePriority(
				FORECASTING_TYPE.SKILLSET, priority);
		HashMap<Integer, Service> serviceComs = trainOnePriority(
				FORECASTING_TYPE.SERVICE_COM, priority);
		HashMap<Integer, Service> skillsetComs = trainOnePriority(
				FORECASTING_TYPE.SKILLSET_COM, priority);
		return new List(services, skillsets, serviceComs, skillsetComs);
	}

	public List updateService() {
		HashMap<Integer, Service> services = list
				.update(FORECASTING_TYPE.SERVICE);
		HashMap<Integer, Service> skillsets = list
				.update(FORECASTING_TYPE.SKILLSET);
		list.load();
		return new List(services, skillsets);
	}

	public boolean insertCombination(final String name,
			final FORECASTING_TYPE type, final int priority,
			final HashMap<Integer, String> services) {
		return list.insertCombination(name, type, priority, services);
	}

	private Instances readForecasintData(final FORECASTING_KPI kpi) {
		Calendar c = new GregorianCalendar();
		c.add(Calendar.DATE, -1);
		String yesterday = c.get(Calendar.YEAR) + "-"
				+ addZero(c.get(Calendar.MONTH) + 1) + "-"
				+ addZero(c.get(Calendar.DATE));
//		System.out.println(yesterday);
		c.add(Calendar.DATE, -6);
		String lastWeek = c.get(Calendar.YEAR) + "-"
				+ addZero(c.get(Calendar.MONTH) + 1) + "-"
				+ addZero(c.get(Calendar.DATE));

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("time_from", lastWeek);
		parameters.put("time_to", yesterday);
		// parameters.put("time_from", "2011-12-29");
		// parameters.put("time_to", "2012-01-04");
		parameters.put("parameter", selection.generateCDAParameter(type, kpi));
		return read(kpi, parameters);
	}

	private Instances readTrainingData(final FORECASTING_KPI kpi) {
		Calendar c = new GregorianCalendar();
		c.add(Calendar.DATE, -1);
		String yesterday = c.get(Calendar.YEAR) + "-"
				+ addZero(c.get(Calendar.MONTH) + 1) + "-"
				+ addZero(c.get(Calendar.DATE));
		c.add(Calendar.MONTH, -CONFIGURATION.TRINGING_SPAN);
		String lastYear = c.get(Calendar.YEAR) + "-"
				+ addZero(c.get(Calendar.MONTH) + 1) + "-"
				+ addZero(c.get(Calendar.DATE));

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("time_from", lastYear);
		parameters.put("time_to", yesterday);
		parameters.put("parameter", selection.generateCDAParameter(type, kpi));
		// System.out.println(selection.generateCDAParameter(type, kpi));
		return read(kpi, parameters);
	}

	private Instances read(final FORECASTING_KPI kpi,
			HashMap<String, String> parameters) {
		DAOClient client = null;
		String address = null;
		switch (kpi) {
		case TRAFFIC:
			client = new DAOClient(DAO_APPROACH.CDA, TRANSLATOR.HOURLY_SUM);
			address = "TRAFFIC";
			break;
		case ATT:
			client = new DAOClient(DAO_APPROACH.CDA, TRANSLATOR.HOURLY_AVG);
			address = "ATT";
			break;
		}
		Instances result = client.getInstances(address, parameters);

		if (result == null)
			return null;
		return result;
	}

	private String addZero(final int number) {
		String result = "";
		if (number > 9) {
			result += number;
		} else {
			result += "0" + number;
		}
		return result;
	}

	public List getList() {
		return list;
	}

	/**
	 * @return the selection
	 */
	public Service getSelection() {
		return selection;
	}

	/**
	 * @param selection
	 *            the selection to set
	 */
	public void setSelection(Service selection) {
		this.selection = selection;
	}

}

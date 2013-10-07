/**
 * 
 */
package unipv.forecasting;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import unipv.forecasting.CONFIGURATION.FORECASTING_KPI;
import unipv.forecasting.CONFIGURATION.FORECASTING_TYPE;

/**
 * @author Quest
 * 
 */
public class ServiceCombination extends Service {
	private ArrayList<Integer> serviceID;
	private ArrayList<String> serviceName;

	public ServiceCombination(int id, String name) {
		super(id, name);
	}

	public ServiceCombination(int id, String name, String lastoptimization,
			String lastTraining, boolean isInitialized, int priority) {
		super(id, name, lastoptimization, lastTraining, isInitialized, priority);
	}

	@SuppressWarnings("incomplete-switch")
	public String generateCDAParameter(final FORECASTING_TYPE type,
			final FORECASTING_KPI kpi) {
		String header = null;

		switch (kpi) {
		case TRAFFIC:
			switch (type) {
			case SERVICE_COM:
				header = "[Customer].[Company]";
				break;
			case SKILLSET_COM:
				header = "[Skillset].[Skillset]";
				break;
			default:
				break;
			}
			break;
		case ATT:
			switch (type) {
			case SERVICE_COM:
				header = "[Customer].[Company]";
				break;
			case SKILLSET_COM:
				header = "[SkillSet].[Skillset]";
				break;
			}
			break;
		}

		String parameter = "{";
		for (int i = 0; i < serviceName.size(); i++) {
			parameter += header + ".[" + serviceName.get(i) + "],";
		}
		parameter = parameter.substring(0, parameter.length() - 1);
		parameter += "}";
		return parameter;
	}

	public JSONArray generateContentList() {
		JSONArray result = new JSONArray();
		for (int i = 0; i < serviceID.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("name", serviceName.get(i));
			row.put("id", serviceID.get(i));
			result.add(row);
		}
		return result;
	}

	/**
	 * @return the serviceID
	 */
	public ArrayList<Integer> getServiceID() {
		return serviceID;
	}

	/**
	 * @param serviceID
	 *            the serviceID to set
	 */
	public void setServiceID(ArrayList<Integer> serviceID) {
		this.serviceID = serviceID;
	}

	/**
	 * @return the serviceName
	 */
	public ArrayList<String> getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName
	 *            the serviceName to set
	 */
	public void setServiceName(ArrayList<String> serviceName) {
		this.serviceName = serviceName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.Service#getId()
	 */
	@Override
	public int getId() {
		return super.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.Service#setId(int)
	 */
	@Override
	public void setId(int id) {
		super.setId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.Service#getName()
	 */
	@Override
	public String getName() {
		return super.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.Service#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		super.setName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.Service#getLastoptimization()
	 */
	@Override
	public String getLastoptimization() {
		return super.getLastoptimization();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.Service#setLastoptimization(java.util.Date)
	 */
	@Override
	public void setLastoptimization(String lastoptimization) {
		super.setLastoptimization(lastoptimization);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.Service#getLastTraining()
	 */
	@Override
	public String getLastTraining() {
		return super.getLastTraining();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.Service#setLastTraining(java.util.Date)
	 */
	@Override
	public void setLastTraining(String lastTraining) {
		super.setLastTraining(lastTraining);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.Service#isInitialized()
	 */
	@Override
	public boolean isInitialized() {
		return super.isInitialized();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.Service#setInitialized(boolean)
	 */
	@Override
	public void setInitialized(boolean isInitialized) {
		super.setInitialized(isInitialized);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.Service#getPriority()
	 */
	@Override
	public int getPriority() {
		return super.getPriority();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.Service#setPriority(int)
	 */
	@Override
	public void setPriority(int priority) {
		super.setPriority(priority);
	}

}

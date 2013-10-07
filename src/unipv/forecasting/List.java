/**
 * 
 */
package unipv.forecasting;

import java.util.ArrayList;
import java.util.HashMap;

import unipv.forecasting.CONFIGURATION.FORECASTING_TYPE;
import unipv.forecasting.dao.database.ListConnector;

/**
 * @author Quest
 * 
 */
public class List {

	private HashMap<Integer, Service> services;
	private HashMap<Integer, Service> skillsets;
	private HashMap<Integer, Service> serviceComs;
	private HashMap<Integer, Service> skillsetComs;

	public List() {
		load();
	}

	public List(HashMap<Integer, Service> services,
			HashMap<Integer, Service> skillsets,
			HashMap<Integer, Service> serviceComs,
			HashMap<Integer, Service> skillsetComs) {
		super();
		this.services = services;
		this.skillsets = skillsets;
		this.serviceComs = serviceComs;
		this.skillsetComs = skillsetComs;
	}

	public List(HashMap<Integer, Service> services,
			HashMap<Integer, Service> skillsets) {
		super();
		this.services = services;
		this.skillsets = skillsets;
	}

	public Service select(final int id, final FORECASTING_TYPE type) {
		Service service = null;
		switch (type) {
		case SERVICE:
			service = services.get(id);
			break;
		case SKILLSET:
			service = skillsets.get(id);
			break;
		case SERVICE_COM:
			service = serviceComs.get(id);
			break;
		case SKILLSET_COM:
			service = skillsetComs.get(id);
			break;
		}
		return service;
	}

	public HashMap<Integer, String> listAvaliable(final FORECASTING_TYPE type) {
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		HashMap<Integer, Service> cList = null;
		switch (type) {
		case SERVICE:
			cList = services;
			break;
		case SKILLSET:
			cList = skillsets;
			break;
		case SERVICE_COM:
			cList = serviceComs;
			break;
		case SKILLSET_COM:
			cList = skillsetComs;
			break;
		}

		for (int id : cList.keySet()) {
			if (cList.get(id).isInitialized()
					&& cList.get(id).getPriority() != 0)
				result.put(id, cList.get(id).getName());
		}
		return result;
	}

	public HashMap<Integer, String> listPriority(final FORECASTING_TYPE type,
			final int priority) {
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		HashMap<Integer, Service> cList = null;
		switch (type) {
		case SERVICE:
			cList = services;
			break;
		case SKILLSET:
			cList = skillsets;
			break;
		case SERVICE_COM:
			cList = serviceComs;
			break;
		case SKILLSET_COM:
			cList = skillsetComs;
			break;
		}

		for (int id : cList.keySet()) {
			if (cList.get(id).getPriority() == priority)
				result.put(id, cList.get(id).getName());
		}
		return result;
	}

	public HashMap<Integer, String> listNames(final FORECASTING_TYPE type) {
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		HashMap<Integer, Service> cList = null;
		switch (type) {
		case SERVICE:
			cList = services;
			break;
		case SKILLSET:
			cList = skillsets;
			break;
		case SERVICE_COM:
			cList = serviceComs;
			break;
		case SKILLSET_COM:
			cList = skillsetComs;
			break;
		}

		for (int id : cList.keySet()) {
			if (cList.get(id).getPriority() != 0) {
				result.put(id, cList.get(id).getName());
			}
		}
		return result;
	}

	public ArrayList<Integer> listIDs(final FORECASTING_TYPE type) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		HashMap<Integer, Service> cList = null;
		switch (type) {
		case SERVICE:
			cList = services;
			break;
		case SKILLSET:
			cList = skillsets;
			break;
		case SERVICE_COM:
			cList = serviceComs;
			break;
		case SKILLSET_COM:
			cList = skillsetComs;
			break;
		}

		for (int id : cList.keySet()) {
			if (cList.get(id).getPriority() != 0) {
				result.add(id);
			}
		}
		return result;
	}

	public void load() {
		ListConnector connector = new ListConnector();
		this.services = connector.selectList(FORECASTING_TYPE.SERVICE);
		this.skillsets = connector.selectList(FORECASTING_TYPE.SKILLSET);
		this.serviceComs = connector
				.selectCombinationList(FORECASTING_TYPE.SERVICE_COM);
		this.skillsetComs = connector
				.selectCombinationList(FORECASTING_TYPE.SKILLSET_COM);
	}

	public HashMap<Integer, Service> update(final FORECASTING_TYPE type) {
		ListConnector connector = new ListConnector();
		HashMap<Integer, String> nList = connector.update(this, type);
		HashMap<Integer, Service> newList = null;
		if (nList != null) {
			newList = new HashMap<Integer, Service>();
			for (int id : nList.keySet()) {
				Service service = new Service(id, nList.get(id));
				newList.put(id, service);
			}
		}
		return newList;
	}

	public boolean insertCombination(final String name,
			final FORECASTING_TYPE type, final int priority,
			final HashMap<Integer, String> services) {
		ListConnector connector = new ListConnector();
		boolean result = connector.insertCombination(name, type, priority,
				services);
		load();
		return result;
	}

	/**
	 * @return the services
	 */
	public HashMap<Integer, Service> getServices() {
		return services;
	}

	/**
	 * @param services the services to set
	 */
	public void setServices(HashMap<Integer, Service> services) {
		this.services = services;
	}

	/**
	 * @return the skillsets
	 */
	public HashMap<Integer, Service> getSkillsets() {
		return skillsets;
	}

	/**
	 * @param skillsets the skillsets to set
	 */
	public void setSkillsets(HashMap<Integer, Service> skillsets) {
		this.skillsets = skillsets;
	}

	/**
	 * @return the serviceComs
	 */
	public HashMap<Integer, Service> getServiceComs() {
		return serviceComs;
	}

	/**
	 * @param serviceComs the serviceComs to set
	 */
	public void setServiceComs(HashMap<Integer, Service> serviceComs) {
		this.serviceComs = serviceComs;
	}

	/**
	 * @return the skillsetComs
	 */
	public HashMap<Integer, Service> getSkillsetComs() {
		return skillsetComs;
	}

	/**
	 * @param skillsetComs the skillsetComs to set
	 */
	public void setSkillsetComs(HashMap<Integer, Service> skillsetComs) {
		this.skillsetComs = skillsetComs;
	}
	
	
}

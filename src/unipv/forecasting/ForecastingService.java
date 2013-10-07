package unipv.forecasting;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import unipv.forecasting.CONFIGURATION.FORECASTING_TYPE;
import unipv.forecasting.forecaster.erlang.ErlangCModel;
import unipv.forecasting.forecaster.erlang.ErlangResult;

public class ForecastingService {

	private ForecastingClient client;

	public ForecastingService(final boolean needInitiation) {
		this.client = new ForecastingClient(needInitiation);
	}

	public JSONArray getAvaliableList(final int command) {
		FORECASTING_TYPE type = null;
		/** translate type **/
		switch (command) {
		case 1:
			type = FORECASTING_TYPE.SERVICE;
			break;
		case 2:
			type = FORECASTING_TYPE.SKILLSET;
			break;
		case 3:
			type = FORECASTING_TYPE.SERVICE_COM;
			break;
		case 4:
			type = FORECASTING_TYPE.SKILLSET_COM;
			break;
		}

		/** retrieve list **/
		HashMap<Integer, String> list = client.generateAvaliableList(type);

		/** translate to JSONArray **/
		JSONArray items = new JSONArray();
		for (int id : list.keySet()) {
			JSONObject item = new JSONObject();
			item.put("selectionID", id);
			item.put("selectionName", list.get(id));
			items.add(item);
		}
		return items;
	}

	public JSONArray getPriorityList(final int command) {
		FORECASTING_TYPE type = null;
		int priority = -1;
		switch (command) {
		case 5:
		case 6:
		case 7:
			priority = command - 4;
			type = FORECASTING_TYPE.SERVICE;
			break;
		case 8:
		case 9:
		case 10:
			priority = command - 7;
			type = FORECASTING_TYPE.SKILLSET;
			break;
		case 11:
		case 12:
		case 13:
			priority = command - 10;
			type = FORECASTING_TYPE.SERVICE_COM;
			break;
		case 14:
		case 15:
		case 16:
			priority = command - 13;
			type = FORECASTING_TYPE.SKILLSET_COM;
			break;
		case 17:
			priority = 0;
			type = FORECASTING_TYPE.SERVICE;
			break;
		case 18:
			priority = 0;
			type = FORECASTING_TYPE.SKILLSET;
			break;
		case 19:
			priority = 0;
			type = FORECASTING_TYPE.SERVICE_COM;
			break;
		case 20:
			priority = 0;
			type = FORECASTING_TYPE.SKILLSET_COM;
			break;
		}
		// System.out.println("type:" + type + " priority:" + priority);

		/** retrieve list **/
		HashMap<Integer, String> list = client.generatePriorityList(type,
				priority);

		/** translate to JSONArray **/
		JSONArray items = new JSONArray();
		for (int id : list.keySet()) {
			JSONObject item = new JSONObject();
			item.put("selectionID", id);
			item.put("selectionName", list.get(id));
			items.add(item);
		}
		return items;
	}

	public JSONArray getOptionalList(final int command) {
		FORECASTING_TYPE type = null;
		/** translate type **/
		switch (command) {
		case 1:
			type = FORECASTING_TYPE.SERVICE;
			break;
		case 2:
			type = FORECASTING_TYPE.SKILLSET;
			break;
		case -1:
			return null;
		}

		/** retrieve list **/
		HashMap<Integer, String> list = client.generateAllList(type);

		/** translate to JSONArray **/
		JSONArray items = new JSONArray();
		for (int id : list.keySet()) {
			JSONObject item = new JSONObject();
			item.put("selectionID", id);
			item.put("selectionName", list.get(id));
			items.add(item);
		}
		return items;
	}

	public JSONObject select(final int id) {
		Service service = client.select(id);
		JSONObject serviceInfo = new JSONObject();
		serviceInfo.put("name", service.getName());
		serviceInfo.put("lastoptimization", service.getLastoptimization());
		serviceInfo.put("lastTraining", service.getLastTraining());
		serviceInfo.put("priority", service.getPriority());
		serviceInfo.put("content", service.generateContentList());
		return serviceInfo;
	}

	public JSONObject forecast(final int numberInDays) {
		JSONObject result = client.forecast(numberInDays);
		if (result == null)
			return new JSONObject();
		return result;
	}

	public JSONObject optimizeThis(final int numberInDays) {
		if (client.optimizeParameter())
			return forecast(numberInDays);
		else
			return new JSONObject();
	}

	public JSONObject trainThis(final int numberInDays) {
		if (client.train())
			return forecast(numberInDays);
		else
			return new JSONObject();
	}

	public JSONArray calculateWorkforce(final double traffic,
			final double avgTalkingTime, final double avgBackOfficeTime,
			final double targetSL, final double targetAWT) {
		ErlangCModel model = new ErlangCModel(traffic, avgTalkingTime,
				avgBackOfficeTime);
		ArrayList<ErlangResult> result = model.calculateNumAgents(targetSL,
				targetAWT);
		JSONArray json = new JSONArray();
		for (int i = 0; i < result.size(); i++) {
			json.add(result.get(i).toJSON());
		}
		return json;
	}

	public JSONObject updatePriority(final int priority) {
		Service service = client.getSelection();
		JSONObject json = new JSONObject();

		boolean result = client.updatePriority(priority);
		if (result) {
			service.setPriority(priority);
		}

		json.put("name", service.getName());
		json.put("lastoptimization", service.getLastoptimization());
		json.put("lastTraining", service.getLastTraining());
		json.put("content", service.generateContentList());
		json.put("priority", service.getPriority());

		return json;
	}
	
	public JSONObject delete() {
		JSONObject json = new JSONObject();

		boolean result = client.delete();
		if (result) {
			json.put("trafficForecasting", "Deleted");
		} else {
			json.put("trafficForecasting", "Failed");
		}
		return json;
	}

	public JSONObject insertCombination(final int command, final String name,
			final int priority, final JSONArray listJSON) {
		FORECASTING_TYPE type = null;
		/** translate type **/
		switch (command) {
		case 1:
			type = FORECASTING_TYPE.SERVICE_COM;
			break;
		case 2:
			type = FORECASTING_TYPE.SKILLSET_COM;
			break;
		case -1:
			return null;
		}

		HashMap<Integer, String> services = new HashMap<Integer, String>();
		for (int i = 0; i < listJSON.size(); i++) {
			JSONObject row = listJSON.getJSONObject(i);
			services.put(row.getInt("selectionID"),
					row.getString("selectionName"));
		}
		JSONObject result = new JSONObject();
		if (client.insertCombination(name, type, priority, services))
			result.put("success", "Yes");
		else
			result.put("success", "No");
		return result;
	}

	// public static void main(String[] args) throws Exception {
	// ForecastingService system = new ForecastingService(false);
	// system.getList(3);
	// System.out.println(system.select(1));
	// }

}

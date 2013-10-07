/**
 * 
 */
package unipv.forecasting;

import net.sf.json.JSONArray;
import unipv.forecasting.CONFIGURATION.FORECASTING_KPI;
import unipv.forecasting.CONFIGURATION.FORECASTING_TYPE;

/**
 * @author Quest
 * 
 */
public class Service {
	private int id;
	private String name;
	private String lastoptimization;
	private String lastTraining;
	private int priority;
	private boolean isInitialized;

	public Service(int id, String name) {
		this.id = id;
		this.name = name;
		this.isInitialized = false;
	}

	public Service(int id, String name, String lastoptimization,
			String lastTraining, boolean isInitialized, int priority) {
		super();
		this.id = id;
		this.name = name;
		this.lastoptimization = lastoptimization;
		this.lastTraining = lastTraining;
		this.isInitialized = isInitialized;
		this.priority = priority;
	}

	@SuppressWarnings("incomplete-switch")
	public String generateCDAParameter(final FORECASTING_TYPE type,
			final FORECASTING_KPI kpi) {
		String parameter = "";

		switch (kpi) {
		case TRAFFIC:
			switch (type) {
			case SERVICE:
				parameter += "[Customer].[Company]";
				break;
			case SKILLSET:
				parameter += "[Skillset].[Skillset]";
				break;
			}
			break;
		case ATT:
			switch (type) {
			case SERVICE:
				parameter += "[Customer].[Company]";
				break;
			case SKILLSET:
				parameter += "[SkillSet].[Skillset]";
				break;
			}
			break;
		}
		parameter += ".[" + name + "]";
		return parameter;
	}

	public JSONArray generateContentList() {
		JSONArray result = new JSONArray();
		return result;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the lastoptimization
	 */
	public String getLastoptimization() {
		return lastoptimization;
	}

	/**
	 * @param lastoptimization
	 *            the lastoptimization to set
	 */
	public void setLastoptimization(String lastoptimization) {
		this.lastoptimization = lastoptimization;
	}

	/**
	 * @return the lastTraining
	 */
	public String getLastTraining() {
		return lastTraining;
	}

	/**
	 * @param lastTraining
	 *            the lastTraining to set
	 */
	public void setLastTraining(String lastTraining) {
		this.lastTraining = lastTraining;
	}

	/**
	 * @return the isInitialized
	 */
	public boolean isInitialized() {
		return isInitialized;
	}

	/**
	 * @param isInitialized
	 *            the isInitialized to set
	 */
	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

}

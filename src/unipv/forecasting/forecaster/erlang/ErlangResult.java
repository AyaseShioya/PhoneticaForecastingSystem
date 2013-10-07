/**
 * 
 */
package unipv.forecasting.forecaster.erlang;

import net.sf.json.JSONObject;

/**
 * @author AyaseShioya
 * 
 */
public class ErlangResult {
	private double serviceLevel;
	private int numberOfAgents;
	private double queuingProbability;
	private double avgSpeedOfAnswer;
	private double avgNumRequestsInQueue;
	private double avgNumRequestsInSystem;
	private double avgRequestTimeInSystem;
	private double operatorOccupation;

	public ErlangResult(double serviceLevel, int numberOfAgents,
			double queuingProbability, double avgSpeedOfAnswer,
			double avgNumRequestsInQueue, double avgNumRequestsInSystem,
			double avgRequestTimeInSystem, double operatorOccupation) {
		this.serviceLevel = serviceLevel;
		this.numberOfAgents = numberOfAgents;
		this.queuingProbability = queuingProbability;
		this.avgSpeedOfAnswer = avgSpeedOfAnswer;
		this.avgNumRequestsInQueue = avgNumRequestsInQueue;
		this.avgNumRequestsInSystem = avgNumRequestsInSystem;
		this.avgRequestTimeInSystem = avgRequestTimeInSystem;
		this.operatorOccupation = operatorOccupation;
	}

	/**
	 * @return the serviceLevel
	 */
	public double getServiceLevel() {
		return serviceLevel;
	}

	/**
	 * @param serviceLevel
	 *            the serviceLevel to set
	 */
	public void setServiceLevel(double serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	/**
	 * @return the numberOfAgents
	 */
	public int getNumberOfAgents() {
		return numberOfAgents;
	}

	/**
	 * @param numberOfAgents
	 *            the numberOfAgents to set
	 */
	public void setNumberOfAgents(int numberOfAgents) {
		this.numberOfAgents = numberOfAgents;
	}

	/**
	 * @return the queuingProbability
	 */
	public double getQueuingProbability() {
		return queuingProbability;
	}

	/**
	 * @param queuingProbability
	 *            the queuingProbability to set
	 */
	public void setQueuingProbability(double queuingProbability) {
		this.queuingProbability = queuingProbability;
	}

	/**
	 * @return the avgSpeedOfAnswer
	 */
	public double getAvgSpeedOfAnswer() {
		return avgSpeedOfAnswer;
	}

	/**
	 * @param avgSpeedOfAnswer
	 *            the avgSpeedOfAnswer to set
	 */
	public void setAvgSpeedOfAnswer(double avgSpeedOfAnswer) {
		this.avgSpeedOfAnswer = avgSpeedOfAnswer;
	}

	/**
	 * @return the avgNumRequestsInQueue
	 */
	public double getAvgNumRequestsInQueue() {
		return avgNumRequestsInQueue;
	}

	/**
	 * @param avgNumRequestsInQueue
	 *            the avgNumRequestsInQueue to set
	 */
	public void setAvgNumRequestsInQueue(double avgNumRequestsInQueue) {
		this.avgNumRequestsInQueue = avgNumRequestsInQueue;
	}

	/**
	 * @return the avgNumRequestsInSystem
	 */
	public double getAvgNumRequestsInSystem() {
		return avgNumRequestsInSystem;
	}

	/**
	 * @param avgNumRequestsInSystem
	 *            the avgNumRequestsInSystem to set
	 */
	public void setAvgNumRequestsInSystem(double avgNumRequestsInSystem) {
		this.avgNumRequestsInSystem = avgNumRequestsInSystem;
	}

	/**
	 * @return the avgRequestTimeInSystem
	 */
	public double getAvgRequestTimeInSystem() {
		return avgRequestTimeInSystem;
	}

	/**
	 * @param avgRequestTimeInSystem
	 *            the avgRequestTimeInSystem to set
	 */
	public void setAvgRequestTimeInSystem(double avgRequestTimeInSystem) {
		this.avgRequestTimeInSystem = avgRequestTimeInSystem;
	}

	/**
	 * @return the operatorOccupation
	 */
	public double getOperatorOccupation() {
		return operatorOccupation;
	}

	/**
	 * @param operatorOccupation
	 *            the operatorOccupation to set
	 */
	public void setOperatorOccupation(double operatorOccupation) {
		this.operatorOccupation = operatorOccupation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		result += "Number Of Agents: " + numberOfAgents + "\n";
		result += "Service Level: " + serviceLevel + "\n";
		result += "Queuing Probability: " + queuingProbability + "\n";
		result += "Average Speed Of Answer: " + avgSpeedOfAnswer + "\n";
		result += "Avergae Number of Requests In Queue: "
				+ avgNumRequestsInQueue + "\n";
		result += "Avergae Number of Requests In System: "
				+ avgNumRequestsInSystem + "\n";
		result += "Avergae: Requests Time In System" + avgRequestTimeInSystem
				+ "\n";
		result += "Operator Occupation: " + operatorOccupation + "\n";
		return result;
	}

	public JSONObject toJSON() {
		JSONObject result = new JSONObject();
		result.put("na", numberOfAgents);
		result.put("sl", serviceLevel);
		result.put("qp", queuingProbability);
		result.put("asa", avgSpeedOfAnswer);
		result.put("anrq", avgNumRequestsInQueue);
		result.put("anrs", avgNumRequestsInSystem);
		result.put("arts", avgRequestTimeInSystem);
		result.put("oo", operatorOccupation);
		return result;
	}
}

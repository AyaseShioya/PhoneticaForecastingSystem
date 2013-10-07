/**
 * The abstract class for All Erlang Model
 */
package unipv.forecasting.forecaster.erlang;

import java.util.ArrayList;

/**
 * @author Quest
 * 
 */
public class ErlangCModel {
	private double avgTalkingTime;
	private double avgBackOfficeTime;
	private double traffic;

	private double erlang;
	private double avgHandlingTime;

	private double targetAWT;
	private double serviceLevel;

	private int numberOfAgents;

	private double queuingProbability;
	private ArrayList<Double> sArray;

	private double avgSpeedOfAnswer;
	private double avgNumRequestsInQueue;
	private double avgNumRequestsInSystem;
	private double avgRequestTimeInSystem;
	private double operatorOccupation;

	public ErlangCModel(double traffic, double avgTalkingTime,
			double avgBackOfficeTime) {
		this.traffic = traffic / 3600;
		this.avgTalkingTime = avgTalkingTime;
		this.avgBackOfficeTime = avgBackOfficeTime;
		this.avgHandlingTime = avgTalkingTime + avgBackOfficeTime;
		this.erlang = this.traffic * this.avgHandlingTime;
	}

	public ArrayList<ErlangResult> calculateNumAgents(final double targetSL,
			final double targetAWT) {
		ArrayList<ErlangResult> results = new ArrayList<ErlangResult>();
		this.targetAWT = targetAWT;
		initiateS();
		boolean continueCondition1 = true, continueCondition2 = true;
		while (continueCondition1 && continueCondition2) {
			calculateQueuingProbability();
			calculateServiceLevel();
			continueCondition1 = serviceLevel < (targetSL / 100);
			updateSArray();
			numberOfAgents++;
		}
		for (int i = 0; i < 3; i++) {
			updateSArray();
		}
		numberOfAgents = numberOfAgents - 7 < erlang ? (int) erlang + 1
				: numberOfAgents - 7;
		while (numberOfAgents <= sArray.size()) {
			calculateQueuingProbability();
			calculateServiceLevel();
			results.add(batchCalculation());
			numberOfAgents++;
		}
		return results;
	}

	public void initiateS() {
		sArray = new ArrayList<Double>();
		sArray.add(1 / erlang);
		numberOfAgents = 0;
		while (numberOfAgents < erlang) {
			numberOfAgents++;
			updateSArray();
		}
	}

	public void updateSArray() {
		double newS = (1 + sArray.get(numberOfAgents - 1))
				* (sArray.size() + 1) / erlang;
		sArray.add(newS);
	}

	public void calculateAvgSpeedOfAnswer() {
		avgSpeedOfAnswer = queuingProbability * avgHandlingTime
				/ (numberOfAgents - erlang);
	}

	public void calculateAvgNumRequestsInQueue() {
		avgNumRequestsInQueue = erlang * queuingProbability
				/ (numberOfAgents - erlang);
	}

	public void calculateServiceLevel() {
		double exponent = (erlang - numberOfAgents) * targetAWT
				/ avgHandlingTime;
		serviceLevel = 1 - (queuingProbability * Math.pow(Math.E, exponent));
	}

	public void calculateAvgNumRequestsInSystem() {
		avgNumRequestsInSystem = erlang + avgNumRequestsInQueue;
	}

	public void calculateAvgRequestTimeInSystem() {
		avgRequestTimeInSystem = avgHandlingTime + avgSpeedOfAnswer;
	}

	public ErlangResult batchCalculation() {
		calculateAvgSpeedOfAnswer();
		calculateAvgNumRequestsInQueue();
		calculateAvgNumRequestsInSystem();
		calculateAvgRequestTimeInSystem();
		calculateOperatorOccupation();
		ErlangResult result = new ErlangResult(this.serviceLevel * 100,
				this.numberOfAgents, this.queuingProbability * 100,
				this.avgSpeedOfAnswer, this.avgNumRequestsInQueue,
				this.avgNumRequestsInSystem, this.avgRequestTimeInSystem,
				this.operatorOccupation);
		return result;
	}

	public void calculateQueuingProbability() {
		double s = sArray.get(numberOfAgents - 1);
		queuingProbability = (numberOfAgents - erlang) * s / numberOfAgents;
		queuingProbability += 1;
		queuingProbability = 1 / queuingProbability;
	}

	public void calculateOperatorOccupation() {
		operatorOccupation = erlang / numberOfAgents * 100.0;
	}

	/**
	 * @return the avgTalkingTime
	 */
	public double getAvgTalkingTime() {
		return avgTalkingTime;
	}

	/**
	 * @param avgTalkingTime
	 *            the avgTalkingTime to set
	 */
	public void setAvgTalkingTime(double avgTalkingTime) {
		this.avgTalkingTime = avgTalkingTime;
	}

	/**
	 * @return the avgBackOfficeTime
	 */
	public double getAvgBackOfficeTime() {
		return avgBackOfficeTime;
	}

	/**
	 * @param avgBackOfficeTime
	 *            the avgBackOfficeTime to set
	 */
	public void setAvgBackOfficeTime(double avgBackOfficeTime) {
		this.avgBackOfficeTime = avgBackOfficeTime;
	}

	/**
	 * @return the traffic
	 */
	public double getTraffic() {
		return traffic;
	}

	/**
	 * @param traffic
	 *            the traffic to set
	 */
	public void setTraffic(double traffic) {
		this.traffic = traffic;
	}

	/**
	 * @return the erlang
	 */
	public double getErlang() {
		return erlang;
	}

	/**
	 * @param erlang
	 *            the erlang to set
	 */
	public void setErlang(double erlang) {
		this.erlang = erlang;
	}

	/**
	 * @return the avgHandlingTime
	 */
	public double getAvgHandlingTime() {
		return avgHandlingTime;
	}

	/**
	 * @param avgHandlingTime
	 *            the avgHandlingTime to set
	 */
	public void setAvgHandlingTime(double avgHandlingTime) {
		this.avgHandlingTime = avgHandlingTime;
	}

	/**
	 * @return the targetAWT
	 */
	public double getTargetAWT() {
		return targetAWT;
	}

	/**
	 * @param targetAWT
	 *            the targetAWT to set
	 */
	public void setTargetAWT(double targetAWT) {
		this.targetAWT = targetAWT;
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
	 * @return the sArray
	 */
	public ArrayList<Double> getsArray() {
		return sArray;
	}

	/**
	 * @param sArray
	 *            the sArray to set
	 */
	public void setsArray(ArrayList<Double> sArray) {
		this.sArray = sArray;
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

}

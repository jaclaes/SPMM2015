package org.cheetahplatform.recommendation;






/**
 * This class represents a single recommendation result containing the activity and its do- and
 * don't value.
 * 
 * @author Christian Haisjackl
 */
public class RecommendationResult {
	private double sumDoTargetValue = 0.0;
	private double sumDoWeight = 0.0;
	private double sumDontTargetValue = 0.0;
	private double sumDontWeight = 0.0;
	private Trace trace;



	/**
	 * This method generates a recommendation result.
	 * 
	 * @param recommendedTrace The trace for suggested further execution steps.
	 */
	public RecommendationResult( Trace recommendedTrace ) {
		trace = recommendedTrace;
	}



	/**
	 * This method adds the target value and weight for calculating the do and don't value. The given
	 * target value is multiplied by the weight before adding to the sum of all target values.
	 * 
	 * @param targetValue The business value of the current trace
	 * @param weight The weight of the current trace
	 */
	public void addDoAndWeight( double targetValue, double weight ) {
		sumDoTargetValue += ( targetValue * weight );
		sumDoWeight += weight;
	}



	/**
	 * This method adds the target value and weight for calculating the do and don't value. The given
	 * target value is multiplied by the weight before adding to the sum of all target values.
	 * 
	 * @param targetValue The target value of the current trace
	 * @param weight The weight of the current trace
	 */
	public void addDontAndWeight( double targetValue, double weight ) {
		sumDontTargetValue += ( targetValue * weight );
		sumDontWeight += weight;
	}



	/**
	 * @return the doValue
	 */
	public Double getDoValue() {
		return sumDoTargetValue / sumDoWeight;
	}



	/**
	 * @return the dontValue
	 */
	public Double getDontValue() {
		return sumDontTargetValue / sumDontWeight;
	}



	/**
	 * @return the sumDoTargetValue
	 */
	public double getSumDoTargetValue() {
		return sumDoTargetValue;
	}



	/**
	 * @return the sumDoWeight
	 */
	public double getSumDoWeight() {
		return sumDoWeight;
	}



	/**
	 * This method returns the next suggested step(s)
	 * 
	 * @return the trace
	 */
	public Trace getTrace() {
		return trace;
	}
}

package org.cheetahplatform.recommendation.message;

import java.util.List;

/**
 * This message is used to ask the recommendation engine for a recommendation
 * 
 * @author Christian Haisjackl
 */
public class MsgGetRecommendation extends Message {
	private static final long serialVersionUID = 1L;
	private List<Long> activities;
	private List<Long> possibleTasks;


	/**
	 * This message is a request for recommendations.
	 * 
	 * @param activities The partial trace.
	 * @param possibleTasks The next tasks which can be executed
	 */
	public MsgGetRecommendation( List<Long> activities, List<Long> possibleTasks ){
		this.activities = activities;
		this.possibleTasks = possibleTasks;
	}


	/**
	 * The activities already executed are returned by this method
	 * 
	 * @return the activities
	 */
	public List<Long> getActivties(){
		return activities;
	}


	/**
	 * The possible next tasks are returned by this method.
	 * 
	 * @return the possibleTasks
	 */
	public List<Long> getPossibleTasks(){
		return possibleTasks;
	}
}

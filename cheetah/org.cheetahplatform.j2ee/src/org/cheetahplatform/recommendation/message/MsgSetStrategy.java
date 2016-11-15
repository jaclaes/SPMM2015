package org.cheetahplatform.recommendation.message;

import java.util.List;

/**
 * This message configures the recommendation engine
 * 
 * @author Christian Haisjackl
 */
public class MsgSetStrategy extends Message {
	private static final long serialVersionUID = 1L;
	private String strategy;
	private String logFile;
	private String target;
	private boolean calcMaxValue;
	private String eventState;
	private List<Object> additionalParams;


	/**
	 * This message holds the values for setting a new abstraction.
	 * 
	 * @param strategy The class of the abstraction
	 * @param logFile The log file to use
	 * @param target The target value
	 * @param calcMaxValue If the maximum or the minimum of the target value should be used for the target function
	 * @param eventState The event state to use
	 * @param additionalParams A list of additional parameters for the abstraction
	 */
	public MsgSetStrategy( String strategy, String logFile, String target, boolean calcMaxValue, String eventState, List<Object> additionalParams ){
		this.strategy = strategy;
		this.logFile = logFile;
		this.target = target;
		this.calcMaxValue = calcMaxValue;
		this.eventState = eventState;
		this.additionalParams = additionalParams;
	}


	/**
	 * This is the package and class name of the strategy to use.
	 * 
	 * @return the strategy
	 */
	public String getStrategy(){
		return strategy;
	}


	/**
	 * This is the new log to use.
	 * 
	 * @return the logFile
	 */
	public String getLogFile(){
		return logFile;
	}


	/**
	 * This is the package name and class name of the target value to use.
	 * 
	 * @return the target
	 */
	public String getTarget(){
		return target;
	}


	/**
	 * This defines if the maximum or minimum should be derived.
	 * 
	 * @return the calcMaxValue
	 */
	public boolean getCalcMaxValue(){
		return calcMaxValue;
	}


	/**
	 * This defines which event state should be selected in the log.
	 * 
	 * @return the eventState
	 */
	public String getEventState(){
		return eventState;
	}


	/**
	 * This method returns a list of additional attributes, free defined how to use.
	 * 
	 * @return the additionalParams
	 */
	public List<Object> getAdditionalParams(){
		return additionalParams;
	}

}

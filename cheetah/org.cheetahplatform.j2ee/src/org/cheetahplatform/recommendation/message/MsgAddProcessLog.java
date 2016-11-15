package org.cheetahplatform.recommendation.message;

/**
 * This message adds a log to the recommendation engine
 * 
 * @author Christian Haisjackl
 */
public class MsgAddProcessLog extends Message {
	private static final long serialVersionUID = 1L;
	private String log;
	private String logName;


	/**
	 * Creates a new message
	 * 
	 * @param logName The name of the log (file)
	 * @param log The log file in string representation
	 */
	public MsgAddProcessLog( String logName, String log ){
		this.log = log;
		this.logName = logName;
	}


	/**
	 * Returns the log.
	 * 
	 * @return the string representation of the log
	 */
	public String getLog(){
		return log;
	}


	/**
	 * Returns the name of the log.
	 * 
	 * @return the log Name
	 */
	public String getLogname(){
		return logName;
	}
}

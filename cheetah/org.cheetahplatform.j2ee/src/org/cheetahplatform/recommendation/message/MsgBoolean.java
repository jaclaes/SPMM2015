package org.cheetahplatform.recommendation.message;

/**
 * This message is used to send a boolean variable.
 * 
 * @author Christian Haisjackl
 */
public class MsgBoolean extends Message {
	private static final long serialVersionUID = 1L;
	private boolean msg;


	/**
	 * Creates a boolean Message.
	 * 
	 * @param msg The boolean value to send
	 */
	public MsgBoolean( boolean msg ){
		this.msg = msg;
	}


	/**
	 * Returns the value of the message.
	 * 
	 * @return The value of the message
	 */
	public boolean getMessage(){
		return msg;
	}
}

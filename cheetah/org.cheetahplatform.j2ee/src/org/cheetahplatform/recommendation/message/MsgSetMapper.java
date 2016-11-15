/**
 * 
 */
package org.cheetahplatform.recommendation.message;

import java.util.List;

/**
 * This message sets a mapper for a specific PAIS
 * 
 * @author Christian Haisjackl
 */
public class MsgSetMapper extends Message {
	private static final long serialVersionUID = 1L;
	private String mapper;
	private String actorname;
	private List<String> names;
	private List<Long> ids;
	private List<Object> attr;


	/**
	 * Creates a new message.
	 * 
	 * @param mapper The path of the mapper to use
	 * @param actor The actor of the system
	 * @param names The names of the activities
	 * @param ids The IDs of the activities
	 * @param additionalAttributes Additional attributes
	 */
	public MsgSetMapper( String mapper, String actor, List<String> names, List<Long> ids, List<Object> additionalAttributes ){
		this.mapper = mapper;
		actorname = actor;
		this.names = names;
		this.ids = ids;
		attr = additionalAttributes;
	}


	/**
	 * This method returns the package and class name of the mapper.
	 * 
	 * @return the mapper
	 */
	public String getMapper(){
		return mapper;
	}


	/**
	 * This method returns the actor of the PAIS.
	 * 
	 * @return the actor
	 */
	public String getActor(){
		return actorname;
	}


	/**
	 * This message returns the names of the activities.
	 * 
	 * @return the names
	 */
	public List<String> getNames(){
		return names;
	}


	/**
	 * This method returns the IDs of the activities.
	 * 
	 * @return the IDs
	 */
	public List<Long> getIds(){
		return ids;
	}


	/**
	 * This method returns a list of additional attributes, free defined how to use.
	 * 
	 * @return the attr
	 */
	public List<Object> getAdditionalAttributes(){
		return attr;
	}

}

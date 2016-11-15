package org.cheetahplatform.recommendation;



import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.List;



/**
 * The mapper is used to transform a log of a PAIS to a log readable for the recommendation engine.
 * 
 * @author Christian Haisjackl
 */
public abstract class Mapper {
	/** The actor */
	protected String actor;
	/** The name of the activities to map */
	protected List<String> actNames = null;
	/** The IDs of the activities to map */
	protected List<Long> actIDs = null;



	/**
	 * Creates a new mapper.
	 * 
	 * @param actor The actor
	 * @param actNames The names to replace
	 * @param actIDs the IDs to use
	 */
	protected Mapper( String actor, List<String> actNames, List<Long> actIDs ) {
		this.actNames = actNames;
		this.actIDs = actIDs;
		this.actor = actor;
	}



	/**
	 * This method returns a mapper for mapping logs of other systems to a PMS log.
	 * 
	 * @param mapper The path of the mapper
	 * @param actor The actor
	 * @param actNames The list of names to change
	 * @param actIDs The list of IDs to change
	 * @param additionalAttributes Not used yet
	 * @return The mapper
	 * @throws Exception If something went wrong
	 */
	public static Mapper getInstance( String mapper, String actor, List<String> actNames, List<Long> actIDs, List<Object> additionalAttributes ) throws Exception {
		Class<?> c = Class.forName( mapper );
		Constructor<?>[] cons = c.getDeclaredConstructors();
		return (Mapper) cons[0].newInstance( new Object[] {actor, actNames, actIDs, additionalAttributes} );
	}



	/**
	 * This class maps a log file of another system to a log file for the recommendation engine's
	 * log.
	 * 
	 * @param logFile The log file to work with
	 * @return An input stream for the logger
	 * @throws Exception If something goes wrong
	 */
	public abstract InputStream map( File logFile ) throws Exception;
}

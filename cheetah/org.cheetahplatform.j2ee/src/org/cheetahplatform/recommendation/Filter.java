package org.cheetahplatform.recommendation;



import java.lang.reflect.Constructor;




/**
 * This class represents a filter for removing activities in a trace. Which activities are removed
 * from the trace depends on the used filter.
 * 
 * @author Christian Haisjackl
 */
public abstract class Filter {
	/** The attribute which has to be filtered */
	protected String attribute;



	/**
	 * Creates a new filter.
	 * 
	 * @param attribute The attribute to search for
	 */
	protected Filter( String attribute ) {
		this.attribute = attribute;
	}



	/**
	 * This method returns a new instance of a filter.
	 * 
	 * @param filter The class of the filter
	 * @param attribute The attribute the class should have
	 * @return a new filter
	 * @throws Exception If the filter could not be found or something else get wrong
	 */
	public static Filter getInstance( String filter, String attribute ) throws Exception {
		Class<?> c = Class.forName( filter );
		Constructor<?>[] cons = c.getDeclaredConstructors();
		return (Filter) cons[0].newInstance( new Object[] {attribute} );
	}



	/**
	 * This method filters some activities out of a trace and returns them in a new trace.
	 * 
	 * @param trace The trace to filter
	 * @param filterValue The value of the attribute which should be filtered
	 * @param order If the value of the attribute has to be larger/smaller/equals the filteredValue
	 * @return A new trace containing only the filtered log entries
	 */
	public abstract Trace filter( Trace trace, String filterValue, int order );
}

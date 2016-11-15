package org.cheetahplatform.recommendation;



import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Vector;


import pmsedit.logmodel.LogEntry;
import pmsedit.logmodel.Process;
import pmsedit.logmodel.ProcessInstance;




/**
 * The strategy calculates the do- and don't values of the activities returned by the miners.
 * 
 * @author Christian Haisjackl
 */
public abstract class Strategy {
	/** The scale how the recommendations shall be sorted. */
	protected TargetFunction targetFunction;
	/** The traces of the process instance. */
	protected List<Trace> traces;



	/**
	 * Creates a new strategy.
	 * 
	 * @param log The log to use
	 * @param target The scale to use
	 * @param eventState The state of the event to check
	 */
	protected Strategy( Log log, TargetFunction target, String eventState ) {
		this.targetFunction = target;
		traces = new Vector<Trace>();
		addTraces( log, target, eventState );
	}



	/**
	 * This method returns a wanted strategy.
	 * 
	 * @param strategy the class name of the abstraction
	 * @param log The log to use
	 * @param target The scale to use
	 * @param eventState The state of the event to check
	 * @param additionalParams if the abstraction needs additional parameters
	 * @return A new abstraction
	 * @throws Exception If the initialization failed
	 */
	public static Strategy getInstance( String strategy, Log log, TargetFunction target, String eventState, List<Object> additionalParams ) throws Exception {
		Class<?> c = Class.forName( strategy );
		Constructor<?>[] cons = c.getDeclaredConstructors();
		return (Strategy) cons[0].newInstance(log, target, eventState, additionalParams);
	}



	/**
	 * This method updates the traces with the new log. the old one will be deleted
	 * 
	 * @param log The log to use
	 * @param target The scale to use
	 * @param eventState The state of the event to check
	 */
	public void addTraces( Log log, TargetFunction target, String eventState ) {
		List<Trace> tmp = new Vector<Trace>();

		for( Process p : log.getLog() ) {
			for( ProcessInstance pi : p.getProcessInstances() ) {
				Trace t;
				double gtv = target.getGlobalTargetValue( pi );
				if( Double.isNaN( gtv ) )
					t = new Trace( target );
				else
					t = new Trace( target, gtv );
				for( LogEntry le : pi.getLogEntries() ) {
					if( !le.getEventType().getName().equals( eventState ) )
						continue;
					t.addEntry( le );
				}
				tmp.add( t );
			}
		}
		traces.addAll( tmp );
	}



	/**
	 * This method returns the list of logged traces
	 * 
	 * @return The traces
	 */
	public List<Trace> getTraces() {
		return traces;
	}



	/**
	 * This method evaluates the recommendations for the given process by the given partial trace
	 * 
	 * @param partialTrace The trace which has already been executed
	 * @param possibleNextTasks The next task which can are enabled
	 * @return The recommendation result
	 */
	public abstract RecommendationResultList getRecommendations( Trace partialTrace, List<LogEntry> possibleNextTasks );
}

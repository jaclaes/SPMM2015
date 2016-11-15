package org.cheetahplatform.recommendation;



import java.lang.reflect.Constructor;
import java.util.List;


import pmsedit.logmodel.LogEntry;




/**
 * The miner searches the log for similar traces and assignes them a weight before the result set is
 * returned to the strategy.
 * 
 * @author Christian Haisjackl
 */
public abstract class Miner {
	/** The abstraction the miner is used by */
	protected Strategy strategy;



	/**
	 * Creates a new miner.
	 * 
	 * @param strategy The strategy the miner is used by
	 */
	protected Miner( Strategy strategy ) {
		this.strategy = strategy;
	}



	/**
	 * This method returns a miner for mining a trace.
	 * 
	 * @param miner The path of the miner
	 * @param strategy The strategy which is using the miner at the moment.
	 * @return The miner
	 * @throws Exception If something went wrong
	 */
	public static Miner getInstance( String miner, Strategy strategy ) throws Exception {
		Class<?> c = Class.forName( miner );
		Constructor<?>[] cons = c.getDeclaredConstructors();
		return (Miner) cons[0].newInstance( new Object[] {strategy} );
	}



	/**
	 * This method mines a trace for the fitting entries. Not all attributes are used every time.
	 * 
	 * @param trace The trace to mine in
	 * @param partialTrace The already executed tasks
	 * @param maxNumOfEntries The maximal number of following entries to return. If set to 0, every
	 *           entry will be returned.
	 * @param possibleNextTasks The possible next tasks to execute
	 * @return A list of traces, holding recommendation results and weights
	 */
	public abstract List<Trace> mine( Trace trace, Trace partialTrace, int maxNumOfEntries, List<LogEntry> possibleNextTasks );
}

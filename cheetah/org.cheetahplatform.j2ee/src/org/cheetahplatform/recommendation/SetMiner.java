package org.cheetahplatform.recommendation;



import java.util.List;
import java.util.Vector;


import pmsedit.logmodel.LogEntry;



/**
 * This miner returns a copy of the trace, if the partial trace fits, and sets the weight of the
 * trace. If no possible next task is not contained in the trace, the miner returns null.
 * 
 * @author Christian Haisjackl
 */
public class SetMiner extends Miner {
	/**
	 * Creates a new Set Miner.
	 * 
	 * @param strategy not used
	 */
	protected SetMiner( Strategy strategy ) {
		super( strategy );
	}



	/**
	 * @param maxNumOfEntries not used
	 * @param possibleNextTasks The possible next tasks to execute
	 * @return A copy of the trace with the proper weight or null if the trace contains not a
	 *         possible next task
	 * @see org.cheetahplatform.recommendation.Miner#mine(org.cheetahplatform.recommendation.Trace, org.cheetahplatform.recommendation.Trace, int, java.util.List)
	 */
	@Override
	public List<Trace> mine( Trace trace, Trace partialTrace, int maxNumOfEntries, List<LogEntry> possibleNextTasks ) {
		List<LogEntry> t = trace.getEntries();
		List<LogEntry> pt = partialTrace.getEntries();
		double numOfFittingEntries = 0;
		boolean containsPossibleNextTask = false;
		for( LogEntry e : possibleNextTasks )
			containsPossibleNextTask = containsPossibleNextTask || trace.contains( e );
		// if possible next task is not contained, return null
		if( !containsPossibleNextTask )
			return null;

		// reduce doubled entries from trace
		Trace reducedTrace = new Trace( trace.getTargetFunction() );
		for( LogEntry e : pt ) {
			if( !reducedTrace.contains( e ) )
				reducedTrace.addEntry( e );
		}

		List<LogEntry> rt = reducedTrace.getEntries();

		// count if log entry occurs in trace
		for( LogEntry e : rt ) {
			if( trace.contains( e ) )
				numOfFittingEntries++;
		}

		Trace tmp = new Trace( t, trace.getTargetFunction(), trace.getTargetValue() );
		tmp.setWeight( ( rt.size() == 0 ) ? 1 : numOfFittingEntries / rt.size() );
		List<Trace> l = new Vector<Trace>();
		l.add( tmp );
		return l;
	}
}

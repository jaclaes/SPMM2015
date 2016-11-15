package org.cheetahplatform.recommendation;



import java.util.HashMap;
import java.util.List;
import java.util.Vector;


import pmsedit.logmodel.LogEntry;



/**
 * This miner returns a trace containing recommended next tasks for this trace, if the partial trace
 * fits, and sets the weight of the trace. If no possible next task is not contained in the trace
 * the miner returns null.
 * 
 * @author Christian Haisjackl
 */
public class MultiSetMiner extends Miner {
	/**
	 * Creates a new Multi Set Miner.
	 * 
	 * @param strategy not used
	 */
	protected MultiSetMiner( Strategy strategy ) {
		super( strategy );
	}



	/**
	 * @param maxNumOfEntries not used
	 * @see org.cheetahplatform.recommendation.Miner#mine(org.cheetahplatform.recommendation.Trace, org.cheetahplatform.recommendation.Trace, int, java.util.List)
	 */
	@Override
	public List<Trace> mine( Trace trace, Trace partialTrace, int maxNumOfEntries, List<LogEntry> possibleNextTasks ) {
		List<LogEntry> pt = partialTrace.getEntries();
		HashMap<Long, Integer> numOfEntriesT = trace.getNumOfEntries();
		HashMap<Long, Integer> numOfEntriesPT = partialTrace.getNumOfEntries();

		double numOfFittingEntries = 0;
		Trace pnt = new Trace( trace.getTargetFunction() );

		// checking fitting possible next task
		for( LogEntry e : possibleNextTasks ) {
			long l = e.getTask();
			if( numOfEntriesT.get( l ) == null )
				continue;// not in current trace
			if( numOfEntriesPT.get( l ) == null )
				pnt.addEntry( e );// in current trace, but not in partial trace => can be suggested
			else if( numOfEntriesPT.get( l ) < numOfEntriesT.get( l ) )
				pnt.addEntry( e ); // in both traces but in partial trace fewer than in current trace
		}

		// no further suggestions for the non empty partial trace
		if( pnt.getEntries().size() == 0 && pt.size() > 0 )
			return null;

		// calculating numOfFittingEnteis
		for( Long l : numOfEntriesPT.keySet() ) {
			// partial trace entry is not contained in current trace
			if( numOfEntriesT.get( l ) == null )
				continue;
			int numPT = numOfEntriesPT.get( l );
			int numCT = numOfEntriesT.get( l );
			// maximal number is number of current trace entries
			numOfFittingEntries += ( numPT < numCT ) ? numPT : numCT;
		}

		pnt.setWeight( ( pt.size() == 0 ) ? 1 : numOfFittingEntries / pt.size() );
		List<Trace> l = new Vector<Trace>();
		l.add( pnt );
		return l;
	}

}

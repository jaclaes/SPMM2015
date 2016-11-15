package org.cheetahplatform.recommendation;



import java.util.List;
import java.util.Vector;


import pmsedit.logmodel.LogEntry;




/**
 * This miner checks if a partial trace fits this trace and returns following activities. If the
 * trace does not fit, null is returned. The partial trace fits only if for i=0...n entry[i] is the
 * same as in the trace.
 * 
 * @author Christian Haisjackl
 */
public class PrefixMiner extends Miner {
	/**
	 * Creates a new Prefix Miner.
	 * 
	 * @param strategy not used
	 */
	protected PrefixMiner( Strategy strategy ) {
		super( strategy );
	}



	/**
	 * @param maxNumOfEntries The maximal number of following entries to return. If set to 0, every
	 *           entry will be returned.
	 * @param possibleNextTasks not used
	 * @return NULL, if trace does not fit, an empty list if there are no further activities
	 *         afterwards else a list of following activities on the partial trace.
	 * @see org.cheetahplatform.recommendation.Miner#mine(Trace, Trace, int, List)
	 */
	@Override
	public List<Trace> mine( Trace trace, Trace partialTrace, int maxNumOfEntries, List<LogEntry> possibleNextTasks ) {
		List<LogEntry> t = trace.getEntries();
		List<LogEntry> pt = partialTrace.getEntries();
		if( pt.size() > t.size() ) // if trace is longer than actual.
			return null;
		List<LogEntry> entries = new Vector<LogEntry>();
		int i = 0;

		// checking if trace has the same partial part
		while( i < pt.size() ) {
			LogEntry pte = pt.get( i );
			LogEntry e = t.get( i );
			if( pte.getTask() != e.getTask() )
				return null;
			i++;
		}

		// setting maximal number of entries to be returned
		int j = ( maxNumOfEntries == 0 ) ? t.size() : maxNumOfEntries;
		while( i < t.size() && j > 0 ) {
			entries.add( t.get( i ) );
			i++;
			j--;
		}
		Trace result = new Trace( entries, trace.getTargetFunction() );
		result.setWeight( 1.0 );
		List<Trace> l = new Vector<Trace>();
		l.add( result );
		return l;
	}
}

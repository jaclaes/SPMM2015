package org.cheetahplatform.recommendation;



import java.util.List;
import java.util.Vector;


import pmsedit.logmodel.LogEntry;



/**
 * This miner checks if a partial trace fits a sub trace and returns following activities. If the
 * trace does not fit, null is returned. If the partial trace fits more often than once, every
 * fitting successors are returned
 * 
 * @author Christian Haisjackl
 */
public class SubTraceMiner extends Miner {
	/**
	 * Creates a new Sub Trace Miner
	 * 
	 * @param strategy not used
	 */
	protected SubTraceMiner( Strategy strategy ) {
		super( strategy );
	}



	/**
	 * @param maxNumOfEntries Maximal trace size to return
	 * @param possibleNextTasks not used
	 * @return NULL, if trace does not fit at all, an empty list if there are no further activities
	 *         afterwards else a list of lists with following activities on the partial trace.
	 * @see org.cheetahplatform.recommendation.Miner#mine(org.cheetahplatform.recommendation.Trace, org.cheetahplatform.recommendation.Trace, int, java.util.List)
	 */
	@Override
	public List<Trace> mine( Trace trace, Trace partialTrace, int maxNumOfEntries, List<LogEntry> possibleNextTasks ) {
		List<LogEntry> t = trace.getEntries();
		List<LogEntry> pt = partialTrace.getEntries();
		if( pt.size() > t.size() ) // if trace is longer than actual.
			return null;

		List<Trace> results = new Vector<Trace>();

		for( int i = 0; i < t.size(); i++ ) {
			// if there can not be any more fitting traces cause partial trace is longer than remaining
			// trace.
			if( t.size() - i < pt.size() )
				break;

			int j = 0;
			while( j < pt.size() ) {
				LogEntry pte = pt.get( j );
				LogEntry e = t.get( j + i );

				// if the element is not fitting, set break condition
				if( pte.getTask() != e.getTask() )
					j = pt.size();
				j++;
			}

			// if there is no fitting trace. If trace fits but has no more following entries j ==
			// partialTrace.trace.size()
			if( j > pt.size() )
				continue;

			// System.out.println( "Trace.getPartialFollowingEntries: " + j );

			List<LogEntry> entries = new Vector<LogEntry>();

			int max = ( maxNumOfEntries == 0 ) ? t.size() : maxNumOfEntries;
			int count = 0;

			// only add following entries
			j += i;
			while( j < t.size() && count < max ) {
				entries.add( t.get( j ) );
				j++;
				count++;
			}
			Trace tmp = new Trace( entries, trace.getTargetFunction() );
			tmp.setWeight( 1 );
			results.add( tmp );
		}

		return results;
	}

}

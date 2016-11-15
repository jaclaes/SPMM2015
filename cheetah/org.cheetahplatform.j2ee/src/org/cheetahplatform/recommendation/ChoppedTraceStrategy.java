package org.cheetahplatform.recommendation;



import java.util.List;
import java.util.Vector;


import pmsedit.logmodel.LogEntry;




/**
 * This strategy gives chunks of the partial trace to the sub trace miner to derive the
 * recommendations. A window of the size n glides over the partial trace and the activities in the
 * window are handed over to the miner.
 * 
 * @author Christian Haisjackl
 */
public class ChoppedTraceStrategy extends Strategy {
	private Miner miner;
	private int chunkSize = 1;



	/**
	 * Creates a Chopped Trace Strategy.
	 * 
	 * @param log The log to use
	 * @param target The scale to use
	 * @param eventState The state of the event to check
	 * @param additionalParams [Integer chunksize]
	 */
	public ChoppedTraceStrategy( Log log, TargetFunction target, String eventState, List<Object> additionalParams ) {
		super( log, target, eventState );
		chunkSize = ( (Integer) additionalParams.get( 0 ) ).intValue();
		if( chunkSize < 1 )
			chunkSize = 1;
		try {
			miner = Miner.getInstance( "log.miner.SubTraceMiner", this );
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}



	/**
	 * @see org.cheetahplatform.recommendation.Strategy#getRecommendations(org.cheetahplatform.recommendation.Trace, java.util.List)
	 */
	@Override
	public RecommendationResultList getRecommendations( Trace partialTrace, List<LogEntry> possibleNextTasks ) {
		List<RecommendationResult> results = new Vector<RecommendationResult>();
		// only next tasks can be the result
		for( LogEntry e : possibleNextTasks ) {
			Trace tmp = new Trace( targetFunction );
			tmp.addEntry( e ); // result set only contains possible next steps
			RecommendationResult rr = new RecommendationResult( tmp );
			results.add( rr );
		}

		// creating the search junks for the miner
		List<Trace> searchTraces = new Vector<Trace>();
		if( partialTrace.getEntries().size() > chunkSize ) {
			List<LogEntry> ste = partialTrace.getEntries();
			for( int i = 0; i <= ste.size() - chunkSize; i++ ) {
				Trace searchTrace = new Trace( partialTrace.getTargetFunction() );
				for( int j = i; j < i + chunkSize; j++ )
					searchTrace.addEntry( ste.get( j ) );
				searchTraces.add( searchTrace );
			}
		} else
			searchTraces.add( partialTrace );

		for( Trace t : traces ) {
			List<Trace> l = new Vector<Trace>();
			// mining for every single junk and putting results together
			for( Trace st : searchTraces ) {
				List<Trace> tmp = miner.mine( t, st, 1, null );
				if( tmp == null || tmp.size() == 0 )
					continue;
				l.addAll( tmp );
			}
			// if traces are not similar next loop
			if( l.size() == 0 )
				continue;

			for( Trace recoTrace : l ) {

				// if further steps next loop
				if( recoTrace.getEntries().size() == 0 )
					continue;

				boolean isPNT = false;
				for( LogEntry e : possibleNextTasks ) {
					if( recoTrace.contains( e ) ) {
						isPNT = true;
						break;
					}
				}

				if( !isPNT )
					continue;

				// updating do and don't values
				for( RecommendationResult rr : results ) {
					if( recoTrace.contains( rr.getTrace().getEntries().get( 0 ) ) )
						rr.addDoAndWeight( t.getTargetValue(), 1 );
					else
						rr.addDontAndWeight( t.getTargetValue(), 1 );
				}
			}// end for (Trace recoTrace : t)
		}// end for( Trace t : traces )

		RecommendationResultList resultSet = new RecommendationResultList();
		for( RecommendationResult res : results ) {
			resultSet.addResult( res );
		}
		return resultSet;
	}
}

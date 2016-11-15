package org.cheetahplatform.recommendation;



import java.util.List;
import java.util.Vector;


import pmsedit.logmodel.LogEntry;



/**
 * This strategy uses the next n future activities returned by the miner for generating the
 * recommendations.
 * 
 * @author Christian Haisjackl
 */
public class PrefixLookaheadStrategy extends Strategy {
	private int lookAhead = 1;
	private Miner miner;



	/**
	 * Creates a Prefix Lookahead Strategy.
	 * 
	 * @param log The log to use
	 * @param target The scale to use
	 * @param eventState The state of the event to check
	 * @param additionalParams [Integer lookAheadvalue]
	 */
	protected PrefixLookaheadStrategy( Log log, TargetFunction target, String eventState, List<Object> additionalParams ) {
		super( log, target, eventState );
		lookAhead = ( (Integer) additionalParams.get( 0 ) ).intValue();
		if( lookAhead < 1 )
			lookAhead = 1;
		try {
			miner = Miner.getInstance( "log.miner.PrefixMiner", this );
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

		// checking traces if there exists already one similar
		for( Trace t : traces ) {
			List<Trace> l = miner.mine( t, partialTrace, lookAhead, null );
			// if traces are not similar next loop
			if( l == null )
				continue;

			Trace recoTrace = l.get( 0 );
			// if no further steps next loop
			if( recoTrace.getEntries().size() == 0 )
				continue;

			boolean exists = false;
			// updating do and don't values
			for( RecommendationResult rr : results ) {
				if( rr.getTrace().equals( recoTrace ) ) {
					rr.addDoAndWeight( t.getTargetValue(), 1 );
					exists = true;
				} else
					rr.addDontAndWeight( t.getTargetValue(), 1 );

			}

			// adding non existing entry to the list
			if( !exists ) {
				RecommendationResult r = new RecommendationResult( recoTrace );
				r.addDoAndWeight( t.getTargetValue(), 1 );

				// setting don't value for new entries
				for( RecommendationResult rr : results )
					r.addDontAndWeight( rr.getSumDoTargetValue() / rr.getSumDoWeight(), rr.getSumDoWeight() );

				results.add( r );
			}
		}// end for( Trace t : traces )

		RecommendationResultList resultSet = new RecommendationResultList();
		for( RecommendationResult res : results ) {
			resultSet.addResult( res );
		}
		return resultSet;
	}
}

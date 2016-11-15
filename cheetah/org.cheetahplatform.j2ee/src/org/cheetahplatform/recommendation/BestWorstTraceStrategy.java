package org.cheetahplatform.recommendation;



import java.util.List;
import java.util.Vector;


import pmsedit.logmodel.LogEntry;



/**
 * This recommendation strategy takes n traces with the best business value and m traces with the
 * worst business value for calculating the do and don't value.
 * 
 * @author Christian Haisjackl
 */
public class BestWorstTraceStrategy extends Strategy {
	private int numOfBestTraces = 3;
	private int numOfWorstTraces = 3;
	private Miner miner;



	/**
	 * Creates a Best Worst Trace Strategy.
	 * 
	 * @param log The log to use
	 * @param target The scale to use
	 * @param eventState The state of the event to check
	 * @param additionalParams [Number of best Traces for do value, Number of worst Traces for don't
	 *           value]
	 */
	protected BestWorstTraceStrategy( Log log, TargetFunction target, String eventState, List<Object> additionalParams ) {
		super( log, target, eventState );
		if( additionalParams != null && additionalParams.size() == 2 ) {
			numOfBestTraces = ( (Integer) additionalParams.get( 0 ) ).intValue();
			numOfWorstTraces = ( (Integer) additionalParams.get( 1 ) ).intValue();
		}
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
		List<ResultSet> rs = new Vector<ResultSet>();
		// only next tasks can be the result
		for( LogEntry e : possibleNextTasks ) {
			Trace tmp = new Trace( targetFunction );
			tmp.addEntry( e ); // result set only contains possible next steps
			RecommendationResult rr = new RecommendationResult( tmp );
			results.add( rr );
		}

		// checking traces if there exists already one similar
		for( Trace t : traces ) {
			List<Trace> l = miner.mine( t, partialTrace, 1, null );
			if( l == null )
				continue;

			// should be possible to implement (multi) set only by changing that function
			Trace recoTrace = l.get( 0 );
			// if traces are not similar or no further steps, set weight to 0 and next loop
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

			rs.add( new ResultSet( recoTrace, t.getTargetValue() ) );
		}// end for( Trace t : traces )

		for( RecommendationResult rr : results ) {
			// ordered list for do and don't values, first entry has highest BV
			List<ResultSet> doList = new Vector<ResultSet>();
			List<ResultSet> dontList = new Vector<ResultSet>();

			// iterating over result set, sorting do and don't traces
			for( ResultSet res : rs ) {
				int i;
				// calculate do value (trace contains task)
				if( res.trace.contains( rr.getTrace().getEntries().get( 0 ) ) ) {
					for( i = 0; i < doList.size() && i < numOfBestTraces; i++ ) {
						ResultSet r = doList.get( i );
						if( r.tv * r.trace.getWeight() < res.tv * res.trace.getWeight() )
							break;
					}
					doList.add( i, res );
				} else {// calculate don't value (trace doesn't contain task)
					for( i = 0; i < dontList.size() && i < numOfWorstTraces; i++ ) {
						ResultSet r = dontList.get( i );
						if( r.tv * r.trace.getWeight() < res.tv * res.trace.getWeight() )
							break;
					}
					dontList.add( i, res );
				}
			}

			// calculating do values
			for( int i = 0; i < doList.size() && i < numOfBestTraces; i++ )
				rr.addDoAndWeight( doList.get( i ).tv, doList.get( i ).trace.getWeight() );

			// calculating don't values
			for( int i = 0; i < dontList.size() && i < numOfWorstTraces; i++ )
				rr.addDontAndWeight( dontList.get( i ).tv, dontList.get( i ).trace.getWeight() );
		}

		RecommendationResultList resultSet = new RecommendationResultList();
		for( RecommendationResult res : results ) {
			resultSet.addResult( res );
		}
		return resultSet;
	}

	/**
	 * This class represents a result set from the miner.
	 * 
	 * @author Christian Haisjackl
	 */
	private class ResultSet {
		private Trace trace;
		private double tv;



		/**
		 * @param result The recommendation result
		 * @param trace The trace which calculated the result
		 * @param tv the target value
		 */
		public ResultSet( Trace trace, double tv ) {
			this.trace = trace;
			this.tv = tv;
		}
	}
}

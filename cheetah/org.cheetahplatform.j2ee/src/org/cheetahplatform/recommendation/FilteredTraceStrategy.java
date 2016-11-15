package org.cheetahplatform.recommendation;



import java.util.List;
import java.util.Vector;


import pmsedit.logmodel.LogEntry;



/**
 * This strategy uses a filter. Activities which do not fulfill certain requirements are removed
 * from the partial trace. The remaining activties are handed over to the set miner.
 * 
 * @author Christian Haisjackl
 */
public class FilteredTraceStrategy extends Strategy {
	private Filter filter;
	private String filterValue;
	private int filterOrder;
	private Miner miner;



	/**
	 * Creates a Filtered Trace Strategy.
	 * 
	 * @param log The log to use
	 * @param target The scale to use
	 * @param eventState The state of the event to check
	 * @param additionalParams [filter, filterAttribute, filterValue, filterOrder]
	 * @throws Exception If the filter can not be initialized proper
	 */
	protected FilteredTraceStrategy( Log log, TargetFunction target, String eventState, List<Object> additionalParams ) throws Exception {
		super( log, target, eventState );
		try {
			filter = Filter.getInstance( additionalParams.get( 0 ).toString(), additionalParams.get( 1 ).toString() );
			miner = Miner.getInstance( "log.miner.SetMiner", this );
		} catch( Exception e ) {
			e.printStackTrace();
		}
		filter = Filter.getInstance( additionalParams.get( 0 ).toString(), additionalParams.get( 1 ).toString() );
		filterValue = additionalParams.get( 2 ).toString();
		filterOrder = Integer.parseInt( additionalParams.get( 3 ).toString() );
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

		Trace newPartialTrace = filter.filter( partialTrace, filterValue, filterOrder );

		// gathering all relevant traces which have a possible next task and contain at some elements
		// of the partial trace
		for( Trace t : traces ) {
			List<Trace> l = miner.mine( t, newPartialTrace, 0, possibleNextTasks );
			if( l == null )
				continue;

			Trace tmp = l.get( 0 );
			if( tmp.getWeight() == 0.0 )
				continue;

			// calculating do and don't values
			for( RecommendationResult res : results ) {
				if( t.contains( res.getTrace().getEntries().get( 0 ) ) ) {
					// current entry gets do value
					LogEntry resE = res.getTrace().getEntries().get( 0 );
					res.addDoAndWeight( t.getTargetValue(), tmp.getWeight() );
					for( RecommendationResult rr : results ) {
						// other entries get don't value, which are not in trace
						LogEntry rrE = rr.getTrace().getEntries().get( 0 );
						if( rrE.getTask() != resE.getTask() && !t.contains( rrE ) )
							rr.addDontAndWeight( t.getTargetValue(), tmp.getWeight() );
					}
				}
			}
		}

		RecommendationResultList resultSet = new RecommendationResultList();
		for( RecommendationResult res : results )
			resultSet.addResult( res );
		return resultSet;
	}
}

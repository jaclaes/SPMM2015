package org.cheetahplatform.recommendation;

import java.util.List;
import java.util.Vector;

import pmsedit.logmodel.LogEntry;

/**
 * This strategy guesses the used process model for generating the recommendations.
 * 
 * @author Christian Haisjackl
 */
public class PartialOrderStrategy extends Strategy {
	private Miner miner;

	/**
	 * Creates a Partial Order Strategy.
	 * 
	 * @param log
	 *            The log to use
	 * @param target
	 *            The scale to use
	 * @param eventState
	 *            The state of the event to check
	 * @param additionalParams
	 *            [filter, filterAttribute, filterValue, filterOrder]
	 */
	@SuppressWarnings("unused")
	protected PartialOrderStrategy(Log log, TargetFunction target, String eventState, List<Object> additionalParams) {
		super(log, target, eventState);
		try {
			miner = Miner.getInstance("log.miner.PartialOrderMiner", this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see org.cheetahplatform.recommendation.Strategy#getRecommendations(org.cheetahplatform.recommendation.Trace, java.util.List)
	 */
	@Override
	public RecommendationResultList getRecommendations(Trace partialTrace, List<LogEntry> possibleNextTasks) {
		List<RecommendationResult> results = new Vector<RecommendationResult>();
		// only next tasks can be the result
		for (LogEntry e : possibleNextTasks) {
			Trace tmp = new Trace(targetFunction);
			tmp.addEntry(e); // result set only contains possible next steps
			RecommendationResult rr = new RecommendationResult(tmp);
			results.add(rr);
		}

		// gathering all relevant traces which have a possible next task and contain at some elements
		// of the partial trace
		List<Trace> l = miner.mine(new Trace(targetFunction), partialTrace, 0, possibleNextTasks);

		for (RecommendationResult rr : results) {
			for (Trace t : l) {
				boolean isPNT = false;

				for (LogEntry e : possibleNextTasks) {
					if (t.contains(e)) {
						isPNT = true;
						break;
					}
				}

				if (!isPNT)
					continue;
				if (t.contains(rr.getTrace().getEntries().get(0))) {
					rr.addDoAndWeight(t.getWeight(), 1);
					rr.addDontAndWeight(0, 0);
				} else {
					rr.addDoAndWeight(0, 0);
					rr.addDontAndWeight(t.getWeight(), 1);
				}
			}
		}

		RecommendationResultList resultSet = new RecommendationResultList();
		for (RecommendationResult res : results)
			resultSet.addResult(res);
		return resultSet;
	}
}

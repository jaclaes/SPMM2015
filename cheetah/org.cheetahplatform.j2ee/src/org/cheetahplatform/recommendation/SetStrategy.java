package org.cheetahplatform.recommendation;

import java.util.List;
import java.util.Vector;

import pmsedit.logmodel.LogEntry;

/**
 * This strategy does not take the structure of the logged activties and the partial trace into account. It only uses the occurrence of
 * activities.
 * 
 * @author Christian Haisjackl
 */
public class SetStrategy extends Strategy {
	private Miner miner;

	/**
	 * Creates a Set Strategy.
	 * 
	 * @param log
	 *            The log to use
	 * @param target
	 *            The scale to use
	 * @param eventState
	 *            The state of the event to check
	 * @param additionalParams
	 *            not used
	 */
	@SuppressWarnings("unused")
	protected SetStrategy(Log log, TargetFunction target, String eventState, List<Object> additionalParams) {
		super(log, target, eventState);
		try {
			miner = Miner.getInstance("log.miner.SetMiner", this);
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
		for (Trace t : traces) {
			List<Trace> l = miner.mine(t, partialTrace, 0, possibleNextTasks);
			if (l == null)
				continue;

			Trace tmp = l.get(0);
			if (tmp.getWeight() == 0.0)
				continue;

			// calculating do and don't values
			for (RecommendationResult res : results) {
				if (tmp.contains(res.getTrace().getEntries().get(0))) {
					// current entry gets do value
					LogEntry resE = res.getTrace().getEntries().get(0);
					res.addDoAndWeight(t.getTargetValue(), tmp.getWeight());
					for (RecommendationResult rr : results) {
						// other entries get don't value, which are not in trace
						LogEntry rrE = rr.getTrace().getEntries().get(0);
						if (rrE.getTask() != resE.getTask() && !tmp.contains(rrE))
							rr.addDontAndWeight(t.getTargetValue(), tmp.getWeight());
					}
				}
			}
		}

		RecommendationResultList resultSet = new RecommendationResultList();
		for (RecommendationResult res : results)
			resultSet.addResult(res);
		return resultSet;
	}
}

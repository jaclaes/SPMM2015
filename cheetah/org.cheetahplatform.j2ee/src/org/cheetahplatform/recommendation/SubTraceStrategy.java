package org.cheetahplatform.recommendation;

import java.util.List;
import java.util.Vector;

import pmsedit.logmodel.LogEntry;

/**
 * This trace works similar to the prefix strategy but the log trace's sequence equal to the partial trace need not to be at the beginning
 * of the log trace.
 * 
 * @author Christian Haisjackl
 */
public class SubTraceStrategy extends Strategy {
	private Miner miner;

	/**
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
	public SubTraceStrategy(Log log, TargetFunction target, String eventState, List<Object> additionalParams) {
		super(log, target, eventState);
		try {
			miner = Miner.getInstance("log.miner.SubTraceMiner", this);
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

		for (Trace t : traces) {
			List<Trace> l = miner.mine(t, partialTrace, 1, null);
			// if traces are not similar next loop
			if (l == null || l.size() == 0)
				continue;

			for (Trace recoTrace : l) {

				// if further steps next loop
				if (recoTrace.getEntries().size() == 0)
					continue;

				boolean isPNT = false;
				for (LogEntry e : possibleNextTasks) {
					if (recoTrace.contains(e)) {
						isPNT = true;
						break;
					}
				}

				if (!isPNT)
					continue;

				// updating do and don't values
				for (RecommendationResult rr : results) {
					if (recoTrace.contains(rr.getTrace().getEntries().get(0)))
						rr.addDoAndWeight(t.getTargetValue(), 1);
					else
						rr.addDontAndWeight(t.getTargetValue(), 1);
				}
			}// end for (Trace recoTrace : t)
		}// end for( Trace t : traces )

		RecommendationResultList resultSet = new RecommendationResultList();
		for (RecommendationResult res : results) {
			resultSet.addResult(res);
		}
		return resultSet;
	}
}

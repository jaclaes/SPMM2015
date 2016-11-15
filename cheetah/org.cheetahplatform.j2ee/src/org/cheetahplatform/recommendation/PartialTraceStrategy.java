package org.cheetahplatform.recommendation;

import java.util.List;
import java.util.Vector;

import pmsedit.logmodel.LogEntry;

/**
 * This strategy hands only the last n activities of the partial trace over to the Sub Trace Miner.
 * 
 * @author Christian Haisjackl
 */
public class PartialTraceStrategy extends Strategy {
	private Miner miner;
	private int chunkSize = 1;

	/**
	 * Creates a new Partial Trace Strategy.
	 * 
	 * @param log
	 *            The log to use
	 * @param target
	 *            The scale to use
	 * @param eventState
	 *            The state of the event to check
	 * @param additionalParams
	 *            [Integer chunksize]
	 */
	public PartialTraceStrategy(Log log, TargetFunction target, String eventState, List<Object> additionalParams) {
		super(log, target, eventState);
		if (additionalParams.isEmpty()) {
			chunkSize = 1;
		} else {
			chunkSize = ((Integer) additionalParams.get(0)).intValue();
			if (chunkSize < 1)
				chunkSize = 1;
		}

		try {
			miner = Miner.getInstance("org.cheetahplatform.recommendation.SubTraceMiner", this);
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

		// creating the search junk for the miner
		Trace searchTrace = new Trace(partialTrace.getTargetFunction());
		List<LogEntry> ste = partialTrace.getEntries();

		if (ste.size() > chunkSize) {
			for (int i = ste.size() - chunkSize; i < ste.size(); i++)
				searchTrace.addEntry(ste.get(i));
		} else
			searchTrace = partialTrace;

		for (Trace t : traces) {
			List<Trace> l = miner.mine(t, searchTrace, 1, null);
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

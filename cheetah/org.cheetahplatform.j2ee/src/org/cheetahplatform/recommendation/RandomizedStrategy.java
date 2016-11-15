package org.cheetahplatform.recommendation;

import java.util.List;
import java.util.Random;

import pmsedit.logmodel.LogEntry;

/**
 * This strategy returns an activity of the possibly next tasks chosen randomly.
 * 
 * @author Christian Haisjackl
 */
public class RandomizedStrategy extends Strategy {
	private Random rand;

	/**
	 * Creates a Randomized Strategy.
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
	protected RandomizedStrategy(Log log, TargetFunction target, String eventState, List<Object> additionalParams) {
		super(log, target, eventState);
		rand = new Random(System.currentTimeMillis());
	}

	/**
	 * @see org.cheetahplatform.recommendation.Strategy#getRecommendations(org.cheetahplatform.recommendation.Trace, java.util.List)
	 */
	@Override
	public RecommendationResultList getRecommendations(Trace partialTrace, List<LogEntry> possibleNextTasks) {
		Trace tmp = new Trace(targetFunction);
		tmp.addEntry(possibleNextTasks.get(rand.nextInt(possibleNextTasks.size())));
		RecommendationResult rr = new RecommendationResult(tmp);
		rr.addDoAndWeight(1.0, 1.0);
		rr.addDontAndWeight(1.0, 1.0);
		RecommendationResultList resultSet = new RecommendationResultList();
		resultSet.addResult(rr);
		return resultSet;
	}
}

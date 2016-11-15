package org.cheetahplatform.modeler.graph.export;

import org.eclipse.core.runtime.Assert;

public class FixedDurationsPhaseSimilarityCalculationStrategy implements IPhaseSimilarityCalculationStrategy {

	private long slidingWindowDuration;
	private long stepSize;

	public FixedDurationsPhaseSimilarityCalculationStrategy(long slidingWindowDuration, long stepSize) {
		Assert.isLegal(slidingWindowDuration > 0);
		Assert.isLegal(stepSize > 0);
		this.slidingWindowDuration = slidingWindowDuration;
		this.stepSize = stepSize;
	}

	@Override
	public long getSlidingWindowDuration() {
		return slidingWindowDuration;
	}

	@Override
	public long getStepSize() {
		return stepSize;
	}
}

package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.Assert;

public class AverageIterationDurationSimilarityCalculationStrategy implements IPhaseSimilarityCalculationStrategy {

	private final List<ProcessOfProcessModelingIteration> iterations;

	private long slidingWindowSize;

	private final long stepSize;

	public AverageIterationDurationSimilarityCalculationStrategy(List<ProcessOfProcessModelingIteration> iterations, long stepSize) {
		Assert.isNotNull(iterations);
		Assert.isLegal(stepSize > 0);
		this.stepSize = stepSize;
		this.iterations = iterations;
	}

	@Override
	public long getSlidingWindowDuration() {
		if (slidingWindowSize == 0) {
			AverageIterationDurationStatistic averageIterationDurationStatistic = new AverageIterationDurationStatistic();
			String value = averageIterationDurationStatistic.getValue(null, null, iterations);
			if (value.equals(IPpmStatistic.N_A)) {
				slidingWindowSize = 1;
				return slidingWindowSize;
			}

			slidingWindowSize = Long.parseLong(value);
		}
		return slidingWindowSize;
	}

	@Override
	public long getStepSize() {
		return stepSize;
	}
}

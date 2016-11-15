package org.cheetahplatform.modeler.graph.export;

public interface IPhaseSimilarityCalculationStrategy {
	long getSlidingWindowDuration();

	long getStepSize();
}

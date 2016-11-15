package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class ContentAddingDistributionStatistic implements IPpmStatistic {

	private static final int NUMBER_OF_PARTS = 4;

	public int calculateSize(List<SlidingWindow> filtered) {
		int totalSize = 0;
		for (SlidingWindow slidingWindow : filtered) {
			int numberOfEndElements = slidingWindow.getNumberOfEndElements();
			int numberOfStartElements = slidingWindow.getNumberOfStartElements();
			int size = numberOfEndElements - numberOfStartElements;
			totalSize += size;
		}
		return totalSize;
	}

	private List<SlidingWindow> filterSlidingWindows(List<SlidingWindow> slidingWindows, long start, long end) {
		List<SlidingWindow> filtered = new ArrayList<SlidingWindow>();
		for (SlidingWindow slidingWindow : slidingWindows) {
			long startTimestamp = slidingWindow.getStartTimestamp();
			if (startTimestamp >= start && startTimestamp <= end) {
				filtered.add(slidingWindow);
			}
		}
		return filtered;
	}

	@Override
	public String getHeader() {
		return "Content Distribution";
	}

	@Override
	public String getName() {
		return "Content Distribution";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (processInstance == null) {
			return N_A;
		}

		if (iterations == null || iterations.isEmpty()) {
			return N_A;
		}

		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}

		PhaseSimilarityCalculator calculator = new PhaseSimilarityCalculator(new AverageIterationDurationSimilarityCalculationStrategy(
				iterations, 2000));
		List<SlidingWindow> slidingWindows = calculator.calculate(processInstance, chunks, iterations);
		if (slidingWindows.isEmpty()) {
			return N_A;
		}

		SlidingWindow firstWindow = slidingWindows.get(0);
		SlidingWindow lastWindow = slidingWindows.get(slidingWindows.size() - 1);
		long startTimestamp = firstWindow.getStartTimestamp();
		long endTimestamp = lastWindow.getEndTimestamp();
		long duration = endTimestamp - startTimestamp;
		long stepSize = duration / NUMBER_OF_PARTS;

		int totalSize = calculateSize(slidingWindows);

		StringBuilder builder = new StringBuilder();

		long time = startTimestamp;
		while (time + stepSize <= endTimestamp) {
			List<SlidingWindow> filtered = filterSlidingWindows(slidingWindows, time, time + stepSize);
			int currentSize = calculateSize(filtered);
			builder.append(((double) currentSize) / totalSize);
			builder.append(";");
			time += stepSize;
		}

		return builder.toString().replaceAll("\\.", ",");
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

}

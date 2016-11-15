package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class AverageIterationDurationStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Avg. Iteration Duration";
	}

	@Override
	public String getName() {
		return "Avg. Iteration Duration";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (iterations == null || iterations.isEmpty()) {
			return N_A;
		}

		long slidingWindowSize;

		long totalDuration = 0;
		for (ProcessOfProcessModelingIteration iteration : iterations) {
			long duration = iteration.getDuration();
			totalDuration += duration;
		}

		slidingWindowSize = totalDuration / iterations.size();

		return String.valueOf(slidingWindowSize);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

}

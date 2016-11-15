package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class TotalComprehensionDurationStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Total Comp. Duration";
	}

	@Override
	public String getName() {
		return "Total Durations of Comprehension Phases";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		long totalComprehensionTime = 0;
		for (Chunk chunk : chunks) {
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.COMPREHENSION)) {
				totalComprehensionTime += chunk.getDuration();
				continue;
			}
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.MODELING)) {
				continue;
			}
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.RECONCILIATION)) {
				continue;
			}
			throw new IllegalStateException("Illegal type of phase" + chunk.getType());
		}

		return String.valueOf(totalComprehensionTime);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_PPM_STATISTICS);
	}
}

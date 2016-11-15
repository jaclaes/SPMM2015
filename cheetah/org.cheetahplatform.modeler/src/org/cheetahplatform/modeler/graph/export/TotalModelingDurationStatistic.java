package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class TotalModelingDurationStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Total Mod. Duration";
	}

	@Override
	public String getName() {
		return "Total Durations of Modeling Phases";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		long totalModelingTime = 0;
		for (Chunk chunk : chunks) {
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.COMPREHENSION)) {
				continue;
			}
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.MODELING)) {
				totalModelingTime += chunk.getDuration();
				continue;
			}
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.RECONCILIATION)) {
				continue;
			}
			throw new IllegalStateException("Illegal type of phase" + chunk.getType());
		}

		return String.valueOf(totalModelingTime);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_PPM_STATISTICS);
	}

}

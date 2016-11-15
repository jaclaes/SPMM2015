package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class TotalReconciliationDurationStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Total Rec. Duration";
	}

	@Override
	public String getName() {
		return "Total Durations of Reconciliation Phases";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		long totalReconciliationTime = 0;
		for (Chunk chunk : chunks) {
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.COMPREHENSION)) {
				continue;
			}
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.MODELING)) {
				continue;
			}
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.RECONCILIATION)) {
				totalReconciliationTime += chunk.getDuration();
				continue;
			}
			throw new IllegalStateException("Illegal type of phase" + chunk.getType());
		}

		return String.valueOf(totalReconciliationTime);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_PPM_STATISTICS);
	}

}

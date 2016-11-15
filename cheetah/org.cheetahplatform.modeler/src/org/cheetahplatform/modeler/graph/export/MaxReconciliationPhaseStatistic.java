package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class MaxReconciliationPhaseStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Max Reconciliation Size";
	}

	@Override
	public String getName() {
		return "Size of the largest reconciliation phase. Non Rec. Events in Rec Phases are also considered.";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}

		int maxSize = Integer.MIN_VALUE;

		for (Chunk chunk : chunks) {
			if (ModelingPhaseChunkExtractor.RECONCILIATION.equals(chunk.getType())) {
				if (chunk.getSize() > maxSize) {
					maxSize = chunk.getSize();
				}
			}
		}

		if (maxSize == Integer.MIN_VALUE) {
			return N_A;
		}

		return String.valueOf(maxSize);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}
}

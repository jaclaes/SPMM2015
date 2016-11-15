package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class MinReconciliationPhaseStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Min Reconciliation Size";
	}

	@Override
	public String getName() {
		return "Size of the smallest reconciliation phase. Non Rec. Events in Rec Phases are also considered.";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}

		int minSize = Integer.MAX_VALUE;

		for (Chunk chunk : chunks) {
			if (ModelingPhaseChunkExtractor.RECONCILIATION.equals(chunk.getType())) {
				if (chunk.getSize() < minSize) {
					minSize = chunk.getSize();
				}
			}
		}

		if (minSize == Integer.MAX_VALUE) {
			return N_A;
		}

		return String.valueOf(minSize);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}
}

package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class NumberOfModelingPhasesStatistic implements IPpmStatistic {
	@Override
	public String getHeader() {
		return "Number of Modeling Phases";
	}

	@Override
	public String getName() {
		return "The number of Modeling phases in the PPM.";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}

		int count = 0;
		for (Chunk chunk : chunks) {
			if (ModelingPhaseChunkExtractor.MODELING.equals(chunk.getType())) {
				count++;
			}
		}

		return String.valueOf(count);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}
}
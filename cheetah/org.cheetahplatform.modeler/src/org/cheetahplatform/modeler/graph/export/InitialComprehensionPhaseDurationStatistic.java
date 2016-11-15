package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class InitialComprehensionPhaseDurationStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Intial Comp. Duration";
	}

	@Override
	public String getName() {
		return "Duration of the initial comprehension phase";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}

		Chunk initialChunk = chunks.get(0);
		if (!initialChunk.getType().equals(ModelingPhaseChunkExtractor.COMPREHENSION)) {
			return N_A;
		}

		return String.valueOf(initialChunk.getDuration());
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

}

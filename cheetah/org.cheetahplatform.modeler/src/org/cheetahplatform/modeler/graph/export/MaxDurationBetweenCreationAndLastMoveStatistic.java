package org.cheetahplatform.modeler.graph.export;

import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class MaxDurationBetweenCreationAndLastMoveStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Max. Duration Creation to Last Move";
	}

	@Override
	public String getName() {
		return "Max Duration between Creation and Last Move of Nodes";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (processInstance == null) {
			return N_A;
		}

		ModelElementInformationExtractor extractor = new ModelElementInformationExtractor();
		Map<Long, ModelElementInformation> information = extractor.extract(processInstance.getEntries());

		long maxDuration = Long.MIN_VALUE;

		for (ModelElementInformation modelElementInformation : information.values()) {
			long durationToLastMove = modelElementInformation.getDurationToLastMove();
			if (durationToLastMove == 0) {
				continue;
			}
			if (durationToLastMove > maxDuration) {
				maxDuration = durationToLastMove;
			}

		}

		if (maxDuration == Long.MIN_VALUE) {
			return N_A;
		}

		return String.valueOf(maxDuration);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}
}

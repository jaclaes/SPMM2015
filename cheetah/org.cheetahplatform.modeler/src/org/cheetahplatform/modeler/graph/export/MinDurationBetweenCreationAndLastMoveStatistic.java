package org.cheetahplatform.modeler.graph.export;

import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class MinDurationBetweenCreationAndLastMoveStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Min. Duration Creation to Last Move";
	}

	@Override
	public String getName() {
		return "Min Duration between Creation and Last Move of Nodes";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (processInstance == null) {
			return N_A;
		}

		ModelElementInformationExtractor extractor = new ModelElementInformationExtractor();
		Map<Long, ModelElementInformation> information = extractor.extract(processInstance.getEntries());

		long minDuration = Long.MAX_VALUE;

		for (ModelElementInformation modelElementInformation : information.values()) {
			long durationToLastMove = modelElementInformation.getDurationToLastMove();
			if (durationToLastMove == 0) {
				continue;
			}
			if (durationToLastMove < minDuration) {
				minDuration = durationToLastMove;
			}

		}

		if (minDuration == Long.MAX_VALUE) {
			return N_A;
		}

		return String.valueOf(minDuration);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}
}

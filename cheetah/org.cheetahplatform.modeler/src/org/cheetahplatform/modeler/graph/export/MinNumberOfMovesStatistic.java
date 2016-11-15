package org.cheetahplatform.modeler.graph.export;

import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class MinNumberOfMovesStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Min Number of Moves per Node";
	}

	@Override
	public String getName() {
		return "Min Number of Moves per Node";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (processInstance == null) {
			return N_A;
		}

		ModelElementInformationExtractor extractor = new ModelElementInformationExtractor();
		Map<Long, ModelElementInformation> information = extractor.extract(processInstance.getEntries());

		int minNumberOfMoves = Integer.MAX_VALUE;

		for (ModelElementInformation modelElementInformation : information.values()) {
			int numberOfMoves = modelElementInformation.getNumberOfMoves();
			if (numberOfMoves == 0) {
				continue;
			}

			if (numberOfMoves < minNumberOfMoves) {
				minNumberOfMoves = numberOfMoves;
			}
		}
		if (minNumberOfMoves == Integer.MAX_VALUE) {
			return N_A;
		}

		return String.valueOf(minNumberOfMoves);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

}

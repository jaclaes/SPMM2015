package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class AverageDurationBetweenCreationAndLastMoveStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Avg. Duration Creation to Last Move";
	}

	@Override
	public String getName() {
		return "Average Duration between Creation and Last Move of Nodes";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (processInstance == null) {
			return N_A;
		}

		ModelElementInformationExtractor extractor = new ModelElementInformationExtractor();
		Map<Long, ModelElementInformation> information = extractor.extract(processInstance.getEntries());

		long totalDuration = 0;
		int count = 0;

		for (ModelElementInformation modelElementInformation : information.values()) {
			totalDuration += modelElementInformation.getDurationToLastMove();
			count++;
		}

		double averageNumberOfMoves = ((double) totalDuration) / count;
		DecimalFormat decimalFormat = new DecimalFormat("##.00");
		return decimalFormat.format(averageNumberOfMoves);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

}

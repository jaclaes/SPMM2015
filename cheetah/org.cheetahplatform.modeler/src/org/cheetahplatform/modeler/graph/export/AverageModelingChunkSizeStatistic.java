package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class AverageModelingChunkSizeStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Avg. Modeling Chunk Size";
	}

	@Override
	public String getName() {
		return "Average Size of Modeling Chunks";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}

		double sum = 0;
		int count = 0;
		for (Chunk chunk : chunks) {
			if (ModelingPhaseChunkExtractor.MODELING.equals(chunk.getType())) {
				count++;
				sum += chunk.getSize();
			}
		}

		if (count == 0) {
			return N_A;
		}

		double averageChunkSize = sum / count;

		DecimalFormat decimalFormat = new DecimalFormat("##.00");
		return decimalFormat.format(averageChunkSize);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}
}

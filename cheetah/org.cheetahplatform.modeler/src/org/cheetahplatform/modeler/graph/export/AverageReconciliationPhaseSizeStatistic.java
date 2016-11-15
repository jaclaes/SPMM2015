package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class AverageReconciliationPhaseSizeStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Average Size of Reconciliation Phases";
	}

	@Override
	public String getName() {
		return "The average size of the reconciliation phases in the PPM.";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}

		int totalSize = 0;
		int count = 0;
		for (Chunk chunk : chunks) {
			if (ModelingPhaseChunkExtractor.RECONCILIATION.equals(chunk.getType())) {
				count++;
				totalSize += chunk.getSize();
			}
		}

		if (count == 0) {
			return N_A;
		}

		double averageSize = ((double) totalSize) / count;
		DecimalFormat decimalFormat = new DecimalFormat("##.00");
		return decimalFormat.format(averageSize);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

}

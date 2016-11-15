package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class ShareOfComprehensionIncludingInitialPhaseStatistic implements IPpmStatistic {
	@Override
	public String getHeader() {
		return "Share of Comp. With Intial Phase[%]";
	}

	@Override
	public String getName() {
		return "Share of Comprehension [%] including the initial comp. Phase";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}
		long totalComprehensionTime = 0;
		int numberOfComprehensionChunks = 0;
		long totalModelingTime = 0;
		int numberOfModelingChunks = 0;
		long totalReconciliationTime = 0;
		int numberOfReconciliationChunks = 0;
		for (Chunk chunk : chunks) {
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.COMPREHENSION)) {
				totalComprehensionTime += chunk.getDuration();
				numberOfComprehensionChunks++;
			}
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.MODELING)) {
				totalModelingTime += chunk.getDuration();
				numberOfModelingChunks++;
			}
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.RECONCILIATION)) {
				totalReconciliationTime += chunk.getDuration();
				numberOfReconciliationChunks++;
			}
		}

		DecimalFormat decimalFormat = new DecimalFormat("##0.00");
		if (numberOfComprehensionChunks == 0) {
			return decimalFormat.format(0.0);
		}

		double total = 0.0;

		double averageComprehensionLength = (double) totalComprehensionTime / numberOfComprehensionChunks;
		total += averageComprehensionLength;
		if (numberOfModelingChunks > 0) {
			double averageModelingLength = (double) totalModelingTime / numberOfModelingChunks;
			total += averageModelingLength;
		}
		if (numberOfReconciliationChunks > 0) {
			double averageReconciliationLength = (double) totalReconciliationTime / numberOfReconciliationChunks;
			total += averageReconciliationLength;
		}
		double shareOfComprehension = averageComprehensionLength / total;

		shareOfComprehension = shareOfComprehension * 100;

		return decimalFormat.format(shareOfComprehension);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

}

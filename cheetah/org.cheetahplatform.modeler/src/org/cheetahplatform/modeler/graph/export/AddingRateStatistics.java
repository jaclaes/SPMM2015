package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class AddingRateStatistics implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Adding Rate";
	}

	@Override
	public String getName() {
		return "Adding Rate - # Add in mod phases/total duration of mod. phases";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}

		TotalModelingDurationStatistic totalModelingDurationStatistic = new TotalModelingDurationStatistic();
		String value = totalModelingDurationStatistic.getValue(processInstance, chunks, iterations);
		if (N_A.equals(value)) {
			return N_A;
		}

		long totalModelingDurationInSeconds = Long.parseLong(value) / 1000;

		int addingEntries = 0;

		for (Chunk chunk : chunks) {
			if (!chunk.getType().equals(ModelingPhaseChunkExtractor.MODELING)) {
				continue;
			}
			List<AuditTrailEntry> entries = chunk.getEntries();

			for (AuditTrailEntry auditTrailEntry : entries) {
				if (ModelingPhaseChunkExtractor.isAddingEntry(auditTrailEntry)) {
					addingEntries++;
				}
			}
		}

		double addingRate = ((double) addingEntries) / totalModelingDurationInSeconds;

		DecimalFormat decimalFormat = new DecimalFormat("##.00");
		return decimalFormat.format(addingRate);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}
}

package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class AverageDurationToLayoutStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Avg. Duration To Layout";
	}

	@Override
	public String getName() {
		return "Avg. duration between layout and create event";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (processInstance == null) {
			return N_A;
		}

		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (entries == null || entries.isEmpty()) {
			return N_A;
		}

		DurationToLayoutCalculator durationToLayoutCalculator = new DurationToLayoutCalculator(processInstance);
		double averageDurationToLayout = durationToLayoutCalculator.getAverageDurationToLayout();

		DecimalFormat decimalFormat = new DecimalFormat("##.00");
		return decimalFormat.format(averageDurationToLayout);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

}

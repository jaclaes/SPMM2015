package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class LayoutIterationStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "% of layouting in the same iteration";
	}

	@Override
	public String getName() {
		return "The % of layouting events that touch elements created in the same iteration.";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (processInstance == null) {
			return N_A;
		}

		if (iterations == null || iterations.isEmpty()) {
			return N_A;
		}

		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (entries == null || entries.isEmpty()) {
			return N_A;
		}

		DurationToLayoutCalculator durationToLayoutCalculator = new DurationToLayoutCalculator(processInstance);

		int inSameIteration = 0;
		Map<AuditTrailEntry, AuditTrailEntry> layoutEventsWithCreateEvents = durationToLayoutCalculator.getLayoutEventsWithCreateEvents();
		Set<Entry<AuditTrailEntry, AuditTrailEntry>> entrySet = layoutEventsWithCreateEvents.entrySet();
		for (Entry<AuditTrailEntry, AuditTrailEntry> entry : entrySet) {
			AuditTrailEntry layoutEvent = entry.getKey();
			AuditTrailEntry createEvent = entry.getValue();

			for (ProcessOfProcessModelingIteration iteration : iterations) {
				if (iteration.containsEntry(layoutEvent) && iteration.containsEntry(createEvent)) {
					inSameIteration++;
					break;
				}
			}
		}

		double relativeInSameIteration = ((double) inSameIteration) / layoutEventsWithCreateEvents.size();

		DecimalFormat decimalFormat = new DecimalFormat("##.00");
		return String.valueOf(inSameIteration) + "/" + layoutEventsWithCreateEvents.size() + "="
				+ decimalFormat.format(relativeInSameIteration);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}
}

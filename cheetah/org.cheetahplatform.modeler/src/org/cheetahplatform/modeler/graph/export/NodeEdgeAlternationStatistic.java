package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class NodeEdgeAlternationStatistic implements IPpmStatistic {

	private int countAlternations(List<AuditTrailEntry> filteredentries) {
		int count = 0;

		for (int i = 0; i < filteredentries.size(); i++) {
			AuditTrailEntry auditTrailEntry = filteredentries.get(i);
			if (auditTrailEntry.getEventType().equals(AbstractGraphCommand.CREATE_EDGE)) {
				continue;
			}

			if (i < filteredentries.size() - 1) {
				AuditTrailEntry nextEntry = filteredentries.get(i + 1);
				if (nextEntry.getEventType().equals(AbstractGraphCommand.CREATE_EDGE)) {
					count++;
				}
			}

		}

		return count;
	}

	private List<AuditTrailEntry> filterEntries(List<AuditTrailEntry> entries, String... types) {
		List<AuditTrailEntry> filtered = new ArrayList<AuditTrailEntry>();
		for (AuditTrailEntry auditTrailEntry : entries) {
			if (passesFilter(auditTrailEntry, types)) {
				filtered.add(auditTrailEntry);
			}
		}
		return filtered;
	}

	@Override
	public String getHeader() {
		return "Node Edge Alternation";
	}

	@Override
	public String getName() {
		return "Node Edge Alternation";
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

		List<AuditTrailEntry> filteredentries = filterEntries(entries, AbstractGraphCommand.CREATE_NODE, AbstractGraphCommand.CREATE_EDGE);
		int alternations = countAlternations(filteredentries);

		List<AuditTrailEntry> allCreateEdgeEvents = filterEntries(entries, AbstractGraphCommand.CREATE_EDGE);

		double result = ((double) alternations) / allCreateEdgeEvents.size();

		DecimalFormat decimalFormat = new DecimalFormat("#0.0000");
		return decimalFormat.format(result);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

	private boolean passesFilter(AuditTrailEntry entry, String... types) {
		String eventType = entry.getEventType();

		List<String> possibleTypes = Arrays.asList(types);
		return possibleTypes.contains(eventType);
	}
}

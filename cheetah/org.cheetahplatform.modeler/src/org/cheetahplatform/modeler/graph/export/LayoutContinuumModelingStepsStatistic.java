package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.eclipse.core.runtime.Assert;

public class LayoutContinuumModelingStepsStatistic extends AbstractLayoutStatistic {

	protected List<AuditTrailEntry> findTopLevelEntries(List<AuditTrailEntry> entries) {
		List<AuditTrailEntry> topLevelEntries = new ArrayList<AuditTrailEntry>();
		boolean isTopLevel = true;
		for (AuditTrailEntry auditTrailEntry : entries) {
			if (PromLogger.GROUP_EVENT_END.equals(auditTrailEntry.getEventType())) {
				isTopLevel = true;
				continue;
			}

			if (!isTopLevel) {
				continue;
			}

			if (PromLogger.GROUP_EVENT_START.equals(auditTrailEntry.getEventType())) {
				topLevelEntries.add(auditTrailEntry);
				isTopLevel = false;
				continue;
			}

			if (isTopLevel) {
				topLevelEntries.add(auditTrailEntry);
			}
		}
		return topLevelEntries;
	}

	@Override
	public String getHeader() {
		return "Modeling Steps Layout Continuum";
	}

	@Override
	public String getName() {
		return "Layout Contiuum [Modeling Steps]";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		Assert.isNotNull(processInstance);

		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (entries.isEmpty()) {
			return N_A;
		}

		List<AuditTrailEntry> topLevelEntries = findTopLevelEntries(entries);
		List<AuditTrailEntry> layoutEntries = findLayoutEntries(topLevelEntries);

		if (layoutEntries.isEmpty()) {
			return N_A;
		}

		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (AuditTrailEntry layoutEntry : layoutEntries) {
			if (!first) {
				builder.append(" ");
			}
			int index = topLevelEntries.indexOf(layoutEntry) + 1;

			double relativePosition = ((double) index) / topLevelEntries.size();
			builder.append(getDecimalFormat().format(relativePosition));

			first = false;
		}

		return builder.toString();
	}

}

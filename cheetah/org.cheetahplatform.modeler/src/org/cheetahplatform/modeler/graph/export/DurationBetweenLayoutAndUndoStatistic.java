package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;

public class DurationBetweenLayoutAndUndoStatistic extends AbstractLayoutStatistic {

	@Override
	public String getHeader() {
		return "Duration before Undo";
	}

	@Override
	public String getName() {
		return "Duration between Layout and Undo Layout";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (entries.isEmpty()) {
			return N_A;
		}
		List<AuditTrailEntry> undoLayoutEntries = findUndoLayoutEntries(entries);

		if (undoLayoutEntries.isEmpty()) {
			return N_A;
		}

		StringBuilder builder = new StringBuilder();

		for (AuditTrailEntry undoEntry : undoLayoutEntries) {
			AuditTrailEntry layoutEntry = entries.get(entries.indexOf(undoEntry) - 1);
			long durationBeForeUndo = undoEntry.getTimestamp().getTime() - layoutEntry.getTimestamp().getTime();
			builder.append(durationBeForeUndo);
			builder.append(" ");
		}

		return builder.toString().trim();
	}
}

package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.eclipse.core.runtime.Assert;

public class UndoLayoutStatistic extends AbstractLayoutStatistic {

	@Override
	public String getHeader() {
		return "# Undo Layout";
	}

	@Override
	public String getName() {
		return "Number of Undo Layout Operations";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		Assert.isNotNull(processInstance);

		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (entries.isEmpty()) {
			return N_A;
		}

		List<AuditTrailEntry> undoLayoutEntries = findUndoLayoutEntries(entries);
		return String.valueOf(undoLayoutEntries.size());
	}

}

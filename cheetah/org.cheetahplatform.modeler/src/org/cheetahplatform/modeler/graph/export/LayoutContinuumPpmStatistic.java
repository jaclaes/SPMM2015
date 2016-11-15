package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.eclipse.core.runtime.Assert;

public class LayoutContinuumPpmStatistic extends AbstractLayoutStatistic {

	@Override
	public String getHeader() {
		return "PPM Layout Continuum";
	}

	@Override
	public String getName() {
		return "Layout Continuum [PPM]";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		Assert.isNotNull(processInstance);
		Assert.isNotNull(iterations);
		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (entries.isEmpty()) {
			return N_A;
		}

		if (iterations.isEmpty()) {
			return N_A;
		}

		List<AuditTrailEntry> layoutEntries = findLayoutEntries(entries);
		if (layoutEntries.isEmpty()) {
			return N_A;
		}

		boolean first = true;
		StringBuilder builder = new StringBuilder();

		for (AuditTrailEntry layoutEntry : layoutEntries) {
			if (!first) {
				builder.append(" ");
			}

			int indexOfGroupEvent = entries.indexOf(layoutEntry);
			AuditTrailEntry firstAuditTrailEntryOfLayoutEvent = entries.get(indexOfGroupEvent + 1);
			if (PromLogger.GROUP_EVENT_END.equals(firstAuditTrailEntryOfLayoutEvent.getEventType())) {
				throw new IllegalStateException("Layout of Empty Process Model");
			}

			for (ProcessOfProcessModelingIteration iteration : iterations) {
				if (iteration.containsEntry(firstAuditTrailEntryOfLayoutEvent)) {
					int index = iterations.indexOf(iteration) + 1;
					double layoutIteration = ((double) index) / iterations.size();
					builder.append(getDecimalFormat().format(layoutIteration));
					first = false;
				}
			}
		}

		if (builder.toString().trim().length() == 0) {
			throw new IllegalStateException("Did not find any layout iterations even though there were layout entries.");
		}

		return builder.toString();
	}
}

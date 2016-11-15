package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.ModelerConstants;
import org.eclipse.core.runtime.Assert;

public class LayoutDurationStatistic extends AbstractLayoutStatistic {

	@Override
	public String getHeader() {
		return "Avg. Layout Invocation Duration";
	}

	@Override
	public String getName() {
		return "Average Duration of Layout Operations";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		Assert.isNotNull(processInstance);

		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (entries.isEmpty()) {
			return N_A;
		}

		int successfulLayout = 0;
		long totalDuration = 0;

		List<AuditTrailEntry> layoutEntries = findLayoutEntries(entries);
		for (AuditTrailEntry auditTrailEntry : layoutEntries) {
			if (!auditTrailEntry.isAttributeDefined(ModelerConstants.ATTRIBUTE_LAYOUT_DURATION)) {
				throw new IllegalStateException("No duration for layout available.");
			}

			totalDuration += auditTrailEntry.getLongAttribute(ModelerConstants.ATTRIBUTE_LAYOUT_DURATION);
			successfulLayout++;
		}

		if (successfulLayout == 0) {
			return N_A;
		}

		double averageDuration = ((double) totalDuration) / successfulLayout;
		return getDecimalFormat().format(averageDuration);
	}

}

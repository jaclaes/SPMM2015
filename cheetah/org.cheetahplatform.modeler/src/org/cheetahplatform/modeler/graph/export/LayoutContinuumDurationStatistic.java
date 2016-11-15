package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.eclipse.core.runtime.Assert;

public class LayoutContinuumDurationStatistic extends AbstractLayoutStatistic {

	@Override
	public String getHeader() {
		return "Duration Layout Continuum";
	}

	@Override
	public String getName() {
		return "Layout Contiuum [Duration]";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		Assert.isNotNull(processInstance);

		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (entries == null || entries.isEmpty()) {
			return N_A;
		}

		List<AuditTrailEntry> layoutEntries = findLayoutEntries(entries);
		if (layoutEntries.isEmpty()) {
			return N_A;
		}

		long startTime = processInstance.getLongAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP);
		long endTime = entries.get(entries.size() - 1).getTimestamp().getTime();
		long relativeEndTime = endTime - startTime;

		StringBuilder stringBuilder = new StringBuilder();
		boolean first = true;
		for (AuditTrailEntry auditTrailEntry : layoutEntries) {
			if (!first) {
				stringBuilder.append(" ");
			}
			long absoluteTime = auditTrailEntry.getTimestamp().getTime();
			long relativeTime = absoluteTime - startTime;

			double normalizedLayout = ((double) relativeTime) / relativeEndTime;

			stringBuilder.append(getDecimalFormat().format(normalizedLayout));
			first = false;
		}

		return stringBuilder.toString();
	}
}

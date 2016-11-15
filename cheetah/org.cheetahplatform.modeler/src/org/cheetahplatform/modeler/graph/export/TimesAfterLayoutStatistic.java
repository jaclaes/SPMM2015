package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.eclipse.core.runtime.Assert;

public class TimesAfterLayoutStatistic extends AbstractLayoutStatistic {

	@Override
	public String getHeader() {
		return "Times After Layouts";
	}

	@Override
	public String getName() {
		return "Times After Layouts";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		Assert.isNotNull(processInstance);

		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (entries.isEmpty()) {
			return N_A;
		}
		List<Long> durationsAfterLayout = getTimesAfterLayout(entries);

		if (durationsAfterLayout.isEmpty()) {
			return N_A;
		}

		StringBuilder builder = new StringBuilder();
		boolean first = true;

		for (Long long1 : durationsAfterLayout) {
			if (!first) {
				builder.append(" ");
			}
			builder.append(String.valueOf(long1));
			first = false;
		}

		return builder.toString();
	}
}

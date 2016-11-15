package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.eclipse.core.runtime.Assert;

public class AverageTimeAfterLayoutStatistic extends AbstractLayoutStatistic {

	@Override
	public String getHeader() {
		return "Avg. time after Layout [ms]";
	}

	@Override
	public String getName() {
		return "Average Time after Layout";
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
		long totalDuration = 0;
		for (Long duration : durationsAfterLayout) {
			totalDuration += duration;
		}

		double averageDuration = ((double) totalDuration) / durationsAfterLayout.size();
		return getDecimalFormat().format(averageDuration);
	}
}

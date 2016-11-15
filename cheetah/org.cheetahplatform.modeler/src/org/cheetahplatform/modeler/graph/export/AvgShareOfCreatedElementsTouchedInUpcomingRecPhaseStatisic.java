package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.common.logging.ProcessInstance;

public class AvgShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic extends AbstractShareOfCreatedElementsInUpcomingRecPhaseStatistc {

	@Override
	public String getHeader() {
		return "Avg. Share of Created Elements touched in upcomding Rec. Phase";
	}

	@Override
	public String getName() {
		return "The number of distinct elements created in a mod. phase divided by the number of distinct touched elements in the next rec. phase. Averaged over the whole process. Iterations without rec. phases are ignored. ";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}

		Map<Chunk, Set<Long>> modelingChunks = getModelingChunks(chunks);
		Map<Chunk, Set<Long>> reconciliationChunks = getReconciliationChunks(chunks);

		int count = 0;
		double total = 0;

		for (Chunk chunk : chunks) {
			double relativeValue = getRelativeValue(modelingChunks, reconciliationChunks, chunks, chunk);

			if (relativeValue < 0) {
				continue;
			}

			total += relativeValue;
			count++;
		}

		if (count == 0) {
			return N_A;
		}

		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		return decimalFormat.format(total / count);
	}
}

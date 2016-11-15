package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.common.logging.ProcessInstance;

public class MaxShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic extends AbstractShareOfCreatedElementsInUpcomingRecPhaseStatistc {
	@Override
	public String getHeader() {
		return "Max. Share of Created Elements touched in upcomding Rec. Phase";
	}

	@Override
	public String getName() {
		return "The max number of distinct elements created in a mod. phase divided by the number of distinct touched elements in the next rec. phase. Iterations without rec. phases are ignored. ";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}

		Map<Chunk, Set<Long>> modelingChunks = getModelingChunks(chunks);
		Map<Chunk, Set<Long>> reconciliationChunks = getReconciliationChunks(chunks);

		double max = Double.MIN_VALUE;

		for (Chunk chunk : chunks) {
			double relativeValue = getRelativeValue(modelingChunks, reconciliationChunks, chunks, chunk);

			if (relativeValue < 0) {
				continue;
			}

			if (relativeValue > max) {
				max = relativeValue;
			}
		}

		if (max < 0) {
			return N_A;
		}

		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		return decimalFormat.format(max);
	}

}

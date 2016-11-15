package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.eclipse.core.runtime.Assert;

import com.ibm.icu.text.DecimalFormat;

public class ReconciliationBreakContinuumIterationStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Reconciliation Break Continuum [PPM Iteration]";
	}

	@Override
	public String getName() {
		return "Distribution of Reconciliation Breaks along the PPM Iterations.";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		Assert.isNotNull(iterations);

		if (iterations == null || iterations.isEmpty()) {
			return N_A;
		}

		StringBuilder stringBuilder = new StringBuilder();
		boolean first = true;
		DecimalFormat format = new DecimalFormat("##0.00");

		for (ProcessOfProcessModelingIteration iteration : iterations) {
			if (iteration.containsModelingPhase()) {
				continue;
			}

			if (!iteration.containsReconciliationPhase()) {
				continue;
			}

			if (!first) {
				stringBuilder.append(" ");
			}

			int iterationNumber = iterations.indexOf(iteration) + 1;

			double relativePosition = ((double) iterationNumber) / iterations.size();

			stringBuilder.append(format.format(relativePosition));

			first = false;
		}

		return stringBuilder.toString();
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

}

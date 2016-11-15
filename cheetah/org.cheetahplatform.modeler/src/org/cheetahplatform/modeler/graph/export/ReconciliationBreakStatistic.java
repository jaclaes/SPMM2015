package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class ReconciliationBreakStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Reconciliation Breaks [%]";
	}

	@Override
	public String getName() {
		return "Reconciliation Breaks [%]";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {

		if (iterations == null || iterations.isEmpty()) {
			return N_A;
		}

		double numberOfReconciliationBreaks = 0;
		for (ProcessOfProcessModelingIteration iteration : iterations) {
			if (iteration.containsModelingPhase()) {
				continue;
			}

			if (!iteration.containsReconciliationPhase()) {
				continue;
			}

			numberOfReconciliationBreaks++;
		}

		int remainingIterations = iterations.size() - (int) numberOfReconciliationBreaks;
		if (remainingIterations == 0) {
			return N_A;
		}
		double shareOfReconciliationBreaks = (numberOfReconciliationBreaks / remainingIterations) * 100;
		DecimalFormat decimalFormat = new DecimalFormat("##0.00");
		return decimalFormat.format(shareOfReconciliationBreaks);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_PPM_STATISTICS);
	}
}

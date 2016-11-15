package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class ReconciliationBreakOutOfAllIterationsStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Reconciliation Breaks Out of All [%]";
	}

	@Override
	public String getName() {
		return "Reconciliation Breaks Out of All[%]";
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

		double shareOfReconciliationBreaks = (numberOfReconciliationBreaks / iterations.size()) * 100;
		DecimalFormat decimalFormat = new DecimalFormat("##0.00");
		return decimalFormat.format(shareOfReconciliationBreaks);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_PPM_STATISTICS);
	}

}

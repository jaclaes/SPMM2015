package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class ThreePhaseIterationsStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Three Phase Iterations";
	}

	@Override
	public String getName() {
		return "Iterations containing all three phases";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (iterations == null || iterations.isEmpty()) {
			return N_A;
		}
		double count = 0;
		for (ProcessOfProcessModelingIteration iteration : iterations) {
			if (iteration.containsComprehensionPhase() && iteration.containsModelingPhase() && iteration.containsReconciliationPhase()) {
				count++;
			}
		}

		double threePhasePercentage = count / (iterations.size());

		DecimalFormat decimalFormat = new DecimalFormat("##.00");
		return decimalFormat.format(threePhasePercentage);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_PPM_STATISTICS);
	}
}

package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class MaxIterationChunkSizeStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Max. Iteration Chunk Size";
	}

	@Override
	public String getName() {
		return "Maximum Iteration Chunk Size";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (iterations == null || iterations.isEmpty()) {
			return N_A;
		}
		double max = 0;
		for (ProcessOfProcessModelingIteration iteration : iterations) {
			double total = 0;
			total += iteration.numberOfAddedElements();
			total += iteration.numberOfReconnectElements();
			total += iteration.numberOfRemovedElements();
			total += iteration.numberOfModelingRenameElements();
			if (total > max) {
				max = total;
			}
		}

		if (max == 0) {
			return N_A;
		}

		DecimalFormat decimalFormat = new DecimalFormat("##.00");
		return decimalFormat.format(max);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

}

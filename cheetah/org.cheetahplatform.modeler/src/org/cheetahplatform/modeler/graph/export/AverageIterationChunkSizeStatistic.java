package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class AverageIterationChunkSizeStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Average Iteration Chunk Size";
	}

	@Override
	public String getName() {
		return "Average Chunk Size Per Iteration";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (iterations == null || iterations.isEmpty()) {
			return N_A;
		}
		double total = 0;
		for (ProcessOfProcessModelingIteration iteration : iterations) {
			total += iteration.numberOfAddedElements();
			total += iteration.numberOfReconnectElements();
			total += iteration.numberOfRemovedElements();
			total += iteration.numberOfModelingRenameElements();
		}

		double averageChunkSize = total / (iterations.size());

		DecimalFormat decimalFormat = new DecimalFormat("##.00");
		return decimalFormat.format(averageChunkSize);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_PPM_STATISTICS);
	}
}

package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class DeleteIterationsStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Delete Iterations [%]";
	}

	@Override
	public String getName() {
		return "Number of Iterations with Delete Operations [%]";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (iterations == null || iterations.isEmpty()) {
			return N_A;
		}

		int numberOfIterationsWithDeleteOperations = 0;
		for (ProcessOfProcessModelingIteration iteration : iterations) {
			if (iteration.numberOfRemovedElements() > 0) {
				numberOfIterationsWithDeleteOperations++;
			}
		}

		DecimalFormat decimalFormat = new DecimalFormat("##0.00");
		double percentage = (((double) numberOfIterationsWithDeleteOperations) / iterations.size()) * 100;
		return decimalFormat.format(percentage);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_PPM_STATISTICS);
	}
}

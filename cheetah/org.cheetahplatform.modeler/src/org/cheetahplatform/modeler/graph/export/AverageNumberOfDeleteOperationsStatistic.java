package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class AverageNumberOfDeleteOperationsStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Average Number Of Delete Operations";
	}

	@Override
	public String getName() {
		return "Average Number of Delete Operations per Iterations";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (iterations == null || iterations.isEmpty()) {
			return N_A;
		}

		int numberOfDeleteOperations = 0;
		for (ProcessOfProcessModelingIteration iteration : iterations) {
			numberOfDeleteOperations += iteration.numberOfRemovedElements();
		}

		DecimalFormat decimalFormat = new DecimalFormat("##0.00");
		double percentage = (((double) numberOfDeleteOperations) / iterations.size());
		return decimalFormat.format(percentage);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}
}

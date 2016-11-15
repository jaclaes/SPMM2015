package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class AverageNumberOfOperationsPerIteration implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Avg. (# of Operations/iteration duration)";
	}

	@Override
	public String getName() {
		return "Average Number of Operations per log(iteration duration)";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (iterations == null || iterations.isEmpty()) {
			return N_A;
		}
		double total = 0;
		for (ProcessOfProcessModelingIteration iteration : iterations) {
			double numberOfOperations = iteration.numberOfAddedElements();
			numberOfOperations += iteration.numberOfReconciliationElements();
			numberOfOperations += iteration.numberOfReconnectElements();
			numberOfOperations -= iteration.numberOfRemovedElements();
			double duration = iteration.getDuration();
			double logDuration = Math.log10(duration);
			total += numberOfOperations / logDuration;
		}

		double result = total / (iterations.size());

		DecimalFormat decimalFormat = new DecimalFormat("##.0000");
		return decimalFormat.format(result);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}
}

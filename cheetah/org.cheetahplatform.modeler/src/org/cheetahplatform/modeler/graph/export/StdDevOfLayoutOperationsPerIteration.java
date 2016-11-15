package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class StdDevOfLayoutOperationsPerIteration implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Std. Dev. LayoutOps/Iteation";
	}

	@Override
	public String getName() {
		return "Standard Deviation of Layout Operations per PPM iteration";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (iterations == null || iterations.isEmpty()) {
			return N_A;
		}

		double[] array = new double[iterations.size()];

		for (int i = 0; i < iterations.size(); i++) {
			ProcessOfProcessModelingIteration processOfProcessModelingIteration = iterations.get(i);
			array[i] = processOfProcessModelingIteration.numberOfLayoutElements();
		}

		StandardDeviation standardDeviation = new StandardDeviation();
		double result = standardDeviation.evaluate(array);

		DecimalFormat decimalFormat = new DecimalFormat("#0.0000");
		return decimalFormat.format(result);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

}

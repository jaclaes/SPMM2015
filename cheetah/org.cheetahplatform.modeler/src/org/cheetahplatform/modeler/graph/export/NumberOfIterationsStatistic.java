package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class NumberOfIterationsStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Number of Iterations";
	}

	@Override
	public String getName() {
		return "Number of Iterations";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		return String.valueOf(iterations.size());
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_PPM_STATISTICS);
	}

}

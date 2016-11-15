package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class MaxModelingChunkSizeStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Max Modeling Chunk Size";
	}

	@Override
	public String getName() {
		return "Max Modeling Chunk Size";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}

		double max = 0;
		for (Chunk chunk : chunks) {
			if (ModelingPhaseChunkExtractor.MODELING.equals(chunk.getType())) {
				if (chunk.getSize() > max) {
					max = chunk.getSize();
				}
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

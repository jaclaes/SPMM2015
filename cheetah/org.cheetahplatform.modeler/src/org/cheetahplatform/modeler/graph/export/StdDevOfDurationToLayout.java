package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;

public class StdDevOfDurationToLayout implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Std. Dev. Duration To Layout";
	}

	@Override
	public String getName() {
		return "Standard Deviation of duration between layout and create event";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (processInstance == null) {
			return N_A;
		}

		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (entries == null || entries.isEmpty()) {
			return N_A;
		}

		DurationToLayoutCalculator durationToLayoutCalculator = new DurationToLayoutCalculator(processInstance);
		Map<AuditTrailEntry, Long> layoutDurations = durationToLayoutCalculator.getLayoutDurations();
		StandardDeviation standardDeviation = new StandardDeviation();

		Collection<Long> values = layoutDurations.values();
		double[] doubles = new double[values.size()];
		int i = 0;
		for (Long long1 : values) {
			doubles[i] = long1;
			i++;
		}

		double result = standardDeviation.evaluate(doubles);
		DecimalFormat decimalFormat = new DecimalFormat("##.0000");
		return decimalFormat.format(result);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}
}

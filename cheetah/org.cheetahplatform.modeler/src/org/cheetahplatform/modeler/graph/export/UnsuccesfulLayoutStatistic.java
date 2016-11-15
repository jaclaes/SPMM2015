package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.eclipse.core.runtime.Assert;

public class UnsuccesfulLayoutStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "# Unsuccesful Layout";
	}

	@Override
	public String getName() {
		return "Number of Unsuccesful Layout Attempts";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		Assert.isNotNull(processInstance);

		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (entries.isEmpty()) {
			return N_A;
		}

		int unsuccesfulLayout = 0;
		for (AuditTrailEntry auditTrailEntry : entries) {
			if (AbstractGraphCommand.UNSUCCESSFUL_LAYOUT.equals(auditTrailEntry.getEventType())) {
				unsuccesfulLayout++;
			}
		}

		return String.valueOf(unsuccesfulLayout);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_LAYOUT_STATISTICS);
	}
}

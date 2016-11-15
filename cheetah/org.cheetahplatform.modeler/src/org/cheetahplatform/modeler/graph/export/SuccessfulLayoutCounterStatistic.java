package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.IBMLayouter;
import org.eclipse.core.runtime.Assert;

public class SuccessfulLayoutCounterStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "# Layout";
	}

	@Override
	public String getName() {
		return "Number of Successful Layout Operations";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		Assert.isNotNull(processInstance);

		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (entries.isEmpty()) {
			return N_A;
		}

		int successfulLayout = 0;
		for (AuditTrailEntry auditTrailEntry : entries) {
			if (auditTrailEntry.isAttributeDefined(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME)) {
				if (IBMLayouter.LAYOUT.equals(auditTrailEntry.getAttribute(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME))) {
					if (auditTrailEntry.isAttributeDefined(CommonConstants.ATTRIBUTE_UNDO_EVENT)) {
						continue;
					}

					successfulLayout++;
				}
			}
		}

		return String.valueOf(successfulLayout);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_LAYOUT_STATISTICS);
	}
}

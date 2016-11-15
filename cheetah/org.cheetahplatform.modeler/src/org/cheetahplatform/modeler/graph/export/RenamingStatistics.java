package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class RenamingStatistics implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Renamings";
	}

	@Override
	public String getName() {
		return "Renamings";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		List<AuditTrailEntry> entries = processInstance.getEntries();
		StringBuilder builder = new StringBuilder();
		for (AuditTrailEntry auditTrailEntry : entries) {
			String eventType = auditTrailEntry.getEventType().trim();
			if (AbstractGraphCommand.RENAME.equals(eventType)) {

				String descriptor = auditTrailEntry.getAttribute(AbstractGraphCommand.DESCRIPTOR);
				builder.append(descriptor);
				builder.append(":");
				String oldName = auditTrailEntry.getAttribute(AbstractGraphCommand.NAME);
				builder.append(oldName);
				builder.append(">");
				String newName = auditTrailEntry.getAttribute(AbstractGraphCommand.NEW_NAME);
				builder.append(newName);
				builder.append("|");
			}
		}
		return builder.toString();
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_RENAMING_STATISTIC);
	}

}

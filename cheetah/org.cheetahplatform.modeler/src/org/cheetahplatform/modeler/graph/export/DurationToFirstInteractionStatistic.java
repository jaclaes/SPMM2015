package org.cheetahplatform.modeler.graph.export;

import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class DurationToFirstInteractionStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Duration to first Interaction";
	}

	@Override
	public String getName() {
		return "Duration to first Interaction";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {

		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (entries.isEmpty()) {
			return N_A;
		}

		AuditTrailEntry firstEntry = null;

		for (AuditTrailEntry auditTrailEntry : entries) {
			String eventType = auditTrailEntry.getEventType();
			if (eventType.equals(AbstractGraphCommand.VSCROLL)) {
				continue;
			}
			if (eventType.equals(AbstractGraphCommand.HSCROLL)) {
				continue;
			}
			if (eventType.equals(PromLogger.GROUP_EVENT_START)) {
				continue;
			}
			if (eventType.equals(PromLogger.GROUP_EVENT_END)) {
				continue;
			}

			firstEntry = auditTrailEntry;
			break;
		}

		if (firstEntry == null) {
			return N_A;
		}

		long startTime = processInstance.getLongAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP);
		Date firstEntryTimestamp = firstEntry.getTimestamp();
		long duration = firstEntryTimestamp.getTime() - startTime;

		return String.valueOf(duration);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}
}

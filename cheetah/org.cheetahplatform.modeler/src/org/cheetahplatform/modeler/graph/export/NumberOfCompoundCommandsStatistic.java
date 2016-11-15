package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class NumberOfCompoundCommandsStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Num. Compound Moves";
	}

	@Override
	public String getName() {
		return "The number of Compound Move Commands in the PPM";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (processInstance == null) {
			return N_A;
		}

		int count = 0;
		List<AuditTrailEntry> entries = processInstance.getEntries();
		for (int i = 0; i < entries.size(); i++) {
			AuditTrailEntry auditTrailEntry = entries.get(i);
			if (auditTrailEntry.getEventType().equals(PromLogger.GROUP_EVENT_START)) {
				AuditTrailEntry nextEntry = entries.get(i + 1);
				if (nextEntry.getEventType().equals(AbstractGraphCommand.MOVE_NODE)) {
					count++;
				}
			}
		}

		return String.valueOf(count);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

}

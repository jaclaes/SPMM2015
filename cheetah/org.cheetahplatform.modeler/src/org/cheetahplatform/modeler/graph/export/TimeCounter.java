package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;

public class TimeCounter {

	public static final String DURATION = "durationInSeconds";

	public Attribute computeTime(ProcessInstance modelingInstance) {
		List<AuditTrailEntry> entries = modelingInstance.getEntries();
		if (entries.size() < 2) {
			return new Attribute(DURATION, "0");
		}

		AuditTrailEntry firstEntry = entries.get(0);
		AuditTrailEntry lastEntry = entries.get(entries.size() - 1);
		long duration = lastEntry.getTimestamp().getTime() - firstEntry.getTimestamp().getTime();
		duration = duration / 1000;
		return new Attribute(DURATION, String.valueOf(duration));
	}

}

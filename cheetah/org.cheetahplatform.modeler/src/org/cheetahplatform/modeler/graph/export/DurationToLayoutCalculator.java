package org.cheetahplatform.modeler.graph.export;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.eclipse.core.runtime.Assert;

public class DurationToLayoutCalculator {
	private final ProcessInstance instance;

	public DurationToLayoutCalculator(ProcessInstance instance) {
		Assert.isNotNull(instance);
		this.instance = instance;
	}

	private long calculateDuration(Entry<AuditTrailEntry, AuditTrailEntry> entry) {
		AuditTrailEntry layoutEntry = entry.getKey();
		AuditTrailEntry createEntry = entry.getValue();

		long duration = layoutEntry.getTimestamp().getTime() - createEntry.getTimestamp().getTime();
		return duration;
	}

	private AuditTrailEntry findCreateEvent(List<AuditTrailEntry> entries, String id) {
		for (AuditTrailEntry auditTrailEntry : entries) {
			if (isCreateEntry(auditTrailEntry)) {
				if (id.equals(auditTrailEntry.getAttribute(AbstractGraphCommand.ID))) {
					return auditTrailEntry;
				}
			}
		}
		throw new IllegalStateException("No Create event found for id " + id);
	}

	public double getAverageDurationToLayout() {
		Map<AuditTrailEntry, Long> layoutDurations = getLayoutDurations();
		long total = 0;
		for (Entry<AuditTrailEntry, Long> entry : layoutDurations.entrySet()) {
			total += entry.getValue();
		}

		return ((double) total) / layoutDurations.size();
	}

	public AuditTrailEntry getCreateEntry(AuditTrailEntry layoutEntry) {
		return getLayoutEventsWithCreateEvents().get(layoutEntry);
	}

	public Map<AuditTrailEntry, Long> getLayoutDurations() {
		Map<AuditTrailEntry, Long> durations = new HashMap<AuditTrailEntry, Long>();
		Set<Entry<AuditTrailEntry, AuditTrailEntry>> entrySet = getLayoutEventsWithCreateEvents().entrySet();
		for (Entry<AuditTrailEntry, AuditTrailEntry> entry : entrySet) {
			long calculateDuration = calculateDuration(entry);
			durations.put(entry.getKey(), calculateDuration);
		}

		return durations;
	}

	public Map<AuditTrailEntry, AuditTrailEntry> getLayoutEventsWithCreateEvents() {
		Map<AuditTrailEntry, AuditTrailEntry> layoutEntriesToCreateEntries = new HashMap<AuditTrailEntry, AuditTrailEntry>();
		for (AuditTrailEntry auditTrailEntry : instance.getEntries()) {
			if (isLayoutEntry(auditTrailEntry)) {
				String attribute = auditTrailEntry.getAttribute(AbstractGraphCommand.ID);
				AuditTrailEntry createEntry = findCreateEvent(instance.getEntries(), attribute);
				layoutEntriesToCreateEntries.put(auditTrailEntry, createEntry);
			}
		}

		return layoutEntriesToCreateEntries;
	}

	private boolean isCreateEntry(AuditTrailEntry auditTrailEntry) {
		if (auditTrailEntry.getEventType().equals(AbstractGraphCommand.CREATE_NODE)) {
			return true;
		}
		if (auditTrailEntry.getEventType().equals(AbstractGraphCommand.CREATE_EDGE)) {
			return true;
		}

		return false;
	}

	private boolean isLayoutEntry(AuditTrailEntry auditTrailEntry) {
		if (auditTrailEntry.getEventType().equals(AbstractGraphCommand.MOVE_NODE)) {
			return true;
		}
		if (auditTrailEntry.getEventType().equals(AbstractGraphCommand.MOVE_EDGE_BENDPOINT)) {
			return true;
		}
		if (auditTrailEntry.getEventType().equals(AbstractGraphCommand.MOVE_EDGE_LABEL)) {
			return true;
		}
		if (auditTrailEntry.getEventType().equals(AbstractGraphCommand.CREATE_EDGE_BENDPOINT)) {
			return true;
		}
		if (auditTrailEntry.getEventType().equals(AbstractGraphCommand.DELETE_EDGE_BENDPOINT)) {
			return true;
		}

		return false;
	}
}

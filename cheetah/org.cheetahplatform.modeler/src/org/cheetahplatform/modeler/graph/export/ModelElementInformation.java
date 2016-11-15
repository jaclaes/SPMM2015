package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class ModelElementInformation {
	private List<AuditTrailEntry> entries;
	private String descriptor;

	public ModelElementInformation(AuditTrailEntry auditTrailEntry) {
		entries = new ArrayList<AuditTrailEntry>();
		entries.add(auditTrailEntry);
		descriptor = auditTrailEntry.getAttribute(AbstractGraphCommand.DESCRIPTOR);
	}

	public void addEntry(AuditTrailEntry auditTrailEntry) {
		entries.add(auditTrailEntry);
	}

	public Date getCreationTimestamp() {
		if (entries.isEmpty()) {
			throw new IllegalStateException("Cannot be empty");
		}

		AuditTrailEntry auditTrailEntry = entries.get(0);
		return auditTrailEntry.getTimestamp();
	}

	/**
	 * @return the descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}

	public long getDurationToLastMove() {
		return getLastMoveTimestamp().getTime() - getCreationTimestamp().getTime();
	}

	/**
	 * @return the entries
	 */
	public List<AuditTrailEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	public Date getLastMoveTimestamp() {
		AuditTrailEntry lastMove = entries.get(0);

		for (AuditTrailEntry entry : entries) {
			if (AbstractGraphCommand.MOVE_NODE.equals(entry.getEventType())) {
				lastMove = entry;
			}
		}
		return lastMove.getTimestamp();

	}

	public Date getLastTouchedTimestamp() {
		AuditTrailEntry lastEntry = entries.get(entries.size() - 1);
		return lastEntry.getTimestamp();
	}

	public int getNumberOfMoves() {
		int count = 0;
		for (AuditTrailEntry entry : entries) {
			if (entry.getEventType().equals(AbstractGraphCommand.MOVE_NODE)) {
				count++;
			}
		}
		return count;
	}
}

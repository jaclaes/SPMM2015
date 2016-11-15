package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.eclipse.core.runtime.Assert;

public class Chunk {
	private String type;
	private Date startTime;
	private Date endTime;
	private int fromStepIndex;
	private int toStepIndex;
	private List<AuditTrailEntry> entries;

	public Chunk(String type, Date startTime, int fromStepIndex) {
		Assert.isNotNull(startTime);
		Assert.isNotNull(type);
		this.fromStepIndex = fromStepIndex;
		this.toStepIndex = fromStepIndex;
		this.startTime = startTime;
		this.endTime = startTime;
		this.type = type.trim();
		entries = new ArrayList<AuditTrailEntry>();
	}

	public void addAllEntries(List<AuditTrailEntry> toAdd) {
		entries.addAll(toAdd);
	}

	public void addEntry(AuditTrailEntry entry) {
		entries.add(entry);
	}

	public boolean containsEntry(AuditTrailEntry entry) {
		return entries.contains(entry);
	}

	public long getDuration() {
		if (endTime == null) {
			return -1;
		}
		return endTime.getTime() - startTime.getTime();
	}

	public Date getEndTime() {
		return endTime;
	}

	public List<AuditTrailEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	public int getFromStepIndex() {
		return fromStepIndex;
	}

	public int getSize() {
		return entries.size();
	}

	public Date getStartTime() {
		return startTime;
	}

	public int getToStepIndex() {
		return toStepIndex;
	}

	public String getType() {
		return type;
	}

	public boolean isSameType(String toCheck) {
		return type.equals(toCheck.trim());
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setToStepIndex(int toStepIndex) {
		this.toStepIndex = toStepIndex;
	}

	@Override
	public String toString() {
		return getType() + " [" + startTime.getTime() + "-" + endTime.getTime() + "] " + getSize() + " steps";
	}
}
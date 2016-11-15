package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;

public class SlidingWindow {
	private int numberOfStartElements;
	private int numberOfEndElements;
	private long startTimestamp;
	private long endTimestamp;
	private List<Chunk> chunks;
	private List<ProcessOfProcessModelingIteration> iterations;
	private List<AuditTrailEntry> entries;

	public SlidingWindow(long startTime, long endTime) {
		this.startTimestamp = startTime;
		this.endTimestamp = endTime;
		chunks = new ArrayList<Chunk>();
		iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		entries = new ArrayList<AuditTrailEntry>();
	}

	public void addAuditTrailEntry(AuditTrailEntry entry) {
		entries.add(entry);
	}

	public void addChunk(Chunk chunk) {
		chunks.add(chunk);
	}

	public void addIteration(ProcessOfProcessModelingIteration iteration) {
		iterations.add(iteration);
	}

	protected List<Chunk> getChunks() {
		return Collections.unmodifiableList(chunks);
	}

	protected long getDuration() {
		return getEndTimestamp() - getStartTimestamp();
	}

	protected long getEndTimestamp() {
		return endTimestamp;
	}

	protected List<AuditTrailEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	protected List<ProcessOfProcessModelingIteration> getIterations() {
		return Collections.unmodifiableList(iterations);
	}

	public int getNumberOfAddEvents() {
		int total = 0;
		for (AuditTrailEntry entry : getEntries()) {
			if (ModelingPhaseChunkExtractor.isAddingEntry(entry)) {
				total++;
			}
		}

		return total;
	}

	public int getNumberOfDeleteEvents() {
		int total = 0;
		for (AuditTrailEntry entry : getEntries()) {
			if (ModelingPhaseChunkExtractor.isRemovingEntry(entry)) {
				total++;
			}
		}

		return total;
	}

	protected int getNumberOfEndElements() {
		return numberOfEndElements;
	}

	public int getNumberOfLayoutEvents() {
		int total = 0;
		for (AuditTrailEntry entry : getEntries()) {
			if (ModelingPhaseChunkExtractor.isReconciliationEvent(entry)) {
				total++;
			}
		}

		return total;
	}

	protected int getNumberOfStartElements() {
		return numberOfStartElements;
	}

	protected long getStartTimestamp() {
		return startTimestamp;
	}

	public long getTimeOfComprehension() {
		long total = endTimestamp - startTimestamp;

		long currentDuration = 1000;
		int startIndex = -1;
		for (int i = 0; i < getEntries().size(); i++) {
			AuditTrailEntry auditTrailEntry = getEntries().get(i);
			if (i == getEntries().size() - 1) {
				total -= currentDuration;
				continue;
			}

			AuditTrailEntry nextEntry = getEntries().get(i + 1);
			long nextTimeStamp = nextEntry.getTimestamp().getTime();
			long currentTimeStamp = auditTrailEntry.getTimestamp().getTime();
			if (nextTimeStamp - currentTimeStamp > 1000) {
				total -= currentDuration;
				currentDuration = 1000;
				startIndex = -1;
				continue;
			}

			if (startIndex >= 0) {
				long startIndexTimeStamp = getEntries().get(startIndex).getTimestamp().getTime();
				currentDuration = nextTimeStamp - startIndexTimeStamp;
				if (currentDuration < 1000) {
					currentDuration = 1000;
				}
				continue;
			}

			startIndex = i;
			currentDuration = nextTimeStamp - currentTimeStamp;
			if (currentDuration < 1000) {
				currentDuration = 1000;
			}
		}

		return total;
	}

	protected void setNumberOfEndElements(int numberOfEndElements) {
		this.numberOfEndElements = numberOfEndElements;
	}

	protected void setNumberOfStartElements(int numberOfStartElements) {
		this.numberOfStartElements = numberOfStartElements;
	}

	public int getNumberOfAddEventsForClustering() {
		int total = 0;
		for (AuditTrailEntry entry : getEntries()) {
			if (ModelingPhaseChunkExtractor.isAddingEntry(entry)) {
				total++;
			}
			if (ModelingPhaseChunkExtractor.isReconnectEvent(entry)) {
				total++;
			}
		}
	
		return total;
	}

	public int getNumberOfDeleteEventsForClustering() {
		int total = 0;
		for (AuditTrailEntry entry : getEntries()) {
			if (ModelingPhaseChunkExtractor.isRemovingEntry(entry)) {
				total++;
			}
			if (ModelingPhaseChunkExtractor.isReconnectEvent(entry)) {
				total++;
			}
		}
	
		return total;
	}

	public int getNumberOfLayoutEventsForClustering() {
		int total = 0;
		for (AuditTrailEntry entry : getEntries()) {
			if (ModelingPhaseChunkExtractor.isRenamingEvent(entry)) {
				if (ModelingPhaseChunkExtractor.isAddNameEvent(entry)) {
					continue;
				}
				total++;
			} else {
				if (ModelingPhaseChunkExtractor.isReconciliationEvent(entry)) {
					total++;
				}
			}
		}
	
		return total;
	}
}

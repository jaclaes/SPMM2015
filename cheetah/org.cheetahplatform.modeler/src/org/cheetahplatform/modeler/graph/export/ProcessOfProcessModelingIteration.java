package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;

public class ProcessOfProcessModelingIteration {
	private List<Chunk> chunks;

	public ProcessOfProcessModelingIteration(Chunk chunk) {
		chunks = new ArrayList<Chunk>();
		chunks.add(chunk);
	}

	public void addChunk(Chunk chunk) {
		chunks.add(chunk);
	}

	public double calcualtePenalty(double comprehensionPenalty, double modelingPenalty, double reconciliationPenalty) {
		double penalty = 0.0;
		if (!containsPhase(ModelingPhaseChunkExtractor.COMPREHENSION)) {
			penalty += comprehensionPenalty;
		}
		if (!containsPhase(ModelingPhaseChunkExtractor.MODELING)) {
			penalty += modelingPenalty;
		}
		if (!containsPhase(ModelingPhaseChunkExtractor.RECONCILIATION)) {
			penalty += reconciliationPenalty;
		}
		return penalty;
	}

	public boolean containsComprehensionPhase() {
		return containsPhase(ModelingPhaseChunkExtractor.COMPREHENSION);
	}

	public boolean containsEntry(AuditTrailEntry entry) {
		for (Chunk chunk : chunks) {
			if (chunk.containsEntry(entry)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsModelingPhase() {
		return containsPhase(ModelingPhaseChunkExtractor.MODELING);
	}

	private boolean containsPhase(String type) {
		for (Chunk chunk : chunks) {
			if (chunk.getType().equals(type)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsReconciliationPhase() {
		return containsPhase(ModelingPhaseChunkExtractor.RECONCILIATION);
	}

	public List<Chunk> getChunks() {
		return Collections.unmodifiableList(chunks);
	}

	public String getDescription() {
		StringBuilder builder = new StringBuilder();
		for (Chunk chunk : chunks) {
			builder.append(chunk.getType());
			builder.append(" ");
		}
		return builder.toString();
	}

	public long getDuration() {
		if (chunks.isEmpty()) {
			return 0;
		}

		long startTime = getStartTime();
		long endTime = getEndTime();
		return endTime - startTime;
	}

	public long getEndTime() {
		if (chunks.isEmpty()) {
			return 0;
		}

		return chunks.get(chunks.size() - 1).getEndTime().getTime();
	}

	public long getStartTime() {
		if (chunks.isEmpty()) {
			return 0;
		}
		return chunks.get(0).getStartTime().getTime();
	}

	public int numberOfAddedElements() {
		int total = 0;

		for (Chunk chunk : chunks) {
			for (AuditTrailEntry entry : chunk.getEntries()) {
				if (ModelingPhaseChunkExtractor.isAddingEntry(entry)) {
					total++;
				}
			}
		}
		return total;
	}

	public int numberOfLayoutElements() {
		int total = 0;

		for (Chunk chunk : chunks) {
			for (AuditTrailEntry entry : chunk.getEntries()) {
				if (ModelingPhaseChunkExtractor.isLayoutEvent(entry)) {
					total++;
				}
			}
		}
		return total;
	}

	public int numberOfModelingRenameElements() {
		int total = 0;

		for (Chunk chunk : chunks) {
			for (AuditTrailEntry entry : chunk.getEntries()) {
				if (ModelingPhaseChunkExtractor.isModelingRenameEvent(entry)) {
					total++;
				}
			}
		}
		return total;
	}

	public double numberOfOperations() {
		int total = 0;

		for (Chunk chunk : chunks) {
			for (AuditTrailEntry entry : chunk.getEntries()) {
				if (ModelingPhaseChunkExtractor.isReconciliationEvent(entry)) {
					total++;
					continue;
				}

				if (ModelingPhaseChunkExtractor.isModelingEvent(entry)) {
					total++;
					continue;
				}
			}
		}
		return total;
	}

	public int numberOfReconciliationElements() {
		int total = 0;

		for (Chunk chunk : chunks) {
			for (AuditTrailEntry entry : chunk.getEntries()) {
				if (ModelingPhaseChunkExtractor.isReconciliationEvent(entry)) {
					total++;
				}
			}
		}
		return total;
	}

	public int numberOfReconnectElements() {
		int total = 0;

		for (Chunk chunk : chunks) {
			for (AuditTrailEntry entry : chunk.getEntries()) {
				if (ModelingPhaseChunkExtractor.isReconnectEvent(entry)) {
					total++;
				}
			}
		}
		return total;
	}

	public int numberOfRemovedElements() {
		int total = 0;

		for (Chunk chunk : chunks) {
			for (AuditTrailEntry entry : chunk.getEntries()) {
				if (ModelingPhaseChunkExtractor.isRemovingEntry(entry)) {
					total++;
				}
			}
		}
		return total;
	}
}
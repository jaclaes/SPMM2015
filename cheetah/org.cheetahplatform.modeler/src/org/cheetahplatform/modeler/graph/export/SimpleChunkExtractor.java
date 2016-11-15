package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;

public class SimpleChunkExtractor implements IChunkExtractor {
	@Override
	public List<Chunk> extractChunks(ProcessInstance modelingInstance) {
		List<Chunk> chunks = new ArrayList<Chunk>();

		List<AuditTrailEntry> entries = modelingInstance.getEntries();
		for (AuditTrailEntry auditTrailEntry : entries) {

			int stepIndex = entries.indexOf(auditTrailEntry);
			String eventType = auditTrailEntry.getEventType();
			if (chunks.isEmpty()) {
				chunks.add(new Chunk(eventType, auditTrailEntry.getTimestamp(), stepIndex));
				continue;
			}
			Chunk chunk = chunks.get(chunks.size() - 1);
			if (chunk.isSameType(eventType)) {
				chunk.addEntry(auditTrailEntry);
				chunk.setToStepIndex(stepIndex);
				chunk.setEndTime(auditTrailEntry.getTimestamp());
			} else {
				chunk.setEndTime(auditTrailEntry.getTimestamp());
				chunks.add(new Chunk(eventType, auditTrailEntry.getTimestamp(), stepIndex));
			}
		}

		return chunks;
	}

	@Override
	public List<String> getAdditionalInformation() {
		return new ArrayList<String>();
	}
}

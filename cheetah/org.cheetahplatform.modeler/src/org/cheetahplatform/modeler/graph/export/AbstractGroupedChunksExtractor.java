package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;

public abstract class AbstractGroupedChunksExtractor implements IChunkExtractor {
	protected List<Chunk> chunks;

	public AbstractGroupedChunksExtractor() {
		chunks = new ArrayList<Chunk>();
	}

	@Override
	public List<Chunk> extractChunks(ProcessInstance modelingInstance) {
		chunks.clear();

		List<AuditTrailEntry> entries = modelingInstance.getEntries();
		for (AuditTrailEntry auditTrailEntry : entries) {
			String eventType = auditTrailEntry.getEventType().trim();
			if (PromLogger.GROUP_EVENT_END.equals(eventType) || PromLogger.GROUP_EVENT_START.equals(eventType)) {
				continue;
			}

			int stepIndex = entries.indexOf(auditTrailEntry);

			if (chunks.isEmpty()) {
				Chunk chunk = new Chunk(getGroup(auditTrailEntry), auditTrailEntry.getTimestamp(), stepIndex);
				chunks.add(chunk);
				continue;
			}
			Chunk chunk = chunks.get(chunks.size() - 1);
			if (chunk.isSameType(getGroup(auditTrailEntry))) {
				chunk.addEntry(auditTrailEntry);
				chunk.setToStepIndex(stepIndex);
				chunk.setEndTime(auditTrailEntry.getTimestamp());
			} else {
				chunks.add(new Chunk(getGroup(auditTrailEntry), auditTrailEntry.getTimestamp(), stepIndex));
			}
		}

		return chunks;
	}

	@Override
	public List<String> getAdditionalInformation() {
		List<String> info = new ArrayList<String>();
		info.add("Number of Chunks;" + chunks.size() + ";");
		return info;
	}

	protected abstract String getGroup(AuditTrailEntry event);
}
package org.cheetahplatform.modeler.graph.export;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class ModelingPhaseChunkExtractor extends AbstractGroupedChunksExtractor {
	public static final int DEFAULT_RENAME_DURATION = 2000;
	public static final String RECONCILIATION = "RECONCILIATION";
	public static final String MODELING = "MODELING";
	public static final String COMPREHENSION = "COMPREHENSION";

	public static String getModelingPhase(AuditTrailEntry event) {
		String eventType = event.getEventType().trim();
		if (AbstractGraphCommand.CREATE_EDGE_BENDPOINT.equals(eventType)) {
			return RECONCILIATION;
		}
		if (AbstractGraphCommand.DELETE_EDGE_BENDPOINT.equals(eventType)) {
			return RECONCILIATION;
		}
		if (AbstractGraphCommand.MOVE_EDGE_BENDPOINT.equals(eventType)) {
			return RECONCILIATION;
		}
		if (AbstractGraphCommand.MOVE_EDGE_LABEL.equals(eventType)) {
			return RECONCILIATION;
		}
		if (AbstractGraphCommand.MOVE_NODE.equals(eventType)) {
			return RECONCILIATION;
		}
		if (AbstractGraphCommand.RENAME.equals(eventType)) {
			if (isModelingRenameEvent(event)) {
				return MODELING;
			}
			return RECONCILIATION;
		}

		if (AbstractGraphCommand.VSCROLL.equals(eventType) || AbstractGraphCommand.HSCROLL.equals(eventType)
				|| PromLogger.GROUP_EVENT_START.equals(eventType) || PromLogger.GROUP_EVENT_END.equals(eventType)) {
			throw new IllegalArgumentException("Illegal event type: " + eventType);
		}

		return MODELING;
	}

	public static boolean isAddingEntry(AuditTrailEntry entry) {
		if (entry.getEventType().equals(AbstractGraphCommand.CREATE_NODE)) {
			return true;
		}
		if (entry.getEventType().equals(AbstractGraphCommand.CREATE_EDGE)) {
			return true;
		}
		return false;
	}

	public static boolean isAddNameEvent(AuditTrailEntry event) {
		String name = event.getAttribute(AbstractGraphCommand.NAME);
		if (name == null) {
			return true;
		}
		name = name.trim();

		if (name.isEmpty()) {
			return true;
		}

		if (name.equals("Sequence Flow")) {
			return true;
		}

		return false;
	}

	public static boolean isLayoutEvent(AuditTrailEntry entry) {
		if (entry.getEventType().equals(AbstractGraphCommand.MOVE_NODE)) {
			return true;
		}
		if (entry.getEventType().equals(AbstractGraphCommand.MOVE_EDGE_LABEL)) {
			return true;
		}
		if (entry.getEventType().equals(AbstractGraphCommand.CREATE_EDGE_BENDPOINT)) {
			return true;
		}
		if (entry.getEventType().equals(AbstractGraphCommand.DELETE_EDGE_BENDPOINT)) {
			return true;
		}
		if (entry.getEventType().equals(AbstractGraphCommand.MOVE_EDGE_BENDPOINT)) {
			return true;
		}

		return false;
	}

	public static boolean isModelingEvent(AuditTrailEntry entry) {
		if (isAddingEntry(entry)) {
			return true;
		}

		if (ModelingPhaseChunkExtractor.isRemovingEntry(entry)) {
			return true;
		}

		if (ModelingPhaseChunkExtractor.isReconnectEvent(entry)) {
			return true;
		}

		return false;
	}

	public static boolean isModelingRenameEvent(AuditTrailEntry event) {
		if (!event.getEventType().equals(AbstractGraphCommand.RENAME)) {
			return false;
		}

		if (isAddNameEvent(event)) {
			return true;
		}

		if (isRemoveNameEvent(event)) {
			return true;
		}

		return false;
	}

	public static boolean isReconciliationEvent(AuditTrailEntry entry) {
		if (entry.getEventType().equals(AbstractGraphCommand.RENAME)) {
			return !isModelingRenameEvent(entry);
		}
		return isLayoutEvent(entry);
	}

	public static boolean isReconnectEvent(AuditTrailEntry entry) {
		return entry.getEventType().equals(AbstractGraphCommand.RECONNECT_EDGE);
	}

	public static boolean isRemoveNameEvent(AuditTrailEntry event) {
		if (!event.getEventType().equals(AbstractGraphCommand.RENAME)) {
			return false;
		}

		String newName = event.getAttribute(AbstractGraphCommand.NEW_NAME);
		if (newName == null) {
			return true;
		}
		if (newName.trim().isEmpty()) {
			return true;
		}

		return false;
	}

	public static boolean isRemovingEntry(AuditTrailEntry entry) {
		if (entry.getEventType().equals(AbstractGraphCommand.DELETE_EDGE)) {
			return true;
		}
		if (entry.getEventType().equals(AbstractGraphCommand.DELETE_NODE)) {
			return true;
		}
		return false;
	}

	public static boolean isRenamingEvent(AuditTrailEntry entry) {
		return entry.getEventType().equals(AbstractGraphCommand.RENAME);
	}

	private IModelingPhaseDetectionStrategy detectionStrategy;

	private int comprehensionTimeThreshold;

	private int comprehensionAggregrationThreshold;

	public ModelingPhaseChunkExtractor(IModelingPhaseDetectionStrategy modelingPhaseDetectionStrategy, int comprehensionThreshHold,
			int comprehensionAggregationThreshold) {
		this.detectionStrategy = modelingPhaseDetectionStrategy;
		comprehensionTimeThreshold = comprehensionThreshHold;
		comprehensionAggregrationThreshold = comprehensionAggregationThreshold;
	}

	@Override
	public List<Chunk> extractChunks(ProcessInstance modelingInstance) {
		chunks.clear();

		long lastStepTime = modelingInstance.getLongAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP);

		List<AuditTrailEntry> entries = modelingInstance.getEntries();
		for (AuditTrailEntry auditTrailEntry : entries) {
			String eventType = auditTrailEntry.getEventType().trim();
			if (PromLogger.GROUP_EVENT_END.equals(eventType) || PromLogger.GROUP_EVENT_START.equals(eventType)) {
				continue;
			}
			if (AbstractGraphCommand.HSCROLL.equals(eventType) || AbstractGraphCommand.VSCROLL.equals(eventType)) {
				continue;
			}

			int stepIndex = entries.indexOf(auditTrailEntry);
			if (isComprehensionPhase(lastStepTime, auditTrailEntry)) {
				Chunk comprehensionChunk = new Chunk(COMPREHENSION, new Date(lastStepTime), stepIndex);
				comprehensionChunk.setToStepIndex(stepIndex);
				comprehensionChunk.setEndTime(getStartTime(auditTrailEntry, lastStepTime));
				chunks.add(comprehensionChunk);
				Chunk chunk = new Chunk(getGroup(auditTrailEntry), getStartTime(auditTrailEntry, lastStepTime), stepIndex);
				chunk.addEntry(auditTrailEntry);
				chunk.setEndTime(auditTrailEntry.getTimestamp());
				chunks.add(chunk);
				lastStepTime = auditTrailEntry.getTimestamp().getTime();
				continue;
			}

			if (chunks.isEmpty()) {
				Chunk chunk = new Chunk(getGroup(auditTrailEntry), getStartTime(auditTrailEntry, lastStepTime), stepIndex);
				chunk.addEntry(auditTrailEntry);
				chunk.setEndTime(auditTrailEntry.getTimestamp());
				chunks.add(chunk);
				lastStepTime = auditTrailEntry.getTimestamp().getTime();
				continue;
			}

			Chunk chunk = chunks.get(chunks.size() - 1);
			if (chunk.isSameType(getGroup(auditTrailEntry))) {
				increaseChunkSize(auditTrailEntry, stepIndex, chunk);
			} else {
				if (detectionStrategy.switchToNextPhase(entries, auditTrailEntry)) {
					Chunk newChunk = new Chunk(getGroup(auditTrailEntry), getStartTime(auditTrailEntry, lastStepTime), stepIndex);
					newChunk.setEndTime(auditTrailEntry.getTimestamp());
					newChunk.addEntry(auditTrailEntry);
					chunks.add(newChunk);
				} else {
					increaseChunkSize(auditTrailEntry, stepIndex, chunk);
				}
			}

			lastStepTime = auditTrailEntry.getTimestamp().getTime();
		}
		mergeComprehensionChunks();

		return chunks;
	}

	@Override
	public List<String> getAdditionalInformation() {
		List<String> additionalInformation = super.getAdditionalInformation();
		additionalInformation.add("Smoothening Strategy; " + detectionStrategy.getDescription() + ";");
		additionalInformation.add("Comprehension Threshold; " + comprehensionTimeThreshold + ";");
		additionalInformation.add("Comprehension Aggregation; " + comprehensionAggregrationThreshold + ";");
		return additionalInformation;
	}

	@Override
	protected String getGroup(AuditTrailEntry event) {
		return getModelingPhase(event);
	}

	protected Date getStartTime(AuditTrailEntry auditTrailEntry, long lastStepTime) {
		if (auditTrailEntry.isAttributeDefined(AbstractGraphCommand.ADD_NODE_START_TIME)) {
			return new Date(auditTrailEntry.getLongAttribute(AbstractGraphCommand.ADD_NODE_START_TIME));
		}
		if (auditTrailEntry.isAttributeDefined(AbstractGraphCommand.RENAME_START_TIME)) {
			return new Date(auditTrailEntry.getLongAttribute(AbstractGraphCommand.RENAME_START_TIME));
		}

		if (auditTrailEntry.getEventType().equals(AbstractGraphCommand.RENAME)) {
			long timeStamp = auditTrailEntry.getTimestamp().getTime();
			if (timeStamp - lastStepTime > comprehensionTimeThreshold) {
				return new Date(timeStamp - DEFAULT_RENAME_DURATION);
			}
		}

		return new Date(auditTrailEntry.getTimestamp().getTime());
	}

	private void increaseChunkSize(AuditTrailEntry auditTrailEntry, int stepIndex, Chunk chunk) {
		chunk.setToStepIndex(stepIndex);
		chunk.setEndTime(auditTrailEntry.getTimestamp());
		chunk.addEntry(auditTrailEntry);
	}

	protected boolean isComprehensionPhase(long lastStepTime, AuditTrailEntry auditTrailEntry) {
		long startTime;
		startTime = getStartTime(auditTrailEntry, lastStepTime).getTime();

		return startTime - lastStepTime > comprehensionTimeThreshold;
	}

	protected void mergeComprehensionChunks() {
		Iterator iterator = chunks.listIterator();
		while (iterator.hasNext()) {
			Chunk chunk = (Chunk) iterator.next();
			if (!chunk.getType().equals(COMPREHENSION)) {
				continue;
			}

			int index = chunks.indexOf(chunk);
			if (index + 2 >= chunks.size()) {
				continue;
			}

			Chunk chunkToCheck = chunks.get(index + 2);
			if (!chunkToCheck.getType().equals(COMPREHENSION)) {
				continue;
			}

			if (chunkToCheck.getStartTime().getTime() - chunk.getEndTime().getTime() < comprehensionAggregrationThreshold) {
				Chunk intermediateChunk = (Chunk) iterator.next();
				chunk.addAllEntries(intermediateChunk.getEntries());
				iterator.remove();
				Chunk comprehensionChunkToRemove = (Chunk) iterator.next();
				chunk.setEndTime(comprehensionChunkToRemove.getEndTime());
				chunk.addAllEntries(comprehensionChunkToRemove.getEntries());
				iterator.remove();
			}
		}
	}
}

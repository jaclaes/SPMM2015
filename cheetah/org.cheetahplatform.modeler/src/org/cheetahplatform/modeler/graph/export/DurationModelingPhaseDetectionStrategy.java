package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.graph.IBMLayouter;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.eclipse.core.runtime.Assert;

public class DurationModelingPhaseDetectionStrategy implements IModelingPhaseDetectionStrategy {
	private final int duration;
	private int timeOut;

	public DurationModelingPhaseDetectionStrategy(int duration, int timeOut) {
		Assert.isLegal(duration >= 0);
		this.duration = duration;
		this.timeOut = timeOut;
	}

	// private AuditTrailEntry findPreviousAuditTrailEntry(long startTime, List<AuditTrailEntry> entries) {
	// for (int i = 0; i < entries.size(); i++) {
	// AuditTrailEntry auditTrailEntry = entries.get(i);
	//
	// if (auditTrailEntry.getTimestamp().getTime() < startTime) {
	// continue;
	// }
	//
	// if (i == 0) {
	// return null;
	// }
	//
	// return getPreviousEntry(auditTrailEntry, entries);
	// }
	//
	// AuditTrailEntry lastEntry = entries.get(entries.size() - 1);
	// if (isEntryToIgnore(lastEntry)) {
	// return getPreviousEntry(lastEntry, entries);
	// }
	// return lastEntry;
	// }

	@Override
	public String getDescription() {
		return "Duration: " + duration + " Timeout: " + timeOut;
	}

	private AuditTrailEntry getPreviousEntry(AuditTrailEntry entry, List<AuditTrailEntry> entries) {
		int index = entries.indexOf(entry) - 1;

		AuditTrailEntry previousEntry = entries.get(index);
		while (isEntryToIgnore(previousEntry)) {
			if (index <= 0) {
				return null;
			}
			previousEntry = entries.get(entries.indexOf(previousEntry) - 1);
		}

		return previousEntry;
	}

	public boolean isAutomatedLayout(List<AuditTrailEntry> entries, int index) {
		AuditTrailEntry previousStep = entries.get(index - 1);
		if (PromLogger.GROUP_EVENT_START.equals(previousStep.getEventType())) {
			if (previousStep.isAttributeDefined(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME)) {
				if (IBMLayouter.LAYOUT.equals(previousStep.getAttribute(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isEntryToIgnore(AuditTrailEntry entry) {
		return isGroupEvent(entry) || isScrollEvent(entry);
	}

	private boolean isGroupEvent(AuditTrailEntry previousEntry) {
		String eventType = previousEntry.getEventType();
		return eventType.equals(PromLogger.GROUP_EVENT_END) || previousEntry.getEventType().equals(PromLogger.GROUP_EVENT_START);
	}

	private boolean isScrollEvent(AuditTrailEntry previousEntry) {
		String eventType = previousEntry.getEventType();
		return eventType.equals(AbstractGraphCommand.HSCROLL) || eventType.equals(AbstractGraphCommand.VSCROLL);
	}

	private boolean isWithinCompoundGraphCommand(AuditTrailEntry auditTrailEntry, List<AuditTrailEntry> entries) {
		for (int i = entries.indexOf(auditTrailEntry) - 1; i >= 0; i--) {
			AuditTrailEntry entry = entries.get(i);
			if (entry.getEventType().equals(PromLogger.GROUP_EVENT_START)) {
				return true;
			}

			if (entry.getEventType().equals(PromLogger.GROUP_EVENT_END)) {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean switchToNextPhase(List<AuditTrailEntry> entries, AuditTrailEntry auditTrailEntry) {
		int index = entries.indexOf(auditTrailEntry);

		if (index == 0) {
			return true;
		}

		if (index == entries.size() - 1) {
			return true;
		}
		List<AuditTrailEntry> subList = entries.subList(index, entries.size());
		if (subList.isEmpty()) {
			return true;
		}

		if (subList.size() == 1) {
			return true;
		}

		AuditTrailEntry previousStep;
		if (isAutomatedLayout(entries, index)) {
			return true;
		}

		previousStep = getPreviousEntry(auditTrailEntry, entries);
		long previousTimestamp = previousStep.getTimestamp().getTime();
		long currentTimestamp = auditTrailEntry.getTimestamp().getTime();
		if (currentTimestamp - previousTimestamp > timeOut) {
			return true;
		}

		// compound graph command
		if (isWithinCompoundGraphCommand(auditTrailEntry, entries)) {
			return false;
		}

		AuditTrailEntry startEntry = subList.get(0);
		long end = startEntry.getTimestamp().getTime();
		for (int i = 1; i < subList.size(); i++) {
			AuditTrailEntry entry = subList.get(i);

			if (isEntryToIgnore(entry)) {
				continue;
			}

			String phase1 = ModelingPhaseChunkExtractor.getModelingPhase(startEntry);
			String phase2 = ModelingPhaseChunkExtractor.getModelingPhase(entry);

			if (!phase1.equals(phase2)) {
				long start = startEntry.getTimestamp().getTime();

				return end - start > duration;
			}
			end = entry.getTimestamp().getTime();
		}
		return true;
	}
}

package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class PhaseSimilarityCalculator {

	private Map<AuditTrailEntry, Integer> numberOfElements;
	private ProcessInstance processInstance;
	private final IPhaseSimilarityCalculationStrategy strategy;

	public PhaseSimilarityCalculator(IPhaseSimilarityCalculationStrategy strategy) {
		Assert.isNotNull(strategy);
		this.strategy = strategy;
	}

	private void addAuditTrailEntries(SlidingWindow slidingWindow) {
		List<AuditTrailEntry> entries = processInstance.getEntries();
		for (AuditTrailEntry auditTrailEntry : entries) {
			if (isEntryToIgnore(auditTrailEntry)) {
				continue;
			}

			long timestamp = auditTrailEntry.getTimestamp().getTime();
			if (timestamp >= slidingWindow.getStartTimestamp() && timestamp <= slidingWindow.getEndTimestamp()) {
				slidingWindow.addAuditTrailEntry(auditTrailEntry);
			}
		}
	}

	private void addChunks(SlidingWindow slidingWindow, List<Chunk> chunks) {
		for (Chunk chunk : chunks) {
			if (chunk.getStartTime().getTime() > slidingWindow.getEndTimestamp()) {
				continue;
			}

			if (chunk.getEndTime().getTime() < slidingWindow.getStartTimestamp()) {
				continue;
			}

			slidingWindow.addChunk(chunk);
		}
	}

	private void addIterations(SlidingWindow slidingWindow, List<ProcessOfProcessModelingIteration> iterations) {
		for (ProcessOfProcessModelingIteration iteration : iterations) {
			if (iteration.getEndTime() < slidingWindow.getStartTimestamp()) {
				continue;
			}

			if (iteration.getStartTime() > slidingWindow.getEndTimestamp()) {
				continue;
			}

			slidingWindow.addIteration(iteration);
		}
	}

	private long calcualteSlidingWindowSize() {
		return strategy.getSlidingWindowDuration();
	}

	public List<SlidingWindow> calculate(ProcessInstance processInstance, List<Chunk> chunks,
			List<ProcessOfProcessModelingIteration> iterations) {
		this.processInstance = processInstance;
		List<SlidingWindow> windows = new ArrayList<SlidingWindow>();
		if (processInstance.getEntries().isEmpty()) {
			return Collections.emptyList();
		}

		ModelingPhaseDiagrammLineFragmentExtrator extrator = new ModelingPhaseDiagrammLineFragmentExtrator();
		numberOfElements = extrator.extractNumberOfElements(processInstance, chunks);

		AuditTrailEntry auditTrailEntry = processInstance.getEntries().get(processInstance.getEntries().size() - 1);
		long endOfProcessTime = auditTrailEntry.getTimestamp().getTime();
		long startTime = processInstance.getLongAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP);

		while (startTime + calcualteSlidingWindowSize() <= endOfProcessTime) {
			SlidingWindow slidingWindow = createSlidingWindow(startTime, iterations, chunks);
			windows.add(slidingWindow);
			startTime += strategy.getStepSize();
		}

		return windows;
	}

	private SlidingWindow createSlidingWindow(long startTime, List<ProcessOfProcessModelingIteration> iterations, List<Chunk> chunks) {
		long endTime = startTime + calcualteSlidingWindowSize();
		SlidingWindow slidingWindow = new SlidingWindow(startTime, endTime);

		setStartNumberOfElements(startTime, slidingWindow);
		setEndNumberOfElements(endTime, slidingWindow);

		addAuditTrailEntries(slidingWindow);
		addChunks(slidingWindow, chunks);
		addIterations(slidingWindow, iterations);

		return slidingWindow;
	}

	private AuditTrailEntry findPreviousAuditTrailEntry(long startTime) {
		List<AuditTrailEntry> entries = processInstance.getEntries();
		for (int i = 0; i < entries.size(); i++) {
			AuditTrailEntry auditTrailEntry = entries.get(i);

			if (auditTrailEntry.getTimestamp().getTime() < startTime) {
				continue;
			}

			if (i == 0) {
				return null;
			}

			return getPreviousEntry(auditTrailEntry);
		}

		AuditTrailEntry lastEntry = entries.get(entries.size() - 1);
		if (isEntryToIgnore(lastEntry)) {
			return getPreviousEntry(lastEntry);
		}
		return lastEntry;
	}

	private AuditTrailEntry getPreviousEntry(AuditTrailEntry entry) {
		List<AuditTrailEntry> entries = processInstance.getEntries();
		int index = entries.indexOf(entry) - 1;

		AuditTrailEntry previousEntry = entries.get(index);
		while (isEntryToIgnore(previousEntry)) {
			if (index <= 0) {
				return null;
			}
			index--;
			previousEntry = entries.get(index);
		}

		return previousEntry;
	}

	private boolean isEntryToIgnore(AuditTrailEntry previousEntry) {
		return isGroupEvent(previousEntry) || isScrollEvent(previousEntry);
	}

	private boolean isGroupEvent(AuditTrailEntry previousEntry) {
		String eventType = previousEntry.getEventType();
		return eventType.equals(PromLogger.GROUP_EVENT_END) || previousEntry.getEventType().equals(PromLogger.GROUP_EVENT_START);
	}

	private boolean isScrollEvent(AuditTrailEntry previousEntry) {
		String eventType = previousEntry.getEventType();
		return eventType.equals(AbstractGraphCommand.HSCROLL) || eventType.equals(AbstractGraphCommand.VSCROLL);
	}

	private void setEndNumberOfElements(long endTime, SlidingWindow slidingWindow) {
		AuditTrailEntry auditTrailEntry = findPreviousAuditTrailEntry(endTime);
		if (auditTrailEntry == null) {
			slidingWindow.setNumberOfEndElements(0);
		} else {
			slidingWindow.setNumberOfEndElements(numberOfElements.get(auditTrailEntry));
		}
	}

	private void setStartNumberOfElements(long startTime, SlidingWindow slidingWindow) {
		AuditTrailEntry auditTrailEntry = findPreviousAuditTrailEntry(startTime);
		if (auditTrailEntry == null) {
			slidingWindow.setNumberOfStartElements(0);
		} else {
			slidingWindow.setNumberOfStartElements(numberOfElements.get(auditTrailEntry));
		}
	}
}

package org.cheetahplatform.modeler.graph.model;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.export.Chunk;
import org.cheetahplatform.modeler.graph.export.IModelingPhaseDetectionStrategy;
import org.cheetahplatform.modeler.graph.export.ModelingPhase;
import org.cheetahplatform.modeler.graph.export.ModelingPhaseChunkExtractor;
import org.cheetahplatform.modeler.graph.export.ModelingPhaseDiagrammLineFragmentExtrator;
import org.cheetahplatform.modeler.graph.export.ProcessOfProcessModelingIteration;
import org.cheetahplatform.modeler.graph.export.ProcessOfProcessModelingIterationsExtractor;

public class ModelingPhaseModel {
	private final ReplayModel replayModel;
	private List<Chunk> chunks;
	private List<ModelingPhase> lineFragments;

	public ModelingPhaseModel(ReplayModel replayModel) {
		Assert.isNotNull(replayModel);
		this.replayModel = replayModel;
	}

	private List<Chunk> extractChunks(IModelingPhaseDetectionStrategy modelingPhaseDetectionStrategy, int comprehensionThreshHold,
			int comprehensionAggregationThreshold) {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(modelingPhaseDetectionStrategy, comprehensionThreshHold,
				comprehensionAggregationThreshold);
		ProcessInstance processInstance = replayModel.getProcessInstance();
		chunks = extractor.extractChunks(processInstance);

		String attribute = processInstance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		Process process = ProcessRepository.getProcess(attribute);
		String type = processInstance.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
		Graph graph = AbstractModelingActivity.loadInitialGraph(process, type);

		ReplayModel localReplayModel = new ReplayModel(new GraphCommandStack(graph), replayModel.getProcessInstanceDatabaseHandle(), graph);
		lineFragments = new ModelingPhaseDiagrammLineFragmentExtrator().extractLineFragments(graph, localReplayModel.getReplayer(),
				processInstance, chunks);
		return chunks;
	}

	public List<Chunk> getChunks(IModelingPhaseDetectionStrategy modelingPhaseDetectionStrategy, int comprehensionThreshHold,
			int comprehensionAggregationThreshold) {
		if (chunks == null) {
			extractChunks(modelingPhaseDetectionStrategy, comprehensionThreshHold, comprehensionAggregationThreshold);
		}

		return chunks;
	}

	public ModelingPhaseSequence getComprehensionLineSeries() {
		List<ModelingPhase> fragments = new ArrayList<ModelingPhase>();
		for (ModelingPhase lineFragment : lineFragments) {
			if (lineFragment.getType().equals(ModelingPhaseChunkExtractor.COMPREHENSION)) {
				fragments.add(lineFragment);
			}
		}
		return new ModelingPhaseSequence(ModelingPhaseChunkExtractor.COMPREHENSION, "Comprehension", fragments);
	}

	public int getDefaultComprehensionAggregationThreshold() {
		return ModelerConstants.DEFAULT_COMPREHENSION_AGGREGATION_THRESHOLD;
	}

	public int getDefaultComprehensionThreshold() {
		return ModelerConstants.DEFAULT_COMPREHENSION_THRESHOLD;
	}

	public IModelingPhaseDetectionStrategy getDefaultDetectionStrategy() {
		return ModelerConstants.DEFAULT_DETECTION_STRATEGY;
	}

	public long getDuration() {
		return getTimeStampOfLastLogEntry() - getStartTimeStamp();
	}

	public int getHighestNumberOfElements() {
		int highest = 0;
		for (ModelingPhase entry : lineFragments) {
			if (entry.getEndNumberOfElements() > highest) {
				highest = entry.getEndNumberOfElements();
			}
		}
		return highest;
	}

	public List<ProcessOfProcessModelingIteration> getIterations() {
		ProcessOfProcessModelingIterationsExtractor extractor = new ProcessOfProcessModelingIterationsExtractor();
		if (chunks == null) {
			throw new IllegalStateException("Chunks not extracted");
		}
		return extractor.extractIterations(chunks);
	}

	public ModelingPhaseSequence getModelingLineSeries() {
		List<ModelingPhase> fragments = new ArrayList<ModelingPhase>();
		for (ModelingPhase lineFragment : lineFragments) {
			if (lineFragment.getType().equals(ModelingPhaseChunkExtractor.MODELING)) {
				fragments.add(lineFragment);
			}
		}
		return new ModelingPhaseSequence(ModelingPhaseChunkExtractor.MODELING, "Modeling", fragments);
	}

	public ModelingPhaseSequence getReconciliationLineSeries() {
		List<ModelingPhase> fragments = new ArrayList<ModelingPhase>();
		for (ModelingPhase lineFragment : lineFragments) {
			if (lineFragment.getType().equals(ModelingPhaseChunkExtractor.RECONCILIATION)) {
				fragments.add(lineFragment);
			}
		}
		return new ModelingPhaseSequence(ModelingPhaseChunkExtractor.RECONCILIATION, "Reconciliation", fragments);
	}

	public long getRelativeTimeOfCurrentCommand() {
		long timestampOfCurrentCommand = replayModel.getTimestampOfCurrentCommand();
		if (timestampOfCurrentCommand < 0) {
			return getDuration();
		}

		long startTimeStamp = getStartTimeStamp();
		return timestampOfCurrentCommand - startTimeStamp;
	}

	public long getStartTimeStamp() {
		return replayModel.getProcessInstance().getLongAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP);
	}

	public long getTimeStampOfLastLogEntry() {
		List<AuditTrailEntry> entries = replayModel.getProcessInstance().getEntries();
		if (entries.isEmpty()) {
			return -1;
		}

		AuditTrailEntry lastEntry = entries.get(entries.size() - 1);
		return lastEntry.getTimestamp().getTime();
	}
}

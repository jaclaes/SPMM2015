package org.cheetahplatform.modeler.graph.export;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public abstract class AbstractShareOfCreatedElementsInUpcomingRecPhaseStatistc extends AbstractNumberOfTouchedElementsStatistic {

	protected double getRelativeValue(Map<Chunk, Set<Long>> modelingChunks, Map<Chunk, Set<Long>> reconciliationChunks, List<Chunk> chunks,
			Chunk chunk) {
		if (!chunk.getType().equals(ModelingPhaseChunkExtractor.RECONCILIATION)) {
			return -1;
		}

		Chunk previousChunk = chunks.get(chunks.indexOf(chunk) - 1);

		Set<Long> modelingChunk = modelingChunks.get(previousChunk);
		int numberOfCreatedElements = 0;
		if (modelingChunk != null) {
			numberOfCreatedElements = modelingChunk.size();
		}

		Set<Long> numberOfTouchedElementsInReconciliationPhase = reconciliationChunks.get(chunk);
		if (numberOfTouchedElementsInReconciliationPhase.size() == 0) {
			return -1;
		}
		double relative = ((double) numberOfCreatedElements) / numberOfTouchedElementsInReconciliationPhase.size();
		return relative;
	}

	protected Map<Chunk, Set<Long>> getModelingChunks(List<Chunk> chunks) {
		Map<Chunk, Set<Long>> modelingChunks = getTouchedElementsPerChunk(chunks, ModelingPhaseChunkExtractor.MODELING,
				AbstractGraphCommand.CREATE_EDGE, AbstractGraphCommand.CREATE_NODE);
		return modelingChunks;
	}

	protected Map<Chunk, Set<Long>> getReconciliationChunks(List<Chunk> chunks) {
		Map<Chunk, Set<Long>> reconciliationChunks = getTouchedElementsPerChunk(chunks, ModelingPhaseChunkExtractor.RECONCILIATION,
				AbstractGraphCommand.MOVE_NODE, AbstractGraphCommand.MOVE_EDGE_LABEL, AbstractGraphCommand.CREATE_EDGE_BENDPOINT,
				AbstractGraphCommand.MOVE_EDGE_BENDPOINT, AbstractGraphCommand.DELETE_EDGE_BENDPOINT);
		return reconciliationChunks;
	}

}
package org.cheetahplatform.modeler.graph.export;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class MaxNumberOfMovedElementsPerReconciliationPhaseStatistic extends AbstractMaxNumberOfTouchedElementsStatistic {
	@Override
	protected Map<Chunk, Set<Long>> filterChunks(List<Chunk> chunks) {
		return getTouchedElementsPerChunk(chunks, ModelingPhaseChunkExtractor.RECONCILIATION, AbstractGraphCommand.MOVE_NODE);
	}

	@Override
	public String getHeader() {
		return "Max. # Diff. Moved Elements/Rec.";
	}

	@Override
	public String getName() {
		return "Max. Number of different moved nodes per reconcliliation phase";
	}
}

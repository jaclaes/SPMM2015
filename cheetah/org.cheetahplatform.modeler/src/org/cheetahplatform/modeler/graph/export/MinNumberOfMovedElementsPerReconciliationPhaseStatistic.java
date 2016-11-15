package org.cheetahplatform.modeler.graph.export;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class MinNumberOfMovedElementsPerReconciliationPhaseStatistic extends AbstractMinNumberOfTouchedElementsStatistic {
	@Override
	protected Map<Chunk, Set<Long>> filterChunks(List<Chunk> chunks) {
		Map<Chunk, Set<Long>> touchedElementsPerChunk = getTouchedElementsPerChunk(chunks, ModelingPhaseChunkExtractor.RECONCILIATION,
				AbstractGraphCommand.MOVE_NODE);
		return touchedElementsPerChunk;
	}

	@Override
	public String getHeader() {
		return "Min. # Diff. Moved Elements/Rec.";
	}

	@Override
	public String getName() {
		return "Min. Number of different moved nodes per reconcliliation phase";
	}
}

package org.cheetahplatform.modeler.graph.export;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class MinNumberOfLayoutedEdgesInReconciliationPhaseStatistic extends AbstractMinNumberOfTouchedElementsStatistic {

	@Override
	protected Map<Chunk, Set<Long>> filterChunks(List<Chunk> chunks) {
		return getTouchedElementsPerChunk(chunks, ModelingPhaseChunkExtractor.RECONCILIATION, AbstractGraphCommand.CREATE_EDGE_BENDPOINT,
				AbstractGraphCommand.DELETE_EDGE_BENDPOINT, AbstractGraphCommand.MOVE_EDGE_BENDPOINT, AbstractGraphCommand.MOVE_EDGE_LABEL);
	}

	@Override
	public String getHeader() {
		return "Min. # Diff. Layouted Edges/Rec.";
	}

	@Override
	public String getName() {
		return "Min Number of different edges layouted per reconcliliation phase";
	}

}
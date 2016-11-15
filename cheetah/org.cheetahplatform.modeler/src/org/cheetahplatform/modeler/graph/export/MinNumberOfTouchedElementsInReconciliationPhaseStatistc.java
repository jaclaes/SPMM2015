package org.cheetahplatform.modeler.graph.export;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class MinNumberOfTouchedElementsInReconciliationPhaseStatistc extends AbstractMinNumberOfTouchedElementsStatistic {
	@Override
	protected Map<Chunk, Set<Long>> filterChunks(List<Chunk> chunks) {
		return getTouchedElementsPerChunk(chunks, ModelingPhaseChunkExtractor.RECONCILIATION, AbstractGraphCommand.MOVE_NODE,
				AbstractGraphCommand.MOVE_EDGE_LABEL, AbstractGraphCommand.MOVE_EDGE_BENDPOINT, AbstractGraphCommand.CREATE_EDGE_BENDPOINT,
				AbstractGraphCommand.DELETE_EDGE_BENDPOINT);
	}

	@Override
	public String getHeader() {
		return "Min. # Diff. Touched Elements/Rec.";
	}

	@Override
	public String getName() {
		return "Min. Number of different touched elements (edges and nodes) per reconcliliation phase. Ignoring renaming";
	}

}

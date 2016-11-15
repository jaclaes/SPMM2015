package org.cheetahplatform.modeler.graph.export;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.cheetahplatform.common.logging.ProcessInstance;

public abstract class AbstractMinNumberOfTouchedElementsStatistic extends AbstractNumberOfTouchedElementsStatistic {

	public AbstractMinNumberOfTouchedElementsStatistic() {
		super();
	}

	protected abstract Map<Chunk, Set<Long>> filterChunks(List<Chunk> chunks);

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}

		Map<Chunk, Set<Long>> touchedElementsPerChunk = filterChunks(chunks);

		int smallestNumberOfMovedElements = Integer.MAX_VALUE;
		Set<Entry<Chunk, Set<Long>>> entrySet = touchedElementsPerChunk.entrySet();
		for (Entry<Chunk, Set<Long>> entry : entrySet) {
			int touchedElements = entry.getValue().size();
			if (touchedElements == 0) {
				continue;
			}

			if (touchedElements < smallestNumberOfMovedElements) {
				smallestNumberOfMovedElements = touchedElements;
			}
		}

		if (smallestNumberOfMovedElements == Integer.MAX_VALUE) {
			return N_A;
		}

		return String.valueOf(smallestNumberOfMovedElements);
	}

}
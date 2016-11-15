package org.cheetahplatform.modeler.graph.export;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.cheetahplatform.common.logging.ProcessInstance;

public abstract class AbstractMaxNumberOfTouchedElementsStatistic extends AbstractNumberOfTouchedElementsStatistic {

	protected abstract Map<Chunk, Set<Long>> filterChunks(List<Chunk> chunks);

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		if (chunks == null || chunks.isEmpty()) {
			return N_A;
		}

		Map<Chunk, Set<Long>> touchedElementsPerChunk = filterChunks(chunks);

		int highestNumerOfTouchedElements = 0;
		Set<Entry<Chunk, Set<Long>>> entrySet = touchedElementsPerChunk.entrySet();
		for (Entry<Chunk, Set<Long>> entry : entrySet) {
			int touchedElements = entry.getValue().size();
			if (touchedElements > highestNumerOfTouchedElements) {
				highestNumerOfTouchedElements = touchedElements;
			}
		}

		return String.valueOf(highestNumerOfTouchedElements);
	}
}
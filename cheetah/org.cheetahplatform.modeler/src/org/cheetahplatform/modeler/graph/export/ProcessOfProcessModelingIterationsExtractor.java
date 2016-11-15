package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProcessOfProcessModelingIterationsExtractor {

	public List<ProcessOfProcessModelingIteration> extractIterations(List<Chunk> chunks) {
		if (chunks == null || chunks.isEmpty()) {
			return Collections.emptyList();
		}

		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		for (int i = 0; i < chunks.size(); i++) {
			Chunk chunk = chunks.get(i);

			if (chunk.getType().equals(ModelingPhaseChunkExtractor.COMPREHENSION)) {
				iterations.add(new ProcessOfProcessModelingIteration(chunk));
				continue;
			}

			if (chunk.getType().equals(ModelingPhaseChunkExtractor.MODELING)) {
				if (i == 0) {
					iterations.add(new ProcessOfProcessModelingIteration(chunk));
					continue;
				}
				Chunk previousChunk = chunks.get(i - 1);
				if (previousChunk.getType().equals(ModelingPhaseChunkExtractor.RECONCILIATION)) {
					iterations.add(new ProcessOfProcessModelingIteration(chunk));
					continue;
				}
			}

			if (iterations.isEmpty()) {
				iterations.add(new ProcessOfProcessModelingIteration(chunk));
				continue;
			}

			ProcessOfProcessModelingIteration currentIteration = iterations.get(iterations.size() - 1);
			currentIteration.addChunk(chunk);
		}

		return iterations;
	}
}

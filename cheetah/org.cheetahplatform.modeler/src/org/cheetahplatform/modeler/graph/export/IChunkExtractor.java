package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;

public interface IChunkExtractor {
	List<Chunk> extractChunks(ProcessInstance modelingInstance);

	List<String> getAdditionalInformation();
}

/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.export.AverageModelingChunkSizeStatistic;
import org.cheetahplatform.modeler.graph.export.Chunk;
import org.cheetahplatform.modeler.graph.export.ModelingPhaseChunkExtractor;
import org.junit.Test;

public class AverageModelingChunkSizeStatisticTest extends LocaleSensitiveTest {

	@Test
	public void chunksIsEmpty() {
		AverageModelingChunkSizeStatistic statistic = new AverageModelingChunkSizeStatistic();
		String value = statistic.getValue(null, new ArrayList<Chunk>(), null);
		assertEquals("N/A", value);
	}

	@Test
	public void chunksIsNull() {
		AverageModelingChunkSizeStatistic statistic = new AverageModelingChunkSizeStatistic();
		String value = statistic.getValue(null, null, null);
		assertEquals("N/A", value);
	}

	private Chunk createChunk(String type, int numberOfCreateCommands) {
		Chunk modelingChunk = new Chunk(type, new Date(), 0);
		for (int i = 0; i < numberOfCreateCommands; i++) {
			modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		}
		return modelingChunk;
	}

	@Test
	public void ignoreComprehensionChunk() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(createChunk(ModelingPhaseChunkExtractor.MODELING, 5));
		chunks.add(createChunk(ModelingPhaseChunkExtractor.COMPREHENSION, 7));

		AverageModelingChunkSizeStatistic statistic = new AverageModelingChunkSizeStatistic();
		String value = statistic.getValue(null, chunks, null);
		assertEquals("5,00", value);
	}

	@Test
	public void ignoreReconciliationChunk() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(createChunk(ModelingPhaseChunkExtractor.MODELING, 9));
		chunks.add(createChunk(ModelingPhaseChunkExtractor.RECONCILIATION, 7));

		AverageModelingChunkSizeStatistic statistic = new AverageModelingChunkSizeStatistic();
		String value = statistic.getValue(null, chunks, null);
		assertEquals("9,00", value);
	}

	@Test
	public void multipleModelingChunks() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(createChunk(ModelingPhaseChunkExtractor.MODELING, 7));
		chunks.add(createChunk(ModelingPhaseChunkExtractor.MODELING, 8));

		AverageModelingChunkSizeStatistic statistic = new AverageModelingChunkSizeStatistic();
		String value = statistic.getValue(null, chunks, null);
		assertEquals("7,50", value);
	}

	@Test
	public void singleModelingChunk() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(createChunk(ModelingPhaseChunkExtractor.MODELING, 7));

		AverageModelingChunkSizeStatistic statistic = new AverageModelingChunkSizeStatistic();
		String value = statistic.getValue(null, chunks, null);
		assertEquals("7,00", value);
	}
}

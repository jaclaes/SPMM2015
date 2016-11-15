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
import org.cheetahplatform.modeler.graph.export.AvgShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic;
import org.cheetahplatform.modeler.graph.export.Chunk;
import org.cheetahplatform.modeler.graph.export.IPpmStatistic;
import org.cheetahplatform.modeler.graph.export.ModelingPhaseChunkExtractor;
import org.junit.Test;

public class AvgShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisicTest extends LocaleSensitiveTest {

	@Test
	public void considerOnlyDistinctElements() {
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		chunks.add(modelingChunk);
		Chunk reconcilationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(2000), 5);
		chunks.add(reconcilationChunk);

		createAuditTrailEntry(modelingChunk, 1l, AbstractGraphCommand.CREATE_NODE);
		createAuditTrailEntry(modelingChunk, 1l, AbstractGraphCommand.CREATE_EDGE);

		createAuditTrailEntry(reconcilationChunk, 2l, AbstractGraphCommand.MOVE_NODE);
		createAuditTrailEntry(reconcilationChunk, 2l, AbstractGraphCommand.CREATE_EDGE_BENDPOINT);
		createAuditTrailEntry(reconcilationChunk, 2l, AbstractGraphCommand.MOVE_EDGE_LABEL);
		createAuditTrailEntry(reconcilationChunk, 2l, AbstractGraphCommand.DELETE_EDGE_BENDPOINT);

		String value = new AvgShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic().getValue(null, chunks, null);

		assertEquals("1,00", value);
	}

	public void createAuditTrailEntry(Chunk modelingChunk, long id, String type) {
		AuditTrailEntry auditTrailEntry = new AuditTrailEntry(type);
		auditTrailEntry.setAttribute(AbstractGraphCommand.ID, id);
		modelingChunk.addEntry(auditTrailEntry);
	}

	@Test
	public void emptyChunks() {
		assertEquals(IPpmStatistic.N_A,
				new AvgShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic().getValue(null, new ArrayList<Chunk>(), null));
	}

	@Test
	public void moreAddingThanReconciliation() {
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		chunks.add(modelingChunk);
		Chunk reconcilationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(2000), 5);
		chunks.add(reconcilationChunk);

		createAuditTrailEntry(modelingChunk, 1l, AbstractGraphCommand.CREATE_NODE);
		createAuditTrailEntry(modelingChunk, 2l, AbstractGraphCommand.CREATE_EDGE);
		createAuditTrailEntry(modelingChunk, 4l, AbstractGraphCommand.CREATE_EDGE);
		createAuditTrailEntry(modelingChunk, 5l, AbstractGraphCommand.CREATE_EDGE);
		createAuditTrailEntry(modelingChunk, 6l, AbstractGraphCommand.CREATE_EDGE);
		createAuditTrailEntry(modelingChunk, 7l, AbstractGraphCommand.CREATE_EDGE);

		createAuditTrailEntry(reconcilationChunk, 2l, AbstractGraphCommand.MOVE_NODE);
		createAuditTrailEntry(reconcilationChunk, 3l, AbstractGraphCommand.CREATE_EDGE_BENDPOINT);

		String value = new AvgShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic().getValue(null, chunks, null);

		assertEquals("3,00", value);
	}

	@Test
	public void noChunks() {
		assertEquals(IPpmStatistic.N_A, new AvgShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic().getValue(null, null, null));
	}

	@Test
	public void reconciliationBreak() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(0), 0));
		Chunk reconcilationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(2000), 5);
		chunks.add(reconcilationChunk);

		createAuditTrailEntry(reconcilationChunk, 2l, AbstractGraphCommand.MOVE_NODE);
		createAuditTrailEntry(reconcilationChunk, 3l, AbstractGraphCommand.CREATE_EDGE_BENDPOINT);
		createAuditTrailEntry(reconcilationChunk, 4l, AbstractGraphCommand.MOVE_EDGE_LABEL);
		createAuditTrailEntry(reconcilationChunk, 5l, AbstractGraphCommand.DELETE_EDGE_BENDPOINT);

		String value = new AvgShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic().getValue(null, chunks, null);

		assertEquals("0,00", value);
	}

	@Test
	public void singleIteration() {
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		chunks.add(modelingChunk);
		Chunk reconcilationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(2000), 5);
		chunks.add(reconcilationChunk);

		createAuditTrailEntry(modelingChunk, 1l, AbstractGraphCommand.CREATE_NODE);
		createAuditTrailEntry(modelingChunk, 2l, AbstractGraphCommand.CREATE_EDGE);

		createAuditTrailEntry(reconcilationChunk, 2l, AbstractGraphCommand.MOVE_NODE);
		createAuditTrailEntry(reconcilationChunk, 3l, AbstractGraphCommand.CREATE_EDGE_BENDPOINT);
		createAuditTrailEntry(reconcilationChunk, 4l, AbstractGraphCommand.MOVE_EDGE_LABEL);
		createAuditTrailEntry(reconcilationChunk, 5l, AbstractGraphCommand.DELETE_EDGE_BENDPOINT);

		String value = new AvgShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic().getValue(null, chunks, null);

		assertEquals("0,50", value);
	}

	@Test
	public void twoIterations() {
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		chunks.add(modelingChunk);
		Chunk reconcilationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(2000), 5);
		chunks.add(reconcilationChunk);
		Chunk modelingChunk2 = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		chunks.add(modelingChunk2);
		Chunk reconcilationChunk2 = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(2000), 5);
		chunks.add(reconcilationChunk2);

		createAuditTrailEntry(modelingChunk, 1l, AbstractGraphCommand.CREATE_NODE);
		createAuditTrailEntry(modelingChunk, 2l, AbstractGraphCommand.CREATE_EDGE);

		createAuditTrailEntry(reconcilationChunk, 2l, AbstractGraphCommand.MOVE_NODE);
		createAuditTrailEntry(reconcilationChunk, 3l, AbstractGraphCommand.CREATE_EDGE_BENDPOINT);
		createAuditTrailEntry(reconcilationChunk, 4l, AbstractGraphCommand.MOVE_EDGE_LABEL);
		createAuditTrailEntry(reconcilationChunk, 5l, AbstractGraphCommand.DELETE_EDGE_BENDPOINT);

		createAuditTrailEntry(modelingChunk2, 5l, AbstractGraphCommand.CREATE_NODE);
		createAuditTrailEntry(reconcilationChunk2, 6l, AbstractGraphCommand.MOVE_EDGE_BENDPOINT);

		String value = new AvgShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic().getValue(null, chunks, null);

		assertEquals("0,75", value);
	}
}

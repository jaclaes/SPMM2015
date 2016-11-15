/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.export.Chunk;
import org.cheetahplatform.modeler.graph.export.ModelingPhaseChunkExtractor;
import org.cheetahplatform.modeler.graph.export.ProcessOfProcessModelingIteration;
import org.junit.Test;

public class ProcessOfProcessModelingIterationTest {
	private static final int RECONCILIATION_PENALTY = 1;
	private static final int MODELING_PENALTY = 2;
	private static final int COMPREHENSION_PENALTY = 3;

	@Test
	public void calculateNumberOfAddedElements() {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(new Chunk(
				ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));

		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.DELETE_EDGE));
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		iteration.addChunk(modelingChunk);

		Chunk reconciliationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(0), 0);
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.MOVE_EDGE_BENDPOINT));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.MOVE_EDGE_BENDPOINT));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_EDGE));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.MOVE_EDGE_BENDPOINT));
		iteration.addChunk(reconciliationChunk);

		assertEquals(4, iteration.numberOfAddedElements());
	}

	@Test
	public void calculateNumberOfModelingElementIncludingRenameEvents() {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(new Chunk(
				ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));

		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.MOVE_NODE));
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		iteration.addChunk(modelingChunk);

		Chunk reconciliationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(0), 0);
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.MOVE_EDGE_BENDPOINT));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.DELETE_EDGE_BENDPOINT));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.DELETE_NODE));

		AuditTrailEntry renameAuditTrailEntry = new AuditTrailEntry(AbstractGraphCommand.RENAME);
		renameAuditTrailEntry.setAttribute(AbstractGraphCommand.NAME, null);
		renameAuditTrailEntry.setAttribute(AbstractGraphCommand.NEW_NAME, "something2");
		reconciliationChunk.addEntry(renameAuditTrailEntry);

		AuditTrailEntry renameAuditTrailEntry2 = new AuditTrailEntry(AbstractGraphCommand.RENAME);
		renameAuditTrailEntry2.setAttribute(AbstractGraphCommand.NAME, "baby");
		renameAuditTrailEntry2.setAttribute(AbstractGraphCommand.NEW_NAME, "");
		reconciliationChunk.addEntry(renameAuditTrailEntry2);

		AuditTrailEntry renameAuditTrailEntry3 = new AuditTrailEntry(AbstractGraphCommand.RENAME);
		renameAuditTrailEntry3.setAttribute(AbstractGraphCommand.NAME, "baby");
		renameAuditTrailEntry3.setAttribute(AbstractGraphCommand.NEW_NAME, "yeah");
		reconciliationChunk.addEntry(renameAuditTrailEntry3);

		iteration.addChunk(reconciliationChunk);

		assertEquals(4, iteration.numberOfReconciliationElements());
		assertEquals(2, iteration.numberOfModelingRenameElements());
	}

	@Test
	public void calculateNumberOfReconciliationElements() {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(new Chunk(
				ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));

		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.MOVE_NODE));
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		iteration.addChunk(modelingChunk);

		Chunk reconciliationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(0), 0);
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.MOVE_EDGE_BENDPOINT));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.DELETE_EDGE_BENDPOINT));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.DELETE_NODE));
		AuditTrailEntry renameAuditTrailEntry = new AuditTrailEntry(AbstractGraphCommand.RENAME);
		renameAuditTrailEntry.setAttribute(AbstractGraphCommand.NAME, "something");
		renameAuditTrailEntry.setAttribute(AbstractGraphCommand.NEW_NAME, "something2");
		reconciliationChunk.addEntry(renameAuditTrailEntry);
		iteration.addChunk(reconciliationChunk);

		assertEquals(4, iteration.numberOfReconciliationElements());
	}

	@Test
	public void calculateNumberOfReconnectElements() {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(new Chunk(
				ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));

		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.RECONNECT_EDGE));
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.RECONNECT_EDGE));
		iteration.addChunk(modelingChunk);

		Chunk reconciliationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(0), 0);
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.MOVE_EDGE_BENDPOINT));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.DELETE_EDGE_BENDPOINT));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.RECONNECT_EDGE));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.RENAME));
		iteration.addChunk(reconciliationChunk);

		assertEquals(3, iteration.numberOfReconnectElements());
	}

	@Test
	public void calculateNumberOfRemovedElements() {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(new Chunk(
				ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));

		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.DELETE_EDGE));
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		iteration.addChunk(modelingChunk);

		Chunk reconciliationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(0), 0);
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.MOVE_EDGE_BENDPOINT));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.MOVE_EDGE_BENDPOINT));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.DELETE_NODE));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.MOVE_EDGE_BENDPOINT));
		iteration.addChunk(reconciliationChunk);

		assertEquals(2, iteration.numberOfRemovedElements());
	}

	@Test
	public void calculatePenaltyMissingComprehension() {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(new Chunk(ModelingPhaseChunkExtractor.MODELING,
				new Date(), 0));
		iteration.addChunk(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));

		assertEquals(COMPREHENSION_PENALTY, iteration.calcualtePenalty(COMPREHENSION_PENALTY, MODELING_PENALTY, RECONCILIATION_PENALTY),
				0.0);
	}

	@Test
	public void calculatePenaltyMissingComprehensionAndReconciliation() {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(new Chunk(ModelingPhaseChunkExtractor.MODELING,
				new Date(), 0));

		assertEquals(COMPREHENSION_PENALTY + RECONCILIATION_PENALTY,
				iteration.calcualtePenalty(COMPREHENSION_PENALTY, MODELING_PENALTY, RECONCILIATION_PENALTY), 0.0);
	}

	@Test
	public void calculatePenaltyMissingModeling() {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(new Chunk(
				ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));
		iteration.addChunk(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));

		assertEquals(MODELING_PENALTY, iteration.calcualtePenalty(COMPREHENSION_PENALTY, MODELING_PENALTY, RECONCILIATION_PENALTY), 0.0);
	}

	@Test
	public void calculatePenaltyMissingModelingAndReconciliation() {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(new Chunk(
				ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));

		assertEquals(MODELING_PENALTY + RECONCILIATION_PENALTY,
				iteration.calcualtePenalty(COMPREHENSION_PENALTY, MODELING_PENALTY, RECONCILIATION_PENALTY), 0.0);
	}

	@Test
	public void calculatePenaltyMissingReconciliation() {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(new Chunk(
				ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));
		iteration.addChunk(new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0));

		assertEquals(RECONCILIATION_PENALTY, iteration.calcualtePenalty(COMPREHENSION_PENALTY, MODELING_PENALTY, RECONCILIATION_PENALTY),
				0.0);
	}

	@Test
	public void calculateTotalDuration() {
		Chunk chunk1 = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(0), 0);
		chunk1.setEndTime(new Date(1000));
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(chunk1);
		Chunk chunk2 = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(1500), 0);
		chunk2.setEndTime(new Date(3000));
		iteration.addChunk(chunk2);
		assertEquals(3000, iteration.getDuration());
	}

	@Test
	public void containsEntry() {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(new Chunk(
				ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));

		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		AuditTrailEntry entry1 = new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE);
		modelingChunk.addEntry(entry1);
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.DELETE_EDGE));
		modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		iteration.addChunk(modelingChunk);

		Chunk reconciliationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(0), 0);
		AuditTrailEntry entry2 = new AuditTrailEntry(AbstractGraphCommand.MOVE_EDGE_BENDPOINT);
		reconciliationChunk.addEntry(entry2);
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.MOVE_EDGE_BENDPOINT));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.DELETE_NODE));
		reconciliationChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.MOVE_EDGE_BENDPOINT));
		iteration.addChunk(reconciliationChunk);

		assertTrue(iteration.containsEntry(entry1));
		assertTrue(iteration.containsEntry(entry2));
		assertFalse(iteration.containsEntry(new AuditTrailEntry(AbstractGraphCommand.DELETE_EDGE)));
	}
}

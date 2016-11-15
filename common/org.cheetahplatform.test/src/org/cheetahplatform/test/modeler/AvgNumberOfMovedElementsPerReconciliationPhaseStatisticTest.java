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
import org.cheetahplatform.modeler.graph.export.AvgNumberOfMovedElementsPerReconciliationPhaseStatistic;
import org.cheetahplatform.modeler.graph.export.Chunk;
import org.cheetahplatform.modeler.graph.export.IPpmStatistic;
import org.cheetahplatform.modeler.graph.export.ModelingPhaseChunkExtractor;
import org.junit.Test;

public class AvgNumberOfMovedElementsPerReconciliationPhaseStatisticTest extends LocaleSensitiveTest {
	@Test
	public void emptyChunks() {
		assertEquals(IPpmStatistic.N_A,
				new AvgNumberOfMovedElementsPerReconciliationPhaseStatistic().getValue(null, new ArrayList<Chunk>(), null));
	}

	@Test
	public void nullChunks() {
		assertEquals(IPpmStatistic.N_A, new AvgNumberOfMovedElementsPerReconciliationPhaseStatistic().getValue(null, null, null));
	}

	@Test
	public void severalMoveCommandsOnSameElement() {
		List<Chunk> chunks = new ArrayList<Chunk>();

		Chunk chunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(0), 0);
		chunks.add(chunk);

		AuditTrailEntry entry = new AuditTrailEntry(AbstractGraphCommand.MOVE_NODE);
		entry.setAttribute(AbstractGraphCommand.ID, 1l);
		chunk.addEntry(entry);
		AuditTrailEntry entry2 = new AuditTrailEntry(AbstractGraphCommand.MOVE_NODE);
		entry2.setAttribute(AbstractGraphCommand.ID, 1l);
		chunk.addEntry(entry2);

		Chunk chunk2 = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(1000), 0);
		chunks.add(chunk2);

		String value = new AvgNumberOfMovedElementsPerReconciliationPhaseStatistic().getValue(null, chunks, null);
		assertEquals("0,50", value);
	}

	@Test
	public void singlePhaseWithOneMoveIgnoringOtherEvents() {
		List<Chunk> chunks = new ArrayList<Chunk>();

		Chunk chunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(0), 0);
		chunks.add(chunk);

		AuditTrailEntry entry = new AuditTrailEntry(AbstractGraphCommand.MOVE_NODE);
		entry.setAttribute(AbstractGraphCommand.ID, 1l);
		chunk.addEntry(entry);
		AuditTrailEntry entry2 = new AuditTrailEntry(AbstractGraphCommand.DELETE_EDGE);
		entry2.setAttribute(AbstractGraphCommand.ID, 2l);
		chunk.addEntry(entry2);

		assertEquals(2, chunk.getSize());
		String value = new AvgNumberOfMovedElementsPerReconciliationPhaseStatistic().getValue(null, chunks, null);
		assertEquals("1,00", value);
	}

	@Test
	public void twoPhasesWithOneMoveIgnoringOtherEvents() {
		List<Chunk> chunks = new ArrayList<Chunk>();

		Chunk chunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(0), 0);
		chunks.add(chunk);

		AuditTrailEntry entry = new AuditTrailEntry(AbstractGraphCommand.MOVE_NODE);
		entry.setAttribute(AbstractGraphCommand.ID, 1l);
		chunk.addEntry(entry);
		AuditTrailEntry entry2 = new AuditTrailEntry(AbstractGraphCommand.DELETE_EDGE);
		entry2.setAttribute(AbstractGraphCommand.ID, 2l);
		chunk.addEntry(entry2);

		Chunk chunk2 = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(1000), 0);
		chunks.add(chunk2);

		String value = new AvgNumberOfMovedElementsPerReconciliationPhaseStatistic().getValue(null, chunks, null);
		assertEquals("0,50", value);
	}
}

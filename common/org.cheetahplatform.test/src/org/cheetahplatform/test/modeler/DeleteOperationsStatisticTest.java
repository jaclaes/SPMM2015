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
import org.cheetahplatform.modeler.graph.export.Chunk;
import org.cheetahplatform.modeler.graph.export.DeleteIterationsStatistic;
import org.cheetahplatform.modeler.graph.export.ModelingPhaseChunkExtractor;
import org.cheetahplatform.modeler.graph.export.ProcessOfProcessModelingIteration;
import org.junit.Test;

public class DeleteOperationsStatisticTest extends LocaleSensitiveTest {
	private Chunk createChunk(String type) {
		return new Chunk(type, new Date(), 0);
	}

	private Chunk createChunk(String type, int numberOfCreateCommands, int numberOfDeleteCommands) {
		Chunk modelingChunk = new Chunk(type, new Date(), 0);
		for (int i = 0; i < numberOfCreateCommands; i++) {
			modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		}
		for (int i = 0; i < numberOfDeleteCommands; i++) {
			modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.DELETE_NODE));
		}
		return modelingChunk;
	}

	protected void createDeleteIteration(List<ProcessOfProcessModelingIteration> iterations) {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(
				createChunk(ModelingPhaseChunkExtractor.COMPREHENSION));
		iteration.addChunk(createChunk(ModelingPhaseChunkExtractor.MODELING, 4, 3));
		iteration.addChunk(createChunk(ModelingPhaseChunkExtractor.RECONCILIATION));
		iterations.add(iteration);
	}

	protected void createNonDeleteIteration(List<ProcessOfProcessModelingIteration> iterations) {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(
				createChunk(ModelingPhaseChunkExtractor.COMPREHENSION));
		iteration.addChunk(createChunk(ModelingPhaseChunkExtractor.MODELING, 4, 0));
		iteration.addChunk(createChunk(ModelingPhaseChunkExtractor.RECONCILIATION));
		iterations.add(iteration);
	}

	@Test
	public void iterationsIsEmpty() {
		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		DeleteIterationsStatistic statistic = new DeleteIterationsStatistic();
		String value = statistic.getValue(null, null, iterations);
		assertEquals("N/A", value);
	}

	@Test
	public void iterationsIsNull() {
		DeleteIterationsStatistic statistic = new DeleteIterationsStatistic();
		String value = statistic.getValue(null, null, null);
		assertEquals("N/A", value);
	}

	@Test
	public void multipleIterationsWithDeleteOperations() {
		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		createDeleteIteration(iterations);
		createDeleteIteration(iterations);
		createNonDeleteIteration(iterations);

		DeleteIterationsStatistic deleteOperationsStatistic = new DeleteIterationsStatistic();
		String value = deleteOperationsStatistic.getValue(null, null, iterations);
		assertEquals("66,67", value);
	}

	@Test
	public void noDeleteOperations() {
		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		createNonDeleteIteration(iterations);

		DeleteIterationsStatistic deleteOperationsStatistic = new DeleteIterationsStatistic();
		String value = deleteOperationsStatistic.getValue(null, null, iterations);
		assertEquals("0,00", value);
	}

	@Test
	public void singleIterationWithDeleteOperation() {
		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(
				createChunk(ModelingPhaseChunkExtractor.COMPREHENSION));
		iteration.addChunk(createChunk(ModelingPhaseChunkExtractor.MODELING, 4, 1));
		iteration.addChunk(createChunk(ModelingPhaseChunkExtractor.RECONCILIATION));
		iterations.add(iteration);

		DeleteIterationsStatistic deleteOperationsStatistic = new DeleteIterationsStatistic();
		String value = deleteOperationsStatistic.getValue(null, null, iterations);
		assertEquals("100,00", value);
	}

}

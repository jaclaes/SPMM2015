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
import org.cheetahplatform.modeler.graph.export.AverageIterationChunkSizeStatistic;
import org.cheetahplatform.modeler.graph.export.Chunk;
import org.cheetahplatform.modeler.graph.export.ModelingPhaseChunkExtractor;
import org.cheetahplatform.modeler.graph.export.ProcessOfProcessModelingIteration;
import org.junit.Test;

public class AverageChunkSizeStatisticTest extends LocaleSensitiveTest {

	private Chunk createChunk(String type, int numberOfCreateCommands) {
		Chunk modelingChunk = new Chunk(type, new Date(), 0);
		for (int i = 0; i < numberOfCreateCommands; i++) {
			modelingChunk.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		}
		return modelingChunk;
	}

	@Test
	public void iterationsIsEmpty() {
		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		AverageIterationChunkSizeStatistic statistic = new AverageIterationChunkSizeStatistic();
		String value = statistic.getValue(null, null, iterations);
		assertEquals("N/A", value);
	}

	@Test
	public void iterationsIsNull() {
		AverageIterationChunkSizeStatistic statistic = new AverageIterationChunkSizeStatistic();
		String value = statistic.getValue(null, null, null);
		assertEquals("N/A", value);
	}

	@Test
	public void renameAsModelingStep() {
		Chunk comprehensionChunk = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0);
		Chunk modelingChunk = createChunk(ModelingPhaseChunkExtractor.MODELING, 3);
		AuditTrailEntry renameAudiTrailEntry = new AuditTrailEntry(AbstractGraphCommand.RENAME);
		renameAudiTrailEntry.setAttribute(AbstractGraphCommand.NAME, null);
		modelingChunk.addEntry(renameAudiTrailEntry);

		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(comprehensionChunk);
		iteration.addChunk(comprehensionChunk);
		iteration.addChunk(modelingChunk);

		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		iterations.add(iteration);
		AverageIterationChunkSizeStatistic statistic = new AverageIterationChunkSizeStatistic();
		String value = statistic.getValue(null, null, iterations);
		assertEquals("4,00", value);
	}

	@Test
	public void singleCMIteration() {
		Chunk comprehensionChunk = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0);
		Chunk modelingChunk = createChunk(ModelingPhaseChunkExtractor.MODELING, 3);

		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(comprehensionChunk);
		iteration.addChunk(comprehensionChunk);
		iteration.addChunk(modelingChunk);

		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		iterations.add(iteration);
		AverageIterationChunkSizeStatistic statistic = new AverageIterationChunkSizeStatistic();
		String value = statistic.getValue(null, null, iterations);
		assertEquals("3,00", value);
	}

	@Test
	public void singleCMRIteration() {
		Chunk comprehensionChunk = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0);
		Chunk modelingChunk = createChunk(ModelingPhaseChunkExtractor.MODELING, 3);
		Chunk reconciliationChunk = createChunk(ModelingPhaseChunkExtractor.RECONCILIATION, 2);
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(comprehensionChunk);
		iteration.addChunk(comprehensionChunk);
		iteration.addChunk(modelingChunk);
		iteration.addChunk(reconciliationChunk);

		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		iterations.add(iteration);
		AverageIterationChunkSizeStatistic statistic = new AverageIterationChunkSizeStatistic();
		String value = statistic.getValue(null, null, iterations);
		assertEquals("5,00", value);
	}

	@Test
	public void twoCMIteration() {
		Chunk comprehensionChunk = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0);
		Chunk modelingChunk = createChunk(ModelingPhaseChunkExtractor.MODELING, 5);
		ProcessOfProcessModelingIteration iteration1 = new ProcessOfProcessModelingIteration(comprehensionChunk);
		iteration1.addChunk(comprehensionChunk);
		iteration1.addChunk(modelingChunk);

		Chunk comprehensionChunk2 = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0);
		Chunk modelingChunk2 = createChunk(ModelingPhaseChunkExtractor.MODELING, 2);
		ProcessOfProcessModelingIteration iteration2 = new ProcessOfProcessModelingIteration(comprehensionChunk);
		iteration2.addChunk(comprehensionChunk2);
		iteration2.addChunk(modelingChunk2);

		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		iterations.add(iteration1);
		iterations.add(iteration2);
		AverageIterationChunkSizeStatistic statistic = new AverageIterationChunkSizeStatistic();
		String value = statistic.getValue(null, null, iterations);
		assertEquals("3,50", value);
	}
}

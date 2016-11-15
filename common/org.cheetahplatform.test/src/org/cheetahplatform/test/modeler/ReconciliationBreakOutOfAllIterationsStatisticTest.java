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

import org.cheetahplatform.modeler.graph.export.Chunk;
import org.cheetahplatform.modeler.graph.export.ModelingPhaseChunkExtractor;
import org.cheetahplatform.modeler.graph.export.ProcessOfProcessModelingIteration;
import org.cheetahplatform.modeler.graph.export.ReconciliationBreakOutOfAllIterationsStatistic;
import org.junit.Test;

public class ReconciliationBreakOutOfAllIterationsStatisticTest extends LocaleSensitiveTest {

	@Test
	public void CRandCMandMIteration() {
		Chunk comprehensionChunk = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0);
		Chunk reconciliationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0);
		ProcessOfProcessModelingIteration iteration1 = new ProcessOfProcessModelingIteration(comprehensionChunk);
		iteration1.addChunk(reconciliationChunk);
		Chunk comprehensionChunk2 = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0);
		Chunk modelingChunk2 = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0);
		ProcessOfProcessModelingIteration iteration0 = new ProcessOfProcessModelingIteration(comprehensionChunk2);
		iteration0.addChunk(modelingChunk2);

		Chunk modelingChunk3 = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0);
		ProcessOfProcessModelingIteration iteration2 = new ProcessOfProcessModelingIteration(comprehensionChunk2);
		iteration2.addChunk(modelingChunk3);

		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		iterations.add(iteration0);
		iterations.add(iteration1);
		iterations.add(iteration2);

		ReconciliationBreakOutOfAllIterationsStatistic statistic = new ReconciliationBreakOutOfAllIterationsStatistic();
		String value = statistic.getValue(null, null, iterations);
		assertEquals("33,33", value);
	}

	@Test
	public void CRandCMIteration() {
		Chunk comprehensionChunk = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0);
		Chunk reconciliationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0);
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(comprehensionChunk);
		iteration.addChunk(reconciliationChunk);
		Chunk comprehensionChunk2 = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0);
		Chunk modelingChunk2 = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0);
		ProcessOfProcessModelingIteration iteration2 = new ProcessOfProcessModelingIteration(comprehensionChunk2);
		iteration2.addChunk(modelingChunk2);

		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		iterations.add(iteration);
		iterations.add(iteration2);

		ReconciliationBreakOutOfAllIterationsStatistic statistic = new ReconciliationBreakOutOfAllIterationsStatistic();
		String value = statistic.getValue(null, null, iterations);
		assertEquals("50,00", value);
	}

	@Test
	public void iterationsIsEmpty() {
		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		ReconciliationBreakOutOfAllIterationsStatistic statistic = new ReconciliationBreakOutOfAllIterationsStatistic();
		String value = statistic.getValue(null, null, iterations);
		assertEquals("N/A", value);
	}

	@Test
	public void iterationsIsNull() {
		ReconciliationBreakOutOfAllIterationsStatistic statistic = new ReconciliationBreakOutOfAllIterationsStatistic();
		String value = statistic.getValue(null, null, null);
		assertEquals("N/A", value);
	}

	@Test
	public void simpleCMRIteration() {
		Chunk comprehensionChunk = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0);
		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0);
		Chunk reconciliationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0);
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(comprehensionChunk);
		iteration.addChunk(modelingChunk);
		iteration.addChunk(reconciliationChunk);

		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		iterations.add(iteration);

		ReconciliationBreakOutOfAllIterationsStatistic statistic = new ReconciliationBreakOutOfAllIterationsStatistic();
		String value = statistic.getValue(null, null, iterations);
		assertEquals("0,00", value);
	}

	@Test
	public void singleCRIteration() {
		Chunk comprehensionChunk = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0);
		Chunk reconciliationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0);
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(comprehensionChunk);
		iteration.addChunk(reconciliationChunk);

		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		iterations.add(iteration);

		ReconciliationBreakOutOfAllIterationsStatistic statistic = new ReconciliationBreakOutOfAllIterationsStatistic();
		String value = statistic.getValue(null, null, iterations);
		assertEquals("100,00", value);
	}
}

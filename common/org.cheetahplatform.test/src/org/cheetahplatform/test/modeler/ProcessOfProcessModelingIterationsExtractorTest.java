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
import org.cheetahplatform.modeler.graph.export.ProcessOfProcessModelingIterationsExtractor;
import org.junit.Test;

public class ProcessOfProcessModelingIterationsExtractorTest {
	@Test
	public void firstStepIsReconciliation() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));

		List<ProcessOfProcessModelingIteration> iterations = new ProcessOfProcessModelingIterationsExtractor().extractIterations(chunks);
		assertEquals(2, iterations.size());
		ProcessOfProcessModelingIteration iteration = iterations.get(0);
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, iteration.getChunks().get(0).getType());
		iteration = iterations.get(1);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, iteration.getChunks().get(0).getType());
		assertEquals(ModelingPhaseChunkExtractor.MODELING, iteration.getChunks().get(1).getType());
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, iteration.getChunks().get(2).getType());
	}

	@Test
	public void missingComprehensionPhase() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));

		List<ProcessOfProcessModelingIteration> iterations = new ProcessOfProcessModelingIterationsExtractor().extractIterations(chunks);
		assertEquals(2, iterations.size());
		ProcessOfProcessModelingIteration iteration = iterations.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, iteration.getChunks().get(0).getType());
		assertEquals(ModelingPhaseChunkExtractor.MODELING, iteration.getChunks().get(1).getType());
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, iteration.getChunks().get(2).getType());
		iteration = iterations.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, iteration.getChunks().get(0).getType());
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, iteration.getChunks().get(1).getType());
	}

	@Test
	public void missingFirstComprehensionPhase() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));

		List<ProcessOfProcessModelingIteration> iterations = new ProcessOfProcessModelingIterationsExtractor().extractIterations(chunks);
		assertEquals(2, iterations.size());
		ProcessOfProcessModelingIteration iteration = iterations.get(0);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, iteration.getChunks().get(0).getType());
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, iteration.getChunks().get(1).getType());
		iteration = iterations.get(1);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, iteration.getChunks().get(0).getType());
		assertEquals(ModelingPhaseChunkExtractor.MODELING, iteration.getChunks().get(1).getType());
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, iteration.getChunks().get(2).getType());
	}

	@Test
	public void missingModelingPhase() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));

		List<ProcessOfProcessModelingIteration> iterations = new ProcessOfProcessModelingIterationsExtractor().extractIterations(chunks);
		assertEquals(2, iterations.size());
		ProcessOfProcessModelingIteration iteration = iterations.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, iteration.getChunks().get(0).getType());
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, iteration.getChunks().get(1).getType());
		iteration = iterations.get(1);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, iteration.getChunks().get(0).getType());
		assertEquals(ModelingPhaseChunkExtractor.MODELING, iteration.getChunks().get(1).getType());
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, iteration.getChunks().get(2).getType());
	}

	@Test
	public void missingReconciliationPhase() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));

		List<ProcessOfProcessModelingIteration> iterations = new ProcessOfProcessModelingIterationsExtractor().extractIterations(chunks);
		assertEquals(2, iterations.size());
		ProcessOfProcessModelingIteration iteration = iterations.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, iteration.getChunks().get(0).getType());
		assertEquals(ModelingPhaseChunkExtractor.MODELING, iteration.getChunks().get(1).getType());
		iteration = iterations.get(1);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, iteration.getChunks().get(0).getType());
		assertEquals(ModelingPhaseChunkExtractor.MODELING, iteration.getChunks().get(1).getType());
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, iteration.getChunks().get(2).getType());
	}

	@Test
	public void severalMissingComprehesionPhases() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0));

		List<ProcessOfProcessModelingIteration> iterations = new ProcessOfProcessModelingIterationsExtractor().extractIterations(chunks);
		assertEquals(2, iterations.size());
		ProcessOfProcessModelingIteration iteration = iterations.get(0);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, iteration.getChunks().get(0).getType());
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, iteration.getChunks().get(1).getType());
		iteration = iterations.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, iteration.getChunks().get(0).getType());
	}

	@Test
	public void simpleIteration() {
		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(), 0));
		chunks.add(new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(), 0));

		List<ProcessOfProcessModelingIteration> iterations = new ProcessOfProcessModelingIterationsExtractor().extractIterations(chunks);
		assertEquals(2, iterations.size());
		ProcessOfProcessModelingIteration iteration = iterations.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, iteration.getChunks().get(0).getType());
		assertEquals(ModelingPhaseChunkExtractor.MODELING, iteration.getChunks().get(1).getType());
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, iteration.getChunks().get(2).getType());
		iteration = iterations.get(1);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, iteration.getChunks().get(0).getType());
		assertEquals(ModelingPhaseChunkExtractor.MODELING, iteration.getChunks().get(1).getType());
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, iteration.getChunks().get(2).getType());
	}
}

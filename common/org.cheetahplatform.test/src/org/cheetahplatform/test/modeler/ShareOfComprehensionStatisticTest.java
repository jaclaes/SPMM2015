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
import org.cheetahplatform.modeler.graph.export.ShareOfComprehensionStatistic;
import org.junit.Test;

public class ShareOfComprehensionStatisticTest extends LocaleSensitiveTest {
	@Test
	public void chunksIsEmpty() {
		ShareOfComprehensionStatistic statistic = new ShareOfComprehensionStatistic();
		String value = statistic.getValue(null, new ArrayList<Chunk>(), null);
		assertEquals("N/A", value);
	}

	@Test
	public void chunksIsNull() {
		ShareOfComprehensionStatistic statistic = new ShareOfComprehensionStatistic();
		String value = statistic.getValue(null, null, null);
		assertEquals("N/A", value);
	}

	@Test
	public void ignoreFirstComprehensionChunk() {
		Chunk comprehensionChunk1 = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(1000), 0);
		comprehensionChunk1.setEndTime(new Date(20000));
		Chunk comprehensionChunk2 = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(21000), 0);
		comprehensionChunk2.setEndTime(new Date(25000));
		Chunk modelingChunk1 = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(27000), 0);
		modelingChunk1.setEndTime(new Date(35000));
		Chunk reconciliationChunk1 = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(36000), 0);
		reconciliationChunk1.setEndTime(new Date(40000));

		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(comprehensionChunk1);
		chunks.add(comprehensionChunk2);
		chunks.add(modelingChunk1);
		chunks.add(reconciliationChunk1);
		ShareOfComprehensionStatistic statistic = new ShareOfComprehensionStatistic();
		String value = statistic.getValue(null, chunks, null);
		assertEquals("25,00", value);
	}

	@Test
	public void multipleComprehensionAndModelingPhasesOfDifferentLength() {
		Chunk modelingChunk1 = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		modelingChunk1.setEndTime(new Date(1000));
		Chunk modelingChunk2 = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(1000), 0);
		modelingChunk2.setEndTime(new Date(4000));
		Chunk comprehensionChunk1 = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(5000), 0);
		comprehensionChunk1.setEndTime(new Date(7000));
		Chunk comprehensionChunk2 = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(7000), 0);
		comprehensionChunk2.setEndTime(new Date(8000));
		Chunk reconciliationChunk1 = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(8000), 0);
		reconciliationChunk1.setEndTime(new Date(9000));

		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(modelingChunk1);
		chunks.add(modelingChunk2);
		chunks.add(comprehensionChunk1);
		chunks.add(comprehensionChunk2);
		chunks.add(reconciliationChunk1);

		ShareOfComprehensionStatistic statistic = new ShareOfComprehensionStatistic();
		String value = statistic.getValue(null, chunks, null);
		assertEquals("33,33", value);
	}

	@Test
	public void multipleComprehensionPhasesOfDifferentLength() {
		Chunk modelingChunk1 = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		modelingChunk1.setEndTime(new Date(1000));
		Chunk comprehensionChunk1 = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(1000), 0);
		comprehensionChunk1.setEndTime(new Date(3000));
		Chunk comprehensionChunk2 = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(3000), 0);
		comprehensionChunk2.setEndTime(new Date(4000));
		Chunk reconciliationChunk1 = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(5000), 0);
		reconciliationChunk1.setEndTime(new Date(6000));

		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(modelingChunk1);
		chunks.add(comprehensionChunk1);
		chunks.add(comprehensionChunk2);
		chunks.add(reconciliationChunk1);

		ShareOfComprehensionStatistic statistic = new ShareOfComprehensionStatistic();
		String value = statistic.getValue(null, chunks, null);
		assertEquals("42,86", value);
	}

	@Test
	public void multipleComprehensionPhasesOfSameLength() {
		Chunk modelingChunk1 = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		modelingChunk1.setEndTime(new Date(1000));
		Chunk comprehensionChunk1 = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(1000), 0);
		comprehensionChunk1.setEndTime(new Date(2000));
		Chunk comprehensionChunk2 = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(3000), 0);
		comprehensionChunk2.setEndTime(new Date(4000));
		Chunk reconciliationChunk1 = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(5000), 0);
		reconciliationChunk1.setEndTime(new Date(6000));

		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(modelingChunk1);
		chunks.add(comprehensionChunk1);
		chunks.add(comprehensionChunk1);
		chunks.add(reconciliationChunk1);

		ShareOfComprehensionStatistic statistic = new ShareOfComprehensionStatistic();
		String value = statistic.getValue(null, chunks, null);
		assertEquals("33,33", value);
	}

	@Test
	public void noComprehensionPhase() {
		Chunk modelingChunk0 = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		modelingChunk0.setEndTime(new Date(10000));
		Chunk reconciliationChunk1 = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(38000), 0);
		reconciliationChunk1.setEndTime(new Date(48000));

		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(modelingChunk0);
		chunks.add(reconciliationChunk1);
		ShareOfComprehensionStatistic statistic = new ShareOfComprehensionStatistic();
		String value = statistic.getValue(null, chunks, null);
		assertEquals("0,00", value);
	}

	@Test
	public void noIntitalComprehension() {
		Chunk modelingChunk0 = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		modelingChunk0.setEndTime(new Date(10000));
		Chunk comprehensionChunk1 = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(10000), 0);
		comprehensionChunk1.setEndTime(new Date(30000));
		Chunk reconciliationChunk1 = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(38000), 0);
		reconciliationChunk1.setEndTime(new Date(48000));

		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(modelingChunk0);
		chunks.add(comprehensionChunk1);
		chunks.add(reconciliationChunk1);
		ShareOfComprehensionStatistic statistic = new ShareOfComprehensionStatistic();
		String value = statistic.getValue(null, chunks, null);
		assertEquals("50,00", value);
	}

	@Test
	public void noModelingPhase() {
		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(0), 0);
		modelingChunk.setEndTime(new Date(10000));
		Chunk comprehensionChunk = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(10000), 0);
		comprehensionChunk.setEndTime(new Date(30000));

		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(modelingChunk);
		chunks.add(comprehensionChunk);
		ShareOfComprehensionStatistic statistic = new ShareOfComprehensionStatistic();
		String value = statistic.getValue(null, chunks, null);
		assertEquals("66,67", value);
	}

	@Test
	public void noReconciliationPhase() {
		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		modelingChunk.setEndTime(new Date(10000));
		Chunk comprehensionChunk = new Chunk(ModelingPhaseChunkExtractor.COMPREHENSION, new Date(10000), 0);
		comprehensionChunk.setEndTime(new Date(30000));

		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(modelingChunk);
		chunks.add(comprehensionChunk);
		ShareOfComprehensionStatistic statistic = new ShareOfComprehensionStatistic();
		String value = statistic.getValue(null, chunks, null);
		assertEquals("66,67", value);
	}
}

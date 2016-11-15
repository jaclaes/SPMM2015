/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.graph.IBMLayouter;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.export.Chunk;
import org.cheetahplatform.modeler.graph.export.DurationModelingPhaseDetectionStrategy;
import org.cheetahplatform.modeler.graph.export.LookAheadModelingPhaseDetectionStrategy;
import org.cheetahplatform.modeler.graph.export.ModelingPhaseChunkExtractor;
import org.junit.Test;

public class ModelingPhaseChunkExtractorTest {
	@Test
	public void checkAuditTrailEntries() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 5000),
				3500, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		AuditTrailEntry createNode1 = new AuditTrailEntry(new Date(4000), AbstractGraphCommand.CREATE_NODE, null);
		processInstance.addEntry(createNode1);
		AuditTrailEntry moveNode1 = new AuditTrailEntry(new Date(5000), AbstractGraphCommand.MOVE_NODE, null);
		processInstance.addEntry(moveNode1);
		AuditTrailEntry moveNode2 = new AuditTrailEntry(new Date(6500), AbstractGraphCommand.MOVE_NODE, null);
		processInstance.addEntry(moveNode2);
		AuditTrailEntry createEdge1 = new AuditTrailEntry(new Date(17000), AbstractGraphCommand.CREATE_EDGE, null);
		processInstance.addEntry(createEdge1);
		AuditTrailEntry moveNode3 = new AuditTrailEntry(new Date(17500), AbstractGraphCommand.MOVE_NODE, null);
		processInstance.addEntry(moveNode3);
		AuditTrailEntry createNode2 = new AuditTrailEntry(new Date(18000), AbstractGraphCommand.CREATE_NODE, null);
		processInstance.addEntry(createNode2);
		AuditTrailEntry createNode3 = new AuditTrailEntry(new Date(19000), AbstractGraphCommand.CREATE_NODE, null);
		processInstance.addEntry(createNode3);

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(5, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertTrue(comprehensionChunk.getEntries().isEmpty());

		Chunk modelingChunk = chunks.get(1);
		assertEquals(1, modelingChunk.getEntries().size());
		assertSame(createNode1, modelingChunk.getEntries().get(0));

		Chunk reconciliationChunk = chunks.get(2);
		assertEquals(2, reconciliationChunk.getEntries().size());
		assertSame(moveNode1, reconciliationChunk.getEntries().get(0));
		assertSame(moveNode2, reconciliationChunk.getEntries().get(1));

		Chunk comprehensionChunk2 = chunks.get(3);
		assertTrue(comprehensionChunk2.getEntries().isEmpty());

		Chunk modelingChunk2 = chunks.get(4);
		assertEquals(4, modelingChunk2.getEntries().size());
		assertSame(createEdge1, modelingChunk2.getEntries().get(0));
		assertSame(moveNode3, modelingChunk2.getEntries().get(1));
		assertSame(createNode2, modelingChunk2.getEntries().get(2));
		assertSame(createNode3, modelingChunk2.getEntries().get(3));
	}

	@Test
	public void checkLookAheadAtEndOfLog() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new LookAheadModelingPhaseDetectionStrategy(1), 1500, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(2000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2500), AbstractGraphCommand.CREATE_EDGE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3000), AbstractGraphCommand.MOVE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(3, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());

		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(2, modelingChunk.getSize());
		assertEquals(0, modelingChunk.getFromStepIndex());
		assertEquals(1, modelingChunk.getToStepIndex());

		Chunk reconciliationChunk = chunks.get(2);
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, reconciliationChunk.getType());
		assertEquals(1, reconciliationChunk.getSize());
		assertEquals(2, reconciliationChunk.getFromStepIndex());
		assertEquals(2, reconciliationChunk.getToStepIndex());
	}

	@Test
	public void checkSizeOfChunk() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new LookAheadModelingPhaseDetectionStrategy(0), 1500, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(2000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2500), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3000), AbstractGraphCommand.CREATE_EDGE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(2, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());

		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(3, modelingChunk.getSize());
		assertEquals(0, modelingChunk.getFromStepIndex());
		assertEquals(2, modelingChunk.getToStepIndex());
	}

	@Test
	public void checkSizeOfChunkWithScrollEvents() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new LookAheadModelingPhaseDetectionStrategy(0), 1500, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(2000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2001), AbstractGraphCommand.VSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2002), AbstractGraphCommand.VSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2003), AbstractGraphCommand.VSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2004), AbstractGraphCommand.VSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2005), AbstractGraphCommand.VSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2500), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2501), AbstractGraphCommand.HSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3000), AbstractGraphCommand.CREATE_EDGE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(2, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());

		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(3, modelingChunk.getSize());
		assertEquals(0, modelingChunk.getFromStepIndex());
		assertEquals(8, modelingChunk.getToStepIndex());
	}

	@Test
	public void defaultRenameDuration() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 100000),
				5000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		AuditTrailEntry entry = new AuditTrailEntry(new Date(10000), AbstractGraphCommand.RENAME, null);
		entry.setAttribute(AbstractGraphCommand.NAME, "name");
		entry.setAttribute(AbstractGraphCommand.NEW_NAME, "namesadf");
		processInstance.addEntry(entry);

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(2, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());
		assertEquals(0, comprehensionChunk.getStartTime().getTime());
		assertEquals(10000 - ModelingPhaseChunkExtractor.DEFAULT_RENAME_DURATION, comprehensionChunk.getEndTime().getTime());

		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, modelingChunk.getType());
		assertEquals(10000 - ModelingPhaseChunkExtractor.DEFAULT_RENAME_DURATION, modelingChunk.getStartTime().getTime());
		assertEquals(10000, modelingChunk.getEndTime().getTime());
		assertEquals(1, modelingChunk.getSize());
	}

	@Test
	public void identifyMultipleComprehensionPhases() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 100000),
				5000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(7000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(15000), AbstractGraphCommand.CREATE_EDGE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(16000), AbstractGraphCommand.CREATE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(4, chunks.size());
		Chunk comprehensionChunk1 = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk1.getType());
		assertEquals(0l, comprehensionChunk1.getStartTime().getTime());
		assertEquals(7000, comprehensionChunk1.getEndTime().getTime());
		assertEquals(0, comprehensionChunk1.getSize());

		assertEquals(ModelingPhaseChunkExtractor.MODELING, chunks.get(1).getType());

		Chunk comprehensionChunk2 = chunks.get(2);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk2.getType());
		assertEquals(7000, comprehensionChunk2.getStartTime().getTime());
		assertEquals(15000, comprehensionChunk2.getEndTime().getTime());

		assertEquals(ModelingPhaseChunkExtractor.MODELING, chunks.get(3).getType());
	}

	@Test
	public void ignoreGroupEvent() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 2000),
				8500, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(4000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(4500), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(5200), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(7500), PromLogger.GROUP_EVENT_START, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(7500), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(7500), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(7500), PromLogger.GROUP_EVENT_END, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(2, chunks.size());

		Chunk modelingChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(3, modelingChunk.getSize());
		assertEquals(0, modelingChunk.getFromStepIndex());
		assertEquals(2, modelingChunk.getToStepIndex());
		Chunk reconciliationChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, reconciliationChunk.getType());
		assertEquals(2, reconciliationChunk.getSize());
		assertEquals(4, reconciliationChunk.getFromStepIndex());
		assertEquals(5, reconciliationChunk.getToStepIndex());
	}

	@Test
	public void ignoreScrollEventsAndSwitchToNewGroup() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 2000),
				3500, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(4000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(4500), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(5200), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(5500), AbstractGraphCommand.HSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(6000), AbstractGraphCommand.HSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(6500), AbstractGraphCommand.HSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(7000), AbstractGraphCommand.HSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(7500), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(7700), AbstractGraphCommand.MOVE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(3, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());

		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(3, modelingChunk.getSize());
		assertEquals(0, modelingChunk.getFromStepIndex());
		assertEquals(2, modelingChunk.getToStepIndex());

		Chunk reconciliationChunk = chunks.get(2);
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, reconciliationChunk.getType());
		assertEquals(2, reconciliationChunk.getSize());
		assertEquals(7, reconciliationChunk.getFromStepIndex());
		assertEquals(8, reconciliationChunk.getToStepIndex());
	}

	@Test
	public void ignoreScrollEventsInComprehensionDetection() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 100000),
				5000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(2000), AbstractGraphCommand.VSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2001), AbstractGraphCommand.VSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2002), AbstractGraphCommand.VSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2003), AbstractGraphCommand.HSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2004), AbstractGraphCommand.VSCROLL, null));
		AuditTrailEntry createNodeAuditTrailEntry = new AuditTrailEntry(new Date(7000), AbstractGraphCommand.CREATE_NODE, null);
		createNodeAuditTrailEntry.setAttribute(AbstractGraphCommand.ADD_NODE_START_TIME, 6000);
		processInstance.addEntry(new AuditTrailEntry(new Date(7003), AbstractGraphCommand.HSCROLL, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(7004), AbstractGraphCommand.VSCROLL, null));
		processInstance.addEntry(createNodeAuditTrailEntry);

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(2, chunks.size());

		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(0, comprehensionChunk.getStartTime().getTime());
		assertEquals(6000, comprehensionChunk.getEndTime().getTime());

		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(6000, modelingChunk.getStartTime().getTime());
		assertEquals(7000, modelingChunk.getEndTime().getTime());
		assertEquals(1, modelingChunk.getSize());
	}

	@Test
	public void illegalIncreaseOfComprehenionPhase() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 100000),
				5000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(7000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(15000), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(16000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(17000), AbstractGraphCommand.CREATE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(5, chunks.size());

		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());
		assertEquals(0, comprehensionChunk.getSize());
		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(1, modelingChunk.getSize());

		Chunk comprehensionChunk2 = chunks.get(2);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk2.getType());
		assertEquals(0, comprehensionChunk2.getSize());

		Chunk reconciliationChunk = chunks.get(3);
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, reconciliationChunk.getType());
		assertEquals(1, reconciliationChunk.getSize());

		Chunk modelingChunk2 = chunks.get(4);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk2.getType());
		assertEquals(2, modelingChunk2.getSize());
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalLookAhead() {
		new LookAheadModelingPhaseDetectionStrategy(-1);
	}

	@Test
	public void improvedComprehensionDetectionAtBeginning() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 100000),
				5000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		AuditTrailEntry createNodeAuditTrailEntry = new AuditTrailEntry(new Date(7000), AbstractGraphCommand.CREATE_NODE, null);
		createNodeAuditTrailEntry.setAttribute(AbstractGraphCommand.ADD_NODE_START_TIME, 2000);
		processInstance.addEntry(createNodeAuditTrailEntry);

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(1, chunks.size());
		Chunk modelingChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(2000, modelingChunk.getStartTime().getTime());
		assertEquals(7000, modelingChunk.getEndTime().getTime());
		assertEquals(1, modelingChunk.getSize());
	}

	@Test
	public void improvedComprehensionDetectionForRenameAtBeginning() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 100000),
				5000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		AuditTrailEntry createNodeAuditTrailEntry = new AuditTrailEntry(new Date(7000), AbstractGraphCommand.RENAME, null);
		createNodeAuditTrailEntry.setAttribute(AbstractGraphCommand.RENAME_START_TIME, 2000);
		createNodeAuditTrailEntry.setAttribute(AbstractGraphCommand.NAME, "sowieso");
		createNodeAuditTrailEntry.setAttribute(AbstractGraphCommand.NEW_NAME, "asdf");
		processInstance.addEntry(createNodeAuditTrailEntry);

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(1, chunks.size());
		Chunk modelingChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, modelingChunk.getType());
		assertEquals(2000, modelingChunk.getStartTime().getTime());
		assertEquals(7000, modelingChunk.getEndTime().getTime());
		assertEquals(1, modelingChunk.getSize());
	}

	@Test
	public void improvedComprehensionDetectionInTheMiddle() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 100000),
				5000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		AuditTrailEntry createNodeAuditTrailEntry = new AuditTrailEntry(new Date(10000), AbstractGraphCommand.CREATE_NODE, null);
		createNodeAuditTrailEntry.setAttribute(AbstractGraphCommand.ADD_NODE_START_TIME, 7000);
		processInstance.addEntry(createNodeAuditTrailEntry);

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(2, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());
		assertEquals(0, comprehensionChunk.getStartTime().getTime());
		assertEquals(7000, comprehensionChunk.getEndTime().getTime());

		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(7000, modelingChunk.getStartTime().getTime());
		assertEquals(10000, modelingChunk.getEndTime().getTime());
		assertEquals(1, modelingChunk.getSize());
	}

	@Test
	public void improvedComprehensionDetectionRenameInTheMiddle() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 100000),
				5000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		AuditTrailEntry createNodeAuditTrailEntry = new AuditTrailEntry(new Date(10000), AbstractGraphCommand.RENAME, null);
		createNodeAuditTrailEntry.setAttribute(AbstractGraphCommand.RENAME_START_TIME, 7000);
		createNodeAuditTrailEntry.setAttribute(AbstractGraphCommand.NAME, "jaja");
		createNodeAuditTrailEntry.setAttribute(AbstractGraphCommand.NEW_NAME, "sadf");
		processInstance.addEntry(createNodeAuditTrailEntry);

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(2, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());
		assertEquals(0, comprehensionChunk.getStartTime().getTime());
		assertEquals(7000, comprehensionChunk.getEndTime().getTime());

		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, modelingChunk.getType());
		assertEquals(7000, modelingChunk.getStartTime().getTime());
		assertEquals(10000, modelingChunk.getEndTime().getTime());
		assertEquals(1, modelingChunk.getSize());
	}

	@Test
	public void layoutDetection() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 100000),
				5000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(14000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(15000), AbstractGraphCommand.CREATE_EDGE, null));
		AuditTrailEntry layoutAuditTrailEntry = new AuditTrailEntry(PromLogger.GROUP_EVENT_START);
		layoutAuditTrailEntry.setAttribute(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME, IBMLayouter.LAYOUT);
		processInstance.addEntry(layoutAuditTrailEntry);
		processInstance.addEntry(new AuditTrailEntry(new Date(16000), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(16000), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(16000), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(18000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(20000), AbstractGraphCommand.CREATE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(4, chunks.size());
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, chunks.get(0).getType());
		assertEquals(ModelingPhaseChunkExtractor.MODELING, chunks.get(1).getType());
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, chunks.get(2).getType());
		assertEquals(ModelingPhaseChunkExtractor.MODELING, chunks.get(3).getType());
	}

	@Test
	public void longBreakBeforeShortPhase() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 5000),
				3500, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(4000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(5000), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(6500), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(17000), AbstractGraphCommand.CREATE_EDGE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(17500), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(18000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(19000), AbstractGraphCommand.CREATE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(5, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());
		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(1, modelingChunk.getSize());

		Chunk reconciliationChunk = chunks.get(2);
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, reconciliationChunk.getType());
		assertEquals(2, reconciliationChunk.getSize());

		Chunk comprehensionChunk2 = chunks.get(3);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk2.getType());

		Chunk modelingChunk2 = chunks.get(4);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk2.getType());
		assertEquals(4, modelingChunk2.getSize());

	}

	@Test
	public void mergeComprehensionPhases() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 100000),
				5000, 1000);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(7000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(15000), AbstractGraphCommand.CREATE_EDGE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(16000), AbstractGraphCommand.CREATE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);

		assertEquals(2, chunks.size());
		Chunk comprehensionChunk1 = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk1.getType());
		assertEquals(0l, comprehensionChunk1.getStartTime().getTime());
		assertEquals(15000, comprehensionChunk1.getEndTime().getTime());
		assertEquals(1, comprehensionChunk1.getSize());

		assertEquals(ModelingPhaseChunkExtractor.MODELING, chunks.get(1).getType());
	}

	@Test
	public void mergeThreeComprehensionPhases() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 100000),
				5000, 1000);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(7000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(15000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(22000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(23000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(24000), AbstractGraphCommand.CREATE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);

		assertEquals(2, chunks.size());
		Chunk comprehensionChunk1 = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk1.getType());
		assertEquals(0l, comprehensionChunk1.getStartTime().getTime());
		assertEquals(22000, comprehensionChunk1.getEndTime().getTime());
		assertEquals(2, comprehensionChunk1.getSize());

		assertEquals(ModelingPhaseChunkExtractor.MODELING, chunks.get(1).getType());
	}

	@Test
	public void modelingPhaseInterruptedBySingleReconciliationChunk() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 5000),
				1000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(2100), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3500), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3900), AbstractGraphCommand.CREATE_EDGE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(4200), AbstractGraphCommand.CREATE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(2, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());
		assertEquals(0, comprehensionChunk.getSize());
		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(5, modelingChunk.getSize());
	}

	@Test
	public void modelingPhaseInterruptedByTwoShortReconciliationChunk() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 5000),
				1000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(2100), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3500), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3700), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3900), AbstractGraphCommand.CREATE_EDGE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(4200), AbstractGraphCommand.CREATE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(2, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());
		assertEquals(0, comprehensionChunk.getSize());
		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(6, modelingChunk.getSize());
	}

	@Test
	public void shortModelingStepWithinReconcliationPhase() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 5000),
				2000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(3100), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(5000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(7000), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(8001), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(8100), AbstractGraphCommand.CREATE_EDGE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(8300), AbstractGraphCommand.CREATE_EDGE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(8500), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(9000), AbstractGraphCommand.MOVE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(3, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());
		assertEquals(0, comprehensionChunk.getSize());
		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(2, modelingChunk.getSize());
		Chunk reconciliationChunk = chunks.get(2);
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, reconciliationChunk.getType());
		assertEquals(6, reconciliationChunk.getSize());
	}

	@Test
	public void simpleChunkExtractionWithoutLookahead() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new LookAheadModelingPhaseDetectionStrategy(0), 1000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(2000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2500), AbstractGraphCommand.MOVE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(3, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());
		assertEquals(0, comprehensionChunk.getSize());
		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(1, modelingChunk.getSize());
		assertEquals(0, modelingChunk.getFromStepIndex());
		assertEquals(0, modelingChunk.getToStepIndex());
		Chunk reconciliationChunk = chunks.get(2);
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, reconciliationChunk.getType());
		assertEquals(1, reconciliationChunk.getSize());
		assertEquals(1, reconciliationChunk.getFromStepIndex());
		assertEquals(1, reconciliationChunk.getToStepIndex());
	}

	@Test
	public void splittingOfGroupedEvents() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 5000),
				2000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(2100), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(2500), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3000), PromLogger.GROUP_EVENT_START, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3000), AbstractGraphCommand.CREATE_EDGE, null));
		// it can happen that the next entry is logged a millisecond later
		processInstance.addEntry(new AuditTrailEntry(new Date(3001), AbstractGraphCommand.DELETE_EDGE_BENDPOINT, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3001), PromLogger.GROUP_EVENT_END, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3800), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(4200), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(4900), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(8500), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(8700), AbstractGraphCommand.CREATE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(5, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());

		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(4, modelingChunk.getSize());
		assertEquals(0, modelingChunk.getFromStepIndex());
		assertEquals(4, modelingChunk.getToStepIndex());

		Chunk reconciliationChunk = chunks.get(2);
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, reconciliationChunk.getType());
		assertEquals(3, reconciliationChunk.getSize());
		assertEquals(6, reconciliationChunk.getFromStepIndex());
		assertEquals(8, reconciliationChunk.getToStepIndex());

		Chunk comprehensionChunk2 = chunks.get(3);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk2.getType());

		Chunk modelingChunk2 = chunks.get(4);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(2, modelingChunk2.getSize());
		assertEquals(9, modelingChunk2.getFromStepIndex());
		assertEquals(10, modelingChunk2.getToStepIndex());
	}

	@Test
	public void switchToReconciliationPhaseDueToTooLongReconliationPhase() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new DurationModelingPhaseDetectionStrategy(1000, 5000),
				10000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(11100), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(13000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(13500), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(14501), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(24700), AbstractGraphCommand.CREATE_EDGE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(25500), AbstractGraphCommand.CREATE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(5, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());
		assertEquals(0, comprehensionChunk.getSize());
		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(2, modelingChunk.getSize());
		Chunk reconciliationChunk = chunks.get(2);
		assertEquals(ModelingPhaseChunkExtractor.RECONCILIATION, reconciliationChunk.getType());
		assertEquals(2, reconciliationChunk.getSize());
		Chunk modelingChunk2 = chunks.get(4);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk2.getType());
		assertEquals(2, modelingChunk2.getSize());
	}

	@Test
	public void testLookAhead() {
		ModelingPhaseChunkExtractor extractor = new ModelingPhaseChunkExtractor(new LookAheadModelingPhaseDetectionStrategy(1), 1000, 0);
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 1000));
		processInstance.addEntry(new AuditTrailEntry(new Date(3000), AbstractGraphCommand.CREATE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3500), AbstractGraphCommand.CREATE_EDGE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(4000), AbstractGraphCommand.MOVE_NODE, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(4500), AbstractGraphCommand.CREATE_NODE, null));

		List<Chunk> chunks = extractor.extractChunks(processInstance);
		assertEquals(2, chunks.size());
		Chunk comprehensionChunk = chunks.get(0);
		assertEquals(ModelingPhaseChunkExtractor.COMPREHENSION, comprehensionChunk.getType());

		Chunk modelingChunk = chunks.get(1);
		assertEquals(ModelingPhaseChunkExtractor.MODELING, modelingChunk.getType());
		assertEquals(4, modelingChunk.getSize());
		assertEquals(0, modelingChunk.getFromStepIndex());
		assertEquals(3, modelingChunk.getToStepIndex());
	}
}

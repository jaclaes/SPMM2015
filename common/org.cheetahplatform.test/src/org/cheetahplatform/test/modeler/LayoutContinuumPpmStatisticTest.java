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

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.export.AbstractLayoutStatistic;
import org.cheetahplatform.modeler.graph.export.Chunk;
import org.cheetahplatform.modeler.graph.export.LayoutContinuumPpmStatistic;
import org.cheetahplatform.modeler.graph.export.ModelingPhaseChunkExtractor;
import org.cheetahplatform.modeler.graph.export.ProcessOfProcessModelingIteration;
import org.cheetahplatform.test.TestHelper;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class LayoutContinuumPpmStatisticTest extends LocaleSensitiveTest {
	protected void addModelingEntry(ProcessInstance processInstance, Chunk modelingChunk) {
		AuditTrailEntry entry = new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE);
		modelingChunk.addEntry(entry);
		processInstance.addEntry(entry);
	}

	protected void addReconciliationEntry(ProcessInstance processInstance, Chunk modelingChunk) {
		AuditTrailEntry entry = new AuditTrailEntry(AbstractGraphCommand.CREATE_EDGE);
		modelingChunk.addEntry(entry);
		processInstance.addEntry(entry);
	}

	protected ProcessOfProcessModelingIteration createIterationWithoutLayout(ProcessInstance processInstance) {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(new Chunk(
				ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));

		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		addModelingEntry(processInstance, modelingChunk);
		addModelingEntry(processInstance, modelingChunk);
		addModelingEntry(processInstance, modelingChunk);
		addModelingEntry(processInstance, modelingChunk);
		iteration.addChunk(modelingChunk);

		Chunk reconciliationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(0), 0);
		addReconciliationEntry(processInstance, reconciliationChunk);
		addReconciliationEntry(processInstance, reconciliationChunk);
		addReconciliationEntry(processInstance, reconciliationChunk);
		iteration.addChunk(reconciliationChunk);
		return iteration;
	}

	protected ProcessOfProcessModelingIteration createLayoutIteration(ProcessInstance processInstance) {
		ProcessOfProcessModelingIteration iteration = new ProcessOfProcessModelingIteration(new Chunk(
				ModelingPhaseChunkExtractor.COMPREHENSION, new Date(), 0));

		Chunk modelingChunk = new Chunk(ModelingPhaseChunkExtractor.MODELING, new Date(0), 0);
		addModelingEntry(processInstance, modelingChunk);
		addModelingEntry(processInstance, modelingChunk);
		addModelingEntry(processInstance, modelingChunk);
		addModelingEntry(processInstance, modelingChunk);
		iteration.addChunk(modelingChunk);

		Chunk reconciliationChunk = new Chunk(ModelingPhaseChunkExtractor.RECONCILIATION, new Date(0), 0);
		reconciliationChunk.addEntry(TestHelper.createLayoutEntry(processInstance, new Date(2000)));
		addReconciliationEntry(processInstance, reconciliationChunk);
		addReconciliationEntry(processInstance, reconciliationChunk);
		addReconciliationEntry(processInstance, reconciliationChunk);
		addReconciliationEntry(processInstance, reconciliationChunk);
		processInstance.addEntry(new AuditTrailEntry(PromLogger.GROUP_EVENT_END));
		reconciliationChunk.addEntry(new AuditTrailEntry(PromLogger.GROUP_EVENT_END));
		addReconciliationEntry(processInstance, reconciliationChunk);
		iteration.addChunk(reconciliationChunk);
		return iteration;
	}

	@Test
	public void emptyAuditTrailEntries() {
		AbstractLayoutStatistic statistic = new LayoutContinuumPpmStatistic();
		String value = statistic.getValue(new ProcessInstance("id"), null, new ArrayList<ProcessOfProcessModelingIteration>());
		assertEquals("N/A", value);
	}

	@Test
	public void emptyPpmIterations() {
		AbstractLayoutStatistic statistic = new LayoutContinuumPpmStatistic();
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		String value = statistic.getValue(processInstance, null, new ArrayList<ProcessOfProcessModelingIteration>());
		assertEquals("N/A", value);
	}

	@Test
	public void multipleLayoutEvents() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		LayoutContinuumPpmStatistic statistic = new LayoutContinuumPpmStatistic();
		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		iterations.add(createIterationWithoutLayout(processInstance));
		iterations.add(createLayoutIteration(processInstance));
		iterations.add(createIterationWithoutLayout(processInstance));
		iterations.add(createLayoutIteration(processInstance));

		String value = statistic.getValue(processInstance, null, iterations);
		assertEquals("0,50 1,00", value);
	}

	@Test
	public void noLayoutEvents() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));

		AbstractLayoutStatistic statistic = new LayoutContinuumPpmStatistic();
		String value = statistic.getValue(processInstance, null, new ArrayList<ProcessOfProcessModelingIteration>());
		assertEquals("N/A", value);
	}

	@Test(expected = AssertionFailedException.class)
	public void nullPpmIterations() {
		ProcessInstance processInstance = new ProcessInstance();
		AbstractLayoutStatistic statistic = new LayoutContinuumPpmStatistic();
		statistic.getValue(processInstance, null, null);
	}

	@Test(expected = AssertionFailedException.class)
	public void nullProcessInstance() {
		AbstractLayoutStatistic statistic = new LayoutContinuumPpmStatistic();
		statistic.getValue(null, null, null);
	}

	@Test
	public void singleLayoutEventAtTheEnd() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		LayoutContinuumPpmStatistic statistic = new LayoutContinuumPpmStatistic();
		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		iterations.add(createLayoutIteration(processInstance));

		String value = statistic.getValue(processInstance, null, iterations);
		assertEquals("1,00", value);
	}

	@Test
	public void singleLayoutEventInTheMiddle() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		LayoutContinuumPpmStatistic statistic = new LayoutContinuumPpmStatistic();
		List<ProcessOfProcessModelingIteration> iterations = new ArrayList<ProcessOfProcessModelingIteration>();
		iterations.add(createLayoutIteration(processInstance));
		iterations.add(createIterationWithoutLayout(processInstance));

		String value = statistic.getValue(processInstance, null, iterations);
		assertEquals("0,50", value);
	}
}

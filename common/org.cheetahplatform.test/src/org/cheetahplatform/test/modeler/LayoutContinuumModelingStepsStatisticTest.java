/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.export.AbstractLayoutStatistic;
import org.cheetahplatform.modeler.graph.export.IPpmStatistic;
import org.cheetahplatform.modeler.graph.export.LayoutContinuumModelingStepsStatistic;
import org.cheetahplatform.test.TestHelper;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class LayoutContinuumModelingStepsStatisticTest extends LocaleSensitiveTest {
	@Test
	public void emptyAuditTrailEntries() {
		IPpmStatistic statistic = new LayoutContinuumModelingStepsStatistic();
		String value = statistic.getValue(new ProcessInstance("id"), null, null);
		assertEquals("N/A", value);
	}

	@Test
	public void ignoreGroupEvent() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(PromLogger.GROUP_EVENT_START));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		processInstance.addEntry(new AuditTrailEntry(PromLogger.GROUP_EVENT_END));

		TestHelper.createLayoutEntry(processInstance, new Date(3000));
		processInstance.addEntry(new AuditTrailEntry(PromLogger.GROUP_EVENT_END));
		processInstance.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		TestHelper.createLayoutEntry(processInstance, new Date(4000));
		processInstance.addEntry(new AuditTrailEntry(PromLogger.GROUP_EVENT_END));

		AbstractLayoutStatistic statistic = new LayoutContinuumModelingStepsStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("0,50 1,00", value);
	}

	@Test
	public void multipleLayoutEvents() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		processInstance.addEntry(new AuditTrailEntry(PromLogger.GROUP_EVENT_END));
		processInstance.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		processInstance.addEntry(new AuditTrailEntry(PromLogger.GROUP_EVENT_END));

		AbstractLayoutStatistic statistic = new LayoutContinuumModelingStepsStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("0,50 1,00", value);
	}

	@Test
	public void noLayoutEvents() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));

		AbstractLayoutStatistic statistic = new LayoutContinuumModelingStepsStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("N/A", value);
	}

	@Test(expected = AssertionFailedException.class)
	public void nullAuditTrailEntries() {
		AbstractLayoutStatistic statistic = new LayoutContinuumModelingStepsStatistic();
		statistic.getValue(null, null, null);
	}

	@Test
	public void singleLayoutEventAtTheEnd() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));

		AbstractLayoutStatistic statistic = new LayoutContinuumModelingStepsStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("1,00", value);
	}

	@Test
	public void singleLayoutEventInTheMiddle() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		processInstance.addEntry(new AuditTrailEntry(PromLogger.GROUP_EVENT_END));
		processInstance.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));

		AbstractLayoutStatistic statistic = new LayoutContinuumModelingStepsStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("0,50", value);
	}
}

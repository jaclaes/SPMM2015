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
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.export.AbstractLayoutStatistic;
import org.cheetahplatform.modeler.graph.export.LayoutContinuumDurationStatistic;
import org.cheetahplatform.test.TestHelper;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class LayoutContinuumDurationStatisticTest extends LocaleSensitiveTest {
	@Test
	public void emptyAuditTrailEntries() {
		AbstractLayoutStatistic statistic = new LayoutContinuumDurationStatistic();
		String value = statistic.getValue(new ProcessInstance("id"), null, null);
		assertEquals("N/A", value);
	}

	@Test
	public void noLayoutEvents() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));

		AbstractLayoutStatistic statistic = new LayoutContinuumDurationStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("N/A", value);
	}

	@Test(expected = AssertionFailedException.class)
	public void nullAuditTrailEntries() {
		AbstractLayoutStatistic statistic = new LayoutContinuumDurationStatistic();
		statistic.getValue(null, null, null);
	}

	@Test
	public void singleLayoutAtTheEnd() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(new Date(2000), AbstractGraphCommand.CREATE_NODE, null));
		TestHelper.createLayoutEntry(processInstance, new Date(6000));

		AbstractLayoutStatistic statistic = new LayoutContinuumDurationStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("1,00", value);
	}

	@Test
	public void singleLayoutInTheMiddle() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(3000));
		processInstance.addEntry(new AuditTrailEntry(new Date(6000), AbstractGraphCommand.CREATE_NODE, null));

		AbstractLayoutStatistic statistic = new LayoutContinuumDurationStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("0,50", value);
	}

	@Test
	public void threeLayoutEvents() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		TestHelper.createLayoutEntry(processInstance, new Date(4000));
		TestHelper.createLayoutEntry(processInstance, new Date(6000));

		AbstractLayoutStatistic statistic = new LayoutContinuumDurationStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("0,33 0,67 1,00", value);
	}

	@Test
	public void threeLayoutEventsNotCountingUndoEvent() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		TestHelper.createLayoutEntry(processInstance, new Date(4000));
		TestHelper.createUndoLayoutEntry(processInstance, new Date(5000));
		TestHelper.createLayoutEntry(processInstance, new Date(6000));

		AbstractLayoutStatistic statistic = new LayoutContinuumDurationStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("0,33 0,67 1,00", value);
	}

	@Test
	public void twoLayoutsInTheMiddleAndAtTheEnd() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(3000));
		TestHelper.createLayoutEntry(processInstance, new Date(6000));

		AbstractLayoutStatistic statistic = new LayoutContinuumDurationStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("0,50 1,00", value);
	}
}

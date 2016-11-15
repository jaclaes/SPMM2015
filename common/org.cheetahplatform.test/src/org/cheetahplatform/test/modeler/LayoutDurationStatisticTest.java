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
import org.cheetahplatform.modeler.graph.export.LayoutDurationStatistic;
import org.cheetahplatform.test.TestHelper;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class LayoutDurationStatisticTest extends LocaleSensitiveTest {
	@Test
	public void emptyAuditTrailEntries() {
		LayoutDurationStatistic statistic = new LayoutDurationStatistic();
		String value = statistic.getValue(new ProcessInstance("id"), null, null);
		assertEquals("N/A", value);
	}

	@Test
	public void noLayoutEvents() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));

		LayoutDurationStatistic statistic = new LayoutDurationStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("N/A", value);
	}

	@Test(expected = AssertionFailedException.class)
	public void nullAuditTrailEntries() {
		LayoutDurationStatistic statistic = new LayoutDurationStatistic();
		statistic.getValue(null, null, null);
	}

	@Test
	public void singleLayoutEvent() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(2000), 200);

		LayoutDurationStatistic statistic = new LayoutDurationStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("200,00", value);
	}

	@Test
	public void threeLayoutEvents() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(2000), 1568);
		TestHelper.createLayoutEntry(processInstance, new Date(2000), 555);
		TestHelper.createLayoutEntry(processInstance, new Date(2000), 252);

		LayoutDurationStatistic statistic = new LayoutDurationStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("791,67", value);
	}

	@Test
	public void twoLayoutEvents() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(2000), 2000);
		TestHelper.createLayoutEntry(processInstance, new Date(2000), 4000);

		LayoutDurationStatistic statistic = new LayoutDurationStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("3000,00", value);
	}
}

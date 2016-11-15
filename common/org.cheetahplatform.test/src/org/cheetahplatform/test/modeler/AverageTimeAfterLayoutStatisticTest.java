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
import org.cheetahplatform.modeler.graph.export.AverageTimeAfterLayoutStatistic;
import org.cheetahplatform.test.TestHelper;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class AverageTimeAfterLayoutStatisticTest extends LocaleSensitiveTest {
	@Test
	public void emptyAuditTrailEntries() {
		AbstractLayoutStatistic statistic = new AverageTimeAfterLayoutStatistic();
		String value = statistic.getValue(new ProcessInstance("id"), null, null);
		assertEquals("N/A", value);
	}

	@Test
	public void multipleLayoutEvent() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		processInstance.addEntry(new AuditTrailEntry(new Date(2000), PromLogger.GROUP_EVENT_END, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3000), AbstractGraphCommand.CREATE_NODE, null));
		TestHelper.createLayoutEntry(processInstance, new Date(5000));
		processInstance.addEntry(new AuditTrailEntry(new Date(6000), PromLogger.GROUP_EVENT_END, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(8000), AbstractGraphCommand.CREATE_NODE, null));

		AbstractLayoutStatistic statistic = new AverageTimeAfterLayoutStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("1500,00", value);
	}

	@Test
	public void noLayoutEvents() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		processInstance.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE));

		AbstractLayoutStatistic statistic = new AverageTimeAfterLayoutStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("N/A", value);
	}

	@Test(expected = AssertionFailedException.class)
	public void nullAuditTrailEntries() {
		AbstractLayoutStatistic statistic = new AverageTimeAfterLayoutStatistic();
		statistic.getValue(null, null, null);
	}

	@Test
	public void singeLayoutEvent() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		processInstance.addEntry(new AuditTrailEntry(new Date(2000), PromLogger.GROUP_EVENT_END, null));
		processInstance.addEntry(new AuditTrailEntry(new Date(3000), AbstractGraphCommand.CREATE_NODE, null));

		AbstractLayoutStatistic statistic = new AverageTimeAfterLayoutStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("1000,00", value);
	}
}

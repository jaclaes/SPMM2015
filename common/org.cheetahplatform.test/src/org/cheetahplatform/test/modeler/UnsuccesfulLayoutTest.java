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
import org.cheetahplatform.modeler.graph.export.UnsuccesfulLayoutStatistic;
import org.cheetahplatform.test.TestHelper;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class UnsuccesfulLayoutTest extends LocaleSensitiveTest {
	@Test
	public void emptyAuditTrailEntries() {
		UnsuccesfulLayoutStatistic statistic = new UnsuccesfulLayoutStatistic();
		String value = statistic.getValue(new ProcessInstance("id"), null, null);
		assertEquals("N/A", value);
	}

	@Test(expected = AssertionFailedException.class)
	public void nullAuditTrailEntries() {
		UnsuccesfulLayoutStatistic statistic = new UnsuccesfulLayoutStatistic();
		statistic.getValue(null, null, null);
	}

	@Test
	public void simpleLayout() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		processInstance.addEntry(new AuditTrailEntry(AbstractGraphCommand.UNSUCCESSFUL_LAYOUT));
		processInstance.addEntry(new AuditTrailEntry(AbstractGraphCommand.CREATE_EDGE));
		processInstance.addEntry(new AuditTrailEntry(AbstractGraphCommand.UNSUCCESSFUL_LAYOUT));
		TestHelper.createLayoutEntry(processInstance, new Date(6000));
		TestHelper.createUndoLayoutEntry(processInstance, new Date(8000));

		UnsuccesfulLayoutStatistic statistic = new UnsuccesfulLayoutStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("2", value);
	}
}

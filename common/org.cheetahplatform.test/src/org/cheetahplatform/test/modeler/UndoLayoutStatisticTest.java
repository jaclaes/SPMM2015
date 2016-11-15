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
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.graph.export.AbstractLayoutStatistic;
import org.cheetahplatform.modeler.graph.export.UndoLayoutStatistic;
import org.cheetahplatform.test.TestHelper;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class UndoLayoutStatisticTest extends LocaleSensitiveTest {
	@Test
	public void emptyAuditTrailEntries() {
		AbstractLayoutStatistic statistic = new UndoLayoutStatistic();
		String value = statistic.getValue(new ProcessInstance("id"), null, null);
		assertEquals("N/A", value);
	}

	@Test(expected = AssertionFailedException.class)
	public void nullAuditTrailEntries() {
		AbstractLayoutStatistic statistic = new UndoLayoutStatistic();
		statistic.getValue(null, null, null);
	}

	@Test
	public void simpleLayout() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		TestHelper.createLayoutEntry(processInstance, new Date(4000));
		TestHelper.createLayoutEntry(processInstance, new Date(6000));

		AbstractLayoutStatistic statistic = new UndoLayoutStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("0", value);
	}

	@Test
	public void undoLayout() {
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_TIMESTAMP, 0));
		TestHelper.createLayoutEntry(processInstance, new Date(2000));
		TestHelper.createUndoLayoutEntry(processInstance, new Date(4000));
		TestHelper.createLayoutEntry(processInstance, new Date(6000));
		TestHelper.createUndoLayoutEntry(processInstance, new Date(4000));

		AbstractLayoutStatistic statistic = new UndoLayoutStatistic();
		String value = statistic.getValue(processInstance, null, null);
		assertEquals("2", value);
	}
}

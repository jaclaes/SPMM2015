/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.export.AverageDurationBetweenCreationAndLastMoveStatistic;
import org.cheetahplatform.modeler.graph.export.IPpmStatistic;
import org.junit.Test;

public class AverageDurationBetweenCreationAndLastMoveStatisticTest extends LocaleSensitiveTest {
	@Test
	public void noProcessInstance() {
		AverageDurationBetweenCreationAndLastMoveStatistic statistic = new AverageDurationBetweenCreationAndLastMoveStatistic();
		String value = statistic.getValue(null, null, null);
		assertEquals(IPpmStatistic.N_A, value);
	}

	@Test
	public void oneNode() {
		AverageDurationBetweenCreationAndLastMoveStatistic statistic = new AverageDurationBetweenCreationAndLastMoveStatistic();

		ProcessInstance instance = new ProcessInstance("id");

		AuditTrailEntry auditTrailEntry = new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE);
		auditTrailEntry.setTimestamp(new Date(1000));
		auditTrailEntry.setAttribute(AbstractGraphCommand.DESCRIPTOR, EditorRegistry.BPMN_ACTIVITY);
		auditTrailEntry.setAttribute(AbstractGraphCommand.ID, 1l);
		instance.addEntry(auditTrailEntry);
		AuditTrailEntry auditTrailEntry2 = new AuditTrailEntry(AbstractGraphCommand.MOVE_NODE);
		auditTrailEntry2.setAttribute(AbstractGraphCommand.ID, 1l);
		auditTrailEntry2.setTimestamp(new Date(2000));
		AuditTrailEntry auditTrailEntry3 = new AuditTrailEntry(AbstractGraphCommand.MOVE_NODE);
		auditTrailEntry3.setTimestamp(new Date(3000));
		auditTrailEntry3.setAttribute(AbstractGraphCommand.ID, 1l);
		instance.addEntry(auditTrailEntry2);
		instance.addEntry(auditTrailEntry3);

		AuditTrailEntry auditTrailEntry4 = new AuditTrailEntry(AbstractGraphCommand.RENAME);
		auditTrailEntry4.setTimestamp(new Date(4000));
		auditTrailEntry4.setAttribute(AbstractGraphCommand.ID, 1l);
		instance.addEntry(auditTrailEntry4);

		String value = statistic.getValue(instance, null, null);
		assertEquals("2000,00", value);
	}

	@Test
	public void severalNodes() {
		AverageDurationBetweenCreationAndLastMoveStatistic statistic = new AverageDurationBetweenCreationAndLastMoveStatistic();

		ProcessInstance instance = new ProcessInstance("id");

		AuditTrailEntry auditTrailEntry = new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE);
		auditTrailEntry.setTimestamp(new Date(1000));
		auditTrailEntry.setAttribute(AbstractGraphCommand.DESCRIPTOR, EditorRegistry.BPMN_ACTIVITY);
		auditTrailEntry.setAttribute(AbstractGraphCommand.ID, 1l);
		instance.addEntry(auditTrailEntry);
		AuditTrailEntry auditTrailEntry2 = new AuditTrailEntry(AbstractGraphCommand.MOVE_NODE);
		auditTrailEntry2.setAttribute(AbstractGraphCommand.ID, 1l);
		auditTrailEntry2.setTimestamp(new Date(2000));
		AuditTrailEntry auditTrailEntry3 = new AuditTrailEntry(AbstractGraphCommand.MOVE_NODE);
		auditTrailEntry3.setTimestamp(new Date(5000));
		auditTrailEntry3.setAttribute(AbstractGraphCommand.ID, 1l);
		instance.addEntry(auditTrailEntry2);
		instance.addEntry(auditTrailEntry3);

		AuditTrailEntry auditTrailEntry4 = new AuditTrailEntry(AbstractGraphCommand.CREATE_NODE);
		auditTrailEntry4.setTimestamp(new Date(6000));
		auditTrailEntry4.setAttribute(AbstractGraphCommand.DESCRIPTOR, EditorRegistry.BPMN_ACTIVITY);
		auditTrailEntry4.setAttribute(AbstractGraphCommand.ID, 3l);
		instance.addEntry(auditTrailEntry4);

		AuditTrailEntry auditTrailEntry5 = new AuditTrailEntry(AbstractGraphCommand.MOVE_NODE);
		auditTrailEntry5.setAttribute(AbstractGraphCommand.ID, 3l);
		auditTrailEntry5.setTimestamp(new Date(8000));
		instance.addEntry(auditTrailEntry5);

		String value = statistic.getValue(instance, null, null);
		assertEquals("3000,00", value);
	}
}

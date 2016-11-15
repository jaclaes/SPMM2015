/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.testarossa;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.MilestoneInstance;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyMilestone;
import org.cheetahplatform.test.modeler.TestLogListener;
import org.junit.Test;

public class WeeklyTest {
	@Test
	public void moveMileStone() throws Exception {
		DeclarativeProcessSchema schema = new DeclarativeProcessSchema();
		schema.createMilestone("A", new Duration(1, 0));
		Workspace workspace = new Workspace(schema);

		MilestoneInstance milestoneCore = workspace.getProcessInstance().getMilestones().iterator().next();
		TestLogListener listener = new TestLogListener();
		workspace.getProcessInstance().addLogListener(listener, false);
		workspace.getWeekly().moveToNextWeek(new WeeklyMilestone(workspace, milestoneCore));

		List<AuditTrailEntry> entries = listener.getEntries();
		assertEquals(1, entries.size());
		assertEquals(TDMConstants.MILESTONE_MOVE_FORWARD, entries.get(0).getAttribute(TDMConstants.ATTRIBUTE_MILESTONE_MOVE_DIRECTION));
		assertEquals(milestoneCore.getCheetahId(), entries.get(0).getWorkflowModeleElementAsLong());
	}
}

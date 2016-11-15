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
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.modeling.Milestone;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.modeler.test.model.MultiWeek;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.cheetahplatform.test.modeler.TestLogListener;
import org.eclipse.draw2d.geometry.Point;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class WeeklyActivityTest {
	@Test
	public void moveNoLogging() throws Exception {
		DeclarativeProcessSchema schema = new DeclarativeProcessSchema();
		DeclarativeActivity activity = schema.createActivity("A");
		Milestone milestone = schema.createMilestone("B", new Duration(10, 0));
		milestone.addActivity(activity);
		Workspace workspace = new Workspace(schema);

		TestLogListener listener = new TestLogListener();
		workspace.getProcessInstance().addLogListener(listener, false);
		WeeklyActivity weeklyActivity = workspace.getWeekly().getActivity(activity);
		MultiWeek multiWeek = (MultiWeek) weeklyActivity.getParent();
		weeklyActivity.move(multiWeek, new Point(0, 0), 0);

		List<AuditTrailEntry> entries = listener.getEntries();
		assertEquals(0, entries.size());
	}

	@Test
	public void moveWithLogging() throws Exception {
		DeclarativeProcessSchema schema = new DeclarativeProcessSchema();
		DeclarativeActivity activity = schema.createActivity("A");
		Milestone milestone = schema.createMilestone("B", new Duration(10, 0));
		milestone.addActivity(activity);
		Workspace workspace = new Workspace(schema);

		TestLogListener listener = new TestLogListener();
		workspace.getProcessInstance().addLogListener(listener, false);
		WeeklyActivity weeklyActivity = workspace.getWeekly().getActivity(activity);
		MultiWeek multiWeek = (MultiWeek) weeklyActivity.getParent();
		weeklyActivity.move(multiWeek, new Point(0, 0), 2);

		List<AuditTrailEntry> entries = listener.getEntries();
		assertEquals(1, entries.size());
		assertEquals(weeklyActivity.getActivity().getCheetahId(), entries.get(0).getWorkflowModeleElementAsLong());
	}

}

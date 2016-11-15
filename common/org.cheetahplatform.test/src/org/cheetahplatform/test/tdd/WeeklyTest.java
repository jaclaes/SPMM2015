/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.tdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.modeling.Milestone;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.modeler.test.model.MultiWeek;
import org.cheetahplatform.tdm.modeler.test.model.Weekly;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyMilestone;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyEditPart;
import org.junit.Test;

public class WeeklyTest {
	@Test
	public void findMilestone() throws Exception {
		DeclarativeProcessSchema schema = new DeclarativeProcessSchema();
		Milestone a = schema.createMilestone("A", new Duration(0, 0));
		Milestone b = schema.createMilestone("B", new Duration(1, 0));
		Milestone c = schema.createMilestone("C", new Duration(2, 0));
		Workspace workspace = new Workspace(schema);

		DateTime start = workspace.getProcessInstance().getStartTime().copy();
		Weekly weekly = workspace.getWeekly();
		WeeklyMilestone milestone = weekly.findMilestone(new DateTime(1970, 0, 1, 0, 0, true));
		assertEquals(a, milestone.getMilestone().getNode());

		assertEquals(b, weekly.findMilestone(start).getMilestone().getNode());

		start.plus(new Duration(1, 0));
		assertEquals(c, weekly.findMilestone(start).getMilestone().getNode());

		start.plus(new Duration(1, 0));
		assertEquals(null, weekly.findMilestone(start));
	}

	@Test
	public void moveMilestone() throws Exception {
		DeclarativeProcessSchema schema = new DeclarativeProcessSchema();
		schema.createMilestone("A", new Duration(0, 0));
		DeclarativeProcessInstance instance = schema.instantiate();
		DateTime startTime = new DateTime(0, 0, true);
		instance.setStartTime(startTime);
		Workspace workspace = new Workspace(instance);
		Weekly weekly = workspace.getWeekly();

		MultiWeek week = weekly.getWeek(startTime);
		assertNotNull(week);

		for (int i = 0; i <= WeeklyEditPart.DEFAULT_WEEKS_PER_ROW; i++) {
			List<WeeklyMilestone> milestones = weekly.getMilestones(week);

			if (i == WeeklyEditPart.DEFAULT_WEEKS_PER_ROW) {
				assertEquals(0, milestones.size());
			} else {
				assertEquals(1, milestones.size());
				milestones.get(0).moveToNextWeek();
			}
		}
	}
}

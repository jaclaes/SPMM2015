/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.tdd;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.modeling.Milestone;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.MilestoneInstance;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyMilestone;
import org.junit.Test;

public class WeeklyMilestoneTest {
	@Test
	public void setStart() throws Exception {
		DeclarativeProcessSchema schema = new DeclarativeProcessSchema();
		DeclarativeProcessInstance instance = schema.instantiate();
		instance.setStartTime(new DateTime(0, 0, true));
		WeeklyMilestone milestone = new WeeklyMilestone(new Workspace(instance), (MilestoneInstance) new Milestone("A", new Duration(2, 0))
				.instantiate(instance));

		// future
		DateTime startTime = milestone.getStartTime();
		assertEquals(0, instance.getStartTime().copy().plus(new Duration(2, 0)).compareTo(startTime));

		// same
		milestone.getMilestone().setStartTime(instance.getStartTime());
		startTime = milestone.getStartTime();
		assertEquals(0, instance.getStartTime().compareTo(startTime));

		// past
		DateTime expected = (DateTime) instance.getStartTime().copy().minus(new Duration(3, 0));
		milestone.getMilestone().setStartTime(expected);
		startTime = milestone.getStartTime();
		assertEquals(0, expected.compareTo(startTime));
	}
}
/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.MilestoneInstance;
import org.junit.Test;

public class MilestoneInstanceTest {
	@Test
	public void setStart() throws Exception {
		DeclarativeProcessSchema schema = new DeclarativeProcessSchema();
		schema.createMilestone("A", new Duration(5, 0));

		DeclarativeProcessInstance instance = schema.instantiate();
		Set<MilestoneInstance> instances = instance.getMilestones();
		assertEquals(1, instances.size());
		MilestoneInstance milestoneInstance = instances.iterator().next();
		Duration duration = new Duration(7, 0);
		DateTime newStart = milestoneInstance.getStartTime();
		newStart.plus(duration);
		milestoneInstance.setStartTime(newStart);

		Duration actual = new Duration(instance.getStartTime(), milestoneInstance.getStartTime());
		assertEquals(0, new Duration(12, 0).compareTo(actual));
	}
}

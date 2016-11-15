/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core.imperative.runtime;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.junit.Assert;
import org.junit.Test;

public class ImperativeProcessInstanceTest {
	@Test
	public void getActiveActivitiesEmpty() throws Exception {
		ImperativeProcessInstance instance = new ImperativeProcessInstance(new ImperativeProcessSchema());
		List<INodeInstance> activities = instance.getActiveActivities();
		Assert.assertEquals(0, activities.size());
	}

	@Test
	public void getActiveActivitiesSingleActivity() throws Exception {
		ImperativeProcessSchema schema = new ImperativeProcessSchema();
		ImperativeActivity activity = schema.createActivity("A");
		schema.getStart().addSuccessor(activity);
		ImperativeProcessInstance instance = new ImperativeProcessInstance(schema);
		List<INodeInstance> activities = instance.getActiveActivities();
		Assert.assertEquals(1, activities.size());
	}

	@Test
	public void getActiveActivitiesSplit() throws Exception {
		ImperativeProcessSchema schema = new ImperativeProcessSchema();
		ImperativeActivity activityA = schema.createActivity("A");
		ImperativeActivity activityB = schema.createActivity("B");
		AndSplit split = schema.createAndSplit("Split");
		schema.getStart().addSuccessor(split);
		split.addSuccessor(activityA);
		split.addSuccessor(activityB);
		ImperativeProcessInstance instance = new ImperativeProcessInstance(schema);
		List<INodeInstance> activities = instance.getActiveActivities();
		Assert.assertEquals(2, activities.size());
	}

	@Test
	public void getActiveActivitiesTwoLinkedActivity() throws Exception {
		ImperativeProcessSchema schema = new ImperativeProcessSchema();
		ImperativeActivity activityA = schema.createActivity("A");
		ImperativeActivity activityB = schema.createActivity("B");
		schema.getStart().addSuccessor(activityA);
		activityA.addSuccessor(activityB);
		ImperativeProcessInstance instance = new ImperativeProcessInstance(schema);
		List<INodeInstance> activities = instance.getActiveActivities();
		Assert.assertEquals(1, activities.size());
	}

	@Test
	public void requestTermination() throws Exception {
		ImperativeProcessSchema schema = new ImperativeProcessSchema();
		ImperativeActivity activity = schema.createActivity("A");
		schema.getStart().addSuccessor(activity);
		activity.addSuccessor(schema.getEnd());
		ImperativeProcessInstance instance = new ImperativeProcessInstance(schema);

		assertEquals(false, instance.isTerminated());
		INodeInstance activityInstanceA = instance.getActiveActivities().get(0);
		activityInstanceA.launch();
		assertEquals(false, instance.isTerminated());
		activityInstanceA.complete();

		assertEquals(true, instance.isTerminated());
	}
}

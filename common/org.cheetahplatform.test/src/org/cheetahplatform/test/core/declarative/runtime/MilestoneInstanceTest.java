/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core.declarative.runtime;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.modeling.Milestone;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.test.modeler.TestLogListener;
import org.junit.Test;

public class MilestoneInstanceTest {
	@Test
	public void instantiate() throws Exception {
		Milestone milestone = new Milestone("A", new Duration(0, 0));
		DeclarativeProcessInstance instance = new DeclarativeProcessSchema().instantiate();

		TestLogListener listener = new TestLogListener();
		instance.addLogListener(listener, false);
		INodeInstance mileStoneInstance = milestone.instantiate(instance);
		List<AuditTrailEntry> entries = listener.getEntries();

		assertEquals(1, entries.size());
		assertEquals(mileStoneInstance.getCheetahId(), entries.get(0).getWorkflowModeleElementAsLong());
	}
}

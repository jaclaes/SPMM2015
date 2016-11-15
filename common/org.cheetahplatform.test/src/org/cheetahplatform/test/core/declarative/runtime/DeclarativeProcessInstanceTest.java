/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core.declarative.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.shared.CheetahConstants;
import org.cheetahplatform.test.modeler.TestLogListener;
import org.junit.Test;

public class DeclarativeProcessInstanceTest {

	@Test
	public void hasInstanceOf() throws Exception {
		DeclarativeProcessSchema schema = new DeclarativeProcessSchema();
		DeclarativeActivity activityA = schema.createActivity("A");
		DeclarativeActivity activityB = schema.createActivity("B");
		DeclarativeProcessInstance instance = schema.instantiate();

		assertFalse(instance.hasUncanceledInstanceOf(activityA));
		assertFalse(instance.hasUncanceledInstanceOf(activityB));

		activityA.instantiate(instance);

		assertTrue(instance.hasUncanceledInstanceOf(activityA));
		assertFalse(instance.hasUncanceledInstanceOf(activityB));

		activityB.instantiate(instance);

		assertTrue(instance.hasUncanceledInstanceOf(activityA));
		assertTrue(instance.hasUncanceledInstanceOf(activityB));
	}

	@Test
	public void logInstantiate() throws Exception {
		DeclarativeProcessSchema schema = new DeclarativeProcessSchema();
		DeclarativeProcessInstance instance = schema.instantiate();
		TestLogListener listener = new TestLogListener();
		instance.addLogListener(listener, true);

		assertEquals(1, listener.getEntries().size());
		AuditTrailEntry entry = listener.getEntries().get(0);
		assertEquals(schema.getCheetahId(), entry.getLongAttribute(CheetahConstants.SCHEMA));
	}
}

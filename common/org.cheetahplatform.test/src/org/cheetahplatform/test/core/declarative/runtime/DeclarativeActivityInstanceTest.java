/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core.declarative.runtime;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.shared.CheetahConstants;
import org.cheetahplatform.test.modeler.TestLogListener;
import org.junit.Test;

public class DeclarativeActivityInstanceTest {
	@Test
	public void instantiate() throws Exception {
		DeclarativeProcessSchema schema = new DeclarativeProcessSchema();
		DeclarativeActivity activity = schema.createActivity("A");
		DeclarativeProcessInstance instance = schema.instantiate();

		TestLogListener listener = new TestLogListener();
		instance.addLogListener(listener, false);
		activity.instantiate(instance);

		assertEquals(1, listener.getEntries().size());
		AuditTrailEntry entry = listener.getEntries().get(0);
		assertEquals(activity.getCheetahId(), entry.getLongAttribute(CheetahConstants.NODE));
		assertEquals(instance.getCheetahId(), entry.getLongAttribute(CheetahConstants.PROCESS_INSTANCE));
	}
}

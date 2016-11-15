/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.testarossa;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.testarossa.model.InstanceSelectionDialogModel;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

public class InstanceSelectionDialogModelTest {
	@Test
	public void instantiate() throws Exception {
		InstanceSelectionDialogModel model = new InstanceSelectionDialogModel();
		Role role = new Role("", new RGB(0, 0, 0));
		DeclarativeProcessInstance instance = model.instantiateProcess(new DeclarativeProcessSchema(), "A", Calendar.getInstance(),
				Calendar.getInstance(), role, role);
		DateTime startTime = instance.getStartTime();

		assertEquals(0, startTime.getHour());
		assertEquals(0, startTime.getMinute());
		assertEquals(0, startTime.getMinute());
		assertEquals(0, startTime.getMilliSeconds());
	}
}

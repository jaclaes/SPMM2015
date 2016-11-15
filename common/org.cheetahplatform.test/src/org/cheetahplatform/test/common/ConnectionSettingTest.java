/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.common;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.common.logging.db.ConnectionSetting;
import org.junit.Test;

public class ConnectionSettingTest {
	@Test
	public void getHost() throws Exception {
		ConnectionSetting setting = new ConnectionSetting("", "", "", "", "jdbc:mysql://138.232.65.123:13306/ibm_layout");
		assertEquals("138.232.65.123", setting.getHost());
	}

	@Test
	public void getPort() throws Exception {
		ConnectionSetting setting = new ConnectionSetting("", "", "", "", "jdbc:mysql://138.232.65.123:13306/ibm_layout");
		assertEquals("13306", setting.getPort());
	}
}

/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test;

import java.sql.SQLException;

import org.junit.Before;

public class DataBaseTest {
	@Before
	public void before() throws SQLException {
		TestHelper.setTestEnvironment();
	}

}

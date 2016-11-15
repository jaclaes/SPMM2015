/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import java.util.Locale;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class LocaleSensitiveTest {

	private static Locale defaultLocale;

	@BeforeClass
	public static void initalizeLocale() {
		defaultLocale = Locale.getDefault();
		Locale.setDefault(Locale.GERMAN);
	}

	@AfterClass
	public static void resetLocale() {
		Locale.setDefault(defaultLocale);
	}

	public LocaleSensitiveTest() {
		super();
	}

}
package org.cheetahplatform.experiment.editor;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.cheetahplatform.experiment.editor.messages"; //$NON-NLS-1$
	public static String UNTITLED;
	public static String ERROR_IN_CONFIG;
	public static String ERROR_IN_CONFIG_LONG;
	public static String UNABLE_TO_SAVE;
	public static String SAVE_FAILED;
	public static String PROCESS_NAME_EMPTY;
	public static String EMAIL_EMPTY;
	public static String DB_URI_EMPTY;
	public static String DB_USERNAME_EMPTY;
	public static String DB_PASSWD_EMPTY;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}

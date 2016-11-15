package org.cheetahplatform.modeler;

import java.util.Arrays;

import org.cheetahplatform.tdm.TDMConstants;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.app.CommandLineArgs;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.cheetahplatform.modeler";

	// The shared instance
	private static Activator plugin;

	private static boolean testEnvironment;

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public static boolean isTestEnvironment() {
		return testEnvironment;
	}

	public static void logError(String message, Throwable exception) {
		if (getDefault() == null) {
			System.err.println(message);
			if (exception != null) {
				exception.printStackTrace();
			}
		} else {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, exception);
			getDefault().getLog().log(status);
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		testEnvironment = Arrays.asList(CommandLineArgs.getAllArgs()).contains("-test");

		getPreferenceStore().setDefault(TDMConstants.KEY_SHOW_COMPLETED_ACTIVITIES, true);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
}

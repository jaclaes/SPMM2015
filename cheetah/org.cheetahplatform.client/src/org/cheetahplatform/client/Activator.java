package org.cheetahplatform.client;

import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
@SuppressWarnings("restriction")
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.cheetahplatform.client";

	// The shared instance
	private static Activator plugin;

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	@Override
	public void start(BundleContext context) throws Exception {
		WorkbenchPlugin.getDefault().getPreferenceStore().setValue(IPreferenceConstants.USE_32_THREADING, true);
		CheetahPlatformConfigurator.getInstance().set(IConfiguration.INITIAL_ACTIVITIY_SIZE, new Point(85, 63));
		CheetahPlatformConfigurator.getInstance().set(IConfiguration.INITIAL_GATEWAY_SIZE, new Point(42, 42));
		CheetahPlatformConfigurator.getInstance().set(IConfiguration.INITIAL_EVENT_SIZE, new Point(31, 31));
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
}

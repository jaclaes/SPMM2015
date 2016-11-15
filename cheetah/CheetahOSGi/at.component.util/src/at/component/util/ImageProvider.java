package at.component.util;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

public class ImageProvider {

	public static Image getGrayScaleImage(String path) {
		Bundle bundleOfImageProvider = Activator.getBundleContext().getBundle();
		return getGrayScaleImage(path, bundleOfImageProvider);
	}

	public static Image getGrayScaleImage(String path, Bundle bundle) {
		try {
			Image image = new Image(Display.getDefault(), bundle.getEntry(path).openStream());
			return new Image(Display.getDefault(), image, SWT.IMAGE_GRAY);
		} catch (IOException e) {
			throw new RuntimeException("ImageFile can't be located.");
		}
	}

	/**
	 * Returns the image located at the specified path, relative to the root of the bundle of the ImageProvider.
	 * 
	 * @param path
	 *            The path of the image relative to the root of the bundle of the ImageProvider
	 * @return The according image
	 * @throws IOException
	 *             if the file at the given path can't be read
	 */
	public static Image getImage(String path) {
		Bundle bundleOfImageProvider = Activator.getBundleContext().getBundle();
		return getImage(path, bundleOfImageProvider);
	}

	/**
	 * Returns the image located at the specified path, relative to the root of the given bundle.
	 * 
	 * @param path
	 *            The path of the image relative to the root of the given bundle
	 * @param bundle
	 *            The bundle
	 * @return The according image
	 * @throws IOException
	 *             if the file at the given path can't be read
	 */
	public static Image getImage(String path, Bundle bundle) {
		try {
			Image image = new Image(Display.getDefault(), bundle.getEntry(path).openStream());
			return image;
		} catch (IOException e) {
			throw new RuntimeException("ImageFile can't be located.");
		}
	}
}

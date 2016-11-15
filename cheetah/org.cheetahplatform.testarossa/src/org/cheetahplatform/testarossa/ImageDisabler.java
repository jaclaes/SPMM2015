package org.cheetahplatform.testarossa;

import java.net.URL;

import com.swtdesigner.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

/**
 * Really simple tool creating a disabled image from an existing one.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.07.2009
 */
public class ImageDisabler {
	private static final String FILENAME = "previous.gif";

	public static void main(String[] args) {
		String path = "img/";
		Image image = SWTResourceManager.getImage(path + FILENAME);

		URL location = ImageDisabler.class.getProtectionDomain().getCodeSource().getLocation();
		String disabledPath = location.getFile() + "../img/";

		Image disabledImage = new Image(Display.getDefault(), image, SWT.IMAGE_GRAY);
		String disabledFilename = disabledPath + FILENAME.substring(0, FILENAME.lastIndexOf('.')) + "_disabled."
				+ FILENAME.substring(FILENAME.lastIndexOf('.') + 1);

		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[] { disabledImage.getImageData() };
		imageLoader.save(disabledFilename, SWT.IMAGE_GIF);
	}
}

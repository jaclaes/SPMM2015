package org.cheetahplatform.modeler.understandability;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ImageView extends ViewPart {

	public static final String ID = "org.cheetahplatform.modeler.understandability.ImageView";

	private ImageViewComposite composite;

	@Override
	public void createPartControl(Composite parent) {
		composite = new ImageViewComposite(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {
		// ignore
	}

	/**
	 * Sets the view's image. The image should not be disposed afterwards.
	 * 
	 * @param image
	 *            the image
	 */
	public void setImage(Image image) {
		composite.getImageLabel().setImage(image);
		composite.layout(true, true);
	}

}

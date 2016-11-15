package org.cheetahplatform.common.ui.composite;

import org.cheetahplatform.common.CommonConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Jakob Pinggera <br>
 *         Stefan Zugal
 */
public class MultiLineButton extends Composite {
	private class BorderPaintListener implements PaintListener {
		@Override
		public void paintControl(PaintEvent e) {
			GC gc = e.gc;
			gc.setForeground(CommonConstants.DIVIDER_COLOR);
			Rectangle bounds = getBounds();
			e.gc.setForeground(CommonConstants.DIVIDER_COLOR);
			gc.drawRectangle(0, 0, bounds.width - 1, bounds.height - 1);
		}
	}

	/**
	 * @author Jakob Pinggera <br>
	 *         Stefan Zugal
	 */
	private class ChangeBackgroundListener extends MouseTrackAdapter {
		@Override
		public void mouseEnter(MouseEvent e) {
			setBackground(CommonConstants.SELECTION_COLOR);
			addPaintListener(borderPaintListener);
		}

		@Override
		public void mouseExit(MouseEvent e) {
			setBackground(CommonConstants.BACKGROUND_COLOR);
			removePaintListener(borderPaintListener);
		}
	}

	private class SelectionMouseListener extends MouseAdapter {
		private final SelectionListener listener;

		public SelectionMouseListener(SelectionListener listener) {
			this.listener = listener;

		}

		@Override
		public void mouseUp(MouseEvent e) {
			listener.widgetSelected(null);
		}
	}

	private Label imageLabel;
	private Label textLabel;
	private BorderPaintListener borderPaintListener;
	private final Image image;
	private Image disabledImage;

	public MultiLineButton(Composite parent, int style, String text, Image image) {
		super(parent, style);

		borderPaintListener = new BorderPaintListener();
		this.image = image;
		this.disabledImage = new Image(parent.getDisplay(), image, SWT.IMAGE_DISABLE);

		setLayout(new GridLayout());
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		imageLabel = new Label(this, SWT.NONE);
		imageLabel.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		imageLabel.setImage(image);
		textLabel = new Label(this, SWT.NONE);
		textLabel.setText(text);
		textLabel.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		addMouseTrackListener(new ChangeBackgroundListener());
		textLabel.addMouseTrackListener(new ChangeBackgroundListener());
		imageLabel.addMouseTrackListener(new ChangeBackgroundListener());
	}

	public void addSelectionListener(SelectionListener listener) {
		addMouseListener(new SelectionMouseListener(listener));
		textLabel.addMouseListener(new SelectionMouseListener(listener));
		imageLabel.addMouseListener(new SelectionMouseListener(listener));
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		if (enabled) {
			imageLabel.setImage(image);
		} else {
			imageLabel.setImage(disabledImage);
		}
	}
}

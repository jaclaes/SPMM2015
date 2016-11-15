package org.cheetahplatform.common.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.swtdesigner.SWTResourceManager;

/**
 * @author Jakob Pinggera <br>
 *         Stefan Zugal
 */
public class PlainMultiLineButton extends Composite {
	private class BorderPaintListener implements PaintListener {
		@Override
		public void paintControl(PaintEvent e) {
			if (!enabled) {
				return;
			}

			GC gc = e.gc;
			gc.setForeground(DIVIDER_COLOR);
			Rectangle bounds = getBounds();
			e.gc.setForeground(DIVIDER_COLOR);
			gc.drawRectangle(0, 0, bounds.width - 1, bounds.height - 1);
		}
	}

	/**
	 * @author Jakob Pinggera <br>
	 *         Stefan Zugal
	 */
	private class ChangeBackgroundListener implements MouseTrackListener {
		@Override
		public void mouseEnter(MouseEvent e) {
			if (!enabled) {
				return;
			}
			setBackground(SELECTION_COLOR);
			addPaintListener(borderPaintListener);
		}

		@Override
		public void mouseExit(MouseEvent e) {
			setBackground(BACKGROUND_COLOR);
			removePaintListener(borderPaintListener);
		}

		@Override
		public void mouseHover(MouseEvent e) {
			// do nothing
		}
	}

	private class SelectionMouseListener extends MouseAdapter {
		private final SelectionListener listener;

		public SelectionMouseListener(SelectionListener listener) {
			this.listener = listener;
		}

		@Override
		public void mouseUp(MouseEvent e) {
			if (!enabled) {
				return;
			}
			listener.widgetSelected(null);
		}
	}

	private static final Color BACKGROUND_COLOR = SWTResourceManager.getColor(255, 255, 255);

	private static final Color DIVIDER_COLOR = SWTResourceManager.getColor(180, 180, 180);

	private static final Color SELECTION_COLOR = SWTResourceManager.getColor(233, 233, 233);

	private Label imageLabel;
	private Label textLabel;
	private BorderPaintListener borderPaintListener;
	private boolean enabled;

	private final Image image;

	private final Image disabledImage;

	public PlainMultiLineButton(Composite parent, int style, String text, Image image, Image disabledImage) {
		super(parent, SWT.NONE);
		this.image = image;
		this.disabledImage = disabledImage;

		borderPaintListener = new BorderPaintListener();
		GridLayout layout = new GridLayout();
		setLayout(layout);
		if ((style & SWT.HORIZONTAL) != 0) {
			layout.numColumns++;
		}

		setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		imageLabel = new Label(this, SWT.NONE);
		boolean imageLabelGrabHorizontal = true;
		int textLabelStyle = SWT.CENTER;

		if ((style & SWT.HORIZONTAL) != 0) {
			imageLabelGrabHorizontal = false;
			textLabelStyle = SWT.LEFT;
		}

		enabled = true;

		GridData imageLayoutData = new GridData(SWT.CENTER, SWT.BOTTOM, imageLabelGrabHorizontal, false);
		imageLabel.setLayoutData(imageLayoutData);
		imageLabel.setImage(image);

		textLabel = new Label(this, SWT.WRAP);
		textLabel.setText(text);
		textLabel.setAlignment(SWT.CENTER);
		GridData textLayoutData = new GridData(textLabelStyle, SWT.FILL, true, false);
		textLabel.setLayoutData(textLayoutData);
		if (text == null || text.trim().isEmpty()) {
			textLayoutData.exclude = true;
			textLabel.setVisible(false);
		}

		addMouseTrackListener(new ChangeBackgroundListener());
		textLabel.addMouseTrackListener(new ChangeBackgroundListener());
		imageLabel.addMouseTrackListener(new ChangeBackgroundListener());
		setBackground(BACKGROUND_COLOR);
	}

	public void addSelectionListener(SelectionListener listener) {
		addMouseListener(new SelectionMouseListener(listener));
		textLabel.addMouseListener(new SelectionMouseListener(listener));
		imageLabel.addMouseListener(new SelectionMouseListener(listener));
	}

	@Override
	public void setBackground(Color color) {
		super.setBackground(color);

		imageLabel.setBackground(color);
		textLabel.setBackground(color);
	}

	@Override
	public void setBackgroundImage(Image image) {
		imageLabel.setImage(image);
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			imageLabel.setImage(image);
		} else {
			if (disabledImage != null) {
				imageLabel.setImage(disabledImage);
			}
		}
	}

	@Override
	public void setFont(Font font) {
		textLabel.setFont(font);
	}
}

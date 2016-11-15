package org.cheetahplatform.modeler.generic.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

public class MultiLineLabel extends Label {

	private int alignment;

	public MultiLineLabel() {
		this("", SWT.CENTER);
	}

	public MultiLineLabel(String label) {
		this(label, SWT.CENTER);
	}

	public MultiLineLabel(String label, int alignment) {
		super(label);

		this.alignment = alignment;
	}

	@Override
	protected Dimension calculateTextSize() {
		return super.calculateTextSize().expand(4, 4);
	}

	@Override
	public Font getFont() {
		Font font = super.getFont();
		if (font == null) {
			return Display.getCurrent().getSystemFont();
		}
		return font;
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		if (isOpaque())
			super.paintFigure(graphics);

		Rectangle bounds = getBounds().getCopy();
		if (getIcon() != null)
			graphics.drawImage(getIcon(), getIconLocation());
		if (!isEnabled()) {
			graphics.translate(1, 1);
			graphics.setForegroundColor(ColorConstants.buttonLightest);
			graphics.drawText(getSubStringText(), getTextLocation());
			graphics.translate(-1, -1);
			graphics.setForegroundColor(ColorConstants.buttonDarker);
		}

		TextLayout layout = new TextLayout(Display.getDefault());
		layout.setWidth(Math.max(bounds.width - 4, 1));
		layout.setAlignment(alignment);
		layout.setFont(getFont());
		String text = getText();

		if (text == null) {
			text = "";
		}
		layout.setText(text);
		int lines = layout.getLineCount();
		int height = 0;

		for (int i = 0; i < lines; i++) {
			org.eclipse.swt.graphics.Rectangle lineBounds = layout.getLineBounds(i);
			height += lineBounds.height;
		}

		int freeSpace = Math.max(bounds.height - height, 0) / 2;
		graphics.drawTextLayout(layout, bounds.x + 2, bounds.y + freeSpace);
		layout.dispose();
	}

	/**
	 * @param alignment
	 *            the alignment to set
	 */
	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}
}
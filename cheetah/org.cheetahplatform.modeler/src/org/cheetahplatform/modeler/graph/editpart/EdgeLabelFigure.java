package org.cheetahplatform.modeler.graph.editpart;

import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.generic.figure.MultiLineLabel;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         10.06.2010
 */
public class EdgeLabelFigure extends Figure {
	private MultiLineLabel label;

	public EdgeLabelFigure() {
		setLayoutManager(new GridLayout());
		label = new MultiLineLabel();
		label.setAlignment(SWT.LEFT);
		add(label, new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension size = super.getPreferredSize(wHint, hHint).getCopy();
		int width = size.width;
		int maxWidth = ModelerConstants.MAXIMUM_EDGE_LABEL_WIDTH;
		size.width = Math.min(size.width, maxWidth);
		size.height = 10 + ((width / maxWidth) + 1) * 20;
		return size;
	}

	/**
	 * @param text
	 */
	public void setText(String text) {
		label.setText(text);
	}
}
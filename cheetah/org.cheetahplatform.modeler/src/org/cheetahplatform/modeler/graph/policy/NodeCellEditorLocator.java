package org.cheetahplatform.modeler.graph.policy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

public class NodeCellEditorLocator implements CellEditorLocator {

	private final IFigure figure;

	public NodeCellEditorLocator(IFigure figure) {
		this.figure = figure;
	}

	@Override
	public void relocate(CellEditor celleditor) {
		Text text = (Text) celleditor.getControl();
		Rectangle figureBounds = figure.getBounds();

		org.eclipse.swt.graphics.Rectangle bounds = new org.eclipse.swt.graphics.Rectangle(figureBounds.x + 7, figureBounds.y + 7,
				figureBounds.width - 15, figureBounds.height - 15);
		text.setBounds(bounds);
	}
}

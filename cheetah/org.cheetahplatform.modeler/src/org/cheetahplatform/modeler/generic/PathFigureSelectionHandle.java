/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.modeler.generic;

import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.modeler.generic.figure.IPathFigure;
import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

/**
 * Selection handle for activities which draws a black border.
 * 
 * @author Stefan Zugal
 * 
 */
public class PathFigureSelectionHandle extends MoveHandle {
	private class SelectionBorder extends AbstractBorder {
		private final Color borderColor = SWTResourceManager.getColor(0, 0, 0);

		@Override
		public Insets getInsets(IFigure figure) {
			return IFigure.NO_INSETS;
		}

		@Override
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			IPathFigure activityFigure = (IPathFigure) (getOwner()).getFigure();
			Path path = activityFigure.computePath();

			graphics.setAntialias(SWT.ON);
			graphics.setForegroundColor(borderColor);
			graphics.setLineWidth(2);
			graphics.drawPath(path);
			path.dispose();
		}
	}

	@SuppressWarnings("unchecked")
	public PathFigureSelectionHandle(GraphicalEditPart editPart) {
		super(editPart);

		editPart.getViewer().getVisualPartMap().put(this, editPart);
	}

	@Override
	protected void initialize() {
		setOpaque(false);
		setBorder(new SelectionBorder());
	}
}

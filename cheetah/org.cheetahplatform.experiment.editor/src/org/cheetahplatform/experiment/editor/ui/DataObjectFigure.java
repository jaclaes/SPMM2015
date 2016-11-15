package org.cheetahplatform.experiment.editor.ui;

import org.cheetahplatform.modeler.generic.figure.GenericActivityFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

public class DataObjectFigure extends GenericActivityFigure {

	public DataObjectFigure(String label) {
		super(label);
	}

	@Override
	protected Path computeFigurePath(Point startLocation, Insets insets) {
		Rectangle bounds = this.bounds.getCopy();
		bounds.x = startLocation.x;
		bounds.y = startLocation.y;
		bounds.y += insets.top;
		bounds.x += insets.left;
		bounds.height -= insets.bottom;
		bounds.width -= insets.right;
		
//		int triHeight = bounds.height / 5;
//		int triWidth = bounds.width / 5;
//		
		int width = Math.min(bounds.height / 4, bounds.width / 4);

		Path path = new Path(Display.getDefault());
		
		path.moveTo(bounds.x + bounds.width - 2 , bounds.y + width);
		path.lineTo(bounds.x + bounds.width - 2 - width, bounds.y + width);
		path.lineTo(bounds.x + bounds.width - 2 - width, bounds.y);
		path.lineTo(bounds.x + bounds.width - 2 , bounds.y + width);
		path.lineTo(bounds.x + bounds.width - 2, bounds.y + bounds.height - 2);
		path.lineTo(bounds.x , bounds.y + bounds.height - 2);
		path.lineTo(bounds.x , bounds.y);
		path.lineTo(bounds.x + bounds.width - 2 - width , bounds.y);
		
		
		
		return path;
	}
	
	

}

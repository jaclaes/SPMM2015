package org.cheetahplatform.common.ui.gef;

import java.util.Collection;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.widgets.Control;

public class ScrollingGraphicalViewerWithForeignSupport extends ScrollingGraphicalViewer {

	public static final String GRAPHICAL_VIEWER = "GRAPHICAL_VIEWER";

	@SuppressWarnings("rawtypes")
	@Override
	public EditPart findObjectAtExcluding(Point pt, Collection exclude, Conditional condition) {
		org.eclipse.swt.graphics.Point searchedLocation = getControl().toDisplay(new org.eclipse.swt.graphics.Point(pt.x, pt.y));
		org.eclipse.swt.graphics.Point cursorLocation = getControl().getDisplay().getCursorLocation();

		if (searchedLocation.equals(cursorLocation)) {
			Control cursorControl = getControl().getDisplay().getCursorControl();
			if (!getControl().equals(cursorControl) && cursorControl != null) {
				ScrollingGraphicalViewer foreignViewer = (ScrollingGraphicalViewer) cursorControl.getData(GRAPHICAL_VIEWER);
				if (foreignViewer != null) {
					org.eclipse.swt.graphics.Point absoluteLocation = getControl().toDisplay(pt.getSWTPoint());
					org.eclipse.swt.graphics.Point translatedLocation = cursorControl.toControl(absoluteLocation);
					return foreignViewer.findObjectAt(new Point(translatedLocation));
				}
			}
		}

		return super.findObjectAtExcluding(pt, exclude, condition);
	}

	@Override
	public void setEditDomain(EditDomain domain) {
		super.setEditDomain(domain);

		getLightweightSystem().setEventDispatcher(new CustomEventDispatcher(domain, this));
	}
}

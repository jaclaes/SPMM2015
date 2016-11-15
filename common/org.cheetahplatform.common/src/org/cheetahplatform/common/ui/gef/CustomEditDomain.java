/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.common.ui.gef;

import static org.cheetahplatform.common.CommonConstants.KEY_MOUSE_LOCATION;
import static org.cheetahplatform.common.CommonConstants.KEY_VIEWER;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.Activator;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.gef.tools.TargetingTool;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class CustomEditDomain extends EditDomain {
	private MouseEvent lastMouseDown;
	private MouseEvent currentMouseDown;
	private Map<Integer, IKeyAction> actions;
	private boolean editable;
	private boolean navigatable; // used for hierarchical models. can be used to make a model non editable but still navigatable
	private boolean directEditingEnabled;

	public CustomEditDomain() {
		setActiveTool(new CustomSelectionTool());

		this.editable = true;
		this.navigatable = true;
		this.directEditingEnabled = true;

		actions = new HashMap<Integer, IKeyAction>();
		actions.put((int) SWT.DEL, new IKeyAction() {
			@SuppressWarnings("unchecked")
			@Override
			public void run(EditPartViewer viewer) {
				List<Object> parts = new ArrayList<Object>(viewer.getSelectedEditParts());
				for (Object object : parts) {
					Request request = new Request(RequestConstants.REQ_DELETE);
					getCommandStack().execute(((EditPart) object).getCommand(request));
				}
			}
		});

		setActiveTool(new CustomSelectionTool());
	}

	@SuppressWarnings("unchecked")
	private Point adaptEventToForeignViewerIfNecessary(int x, int y, EditPartViewer viewer) {
		try {
			ScrollingGraphicalViewer foreignViewer = getForeingViewer(viewer);
			Method getTrackerMethod = SelectionTool.class.getDeclaredMethod("getDragTracker", new Class[0]);
			getTrackerMethod.setAccessible(true);
			Object tracker = null;
			if (getActiveTool() instanceof SelectionTool) {
				tracker = getTrackerMethod.invoke(getActiveTool());
			}

			Method getTargetRequest = TargetingTool.class.getDeclaredMethod("getTargetRequest", new Class[0]);
			getTargetRequest.setAccessible(true);

			Request toolRequest = null;
			if (getActiveTool() instanceof TargetingTool) {
				toolRequest = (Request) getTargetRequest.invoke(getActiveTool());
			}

			Request trackerRequest = null;
			if (tracker instanceof TargetingTool) {
				trackerRequest = (Request) getTargetRequest.invoke(tracker);
				initializeRequest(viewer, trackerRequest);
			}

			initializeRequest(viewer, toolRequest);
			if (foreignViewer == null || (trackerRequest == null && toolRequest == null)) {
				return new Point(x, y); // no further adaptations required
			}

			Point absoluteLocation = viewer.getControl().toDisplay(x, y);
			Point relativeLocation = foreignViewer.getControl().toControl(absoluteLocation);

			if (tracker != null) {
				if (trackerRequest.getExtendedData() != null) {
					trackerRequest.getExtendedData().put(KEY_MOUSE_LOCATION, new org.eclipse.draw2d.geometry.Point(relativeLocation));
					trackerRequest.getExtendedData().put(KEY_VIEWER, foreignViewer);
				}

				if (trackerRequest instanceof LocationRequest) {
					((LocationRequest) trackerRequest).setLocation(new org.eclipse.draw2d.geometry.Point(relativeLocation));
				}
				if (trackerRequest instanceof ChangeBoundsRequest) {
					((ChangeBoundsRequest) trackerRequest).setLocation(new org.eclipse.draw2d.geometry.Point(relativeLocation));
				}
			}

			if (toolRequest instanceof LocationRequest) {
				((LocationRequest) toolRequest).setLocation(new org.eclipse.draw2d.geometry.Point(relativeLocation));
			}

			toolRequest.getExtendedData().put(KEY_VIEWER, foreignViewer);
			toolRequest.getExtendedData().put(KEY_MOUSE_LOCATION, new org.eclipse.draw2d.geometry.Point(relativeLocation));
			return relativeLocation;
		} catch (Exception e) {
			Activator.logError("Could not adapt the mouse location.", e);
		}

		return new Point(x, y);
	}

	private void adaptEventToForeignViewerIfNecessary(MouseEvent mouseEvent, EditPartViewer viewer) {
		adaptEventToForeignViewerIfNecessary(mouseEvent.x, mouseEvent.y, viewer);
	}

	/**
	 * @return the currentMouseDown
	 */
	public MouseEvent getCurrentMouseDown() {
		return currentMouseDown;
	}

	private ScrollingGraphicalViewer getForeingViewer(EditPartViewer viewer) {
		Control cursorControl = Display.getDefault().getCursorControl();
		if (!viewer.getControl().equals(cursorControl) && cursorControl != null) {
			ScrollingGraphicalViewer foreignViewer = (ScrollingGraphicalViewer) cursorControl
					.getData(ScrollingGraphicalViewerWithForeignSupport.GRAPHICAL_VIEWER);
			return foreignViewer;
		}

		return null;
	}

	/**
	 * Return the lastMouseDown.
	 * 
	 * @return the lastMouseDown
	 */
	public MouseEvent getLastMouseDown() {
		return lastMouseDown;
	}

	@SuppressWarnings({ "unchecked", "serial" })
	private void initializeRequest(EditPartViewer viewer, Request trackerRequest) {
		if (trackerRequest == null) {
			return;
		}

		trackerRequest.getExtendedData().put(KEY_VIEWER, viewer);

		// otherwise the extended data is cleared by the tool/edit part tracker
		trackerRequest.setExtendedData(new HashMap<Object, Object>(trackerRequest.getExtendedData()) {
			@Override
			public void clear() {
				// ignore
			}

		});
	}

	/**
	 * Returns the directEditingEnabled.
	 * 
	 * @return the directEditingEnabled
	 */
	public boolean isDirectEditingEnabled() {
		if (!editable) {
			return false;
		}
		return directEditingEnabled;
	}

	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}

	public boolean isNavigatable() {
		return navigatable;
	}

	@Override
	public void keyDown(KeyEvent keyEvent, EditPartViewer viewer) {
		if (!editable) {
			return;
		}

		super.keyDown(keyEvent, viewer);

		IKeyAction action = actions.get(keyEvent.keyCode);
		if (action != null) {
			action.run(viewer);
		}
	}

	@Override
	public void keyTraversed(TraverseEvent traverseEvent, EditPartViewer viewer) {
		if (editable) {
			super.keyTraversed(traverseEvent, viewer);
		}
	}

	@Override
	public void keyUp(KeyEvent keyEvent, EditPartViewer viewer) {
		if (editable) {
			super.keyUp(keyEvent, viewer);
		}
	}

	@Override
	public void mouseDoubleClick(MouseEvent mouseEvent, EditPartViewer viewer) {
		adaptEventToForeignViewerIfNecessary(mouseEvent, viewer);

		if (editable || navigatable) {
			super.mouseDoubleClick(mouseEvent, viewer);
		}
	}

	@Override
	public void mouseDown(MouseEvent mouseEvent, EditPartViewer viewer) {
		currentMouseDown = mouseEvent;
		adaptEventToForeignViewerIfNecessary(mouseEvent, viewer);
		super.mouseDown(mouseEvent, viewer);

		lastMouseDown = mouseEvent;
	}

	@Override
	public void mouseDrag(MouseEvent mouseEvent, EditPartViewer viewer) {
		adaptEventToForeignViewerIfNecessary(mouseEvent, viewer);
		ISelection selection = viewer.getSelection();

		// not too nice, but I really want to keep this class in the commons package... (and there is no GraphEditPart)
		if (((StructuredSelection) selection).getFirstElement().getClass().getSimpleName().equals("GraphEditPart")) {
			super.mouseDrag(mouseEvent, viewer);
			return;
		}

		if (editable) {
			super.mouseDrag(mouseEvent, viewer);
		}
	}

	@Override
	public void mouseMove(MouseEvent mouseEvent, EditPartViewer viewer) {
		adaptEventToForeignViewerIfNecessary(mouseEvent, viewer);

		super.mouseMove(mouseEvent, viewer);
	}

	@Override
	public void mouseUp(MouseEvent mouseEvent, EditPartViewer viewer) {
		adaptEventToForeignViewerIfNecessary(mouseEvent, viewer);
		super.mouseUp(mouseEvent, viewer);
	}

	@Override
	public void nativeDragFinished(DragSourceEvent event, EditPartViewer viewer) {
		adaptEventToForeignViewerIfNecessary(event.x, event.y, viewer);

		if (editable) {
			super.nativeDragFinished(event, viewer);
		}
	}

	@Override
	public void nativeDragStarted(DragSourceEvent event, EditPartViewer viewer) {
		adaptEventToForeignViewerIfNecessary(event.x, event.y, viewer);

		if (editable) {
			super.nativeDragStarted(event, viewer);
		}
	}

	public void registerAction(char key, IKeyAction action) {
		actions.put((int) key, action);
	}

	/**
	 * Sets the directEditingEnabled.
	 * 
	 * @param directEditingEnabled
	 *            the directEditingEnabled to set
	 */
	public void setDirectEditingEnabled(boolean directEditingEnabled) {
		this.directEditingEnabled = directEditingEnabled;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setNavigatable(boolean navigatable) {
		this.navigatable = navigatable;
	}

	public void unregisterAction(char key) {
		actions.put((int) key, null);
	}

}

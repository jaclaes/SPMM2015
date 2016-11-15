package at.component.group.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.osgi.framework.BundleException;

import at.component.ComponentConstants;
import at.component.IComponent;
import at.component.framework.services.componentservice.IComponentService;
import at.component.group.Activator;

public class ComponentDecorationCompositeListener implements Listener {

	public static final int RESIZE_WIDTH = 2;
	public static final int EDGE_WIDTH = 5;

	private IComponentService componentService;
	private IComponent component;
	private Point originalMousePosition;

	private Cursor eastCursor = new Cursor(Display.getDefault(),
			SWT.CURSOR_SIZEE);
	private Cursor westCursor = new Cursor(Display.getDefault(),
			SWT.CURSOR_SIZEW);
	private Cursor arrowCursor = new Cursor(Display.getDefault(),
			SWT.CURSOR_ARROW);
	private Cursor northCursor = new Cursor(Display.getDefault(),
			SWT.CURSOR_SIZEN);
	private Cursor southCursor = new Cursor(Display.getDefault(),
			SWT.CURSOR_SIZES);
	private Cursor southEastCursor = new Cursor(Display.getDefault(),
			SWT.CURSOR_SIZESE);
	private Cursor northEastCursor = new Cursor(Display.getDefault(),
			SWT.CURSOR_SIZENE);
	private Cursor northWestCursor = new Cursor(Display.getDefault(),
			SWT.CURSOR_SIZENW);
	private Cursor southWestCursor = new Cursor(Display.getDefault(),
			SWT.CURSOR_SIZESW);

	private boolean westCursorSet;
	private boolean eastCursorSet;
	private boolean northCursorSet;
	private boolean southCursorSet;
	private boolean southEastCursorSet;
	private boolean northEastCursorSet;
	private boolean northWestCursorSet;
	private boolean southWestCursorSet;

	public ComponentDecorationCompositeListener(IComponent component) {
		this.component = component;
		componentService = Activator.getComponentService();
	}

	private void changeCursorAndResize(Event event) {
		Composite componentDecorationComposite = (Composite) event.widget;
		Rectangle bounds = componentDecorationComposite.getBounds();

		setCursor(event, componentDecorationComposite, bounds);

		if (resizeComponentDecorationComposite(event, componentDecorationComposite))
			componentService.getActiveProject().setDirty(true);
	}

	@Override
	public void handleEvent(Event event) {
		if (event.type == SWT.Dispose)
			widgetDisposed(event);

		if (event.type == SWT.MouseMove)
			mouseMove(event);

		if (event.type == SWT.Paint)
			paintControl(event);

		if (event.type == SWT.MouseDown)
			mouseDown(event);

		if (event.type == SWT.MouseUp)
			mouseUp(event);

		if (event.type == SWT.MouseExit)
			mouseExit(event);
	}

	private void mouseDown(Event event) {
		originalMousePosition = new Point(event.x, event.y);
	}

	private void mouseExit(Event event) {
		if (event.widget instanceof Composite) {
			((Composite) event.widget).setCursor(arrowCursor);
			eastCursorSet = false;
			westCursorSet = false;
			northCursorSet = false;
			southCursorSet = false;
			southEastCursorSet = false;
			northEastCursorSet = false;
			northWestCursorSet = false;
			southWestCursorSet = false;
		}
	}

	private void mouseMove(Event event) {
		if (event.widget instanceof Composite) {
			changeCursorAndResize(event);
		}
	}

	private void mouseUp(Event event) {
		if (event.widget instanceof Composite) {
			originalMousePosition = null;
			westCursorSet = false;
			eastCursorSet = false;
			northCursorSet = false;
			southCursorSet = false;
			southEastCursorSet = false;
			northEastCursorSet = false;
			northWestCursorSet = false;
			southWestCursorSet = false;
		}
	}

	private void paintControl(Event event) {
		if (event.widget instanceof Composite) {
			GC gc = event.gc;
			gc.setForeground(Display.getDefault().getSystemColor(
					SWT.COLOR_DARK_BLUE));
			Rectangle bounds = ((Composite) event.widget).getBounds();
			gc.drawRectangle(new Rectangle(0, 0, bounds.width - 1,
					bounds.height - 1));
		}
	}

	/**
	 * Resizes the given composite.
	 * 
	 * @param event
	 *            The event which holds necessary information
	 * @param componentDecorationComposite
	 *            The composite which should be resized.
	 * 
	 * @return true if the composite was resized
	 */
	private boolean resizeComponentDecorationComposite(Event event,
			Composite composite) {
		if (originalMousePosition != null) {
			Point parentLocation = composite.getLocation();
			Point newMousePosition = new Point(event.x, event.y);
			int xDifference = newMousePosition.x - originalMousePosition.x;
			int yDifference = newMousePosition.y - originalMousePosition.y;
			Rectangle bounds = composite.getBounds();

			if (westCursorSet) {
				int newParentLocationX = parentLocation.x + xDifference;
				int newParentLocationY = parentLocation.y;

				if (newParentLocationX < 0)
					return false;

				int newWidth = bounds.width - xDifference;

				if (newWidth < ComponentConstants.MIN_COMPONENT_DECORATION_WIDTH) {
					newWidth = ComponentConstants.MIN_COMPONENT_DECORATION_WIDTH;
					newParentLocationX = parentLocation.x;
				}

				if (newWidth != bounds.width) {
					composite.setBounds(newParentLocationX, newParentLocationY,
							newWidth, bounds.height);
					return true;
				}
			} else if (eastCursorSet) {
				int newParentLocationX = parentLocation.x;
				int newParentLocationY = parentLocation.y;

				if (newParentLocationX < 0)
					return false;

				int newWidth = bounds.width + xDifference;

				if (newWidth < ComponentConstants.MIN_COMPONENT_DECORATION_WIDTH)
					newWidth = ComponentConstants.MIN_COMPONENT_DECORATION_WIDTH;

				if (newWidth != bounds.width) {
					composite.setBounds(newParentLocationX, newParentLocationY,
							newWidth, bounds.height);
					originalMousePosition = newMousePosition;
					return true;
				}
			} else if (northCursorSet) {
				int newParentLocationX = parentLocation.x;
				int newParentLocationY = parentLocation.y + yDifference;

				if (newParentLocationY < 0)
					return false;

				int newHeight = bounds.height - yDifference;

				if (newHeight < ComponentConstants.MIN_COMPONENT_DECORATION_HEIGHT) {
					newHeight = ComponentConstants.MIN_COMPONENT_DECORATION_HEIGHT;
					newParentLocationY = parentLocation.y;
				}

				if (newHeight != bounds.height) {
					composite.setBounds(newParentLocationX, newParentLocationY,
							bounds.width, newHeight);
					return true;
				}
			} else if (southCursorSet) {
				int newParentLocationX = parentLocation.x;
				int newParentLocationY = parentLocation.y;

				if (newParentLocationY < 0)
					return false;

				int newHeight = bounds.height + yDifference;

				if (newHeight < ComponentConstants.MIN_COMPONENT_DECORATION_HEIGHT)
					newHeight = ComponentConstants.MIN_COMPONENT_DECORATION_HEIGHT;

				if (newHeight != bounds.height) {
					composite.setBounds(newParentLocationX, newParentLocationY,
							bounds.width, newHeight);
					originalMousePosition = newMousePosition;
					return true;
				}
			} else if (southEastCursorSet) {
				int newParentLocationX = parentLocation.x;
				int newParentLocationY = parentLocation.y;

				if (newParentLocationY < 0 && newParentLocationX < 0)
					return false;

				int newWidth = bounds.width + xDifference;
				int newHeight = bounds.height + yDifference;

				if (newWidth < ComponentConstants.MIN_COMPONENT_DECORATION_WIDTH)
					newWidth = ComponentConstants.MIN_COMPONENT_DECORATION_WIDTH;
				if (newHeight < ComponentConstants.MIN_COMPONENT_DECORATION_HEIGHT)
					newHeight = ComponentConstants.MIN_COMPONENT_DECORATION_HEIGHT;

				if (newWidth != bounds.width || newHeight != bounds.height) {
					composite.setBounds(newParentLocationX, newParentLocationY,
							newWidth, newHeight);
					originalMousePosition = new Point(newMousePosition.x,
							newMousePosition.y);
					return true;
				}

			} else if (northEastCursorSet) {
				int newParentLocationX = parentLocation.x;
				int newParentLocationY = parentLocation.y + yDifference;

				if (newParentLocationY < 0 && newParentLocationX < 0)
					return false;

				int newWidth = bounds.width + xDifference;
				int newHeight = bounds.height - yDifference;

				if (newWidth < ComponentConstants.MIN_COMPONENT_DECORATION_WIDTH)
					newWidth = ComponentConstants.MIN_COMPONENT_DECORATION_WIDTH;
				if (newHeight < ComponentConstants.MIN_COMPONENT_DECORATION_HEIGHT) {
					newHeight = ComponentConstants.MIN_COMPONENT_DECORATION_HEIGHT;
					newParentLocationY = parentLocation.y;
				}

				if (newWidth != bounds.width || newHeight != bounds.height) {
					composite.setBounds(newParentLocationX, newParentLocationY,
							newWidth, newHeight);

					if (newWidth != bounds.width) {
						originalMousePosition = new Point(newMousePosition.x,
								originalMousePosition.y);
					}

					return true;
				}

			} else if (northWestCursorSet) {
				int newParentLocationX = parentLocation.x + xDifference;
				int newParentLocationY = parentLocation.y + yDifference;

				if (newParentLocationY < 0 && newParentLocationX < 0)
					return false;

				int newWidth = bounds.width - xDifference;
				int newHeight = bounds.height - yDifference;

				if (newWidth < ComponentConstants.MIN_COMPONENT_DECORATION_WIDTH) {
					newWidth = ComponentConstants.MIN_COMPONENT_DECORATION_WIDTH;
					newParentLocationX = parentLocation.x;
				}
				if (newHeight < ComponentConstants.MIN_COMPONENT_DECORATION_HEIGHT) {
					newHeight = ComponentConstants.MIN_COMPONENT_DECORATION_HEIGHT;
					newParentLocationY = parentLocation.y;
				}

				if (newWidth != bounds.width || newHeight != bounds.height) {
					composite.setBounds(newParentLocationX, newParentLocationY,
							newWidth, newHeight);
					return true;
				}
			} else { // southWestCursorSet
				int newParentLocationX = parentLocation.x + xDifference;
				int newParentLocationY = parentLocation.y;

				if (newParentLocationY < 0 && newParentLocationX < 0)
					return false;

				int newWidth = bounds.width - xDifference;
				int newHeight = bounds.height + yDifference;

				if (newWidth < ComponentConstants.MIN_COMPONENT_DECORATION_WIDTH) {
					newWidth = ComponentConstants.MIN_COMPONENT_DECORATION_WIDTH;
					newParentLocationX = parentLocation.x;
				}
				if (newHeight < ComponentConstants.MIN_COMPONENT_DECORATION_HEIGHT)
					newHeight = ComponentConstants.MIN_COMPONENT_DECORATION_HEIGHT;

				if (newWidth != bounds.width || newHeight != bounds.height) {
					composite.setBounds(newParentLocationX, newParentLocationY,
							newWidth, newHeight);
					
					if (newHeight != bounds.height) {
						originalMousePosition = new Point(originalMousePosition.x,
								newMousePosition.y);
					}
					
					return true;
				}
			}
		}

		return false;
	}

	private void setCursor(Event event, Composite componentDecorationComposite,
			Rectangle bounds) {
		if ((event.x >= 0 && event.x <= (0 + RESIZE_WIDTH)
				&& event.y > EDGE_WIDTH
				&& event.y < (bounds.height - EDGE_WIDTH - 1) && !eastCursorSet
				&& !northCursorSet && !southCursorSet && !southEastCursorSet
				&& !northEastCursorSet && !northWestCursorSet && !southWestCursorSet)
				|| (originalMousePosition != null && westCursorSet)) {
			componentDecorationComposite.setCursor(westCursor);
			westCursorSet = true;
			eastCursorSet = false;
			northCursorSet = false;
			southCursorSet = false;
			southEastCursorSet = false;
			northEastCursorSet = false;
			northWestCursorSet = false;
			southWestCursorSet = false;
		} else if (((event.x <= bounds.width - 1 && event.x >= bounds.width
				- RESIZE_WIDTH - 1)
				&& (event.y > EDGE_WIDTH && event.y < (bounds.height
						- EDGE_WIDTH - 1))
				&& !westCursorSet
				&& !northCursorSet
				&& !southCursorSet
				&& !southEastCursorSet
				&& !northEastCursorSet && !northWestCursorSet && !southWestCursorSet)
				|| (originalMousePosition != null && eastCursorSet)) {
			componentDecorationComposite.setCursor(eastCursor);
			westCursorSet = false;
			eastCursorSet = true;
			northCursorSet = false;
			southCursorSet = false;
			southEastCursorSet = false;
			northEastCursorSet = false;
			northWestCursorSet = false;
			southWestCursorSet = false;
		} else if ((event.y >= 0 && event.y <= (0 + RESIZE_WIDTH)
				&& event.x > EDGE_WIDTH
				&& event.x < (bounds.width - EDGE_WIDTH - 1) && !eastCursorSet
				&& !westCursorSet && !southCursorSet && !southEastCursorSet
				&& !northEastCursorSet && !northWestCursorSet && !southWestCursorSet)
				|| (originalMousePosition != null && northCursorSet)) {
			componentDecorationComposite.setCursor(northCursor);
			eastCursorSet = false;
			westCursorSet = false;
			northCursorSet = true;
			southCursorSet = false;
			southEastCursorSet = false;
			northEastCursorSet = false;
			northWestCursorSet = false;
			southWestCursorSet = false;
		} else if (((event.y <= bounds.height - 1 && event.y >= bounds.height
				- RESIZE_WIDTH - 1)
				&& (event.x > EDGE_WIDTH && event.x < (bounds.width
						- EDGE_WIDTH - 1))
				&& !eastCursorSet
				&& !westCursorSet
				&& !northCursorSet
				&& !southEastCursorSet
				&& !northEastCursorSet && !northWestCursorSet && !southWestCursorSet)
				|| (originalMousePosition != null && southCursorSet)) {
			componentDecorationComposite.setCursor(southCursor);
			eastCursorSet = false;
			westCursorSet = false;
			northCursorSet = false;
			southCursorSet = true;
			southEastCursorSet = false;
			northEastCursorSet = false;
			northWestCursorSet = false;
			southWestCursorSet = false;
		} else if (((event.y <= bounds.height - 1
				&& event.y >= bounds.height - EDGE_WIDTH - 1
				&& event.x <= bounds.width - 1 && event.x >= bounds.width
				- EDGE_WIDTH - 1)
				&& !eastCursorSet
				&& !westCursorSet
				&& !northCursorSet
				&& !southCursorSet
				&& !northEastCursorSet
				&& !northWestCursorSet && !southWestCursorSet)
				|| (originalMousePosition != null && southEastCursorSet)) {
			componentDecorationComposite.setCursor(southEastCursor);
			eastCursorSet = false;
			westCursorSet = false;
			northCursorSet = false;
			southCursorSet = false;
			southEastCursorSet = true;
			northEastCursorSet = false;
			northWestCursorSet = false;
			southWestCursorSet = false;
		} else if (((event.y >= 0 && event.y <= EDGE_WIDTH
				&& event.x <= bounds.width - 1 && event.x >= bounds.width
				- EDGE_WIDTH - 1)
				&& !eastCursorSet
				&& !westCursorSet
				&& !northCursorSet
				&& !southCursorSet
				&& !southEastCursorSet
				&& !northWestCursorSet && !southWestCursorSet)
				|| (originalMousePosition != null && northEastCursorSet)) {
			componentDecorationComposite.setCursor(northEastCursor);
			eastCursorSet = false;
			westCursorSet = false;
			northCursorSet = false;
			southCursorSet = false;
			southEastCursorSet = false;
			northEastCursorSet = true;
			northWestCursorSet = false;
			southWestCursorSet = false;
		} else if (((event.y >= 0 && event.y <= EDGE_WIDTH && event.x >= 0 && event.x <= EDGE_WIDTH)
				&& !eastCursorSet
				&& !westCursorSet
				&& !northCursorSet
				&& !southCursorSet
				&& !southEastCursorSet
				&& !northEastCursorSet && !southWestCursorSet)
				|| (originalMousePosition != null && northWestCursorSet)) {
			componentDecorationComposite.setCursor(northWestCursor);
			eastCursorSet = false;
			westCursorSet = false;
			northCursorSet = false;
			southCursorSet = false;
			southEastCursorSet = false;
			northEastCursorSet = false;
			northWestCursorSet = true;
			southWestCursorSet = false;
		} else if (((event.y >= (bounds.height - EDGE_WIDTH - 1)
				&& event.y <= bounds.height - 1 && event.x >= 0 && event.x <= 0 + EDGE_WIDTH)
				&& !eastCursorSet
				&& !westCursorSet
				&& !northCursorSet
				&& !southCursorSet
				&& !southEastCursorSet
				&& !northEastCursorSet && !northWestCursorSet)
				|| (originalMousePosition != null && southWestCursorSet)) {
			componentDecorationComposite.setCursor(southWestCursor);
			eastCursorSet = false;
			westCursorSet = false;
			northCursorSet = false;
			southCursorSet = false;
			southEastCursorSet = false;
			northEastCursorSet = false;
			northWestCursorSet = false;
			southWestCursorSet = true;
		} else {
			componentDecorationComposite.setCursor(arrowCursor);
			eastCursorSet = false;
			westCursorSet = false;
			northCursorSet = false;
			southCursorSet = false;
			southEastCursorSet = false;
			northEastCursorSet = false;
			northWestCursorSet = false;
			southWestCursorSet = false;
		}
	}

	private void widgetDisposed(Event event) {
		try {
			componentService.stopComponent(component);
		} catch (BundleException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
					"Stop nicht möglich",
					"Die Komponente konnte nicht gestoppt werden!");
		}
	}
}

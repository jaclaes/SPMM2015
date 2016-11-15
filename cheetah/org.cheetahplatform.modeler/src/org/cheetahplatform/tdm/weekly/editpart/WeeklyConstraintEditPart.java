/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.editpart;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.cheetahplatform.modeler.decserflow.figure.ActivityFigure;
import org.cheetahplatform.modeler.generic.editpart.GenericConnectionEditPart;
import org.cheetahplatform.modeler.generic.editpart.IGenericEditPart;
import org.cheetahplatform.modeler.generic.figure.FilledArrowFigure;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.cheetahplatform.tdm.weekly.figure.WeeklyPlanningAreaFigure;
import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.CoordinateListener;
import org.eclipse.draw2d.EventDispatcher;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.KeyListener;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.XYAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;

public class WeeklyConstraintEditPart extends GenericConnectionEditPart {
	public class ConstraintFigure extends PolylineConnection {
		public ConstraintFigure() {
			setTargetDecoration(new FilledArrowFigure());
			updateConnectionRouter();
		}

		private ConnectionAnchor createBottomRightAnchor() {
			IGenericEditPart validPart = (IGenericEditPart) getSource();
			if (validPart == null) {
				validPart = (IGenericEditPart) getTarget();
			}
			IGenericEditPart editPart = validPart.getEditPart(WeeklyEditPart.class);
			Rectangle bounds = editPart.getFigure().getBounds();
			return new XYAnchor(new Point(bounds.width, bounds.height));
		}

		@Override
		public ConnectionAnchor getSourceAnchor() {
			if (getSource() == null && getTarget() != null) {
				if (startBeforeEnd()) {
					return new XYAnchor(new Point(0, 0));
				}

				return createBottomRightAnchor();
			}

			return super.getSourceAnchor();
		}

		@Override
		public ConnectionAnchor getTargetAnchor() {
			if (getTarget() == null && getSource() != null) {
				if (startBeforeEnd()) {
					return createBottomRightAnchor();
				}

				return new XYAnchor(new Point(0, 0));
			}

			return super.getTargetAnchor();
		}

		@Override
		protected void outlineShape(Graphics g) {
			g.pushState();

			g.setAntialias(SWT.ON);
			super.outlineShape(g);

			g.popState();
		}

		private boolean startBeforeEnd() {
			if (sourceActivity == null) {
				sourceActivity = ((WeeklyEditPart) getEditPart(WeeklyEditPart.class)).findSource(WeeklyConstraintEditPart.this);
				if (sourceActivity == null) {
					return true;
				}
			}
			if (targetActivity == null) {
				targetActivity = ((WeeklyEditPart) getEditPart(WeeklyEditPart.class)).findTarget(WeeklyConstraintEditPart.this);
				if (targetActivity == null) {
					return true;
				}
			}

			return sourceActivity.getStartTime().compareTo(targetActivity.getStartTime()) < 0;
		}

		public void updateConnectionRouter() {
			EditPart parent = getSource();
			if (parent == null) {
				parent = getTarget(); // never know whether the source or the target is created first
			}

			if (parent == null) {
				return;
			}

			IFigure obstacles = ((AbstractGraphicalEditPart) parent.getParent().getParent().getParent()).getFigure();
			setConnectionRouter(new ShortestPathConnectionRouter(new WrappedFigure(obstacles)));
		}
	}

	/**
	 * Can't extend Figure as translateToRelative is final ...
	 * 
	 * @author Stefan Zugal
	 * 
	 */
	private static class WrappedFigure implements IFigure {
		private final IFigure wrapped;

		public WrappedFigure(IFigure wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public void add(IFigure figure) {
			// not implemented

		}

		@Override
		public void add(IFigure figure, int index) {
			// not implemented

		}

		@Override
		public void add(IFigure figure, Object constraint) {
			// not implemented

		}

		@Override
		public void add(IFigure figure, Object constraint, int index) {
			// not implemented

		}

		@Override
		public void addAncestorListener(AncestorListener listener) {
			// not implemented

		}

		@Override
		public void addCoordinateListener(CoordinateListener listener) {
			// not implemented

		}

		@Override
		public void addFigureListener(FigureListener listener) {
			// not implemented

		}

		@Override
		public void addFocusListener(FocusListener listener) {
			// not implemented

		}

		@Override
		public void addKeyListener(KeyListener listener) {
			// not implemented

		}

		@Override
		public void addLayoutListener(LayoutListener listener) {
			for (Object child : getChildren(WeeklyPlanningAreaFigure.class)) {
				((IFigure) child).addLayoutListener(listener);
			}
		}

		@Override
		public void addMouseListener(MouseListener listener) {
			// not implemented

		}

		@Override
		public void addMouseMotionListener(MouseMotionListener listener) {
			// not implemented

		}

		@Override
		public void addNotify() {
			// not implemented

		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			// not implemented

		}

		@Override
		public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
			// not implemented

		}

		@SuppressWarnings("unchecked")
		private void collectChildren(IFigure figure, List collected) {
			collected.add(figure);
			for (Object child : figure.getChildren()) {
				collectChildren((IFigure) child, collected);
			}
		}

		@Override
		public boolean containsPoint(int x, int y) {
			// not implemented
			return false;
		}

		@Override
		public boolean containsPoint(Point p) {
			// not implemented
			return false;
		}

		@Override
		public void erase() {
			// not implemented

		}

		@Override
		public IFigure findFigureAt(int x, int y) {
			// not implemented
			return null;
		}

		@Override
		public IFigure findFigureAt(int x, int y, TreeSearch search) {
			// not implemented
			return null;
		}

		@Override
		public IFigure findFigureAt(Point p) {
			// not implemented
			return null;
		}

		@Override
		public IFigure findFigureAtExcluding(int x, int y, Collection collection) {
			// not implemented
			return null;
		}

		@Override
		public IFigure findMouseEventTargetAt(int x, int y) {
			// not implemented
			return null;
		}

		@Override
		public Color getBackgroundColor() {
			// not implemented
			return null;
		}

		@Override
		public Border getBorder() {
			// not implemented
			return null;
		}

		@Override
		public Rectangle getBounds() {
			// not implemented
			return null;
		}

		@Override
		public List getChildren() {
			return getChildren(ActivityFigure.class);
		}

		private List getChildren(Class<?> type) {
			List<IFigure> children = new ArrayList<IFigure>();
			collectChildren(wrapped, children);
			Iterator<IFigure> iterator = children.iterator();

			// remove all figures except activities
			while (iterator.hasNext()) {
				if (!(type.isAssignableFrom(iterator.next().getClass()))) {
					iterator.remove();
				}
			}

			return children;
		}

		@Override
		public Rectangle getClientArea() {
			// not implemented
			return null;
		}

		@Override
		public Rectangle getClientArea(Rectangle rect) {
			// not implemented
			return null;
		}

		@Override
		public Cursor getCursor() {
			// not implemented
			return null;
		}

		@Override
		public Font getFont() {
			// not implemented
			return null;
		}

		@Override
		public Color getForegroundColor() {
			// not implemented
			return null;
		}

		@Override
		public Insets getInsets() {
			// not implemented
			return null;
		}

		@Override
		public LayoutManager getLayoutManager() {
			// not implemented
			return null;
		}

		@Override
		public Color getLocalBackgroundColor() {
			// not implemented
			return null;
		}

		@Override
		public Color getLocalForegroundColor() {
			// not implemented
			return null;
		}

		@Override
		public Dimension getMaximumSize() {
			// not implemented
			return null;
		}

		@Override
		public Dimension getMinimumSize() {
			// not implemented
			return null;
		}

		@Override
		public Dimension getMinimumSize(int wHint, int hHint) {
			// not implemented
			return null;
		}

		@Override
		public IFigure getParent() {
			// not implemented
			return null;
		}

		@Override
		public Dimension getPreferredSize() {
			// not implemented
			return null;
		}

		@Override
		public Dimension getPreferredSize(int wHint, int hHint) {
			// not implemented
			return null;
		}

		@Override
		public Dimension getSize() {
			// not implemented
			return null;
		}

		@Override
		public IFigure getToolTip() {
			// not implemented
			return null;
		}

		@Override
		public UpdateManager getUpdateManager() {
			// not implemented
			return null;
		}

		@Override
		public void handleFocusGained(FocusEvent event) {
			// not implemented
		}

		@Override
		public void handleFocusLost(FocusEvent event) {
			// not implemented
		}

		@Override
		public void handleKeyPressed(KeyEvent event) {
			// not implemented
		}

		@Override
		public void handleKeyReleased(KeyEvent event) {
			// not implemented
		}

		@Override
		public void handleMouseDoubleClicked(MouseEvent event) {
			// not implemented

		}

		@Override
		public void handleMouseDragged(MouseEvent event) {
			// not implemented
		}

		@Override
		public void handleMouseEntered(MouseEvent event) {
			// not implemented
		}

		@Override
		public void handleMouseExited(MouseEvent event) {
			// not implemented
		}

		@Override
		public void handleMouseHover(MouseEvent event) {
			// not implemented
		}

		@Override
		public void handleMouseMoved(MouseEvent event) {
			// not implemented
		}

		@Override
		public void handleMousePressed(MouseEvent event) {
			// not implemented
		}

		@Override
		public void handleMouseReleased(MouseEvent event) {
			// not implemented
		}

		@Override
		public boolean hasFocus() {
			// not implemented
			return false;
		}

		@Override
		public EventDispatcher internalGetEventDispatcher() {
			// not implemented
			return null;
		}

		@Override
		public boolean intersects(Rectangle rect) {
			// not implemented
			return false;
		}

		@Override
		public void invalidate() {
			// not implemented
		}

		@Override
		public void invalidateTree() {
			// not implemented
		}

		@Override
		public boolean isCoordinateSystem() {
			// not implemented
			return false;
		}

		@Override
		public boolean isEnabled() {
			// not implemented
			return false;
		}

		@Override
		public boolean isFocusTraversable() {
			// not implemented
			return false;
		}

		@Override
		public boolean isMirrored() {
			// not implemented
			return false;
		}

		@Override
		public boolean isOpaque() {
			// not implemented
			return false;
		}

		@Override
		public boolean isRequestFocusEnabled() {
			// not implemented
			return false;
		}

		@Override
		public boolean isShowing() {
			// not implemented
			return false;
		}

		@Override
		public boolean isVisible() {
			// not implemented
			return false;
		}

		@Override
		public void paint(Graphics graphics) {
			// not implemented
		}

		@Override
		public void remove(IFigure figure) {
			// not implemented
		}

		@Override
		public void removeAncestorListener(AncestorListener listener) {
			// not implemented
		}

		@Override
		public void removeCoordinateListener(CoordinateListener listener) {
			// not implemented
		}

		@Override
		public void removeFigureListener(FigureListener listener) {
			// not implemented
		}

		@Override
		public void removeFocusListener(FocusListener listener) {
			// not implemented
		}

		@Override
		public void removeKeyListener(KeyListener listener) {
			// not implemented
		}

		@Override
		public void removeLayoutListener(LayoutListener listener) {
			for (Object child : getChildren(WeeklyPlanningAreaFigure.class)) {
				((IFigure) child).removeLayoutListener(listener);
			}
		}

		@Override
		public void removeMouseListener(MouseListener listener) {
			// not implemented
		}

		@Override
		public void removeMouseMotionListener(MouseMotionListener listener) {
			// not implemented
		}

		@Override
		public void removeNotify() {
			// not implemented
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			// not implemented
		}

		@Override
		public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
			// not implemented
		}

		@Override
		public void repaint() {
			// not implemented
		}

		@Override
		public void repaint(int x, int y, int w, int h) {
			// not implemented
		}

		@Override
		public void repaint(Rectangle rect) {
			// not implemented
		}

		@Override
		public void requestFocus() {
			// not implemented
		}

		@Override
		public void revalidate() {
			// not implemented
		}

		@Override
		public void setBackgroundColor(Color c) {
			// not implemented
		}

		@Override
		public void setBorder(Border b) {
			// not implemented
		}

		@Override
		public void setBounds(Rectangle rect) {
			// not implemented
		}

		@Override
		public void setConstraint(IFigure child, Object constraint) {
			// not implemented
		}

		@Override
		public void setCursor(Cursor cursor) {
			// not implemented
		}

		@Override
		public void setEnabled(boolean value) {
			// not implemented
		}

		@Override
		public void setFocusTraversable(boolean value) {
			// not implemented
		}

		@Override
		public void setFont(Font f) {
			// not implemented
		}

		@Override
		public void setForegroundColor(Color c) {
			// not implemented
		}

		@Override
		public void setLayoutManager(LayoutManager lm) {
			// not implemented
		}

		@Override
		public void setLocation(Point p) {
			// not implemented
		}

		@Override
		public void setMaximumSize(Dimension size) {
			// not implemented
		}

		@Override
		public void setMinimumSize(Dimension size) {
			// not implemented
		}

		@Override
		public void setOpaque(boolean isOpaque) {
			// not implemented
		}

		@Override
		public void setParent(IFigure parent) {
			// not implemented
		}

		@Override
		public void setPreferredSize(Dimension size) {
			// not implemented
		}

		@Override
		public void setRequestFocusEnabled(boolean requestFocusEnabled) {
			// not implemented
		}

		@Override
		public void setSize(Dimension d) {
			// not implemented
		}

		@Override
		public void setSize(int w, int h) {
			// not implemented
		}

		@Override
		public void setToolTip(IFigure figure) {
			// not implemented
		}

		@Override
		public void setVisible(boolean visible) {
			// not implemented
		}

		@Override
		public void translate(int x, int y) {
			// not implemented
		}

		@Override
		public void translateFromParent(Translatable t) {
			// not implemented
		}

		@Override
		public void translateToAbsolute(Translatable t) {
			// not implemented
		}

		@Override
		public void translateToParent(Translatable t) {
			// not implemented
		}

		@Override
		public void translateToRelative(Translatable t) {
			wrapped.translateToRelative(t);
		}

		@Override
		public void validate() {
			// not implemented
		}
	}

	private WeeklyActivity sourceActivity;
	private WeeklyActivity targetActivity;

	public WeeklyConstraintEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		return new ConstraintFigure();
	}

	/**
	 * Return the sourceActivity.
	 * 
	 * @return the sourceActivity
	 */
	public WeeklyActivity getSourceActivity() {
		return sourceActivity;
	}

	/**
	 * Return the targetActivity.
	 * 
	 * @return the targetActivity
	 */
	public WeeklyActivity getTargetActivity() {
		return targetActivity;
	}

	@Override
	public void setSource(EditPart editPart) {
		super.setSource(editPart);

		if (editPart != null) {
			sourceActivity = (WeeklyActivity) editPart.getModel();
		}

		updateVisibility();
		updateRouting();
	}

	@Override
	public void setTarget(EditPart editPart) {
		super.setTarget(editPart);

		if (editPart != null) {
			targetActivity = (WeeklyActivity) editPart.getModel();
		}

		updateVisibility();
		updateRouting();
	}

	private void updateRouting() {
		if (getTarget() == null ^ getSource() == null) {
			refresh();
		}
	}

	private void updateVisibility() {
		((ConstraintFigure) getFigure()).updateConnectionRouter();
		boolean sourceHidden = sourceActivity != null && !sourceActivity.isVisible();
		boolean targetHidden = targetActivity != null && !targetActivity.isVisible();
		boolean visible = !(sourceHidden || targetHidden);
		figure.setVisible(visible);
	}
}

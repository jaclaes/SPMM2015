/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.editpart;

import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_ACTIVITIES;
import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_EXECUTION_ASSERTIONS;
import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_TERMINATION_ASSERTIONS;
import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_TIMESLOT_SELECTION;
import static org.eclipse.gef.EditPolicy.COMPONENT_ROLE;

import java.beans.PropertyChangeEvent;

import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.tdm.daily.figure.HorizontalScrollBar;
import org.cheetahplatform.tdm.daily.figure.VerticalScrollBar;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.daily.policy.ActivityEditPolicy;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;

import com.swtdesigner.SWTResourceManager;

public class WorkspaceEditPart extends GenericEditPart {

	public static final int DAY_OFFSET_X = 5;
	public static final int DAY_OFFSET_Y = 5;
	public static final int DAY_SPACING = 5;

	private Figure content;
	private ScrollPane scrollPane;
	private PixelTimeConverter converter;

	public WorkspaceEditPart(Workspace tddWorkspace) {
		super(tddWorkspace);

		this.converter = new PixelTimeConverter(this);
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
		content.add(child, index);
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(COMPONENT_ROLE, new ActivityEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		scrollPane = new ScrollPane() {

			@Override
			protected void createHorizontalScrollBar() {
				setHorizontalScrollBar(new HorizontalScrollBar());
			}

			@Override
			protected void createVerticalScrollBar() {
				setVerticalScrollBar(new VerticalScrollBar());
			}

			@Override
			public Dimension getPreferredSize(int hint, int hint2) {
				Rectangle bounds = new Rectangle(getViewer().getControl().getBounds());
				bounds.x = 0;
				bounds.y = 0;
				bounds.expand(-6, -6);
				return bounds.getSize();
			}

		};

		content = new Figure();
		scrollPane.setContents(content);
		content.setOpaque(true);
		content.setBackgroundColor(SWTResourceManager.getColor(255, 255, 255));
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = DAY_SPACING;
		layout.marginHeight = DAY_OFFSET_Y;
		layout.numColumns = (getModel()).getChildren().size() + 1;
		content.setLayoutManager(layout);

		return scrollPane;
	}

	/**
	 * Return the content.
	 * 
	 * @return the content
	 */
	public Figure getContent() {
		return content;
	}

	/**
	 * Return the converter.
	 * 
	 * @return the converter
	 */
	public PixelTimeConverter getConverter() {
		return converter;
	}

	@Override
	public Workspace getModel() {
		return (Workspace) super.getModel();
	}

	public int getScrolledX() {
		HorizontalScrollBar bar = (HorizontalScrollBar) scrollPane.getHorizontalScrollBar();
		return bar.getValue();
	}

	public int getScrolledY() {
		VerticalScrollBar bar = (VerticalScrollBar) scrollPane.getVerticalScrollBar();
		return bar.getValue();
	}

	/**
	 * Return the scrollPane.
	 * 
	 * @return the scrollPane
	 */
	public ScrollPane getScrollPane() {
		return scrollPane;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if (PROPERTY_ACTIVITIES.equals(name) || PROPERTY_EXECUTION_ASSERTIONS.equals(name) || PROPERTY_TERMINATION_ASSERTIONS.equals(name)) {
			refresh(true);
		} else if (PROPERTY_TIMESLOT_SELECTION.equals(name)) {
			getFigure().repaint();
		}
	}

	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
		content.remove(child);
	}

}

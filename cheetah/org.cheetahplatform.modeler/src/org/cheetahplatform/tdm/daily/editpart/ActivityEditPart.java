/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.editpart;

import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_BACKGROUND_COLOR;
import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_NAME;
import static org.cheetahplatform.tdm.TDMConstants.COLOR_TEST_FAILED;
import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_LAYOUT;
import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_SELECTION;
import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_STATE;
import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_TOOLTIP;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.decserflow.figure.ActivityFigure;
import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.tdm.daily.action.RenameActivityAction;
import org.cheetahplatform.tdm.daily.model.AbstractActivity;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.Day;
import org.cheetahplatform.tdm.daily.model.DeleteActivityAction;
import org.cheetahplatform.tdm.daily.policy.ActivityEditPolicy;
import org.cheetahplatform.tdm.daily.policy.MoveActivityEditPolicy;
import org.cheetahplatform.tdm.engine.ITDMStep;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.SelectionManager;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.swtdesigner.SWTResourceManager;

public class ActivityEditPart extends GenericEditPart {

	public static final int SPACING_X = 0;

	/**
	 * A constraint may consist of several graph elements (e.g., multi precedence).
	 */
	private List<GraphElement> constraintCausingFailure;

	public ActivityEditPart(Activity tddActivityInstance) {
		super(tddActivityInstance);

		constraintCausingFailure = new ArrayList<GraphElement>();
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_MODIFICATION_OF_TESTS)) {
			menu.add(new RenameActivityAction(getModel()));
			menu.add(new DeleteActivityAction(getModel()));
		}
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ActivityEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new MoveActivityEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		Activity model = getModel();
		ActivityFigure figure = new ActivityFigure(model.getName());
		updateRectangularSettings(figure);
		figure.setBackgroundColor(getBackgroundColor());
		figure.setState(model.getState());

		return figure;
	}

	private void editName() {
		new RenameActivityAction(getModel()).run();
	}

	private Color getBackgroundColor() {
		RGB rgb = ((AbstractActivity) getModel()).getBackgroundColor();
		Color color = SWTResourceManager.getColor(rgb);
		return color;
	}

	public DayEditPart getDay() {
		return (DayEditPart) getEditPart(DayEditPart.class);
	}

	@Override
	public ActivityFigure getFigure() {
		return (ActivityFigure) super.getFigure();
	}

	@Override
	public Activity getModel() {
		return (Activity) super.getModel();
	}

	public WorkspaceEditPart getWorkspace() {
		return (WorkspaceEditPart) getEditPart(WorkspaceEditPart.class);
	}

	@Override
	public void performRequest(Request req) {
		if (REQ_OPEN.equals(req.getType())) {
			editName();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();

		if (PROPERTY_LAYOUT.equals(name)) {
			updateLayout();
		} else if (PROPERTY_SELECTION.equals(name)) {
			updateSelection(evt);
		} else if (PROPERTY_STATE.equals(name)) {
			updateBackground();
			updateConstraintHighlighting();
		} else if (PROPERTY_NAME.equals(name)) {
			updateName();
		} else if (PROPERTY_BACKGROUND_COLOR.equals(name)) {
			updateBackground();
		} else if (PROPERTY_TOOLTIP.equals(name)) {
			updateTooltip();
		}
	}

	@Override
	public void setSelected(int value) {
		super.setSelected(value);

		updateConstraintHighlighting();
	}

	private void updateBackground() {
		Color color = getBackgroundColor();
		figure.setBackgroundColor(color);
		getFigure().setState(getModel().getState());
	}

	public void updateConstraintHighlighting() {
		TDMTest test = getWorkspace().getModel().getTest();
		ITDMStep step = test.getFailure(getModel());
		if (step != null) {
			constraintCausingFailure = step.getFailure().getModelElementCausingFailure();
		}

		RGB color = new RGB(0, 0, 0);
		if (COLOR_TEST_FAILED.equals(getModel().getBackgroundColor()) && getSelected() != SELECTED_NONE) {
			color = new RGB(255, 0, 0);
		}

		for (GraphElement element : constraintCausingFailure) {
			element.setProperty(PROPERTY_BACKGROUND_COLOR, color);
		}
	}

	protected void updateLayout() {
		updateRectangularSettings((ActivityFigure) figure);

		Day day = (Day) getParent().getParent().getModel();
		Rectangle bounds = new Rectangle((getModel()).getRelativeBounds(day));
		bounds.x += -1;
		bounds.width -= 3 * SPACING_X;

		((AbstractGraphicalEditPart) getParent()).getFigure().setConstraint(figure, bounds);
	}

	private void updateName() {
		ActivityFigure figure = getFigure();
		figure.setName(getModel().getName());
	}

	protected void updateRectangularSettings(ActivityFigure figure) {
		Activity instance = getModel();
		Date date = ((Day) getParent().getParent().getModel()).getDate();
		boolean rectangularStart = !date.sameDay(instance.getStartTime());
		boolean rectangularEnd = !date.sameDay(instance.getEndTime());
		figure.setRectangularStart(rectangularStart);
		figure.setRectangularEnd(rectangularEnd);
	}

	private void updateSelection(PropertyChangeEvent evt) {
		SelectionManager selectionManager = getViewer().getSelectionManager();

		if ((Boolean) evt.getNewValue()) {
			selectionManager.appendSelection(this);
			setSelected(SELECTED);
		} else {
			selectionManager.deselect(this);
			setSelected(SELECTED_NONE);
		}
	}

	private void updateTooltip() {
		String tooltip = getModel().getTooltip();
		getFigure().setToolTip(tooltip);
	}
}

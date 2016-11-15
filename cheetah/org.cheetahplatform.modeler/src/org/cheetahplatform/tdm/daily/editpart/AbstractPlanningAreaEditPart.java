package org.cheetahplatform.tdm.daily.editpart;

import java.beans.PropertyChangeEvent;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.daily.figure.PlanningAreaFigure;
import org.cheetahplatform.tdm.daily.model.AbstractPlanningArea;
import org.cheetahplatform.tdm.daily.model.ActivityLayouter;
import org.cheetahplatform.tdm.daily.policy.PlanningAreaEditPolicy;
import org.cheetahplatform.tdm.daily.policy.PlanningAreaSelectionEditPolicy;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;

public abstract class AbstractPlanningAreaEditPart extends GenericEditPart {

	public static final int WIDTH = (Integer) CheetahPlatformConfigurator.getObject(IConfiguration.TDM_PLANNING_AREA_WIDTH);

	public AbstractPlanningAreaEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	protected void createEditPolicies() {
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_MODIFICATION_OF_TESTS)) {
			installEditPolicy(EditPolicy.CONTAINER_ROLE, new PlanningAreaEditPolicy());
			installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new PlanningAreaSelectionEditPolicy());
		}
	}

	@Override
	protected IFigure createFigure() {
		return new PlanningAreaFigure(getModel().getParent().getDate());
	}

	@Override
	public PlanningAreaFigure getFigure() {
		return (PlanningAreaFigure) super.getFigure();
	}

	@Override
	public AbstractPlanningArea getModel() {
		return (AbstractPlanningArea) super.getModel();
	}

	@Override
	public int getSelected() {
		return super.getSelected();
	}

	/**
	 * Determine the edit part's selection.
	 * 
	 * @return the selection
	 */
	public abstract TimeSlot getSelection();

	public WorkspaceEditPart getWorkspace() {
		return (WorkspaceEditPart) getEditPart(WorkspaceEditPart.class);
	}

	private void layout() {
		ActivityLayouter layouter = new ActivityLayouter(getModel());
		layouter.layout();
	}

	@Override
	public void performRequest(Request req) {
		// ignore
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(TDMConstants.PROPERTY_TIMESLOT_SELECTION)) {
			TimeSlot selection = getSelection();
			getFigure().setSelection(selection);
		}
	}

	@Override
	public void refresh() {
		super.refresh();

		layout();
	}

	@Override
	public void refresh(boolean recursive) {
		super.refresh(recursive);

		layout();
	}

	@Override
	protected void refreshVisuals() {
		GridData constraint = new GridData();
		constraint.widthHint = getModel().getWidth();
		constraint.grabExcessVerticalSpace = true;
		constraint.verticalAlignment = SWT.FILL;
		((AbstractGraphicalEditPart) getParent()).getFigure().setConstraint(figure, constraint);
	}

	/**
	 * Select the timeslot at the given absolute location.
	 * 
	 * @param location
	 *            the absolute location
	 */
	public void selectTimeslot(Point location) {
		WorkspaceEditPart workspace = (WorkspaceEditPart) getEditPart(WorkspaceEditPart.class);
		DateTime time = workspace.getConverter().computeDateTime(location, false);

		DateTime start = new DateTime(time);
		start.truncateToDate();
		DateTime end = new DateTime(start, false);
		end.plus(new Duration(0, 0, TDMConstants.LOCKING_INTERVAL, true));

		TimeSlot slot = new TimeSlot(start, end);
		while (!slot.includes(time)) {
			slot.getEnd().increaseMinutesBy(TDMConstants.LOCKING_INTERVAL);
			slot.getStart().increaseMinutesBy(TDMConstants.LOCKING_INTERVAL);
		}

		slot.getEnd().increaseMinutesBy(TDMConstants.LOCKING_INTERVAL);
		setSelection(slot);
	}

	public void selectTimeSlot(Point startPoint, Point endPoint) {
		WorkspaceEditPart workspace = (WorkspaceEditPart) getEditPart(WorkspaceEditPart.class);
		DateTime start = workspace.getConverter().computeDateTime(startPoint, true);
		DateTime end = workspace.getConverter().computeDateTime(endPoint, true);

		if (start.compareTo(end) > 0) {
			DateTime temp = start;
			start = end;
			end = temp;
		} else if (start.compareTo(end) == 0) {
			getModel().getWorkspace().setPlanningAreaSelection(null);
			return;
		}

		TimeSlot slot = new TimeSlot(start, new DateTime(end, false));
		setSelection(slot);
	}

	/**
	 * Set the edit part's selection.
	 * 
	 * @param slot
	 *            the selected slot
	 */
	public abstract void setSelection(TimeSlot slot);

}

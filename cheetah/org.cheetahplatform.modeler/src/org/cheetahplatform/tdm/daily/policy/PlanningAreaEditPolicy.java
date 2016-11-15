/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.policy;

import static org.cheetahplatform.tdm.TDMConstants.KEY_ACTIVITIES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.modeler.graph.command.DummyCommand;
import org.cheetahplatform.tdm.daily.editpart.AbstractPlanningAreaEditPart;
import org.cheetahplatform.tdm.daily.editpart.PixelTimeConverter;
import org.cheetahplatform.tdm.daily.editpart.WorkspaceEditPart;
import org.cheetahplatform.tdm.daily.model.AbstractPlanningArea;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.Day;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

public class PlanningAreaEditPolicy extends ContainerEditPolicy {
	private ActivityEditPolicy activityEditPolicy;

	public PlanningAreaEditPolicy() {
		this(new ActivityEditPolicy());
	}

	public PlanningAreaEditPolicy(ActivityEditPolicy activityEditPolicy) {
		this.activityEditPolicy = activityEditPolicy;
	}

	private DateTime computeStartTime(ChangeBoundsRequest changeBoundsRequest) {
		AbstractPlanningAreaEditPart host = getHost();
		WorkspaceEditPart workspace = (WorkspaceEditPart) host.getEditPart(WorkspaceEditPart.class);
		PixelTimeConverter converter = new PixelTimeConverter(workspace);
		Point location = changeBoundsRequest.getLocation();
		if (changeBoundsRequest.getExtendedData().containsKey(CommonConstants.KEY_MOUSE_LOCATION)) {
			// adapt the location if the request came from another viewer
			location = (Point) changeBoundsRequest.getExtendedData().get(CommonConstants.KEY_MOUSE_LOCATION);
		}

		DateTime time = converter.computeDateTime(location);
		return time;
	}

	protected Command createAddActivityCommand(List<DeclarativeActivity> activities, AbstractPlanningArea area, DateTime time) {
		return new AddActivityCommand(area.getWorkspace(), activities, time);
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		activityEditPolicy.eraseFeedback();
	}

	private List<DeclarativeActivity> extractActivities(ChangeBoundsRequest changeBoundsRequest) {
		List editParts = changeBoundsRequest.getEditParts();
		List<DeclarativeActivity> activities = new ArrayList<DeclarativeActivity>();
		for (Object editPart : editParts) {
			if (!(editPart instanceof IAdaptable)) {
				continue;
			}

			DeclarativeActivity activity = (DeclarativeActivity) ((IAdaptable) editPart).getAdapter(DeclarativeActivity.class);
			if (activity == null) {
				continue; // not adaptable
			}
			activities.add(activity);
		}
		return activities;
	}

	@Override
	public Command getCommand(Request request) {
		Object type = request.getType();

		if (REQ_ADD.equals(type)) {
			ChangeBoundsRequest changeBoundsRequest = (ChangeBoundsRequest) request;
			if (changeBoundsRequest.getEditParts().isEmpty()) {
				return handlePlanningAreaSelection(changeBoundsRequest);
			}

			return handleAddActivities(changeBoundsRequest);
		}

		return null;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	@Override
	public AbstractPlanningAreaEditPart getHost() {
		return (AbstractPlanningAreaEditPart) super.getHost();
	}

	@Override
	public EditPart getTargetEditPart(Request request) {
		Object type = request.getType();

		if (REQ_ADD.equals(type) || REQ_SELECTION.equals(type)) {
			return getHost();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private Command handleAddActivities(ChangeBoundsRequest changeBoundsRequest) {
		List<DeclarativeActivity> activities = extractActivities(changeBoundsRequest);

		changeBoundsRequest.getExtendedData().put(KEY_ACTIVITIES, activities);
		AbstractPlanningArea area = getHost().getModel();
		DateTime time = computeStartTime(changeBoundsRequest);

		return createAddActivityCommand(activities, area, time);
	}

	protected Command handlePlanningAreaSelection(ChangeBoundsRequest request) {
		Point endLocation = request.getLocation();
		Point startLocation = new Point(endLocation).translate(request.getMoveDelta().getCopy().negate());

		EditPart sourceEditPart = getHost().getViewer().findObjectAt(startLocation);
		if (!getHost().getClass().equals(sourceEditPart.getClass())) {
			// request came from another planning area type, delegate request
			for (Object sibling : getHost().getParent().getChildren()) {
				if (sibling.getClass().equals(sourceEditPart.getClass())) {
					return ((EditPart) sibling).getCommand(request);
				}
			}

		}

		getHost().selectTimeSlot(startLocation, endLocation);
		return new DummyCommand();
	}

	@Override
	public void showTargetFeedback(Request request) {
		if (RequestConstants.REQ_ADD.equals(request.getType())) {
			activityEditPolicy.eraseFeedback();
			activityEditPolicy.setHost(getHost());

			Workspace dummy = new Workspace();
			Map<Activity, TimeSlot> feedbackModel = new HashMap<Activity, TimeSlot>();

			for (DeclarativeActivity activityDefinition : extractActivities((ChangeBoundsRequest) request)) {
				DateTime startTime = computeStartTime((ChangeBoundsRequest) request);
				DateTime endTime = new DateTime(startTime, false);
				endTime.plus(activityDefinition.getExpectedDuration());
				TimeSlot slot = new TimeSlot(startTime, endTime);

				Day day = dummy.getDays().get(0);
				Activity activity = new Activity(day, activityDefinition.instantiate(dummy.getProcessInstance()));
				feedbackModel.put(activity, slot);
			}

			activityEditPolicy.createFeedback(feedbackModel);
		}
	}

}

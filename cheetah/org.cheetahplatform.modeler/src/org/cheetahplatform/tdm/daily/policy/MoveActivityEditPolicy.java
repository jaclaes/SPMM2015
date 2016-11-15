package org.cheetahplatform.tdm.daily.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.modeler.generic.PathFigureSelectionHandle;
import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.tdm.ActivityNonResizableEditPolicy;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.daily.editpart.ActivityEditPart;
import org.cheetahplatform.tdm.daily.editpart.DayEditPart;
import org.cheetahplatform.tdm.daily.editpart.PixelTimeConverter;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.Day;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.SelectEditPartTracker;

public class MoveActivityEditPolicy extends ActivityNonResizableEditPolicy {

	private boolean recursive;

	private static final Map<Activity, TimeSlot> EMPTY_MAP = new HashMap<Activity, TimeSlot>(0);

	public MoveActivityEditPolicy() {
		super();
	}

	public MoveActivityEditPolicy(ActivityEditPolicy editPolicy) {
		super(editPolicy);
	}

	/**
	 * Compute the day at a certain location.
	 * 
	 * @param location
	 *            the location
	 * 
	 * @return the day, <code>null</code> if there is no day at the specified location
	 */
	private Day computeDay(Point location) {
		EditPart target = getHost().getViewer().findObjectAt(location);
		if (!(target instanceof GenericEditPart)) {
			return null; // target may also be the viewer itself
		}

		GenericEditPart dayTarget = ((GenericEditPart) target).getEditPart(DayEditPart.class);
		if (dayTarget == null) {
			return null; // not within a day
		}

		return (Day) dayTarget.getModel();
	}

	/**
	 * Compute the new timeslot for the given activity, taking into account the request.
	 * 
	 * @param targetDay
	 *            the day to which the activity is moved
	 * @param request
	 *            the request describing the move
	 * @param activity
	 *            the activity itself
	 * @return the new timeslot after the move
	 */
	private TimeSlot computeTimeSlot(Day targetDay, ChangeBoundsRequest request, Activity activity) {
		Workspace workspace = targetDay.getWorkspace();
		boolean isFirstDay = workspace.isFirstDay(targetDay);
		boolean isLastDay = workspace.isLastDay(targetDay);

		int minutesDelta = PixelTimeConverter.computeDuration(request.getMoveDelta().y);
		minutesDelta -= minutesDelta % TDMConstants.LOCKING_INTERVAL;

		Point originalLocation = new Point(request.getLocation());
		originalLocation.translate(-request.getMoveDelta().x, -request.getMoveDelta().y);
		Day originalDay = computeDay(originalLocation);

		int daysDelta = targetDay.getDate().getDay() - originalDay.getDate().getDay();
		int hoursDelta = daysDelta * 24;

		DateTime newStartTime = new DateTime(activity.getStartTime(), true);
		Duration duration = new Duration(hoursDelta, minutesDelta);
		newStartTime.plus(duration);

		DateTime newEndTime = new DateTime(activity.getEndTime(), false);

		if (isFirstDay && newStartTime.compareTo(targetDay.getDate()) < 0) {
			newStartTime = new DateTime(targetDay.getDate());
			newEndTime = new DateTime(newStartTime, false);
			newEndTime.plus(new Duration(0, activity.getTimeSlot().getDurationInMinutes()));
		} else {
			newEndTime.plus(new Duration(hoursDelta, minutesDelta));
		}

		DateTime endOfLastDay = new DateTime(targetDay.getDate());
		endOfLastDay.plus(new Duration(0, 24, 0, true));

		if (isLastDay && newEndTime.compareTo(endOfLastDay) > 0) {
			newEndTime = new DateTime(endOfLastDay, false);
			newStartTime = new DateTime(endOfLastDay);
			newStartTime.minus(new Duration(0, 0, activity.getTimeSlot().getDurationInMinutes(), true));
		}

		return new TimeSlot(newStartTime, newEndTime);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected List createSelectionHandles() {
		List<PathFigureSelectionHandle> handles = new ArrayList<PathFigureSelectionHandle>();
		GraphicalEditPart editPart = getHost();
		PathFigureSelectionHandle handle = new PathFigureSelectionHandle(editPart);
		if (!isDragAllowed()) {
			handle.setDragTracker(new SelectEditPartTracker(getHost()));
			handle.setCursor(Cursors.ARROW);
		}
		handles.add(handle);

		ResizableHandleKit.addHandle(editPart, handles, PositionConstants.SOUTH);
		editPart.getViewer().getVisualPartMap().put(handles.get(handles.size() - 1), editPart);
		ResizableHandleKit.addHandle(editPart, handles, PositionConstants.NORTH);
		editPart.getViewer().getVisualPartMap().put(handles.get(handles.size() - 1), editPart);

		return handles;
	}

	@Override
	public Command getCommand(Request request) {
		Object type = request.getType();
		if (REQ_MOVE.equals(request.getType()) || REQ_ORPHAN.equals(request.getType()) || REQ_ADD.equals(type)) {
			return handleMoveRequest((ChangeBoundsRequest) request);
		}
		if (REQ_DELETE.equals(request.getType())) {
			return new DeleteActivityCommand(getHost().getModel());
		} else if (REQ_RESIZE.equals(request.getType())) {
			return handleResizeRequest((ChangeBoundsRequest) request);
		}

		return null;
	}

	@Override
	public ActivityEditPart getHost() {
		return (ActivityEditPart) super.getHost();
	}

	@SuppressWarnings("unchecked")
	private CompoundCommand handleMoveRequest(ChangeBoundsRequest request) {
		Day day = computeDay(request.getLocation());
		if (day == null) {
			return null; // invalid drop target
		}

		CompoundCommand command = new CompoundCommand();
		for (EditPart part : (List<EditPart>) request.getEditParts()) {
			if (!(part instanceof ActivityEditPart)) {
				continue;
			}

			Activity activity = (Activity) part.getModel();
			DateTime newStart = computeTimeSlot(day, request, activity).getStart();
			command.add(new MoveActivityCommand(activity, newStart));
		}

		if (command.getCommands().isEmpty()) {
			return null;
		}

		return command;
	}

	@SuppressWarnings({ "unchecked", "serial" })
	private Command handleResizeRequest(ChangeBoundsRequest request) {
		Activity activity = getHost().getModel();

		Point location = request.getLocation();
		DateTime time = getHost().getWorkspace().getConverter().computeDateTime(location, true);
		DateTime newStartTime = activity.getStartTime();
		DateTime newEndTime = activity.getEndTime();

		if (request.getResizeDirection() == PositionConstants.NORTH) {
			newStartTime = time;
		} else if (request.getResizeDirection() == PositionConstants.SOUTH) {
			newEndTime = new DateTime(time, false);
		}

		if (newStartTime.compareTo(newEndTime) > 0) {
			DateTime temp = newStartTime;
			newStartTime = new DateTime(newEndTime, true);
			newEndTime = new DateTime(temp, false);
		}

		if (newEndTime.getTimeInMilliseconds() - newStartTime.getTimeInMilliseconds() < 1000) {
			newEndTime.plus(new Duration(0, 0, TDMConstants.LOCKING_INTERVAL));
		}

		TimeSlot newslot = new TimeSlot(newStartTime, newEndTime);
		request.setExtendedData(new HashMap<Object, Object>() {
			@Override
			public void clear() {
				// no clearing allowed
			}
		});
		request.getExtendedData().put(TDMConstants.KEY_NEW_TIME_SLOT, newslot);
		return new ResizeActivityCommand(activity, newslot);
	}

	@Override
	public void hideSelection() {
		super.hideSelection();

		if (recursive) {
			return;
		}

		recursive = true;
		Activity activity = getHost().getModel();
		activity.firePropertyChanged(TDMConstants.PROPERTY_SELECTION, getHost(), false);
		recursive = false;
	}

	@SuppressWarnings("unchecked")
	private void showFeedback(ChangeBoundsRequest request, Map<Activity, TimeSlot> overrides) {
		activityPolicy.eraseFeedback();
		Day day = computeDay(request.getLocation());
		if (day == null) {
			return; // invalid drop target
		}

		Map<Activity, TimeSlot> figureModels = new HashMap<Activity, TimeSlot>();
		for (EditPart part : (List<EditPart>) request.getEditParts()) {
			if (!activityPolicy.supports(part)) {
				continue;
			}

			Activity activity = (Activity) part.getModel();
			TimeSlot slot = computeTimeSlot(day, request, activity);
			if (overrides.containsKey(activity)) {
				slot = overrides.get(activity);
			}

			figureModels.put(activity, slot);
		}

		activityPolicy.createFeedback(figureModels);
	}

	@Override
	public void showSelection() {
		super.showSelection();

		if (recursive) {
			return;
		}

		recursive = true;
		Activity activity = getHost().getModel();
		activity.firePropertyChanged(TDMConstants.PROPERTY_SELECTION, getHost(), true);
		recursive = false;
	}

	@Override
	public void showSourceFeedback(Request request) {
		Object type = request.getType();
		if (REQ_MOVE.equals(type) || REQ_ADD.equals(type)) {
			showFeedback((ChangeBoundsRequest) request, EMPTY_MAP);
		} else if (REQ_RESIZE.equals(type)) {
			TimeSlot slot = (TimeSlot) request.getExtendedData().get(TDMConstants.KEY_NEW_TIME_SLOT);
			Map<Activity, TimeSlot> overrides = new HashMap<Activity, TimeSlot>();
			if (slot != null) {
				overrides.put(getHost().getModel(), slot);
			}

			showFeedback((ChangeBoundsRequest) request, overrides);
		}
	}

	@Override
	public boolean understandsRequest(Request request) {
		return super.understandsRequest(request) || request.getType().equals(REQ_RESIZE);
	}
}

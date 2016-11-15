/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.policy;

import static org.cheetahplatform.tdm.TDMConstants.REQUEST_ADD_ACTIVITY;
import static org.cheetahplatform.tdm.daily.editpart.ActivityEditPart.SPACING_X;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.modeler.decserflow.figure.ActivityFigure;
import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.editpart.IGenericEditPart;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.daily.editpart.ActivityEditPart;
import org.cheetahplatform.tdm.daily.editpart.PixelTimeConverter;
import org.cheetahplatform.tdm.daily.editpart.PlanningAreaEditPart;
import org.cheetahplatform.tdm.daily.editpart.WorkspaceEditPart;
import org.cheetahplatform.tdm.daily.figure.ActivityFeedbackFigure;
import org.cheetahplatform.tdm.daily.figure.DayTimeLineFigure;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.ActivityLayouter;
import org.cheetahplatform.tdm.daily.model.Day;
import org.cheetahplatform.tdm.daily.model.IActivityContainer;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

public class ActivityEditPolicy extends GraphicalEditPolicy {

	private class ActivityFeedbackHelper implements IFeedbackHelper {

		@Override
		public Activity addActivity(Workspace dummyWorkspace, Activity activity, TimeSlot adaptedSlot) {
			return dummyWorkspace.addActivityInstance(activity.getName(), adaptedSlot);
		}

		@Override
		public void computeLayout(Map<Activity, ActivityFigure> modelToFigure) {
			WorkspaceEditPart workspaceEditPart = (WorkspaceEditPart) ((IGenericEditPart) getHost()).getEditPart(WorkspaceEditPart.class);
			int scrolledX = workspaceEditPart.getScrolledX();

			for (Day day : dummyWorkspace.getDays()) {
				FeedbackActivityContainer container = new FeedbackActivityContainer(day);
				ActivityLayouter layouter = new ActivityLayouter(container);
				layouter.layout();

				for (Activity activity : container.getActivities()) {
					ActivityFigure figure = modelToFigure.get(activity);

					Rectangle bounds = figure.getBounds();
					PixelTimeConverter converter = getWorkspaceEditPart().getConverter();
					bounds.x = activity.getX(day) + converter.computeXAbsolute(day.getDate()) - SPACING_X - scrolledX;
					bounds.y = converter.computeYAbsolute(day.getDate(), activity.getTimeSlot());
					bounds.width = activity.getWidth(day) - 2 * SPACING_X;
					bounds.height = activity.getHeight(day) + 2;
				}
			}
		}

		@Override
		public ActivityFeedbackFigure createActivityFigure(Activity activity, boolean rectangularStart, boolean rectangularEnd) {
			return new ActivityFeedbackFigure(activity.getName(), rectangularStart, rectangularEnd);
		}

		@Override
		public Dimension getDefaultFeedbackFigureSize() {
			return new Dimension(PlanningAreaEditPart.WIDTH - 10, DayTimeLineFigure.HOUR_HEIGHT);
		}
	}

	protected class FeedbackActivityContainer implements IActivityContainer {

		private final Day day;

		public FeedbackActivityContainer(Day day) {
			this.day = day;
		}

		@Override
		public List<Activity> getActivities() {
			if (dayToActivities.isEmpty()) {
				return Collections.emptyList();
			}

			return dayToActivities.get(day);
		}

		@Override
		public int getAvailableWidth() {
			return day.getWidth();
		}

		@Override
		public Date getDate() {
			return day.getDate();
		}

	}

	public static ActivityFigure createFeedbackFigure(String label) {
		return new ActivityFeedbackFigure(label, false, false);
	}

	protected Map<Day, List<Activity>> dayToActivities;
	protected final List<ActivityFigure> feedbackFigures;
	/**
	 * Temporary workspace for creating feedback figures.
	 */
	protected Workspace dummyWorkspace;

	public ActivityEditPolicy() {
		this.feedbackFigures = new ArrayList<ActivityFigure>();
		DeclarativeProcessInstance dummyInstance = new DeclarativeProcessSchema().instantiate();
		dummyInstance.setStartTime(new DateTime(0, 0, true));
		this.dummyWorkspace = new Workspace(dummyInstance);
		this.dayToActivities = new HashMap<Day, List<Activity>>();
	}

	public void addFigure(ActivityFigure figure) {
		addFeedback(figure);
		feedbackFigures.add(figure);
	}

	/**
	 * Compute the feedback figure for given model
	 * 
	 * @param modelToFigure
	 *            a map holding already computed figures - the computed figure is added to the map
	 * @param entry
	 *            an map entry describing the figure
	 */
	private Map<Activity, ActivityFigure> computeFeedbackFigure(Map.Entry<Activity, TimeSlot> entry) {
		Map<Activity, ActivityFigure> modelToFigure = new HashMap<Activity, ActivityFigure>();
		Activity activity = entry.getKey();
		TimeSlot slot = entry.getValue();

		Date start = slot.getStart();
		Date current = new Date(start);
		DateTime end = new DateTime(slot.getEnd(), false);

		for (int i = start.getDay(); i <= end.getDay(); i++) {
			Day day = getDayFor(new Date(current));
			TimeSlot adaptedSlot = PixelTimeConverter.trimToDay(day.getDate(), slot);
			if (adaptedSlot == null) {
				continue; // do not need to draw empty feedback ...
			}

			Activity instance = getFeedbackHelper().addActivity(dummyWorkspace, activity, adaptedSlot);

			boolean rectangularStart = i != slot.getStart().getDay();
			boolean rectangularEnd = i != slot.getEnd().getDay();
			ActivityFeedbackFigure figure = getFeedbackHelper().createActivityFigure(instance, rectangularStart, rectangularEnd);
			feedbackFigures.add(figure);
			addFeedback(figure);
			modelToFigure.put(instance, figure);

			List<Activity> activities = dayToActivities.get(day);
			activities.add(instance);
			current.increaseDaysBy(1);
		}

		return modelToFigure;
	}

	protected Command createAddActivityCommand(DateTime start, List<DeclarativeActivity> activities) {
		return new AddActivityCommand(getWorkspaceEditPart().getModel(), activities, start);
	}

	/**
	 * Create the feedback figure for the given activities.
	 * 
	 * @param figureModels
	 *            the models for the figures to be computed
	 */
	public void createFeedback(Map<Activity, TimeSlot> figureModels) {
		Map<Activity, ActivityFigure> modelToFigure = new HashMap<Activity, ActivityFigure>();

		// first compute all figures
		for (Map.Entry<Activity, TimeSlot> entry : figureModels.entrySet()) {
			Map<Activity, ActivityFigure> figures = computeFeedbackFigure(entry);
			modelToFigure.putAll(figures);
		}

		// then compute the layout
		getFeedbackHelper().computeLayout(modelToFigure);
	}

	public void eraseFeedback() {
		for (IFigure figure : feedbackFigures) {
			removeFeedback(figure);
		}

		feedbackFigures.clear();
		DeclarativeProcessInstance dummyInstance = new DeclarativeProcessSchema().instantiate();
		dummyInstance.setStartTime(new DateTime(0, 0, true));
		dummyWorkspace = new Workspace(dummyInstance);
		dayToActivities.clear();
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		eraseFeedback();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Command getCommand(Request request) {
		if (REQUEST_ADD_ACTIVITY.equals(request.getType())) {
			Point dropLocation = (Point) request.getExtendedData().get(TDMConstants.KEY_DROP_LOCATION);
			DateTime start = getWorkspaceEditPart().getConverter().computeDateTime(dropLocation);

			List<DeclarativeActivity> activities = new ArrayList<DeclarativeActivity>((List) request.getExtendedData().get(
					TDMConstants.KEY_ACTIVITIES));
			return createAddActivityCommand(start, activities);
		}

		return null;
	}

	private Day getDayFor(Date date) {
		Day day = dummyWorkspace.getDay(date);

		if (day == null) {
			day = dummyWorkspace.addDay(date);
		}
		if (!dayToActivities.containsKey(day)) {
			dayToActivities.put(day, new ArrayList<Activity>());
		}

		return day;
	}

	public IFeedbackHelper getFeedbackHelper() {
		return new ActivityFeedbackHelper();
	}

	@Override
	public EditPart getTargetEditPart(Request request) {
		if (REQ_ADD.equals(request.getType())) {
			return ((GenericEditPart) getHost()).getEditPart(PlanningAreaEditPart.class);
		}
		if (REQUEST_ADD_ACTIVITY.equals(request.getType())) {
			return getWorkspaceEditPart();
		}

		return getHost();
	}

	protected WorkspaceEditPart getWorkspaceEditPart() {
		EditPart current = getHost();
		while (!(current instanceof WorkspaceEditPart)) {
			current = current.getParent();
		}

		return (WorkspaceEditPart) current;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void showTargetFeedback(Request request) {
		if (TDMConstants.REQUEST_ADD_ACTIVITY.equals(request.getType())) {
			List<DeclarativeActivity> activities = (List<DeclarativeActivity>) request.getExtendedData().get(TDMConstants.KEY_ACTIVITIES);
			Point dropLocation = (Point) request.getExtendedData().get(TDMConstants.KEY_DROP_LOCATION);
			DateTime start = getWorkspaceEditPart().getConverter().computeDateTime(dropLocation);

			eraseTargetFeedback(request);

			DeclarativeProcessInstance dummyInstance = new DeclarativeProcessSchema().instantiate();
			dummyInstance.setStartTime(new DateTime(0, 0, true));
			Workspace dummy = new Workspace(dummyInstance);
			Map<Activity, TimeSlot> figureModels = new HashMap<Activity, TimeSlot>();
			for (DeclarativeActivity activityDefinition : activities) {
				DateTime end = new DateTime(start, false);
				end.plus(activityDefinition.getExpectedDuration());
				TimeSlot slot = new TimeSlot(start, end);

				Day day = dummy.getDays().get(0);
				Activity activity = new Activity(day, activityDefinition.instantiate(dummy.getProcessInstance()));
				figureModels.put(activity, slot);
			}

			createFeedback(figureModels);
		}
	}

	public boolean supports(EditPart editPart) {
		return editPart.getClass().equals(ActivityEditPart.class);
	}

}

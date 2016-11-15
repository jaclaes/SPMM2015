package org.cheetahplatform.tdm.daily.editpart;

import static org.cheetahplatform.tdm.TDMConstants.REQUEST_ADD_ACTIVITY;
import static org.cheetahplatform.tdm.daily.editpart.ActivityEditPart.SPACING_X;

import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.modeler.decserflow.figure.ActivityFigure;
import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.editpart.IGenericEditPart;
import org.cheetahplatform.tdm.daily.figure.ActivityFeedbackFigure;
import org.cheetahplatform.tdm.daily.figure.DayTimeLineFigure;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.ActivityLayouter;
import org.cheetahplatform.tdm.daily.model.Day;
import org.cheetahplatform.tdm.daily.model.ExecutionAssertion;
import org.cheetahplatform.tdm.daily.model.IActivityContainer;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.daily.policy.ActivityEditPolicy;
import org.cheetahplatform.tdm.daily.policy.AddExecutionAssertionCommand;
import org.cheetahplatform.tdm.daily.policy.IFeedbackHelper;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

import com.swtdesigner.SWTResourceManager;

public class ExecutionAssertionEditPolicy extends ActivityEditPolicy {
	private class ExecutionAssertionContainer extends FeedbackActivityContainer {

		public ExecutionAssertionContainer(Day day) {
			super(day);
		}

		@Override
		public int getAvailableWidth() {
			return ExecutionAssertionAreaEditPart.WIDTH;
		}

	}

	private class ExecutionAssertionFeedbackHelper implements IFeedbackHelper {

		@Override
		public Activity addActivity(Workspace dummyWorkspace, Activity activity, TimeSlot adaptedSlot) {
			Day day = dummyWorkspace.getDay(adaptedSlot.getStart());
			ExecutionAssertion assertion = new ExecutionAssertion(day, adaptedSlot, activity.getActivityDefinition());
			dummyWorkspace.addAssertion(assertion);

			return assertion;
		}

		@Override
		public void computeLayout(Map<Activity, ActivityFigure> modelToFigure) {
			WorkspaceEditPart workspaceEditPart = (WorkspaceEditPart) ((IGenericEditPart) getHost()).getEditPart(WorkspaceEditPart.class);
			int scrolledX = workspaceEditPart.getScrolledX();

			for (Day day : dummyWorkspace.getDays()) {
				IActivityContainer container = new ExecutionAssertionContainer(day);
				ActivityLayouter layouter = new ActivityLayouter(container);
				layouter.layout();

				for (Activity activity : container.getActivities()) {
					ActivityFigure figure = modelToFigure.get(activity);

					Rectangle bounds = figure.getBounds();
					PixelTimeConverter converter = getWorkspaceEditPart().getConverter();
					bounds.x = PlanningAreaEditPart.WIDTH + activity.getX(day) + converter.computeXAbsolute(day.getDate()) - SPACING_X
							- scrolledX;
					bounds.y = converter.computeYAbsolute(day.getDate(), activity.getTimeSlot());
					bounds.width = activity.getWidth(day) - 2 * SPACING_X;
					bounds.height = activity.getHeight(day) + 2;
				}
			}
		}

		@Override
		public ActivityFeedbackFigure createActivityFigure(Activity activity, boolean rectangularStart, boolean rectangularEnd) {
			ActivityFeedbackFigure figure = new ActivityFeedbackFigure(activity.getName(), rectangularStart, rectangularEnd);
			figure.setBackgroundColor(SWTResourceManager.getColor(activity.getBackgroundColor()));
			figure.setState(activity.getState());

			return figure;
		}

		@Override
		public Dimension getDefaultFeedbackFigureSize() {
			return new Dimension(ExecutionAssertionAreaEditPart.WIDTH - 10, DayTimeLineFigure.HOUR_HEIGHT);
		}

	}

	@Override
	protected Command createAddActivityCommand(DateTime start, List<DeclarativeActivity> activities) {
		return new AddExecutionAssertionCommand(getWorkspaceEditPart().getModel(), start, activities);
	}

	@Override
	public IFeedbackHelper getFeedbackHelper() {
		return new ExecutionAssertionFeedbackHelper();
	}

	@Override
	public EditPart getTargetEditPart(Request request) {
		if (REQ_ADD.equals(request.getType())) {
			return ((GenericEditPart) getHost()).getEditPart(ExecutionAssertionAreaEditPart.class);
		}
		if (REQUEST_ADD_ACTIVITY.equals(request.getType())) {
			return getWorkspaceEditPart();
		}

		return getHost();
	}

	@Override
	public boolean supports(EditPart editPart) {
		return editPart.getClass().equals(ExecutionAssertionEditPart.class);
	}
}

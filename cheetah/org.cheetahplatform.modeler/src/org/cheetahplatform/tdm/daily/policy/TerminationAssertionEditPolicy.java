package org.cheetahplatform.tdm.daily.policy;

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
import org.cheetahplatform.tdm.daily.editpart.ExecutionAssertionAreaEditPart;
import org.cheetahplatform.tdm.daily.editpart.PixelTimeConverter;
import org.cheetahplatform.tdm.daily.editpart.PlanningAreaEditPart;
import org.cheetahplatform.tdm.daily.editpart.TerminationAssertionAreaEditPart;
import org.cheetahplatform.tdm.daily.editpart.TerminationAssertionEditPart;
import org.cheetahplatform.tdm.daily.editpart.WorkspaceEditPart;
import org.cheetahplatform.tdm.daily.figure.ActivityFeedbackFigure;
import org.cheetahplatform.tdm.daily.figure.DayTimeLineFigure;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.ActivityLayouter;
import org.cheetahplatform.tdm.daily.model.Day;
import org.cheetahplatform.tdm.daily.model.IActivityContainer;
import org.cheetahplatform.tdm.daily.model.TerminationAssertion;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

import com.swtdesigner.SWTResourceManager;

public class TerminationAssertionEditPolicy extends ActivityEditPolicy {
	private class TerminationAssertionContainer extends FeedbackActivityContainer {

		public TerminationAssertionContainer(Day day) {
			super(day);
		}

		@Override
		public int getAvailableWidth() {
			return TerminationAssertionAreaEditPart.WIDTH;
		}

	}

	private class TerminationAssertionFeedbackHelper implements IFeedbackHelper {

		@Override
		public Activity addActivity(Workspace dummyWorkspace, Activity activity, TimeSlot adaptedSlot) {
			Day day = dummyWorkspace.getDay(adaptedSlot.getStart());
			TerminationAssertion assertion = new TerminationAssertion(day, adaptedSlot);
			dummyWorkspace.addAssertion(assertion);

			return assertion;
		}

		@Override
		public void computeLayout(Map<Activity, ActivityFigure> modelToFigure) {
			WorkspaceEditPart workspaceEditPart = (WorkspaceEditPart) ((IGenericEditPart) getHost()).getEditPart(WorkspaceEditPart.class);
			int scrolledX = workspaceEditPart.getScrolledX();

			for (Day day : dummyWorkspace.getDays()) {
				IActivityContainer container = new TerminationAssertionContainer(day);
				ActivityLayouter layouter = new ActivityLayouter(container);
				layouter.layout();

				for (Activity activity : container.getActivities()) {
					ActivityFigure figure = modelToFigure.get(activity);

					Rectangle bounds = figure.getBounds();
					PixelTimeConverter converter = getWorkspaceEditPart().getConverter();
					bounds.x = PlanningAreaEditPart.WIDTH + ExecutionAssertionAreaEditPart.WIDTH + activity.getX(day)
							+ converter.computeXAbsolute(day.getDate()) - SPACING_X - scrolledX;
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
			return new Dimension(TerminationAssertionAreaEditPart.WIDTH - 10, DayTimeLineFigure.HOUR_HEIGHT);
		}

	}

	@Override
	protected Command createAddActivityCommand(DateTime start, List<DeclarativeActivity> activities) {
		Workspace workspace = getWorkspaceEditPart().getModel();
		TimeSlot selection = workspace.getTerminationAreaSelection();

		return new AddTerminationAssertionCommand(getWorkspaceEditPart().getModel(), selection);
	}

	@Override
	public IFeedbackHelper getFeedbackHelper() {
		return new TerminationAssertionFeedbackHelper();
	}

	@Override
	public EditPart getTargetEditPart(Request request) {
		if (REQ_ADD.equals(request.getType())) {
			return ((GenericEditPart) getHost()).getEditPart(TerminationAssertionAreaEditPart.class);
		}
		if (REQUEST_ADD_ACTIVITY.equals(request.getType())) {
			return getWorkspaceEditPart();
		}

		return getHost();
	}

	@Override
	public boolean supports(EditPart editPart) {
		return editPart.getClass().equals(TerminationAssertionEditPart.class);
	}
}

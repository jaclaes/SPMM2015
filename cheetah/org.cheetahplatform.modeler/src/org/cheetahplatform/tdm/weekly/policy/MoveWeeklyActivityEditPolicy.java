/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.policy;

import static org.cheetahplatform.tdm.TDMConstants.KEY_EDIT_POLICY_HANDLDER;

import java.util.List;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.modeler.decserflow.figure.ActivityFigure;
import org.cheetahplatform.modeler.generic.GEFUtils;
import org.cheetahplatform.modeler.generic.editpart.IGenericEditPart;
import org.cheetahplatform.tdm.ActivityNonResizableEditPolicy;
import org.cheetahplatform.tdm.daily.model.AbstractActivity;
import org.cheetahplatform.tdm.daily.policy.ActivityEditPolicy;
import org.cheetahplatform.tdm.modeler.test.model.MultiWeek;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.cheetahplatform.tdm.weekly.editpart.MultiWeekEditPart;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyActivityEditPart;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyEditPart;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;

public class MoveWeeklyActivityEditPolicy extends ActivityNonResizableEditPolicy {

	@Override
	public Command getCommand(Request request) {
		if (REQ_MOVE.equals(request.getType()) || REQ_ORPHAN.equals(request.getType())) {
			return handleMoveRequest((ChangeBoundsRequest) request);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private Command handleMoveRequest(ChangeBoundsRequest request) {
		Object handler = request.getExtendedData().get(KEY_EDIT_POLICY_HANDLDER);
		if (handler != null && !equals(handler)) {
			return null;// request handled by some other edit policy of another selected part
		}

		request.getExtendedData().put(KEY_EDIT_POLICY_HANDLDER, this);

		List<EditPart> editParts = request.getEditParts();
		CompoundCommand command = new CompoundCommand();

		for (EditPart uncasted : editParts) {
			if (!(uncasted instanceof WeeklyActivityEditPart)) {
				continue;

			}

			WeeklyActivityEditPart part = (WeeklyActivityEditPart) uncasted;
			EditPart target = part.getViewer().findObjectAt(request.getLocation());
			if (target == null || !(target instanceof IGenericEditPart)) {
				continue; // no target
			}

			IGenericEditPart targetWeekEditPart = ((IGenericEditPart) target).getEditPart(MultiWeekEditPart.class);
			if (targetWeekEditPart == null) {
				continue; // invalid target
			}
			boolean retarget = target instanceof IGenericEditPart && !targetWeekEditPart.equals(part.getEditPart(MultiWeekEditPart.class));
			MultiWeek week = (MultiWeek) part.getParent().getParent().getModel();
			WeeklyActivity activity = (WeeklyActivity) part.getModel();
			target = (EditPart) ((IGenericEditPart) target).getEditPart(MultiWeekEditPart.class);
			Point absoluteLocation = GEFUtils.getAbsoluteLocation(((AbstractGraphicalEditPart) target));

			int dropLocationRelativeToWeek = request.getLocation().x;
			dropLocationRelativeToWeek -= absoluteLocation.x;
			int weeksPerRow = WeeklyEditPart.getWeeksPerRow();
			int weekOffset = dropLocationRelativeToWeek / (((GraphicalEditPart) target).getFigure().getBounds().width / weeksPerRow);
			boolean moveable = ((AbstractActivity) part.getModel()).canBeMoved();

			if (retarget) {
				if (!moveable) {
					continue; // do not allow to move finished / running activities to other weeks
				}

				MultiWeek targetModel = (MultiWeek) target.getModel();
				Rectangle bounds = ((AbstractGraphicalEditPart) uncasted).getFigure().getBounds().getCopy();
				bounds.translate(request.getMoveDelta());
				bounds.translate(-absoluteLocation.x, -absoluteLocation.y);
				bounds.translate(0, -WeeklyActivityEditPart.HEIGHT);

				DateTime newStart = new DateTime(targetModel.getSlot().getStart());
				newStart.plus(new Duration(weekOffset * 7, 0, 0, true));
				if (!isProhibitedByMilestone(activity, new DateTime(newStart))) {
					command.add(new MoveWeeklyActivityToOtherWeekCommand(activity, targetModel, bounds.getLocation(), newStart));
				}
				continue;
			}

			Date newStart = new Date(week.getStartTime());
			newStart.plus(new Duration(7 * weekOffset, 0, 0, true));
			boolean targetIsSameWeek = newStart.sameWeek(activity.getStartTime());
			if (!moveable && !targetIsSameWeek) {
				continue; // do not allow to move finished / running activities to other weeks
			}

			boolean prohibitedByMilestone = isProhibitedByMilestone(activity, new DateTime(newStart));
			if (!prohibitedByMilestone) {
				command.add(new MoveWeeklyActivityCommand(activity, week, request.getMoveDelta(), weekOffset));
			}
		}

		if (command.isEmpty()) {
			return null;
		}

		return command;
	}

	private boolean isProhibitedByMilestone(WeeklyActivity activity, DateTime newStart) {
		return activity.getWorkspace().getWeekly().isMoveProhibitedByMilestone(activity, newStart);
	}

	@SuppressWarnings("unchecked")
	private void showFeedback(ChangeBoundsRequest request) {
		activityPolicy.eraseFeedback();

		for (EditPart uncasted : (List<EditPart>) request.getEditParts()) {
			if (!(uncasted instanceof WeeklyActivityEditPart)) {
				continue;
			}

			WeeklyActivity activity = (WeeklyActivity) uncasted.getModel();
			String name = activity.getActivity().getNode().getName();
			ActivityFigure figure = ActivityEditPolicy.createFeedbackFigure(name);

			Rectangle bounds = ((AbstractGraphicalEditPart) uncasted).getFigure().getBounds().getCopy();
			bounds.translate(request.getMoveDelta());
			figure.setBounds(bounds);

			activityPolicy.addFigure(figure);
		}
	}

	@Override
	protected void showSelection() {
		super.showSelection();

		((WeeklyActivityEditPart) getHost()).moveToTop();
	}

	@Override
	public void showSourceFeedback(Request request) {
		showTargetFeedback(request);
	}

	@Override
	public void showTargetFeedback(Request request) {
		if (REQ_MOVE.equals(request.getType()) || REQ_ADD.equals(request.getType())) {
			showFeedback((ChangeBoundsRequest) request);
		}
	}
}

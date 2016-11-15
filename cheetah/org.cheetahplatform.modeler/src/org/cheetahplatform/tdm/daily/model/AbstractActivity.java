/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.model;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.ACTIVATED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.COMPLETED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.CREATED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.LAUNCHED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.SKIPPED;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.tdm.GenericTDMModel;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.tdm.RoleLookup;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.cheetahplatform.tdm.dialog.ConstraintViolationDialog;
import org.cheetahplatform.tdm.modeler.test.model.MultiWeek;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractActivity extends GenericTDMModel {
	protected final DeclarativeActivityInstance activity;
	private RGB backgroundColor;
	private String tooltip;
	private long cheetahId;

	public AbstractActivity(IGenericModel parent, DeclarativeActivityInstance activity) {
		this(parent, activity, IIdentifiableObject.NO_ID);
	}

	public AbstractActivity(IGenericModel parent, DeclarativeActivityInstance activity, long cheetahId) {
		super(parent);

		this.activity = activity;
		if (cheetahId == IIdentifiableObject.NO_ID) {
			this.cheetahId = Services.getIdGenerator().generateId();
		} else {
			this.cheetahId = cheetahId;
		}
	}

	/**
	 * Checks all constraints if they are violated.
	 * 
	 * @return a list of violated constraints
	 */
	private List<IDeclarativeConstraint> calculateViolatedConstraints() {
		DeclarativeProcessInstance processInstance = getWorkspace().getProcessInstance();
		DeclarativeProcessSchema processSchema = processInstance.getSchema();
		Set<IDeclarativeConstraint> constraints = processSchema.getConstraints();

		List<IDeclarativeConstraint> violatedConstraints = new ArrayList<IDeclarativeConstraint>();
		for (IDeclarativeConstraint constraint : constraints) {
			if (!constraint.isExecutable((DeclarativeActivity) activity.getNode(), processInstance)) {
				violatedConstraints.add(constraint);
			}
		}

		return violatedConstraints;
	}

	public boolean canBeCanceled() {
		return isSameWeek() && activity.getState().equals(INodeInstanceState.LAUNCHED);
	}

	public boolean canBeCompleted() {
		INodeInstanceState state = activity.getState();
		return isSameWeek() && (state.equals(LAUNCHED) || state.equals(ACTIVATED) || state.equals(CREATED));
	}

	public boolean canBeLaunched() {
		INodeInstanceState state = activity.getState();
		boolean correctState = state.equals(ACTIVATED) || state.equals(CREATED) || state.equals(SKIPPED) || state.equals(COMPLETED);
		return correctState && isSameWeek();
	}

	public boolean canBeMoved() {
		return true;
	}

	public boolean canBeSkipped() {
		return !activity.getState().isFinal();
	}

	public void cancel() {
		activity.cancel();
		firePropertyChanged(TDMConstants.PROPERTY_STATE);
	}

	public void complete() {
		INodeInstanceState state = activity.getState();
		if (state.equals(CREATED) || state.equals(ACTIVATED)) {
			launch();
			if (!activity.getState().equals(INodeInstanceState.LAUNCHED))
				return;
		}

		activity.complete();
		firePropertyChanged(TDMConstants.PROPERTY_STATE);
	}

	/**
	 * Return the activity.
	 * 
	 * @return the activity
	 */
	public DeclarativeActivityInstance getActivity() {
		return activity;
	}

	public RGB getBackgroundColor() {
		if (backgroundColor != null) {
			return backgroundColor;
		}

		if (!isCompleted()) {
			Role role = RoleLookup.getInstance().getRole(activity);

			if (role != null) {
				return role.getColor();
			}
			return TDMConstants.COLOR_ACTIVITY_BOTTOM;
		}

		RGB color = TDMConstants.COLOR_ACTIVITY_TOP;
		RGB completedColor = new RGB(color.red, color.red, color.blue);
		float[] hsb = completedColor.getHSB();
		hsb[1] = Math.max(0, hsb[1] - 0.4f);
		hsb[2] = Math.max(0, hsb[2] - 0.4f);

		return new RGB(hsb[0], hsb[1], hsb[2]);
	}

	protected String getChangeTimeSlotCommand() {
		return TDMCommand.COMMAND_CHANGE_ACTIVITY_INSTANCE_TIME_SLOT;
	}

	public long getCheetahId() {
		return cheetahId;
	}

	public DateTime getEndTime() {
		return getTimeSlot().getEnd();
	}

	public DateTime getStartTime() {
		return getTimeSlot().getStart();
	}

	public INodeInstanceState getState() {
		return activity.getState();
	}

	public TimeSlot getTimeSlot() {
		return activity.getSlot();
	}

	/**
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	public boolean isCompleted() {
		return getState().isFinal();
	}

	private boolean isSameWeek() {
		return DateTimeProvider.getDateTimeSource().getCurrentTime(true).sameWeek(activity.getStartTime());
	}

	public void launch() {
		if (activity.getState().equals(CREATED) || activity.getState().equals(SKIPPED)) {
			activity.requestActivation();
		}

		List<IDeclarativeConstraint> violatedConstraints = calculateViolatedConstraints();
		if (!violatedConstraints.isEmpty()) {
			ConstraintViolationDialog dialog = new ConstraintViolationDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell(), violatedConstraints);
			if (dialog.open() == Window.CANCEL) {
				return;
			}

			AuditTrailEntry entry = new AuditTrailEntry(TDMConstants.LOG_EVENT_TYPE_CONSTRAINT_VIOLATION, String.valueOf(activity
					.getCheetahId()));
			for (int i = 0; i < violatedConstraints.size(); i++) {
				IDeclarativeConstraint constraint = violatedConstraints.get(i);
				entry.setAttribute(TDMConstants.ATTRIBUTE_CONSTRAINT + i, constraint.getCheetahId());
			}

			getWorkspace().getProcessInstance().log(entry);
		}

		activity.launch();
		firePropertyChanged(TDMConstants.PROPERTY_STATE);
	}

	public void launchAndComplete() {
		launch();
		complete();
	}

	/**
	 * @param backgroundColor
	 *            the backgroundColor to set
	 */
	public void setBackgroundColor(RGB backgroundColor) {
		this.backgroundColor = backgroundColor;

		firePropertyChanged(TDMConstants.PROPERTY_BACKGROUND_COLOR);
	}

	public void setCheetahId(long cheetahId) {
		this.cheetahId = cheetahId;
	}

	public void setStartTime(DateTime start) {
		setStartTime(start, true);
	}

	public void setStartTime(DateTime start, boolean firePropertyChange) {
		Duration duration = new Duration(0, getTimeSlot().getDurationInMinutes());
		DateTime end = new DateTime(start, false);
		end.plus(duration);
		setTimeSlot(new TimeSlot(start, end));

		MultiWeek newParent = getWorkspace().getWeekly().findWeek(start);
		setParent(newParent);

		if (firePropertyChange) {
			firePropertyChanged(Workspace.class, TDMConstants.PROPERTY_ACTIVITIES);
		}
	}

	public void setTimeSlot(TimeSlot newSlot) {
		activity.setTimeSlot(newSlot);
		firePropertyChanged(Workspace.class, TDMConstants.PROPERTY_ACTIVITIES);

		AuditTrailEntry entry = new AuditTrailEntry(getChangeTimeSlotCommand());
		entry.setAttribute(AbstractGraphCommand.ID, getCheetahId());
		entry.setAttribute(TDMCommand.ATTRIBUTE_TIME_SLOT, getTimeSlot().toStringRepresentation());
		entry.setAttribute(TDMCommand.ATTRIBUTE_ACTIVITY_NAME, activity.getNode().getName());
		getWorkspace().log(entry);
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
		firePropertyChanged(TDMConstants.PROPERTY_TOOLTIP);
	}

	public void skip() {
		if (activity.getState().equals(LAUNCHED)) {
			activity.cancel();
		}
		activity.skip(true);
		firePropertyChanged(TDMConstants.PROPERTY_STATE);
	}

}

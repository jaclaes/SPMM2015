package org.cheetahplatform.tdm.daily.model;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.DELETE_NODE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.RENAME;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.command.TDMCommand;

public abstract class Assertion extends Activity implements PropertyChangeListener {

	private TimeSlot slot;
	private final DeclarativeActivity activity;
	protected boolean positive;

	public Assertion(Day day, TimeSlot slot, DeclarativeActivity activity, long cheetahId) {
		super(day, null, cheetahId);

		this.slot = slot;
		this.activity = activity;
		this.positive = true;
	}

	@Override
	public abstract void delete();

	@Override
	public DeclarativeActivity getActivityDefinition() {
		return activity;
	}

	@Override
	public String getName() {
		return activity.getName();
	}

	@Override
	public INodeInstanceState getState() {
		if (positive) {
			return INodeInstanceState.COMPLETED;
		}

		return INodeInstanceState.SKIPPED;
	}

	protected abstract String getSwitchExpectationStateCommand();

	@Override
	public TimeSlot getTimeSlot() {
		return slot;
	}

	protected boolean isPositive() {
		return positive;
	}

	@Override
	public boolean occursAt(Day day) {
		return slot.includesDay(day.getDate());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if (DELETE_NODE.equals(name)) {
			delete();
		} else if (RENAME.equals(name)) {
			firePropertyChanged(ModelerConstants.PROPERTY_NAME);
		}
	}

	public void setPositive(boolean positive) {
		this.positive = positive;

		firePropertyChanged(TDMConstants.PROPERTY_STATE);
		firePropertyChanged(Workspace.class, TDMConstants.PROPERTY_STATE);

		AuditTrailEntry entry = new AuditTrailEntry(getSwitchExpectationStateCommand());
		entry.setAttribute(AbstractGraphCommand.ID, getCheetahId());
		entry.setAttribute(TDMCommand.ATTRIBUTE_EXPECTED_STATE, positive);
		getWorkspace().log(entry);
	}

	@Override
	public void setTimeSlot(TimeSlot newSlot) {
		this.slot = newSlot;

		firePropertyChanged(Workspace.class, TDMConstants.PROPERTY_ACTIVITIES);
		AuditTrailEntry entry = new AuditTrailEntry(getChangeTimeSlotCommand());
		entry.setAttribute(AbstractGraphCommand.ID, getCheetahId());
		entry.setAttribute(TDMCommand.ATTRIBUTE_TIME_SLOT, getTimeSlot().toStringRepresentation());
		entry.setAttribute(TDMCommand.ATTRIBUTE_ACTIVITY_NAME, activity.getName());
		getWorkspace().log(entry);
	}

}

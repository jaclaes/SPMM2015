package org.cheetahplatform.tdm.daily.model;

import java.util.List;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.tdm.daily.editpart.TerminationAssertionAreaEditPart;
import org.eclipse.gef.EditPart;

public class TerminationAssertionArea extends AbstractPlanningArea {

	public TerminationAssertionArea(Day parent) {
		super(parent, TerminationAssertionAreaEditPart.WIDTH);
	}

	public void addAssertion(TimeSlot timeSelection) {
		TerminationAssertion assertion = new TerminationAssertion(getParent(), timeSelection);
		getWorkspace().addAssertion(assertion);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new TerminationAssertionAreaEditPart(this);
	}

	@Override
	public List<Activity> getActivities() {
		return getWorkspace().getTerminationAssertions(getParent());
	}

	@Override
	public List<? extends Object> getChildren() {
		return getActivities();
	}

}

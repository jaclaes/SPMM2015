package org.cheetahplatform.tdm.daily.action;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.tdm.daily.editpart.AbstractPlanningAreaEditPart;
import org.cheetahplatform.tdm.daily.model.TerminationAssertionArea;
import org.eclipse.draw2d.geometry.Point;

import com.swtdesigner.ResourceManager;

public class CreateTerminationAssertionAction extends AbstractActivityTimeSlotAction {

	private static final String ID = "org.cheetahplatform.tdm.daily.action.CreateTerminationAssertionAction";

	public CreateTerminationAssertionAction(AbstractPlanningAreaEditPart planningAreaEditPart, Point location) {
		super(planningAreaEditPart, location);

		setId(ID);
		setText("Create Termination Assertion");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/add_execution_assertion.png"));
	}

	@Override
	protected void doRun(TimeSlot timeSelection, DeclarativeActivity activity) {
		TerminationAssertionArea assertionArea = (TerminationAssertionArea) planningAreaEditPart.getModel();
		assertionArea.addAssertion(timeSelection);
		planningAreaEditPart.setSelection(null);
	}

	@Override
	protected DeclarativeActivity getActivity() {
		return new DeclarativeActivity("dummy");
	}

}

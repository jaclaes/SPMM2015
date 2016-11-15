package org.cheetahplatform.tdm.daily.action;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.tdm.daily.editpart.AbstractPlanningAreaEditPart;
import org.eclipse.draw2d.geometry.Point;

import com.swtdesigner.ResourceManager;

public class AddActivityInstanceAction extends AbstractActivityTimeSlotAction {
	public static final String ID = "org.cheetahplatform.tdm.daily.model.AddActivityAction";

	public AddActivityInstanceAction(AbstractPlanningAreaEditPart planningAreaEditPart, Point location) {
		super(planningAreaEditPart, location);

		setId(ID);
		setText("Instantiate Existing Activity");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/add_activity.png"));
	}

	@Override
	protected void doRun(TimeSlot timeSelection, DeclarativeActivity activity) {
		addActivity(timeSelection, activity);
	}

	@Override
	protected DeclarativeActivity getActivity() {
		return getExistingActivity();
	}

}

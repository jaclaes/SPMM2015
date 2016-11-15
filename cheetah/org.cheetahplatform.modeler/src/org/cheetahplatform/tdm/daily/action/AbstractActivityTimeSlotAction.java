package org.cheetahplatform.tdm.daily.action;

import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_DECLARATIVE_PROCESS_SCHEMA;

import java.util.List;

import org.cheetahplatform.common.INamed;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.tdm.daily.editpart.AbstractPlanningAreaEditPart;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.Day;
import org.cheetahplatform.tdm.daily.model.ExecutionAssertion;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.daily.policy.ActivityChangeListener;
import org.cheetahplatform.tdm.dialog.SelectNamedElementDialog;
import org.cheetahplatform.tdm.modeler.declarative.TDMDeclarativeModelerView;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractActivityTimeSlotAction extends AbstractTimeSlotAction {

	public AbstractActivityTimeSlotAction(AbstractPlanningAreaEditPart planningAreaEditPart, Point location) {
		super(planningAreaEditPart, location);
	}

	protected void addActivity(TimeSlot timeSelection, DeclarativeActivity activity) {
		DeclarativeProcessInstance instance = planningAreaEditPart.getWorkspace().getModel().getProcessInstance();
		DeclarativeActivityInstance activityInstance = activity.instantiate(instance);
		activityInstance.setTimeSlot(timeSelection);
		Activity activityUiModel = getWorkspace().addActivityInstance(activityInstance);
		planningAreaEditPart.setSelection(null);

		Services.getModificationService().addListener(new ActivityChangeListener(activityUiModel), activity);
	}

	protected void addExecutionAssertion(TimeSlot slot, DeclarativeActivity activity) {
		Workspace workspace = planningAreaEditPart.getWorkspace().getModel();
		Day day = workspace.getDay(slot.getStart());
		ExecutionAssertion assertion = new ExecutionAssertion(day, slot, activity);
		workspace.addAssertion(assertion);
	}

	protected DeclarativeActivity createNewActivity() {
		TDMDeclarativeModelerView view = (TDMDeclarativeModelerView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(TDMDeclarativeModelerView.ID);

		return view.createActivity();
	}

	@Override
	protected final void doRun(TimeSlot timeSelection) {
		DeclarativeActivity activity = getActivity();
		if (activity == null) {
			return;
		}

		doRun(timeSelection, activity);
	}

	protected abstract void doRun(TimeSlot timeSelection, DeclarativeActivity activity);

	protected abstract DeclarativeActivity getActivity();

	@SuppressWarnings("unchecked")
	protected DeclarativeActivity getExistingActivity() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		List<IIdentifiableObject> schemata = Services.getCheetahObjectLookup()
				.getObjectsFromNamespace(NAMESPACE_DECLARATIVE_PROCESS_SCHEMA);
		DeclarativeProcessSchema schema = (DeclarativeProcessSchema) schemata.get(0);
		List<? extends INamed> nodes = schema.getNodes();
		SelectNamedElementDialog dialog = new SelectNamedElementDialog(shell, (List<INamed>) nodes);
		if (dialog.open() != Window.OK) {
			return null;
		}

		DeclarativeActivity activity = (DeclarativeActivity) dialog.getActivity();
		return activity;
	}

}

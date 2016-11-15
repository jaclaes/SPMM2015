package org.cheetahplatform.testarossa.action;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.testarossa.TestaRossaModel;

public class ShowCompletedActivitiesAction extends ProcessInstanceSpecificAction {
	public static final String ID = "org.cheetaplatform.testarossa.actions.FilterCompletedActivitiesAction";

	public ShowCompletedActivitiesAction() {
		super(ID, "img/filter_completed.png", null, AS_CHECK_BOX);

		setToolTipText("Show Completed Activities");
	}

	@Override
	public void instanceChanged() {
		super.instanceChanged();
		firePropertyChange(CHECKED, isChecked(), !isChecked());
	}

	@Override
	public boolean isChecked() {
		if (!isEnabled()) {
			return false;
		}

		return TestaRossaModel.getInstance().getCurrentWorkspace().isShowCompletedActivities();
	}

	@Override
	public void run() {
		boolean newState = !isChecked();
		TestaRossaModel.getInstance().getCurrentWorkspace().setShowCompletedActivities(newState);
		Activator.getDefault().getPreferenceStore().setValue(TDMConstants.KEY_SHOW_COMPLETED_ACTIVITIES, newState);
		firePropertyChange(CHECKED, newState, !newState);
	}
}

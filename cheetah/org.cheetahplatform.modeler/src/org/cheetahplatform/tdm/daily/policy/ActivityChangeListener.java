package org.cheetahplatform.tdm.daily.policy;

import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_NAME;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.DELETE_NODE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.RENAME;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.action.AbstractReplayAction;
import org.cheetahplatform.tdm.daily.model.Activity;

public class ActivityChangeListener implements PropertyChangeListener {

	private final Activity activity;

	public ActivityChangeListener(Activity activity) {
		this.activity = activity;
	}

	public void detach() {
		Services.getModificationService().removeListener(this, null);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (AbstractReplayAction.REPLAY_ACTIVE) {
			return; // logged commands will take care of the change propagation
		}

		if (RENAME.equals(event.getPropertyName())) {
			activity.firePropertyChanged(PROPERTY_NAME);
		} else if (DELETE_NODE.equals(event.getPropertyName())) {
			activity.delete();
			detach();
		}
	}

}

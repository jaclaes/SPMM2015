package org.cheetahplatform.tdm.daily.action;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.tdm.daily.model.ExecutionAssertion;
import org.eclipse.jface.action.Action;

import com.swtdesigner.ResourceManager;

public class ChangeExecutionAssertionTypeAction extends Action {
	public static final String ID = "org.cheetahplatform.tdm.daily.action.SwitchExecutionAssertionTypeAction";

	private final ExecutionAssertion assertion;
	private final boolean newState;

	public ChangeExecutionAssertionTypeAction(ExecutionAssertion assertion, boolean newState) {
		this.assertion = assertion;
		this.newState = newState;

		setId(ID);
		if (newState) {
			setText("Activity must be executable");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/decserflow/completed.png"));
		} else {
			setText("Activity must *not* be executable");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/decserflow/skipped.gif"));
		}
	}

	@Override
	public void run() {
		assertion.setExecutable(newState);
	}
}

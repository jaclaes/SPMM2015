package org.cheetahplatform.tdm.daily.action;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.tdm.daily.model.TerminationAssertion;
import org.eclipse.jface.action.Action;

import com.swtdesigner.ResourceManager;

public class ChangeTerminationAssertionTypeAction extends Action {
	public static final String ID = "org.cheetahplatform.tdm.daily.action.ChangeTerminationAssertionTypeAction";

	private final TerminationAssertion assertion;
	private final boolean newState;

	public ChangeTerminationAssertionTypeAction(TerminationAssertion assertion, boolean newState) {
		this.assertion = assertion;
		this.newState = newState;

		setId(ID);
		if (newState) {
			setText("Process instance should be possible to terminate");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/decserflow/completed.png"));
		} else {
			setText("Process instance should *not* be possible to terminate");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/decserflow/skipped.gif"));
		}
	}

	@Override
	public void run() {
		assertion.setCanTerminate(newState);
	}
}

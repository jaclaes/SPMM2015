package org.cheetahplatform.modeler.action;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;
import org.eclipse.jface.action.Action;

public abstract class AbstractModelingAction extends Action {
	private int code;

	protected AbstractModelingAction(int code) {
		this.code = code;
	}

	@Override
	public void run() {
		try {
			ExperimentalWorkflowEngine.createEngine(code).start();
		} catch (Exception e) {
			Activator.logError("Could not instantiate the workflow engine.", e);
		}
	}
}

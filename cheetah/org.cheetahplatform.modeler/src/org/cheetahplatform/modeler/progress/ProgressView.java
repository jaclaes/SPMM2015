package org.cheetahplatform.modeler.progress;

import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ProgressView extends ViewPart {

	private class ExperimentalWorkflowListener implements Runnable {

		@Override
		public void run() {
			refresh();
		}

	}

	public static final String ID = "org.cheetahplatform.modeler.ProgressView";

	private ProgressViewComposite composite;

	@Override
	public void createPartControl(Composite parent) {
		composite = new ProgressViewComposite(parent, SWT.NONE);
		ExperimentalWorkflowEngine.addListener(new ExperimentalWorkflowListener());
	}

	public void refresh() {
		ExperimentalWorkflowEngine engine = ExperimentalWorkflowEngine.getEngine();
		if (engine == null) {
			return;
		}

		composite.getProgressBar().setMaximum(engine.getActivityCount());
		composite.getProgressBar().setState(engine.getCurrentActivityIndex());
	}

	@Override
	public void setFocus() {
		// ignore
	}

}

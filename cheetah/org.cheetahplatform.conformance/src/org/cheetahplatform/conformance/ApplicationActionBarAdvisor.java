package org.cheetahplatform.conformance;

import org.cheetahplatform.conformance.action.CheckConformanceAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private CheckConformanceAction checkConformanceAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		menuBar.add(checkConformanceAction);
	}

	@Override
	protected void makeActions(IWorkbenchWindow window) {
		checkConformanceAction = new CheckConformanceAction();
		register(checkConformanceAction);
	}

}

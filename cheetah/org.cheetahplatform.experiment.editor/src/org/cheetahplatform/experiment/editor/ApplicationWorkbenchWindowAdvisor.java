package org.cheetahplatform.experiment.editor;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(600, 400));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(false);
		configurer.setTitle("CHEETAH Experiment Editor");
	}
	
	private void hideActionSets() {
		// remove some action sets from the toolbar
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideActionSet("org.eclipse.ui.edit.text.actionSet.navigation");
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.hideActionSet("org.eclipse.ui.edit.text.actionSet.annotationNavigation");
	}

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		hideActionSets();
	}
	
	
}

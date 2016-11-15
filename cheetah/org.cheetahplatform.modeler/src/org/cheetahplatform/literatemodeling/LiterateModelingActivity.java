package org.cheetahplatform.literatemodeling;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.views.AssociationsView;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class LiterateModelingActivity extends AbstractModelingActivity {
	public static final String ID = "LITERATE_MODELING";

	public LiterateModelingActivity(LiterateModel initialGraph, Process process) {
		this(initialGraph, process, false);
	}

	public LiterateModelingActivity(LiterateModel initialGraph, Process process, boolean optional) {
		super(ID, EditorRegistry.LITERATE_MODELING, initialGraph, process, optional);
	}

	@Override
	protected void doExecute() {
		super.doExecute();
		// remove some action sets from the toolbar
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideActionSet("org.eclipse.ui.edit.text.actionSet.navigation");
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.hideActionSet("org.eclipse.ui.edit.text.actionSet.annotationNavigation");

		// set LiterateModeling Perspective
		IWorkbench workbench = PlatformUI.getWorkbench();
		IPerspectiveDescriptor perspective = workbench.getPerspectiveRegistry().findPerspectiveWithId(LiterateModelingPerspective.ID);
		IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow().getActivePage();
		activePage.setPerspective(perspective);

		try {
			AssociationsView assocView = (AssociationsView) activePage.showView(AssociationsView.ID);
			assocView.setInput((LiterateModel) getInitialGraph());
		} catch (PartInitException e) {
			Activator.logError("Unable to open view", e);
		}
	}

	@Override
	public Object getName() {
		return "Literate Modeling";
	}
}

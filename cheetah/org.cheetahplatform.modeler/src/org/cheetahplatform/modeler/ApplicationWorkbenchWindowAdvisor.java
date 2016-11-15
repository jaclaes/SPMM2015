package org.cheetahplatform.modeler;

import java.lang.reflect.InvocationTargetException;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private class PerspectiveListener implements IPerspectiveListener {

		@Override
		public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
			hideActionSets();
		}

		@Override
		public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) {
			// ignore
		}

	}

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	private void hideActionSets() {
		// remove some action sets from the toolbar
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideActionSet("org.eclipse.ui.edit.text.actionSet.navigation");
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.hideActionSet("org.eclipse.ui.edit.text.actionSet.annotationNavigation");
	}

	@Override
	public void postWindowOpen() {
		hideActionSets();
		// ensure that the actions sets are also hidden on any other perspective
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(new PerspectiveListener());

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setMaximized(true);
		if (!CheetahPlatformConfigurator.getBoolean(IConfiguration.START_EXPERIMENTAL_WORKFLOW_ENGINE_AUTOMATICALLY)) {
			return;
		}

		Display.getCurrent().asyncExec(new Runnable() {
			private ExperimentalWorkflowEngine engine;

			@Override
			public void run() {
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
				try {
					dialog.run(false, false, new IRunnableWithProgress() {

						@Override
						public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
							monitor.beginTask("Initializing...", IProgressMonitor.UNKNOWN);

							try {
								ExperimentalWorkflowEngine.initialize();
							} catch (Exception e) {
								Activator.logError("Unable to start workflow engine", e);
							}
						}
					});

					engine = ExperimentalWorkflowEngine.createEngine();
					engine.start();
				} catch (Exception e) {
					Activator.logError("Unable to start workflow engine", e);
				}
			}
		});
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
		configurer.setShellStyle((Integer) CheetahPlatformConfigurator.getObject(IConfiguration.SHELL_STYLE));
		configurer.setShowCoolBar(CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_TOOL_BAR));
		configurer.setShowMenuBar(CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_MENU_BAR));
		configurer.setShowStatusLine(false);
		configurer.setShowProgressIndicator(false);
		String name = (String) CheetahPlatformConfigurator.getObject(IConfiguration.SHELL_TITLE);
		if (name == null) {
			name = "Cheetah Experimental Platform";
		}

		configurer.setTitle(name);
	}
}

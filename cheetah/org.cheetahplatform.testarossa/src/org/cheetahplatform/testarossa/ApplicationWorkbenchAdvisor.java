package org.cheetahplatform.testarossa;

import java.lang.reflect.InvocationTargetException;

import org.cheetahplatform.testarossa.persistence.PersistentModel;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.service.prefs.BackingStoreException;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "org.cheetahplatform.testarossa.perspective";

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public void eventLoopException(Throwable exception) {
		super.eventLoopException(exception);

		exception.printStackTrace();
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	@Override
	public void postShutdown() {
		super.postShutdown();
		try {
			new InstanceScope().getNode(Activator.PLUGIN_ID).flush();
			new InstanceScope().getNode(org.cheetahplatform.modeler.Activator.PLUGIN_ID).flush();
		} catch (BackingStoreException e1) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not save the preferences.", e1);
			Activator.getDefault().getLog().log(status);
		}

		try {
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
			dialog.run(true, false, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Saving Process", 2);
					monitor.worked(1);
					PersistentModel.save();
					monitor.worked(1);
					try {
						monitor.beginTask("Saving Workspace", 1);
						ResourcesPlugin.getWorkspace().save(true, null);
						monitor.worked(1);
					} catch (CoreException e) {
						throw new InvocationTargetException(e);
					}
				}
			});
		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not save the workspace");
			Activator.getDefault().getLog().log(status);
		}
	}
}

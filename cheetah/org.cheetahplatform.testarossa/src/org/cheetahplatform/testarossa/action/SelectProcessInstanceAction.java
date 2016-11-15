package org.cheetahplatform.testarossa.action;

import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.testarossa.Activator;
import org.cheetahplatform.testarossa.TestaRossaModel;
import org.cheetahplatform.testarossa.dialog.SelectProcessInstanceDialog;
import org.cheetahplatform.testarossa.view.WeekView;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class SelectProcessInstanceAction extends Action {
	public static final String ID = "org.cheetahplatform.testarossa.actions.InstanceSelection";

	public SelectProcessInstanceAction() {
		setToolTipText("Select Instance");
		setId(ID);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/open.gif"));
	}

	@Override
	public void run() {
		SelectProcessInstanceDialog dialog = new SelectProcessInstanceDialog(Display.getDefault().getActiveShell());
		if (dialog.open() == Window.OK) {
			DeclarativeProcessInstance selection = dialog.getSelection();
			TestaRossaModel.getInstance().setCurrentInstance(selection);

			try {
				WeekView view = (WeekView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(WeekView.ID);
				view.refresh();
			} catch (PartInitException e) {
				IStatus status = new Status(IStatus.ERROR, org.cheetahplatform.testarossa.Activator.PLUGIN_ID, "Could not open view.");
				Activator.getDefault().getLog().log(status);
			}
		}

	}
}

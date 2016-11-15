package org.cheetahplatform.modeler.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractModelingEditorAction<T> extends Action {

	private Class<T> supported;

	public AbstractModelingEditorAction(Class<T> supported) {
		this.supported = supported;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPart activePart = window.getActivePage().getActivePart();

		if (activePart != null) {
			Object adapted = activePart.getAdapter(supported);
			if (adapted != null) {
				run((T) adapted);
				return;
			}
		}

		MessageDialog
				.openInformation(window.getShell(), "Unsupported editor", "This operation is invalid for the currently active editor.");
	}

	protected abstract void run(T t);
}

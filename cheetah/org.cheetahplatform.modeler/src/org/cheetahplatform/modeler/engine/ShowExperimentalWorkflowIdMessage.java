package org.cheetahplatform.modeler.engine;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.04.2010
 */
public class ShowExperimentalWorkflowIdMessage extends AbstractExperimentsWorkflowActivity {

	public ShowExperimentalWorkflowIdMessage() {
		super("SHOW_EXPERIMENTAL_ID");
	}

	@Override
	protected void doExecute() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			return;
		}

		Shell shell = window.getShell();
		MessageDialog.openInformation(shell, "Experiment Id",
				"The id generated for you is: " + ExperimentalWorkflowEngine.getProcessInstanceId()
						+ ". Please write it on your assignment sheet.");
	}

	@Override
	public Object getName() {
		return "Display Experimental Id";
	}

}

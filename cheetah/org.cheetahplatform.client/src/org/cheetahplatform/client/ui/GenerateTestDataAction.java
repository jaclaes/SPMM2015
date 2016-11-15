package org.cheetahplatform.client.ui;

import javax.jms.JMSException;

import org.cheetahplatform.client.Activator;
import org.cheetahplatform.client.ModificationService;
import org.cheetahplatform.client.UiUtils;
import org.cheetahplatform.client.jms.GenerateSchemaService;
import org.cheetahplatform.client.jms.TestDataGenerationService;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.ResourceManager;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         15.07.2009
 */
public class GenerateTestDataAction extends Action {

	public GenerateTestDataAction() {
		setText("Generate");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/generateTestData-16x16.png"));
		setToolTipText("Generates the database schema");
	}

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		boolean reCreate = MessageDialog.openQuestion(shell, "Create Database", "Do you really want to re-create the database schema?");

		if (!reCreate)
			return;

		GenerateSchemaService generateSchemaService = new GenerateSchemaService();
		TestDataGenerationService generateDataService = new TestDataGenerationService();

		try {
			IStatus status = generateSchemaService.synchronousRequest();
			if (!status.equals(Status.OK_STATUS)) {
				MessageDialog.openError(shell, "Fail", "Failed to create the database schema.");
				return;
			}

			status = generateDataService.synchronousRequest();
			if (status.equals(Status.OK_STATUS)) {
				ModificationService.broadcastChanges(ModificationService.SCHEMA_CREATED);
			}
		} catch (JMSException e) {
			UiUtils.showAndLogError("Failed to to create database.", e);
		}
	}
}

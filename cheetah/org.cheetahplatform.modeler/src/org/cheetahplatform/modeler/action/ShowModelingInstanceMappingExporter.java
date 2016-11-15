package org.cheetahplatform.modeler.action;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.tdm.dialog.EditMessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ShowModelingInstanceMappingExporter extends AbstractExporter {
	StringBuilder mapping = new StringBuilder();

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry) {
		String id = modelingInstance.getId();
		String assignedId = null;

		ProcessInstance processInstance = modelingInstance.getInstance();

		if (processInstance.isAttributeDefined(AbstractGraphCommand.ASSIGNED_ID)) {
			assignedId = processInstance.getAttribute(AbstractGraphCommand.ASSIGNED_ID);
		} else {
			assignedId = "Could not determine assigned id";
		}

		mapping.append(id);
		mapping.append("\t");
		mapping.append(assignedId);
		mapping.append("\n");
	}

	@Override
	public void exportFinished() {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				EditMessageDialog dialog = new EditMessageDialog(shell, mapping.toString(), false, "Process Instance Mapping",
						"The mapping between the ids presented to the user and the database ids.");

				dialog.open();
			}
		});

		super.exportFinished();
	}
}

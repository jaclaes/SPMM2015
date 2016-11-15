package org.cheetahplatform.modeler.action;

import org.cheetahplatform.common.ui.dialog.ExperimentalWorkflowElementDatabaseHandle;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.mapping.MapParagraphDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class MapParagraphAction extends AbstractOpenProcessInstanceAction {
	public static final String ID = "org.cheetahplatform.modeler.action.MapParagraphAction";

	public MapParagraphAction() {
		setId(ID);
		setText("Map Model Elements to Paragraphs");
	}

	@Override
	public void doRun(ProcessInstanceDatabaseHandle handle, ExperimentalWorkflowElementDatabaseHandle experimentalWorkflowElementHandle) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		MapParagraphDialog mappingDialog = new MapParagraphDialog(shell, handle);
		mappingDialog.open();
	}
}

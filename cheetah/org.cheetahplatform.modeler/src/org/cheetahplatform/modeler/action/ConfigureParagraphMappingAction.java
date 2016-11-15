package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.graph.mapping.MappingConfigurationDialog;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         20.10.2009
 */
public class ConfigureParagraphMappingAction extends Action {
	public static final String ID = "org.cheetahplatform.modeler.action.ConfigureParagraphMappingAction";

	public ConfigureParagraphMappingAction() {
		setId(ID);
		setText("Configure Paragraph Mapping");
	}

	@Override
	public void run() {
		MappingConfigurationDialog dialog = new MappingConfigurationDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		dialog.open();
	}
}

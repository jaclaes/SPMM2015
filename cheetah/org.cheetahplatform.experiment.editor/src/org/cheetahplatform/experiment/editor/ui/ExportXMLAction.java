package org.cheetahplatform.experiment.editor.ui;

import org.cheetahplatform.experiment.editor.Activator;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.action.Action;

import com.swtdesigner.ResourceManager;

public class ExportXMLAction extends Action {
	private final Graph initialModel;
	
	public ExportXMLAction(Graph initialModel) {
		this.initialModel = initialModel;
		setText("Export initial model to XML");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/export.gif")); //$NON-NLS-1$
	}

	@Override
	public void run() {
		ExportXMLComand.exportToXML(initialModel);
	}
}

package org.cheetahplatform.experiment.editor.ui;

import org.cheetahplatform.experiment.editor.Activator;
import org.cheetahplatform.modeler.experiment.editor.model.ModelingNode;
import org.cheetahplatform.modeler.experiment.editor.xml.ExpEditorMarshallerException;
import org.cheetahplatform.modeler.experiment.editor.xml.ExperimentEditorMarshaller;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class ExportXMLComand extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		
		NodeEditPart part = null;
		for (Object obj : selection.toList()){
			part = (NodeEditPart) obj;			
		}
		if (part != null){
			Graph initialModel = ((ModelingNode)part.getModel()).getInitialGraph();
			exportToXML(initialModel);
		}
		return null;
	}
	
	public static void exportToXML(Graph initialModel) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.xml" });
		dialog.setFilterNames(new String[] { "XML File" });
		String fileSelected = dialog.open();

		if (fileSelected != null) {
			ExperimentEditorMarshaller marsh = new ExperimentEditorMarshaller();
			try {
				marsh.marshallToFile(initialModel, fileSelected);
			} catch (ExpEditorMarshallerException ex) {
				Activator.log(IStatus.ERROR,
						"Unable to save initial model to file.", ex);
			}
		}
	}

}

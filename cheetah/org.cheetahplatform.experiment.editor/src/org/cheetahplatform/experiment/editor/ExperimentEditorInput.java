package org.cheetahplatform.experiment.editor;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.core.service.IIdGenerator;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.experiment.editor.model.ExperimentGraph;
import org.cheetahplatform.modeler.graph.GraphEditorInput;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class ExperimentEditorInput extends GraphEditorInput {
	
	private static int modelCnt = 1; //TODO use a random process Id?
	
	private String filePath;
	
	public ExperimentEditorInput(Graph graph, List<Attribute> attributes, Process process) {
		super(graph, attributes, process);
		
		 IIdGenerator idGenerator = Services.getIdGenerator();
		 long result = 0;
		 for (Object o : graph.getChildren()){
				GraphElement ga = (GraphElement) o;
				if (result < ga.getId()){
					result = ga.getId();
				}
			}
		 idGenerator.setMinimalId(result + 1);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public static int getNewModelCnt(){
		return modelCnt++;
	}
	
	public static ExperimentEditorInput createEditorInput(Graph graph){
		Process proc = new Process(String.valueOf(getNewModelCnt()));
		if (graph == null){
			graph = new ExperimentGraph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		}
		return new ExperimentEditorInput(graph, new ArrayList<Attribute>(),	proc);		
	}
	
	public static void openEditor(ExperimentEditorInput input){
		try {			
			if (input == null){
				input = createEditorInput(null);
			}
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().
				getActivePage().openEditor(input, ExperimentEditor.ID);
		} catch (PartInitException ex) {
			Activator.log(IStatus.ERROR, 
					 "Opening the editor failed", ex);
		}		
	}

	
}

package org.cheetahplatform.experiment.editor.ui;

import java.util.List;

import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.experiment.editor.ExperimentEditor;
import org.cheetahplatform.experiment.editor.ExperimentEditorInput;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.experiment.editor.xml.ExperimentEditorMarshaller;
import org.cheetahplatform.modeler.graph.command.CreateEdgeCommand;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.command.EdgeCommand;
import org.cheetahplatform.modeler.graph.command.NodeCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class PasteElementCommand extends Command {

	private List<Object> list;
	private Command command;
	
	@Override
	public boolean canExecute() {
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) Clipboard.getDefault().getContents();
		if (list == null || list.isEmpty()) {
			return false;
		} else {
			this.list = list;
			return true;
		}		
	}

	protected Command createCommand(Graph graph){
		CompoundCommand command = new CompoundCommand();
		Graph tmpGraph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		for (Object obj : list){
			if (obj instanceof Node){
				Node node = (Node) obj;
				tmpGraph.addNode(node);
				for (Edge edge : node.getSourceConnections()){
					if (list.contains(edge.getTarget())){
						tmpGraph.addEdge(edge);
					}
				}
			}			
		}
		ExperimentEditorMarshaller eem = new ExperimentEditorMarshaller();
		tmpGraph = (Graph) eem.unmarshall(eem.marshall(tmpGraph));	
		
		for (Node node : tmpGraph.getNodes()){
			Point newLoc = new Point(node.getLocation().x + 10, node.getLocation().y + 10);
			node.setId(Services.getIdGenerator().generateId());
			node.setParent(graph);
			NodeCommand nodeComm = new CreateNodeCommand(graph, node, newLoc, node.getName());
			command.add(nodeComm);
		}
		
		for (Edge edge : tmpGraph.getEdges()){
			edge.setId(Services.getIdGenerator().generateId());
			edge.setParent(graph);
			EdgeCommand edgeCommand = new CreateEdgeCommand(graph, edge, edge.getSource(), edge.getTarget(), edge.getName());
			command.add(edgeCommand);
		}
		return command;		
	}
	

	@Override
	public void execute() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		ExperimentEditor editor = (ExperimentEditor) page.getActiveEditor();
		//CommandStack commandStack = editor.getCommandStack();
		
		ExperimentEditorInput input = (ExperimentEditorInput) editor.getEditorInput();
		if (canExecute()){
			this.command = createCommand(input.getGraph());
			this.command.execute();
			//commandStack.execute(this.command);
		}
	}

	@Override
	public boolean canUndo() {
		return this.command.canExecute();
	}

	@Override
	public void undo() {
		this.command.undo();
	}
}

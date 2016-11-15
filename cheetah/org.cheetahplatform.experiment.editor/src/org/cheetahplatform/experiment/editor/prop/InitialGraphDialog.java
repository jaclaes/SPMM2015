package org.cheetahplatform.experiment.editor.prop;

import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.decserflow.DecSerFlowGraphAdvisor;
import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.command.DeleteNodeCommand;
import org.cheetahplatform.modeler.graph.command.RenameCommand;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.HierarchicalNode;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.modeler.hierarchical.HierarchicalOutlineComposite;
import org.cheetahplatform.modeler.hierarchical.OutlineViewNode;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;

public class InitialGraphDialog extends Dialog {

	private GraphicalGraphViewerWithFlyoutPalette viewer;
	private HierarchicalOutlineComposite hierarchicalOutline;
	private Composite graphComposite, container;
	private Graph graph;
	private List<INodeDescriptor> nodeDescriptors;
	private List<IEdgeDescriptor> edgeDescriptors;
	private String type;
	private Sash sash;
	
	public InitialGraphDialog(Shell parentShell, Graph graph, String type) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.SHELL_TRIM);
		this.graph = graph;
		this.type = type;
	}

	public Graph getGraph() {		
		if (graph == null) {
			graph = DefaultGraphicalGraphViewerAdvisor.createGraph(nodeDescriptors, edgeDescriptors);
		}
		return graph;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		
		CheetahPlatformConfigurator.enableFullAdminMode();
		nodeDescriptors = EditorRegistry.getNodeDescriptors(getType());
		edgeDescriptors = EditorRegistry.getEdgeDescriptors(getType());

		DefaultGraphicalGraphViewerAdvisor advisor;

		if (getType().equals(EditorRegistry.DECSERFLOW)){
			advisor = new DecSerFlowGraphAdvisor(nodeDescriptors, edgeDescriptors, getGraph());
			viewer = new GraphicalGraphViewerWithFlyoutPalette(container, advisor);	
		} else {
			advisor = new DefaultGraphicalGraphViewerAdvisor(nodeDescriptors, edgeDescriptors, getGraph());	
			createHierarchyLayout(container, advisor);
			
			OutlineViewNode<Graph> graphNode = new OutlineViewNode<Graph>("Process", "ID", graph, null);
			createOutline(getGraph(), graphNode);
			hierarchicalOutline.setInput(graphNode);
			hierarchicalOutline.addSelectionChangeListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					if (event.getSelection() instanceof IStructuredSelection) {
						IStructuredSelection selection = (IStructuredSelection) event.getSelection();
						@SuppressWarnings("rawtypes")
						Iterator it = selection.iterator();
						if (it.hasNext()) {
							@SuppressWarnings("unchecked")
							OutlineViewNode<Graph> node = (OutlineViewNode<Graph>) it.next();
							setGraphInput(node);
						}
					}
				}
			});
			attachCommandStackListener();
		}		
		return container;
	}
	
	private void createOutline(Graph graph, OutlineViewNode<Graph> graphNode) {
		for (Node node : graph.getNodes()){
			if (node instanceof HierarchicalNode){
				HierarchicalNode hierNode = (HierarchicalNode) node;
				OutlineViewNode<Graph> child = new OutlineViewNode<Graph>(node.getName(), String.valueOf(node.getId()),hierNode.getSubGraph(), graphNode);
				graphNode.addChild(child);
				createOutline(hierNode.getSubGraph(), child);
			}
		}
	}
	
	protected void setGraphInput(OutlineViewNode<Graph> mainNode) {
		if (graphComposite != null) {
			graphComposite.dispose();
		}
		graphComposite = new Composite(container, SWT.NONE);
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		data.left = new FormAttachment(sash, 0);
		data.right = new FormAttachment(100, 0);
		graphComposite.setLayoutData(data);

		Graph currGraph = mainNode.getData();
		DefaultGraphicalGraphViewerAdvisor advisor = new DefaultGraphicalGraphViewerAdvisor(
				EditorRegistry.getNodeDescriptors(EditorRegistry.BPMN), EditorRegistry.getEdgeDescriptors(EditorRegistry.BPMN), currGraph);

		viewer = new GraphicalGraphViewerWithFlyoutPalette(graphComposite, advisor);
		container.layout(true, true);
		graphComposite.layout(true, true);
		attachCommandStackListener();
	}
	
	private void attachCommandStackListener(){
		getCommandStack().addCommandStackListener(new CommandStackListener(){

			private void handleDeleteCommand(DeleteNodeCommand deleteCmd){
				if (deleteCmd.getNode() instanceof HierarchicalNode){
					HierarchicalNode node = (HierarchicalNode) deleteCmd.getNode();
					OutlineViewNode<Graph> selectedNode= hierarchicalOutline.getSelection();
					selectedNode.removeChild(String.valueOf(node.getId()));
					hierarchicalOutline.refresh();
				}
			}
			
			@Override
			public void commandStackChanged(EventObject event) {
				Command cmd = getCommandStack().getUndoCommand();
				if (cmd == null) return;
				if (cmd instanceof CreateNodeCommand){
					CreateNodeCommand createCmd = (CreateNodeCommand) cmd;
					if (createCmd.getNode() instanceof HierarchicalNode){
						HierarchicalNode node = (HierarchicalNode) createCmd.getNode();
						hierarchicalOutline.getSelection().addChild(
								new OutlineViewNode<Graph>(node.getName(), String.valueOf(node.getId()), node.getSubGraph(), hierarchicalOutline.getSelection()));
						hierarchicalOutline.refresh();
					}
				} else if (cmd instanceof DeleteNodeCommand){
					handleDeleteCommand((DeleteNodeCommand) cmd);					
				} else if (cmd instanceof CompoundCommand){
					for (Object obj : ((CompoundCommand) cmd).getCommands()){
						if (obj instanceof DeleteNodeCommand){
							handleDeleteCommand((DeleteNodeCommand)obj);
						}
					}
				} else if (cmd instanceof RenameCommand){
					RenameCommand renameCmd = (RenameCommand) cmd;
					if (renameCmd.getGraphElement() instanceof HierarchicalNode){ 
						HierarchicalNode node = (HierarchicalNode) renameCmd.getGraphElement();
						hierarchicalOutline.getSelection().renameChild(String.valueOf(node.getId()), node.getName());
						hierarchicalOutline.refresh();
					}
				}
				
			}
			
			
		});
	}
	
	
	
	private CommandStack getCommandStack(){
		return viewer.getViewer().getEditDomain().getCommandStack();
	}
	

	private void createHierarchyLayout(final Composite container, DefaultGraphicalGraphViewerAdvisor advisor) {
		container.setLayout(new FormLayout());

		sash = new Sash(container, SWT.VERTICAL);
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		data.left = new FormAttachment(15, 0);
		sash.setLayoutData(data);

		sash.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				sash.setBounds(e.x, e.y, e.width, e.height);

				FormData formData = new FormData();
				formData.top = new FormAttachment(0, 0);
				formData.left = new FormAttachment(0, e.x);
				formData.bottom = new FormAttachment(100, 0);
				sash.setLayoutData(formData);
				container.layout();
			}
		});

		hierarchicalOutline = new HierarchicalOutlineComposite(container, SWT.NONE);
		data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(sash, 0);
		hierarchicalOutline.setLayoutData(data);

		graphComposite = new Composite(container, SWT.NONE);
		data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		data.left = new FormAttachment(sash, 0);
		data.right = new FormAttachment(100, 0);
		graphComposite.setLayoutData(data);
		
		viewer = new GraphicalGraphViewerWithFlyoutPalette(graphComposite, advisor);	
	}

	public GraphicalGraphViewerWithFlyoutPalette getViewer(){
		return viewer;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Edit initial workflow");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 400);
	}

	public String getType() {
		return type;
	}

}

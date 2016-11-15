package org.cheetahplatform.tdm.modeler.declarative;

import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_DECLARATIVE_ACTIVITIES;
import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_GRAPH;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cheetahplatform.common.logging.IPromLogger;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.service.ICheetahObjectLookup;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.decserflow.DecSerFlowGraphAdvisor;
import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.tdm.engine.TDMProcess;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.ViewPart;

public class TDMDeclarativeModelerView extends ViewPart {
	private static class TDMOperation extends AbstractOperation {

		private final Command command;

		public TDMOperation(Command command) {
			super("some kinda label");

			this.command = command;
			addContext(IOperationHistory.GLOBAL_UNDO_CONTEXT);
		}

		@Override
		public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
			// execution is never performed directly, but through the graph command stack

			return Status.OK_STATUS;
		}

		@Override
		public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
			command.redo();

			return Status.OK_STATUS;
		}

		@Override
		public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
			command.undo();

			return Status.OK_STATUS;
		}

	}

	public static final String ID = "org.cheetahplatform.tdm.DeclarativeModeler";

	private TDMDeclarativeModelerViewComposite composite;
	private TDMDeclarativeModelerModel model;

	private GraphicalGraphViewerWithFlyoutPalette viewer;

	public DeclarativeActivity createActivity() {
		IGraphElementDescriptor descriptor = EditorRegistry.getDescriptor(EditorRegistry.DECSERFLOW_ACTIVITY);
		GraphElement node = descriptor.createModel(model.getGraph());

		Point point = viewer.getEmptySpace();
		CreateNodeCommand command = new CreateNodeCommand(model.getGraph(), (Node) node, point);
		if (!descriptor.canExecuteCreationCommand(command, node)) {
			return null;
		}

		viewer.getViewer().getEditDomain().getCommandStack().execute(command);
		return (DeclarativeActivity) Services.getCheetahObjectLookup().getObject(NAMESPACE_DECLARATIVE_ACTIVITIES, node.getId());
	}

	@Override
	public void createPartControl(Composite parent) {
		composite = new TDMDeclarativeModelerViewComposite(parent, SWT.NONE);
		model = new TDMDeclarativeModelerModel();
	}

	private void createViewer(Graph graph) {
		List<IEdgeDescriptor> edges = new ArrayList<IEdgeDescriptor>(EditorRegistry.getEdgeDescriptors(EditorRegistry.DECSERFLOW));
		List<INodeDescriptor> nodes = EditorRegistry.getNodeDescriptors(EditorRegistry.DECSERFLOW);

		Iterator<IEdgeDescriptor> iterator = edges.iterator();
		while (iterator.hasNext()) {
			IEdgeDescriptor descriptor = iterator.next();
			if (!CheetahPlatformConfigurator.getBoolean(descriptor.getId())) {
				iterator.remove();
			}
		}

		DefaultGraphicalGraphViewerAdvisor advisor = null;
		if (graph != null) {
			advisor = new DecSerFlowGraphAdvisor(nodes, edges, graph);
		} else {
			advisor = new DecSerFlowGraphAdvisor(nodes, edges);
		}

		viewer = new GraphicalGraphViewerWithFlyoutPalette(composite, advisor);
		viewer.getViewer().getEditDomain().getCommandStack().addCommandStackEventListener(new CommandStackEventListener() {

			@Override
			public void stackChanged(CommandStackEvent event) {
				if (event.isPostChangeEvent()) {
					IOperationHistory history = OperationHistoryFactory.getOperationHistory();
					try {
						history.execute(new TDMOperation(event.getCommand()), null, null);
					} catch (ExecutionException e) {
						Activator.logError("An error occurred.", e);
					}
				}
			}
		});
	}

	@Override
	public void dispose() {
		super.dispose();

		model.dispose();
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.equals(ScrollingGraphicalViewer.class)) {
			return viewer.getViewer();
		}
		if (adapter.equals(Graph.class)) {
			return viewer.getGraph();
		}

		return super.getAdapter(adapter);
	}

	/**
	 * @return the viewer
	 */
	public GraphicalGraphViewerWithFlyoutPalette getViewer() {
		return viewer;
	}

	@Override
	public void setFocus() {
		// ignore
	}

	public void setInput(TDMProcess process, IPromLogger logger) {
		ICheetahObjectLookup lookup = Services.getCheetahObjectLookup();

		Graph graph = process.toGraph();
		for (Control child : composite.getChildren()) {
			child.dispose();
		}

		createViewer(graph);
		model.setGraph(graph);
		model.setProcess(process);
		lookup.registerObject(NAMESPACE_GRAPH, new IdentifiableGraph(graph));

		graph.addLogListener(logger);
		graph.addLogListener(model);

		composite.layout(true, true);
	}
}

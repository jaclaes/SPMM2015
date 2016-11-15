package org.cheetahplatform.modeler.decserflow.descriptor;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.ID;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.NAME;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.SOURCE_NODE_ID;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.TARGET_NODE_ID;
import static org.eclipse.gef.RequestConstants.REQ_OPEN;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.declarative.constraint.SelectionConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.decserflow.CreateSelectionConstraintEdgeCommand;
import org.cheetahplatform.modeler.decserflow.EditSelectionConstraintAction;
import org.cheetahplatform.modeler.decserflow.EditSelectionConstraintCommand;
import org.cheetahplatform.modeler.decserflow.EditSelectionConstraintDialog;
import org.cheetahplatform.modeler.decserflow.SelectionConstraintEdge;
import org.cheetahplatform.modeler.decserflow.figure.SelectionConstraintFigure;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.editpart.EdgeEditPart;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class SelectionConstraintDescriptor extends SingleActivityConstraintDescriptor {

	public static final String MINIMUM = "minimum";
	public static final String MAXIMUM = "maximum";
	public static final String EDIT_SELECTION_CONSTRAINT = "EDIT_SELECTION_CONSTRAINT";

	public SelectionConstraintDescriptor() {
		super("img/decserflow/selection.png", "Selection", EditorRegistry.DECSERFLOW_SELECTION, new SelectionConstraint(DUMMY_A, 1, 2));
	}

	@Override
	public void buildContextMenu(EditPart editPart, IMenuManager menu, Point dropLocation) {
		super.buildContextMenu(editPart, menu, dropLocation);
		menu.add(new EditSelectionConstraintAction((EdgeEditPart) editPart));
	}

	@Override
	public boolean canExecuteCreationCommand(AbstractGraphCommand command, GraphElement element) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		SelectionConstraintEdge constraint = (SelectionConstraintEdge) element;

		EditSelectionConstraintDialog dialog = new EditSelectionConstraintDialog(shell, constraint);
		return dialog.open() == Window.OK;
	}

	@Override
	public AbstractGraphCommand createCommand(AuditTrailEntry entry, Graph graph) {
		String type = entry.getEventType();
		if (EDIT_SELECTION_CONSTRAINT.equals(type)) {
			int minimum = entry.getIntegerAttribute(MINIMUM);
			int maximum = entry.getIntegerAttribute(MAXIMUM);
			long constraintId = entry.getLongAttribute(ID);
			SelectionConstraintEdge constraint = (SelectionConstraintEdge) graph.getGraphElement(constraintId);

			return new EditSelectionConstraintCommand(constraint, minimum, maximum);
		}

		return null;
	}

	@Override
	public AbstractGraphCommand createCreateEdgeCommand(Graph graph, AuditTrailEntry entry) {
		Edge edge = (Edge) AbstractGraphCommand.createGraphElement(entry, graph);
		Node source = (Node) graph.getGraphElement(entry.getLongAttribute(SOURCE_NODE_ID));
		Node target = (Node) graph.getGraphElement(entry.getLongAttribute(TARGET_NODE_ID));
		String name = entry.getAttribute(NAME);
		int minimum = entry.getIntegerAttribute(MINIMUM);
		int maximum = entry.getIntegerAttribute(MAXIMUM);

		if (edge.getDescriptor().getName().equals(name)) {
			name = null;
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_AND_TASK_NAME)) {
			String add = " (" + entry.getAttribute(ID) + ")";
			if (name == null || name.equals("null")) {
				name = entry.getAttribute(ID);
			} else if (!name.equals(entry.getAttribute(ID)) && !name.contains(add)) {
				name += add;
			}
		} else if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_NOT_TASK_NAME)) {
			name = entry.getAttribute(ID);
		}

		return new CreateSelectionConstraintEdgeCommand(graph, edge, source, target, name, minimum, maximum);
	}

	@Override
	public AbstractGraphCommand createCreateEdgeCommand(Graph graph, Edge edge, Node source, Node target, String name) {
		return new CreateSelectionConstraintEdgeCommand(graph, edge, source, target, name);
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		return new SelectionConstraintFigure((Edge) element);
	}

	@Override
	public Edge createModel(Graph graph) {
		return new SelectionConstraintEdge(graph, this);
	}

	@Override
	public Edge createModel(Graph graph, long id, AuditTrailEntry entry) {
		return new SelectionConstraintEdge(graph, this, id);
	}

	@Override
	public String getCommandLabel(AuditTrailEntry entry) {
		String type = entry.getEventType();
		if (EDIT_SELECTION_CONSTRAINT.equals(type)) {
			String name = entry.getAttribute(AbstractGraphCommand.NAME);
			return "Edit selection constraint of " + name;
		}

		return "";
	}

	@Override
	public void performRequest(EditPart editPart, Request reqeuest) {
		if (REQ_OPEN.equals(reqeuest.getType())) {
			new EditSelectionConstraintAction((EdgeEditPart) editPart).run();
		}
	}
}

package org.cheetahplatform.modeler.decserflow.descriptor;

import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_DECLARATIVE_ACTIVITIES;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_INIT;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_SELECTION;
import static org.eclipse.gef.RequestConstants.REQ_CREATE;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.decserflow.figure.ActivityFigure;
import org.cheetahplatform.modeler.generic.PathFigureSelectionHandle;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.tdm.TDMConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.SWTResourceManager;

public class DecSerFlowActivityDescriptor extends NodeDescriptor {

	public DecSerFlowActivityDescriptor() {
		super("img/decserflow/activity.png", "Activity", EditorRegistry.DECSERFLOW_ACTIVITY);
	}

	@Override
	public void buildContextMenu(EditPart editPart, IMenuManager menu, Point dropLocation) {
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_CHANGING_OF_DECSERFLOW_ACTIVITY_DESCRIPTION)) {
			menu.add(new EditNodeDescriptionAction((NodeEditPart) editPart));
		}
	}

	@Override
	public boolean canExecuteCreationCommand(AbstractGraphCommand command, GraphElement element) {
		IGraphElementDescriptor activityDescriptor = EditorRegistry.getDescriptor(EditorRegistry.DECSERFLOW_ACTIVITY);

		if (element.getDescriptor().equals(activityDescriptor)) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			InputDialog dialog = new InputDialog(shell, "Name", "Please enter the name", "", new RenameDecSerFlowActivityValidator(
					element.getGraph(), null));
			if (dialog.open() != Window.OK) {
				return false;
			}

			((CreateNodeCommand) command).setName(dialog.getValue());
		}

		return true;
	}

	@Override
	public AbstractGraphCommand createCommand(AuditTrailEntry entry, Graph graph) {
		String event = entry.getEventType();
		if (event.equals(EditNodeDescriptionCommand.COMMAND_EDIT_DESCRIPTION)) {
			Node node = (Node) graph.getGraphElement(entry.getLongAttribute(AbstractGraphCommand.ID));
			return new EditNodeDescriptionCommand(node, entry.getAttribute(EditNodeDescriptionCommand.ATTRIBUTE_DESCRIPTION));
		}

		return null;
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		String label = element.getNameNullSafe();
		ActivityFigure figure = new ActivityFigure(label);
		figure.setSelectionForegroundColor(SWTResourceManager.getColor(TDMConstants.COLOR_ACTIVITY_BOTTOM));
		figure.setSelectionBackgroundColor(SWTResourceManager.getColor(TDMConstants.COLOR_ACTIVITY_TOP));

		return figure;
	}

	@Override
	public IInputValidator createRenameValidator(GraphElement element) {
		return new RenameDecSerFlowActivityValidator(element.getGraph(), (Node) element);
	}

	@Override
	public MoveHandle createSelectionHandle(NodeEditPart editPart) {
		return new PathFigureSelectionHandle(editPart);
	}

	@Override
	public Object getAdapter(Node node, Class<?> key) {
		if (DeclarativeActivity.class.equals(key)) {
			return Services.getCheetahObjectLookup().getObject(NAMESPACE_DECLARATIVE_ACTIVITIES, node.getId());
		}

		return null;
	}

	@Override
	public Command getCommand(EditPart editPart, Request request) {
		Command command = super.getCommand(editPart, request);
		if (command != null) {
			return command;
		}

		if (REQ_CREATE.equals(request.getType())) {
			CreateRequest createRequest = (CreateRequest) request;
			IGraphElementDescriptor newType = (IGraphElementDescriptor) createRequest.getNewObjectType();
			Node node = (Node) editPart.getModel();
			Graph graph = node.getGraph();
			String id = newType.getId();

			if (id.equals(DECSERFLOW_SELECTION)) {
				Edge edge = (Edge) newType.createModel(graph);
				return ((IEdgeDescriptor) newType).createCreateEdgeCommand(graph, edge, node, node, null);
			} else if (id.equals(DECSERFLOW_INIT) || id.equals(EditorRegistry.DECSERFLOW_LAST)) {
				Edge edge = (Edge) newType.createModel(graph);
				return ((IEdgeDescriptor) newType).createCreateEdgeCommand(graph, edge, node, node, null);
			}
		}

		return null;
	}

	@Override
	public String getCommandLabel(AuditTrailEntry entry) {
		String event = entry.getEventType();
		if (event.equals(EditNodeDescriptionCommand.COMMAND_EDIT_DESCRIPTION)) {
			String name = entry.getAttribute(AbstractGraphCommand.NAME);
			return "Edit description of " + name;
		}

		return "";
	}

	@Override
	public Point getInitialSize() {
		return new Point(120, 40);
	}

	@Override
	public EditPart getTargetEditPart(EditPart editpart, Request request) {
		EditPart target = super.getTargetEditPart(editpart, request);
		if (target != null) {
			return target;
		}

		if (RequestConstants.REQ_CREATE.equals(request.getType())) {
			return editpart;
		}

		return null;
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public void updateName(IFigure figure, String name) {
		((ActivityFigure) figure).setName(name);
	}

}

package org.cheetahplatform.modeler.graph.descriptor;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.graphics.Image;

import com.swtdesigner.ResourceManager;

public abstract class GraphElementDescriptor implements IGraphElementDescriptor {

	private Image icon;
	private String name;
	private ImageDescriptor iconDescriptor;
	private final String id;
	private List<IGraphElementDescriptor> delegates;

	public GraphElementDescriptor(String imagePath, String name, String id) {
		this.id = id;
		this.icon = ResourceManager.getPluginImage(Activator.getDefault(), imagePath);
		this.iconDescriptor = ResourceManager.getPluginImageDescriptor(Activator.getDefault(), imagePath);
		this.name = name;
		this.delegates = new ArrayList<IGraphElementDescriptor>();
	}

	@Override
	public void addDelegate(IGraphElementDescriptor delegate) {
		delegates.add(delegate);
	}

	@Override
	public void buildContextMenu(EditPart editPart, IMenuManager menu, Point dropLocation) {
		// ignore
	}

	@Override
	public boolean canExecuteCreationCommand(AbstractGraphCommand command, GraphElement element) {
		// ignore, subclasses may override
		return true;
	}

	@Override
	public AbstractGraphCommand createCommand(AuditTrailEntry entry, Graph graph) {
		return null;
	}

	@Override
	public IInputValidator createRenameValidator(GraphElement element) {
		return null; // nothing to validate
	}

	@Override
	public void dropped(DropTargetEvent event, AbstractGraphicalEditPart targetEditPart) {
		for (IGraphElementDescriptor descriptor : delegates) {
			descriptor.dropped(event, targetEditPart);
		}
	}

	@Override
	public String getCommandLabel(AuditTrailEntry entry) {
		return "";
	}

	@Override
	public Image getIcon() {
		return icon;
	}

	@Override
	public ImageDescriptor getIconDescriptor() {
		return iconDescriptor;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void removeDelegate(IGraphElementDescriptor delegate) {
		delegates.remove(delegate);
	}

}

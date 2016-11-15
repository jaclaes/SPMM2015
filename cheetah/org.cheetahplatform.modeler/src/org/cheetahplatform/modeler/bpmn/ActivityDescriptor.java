package org.cheetahplatform.modeler.bpmn;

import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_BACKGROUND_COLOR;

import java.beans.PropertyChangeEvent;
import java.util.Date;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.generic.figure.GenericActivityFigure;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.SWTResourceManager;

public class ActivityDescriptor extends NodeDescriptor {
	public ActivityDescriptor() {
		super("img/bpmn/activity.png", "Activity", EditorRegistry.BPMN_ACTIVITY); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public ActivityDescriptor(String imagePath, String name, String ID) {
		super(imagePath, name, ID);
	}

	@Override
	public boolean canExecuteCreationCommand(AbstractGraphCommand command, GraphElement element) {
		if (element.getDescriptor().hasCustomName()) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

			InputDialog dialog = new InputDialog(shell, Messages.ActivityDescriptor_2, Messages.ActivityDescriptor_3, "", null);
			Date startTime = new Date();
			if (dialog.open() != Window.OK) {
				return false;
			}

			CreateNodeCommand createNodeCommand = (CreateNodeCommand) command;
			createNodeCommand.setName(dialog.getValue());
			createNodeCommand.setStartTime(startTime);
		}

		return true;
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		String name = element.getName();
		if (name == null) {
			name = ""; //$NON-NLS-1$
		}

		GenericActivityFigure figure = new GenericActivityFigure(name) {
			@Override
			protected Color getBorderColor() {
				return SWTResourceManager.getColor(0, 0, 0);
			}
		};

		figure.setSize(new Dimension(getInitialSize().getSWTPoint()));
		figure.setBackgroundColor(SWTResourceManager.getColor(255, 255, 255));
		figure.setForegroundColor(SWTResourceManager.getColor(255, 255, 255));
		figure.setLabelColor(SWTResourceManager.getColor(0, 0, 0));

		return figure;
	}

	@Override
	public Point getInitialSize() {
		return (Point) CheetahPlatformConfigurator.getObject(IConfiguration.INITIAL_ACTIVITIY_SIZE);
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public void propertyChanged(EditPart editPart, PropertyChangeEvent event) {
		super.propertyChanged(editPart, event);
		String property = event.getPropertyName();

		if (PROPERTY_BACKGROUND_COLOR.equals(property)) {
			GraphElement element = (GraphElement) editPart.getModel();
			RGB rgb = (RGB) element.getProperty(property);
			Color color = null;
			if (rgb != null) {
				color = SWTResourceManager.getColor(rgb);
			}
			IFigure figure = ((AbstractGraphicalEditPart) editPart).getFigure();
			figure.setBackgroundColor(color);
			editPart.setSelected(EditPart.SELECTED_NONE);
		}
	}

	@Override
	public void updateName(IFigure figure, String name) {
		if (name == null) {
			name = ""; //$NON-NLS-1$
		}

		((GenericActivityFigure) figure).setName(name);
	}

}

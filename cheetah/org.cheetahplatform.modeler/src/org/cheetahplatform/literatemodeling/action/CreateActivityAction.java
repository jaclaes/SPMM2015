package org.cheetahplatform.literatemodeling.action;

import static org.cheetahplatform.modeler.ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME;

import org.cheetahplatform.literatemodeling.LiterateModelingEditor;
import org.cheetahplatform.literatemodeling.command.CreateNodeLiterateModelingAssociationCommand;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.bpmn.ActivityDescriptor;
import org.cheetahplatform.modeler.decserflow.CompoundCommandWithAttributes;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.ResourceManager;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         21.04.2010
 */
public class CreateActivityAction extends Action {

	private static final String ID = "org.cheetahplatform.literatemodeling.action.createactivity";
	private final GraphicalGraphViewerWithFlyoutPalette viewer;
	private final ISelectionProvider selectionProvider;
	private final LiterateModel model;

	/**
	 * @param viewer
	 * @param selectionProvider
	 * @param model
	 */
	public CreateActivityAction(GraphicalGraphViewerWithFlyoutPalette viewer, ISelectionProvider selectionProvider, LiterateModel model) {
		setId(ID);
		Assert.isNotNull(model);
		this.model = model;
		setText("Create Activity");
		setToolTipText("Create activity using the current selection");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/bpmn/activity.png"));
		this.viewer = viewer;
		this.selectionProvider = selectionProvider;
	}

	private String getActivityName(ITextSelection textSelection) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		String selectedText = textSelection.getText();
		selectedText = selectedText.replaceAll("(\r\n|\n)", " ");

		InputDialog dialog = new InputDialog(shell, "Enter Name", "Please enter the name for the activity", selectedText,
				new IInputValidator() {
					@Override
					public String isValid(String newText) {
						if (newText.trim().isEmpty()) {
							return "Please enter a name.";
						}
						return null;
					}
				});
		if (dialog.open() != Window.OK) {
			return null;
		}
		String activityName = dialog.getValue();
		return activityName;
	}

	@Override
	public void run() {
		ISelection selection = selectionProvider.getSelection();
		ITextSelection textSelection = (ITextSelection) selection;

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (textSelection.getText().isEmpty()) {
			MessageDialog.openError(shell, "No Selection", "Please select some text that should be converted into an activity");
			return;
		}
		String activityName = getActivityName(textSelection);
		if (activityName == null) {
			return;
		}

		Point location = viewer.getEmptySpace();
		location = viewer.toRelativeLocation(location);
		Graph graph = viewer.getGraph();

		LiterateModelingEditor editor = LiterateModelingEditor.getActiveEditor();
		if (editor == null) {
			return;
		}

		ActivityDescriptor activityDescriptor = new ActivityDescriptor();
		CreateNodeCommand createNodeCommand = new CreateNodeCommand(graph, activityDescriptor.createModel(graph), location, activityName);
		Node graphElement = (Node) createNodeCommand.getGraphElement();
		CreateNodeLiterateModelingAssociationCommand createAssocCommand = new CreateNodeLiterateModelingAssociationCommand(model,
				textSelection, graphElement);

		CompoundCommandWithAttributes command = new CompoundCommandWithAttributes();
		command.setAttribute(ATTRIBUTE_COMPOUND_COMMAND_NAME, "Create Node Association");
		command.add(createNodeCommand);
		command.add(createAssocCommand);
		editor.getCommandStack().execute(command);
	}
}

package org.cheetahplatform.literatemodeling.action;

import static org.cheetahplatform.modeler.ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME;

import org.cheetahplatform.literatemodeling.LiterateModelingEditor;
import org.cheetahplatform.literatemodeling.command.CreateEdgeLiterateModelingAssociationCommand;
import org.cheetahplatform.literatemodeling.dialog.SingleGraphPartSelectionDialog;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.decserflow.CompoundCommandWithAttributes;
import org.cheetahplatform.modeler.graph.command.RenameCommand;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
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
public class SetConditionAction extends Action {
	public static final String ID = "org.cheetahplatform.action.setconditionaction";
	private final Graph graph;
	private final ISelectionProvider selectionProvider;
	private final LiterateModel model;

	public SetConditionAction(Graph graph, ISelectionProvider selectionProvider, LiterateModel model) {
		this.selectionProvider = selectionProvider;
		Assert.isNotNull(model);
		this.model = model;
		setId(ID);
		setToolTipText("Set a condition for a sequence flow.");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/edit_condition.gif"));
		this.graph = graph;
	}

	private String getConditionName(ITextSelection textSelection) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		String selectedText = textSelection.getText();
		selectedText = selectedText.replaceAll("(\r\n|\n)", " ");
		if (selectedText.length() > 50) {
			selectedText = selectedText.substring(0, 50);
		}

		InputDialog dialog = new InputDialog(shell, "Condition Name", "Please enter the name of the condition.", selectedText,
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
		String conditionName = dialog.getValue();
		return conditionName;
	}

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ITextSelection textSelection = (ITextSelection) selectionProvider.getSelection();
		if (textSelection.getText().isEmpty()) {
			MessageDialog.openError(shell, "No Selection", "Please select some text that should be converted into a condition.");
			return;
		}
		String conditionName = getConditionName(textSelection);
		if (conditionName == null) {
			return;
		}

		SingleGraphPartSelectionDialog dialog = new SingleGraphPartSelectionDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell(), graph, IEdgeDescriptor.class);
		int open = dialog.open();
		if (open == Window.CANCEL) {
			return;
		}
		Edge selectedEdge = (Edge) dialog.getSelectedElement();

		LiterateModelingEditor editor = LiterateModelingEditor.getActiveEditor();
		if (editor != null) {
			CompoundCommandWithAttributes command = new CompoundCommandWithAttributes();
			command.setAttribute(ATTRIBUTE_COMPOUND_COMMAND_NAME, "Create Edge Association");
			command.add(new RenameCommand(selectedEdge, conditionName));
			command.add(new CreateEdgeLiterateModelingAssociationCommand(model, conditionName, textSelection, selectedEdge));
			editor.getCommandStack().execute(command);
		}
	}
}

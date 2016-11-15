package org.cheetahplatform.literatemodeling.action;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.literatemodeling.LiterateModelingEditor;
import org.cheetahplatform.literatemodeling.command.CreateCommentLiterateModelingCommand;
import org.cheetahplatform.literatemodeling.dialog.GraphPartSelectionDialog;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.descriptor.EdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.ResourceManager;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         02.06.2010
 */
public class AddCommentAction extends Action {

	private static final String ID = "org.cheetahplatform.action.addCommentAction";
	private final ISelectionProvider selectionProvider;
	private final LiterateModel model;
	private final Graph graph;

	/**
	 * @param graph
	 * @param selectionProvider
	 * @param model
	 */
	public AddCommentAction(Graph graph, ISelectionProvider selectionProvider, LiterateModel model) {
		this.selectionProvider = selectionProvider;
		Assert.isNotNull(model);
		this.model = model;
		setId(ID);
		setToolTipText("Add a association to a series of graph elements.");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/add_comment_16.png"));
		this.graph = graph;
	}

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		InputDialog nameDialog = new InputDialog(shell, "Comment Name", "Please enter the name of the comment.", "", new IInputValidator() {

			@Override
			public String isValid(String newText) {
				if (newText.trim().isEmpty()) {
					return "Please enter a name";
				}

				return null;
			}
		});

		if (nameDialog.open() == Window.CANCEL) {
			return;
		}

		String commentName = nameDialog.getValue();

		List<Class<? extends IGraphElementDescriptor>> allowedClasses = new ArrayList<Class<? extends IGraphElementDescriptor>>();
		allowedClasses.add(NodeDescriptor.class);
		allowedClasses.add(EdgeDescriptor.class);
		GraphPartSelectionDialog dialog = new GraphPartSelectionDialog(shell, graph, allowedClasses);
		int open = dialog.open();
		if (open == Window.CANCEL) {
			return;
		}

		List<GraphElement> selectedElements = dialog.getSelectedElements();
		ITextSelection textSelection = (ITextSelection) selectionProvider.getSelection();

		Command command = new CreateCommentLiterateModelingCommand(model, commentName, textSelection, selectedElements);
		LiterateModelingEditor editor = LiterateModelingEditor.getActiveEditor();
		if (editor != null) {
			editor.getCommandStack().execute(command);
		}

	}
}

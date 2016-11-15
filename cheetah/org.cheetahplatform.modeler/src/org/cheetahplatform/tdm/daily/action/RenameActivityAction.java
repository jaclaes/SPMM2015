package org.cheetahplatform.tdm.daily.action;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.decserflow.descriptor.RenameDecSerFlowActivityValidator;
import org.cheetahplatform.modeler.graph.command.RenameCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.modeler.declarative.TDMDeclarativeModelerView;
import org.cheetahplatform.tdm.modeler.test.TDMTestEditorModel;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.ResourceManager;

public class RenameActivityAction extends Action {
	public static final String ID = "org.cheetahplatform.tdm.daily.action.RenameActivityAction";

	private final Activity activity;

	public RenameActivityAction(Activity activity) {
		this.activity = activity;

		setId(ID);
		setText("Rename");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/rename.gif"));
	}

	@Override
	public void run() {
		Shell shell = Display.getDefault().getActiveShell();
		Graph graph = TDMTestEditorModel.getGraph();
		Node toRename = (Node) graph.getGraphElement(activity.getActivityDefinition().getCheetahId());

		InputDialog dialog = new InputDialog(shell, "Rename", "Please enter the new name", activity.getName(),
				new RenameDecSerFlowActivityValidator(graph, toRename));

		if (dialog.open() == Window.OK) {
			RenameCommand command = new RenameCommand(toRename, dialog.getValue());
			TDMDeclarativeModelerView view = (TDMDeclarativeModelerView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findView(TDMDeclarativeModelerView.ID);
			view.getViewer().getViewer().getEditDomain().getCommandStack().execute(command);
		}
	}
}

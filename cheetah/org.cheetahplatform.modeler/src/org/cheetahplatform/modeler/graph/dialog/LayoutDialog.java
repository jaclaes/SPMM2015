package org.cheetahplatform.modeler.graph.dialog;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.ResourceManager;

public class LayoutDialog extends SimpleLocationPersistentDialog {

	private class LayoutAction extends Action {
		public LayoutAction() {
			setText("layout");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/layout.png"));
		}

		@Override
		public void run() {
			layout();
		}
	}

	private class UndoLayoutAction extends Action {
		public UndoLayoutAction() {
			setText("undo layout");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/undo.png"));
		}

		@Override
		public void run() {
			undoLayout();
		}
	}

	private UndoLayoutAction undoLayoutAction;
	private GraphicalGraphViewerWithFlyoutPalette viewer;
	private LayoutAction layoutAction;

	public LayoutDialog(Shell parentShell, GraphicalGraphViewerWithFlyoutPalette editor) {
		super(parentShell);

		this.viewer = editor;
		undoLayoutAction = new UndoLayoutAction();
		undoLayoutAction.setEnabled(false);

		layoutAction = new LayoutAction();
		layoutAction.setEnabled(false);

		editor.getGraph().addLogListener(new ILogListener() {

			@Override
			public void log(AuditTrailEntry entry) {
				undoLayoutAction.setEnabled(false);
				layoutAction.setEnabled(true);
			}
		});
	}

	@Override
	protected List<IAction> createActions() {
		List<IAction> actions = new ArrayList<IAction>();
		actions.add(undoLayoutAction);
		actions.add(layoutAction);

		return actions;
	}

	public IAction getLayoutAction() {
		return layoutAction;
	}

	protected void layout() {
		org.cheetahplatform.modeler.action.LayoutAction action = new org.cheetahplatform.modeler.action.LayoutAction(viewer);
		action.run();

		if (action.isSuccess()) {
			undoLayoutAction.setEnabled(true);
			layoutAction.setEnabled(false);
		}
	}

	protected void undoLayout() {
		CommandStack stack = viewer.getViewer().getEditDomain().getCommandStack();
		if (!stack.canUndo()) {
			return;
		}

		stack.undo();
		undoLayoutAction.setEnabled(false);
		layoutAction.setEnabled(true);
	}

}

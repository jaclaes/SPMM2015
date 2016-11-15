package org.cheetahplatform.modeler.changepattern.action;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.changepattern.model.AbstractChangePattern;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.swtdesigner.ResourceManager;

public class UndoChangePatternAction extends AbstractChangePatternAction {

	public UndoChangePatternAction(PlainMultiLineButton button, GraphicalGraphViewerWithFlyoutPalette viewer) {
		super(button, viewer);
		setText("Undo");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/undo.png"));
	}

	@Override
	public void run() {
		CommandStack stack = viewer.getViewer().getEditDomain().getCommandStack();
		if (!stack.canUndo()) {
			return;
		}

		stack.undo();
		setEnabled(stack.canUndo());
	}

	@Override
	protected AbstractChangePattern createChangePattern(IStructuredSelection selection) {
		throw new UnsupportedOperationException("Should not be called");
	}

	@Override
	protected boolean isChangePatternExecutable(IStructuredSelection selection) {
		CommandStack stack = viewer.getViewer().getEditDomain().getCommandStack();
		return stack.canUndo();
	}
}

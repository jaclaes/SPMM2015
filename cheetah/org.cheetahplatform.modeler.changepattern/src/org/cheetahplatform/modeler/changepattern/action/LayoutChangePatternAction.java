package org.cheetahplatform.modeler.changepattern.action;

import java.lang.reflect.InvocationTargetException;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.action.LayoutRunnable;
import org.cheetahplatform.modeler.changepattern.model.AbstractChangePattern;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;

public class LayoutChangePatternAction extends AbstractChangePatternAction {

	public LayoutChangePatternAction(PlainMultiLineButton button, GraphicalGraphViewerWithFlyoutPalette viewer) {
		super(button, viewer);
	}

	@Override
	protected AbstractChangePattern createChangePattern(IStructuredSelection selection) {
		throw new UnsupportedOperationException("Should not be called");
	}

	@Override
	public void run() {
		try {
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
			LayoutRunnable layoutRunnable = new LayoutRunnable(viewer);
			dialog.run(false, false, layoutRunnable);
		} catch (InvocationTargetException e) {
			Activator.logError("Could not compute the layout.", e);
		} catch (InterruptedException e) {
			// ignore
		}
	}

	@Override
	protected boolean isChangePatternExecutable(IStructuredSelection selection) {
		return true;
	}
}

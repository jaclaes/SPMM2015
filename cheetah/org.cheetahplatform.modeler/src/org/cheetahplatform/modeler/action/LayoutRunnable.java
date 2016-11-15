package org.cheetahplatform.modeler.action;

import java.lang.reflect.InvocationTargetException;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.IBMLayouter;
import org.cheetahplatform.modeler.graph.command.UnsuccesfulLayoutCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;

public class LayoutRunnable implements IRunnableWithProgress {
	private GraphicalGraphViewerWithFlyoutPalette editor;
	private boolean success;

	public LayoutRunnable(GraphicalGraphViewerWithFlyoutPalette editor) {
		this.editor = editor;
		success = false;
	}

	public boolean isSuccess() {
		return success;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		monitor.beginTask("Computing layout...", IProgressMonitor.UNKNOWN);
		Command command = null;

		try {
			Graph graph = editor.getGraph();
			Viewport viewPort = (Viewport) ((ScalableRootEditPart) editor.getViewer().getRootEditPart()).getFigure();
			Dimension targetDimension = viewPort.getClientArea().getSize();
			command = new IBMLayouter().computeLayoutCommands(graph, targetDimension);
			this.success = true;
		} catch (Exception e) {
			Activator.logError("Could not compute the layout.", e);
			command = new UnsuccesfulLayoutCommand(editor.getGraph(), e);

			MessageDialog.openError(Display.getDefault().getActiveShell(), "Could not layout",
					"The layout could not be computed. Please check your internet connectivity and try again.");
		}

		editor.getViewer().getEditDomain().getCommandStack().execute(command);
	}
}
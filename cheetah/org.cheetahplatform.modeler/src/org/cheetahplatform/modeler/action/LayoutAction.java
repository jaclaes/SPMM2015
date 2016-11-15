package org.cheetahplatform.modeler.action;

import java.lang.reflect.InvocationTargetException;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;

public class LayoutAction extends AbstractModelingEditorAction<GraphicalGraphViewerWithFlyoutPalette> {

	boolean success;

	public static final String ID = "org.cheetahplatform.modeler.layout.LayoutAction";

	private final GraphicalGraphViewerWithFlyoutPalette viewer;

	public LayoutAction() {
		this(null);
	}

	public LayoutAction(GraphicalGraphViewerWithFlyoutPalette viewer) {
		super(GraphicalGraphViewerWithFlyoutPalette.class);

		this.viewer = viewer;
		setId(ID);
		setText("Layout (Ctrl+L)");
	}

	public boolean isSuccess() {
		return success;
	}

	@Override
	public void run() {
		if (viewer == null) {
			super.run();
		} else {
			run(null);
		}
	}

	@Override
	protected void run(GraphicalGraphViewerWithFlyoutPalette editor) {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());

		GraphicalGraphViewerWithFlyoutPalette viewerToUse = viewer;
		if (viewerToUse == null) {
			viewerToUse = editor;
		}

		try {
			LayoutRunnable runnable = new LayoutRunnable(viewerToUse);
			dialog.run(false, false, runnable);
			success = runnable.isSuccess();
		} catch (InvocationTargetException e) {
			Activator.logError("Could not compute the layout.", e);
		} catch (InterruptedException e) {
			// ignore
		}
	}
}

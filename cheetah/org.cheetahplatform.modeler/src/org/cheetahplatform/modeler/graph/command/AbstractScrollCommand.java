package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.modeler.graph.GraphEditor;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.Viewport;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractScrollCommand extends AbstractGraphCommand {

	protected final int min;
	protected final int max;
	protected final int extent;
	protected final int value;

	protected int oldMin;
	protected int oldMax;
	protected int oldExtent;
	protected int oldValue;

	public AbstractScrollCommand(Graph graph, GraphElement element, int min, int max, int extent, int value) {
		super(graph, element);
		this.min = min;
		this.max = max;
		this.extent = extent;
		this.value = value;
	}

	@Override
	public void execute() {
		Viewport viewport = getViewPort();
		// if we export there is no editor -> no scrolling
		if (viewport == null) {
			return;
		}

		RangeModel rangeModel = getRangeModel(viewport);
		oldExtent = rangeModel.getExtent();
		oldMax = rangeModel.getMaximum();
		oldMin = rangeModel.getMinimum();
		oldValue = rangeModel.getValue();

		rangeModel.setAll(min, extent, max);
		getRangeModel(viewport).setValue(value);
	}

	public abstract RangeModel getRangeModel(Viewport viewport);

	public Viewport getViewPort() {
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			return null;
		}
		GraphEditor activeEditor = (GraphEditor) activeWorkbenchWindow.getActivePage().getActiveEditor();
		if (activeEditor == null) {
			return null;
		}

		GraphicalGraphViewerWithFlyoutPalette viewer = activeEditor.getGraphViewer();
		Viewport viewport = ((FigureCanvas) viewer.getViewer().getControl()).getViewport();
		return viewport;
	}

	@Override
	public void undo() {
		Viewport viewPort = getViewPort();
		if (viewPort == null) {
			return;
		}
		RangeModel rangeModel = getRangeModel(viewPort);
		rangeModel.setAll(oldMin, oldExtent, oldMax);
		rangeModel.setValue(oldValue);
	}

}
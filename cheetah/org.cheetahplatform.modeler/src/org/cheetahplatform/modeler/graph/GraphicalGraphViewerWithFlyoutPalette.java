package org.cheetahplatform.modeler.graph;

import java.util.ArrayList;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.modeler.bpmn.ActivityDescriptor;
import org.cheetahplatform.modeler.generic.GEFUtils;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.generic.GraphicalViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.graph.editpart.EdgeEditPart;
import org.cheetahplatform.modeler.graph.editpart.GraphEditPart;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

public class GraphicalGraphViewerWithFlyoutPalette extends GraphicalViewerWithFlyoutPalette {

	private final IGraphicalGraphViewerAdvisor advisor;

	public GraphicalGraphViewerWithFlyoutPalette(Composite parent, IGraphicalGraphViewerAdvisor advisor) {
		super(parent, advisor.getInitialFlyoutPaletteState());
		Assert.isNotNull(advisor);

		this.advisor = advisor;
		initialize();
	}

	private void adaptFeedbackLayer() {
		ScalableRootEditPart rootEditPart = (ScalableRootEditPart) viewer.getRootEditPart();
		Viewport content = (Viewport) rootEditPart.getFigure();
		LayeredPane layeredPane = (LayeredPane) content.getContents();
		Layer old = layeredPane.getLayer(LayerConstants.FEEDBACK_LAYER);
		layeredPane.remove(old);
		layeredPane.add(new Layer() {
			{
				setEnabled(false);
			}

			@Override
			public Dimension getPreferredSize(int wHint, int hHint) {
				int xMax = 0;
				int yMax = 0;

				for (Node node : getGraph().getNodes()) {
					Rectangle bounds = node.getBounds();
					if (bounds.x > xMax) {
						xMax = bounds.x;
					}
					if (bounds.y > yMax) {
						yMax = bounds.y;
					}
				}

				return new Dimension(xMax + 500, yMax + 500);
			}

		}, LayerConstants.FEEDBACK_LAYER);
	}

	private void addConnectionRouter() {
		ScalableRootEditPart rootEditPart = (ScalableRootEditPart) viewer.getRootEditPart();
		ConnectionLayer layer = (ConnectionLayer) rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER);
		ShortestPathConnectionRouter router = new ShortestPathConnectionRouter((IFigure) ((Layer) rootEditPart.getContentPane())
				.getChildren().get(0));
		router.setSpacing(10);
		layer.setConnectionRouter(router);
	}

	private void addDropTarget() {
		DropTarget dropTarget = (DropTarget) viewer.getControl().getData(DND.DROP_TARGET_KEY);
		Transfer[] transfer = dropTarget.getTransfer();
		Transfer[] copy = new Transfer[transfer.length + 1];
		copy[copy.length - 1] = TextTransfer.getInstance();
		System.arraycopy(transfer, 0, copy, 0, transfer.length);
		dropTarget.setTransfer(copy);
		dropTarget.addDropListener(new DropTargetAdapter() {

			@Override
			public void dragOver(DropTargetEvent event) {
				Point location = GEFUtils.getDropLocation(viewer);
				EditPart target = viewer.findObjectAt(location);

				if (target instanceof NodeEditPart || target instanceof EdgeEditPart) {
					event.detail = DND.DROP_MOVE;
				} else {
					event.detail = DND.DROP_NONE;
				}
			}

			@Override
			public void drop(DropTargetEvent event) {
				Point location = GEFUtils.getDropLocation(viewer);
				EditPart target = viewer.findObjectAt(location);
				AbstractGraphicalEditPart targetEditPart = (AbstractGraphicalEditPart) target;

				GraphElement model = null;
				if (targetEditPart instanceof EdgeEditPart) {
					model = ((EdgeEditPart) targetEditPart).getModel();
				} else {
					model = ((NodeEditPart) targetEditPart).getModel();
				}

				model.getDescriptor().dropped(event, targetEditPart);
			}

		});
	}

	@Override
	protected EditDomain createEditDomain() {
		return new GraphEditDomain();
	}

	/**
	 * Extend the palette with graph editor specific entries.
	 */
	private void extendPalette() {
		advisor.fillPalette(this, palette);
	}

	public Point getEmptySpace() {
		Point location = new Point(10, 10);
		Point initialSize = new ActivityDescriptor().getInitialSize();

		boolean free;
		do {
			free = true;
			free = free && isFree(location);
			Point translated = location.getCopy().translate(initialSize.x - 5, 0);
			free = free && isFree(translated);
			translated = location.getCopy().translate(0, initialSize.y - 5);
			free = free && isFree(translated);
			translated = location.getCopy().translate(initialSize.x - 5, initialSize.y - 5);
			free = free && isFree(translated);
			translated = location.getCopy().translate(initialSize.x / 2, initialSize.y / 2);
			free = free && isFree(translated);

			if (!free) {
				location = location.getTranslated(new Dimension(initialSize.x, 0));
			}
		} while (!free);

		return location;
	}

	public Graph getGraph() {
		return advisor.getGraph();
	}

	private void initialize() {
		GraphEditDomain editDomain = (GraphEditDomain) viewer.getEditDomain();
		editDomain.setDirectEditingEnabled(advisor.isDirectEditEnabled());
		extendPalette();

		Graph graph = getGraph();
		viewer.setContents(graph);
		((GraphCommandStack) viewer.getEditDomain().getCommandStack()).setGraph(graph);

		addConnectionRouter();
		adaptFeedbackLayer();
		addDropTarget();

		// refresh the viewer so the new layout can be processed
		((GenericEditPart) viewer.getEditPartRegistry().get(graph)).refresh(true);
	}

	private boolean isFree(Point location) {
		EditPart editPart = viewer.findObjectAt(location);
		if (!(editPart instanceof GraphEditPart)) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public void reset(Graph graph) {
		// need to remove the connections manually
		LayerManager manager = (LayerManager) viewer.getEditPartRegistry().get(LayerManager.ID);
		IFigure connectionLayer = manager.getLayer(LayerConstants.CONNECTION_LAYER);
		for (IFigure connection : new ArrayList<IFigure>(connectionLayer.getChildren())) {
			connectionLayer.remove(connection);
		}

		viewer.setContents(graph);
		addConnectionRouter();
	}

	public Point toRelativeLocation(Point location) {
		LayerManager layerManager = (LayerManager) getViewer().getEditPartRegistry().get(LayerManager.ID);
		IFigure layer = layerManager.getLayer(LayerConstants.PRIMARY_LAYER);

		Point point = location.getCopy();
		layer.translateToRelative(point);
		return point;
	}
}

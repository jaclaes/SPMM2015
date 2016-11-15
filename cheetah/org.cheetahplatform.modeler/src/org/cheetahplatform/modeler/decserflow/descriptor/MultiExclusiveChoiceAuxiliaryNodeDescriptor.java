package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.PathFigureAnchor;
import org.cheetahplatform.modeler.decserflow.MultiExclusiveChoiceAuxiliaryNode;
import org.cheetahplatform.modeler.decserflow.figure.MultiExclusiveChoiceFigure;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

public class MultiExclusiveChoiceAuxiliaryNodeDescriptor extends AuxiliaryNodeDescriptor {

	public MultiExclusiveChoiceAuxiliaryNodeDescriptor() {
		super("", "Exclusive Choice Node", EditorRegistry.DECSERFLOW_AUXILIARY_NODE_FOR_MULTI_EXCLUSIVE_CHOICE);
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		String text = element.getNameNullSafe();

		return new MultiExclusiveChoiceFigure(text);
	}

	@Override
	public Node createModel(Graph graph) {
		return new MultiExclusiveChoiceAuxiliaryNode(graph, this);
	}

	@Override
	public Node createModel(Graph graph, long id, AuditTrailEntry entry) {
		return new MultiExclusiveChoiceAuxiliaryNode(graph, this, id);
	}

	@Override
	public ConnectionAnchor getConnectionAnchor(NodeEditPart editPart) {
		return new PathFigureAnchor(editPart.getFigure());
	}

	@Override
	public Point getInitialSize() {
		return new Point(24, 16);
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public void updateName(IFigure figure, String name) {
		((MultiExclusiveChoiceFigure) figure).setName(name);
	}

}

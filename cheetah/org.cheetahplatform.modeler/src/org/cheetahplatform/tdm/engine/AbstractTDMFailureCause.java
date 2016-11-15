package org.cheetahplatform.tdm.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.modeler.decserflow.descriptor.AuxiliaryNodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.tdm.modeler.declarative.TDMDeclarativeModelerView;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractTDMFailureCause implements ITDMFailureCause {

	/**
	 * Finds all graph elements that are associated with the given constraint.
	 * 
	 * @param constraint
	 *            the constraint
	 * @return all associated graph elements
	 */
	protected List<GraphElement> findAllRelevantElements(IDeclarativeConstraint constraint) {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (activePage == null) {
			return Collections.emptyList();
		}

		TDMDeclarativeModelerView view = (TDMDeclarativeModelerView) activePage.findView(TDMDeclarativeModelerView.ID);
		Edge edge = (Edge) view.getViewer().getGraph().getGraphElement(constraint.getCheetahId());
		List<GraphElement> elements = new ArrayList<GraphElement>();

		if (edge != null) {
			boolean isSourceAuxiliary = edge.getSource() != null && edge.getSource().getDescriptor() instanceof AuxiliaryNodeDescriptor;
			boolean isTargetAuxiliary = edge.getTarget() != null && edge.getTarget().getDescriptor() instanceof AuxiliaryNodeDescriptor;

			if (isSourceAuxiliary || isTargetAuxiliary) {
				Node auxiliaryNode = null;
				if (isSourceAuxiliary) {
					auxiliaryNode = edge.getSource();
				} else {
					auxiliaryNode = edge.getTarget();
				}

				elements.add(auxiliaryNode);
				for (Edge targetEdge : auxiliaryNode.getTargetConnections()) {
					elements.add(targetEdge);
				}
				for (Edge sourceEdge : auxiliaryNode.getSourceConnections()) {
					elements.add(sourceEdge);
				}
			} else {
				elements.add(edge);
			}
		}

		return elements;
	}

	/**
	 * Determine the constraint that caused the failure, if any.
	 * 
	 * @return the constraint causing the failure, <code>null</code> if the failure was not caused by a constraint
	 */
	protected abstract IDeclarativeConstraint getConstraintCausingFailure();

	@Override
	public final List<GraphElement> getModelElementCausingFailure() {
		IDeclarativeConstraint constraint = getConstraintCausingFailure();
		if (constraint != null) {
			return findAllRelevantElements(constraint);
		}

		return Collections.emptyList();
	}
}

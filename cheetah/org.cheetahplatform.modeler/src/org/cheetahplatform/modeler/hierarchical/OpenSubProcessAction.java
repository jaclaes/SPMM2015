package org.cheetahplatform.modeler.hierarchical;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.HierarchicalNode;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class OpenSubProcessAction extends Action {
	public static final String ID = "org.cheetahplatform.modeler.graph.action.OpenSubProcessAction"; //$NON-NLS-1$
	private final EditPart editPart;

	public OpenSubProcessAction(EditPart editPart) {
		this.editPart = editPart;
		setId(ID);
	}

	@SuppressWarnings("unchecked")
	protected OutlineViewNode<Graph> getChildNode(OutlineViewNode<Graph> parentNode) {
		HierarchicalNode selectedNode = (HierarchicalNode) editPart.getModel();
		for (OutlineViewNode node : parentNode.getChildren()) {
			if (node.getId().equals(selectedNode.getSubProcess())) {
				return node;
			}
		}
		return null;
	}

	protected void openInComboView(HierarchicalComboView hierarchicalComboView) {
		OutlineViewNode<Graph> parentNode = hierarchicalComboView.getSelection();
		OutlineViewNode<Graph> childNode = getChildNode(parentNode);

		if (childNode != null) {
			hierarchicalComboView.setSelection(childNode);
		}
	}

	protected void openInView(HierarchicalOutlineView hierarchicalOutlineView) {
		OutlineViewNode<Graph> parentNode = hierarchicalOutlineView.getSelection();
		OutlineViewNode<Graph> childNode = getChildNode(parentNode);

		if (childNode != null) {
			hierarchicalOutlineView.setSelection(childNode);
		}
	}

	@Override
	public void run() {
		// get the hierarchical outline view
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow().getActivePage();
		HierarchicalOutlineView hierarchicalOutlineView = (HierarchicalOutlineView) activePage.findView(HierarchicalOutlineView.ID);

		if (hierarchicalOutlineView != null) {
			openInView(hierarchicalOutlineView);
		} else {
			HierarchicalComboView hierarchicalComboView = (HierarchicalComboView) activePage.findView(HierarchicalComboView.ID);
			if (hierarchicalComboView != null) {
				openInComboView(hierarchicalComboView);
			}
		}

	}

}

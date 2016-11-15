package org.cheetahplatform.modeler.hierarchical;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class OutlineContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		// nothing to do
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		@SuppressWarnings("unchecked")
		OutlineViewNode<?> node = (OutlineViewNode<Graph>) parentElement;
		return node.getChildren().toArray();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object getParent(Object element) {
		@SuppressWarnings("unchecked")
		OutlineViewNode<?> node = (OutlineViewNode<Graph>) element;
		return node.getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		@SuppressWarnings("unchecked")
		OutlineViewNode<?> node = (OutlineViewNode<Graph>) element;
		return node.getChildren().size() > 0;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// nothing to do
	}

}

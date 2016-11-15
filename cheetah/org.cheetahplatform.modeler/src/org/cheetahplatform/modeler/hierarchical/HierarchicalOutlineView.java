package org.cheetahplatform.modeler.hierarchical;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class HierarchicalOutlineView extends ViewPart {

	public static final String ID = "org.cheetahplatform.modeler.hierarchical.HierarchicalOutlineView";

	private HierarchicalOutlineComposite hierarchicalOutline;

	public void addSelectionChangeListener(ISelectionChangedListener listener) {
		hierarchicalOutline.addSelectionChangeListener(listener);
	}

	@Override
	public void createPartControl(Composite parent) {
		hierarchicalOutline = new HierarchicalOutlineComposite(parent, SWT.NONE);
	}

	public OutlineViewNode<Graph> getSelection() {
		return hierarchicalOutline.getSelection();
	}

	public void removeListener(ISelectionChangedListener listener) {
		hierarchicalOutline.removeSelectionChangeListener(listener);
	}

	@Override
	public void setFocus() {
		// nothing to do
	}

	public void setInput(OutlineViewNode<Graph> mainNode) {
		hierarchicalOutline.setInput(mainNode);
	}

	public void setSelection(OutlineViewNode<Graph> node) {
		hierarchicalOutline.setSelection(node);
	}

}

package org.cheetahplatform.modeler.hierarchical;

import java.util.Iterator;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class HierarchicalOutlineComposite extends Composite {

	private TreeViewer treeViewer;

	public HierarchicalOutlineComposite(Composite parent, int style) {
		super(parent, style);

		final GridLayout gridLayout = new GridLayout(1, true);
		setLayout(gridLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;

		treeViewer = new TreeViewer(this, SWT.SINGLE);
		treeViewer.setContentProvider(new OutlineContentProvider());
		treeViewer.setLabelProvider(new OutlineLabelProvider());

		treeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		if ("win32".equals(SWT.getPlatform())) {
			// turn on dotted lines in tree
			OS.SetWindowLong(treeViewer.getTree().handle, OS.GWL_STYLE, OS.GetWindowLong(treeViewer.getTree().handle, OS.GWL_STYLE)
					| OS.TVS_HASLINES);
		}
	}

	public void addSelectionChangeListener(ISelectionChangedListener listener) {
		treeViewer.addSelectionChangedListener(listener);
	}

	public OutlineViewNode<Graph> getSelection() {
		if (treeViewer.getSelection() != null && treeViewer.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
			Iterator it = selection.iterator();
			if (it.hasNext()) {
				@SuppressWarnings("unchecked")
				OutlineViewNode<Graph> node = (OutlineViewNode<Graph>) it.next();
				return node;
			}
		}
		return null;
	}

	public void refresh() {
		treeViewer.refresh();
		treeViewer.expandAll();
	}

	public void removeSelectionChangeListener(ISelectionChangedListener listener) {
		treeViewer.removeSelectionChangedListener(listener);
	}

	public void setInput(OutlineViewNode<Graph> mainNode) {
		OutlineViewNode<Graph> root = new OutlineViewNode<Graph>("root", "root", null, null);
		root.addChild(mainNode);
		mainNode.setParent(root);

		treeViewer.setInput(root);
		treeViewer.expandAll();

		setSelection(mainNode);
	}

	public void setSelection(OutlineViewNode<Graph> node) {
		treeViewer.setSelection(new StructuredSelection(node), true);
	}

}

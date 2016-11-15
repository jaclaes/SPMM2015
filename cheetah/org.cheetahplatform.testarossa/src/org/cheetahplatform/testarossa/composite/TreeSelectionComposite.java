/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.testarossa.composite;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

public class TreeSelectionComposite extends Composite {

	private TreeViewer viewer;
	private Tree tree;

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public TreeSelectionComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());

		viewer = new TreeViewer(this, SWT.BORDER);
		tree = viewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * Returns the viewer.
	 * 
	 * @return the viewer
	 */
	public TreeViewer getViewer() {
		return viewer;
	}

}

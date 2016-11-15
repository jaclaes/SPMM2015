package org.cheetahplatform.client.ui;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class WorklistComposite extends Composite {

	private TreeViewer worklistViewer;

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public WorklistComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		worklistViewer = new TreeViewer(this);
		worklistViewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
		worklistViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
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
	 * Returns the worklistViewer.
	 * 
	 * @return the worklistViewer
	 */
	public TreeViewer getWorklistViewer() {
		return worklistViewer;
	}

}

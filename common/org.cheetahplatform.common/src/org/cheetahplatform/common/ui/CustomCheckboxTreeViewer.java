package org.cheetahplatform.common.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class CustomCheckboxTreeViewer extends CheckboxTreeViewer {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private class SpaceListener extends KeyAdapter {

		protected void adaptCheckState() {
			// check root elements only
			List selection = new ArrayList();
			for (Object object : ((IStructuredSelection) getSelection()).toList()) {
				TreeItem item = (TreeItem) findItem(object);
				if (item.getParentItem() == null) {
					selection.add(object);
				}
			}

			// and check only if at least half of them is unchecked
			List<Object> checked = Arrays.asList(getCheckedElements());
			int checkedCount = 0;
			for (Object object : selection) {
				if (checked.contains(object)) {
					checkedCount++;
				}
			}

			// set the check state
			getControl().setRedraw(false);
			boolean checkElements = checkedCount <= selection.size() / 2;
			for (Object object : selection) {
				setChecked(object, checkElements);
			}
			getControl().setRedraw(true);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.keyCode == ' ') {
				adaptCheckState();
			}
		}

	}

	public CustomCheckboxTreeViewer(Composite parent, int style) {
		super(parent, style);

		getControl().addKeyListener(new SpaceListener());
	}

	public CustomCheckboxTreeViewer(Tree tree) {
		super(tree);

		tree.addKeyListener(new SpaceListener());
	}

}

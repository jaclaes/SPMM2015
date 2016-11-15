package org.cheetahplatform.common.ui;

import org.cheetahplatform.common.Assert;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;

/**
 * An action detecting selection changes in the viewer and setting the appropriate enabled state.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         09.07.2009
 */
public abstract class SelectionSensitiveAction<T> extends Action implements ISelectionChangedListener {
	private Class<?> applicableType;
	protected final StructuredViewer viewer;
	private T selectedElement;

	public SelectionSensitiveAction(StructuredViewer viewer, Class<?> applicableType) {
		Assert.isNotNull(applicableType);
		Assert.isNotNull(viewer);
		this.applicableType = applicableType;
		this.viewer = viewer;
		viewer.addSelectionChangedListener(this);
		setEnabled(false);
	}

	public T getSelection() {
		return selectedElement;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		Object newSelectedElement = selection.getFirstElement();
		boolean isApplicable = false;
		if (newSelectedElement != null) {
			isApplicable = applicableType.isAssignableFrom(newSelectedElement.getClass());
		}

		setEnabled(isApplicable);

		if (isApplicable) {
			this.selectedElement = (T) newSelectedElement;
		} else {
			this.selectedElement = null;
		}
	}
}

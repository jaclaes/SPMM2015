package org.cheetahplatform.modeler.graph;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

public class ReplayModelProvider implements ISelectionProvider {
	private static ReplayModelProvider INSTANCE;

	public static ReplayModelProvider getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ReplayModelProvider();
		}
		return INSTANCE;
	}

	private ReplayModel model;
	private ListenerList listeners = new ListenerList();

	private ReplayModelProvider() {
		// singleton
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}

	@Override
	public ISelection getSelection() {
		return new StructuredSelection(model);
	}

	public void initializeModel(GraphCommandStack stack, ProcessInstanceDatabaseHandle handle) {
		setSelection(new StructuredSelection(new ReplayModel(stack, handle, stack.getGraph())));
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void setSelection(ISelection selection) {
		Object object = ((IStructuredSelection) selection).getFirstElement();
		Assert.isLegal(object instanceof ReplayModel);
		model = (ReplayModel) object;

		for (Object listener : listeners.getListeners()) {
			((ISelectionChangedListener) listener).selectionChanged(new SelectionChangedEvent(this, getSelection()));
		}
	}
}

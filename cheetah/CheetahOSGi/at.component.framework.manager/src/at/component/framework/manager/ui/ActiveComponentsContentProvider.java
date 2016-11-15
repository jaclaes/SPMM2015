package at.component.framework.manager.ui;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.component.IComponent;

public class ActiveComponentsContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IComponent) {
			IComponent component = (IComponent) parentElement;

			List<IComponent> components = component.getChildComponents();

			if (components != null)
				return components.toArray();
		}

		return new Object[] {};
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IComponent)
			return ((IComponent) element).getParent();

		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {
		// nothing todo
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// nothing todo
	}

}

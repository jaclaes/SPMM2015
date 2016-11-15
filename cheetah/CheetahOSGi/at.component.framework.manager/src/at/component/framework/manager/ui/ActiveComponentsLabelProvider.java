package at.component.framework.manager.ui;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import at.component.IComponent;

public class ActiveComponentsLabelProvider implements ILabelProvider {

	@Override
	public Image getImage(Object element) {
		// no images
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof IComponent)
			return ((IComponent) element).getNameWithId();
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// nothing todo
	}

	@Override
	public void dispose() {
		// nothing todo
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// nothing todo
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// nothing todo
	}

}

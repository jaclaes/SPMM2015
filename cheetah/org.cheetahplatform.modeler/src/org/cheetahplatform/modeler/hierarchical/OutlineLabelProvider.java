package org.cheetahplatform.modeler.hierarchical;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class OutlineLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// nothing to do
	}

	@Override
	public void dispose() {
		// nothing to do
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		OutlineViewNode<?> node = (OutlineViewNode<?>) element;
		return node.getLabel();
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// nothing to do
	}
}

package at.component.componentDisplay;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

public class ComponentTableLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// no listener
	}

	@Override
	public void dispose() {
		// do nothing

	}

	@Override
	public Image getImage(Object element) {
		// no image
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof ServiceReference) {
			ServiceReference component = (ServiceReference) element;
			return component.getBundle().getSymbolicName() + " --- ID: " + component.getProperty(Constants.SERVICE_ID);
		}
		return null;
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// do nothing
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// no listener
	}

}

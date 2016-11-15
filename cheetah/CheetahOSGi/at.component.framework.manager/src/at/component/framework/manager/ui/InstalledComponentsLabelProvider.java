package at.component.framework.manager.ui;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

public class InstalledComponentsLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// no images
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof Bundle) {
			Object bundleName = ((Bundle)element).getHeaders().get(Constants.BUNDLE_NAME);
			
			if (bundleName != null && !bundleName.toString().trim().equals(""))
				return bundleName.toString().trim();
			
			return ((Bundle)element).getSymbolicName();
		}
		
		return null; //$NON-NLS-1$
	}

}

package at.component.framework.manager.ui;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import at.component.framework.services.componentservice.ProjectConfiguration;

public class LoadedProjectsLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// no images
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof ProjectConfiguration) {
			return ((ProjectConfiguration) element).getProjectName();
		}
		
		return null; //$NON-NLS-1$
	}

}

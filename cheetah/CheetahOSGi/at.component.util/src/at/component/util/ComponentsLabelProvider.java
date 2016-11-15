package at.component.util;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import at.component.IComponent;

public class ComponentsLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		IComponent component = (IComponent) element;

		switch (columnIndex) {
		case 0:
			return component.getNameWithId();
		default:
			throw new IllegalArgumentException("Unknown column index: " + columnIndex); //$NON-NLS-1$
		}
	}
}
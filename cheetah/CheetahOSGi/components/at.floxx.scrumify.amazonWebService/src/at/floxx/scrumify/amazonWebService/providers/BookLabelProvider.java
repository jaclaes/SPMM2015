package at.floxx.scrumify.amazonWebService.providers;

import java.util.List;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class BookLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		switch (columnIndex) {
		case 4:
			return null;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getColumnText(Object element, int columnIndex) {
		List<String> attributes = (List<String>) element;
		switch (columnIndex) {
		case 0:
			return attributes.get(0);
		case 1:
			return attributes.get(1);
		case 2:
			return attributes.get(2);
		case 3:
			return attributes.get(3);
		case 4:
			return attributes.get(4);
		default:
			throw new RuntimeException(
					"Something wired happened in SprintBacklogItemLabelProvider");
		}
	}

}

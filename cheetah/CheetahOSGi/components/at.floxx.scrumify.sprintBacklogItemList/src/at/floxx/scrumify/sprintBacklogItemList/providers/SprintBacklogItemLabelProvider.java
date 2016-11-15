package at.floxx.scrumify.sprintBacklogItemList.providers;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import at.floxx.scrumify.coreObjectsProvider.core.SprintBacklogItem;


/**Label Provider for SprintBacklogItems.
 * @author Mathias Breuss
 *
 */
public class SprintBacklogItemLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		SprintBacklogItem item = (SprintBacklogItem) element;
		switch (columnIndex) {
		case 0:
			return item.getDescription();
		case 1:
			return String.valueOf(item.getOpenEstimate());
		case 2:
			return item.getResponsibility();
		case 3:
			return String.valueOf(item.getState());
		default:
			throw new RuntimeException(
					"Something wired happened in SprintBacklogItemLabelProvider");

		}
	}

}

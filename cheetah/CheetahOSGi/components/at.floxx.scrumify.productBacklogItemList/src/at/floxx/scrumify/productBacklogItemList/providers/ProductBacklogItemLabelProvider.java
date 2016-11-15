package at.floxx.scrumify.productBacklogItemList.providers;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import at.floxx.scrumify.coreObjectsProvider.core.ProductBacklogItem;



/**Labelprovider for Productbacklog Item
 * @author mathias
 *
 */
public class ProductBacklogItemLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ProductBacklogItem item = (ProductBacklogItem) element;
		switch (columnIndex) {
		case 0:
			return item.getName();
		case 1:
			return item.getDescription();
		case 2:
			return String.valueOf(item.getEstimate());
		default:
			throw new RuntimeException(
					"Something wired happened in ProductBacklogItemLabelProvider");

		}
	}

}

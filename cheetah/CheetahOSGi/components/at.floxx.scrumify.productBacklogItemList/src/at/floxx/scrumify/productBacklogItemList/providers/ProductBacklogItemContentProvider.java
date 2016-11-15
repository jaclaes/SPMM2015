package at.floxx.scrumify.productBacklogItemList.providers;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.floxx.scrumify.coreObjectsProvider.core.ProductBacklogItem;


/**Contentprovider for ProductBacklog Items
 * @author mathias
 *
 */
public class ProductBacklogItemContentProvider implements IStructuredContentProvider {

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		List<ProductBacklogItem> items = (List<ProductBacklogItem>) inputElement;
		return items.toArray();
	}

	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

}

package at.floxx.scrumify.sprintBacklogItemList.providers;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.floxx.scrumify.coreObjectsProvider.core.SprintBacklogItem;


/**Content provider for SprintBacklogItems.
 * @author Mathias Breuss
 *
 */
public class SprintBacklogItemContentProvider implements IStructuredContentProvider {

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		List<SprintBacklogItem> items = (List<SprintBacklogItem>) inputElement;
		return items.toArray();
	}

	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

}

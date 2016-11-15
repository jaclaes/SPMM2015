package at.floxx.scrumify.sprintList.providers;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.floxx.scrumify.coreObjectsProvider.core.Sprint;



/**Sprint Content Provider
 * @author Mathias Breuss
 *
 */
public class SprintContentProvider implements IStructuredContentProvider {

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		List<Sprint> items = (List<Sprint>) inputElement;
		return items.toArray();
	}

	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

}

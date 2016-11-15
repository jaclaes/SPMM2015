package at.floxx.scrumify.releasePlanList.providers;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.floxx.scrumify.coreObjectsProvider.core.ReleasePlan;


/**ContentProvider Class.
 * @author Mathias Breuss
 *
 */
public class ReleasePlanItemContentProvider implements IStructuredContentProvider {

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		List<ReleasePlan> items = (List<ReleasePlan>) inputElement;
		return items.toArray();
	}

	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

}

package at.floxx.scrumify.amazonWebService.providers;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class BookContentProvider implements IStructuredContentProvider {

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		List<List<String>> items = (List<List<String>>) inputElement;
		return items.toArray();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

}

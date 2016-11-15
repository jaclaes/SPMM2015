package at.floxx.scrumify.productManager.gui;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import at.floxx.scrumify.coreObjectsProvider.core.Product;

/**ProductList Composite Class
 * @author mathias
 *
 */
public class ProductListComposite  extends Composite{

	private ListViewer viewer;

	/**
	 * @return viewer
	 */
	public ListViewer getViewer() {
		return viewer;
	}

	/**The Constructor.
	 * @param parent
	 * @param style
	 */
	public ProductListComposite(Composite parent, int style) {
		super(parent, SWT.NULL);
		populateControl();
	}

	/**
	 * Creates the ListViewer.
	 */
	protected void populateControl() {
		FillLayout compositeLayout = new FillLayout();
		setLayout(compositeLayout);
		
		createListViewer(SWT.SINGLE);
	}

	private void createListViewer(int style) {
		viewer = new ListViewer(this, style);
		
		viewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return ((Product)element).getName();
			}
		});
		
		viewer.setSorter( new ViewerSorter() {});
		
		viewer.setContentProvider(new IStructuredContentProvider() {

			@SuppressWarnings("unchecked")
			@Override
			public Object[] getElements(Object inputElement) {
				return ((List)inputElement).toArray();
			}

			@Override
			public void dispose() {}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {}});
		
	}


	

}

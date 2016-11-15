package at.floxx.scrumify.productBacklogItemList;

import java.util.List;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;

import at.floxx.scrumify.coreObjectsProvider.core.ProductBacklogItem;
import at.floxx.scrumify.productBacklogItemList.model.ModelProvider;
import at.floxx.scrumify.productBacklogItemList.providers.ProductBacklogItemContentProvider;
import at.floxx.scrumify.productBacklogItemList.providers.ProductBacklogItemLabelProvider;

/**Contains the ProductBacklog Table
 * @author mathias
 *
 */
public class ProductBacklogComposite extends org.eclipse.swt.widgets.Composite {
	private TableViewer viewer;
	private Table table;
	private boolean isDeleteEnable = false;
	private MenuItem removeMenuItem;


	/**
	 * @return removeMenuItem
	 */
	public MenuItem getRemoveMenuItem() {
		return removeMenuItem;
	}



	/**
	 * @return isDeleteEnable
	 */
	public boolean isDeleteEnable() {
		return isDeleteEnable;
	}



	/**
	 * @param isDeleteEnable
	 */
	public void setDeleteEnable(boolean isDeleteEnable) {
		removeMenuItem.setEnabled(isDeleteEnable);
		this.isDeleteEnable = isDeleteEnable;
	}



	/**
	 * @return table
	 */
	public Table getTable() {
		return table;
	}
	
	

	/**
	 * @return viewer
	 */
	public TableViewer getViewer() {
		return viewer;
	}



	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	 * @param args 
	*/
	public static void main(String[] args) {
		showGUI();
	}
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
	}
	
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		ProductBacklogComposite inst = new ProductBacklogComposite(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if(size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**The Constructor.
	 * @param parent
	 * @param style
	 */
	public ProductBacklogComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			this.setLayout(new FillLayout());
			this.layout();
			
			createViewer();
			viewer.setInput(ModelProvider.getInstance().getItems());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createViewer() {
		viewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		createColumns(viewer);
		viewer.setContentProvider(new ProductBacklogItemContentProvider());
		viewer.setLabelProvider(new ProductBacklogItemLabelProvider());
		
		
		int ops = DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { TextTransfer.getInstance()};

		viewer.addDragSupport(ops, transfers, new DragSourceListener() {
			
			@Override
			public void dragStart(DragSourceEvent event) {}
			
			@SuppressWarnings("unchecked")
			@Override
			public void dragSetData(DragSourceEvent event) {
				List<ProductBacklogItem> items = ((StructuredSelection)viewer.getSelection()).toList();
				StringBuilder stb = new StringBuilder();
				for(ProductBacklogItem item : items) {
					stb.append(item.getId());
					stb.append(";");
				}
				event.data = stb.toString();
				
				
			}
			
			@Override
			public void dragFinished(DragSourceEvent event) {}
		});
		

		Menu popupMenu = new Menu(viewer.getTable().getParent().getShell(), SWT.POP_UP);
		removeMenuItem = new MenuItem(popupMenu, SWT.PUSH);
		removeMenuItem.setText("Remove");
		viewer.getTable().setMenu(popupMenu);
		removeMenuItem.setEnabled(isDeleteEnable);

	}

	private void createColumns(TableViewer viewer) {
		String[] titles = {"Name", "Description", "Estimation"};
		int[] bounds = {150, 330, 70};
		
		for (int i = 0; i < titles.length; i++) {
			TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
			column.getColumn().setText(titles[i]);
			column.getColumn().setWidth(bounds[i]);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
		}
		table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}
	


}

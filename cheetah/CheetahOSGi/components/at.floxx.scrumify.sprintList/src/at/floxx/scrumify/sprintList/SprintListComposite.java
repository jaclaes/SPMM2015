package at.floxx.scrumify.sprintList;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;

import at.floxx.scrumify.sprintList.model.ModelProvider;
import at.floxx.scrumify.sprintList.providers.SprintContentProvider;
import at.floxx.scrumify.sprintList.providers.SprintLabelProvider;

/**Contains the Sprint List.
 * @author Mathias Breuss
 *
 */
public class SprintListComposite extends org.eclipse.swt.widgets.Composite {
	private TableViewer viewer;
	private Table table;

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
		SprintListComposite inst = new SprintListComposite(shell, SWT.NULL);
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
	public SprintListComposite(org.eclipse.swt.widgets.Composite parent, int style) {
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
		viewer.setContentProvider(new SprintContentProvider());
		viewer.setLabelProvider(new SprintLabelProvider());

	}

	private void createColumns(TableViewer viewer) {
		String[] titles = {"Sprint Goal", "Comments", "Speed", "Start", "End"};
		int[] bounds = {120, 250, 30, 80, 80};
		
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

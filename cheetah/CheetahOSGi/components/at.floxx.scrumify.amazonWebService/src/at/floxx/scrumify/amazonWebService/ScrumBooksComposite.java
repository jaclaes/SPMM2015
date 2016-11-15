package at.floxx.scrumify.amazonWebService;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import at.floxx.scrumify.amazonWebService.model.ModelProvider;
import at.floxx.scrumify.amazonWebService.providers.BookContentProvider;
import at.floxx.scrumify.amazonWebService.providers.BookLabelProvider;



public class ScrumBooksComposite extends org.eclipse.swt.widgets.Composite {
	private TableViewer viewer;
	private Button updateBtn;
	private Text keyWordTxt;

	public Button getUpdateBtn() {
		return updateBtn;
	}

	public Text getKeyWordTxt() {
		return keyWordTxt;
	}

	public TableViewer getViewer() {
		return viewer;
	}

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
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
		ScrumBooksComposite inst = new ScrumBooksComposite(shell, SWT.NULL);
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

	public ScrumBooksComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			
			this.setLayout(layout);
			GridData updateBtnLData = new GridData();
			updateBtnLData.grabExcessHorizontalSpace = true;
			updateBtnLData.horizontalAlignment = GridData.FILL;
			
			updateBtn = new Button(this, SWT.NONE);
			updateBtn.setText("Update");
			updateBtn.setLayoutData(updateBtnLData);
			
			GridData keyWordTxtLData = new GridData();
			keyWordTxtLData.grabExcessHorizontalSpace = true;
			keyWordTxtLData.horizontalAlignment = GridData.FILL;
			
			keyWordTxt = new Text(this, SWT.NONE);
			keyWordTxt.setLayoutData(keyWordTxtLData);
			keyWordTxt.setText("Scrum");
			
			createViewer();
			GridData viewerLData = new GridData();
			viewerLData.grabExcessHorizontalSpace = true;
			viewerLData.heightHint = 150;
			viewerLData.widthHint = 400;
			viewerLData.grabExcessVerticalSpace = true;
			viewerLData.horizontalSpan = 2;
			viewerLData.verticalSpan = 6;
			viewerLData.horizontalAlignment = GridData.FILL;
			viewerLData.verticalAlignment = GridData.FILL;
			viewer.getTable().setLayoutData(viewerLData);
			
			this.layout();

	    	String searchKeyWord = Activator.getPreferencesService().getSystemPreferences().get("SearchKeyWordAWS", null);
	    	if(searchKeyWord != null) {   	
	    		viewer.setInput(ModelProvider.getInstance().getItems(searchKeyWord));
	    		keyWordTxt.setText(searchKeyWord);
	    	}
	    	else
	    		viewer.setInput(ModelProvider.getInstance().getItems());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createViewer() {
		viewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		createColumns(viewer);
		viewer.setContentProvider(new BookContentProvider());
		viewer.setLabelProvider(new BookLabelProvider());
	}

	private void createColumns(TableViewer viewer) {
		String[] titles = {"Author", "Press", "Type", "Title"};
		int[] bounds = {100, 100, 50, 250};
		
		for (int i = 0; i < titles.length; i++) {
			TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
			column.getColumn().setText(titles[i]);
			column.getColumn().setWidth(bounds[i]);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
		}
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}
	
	public Point computeSize(int wHint, int hHint, boolean changed) {
		int width = 650;
		int height = 300;
		return new Point(width < wHint ? wHint : width, height < hHint ? hHint : height);
	}


}
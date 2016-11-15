package at.floxx.scrumify.sprintBoardSwt.composites;
import com.cloudgarden.resource.SWTResourceManager;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;



/**Composite containing controls to control the size of the cells.
 * @author Mathias Breuss
 *
 */
public class BoardDimensionComposite extends org.eclipse.swt.widgets.Composite {

	{
		//Register as a resource user - SWTResourceManager will
		//handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}
	

	private Button removeButton;
	private Button addButton;

	/**
	 * @return removeButton
	 */
	public Button getRemoveButton() {
		return removeButton;
	}

	/**
	 * @return addButton
	 */
	public Button getAddButton() {
		return addButton;
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
		BoardDimensionComposite inst = new BoardDimensionComposite(shell, SWT.NULL);
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
	public BoardDimensionComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			RowLayout thisLayout = new RowLayout(org.eclipse.swt.SWT.HORIZONTAL);
			this.setLayout(thisLayout);
			{
				addButton = new Button(this, SWT.PUSH | SWT.CENTER);
				RowData addButtonLData = new RowData();
				addButton.setLayoutData(addButtonLData);
				addButton.setText("+");
				addButton.setFont(SWTResourceManager.getFont("Sans", 6, 0, false, false));
			}
			{
				removeButton = new Button(this, SWT.PUSH | SWT.CENTER);
				RowData removeButtonLData = new RowData();
				removeButton.setLayoutData(removeButtonLData);
				removeButton.setText("-");
				removeButton.setFont(SWTResourceManager.getFont("Sans", 6, 0, false, false));
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

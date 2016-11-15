package at.floxx.scrumify.releasePlanEditForm;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;


/**Contains the Buttons.
 * @author Mathias Breuss
 *
 */
public class ButtonComposite extends org.eclipse.swt.widgets.Composite {
	private Button updateButton;
	private Button newButton;
	private Button cancelButton;
	private Button deleteButton;

	/**
	 * @return updateButton
	 */
	public Button getUpdateButton() {
		return updateButton;
	}

	/**
	 * @return newButton
	 */
	public Button getNewButton() {
		return newButton;
	}

	/**
	 * @return cancelButton
	 */
	public Button getCancelButton() {
		return cancelButton;
	}

	/**
	 * @return deleteButton
	 */
	public Button getDeleteButton() {
		return deleteButton;
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
		ButtonComposite inst = new ButtonComposite(shell, SWT.NULL);
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
	public ButtonComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			RowLayout thisLayout = new RowLayout(org.eclipse.swt.SWT.HORIZONTAL);
			this.setLayout(thisLayout);
			{
				updateButton = new Button(this, SWT.PUSH | SWT.CENTER);
				updateButton.setText("Update");
				RowData updateButtonLData = new RowData();
				updateButton.setLayoutData(updateButtonLData);
			}
			{
				newButton = new Button(this, SWT.PUSH | SWT.CENTER);
				newButton.setText("Save as new");
				RowData newButtonLData = new RowData();
				newButton.setLayoutData(newButtonLData);
			}
			{
				deleteButton = new Button(this, SWT.PUSH | SWT.CENTER);
				deleteButton.setText("Delete");
				RowData deleteButtonLData = new RowData();
				deleteButton.setLayoutData(deleteButtonLData);
			}
			{
				cancelButton = new Button(this, SWT.PUSH | SWT.CENTER);
				cancelButton.setText("Clear");
				RowData cancelButtonLData = new RowData();
				cancelButton.setLayoutData(cancelButtonLData);
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

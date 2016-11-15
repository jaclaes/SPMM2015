package at.floxx.scrumify.productBacklogItemEditForm;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;


/**Contains Buttons and Inputfield Composites.
 * @author Mathias Breuss
 *
 */
public class ProductBacklogItemEditFormComposite extends org.eclipse.swt.widgets.Composite {

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	 * @param args 
	*/
	public static void main(String[] args) {
		showGUI();
	}

	/**
	 * @return inputFieldsComposite
	 */
	public InputFieldsComposite getInputFieldsComposite() {
		return inputFieldsComposite;
	}

	/**
	 * @return buttonComposite
	 */
	public ButtonComposite getButtonComposite() {
		return buttonComposite;
	}

	private InputFieldsComposite inputFieldsComposite;
	private ButtonComposite buttonComposite;
	
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
		ProductBacklogItemEditFormComposite inst = new ProductBacklogItemEditFormComposite(shell, SWT.NULL);
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
	public ProductBacklogItemEditFormComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			GridLayout thisLayout = new GridLayout();
			thisLayout.numColumns = 1;
			this.setLayout(thisLayout);

			GridData inputFieldsGData = new GridData();
			inputFieldsGData.grabExcessHorizontalSpace = true;
			inputFieldsGData.horizontalAlignment = GridData.FILL;
			inputFieldsComposite = new InputFieldsComposite(this, SWT.NONE);
			inputFieldsComposite.setLayoutData(inputFieldsGData);
			
			buttonComposite = new ButtonComposite(this, SWT.NONE);

			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

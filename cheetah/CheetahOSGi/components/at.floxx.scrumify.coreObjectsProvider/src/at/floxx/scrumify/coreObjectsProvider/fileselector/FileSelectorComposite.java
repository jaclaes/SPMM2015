package at.floxx.scrumify.coreObjectsProvider.fileselector;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;


/**Contains the File Selector GUI.
 * @author Mathias Breuss
 *
 */
public class FileSelectorComposite extends org.eclipse.swt.widgets.Composite {
	private Label pathLabel;
	private Text pathText;
	private Button browseButton;
	private Button saveButton;
	private Button loadButton;



	/**
	 * @return the pathText
	 */
	public Text getPathText() {
		return pathText;
	}

	/**
	 * @return the browseButton
	 */
	public Button getBrowseButton() {
		return browseButton;
	}

	/**
	 * @return the saveButton
	 */
	public Button getSaveButton() {
		return saveButton;
	}

	/**
	 * @return the loadButton
	 */
	public Button getLoadButton() {
		return loadButton;
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
		FileSelectorComposite inst = new FileSelectorComposite(shell, SWT.NULL);
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
	public FileSelectorComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			{
				browseButton = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData browseButtonLData = new FormData();
				browseButtonLData.left =  new FormAttachment(0, 1000, 386);
				browseButtonLData.top =  new FormAttachment(0, 1000, 0);
				browseButtonLData.width = 62;
				browseButtonLData.height = 24;
				browseButton.setLayoutData(browseButtonLData);
				browseButton.setText("Browse ");
			}
			{
				saveButton = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData saveButtonLData = new FormData();
				saveButtonLData.left =  new FormAttachment(0, 1000, 505);
				saveButtonLData.top =  new FormAttachment(0, 1000, 0);
				saveButtonLData.width = 42;
				saveButtonLData.height = 24;
				saveButton.setLayoutData(saveButtonLData);
				saveButton.setText("Save  ");
			}
			{
				loadButton = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData loadButtonLData = new FormData();
				loadButtonLData.left =  new FormAttachment(0, 1000, 452);
				loadButtonLData.top =  new FormAttachment(0, 1000, 0);
				loadButtonLData.width = 47;
				loadButtonLData.height = 24;
				loadButton.setLayoutData(loadButtonLData);
				loadButton.setText("Load  ");
			}
			{
				pathLabel = new Label(this, SWT.NONE);
				FormData pathLabelLData = new FormData();
				pathLabelLData.width = 42;
				pathLabelLData.height = 15;
				pathLabelLData.left =  new FormAttachment(1, 1000, 0);
				pathLabelLData.top =  new FormAttachment(35, 1000, 0);
				pathLabel.setLayoutData(pathLabelLData);
				pathLabel.setText("Path:");
			}
			{
				FormData pathTextLData = new FormData();
				pathTextLData.width = 324;
				pathTextLData.height = 17;
				pathTextLData.left =  new FormAttachment(100, 1000, 0);
				pathTextLData.top =  new FormAttachment(35, 1000, 0);
				pathText = new Text(this, SWT.NONE);
				pathText.setLayoutData(pathTextLData);
			}
			this.layout();
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

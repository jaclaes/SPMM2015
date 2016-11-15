package at.floxx.scrumify.sprintEditForm;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;


/**Contains the input fields.
 * @author Mathias Breuss
 *
 */
public class InputFieldsComposite extends org.eclipse.swt.widgets.Composite {

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	 * @param args 
	*/
	public static void main(String[] args) {
		showGUI();
	}

	private Text nameFieldText;
	private Text descriptionText;
	private Text speedText;
	
	/**
	 * @return nameFieldText
	 */
	public Text getNameFieldText() {
		return nameFieldText;
	}

	/**
	 * @return descriptionText
	 */
	public Text getDescriptionText() {
		return descriptionText;
	}

	
	/**
	 * @return speedText
	 */
	public Text getSpeedText() {
		return speedText;
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
		InputFieldsComposite inst = new InputFieldsComposite(shell, SWT.NULL);
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
	public InputFieldsComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 1;
			this.setLayout(gridLayout);
			
			GridData nameLabelLData = new GridData();
			nameLabelLData.grabExcessHorizontalSpace = true;
			Label nameLabel = new Label(this, SWT.NONE);
			nameLabel.setLayoutData(nameLabelLData);
			nameLabel.setText("Sprint Goal:");
			
			GridData nameTextLData = new GridData();
			nameTextLData.grabExcessHorizontalSpace = true;
			nameTextLData.horizontalAlignment = GridData.FILL;
			nameFieldText = new Text(this, SWT.NONE);
			nameFieldText.setLayoutData(nameTextLData);
			
			GridData speedLabelLData = new GridData();
			speedLabelLData.grabExcessHorizontalSpace = true;
			Label speedLabel = new Label(this, SWT.NONE);
			speedLabel.setLayoutData(speedLabelLData);
			speedLabel.setText("Speed:");
			
			GridData speedTextLData = new GridData();
			speedTextLData.grabExcessHorizontalSpace = true;
			speedTextLData.horizontalAlignment = GridData.FILL;
			speedText = new Text(this, SWT.NONE);
			speedText.setLayoutData(speedTextLData);
			speedText.setText("0");


			Label descriptionLabel = new Label(this, SWT.NONE);
			descriptionLabel.setText("Comment:");
			descriptionText = new Text(this, SWT.MULTI | SWT.WRAP);
			GridData descriptionTextLData = new GridData();
			descriptionTextLData.heightHint = 200;
			descriptionTextLData.horizontalAlignment = SWT.FILL;
			descriptionTextLData.verticalAlignment = SWT.END;
			descriptionText.setLayoutData(descriptionTextLData);
			descriptionText.setSize(300, 200);


			
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

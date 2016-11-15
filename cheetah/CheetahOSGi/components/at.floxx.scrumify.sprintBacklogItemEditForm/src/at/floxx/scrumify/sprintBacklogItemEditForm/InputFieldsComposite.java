package at.floxx.scrumify.sprintBacklogItemEditForm;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;

import at.floxx.scrumify.coreObjectsProvider.core.SprintItemState;


/**Contains the Input Fields.
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
	private Text openEstimateText;
	private Combo statusCombo;
	
	/**
	 * @return nameFieldText
	 */
	public Text getNameFieldText() {
		return nameFieldText;
	}

	/**
	 * @return openEstimateText
	 */
	public Text getOpenEstimateText() {
		return openEstimateText;
	}

	/**
	 * @return statusCombo
	 */
	public Combo getStatusCombo() {
		return statusCombo;
	}

	/**
	 * @return descriptionText
	 */
	public Text getDescriptionText() {
		return descriptionText;
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
			gridLayout.numColumns = 2;
			this.setLayout(gridLayout);
			

			Label descriptionLabel = new Label(this, SWT.NONE);
			descriptionLabel.setText("Description:");
			descriptionText = new Text(this, SWT.MULTI | SWT.WRAP);
			GridData descriptionTextLData = new GridData();
			descriptionTextLData.horizontalSpan = 2;
			descriptionTextLData.heightHint = 200;
			descriptionTextLData.horizontalAlignment = SWT.FILL;
			descriptionTextLData.verticalAlignment = SWT.END;
			descriptionText.setLayoutData(descriptionTextLData);
			descriptionText.setSize(300, 200);


			GridData nameLabelLData = new GridData();
			nameLabelLData.grabExcessHorizontalSpace = true;
			nameLabelLData.horizontalSpan = 2;
			Label nameLabel = new Label(this, SWT.NONE);
			nameLabel.setLayoutData(nameLabelLData);
			nameLabel.setText("Responsibel Person:");
			
			GridData nameTextLData = new GridData();
			nameTextLData.horizontalSpan = 2;
			nameTextLData.grabExcessHorizontalSpace = true;
			nameTextLData.horizontalAlignment = GridData.FILL;
			nameFieldText = new Text(this, SWT.NONE);
			nameFieldText.setLayoutData(nameTextLData);
			
			
			GridData openEstimateLabelLData = new GridData();
			openEstimateLabelLData.grabExcessHorizontalSpace = true;
			Label openEstimateLabel = new Label(this, SWT.NONE);
			openEstimateLabel.setLayoutData(openEstimateLabelLData);
			openEstimateLabel.setText("Open Estimate:");
			
			GridData openEstimateTextLData = new GridData();
			openEstimateTextLData.grabExcessHorizontalSpace = true;
			openEstimateTextLData.horizontalAlignment = SWT.FILL;
			openEstimateText = new Text(this, SWT.NONE);
			openEstimateText.setLayoutData(openEstimateTextLData);
			openEstimateText.setText("0");
			
			
			GridData statusLabelLData = new GridData();
			statusLabelLData.grabExcessHorizontalSpace = true;
			Label statusLabel = new Label(this, SWT.NONE);
			statusLabel.setLayoutData(statusLabelLData);
			statusLabel.setText("Status:");
			
			GridData statusComboLData = new GridData();
			statusComboLData.grabExcessHorizontalSpace = true;
			statusCombo = new Combo(this, SWT.READ_ONLY);
			List<String> itemList = new ArrayList<String>();
			for(SprintItemState v : SprintItemState.values())
				itemList.add(v.toString());
			String[] result = {};
			statusCombo.setItems(itemList.toArray(result));
			statusCombo.select(0);
			
			
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

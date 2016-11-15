package org.cheetahplatform.modeler.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SelectProcessInstancesFromTextDialogComposite extends Composite {
	private Text text;
	private Button selectProcessInstancesButton;
	private Button selectExperimentalProcessInstancesButton;
	private Button selectExperimentalWorkflowsDirectlyButton;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	/**
	 * @param parent
	 * @param style
	 */
	public SelectProcessInstancesFromTextDialogComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		text = new Text(this, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.CANCEL | SWT.MULTI);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		selectProcessInstancesButton = new Button(this, SWT.RADIO);
		selectProcessInstancesButton.setText("Select the process instances with the ids pasted above");

		selectExperimentalProcessInstancesButton = new Button(this, SWT.RADIO);
		selectExperimentalProcessInstancesButton
				.setText("Select the process instances that *contain* the process instances with the ids pasted above");

		selectExperimentalWorkflowsDirectlyButton = new Button(this, SWT.RADIO);
		selectExperimentalWorkflowsDirectlyButton.setText("Select experimental workflows with pasted ids");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public Button getSelectExperimentalProcessInstancesButton() {
		return selectExperimentalProcessInstancesButton;
	}

	public Button getSelectExperimentalWorkflowsDirectlyButton() {
		return selectExperimentalWorkflowsDirectlyButton;
	}

	public Button getSelectProcessInstancesButton() {
		return selectProcessInstancesButton;
	}

	public Text getText() {
		return text;
	}

}

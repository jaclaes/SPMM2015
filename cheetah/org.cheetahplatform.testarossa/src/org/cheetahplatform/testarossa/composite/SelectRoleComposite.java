package org.cheetahplatform.testarossa.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SelectRoleComposite extends Composite {
	private Text roleNameText;
	private Button roleSelectionButton;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectRoleComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		setLayout(layout);
		{
			roleNameText = new Text(this, SWT.BORDER);
			roleNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			roleNameText.setEditable(false);
		}
		{
			roleSelectionButton = new Button(this, SWT.NONE);
			roleSelectionButton.setText("Select Role ...");
		}

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the roleNameText
	 */
	public Text getRoleNameText() {
		return roleNameText;
	}

	/**
	 * @return the roleSelectionButton
	 */
	public Button getRoleSelectionButton() {
		return roleSelectionButton;
	}
}

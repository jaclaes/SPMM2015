package org.cheetahplatform.testarossa.composite;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class EditRoleComposite extends Composite {
	private Text roleNameText;
	private Composite composite;
	private Label lblColor;
	private ColorFieldEditor colorEditor;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EditRoleComposite(Composite parent, int style) {
		super(parent, style);

		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		setLayout(new GridLayout(2, false));
		{
			Label lblName = new Label(this, SWT.NONE);
			lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblName.setText("Name");
		}
		{
			roleNameText = new Text(this, SWT.BORDER);
			roleNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
		{
			lblColor = new Label(this, SWT.NONE);
			lblColor.setText("Color");
		}
		{
			composite = new Composite(this, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			GridLayout gridLayout = new GridLayout(1, false);
			gridLayout.verticalSpacing = 0;
			gridLayout.horizontalSpacing = 0;
			composite.setLayout(gridLayout);
		}

		colorEditor = new ColorFieldEditor("color", "", composite);
		Label label = colorEditor.getLabelControl(composite);
		GridData data = (GridData) label.getLayoutData();
		data.exclude = true;
		label.setVisible(false);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the colorEditor
	 */
	public ColorFieldEditor getColorEditor() {
		return colorEditor;
	}

	/**
	 * @return the roleNameText
	 */
	public Text getRoleNameText() {
		return roleNameText;
	}

}

package at.zugal.fitnesse.launcher;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class FitnesseTestLaunchConfigurationTabComposite extends Composite {

	private Text urlText;

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public FitnesseTestLaunchConfigurationTabComposite(Composite parent, int style) {
		super(parent, style);
		final GridLayout gridLayout = new GridLayout();
		setLayout(gridLayout);

		final Group fitnesseSettingsGroup = new Group(this, SWT.NONE);
		fitnesseSettingsGroup.setText("Fitnesse Settings");
		final GridData gd_fitnesseSettingsGroup = new GridData(SWT.FILL, SWT.CENTER, true, false);
		fitnesseSettingsGroup.setLayoutData(gd_fitnesseSettingsGroup);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 2;
		fitnesseSettingsGroup.setLayout(gridLayout_1);

		final Label fitnesseUrlLabel = new Label(fitnesseSettingsGroup, SWT.NONE);
		fitnesseUrlLabel.setText("Fitnesse URL");

		urlText = new Text(fitnesseSettingsGroup, SWT.BORDER);
		urlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		//
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Return the urlText.
	 * 
	 * @return the urlText
	 */
	public Text getUrlText() {
		return urlText;
	}

}

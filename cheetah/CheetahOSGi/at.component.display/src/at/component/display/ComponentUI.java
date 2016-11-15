package at.component.display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ComponentUI {

	private Composite composite;
	private Label label;

	public ComponentUI(ComponentUiController controller, Composite composite) {
		this.composite = composite;

		createUI();
	}

	public void createUI() {
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		label = new Label(composite, SWT.NONE);
		label.setText("Textdisplay");
	}

	public String getDisplayText() {
		return label.getText();
	}

	public Shell getShell() {
		return composite.getShell();
	}

	public void setDisplayText(String displayText) {
		label.setText(displayText);
	}

	public void update(String text) {
		label.setText(text);
		composite.layout();
	}

}

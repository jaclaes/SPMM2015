package at.component.calculation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ComponentUI {

	private ComponentUiController controller;
	private Composite composite;
	private Text firstNumber;
	private Text secondNumber;

	public ComponentUI(ComponentUiController controller, Composite composite) {
		this.controller = controller;
		this.composite = composite;
		
		createUI();
	}

	public Shell getShell() {
		return composite.getShell();
	}

	private void createUI() {
		GridData width = new GridData(40, SWT.DEFAULT);
		
		composite.setLayout(new GridLayout(3,false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		firstNumber = new Text(composite, SWT.BORDER);
		firstNumber.setLayoutData(width);
		Label label = new Label(composite, SWT.NONE);
		label.setText("  +  ");
		secondNumber = new Text(composite, SWT.BORDER);
		secondNumber.setLayoutData(width);
		
		GridData buttonGridData = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1);
		buttonGridData.widthHint = 60;
		
		Button button = new Button(composite, SWT.NONE);
		button.setText("Calculate");
		button.setLayoutData(buttonGridData);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.calculate(firstNumber.getText().trim(), secondNumber.getText().trim());
			}
		});
	}
	
	public void updateNumberOne(String number1) {
		firstNumber.setText(number1);
	}
	
	public void updateNumberTwo(String number2) {
		secondNumber.setText(number2);
	}
	
	public String getFirstNumber() {
		return firstNumber.getText();
	}
	
	public String getSecondNumber() {
		return secondNumber.getText();
	}
}

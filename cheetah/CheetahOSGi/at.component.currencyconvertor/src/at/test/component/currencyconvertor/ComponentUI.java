package at.test.component.currencyconvertor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class ComponentUI {

	private UIController controller;

	public ComponentUI(Composite composite, UIController controller) {
		this.controller = controller;
		createUI(composite);
	}

	private void createUI(final Composite composite) {
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("A Webservice that provides the conversionrate of various currencies");
		
		Button button = new Button(composite, SWT.PUSH);
		button.setText("Get EUR --> USD Conversion Rate");
		
		final Label conversionRateLabel = new Label(composite, SWT.NONE);
		conversionRateLabel.setText("ConversionRate");
		conversionRateLabel.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		conversionRateLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Double conversionRate = controller.getConversionRate();
				conversionRateLabel.setText("Conversion Rate: " + conversionRate);
				composite.layout(true, true);
			}
		});
		
		
	}
}

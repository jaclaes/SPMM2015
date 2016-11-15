package at.component.eventdisplay;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class ComponentUi {

	private Text eventDisplayText;

	public ComponentUi(Composite composite) {
		GridData compositeLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		compositeLayoutData.widthHint = 150;
		compositeLayoutData.heightHint = 150;

		composite.setLayout(new GridLayout());
		composite.setLayoutData(compositeLayoutData);

		eventDisplayText = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		eventDisplayText.setEditable(false);
		eventDisplayText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		eventDisplayText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
	}

	public void appendText(final String text) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				eventDisplayText.append(text);
				while (eventDisplayText.getLineCount() > 100)
					eventDisplayText.setText(eventDisplayText.getText().substring(eventDisplayText.getText().indexOf("\n") + 1));

				eventDisplayText.setSelection(eventDisplayText.getCharCount());
			}
		});
	}
}

package at.component.group.ui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import at.component.IComponent;

public class ShiftComponentDialog extends Dialog {

	private Shell shell;
	private IComponent targetComponent;
	private int result;
	private Combo targetComponentsCombo;
	private Button shiftComponentButton;

	public ShiftComponentDialog(Shell parent, int style) {
		super(parent, style);
		setText("Shift Component");
	}


	public int open(List<IComponent> targetComponents, Point dialogLocation) {
		shell = new Shell(getParent(), getStyle());

		createUI(shell, targetComponents);

		shell.setLocation(dialogLocation);
		shell.setAlpha(200);
		shell.pack();
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return result;
	}

	private void createUI(final Shell shell, List<IComponent> targetComponents) {
		GridLayout shellLayout = new GridLayout(1, true);
		shellLayout.marginHeight = 2;
		shellLayout.marginWidth = 2;
		
		shell.setLayout(shellLayout);
		
		Label infoLabel = new Label(shell, SWT.NONE);
		infoLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		infoLabel.setText("Wählen Sie die Komponente:");

		targetComponentsCombo = new Combo(shell, SWT.READ_ONLY);
		targetComponentsCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		for (IComponent component : targetComponents) {
			String targetComponentName = component.getNameWithId();
			targetComponentsCombo.add(targetComponentName);
			targetComponentsCombo.setData(targetComponentName, component);
		}

		targetComponentsCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				targetComponent = (IComponent) targetComponentsCombo.getData(targetComponentsCombo.getItem(targetComponentsCombo.getSelectionIndex()));
				shiftComponentButton.setEnabled(true);
			}
		});

		Composite buttonComposite = new Composite(shell, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(2, false));
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		GridData shiftComponentButtonGridData = new GridData(SWT.RIGHT, SWT.FILL, true, false);
		shiftComponentButtonGridData.widthHint = 100;

		shiftComponentButton = new Button(buttonComposite, SWT.PUSH);
		shiftComponentButton.setText("Shift Component");
		shiftComponentButton.setEnabled(false);
		shiftComponentButton.setLayoutData(shiftComponentButtonGridData);
		shiftComponentButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = SWT.OK;
				shell.dispose();
			}
		});

		GridData cancelButtonGridData = new GridData(SWT.RIGHT, SWT.FILL, false, false);
		cancelButtonGridData.widthHint = 100;

		final Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setLayoutData(cancelButtonGridData);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = SWT.CANCEL;
				shell.dispose();
			}
		});
	}

	public IComponent getSelectedTargetComponent() {
		return targetComponent;
	}
}

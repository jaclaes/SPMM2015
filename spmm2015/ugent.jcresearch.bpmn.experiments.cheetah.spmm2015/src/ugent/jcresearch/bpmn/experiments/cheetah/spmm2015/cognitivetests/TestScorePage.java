package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class TestScorePage extends WizardPage {

	public static int score = 0;

	public TestScorePage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout());
		control.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));

		Label textLabel = new Label(control, SWT.WRAP);
		textLabel.setFont(new Font(Display.getCurrent(), "Verdana", 12, SWT.NONE));
		GridData labelLayoutData = new GridData(SWT.LEFT, SWT.FILL, false, false);
		labelLayoutData.widthHint = 500;
		textLabel.setLayoutData(labelLayoutData);
		textLabel.setText("Your score on the test is " + score);
		setControl(control);

		setTitle("Test score");
		setDescription("This page shows the results of the test.");
	}
}

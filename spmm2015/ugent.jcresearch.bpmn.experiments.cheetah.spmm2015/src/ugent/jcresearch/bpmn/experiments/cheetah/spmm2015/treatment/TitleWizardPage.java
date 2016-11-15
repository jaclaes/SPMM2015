package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class TitleWizardPage extends AbstractTreatmentWizardPage {
	private String title;
	
	protected TitleWizardPage(LoggingValidator logValidator, String pageName, String title) {
		super(logValidator, pageName);
		this.title = title;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription(""); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);
		GridLayout rootGrid = new GridLayout(1, false);
		rootGrid.marginHeight = 0;
		rootGrid.marginWidth = 0;
		control.setLayout(rootGrid);
		control.setBackground(BACKGROUND_COLOR);
		
		Label top = new Label(control, SWT.NONE);
		top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		top.setBackground(BACKGROUND_COLOR);

		Label mid = new Label(control, SWT.NONE);
		mid.setText(title);
		mid.setFont(FONT_HUGE);
		mid.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		mid.setBackground(BACKGROUND_COLOR);

		Label bottom = new Label(control, SWT.NONE);
		bottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		bottom.setBackground(BACKGROUND_COLOR);

		setControl(control);
		validate();
	}
}

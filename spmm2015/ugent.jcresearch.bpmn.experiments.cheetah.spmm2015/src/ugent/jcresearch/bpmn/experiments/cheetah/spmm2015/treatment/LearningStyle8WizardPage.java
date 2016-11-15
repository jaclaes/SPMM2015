package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class LearningStyle8WizardPage extends AbstractTreatmentWizardPage {
	private int ls;
	private String input;
	
	protected LearningStyle8WizardPage(LoggingValidator logValidator, String pageName, int ls) {
		super(logValidator, pageName);
		this.ls = ls;
	}
	
	public String getInput() {
		return input == null ? "-" : input;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Learning style (8/8)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		if (getLearningStyle(ls) == SEQ)
			makeText(control, format(Messages.LearningStyle8WizardPage_S_Reflect));
		else if (getLearningStyle(ls) == GLOB)
			makeText(control, format(Messages.LearningStyle8WizardPage_G_Reflect));
		else
			makeText(control, format(Messages.LearningStyle8WizardPage_B_Reflect));
		
		final Text inputText = makeMultiText(control);
		inputText.addListener(SWT.FocusOut, new Listener() {
	        public void handleEvent(Event e) {
	        	input = inputText.getText();
	        }
	      });
		
		inputText.setFocus();
		
		setControl(control);
		validate();
	}
}

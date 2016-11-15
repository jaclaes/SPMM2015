package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import java.util.HashMap;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class Quiz1WizardPage extends AbstractTreatmentWizardPage {
	private Button rbQ1A1, rbQ1A2, rbQ1A3, rbQ1A4;
	private Text fbQ1A1, fbQ1A2, fbQ1A3, fbQ1A4;
	private HashMap<Button, Text> feedbackQ1;
	private HashMap<Button, Boolean> answeredQ1;

	protected Quiz1WizardPage(LoggingValidator logValidator, String pageName) {
		super(logValidator, pageName);
		feedbackQ1 = new HashMap<Button, Text>();
		answeredQ1 = new HashMap<Button, Boolean>();
	}
	
	@Override
	public void createControl(Composite parent) {
		setDescription("Quiz time! (1/3)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.Quiz1WizardPage_Introduction));
		makeQuestion1(control);

		setControl(control);
		validate();
		setPageComplete(false);
	}
	private void makeQuestion1(Composite control) {
		final String question, answer1, answer2, answer3, answer4, feedback1, feedback2, feedback3, feedback4;
		question = format(Messages.Quiz1WizardPage_Question1);
		answer1 = format(Messages.Quiz1WizardPage_Question1_answer1);
		answer2 = format(Messages.Quiz1WizardPage_Question1_answer2);
		answer3 = format(Messages.Quiz1WizardPage_Question1_answer3);
		answer4 = format(Messages.Quiz1WizardPage_Question1_answer4);
		feedback1 = format(Messages.Quiz1WizardPage_Question1_feedback1);
		feedback2 = format(Messages.Quiz1WizardPage_Question1_feedback2);
		feedback3 = format(Messages.Quiz1WizardPage_Question1_feedback3);
		feedback4 = format(Messages.Quiz1WizardPage_Question1_feedback4);
			
		makeText(control, question);
		
		Group buttonGroup = makeButtonGroup(control);
		
		SelectionListener selectionQ1Listener = new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent event) {
	    		Button button = ((Button) event.widget);
	    		if (answeredQ1.get(button))
	    			return;
	    		answeredQ1.put(button, true);
	    		
	    		AuditTrailEntry entry = new AuditTrailEntry(PROCESS + "-" + getName() + "-QUESTION1"); //$NON-NLS-1$
				entry.setAttribute("quiz_question1_question", question); //$NON-NLS-1$
				entry.setAttribute("quiz_question1_answer", (String)button.getData(MSG)); //$NON-NLS-1$
				if (button.getData(MSG).equals(answer3)) {
					entry.setAttribute("quiz_question1_correct", "TRUE"); //$NON-NLS-1$ //$NON-NLS-2$
					markWrong(rbQ1A1, fbQ1A1);
					markWrong(rbQ1A2, fbQ1A2);
					markRight(rbQ1A3, fbQ1A3);
					markWrong(rbQ1A4, fbQ1A4);
					setPageComplete(true);
					getControl().redraw();
				}
				else if (button.getData(MSG).equals(answer1) 
						|| button.getData(MSG).equals(answer2)
						|| button.getData(MSG).equals(answer4)) {
					AbstractTreatmentWizardPage.incrError(getShell());
					entry.setAttribute("quiz_question1_correct", "FALSE"); //$NON-NLS-1$ //$NON-NLS-2$
					markWrong(button, feedbackQ1.get(button));
					setPageComplete(false);
					getControl().redraw();
				}
				logValidator.log(entry);
	    	};
	    };
	      
	    rbQ1A1 = makeButton(buttonGroup, answer1, selectionQ1Listener);
	    fbQ1A1 = makeFeedback(buttonGroup, feedback1);
	    feedbackQ1.put(rbQ1A1, fbQ1A1);
	    answeredQ1.put(rbQ1A1, false);
	    
		rbQ1A2 = makeButton(buttonGroup, answer2, selectionQ1Listener);
	    fbQ1A2 = makeFeedback(buttonGroup, feedback2);
	    feedbackQ1.put(rbQ1A2, fbQ1A2);
	    answeredQ1.put(rbQ1A2, false);
	    
		rbQ1A3 = makeButton(buttonGroup, answer3, selectionQ1Listener);
	    fbQ1A3 = makeFeedback(buttonGroup, feedback3);
	    feedbackQ1.put(rbQ1A3, fbQ1A3);
	    answeredQ1.put(rbQ1A3, false);
	    
		rbQ1A4 = makeButton(buttonGroup, answer4, selectionQ1Listener);
	    fbQ1A4 = makeFeedback(buttonGroup, feedback4);
	    feedbackQ1.put(rbQ1A4, fbQ1A4);
	    answeredQ1.put(rbQ1A4, false);
	}
}

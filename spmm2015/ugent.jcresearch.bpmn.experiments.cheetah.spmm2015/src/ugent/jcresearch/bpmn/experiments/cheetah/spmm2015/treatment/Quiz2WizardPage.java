package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import java.util.HashMap;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class Quiz2WizardPage extends AbstractTreatmentWizardPage {
	private int ls;
	
	private Button rbQ2A1, rbQ2A2, rbQ2A3, rbQ2A4;
	private Text fbQ2A1, fbQ2A2, fbQ2A3, fbQ2A4;
	private HashMap<Button, Text> feedbackQ2;
	private HashMap<Button, Boolean> answeredQ2;

	protected Quiz2WizardPage(LoggingValidator logValidator, String pageName, int ls) {
		super(logValidator, pageName);
		this.ls = ls;
		feedbackQ2 = new HashMap<Button, Text>();
		answeredQ2 = new HashMap<Button, Boolean>();
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Quiz time! (2/3)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeQuestion2(control);

		setControl(control);
		validate();
		setPageComplete(false);
	}
	
	private void makeQuestion2(Composite control) {
		final String question, answer1, answer2, answer3, answer4, feedback1, feedback2, feedback3, feedback4;
		if (getLearningStyle(ls) == SEQ) {
			question = format(Messages.Quiz2WizardPage_Question2_S);
			answer1 = format(Messages.Quiz2WizardPage_Question2_S_answer1);
			answer2 = format(Messages.Quiz2WizardPage_Question2_S_answer2);
			answer3 = format(Messages.Quiz2WizardPage_Question2_S_answer3);
			answer4 = format(Messages.Quiz2WizardPage_Question2_S_answer4);
			feedback1 = format(Messages.Quiz2WizardPage_Question2_S_feedback1);
			feedback2 = format(Messages.Quiz2WizardPage_Question2_S_feedback2);
			feedback3 = format(Messages.Quiz2WizardPage_Question2_S_feedback3);
			feedback4 = format(Messages.Quiz2WizardPage_Question2_S_feedback4);
		}
		else if (getLearningStyle(ls) == GLOB) {
			question = format(Messages.Quiz2WizardPage_Question2_G);
			answer1 = format(Messages.Quiz2WizardPage_Question2_G_answer1);
			answer2 = format(Messages.Quiz2WizardPage_Question2_G_answer2);
			answer3 = format(Messages.Quiz2WizardPage_Question2_G_answer3);
			answer4 = format(Messages.Quiz2WizardPage_Question2_G_answer4);
			feedback1 = format(Messages.Quiz2WizardPage_Question2_G_feedback1);
			feedback2 = format(Messages.Quiz2WizardPage_Question2_G_feedback2);
			feedback3 = format(Messages.Quiz2WizardPage_Question2_G_feedback3);
			feedback4 = format(Messages.Quiz2WizardPage_Question2_G_feedback4);
		}
		else {
			question = format(Messages.Quiz2WizardPage_Question2_B);
			answer1 = format(Messages.Quiz2WizardPage_Question2_B_answer1);
			answer2 = format(Messages.Quiz2WizardPage_Question2_B_answer2);
			answer3 = format(Messages.Quiz2WizardPage_Question2_B_answer3);
			answer4 = format(Messages.Quiz2WizardPage_Question2_B_answer4);
			feedback1 = format(Messages.Quiz2WizardPage_Question2_B_feedback1);
			feedback2 = format(Messages.Quiz2WizardPage_Question2_B_feedback2);
			feedback3 = format(Messages.Quiz2WizardPage_Question2_B_feedback3);
			feedback4 = format(Messages.Quiz2WizardPage_Question2_B_feedback4);
		}
		
		makeText(control, question);
		
		Group buttonGroup = makeButtonGroup(control);
		
		SelectionListener selectionQ2Listener = new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent event) {
	    		Button button = ((Button) event.widget);
	    		if (answeredQ2.get(button))
	    			return;
	    		answeredQ2.put(button, true);
	    		
	    		AuditTrailEntry entry = new AuditTrailEntry(PROCESS + "-" + getName() + "-QUESTION2");
				entry.setAttribute("quiz_question2_question", question); //$NON-NLS-1$
				entry.setAttribute("quiz_question2_answer", (String)button.getData(MSG)); //$NON-NLS-1$
				entry.setAttribute("learning_style", ls); //$NON-NLS-1$
				if (button.getData(MSG).equals(answer4)) {
					entry.setAttribute("quiz_question2_correct", "TRUE"); //$NON-NLS-1$ //$NON-NLS-2$
					markWrong(rbQ2A1, fbQ2A1);
					markWrong(rbQ2A2, fbQ2A2);
					markWrong(rbQ2A3, fbQ2A3);
					markRight(rbQ2A4, fbQ2A4);
					setPageComplete(true);
					getControl().redraw();
				}
				else if (button.getData(MSG).equals(answer1)
						|| button.getData(MSG).equals(answer2)
						|| button.getData(MSG).equals(answer3)){
					AbstractTreatmentWizardPage.incrError(getShell());
					entry.setAttribute("quiz_question2_correct", "FALSE"); //$NON-NLS-1$ //$NON-NLS-2$
					markWrong(button, feedbackQ2.get(button));
					setPageComplete(false);
					getControl().redraw();
				}
				logValidator.log(entry);
	    	};
	    };
	      
	    rbQ2A1 = makeButton(buttonGroup, answer1, selectionQ2Listener);
	    fbQ2A1 = makeFeedback(buttonGroup, feedback1);
	    feedbackQ2.put(rbQ2A1, fbQ2A1);
	    answeredQ2.put(rbQ2A1, false);
	    
		rbQ2A2 = makeButton(buttonGroup, answer2, selectionQ2Listener);
	    fbQ2A2 = makeFeedback(buttonGroup, feedback2);
	    feedbackQ2.put(rbQ2A2, fbQ2A2);
	    answeredQ2.put(rbQ2A2, false);
	    
		rbQ2A3 = makeButton(buttonGroup, answer3, selectionQ2Listener);
	    fbQ2A3 = makeFeedback(buttonGroup, feedback3);
	    feedbackQ2.put(rbQ2A3, fbQ2A3);
	    answeredQ2.put(rbQ2A3, false);
	    
		rbQ2A4 = makeButton(buttonGroup, answer4, selectionQ2Listener);
	    fbQ2A4 = makeFeedback(buttonGroup, feedback4);
	    feedbackQ2.put(rbQ2A4, fbQ2A4);
	    answeredQ2.put(rbQ2A4, false);
	}
}

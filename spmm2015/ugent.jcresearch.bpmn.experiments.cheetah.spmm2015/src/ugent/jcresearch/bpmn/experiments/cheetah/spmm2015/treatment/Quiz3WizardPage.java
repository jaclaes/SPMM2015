package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import java.util.HashMap;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class Quiz3WizardPage extends AbstractTreatmentWizardPage {
	private int ls;
	
	private Button rbQ3A1, rbQ3A2, rbQ3A3, rbQ3A4;
	private Text fbQ3A1, fbQ3A2, fbQ3A3, fbQ3A4;
	private HashMap<Button, Text> feedbackQ3;
	private HashMap<Button, Boolean> answeredQ3;

	protected Quiz3WizardPage(LoggingValidator logValidator, String pageName, int ls) {
		super(logValidator, pageName);
		this.ls = ls;
		feedbackQ3 = new HashMap<Button, Text>();
		answeredQ3 = new HashMap<Button, Boolean>();
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Quiz time! (3/3)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeQuestion3(control);

		setControl(control);
		validate();
		setPageComplete(false);
	}
	private void makeQuestion3(Composite control) {
		final String question, answer1, answer2, answer3, answer4, feedback1, feedback2, feedback3, feedback4;
		if (getLearningStyle(ls) == SEQ) {
			question = format(Messages.Quiz3WizardPage_Question3_S);
			answer1 = format(Messages.Quiz3WizardPage_Question3_S_answer1);
			answer2 = format(Messages.Quiz3WizardPage_Question3_S_answer2);
			answer3 = format(Messages.Quiz3WizardPage_Question3_S_answer3);
			answer4 = format(Messages.Quiz3WizardPage_Question3_S_answer4);
			feedback1 = format(Messages.Quiz3WizardPage_Question3_S_feedback1);
			feedback2 = format(Messages.Quiz3WizardPage_Question3_S_feedback2);
			feedback3 = format(Messages.Quiz3WizardPage_Question3_S_feedback3);
			feedback4 = format(Messages.Quiz3WizardPage_Question3_S_feedback4);
		}
		else if (getLearningStyle(ls) == GLOB) {
			question = format(Messages.Quiz3WizardPage_Question3_G);
			answer1 = format(Messages.Quiz3WizardPage_Question3_G_answer1);
			answer2 = format(Messages.Quiz3WizardPage_Question3_G_answer2);
			answer3 = format(Messages.Quiz3WizardPage_Question3_G_answer3);
			answer4 = format(Messages.Quiz3WizardPage_Question3_G_answer4);
			feedback1 = format(Messages.Quiz3WizardPage_Question3_G_feedback1);
			feedback2 = format(Messages.Quiz3WizardPage_Question3_G_feedback2);
			feedback3 = format(Messages.Quiz3WizardPage_Question3_G_feedback3);
			feedback4 = format(Messages.Quiz3WizardPage_Question3_G_feedback4);
		}
		else {
			question = format(Messages.Quiz3WizardPage_Question3_B);
			answer1 = format(Messages.Quiz3WizardPage_Question3_B_answer1);
			answer2 = format(Messages.Quiz3WizardPage_Question3_B_answer2);
			answer3 = format(Messages.Quiz3WizardPage_Question3_B_answer3);
			answer4 = format(Messages.Quiz3WizardPage_Question3_B_answer4);
			feedback1 = format(Messages.Quiz3WizardPage_Question3_B_feedback1);
			feedback2 = format(Messages.Quiz3WizardPage_Question3_B_feedback2);
			feedback3 = format(Messages.Quiz3WizardPage_Question3_B_feedback3);
			feedback4 = format(Messages.Quiz3WizardPage_Question3_B_feedback4);
		}
		
		makeText(control, question);
		
		Group buttonGroup = makeButtonGroup(control);
		
		SelectionListener selectionQ3Listener = new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent event) {
	    		Button button = ((Button) event.widget);
	    		if (answeredQ3.get(button))
	    			return;
	    		answeredQ3.put(button, true);
	    		
	    		AuditTrailEntry entry = new AuditTrailEntry(PROCESS + "-" + getName() + "-QUESTION3");
				entry.setAttribute("quiz_question3_question", question); //$NON-NLS-1$
				entry.setAttribute("quiz_question3_answer", (String)button.getData(MSG)); //$NON-NLS-1$
				entry.setAttribute("learning_style", ls); //$NON-NLS-1$
				if (button.getData(MSG).equals(answer1)) {
					entry.setAttribute("quiz_question3_correct", "TRUE"); //$NON-NLS-1$ //$NON-NLS-2$
					markRight(rbQ3A1, fbQ3A1);
					markWrong(rbQ3A2, fbQ3A2);
					markWrong(rbQ3A3, fbQ3A3);
					markWrong(rbQ3A4, fbQ3A4);
					setPageComplete(true);
					getControl().redraw();
				}
				else if (button.getData(MSG).equals(answer2)
						|| button.getData(MSG).equals(answer3)
						|| button.getData(MSG).equals(answer4)){
					AbstractTreatmentWizardPage.incrError(getShell());
					entry.setAttribute("quiz_question3_correct", "FALSE"); //$NON-NLS-1$ //$NON-NLS-2$
					markWrong(button, feedbackQ3.get(button));
					setPageComplete(false);
					getControl().redraw();
				}
				logValidator.log(entry);
	    	};
	    };
	      
	    rbQ3A1 = makeButton(buttonGroup, answer1, selectionQ3Listener);
	    fbQ3A1 = makeFeedback(buttonGroup, feedback1);
	    feedbackQ3.put(rbQ3A1, fbQ3A1);
	    answeredQ3.put(rbQ3A1, false);
	    
		rbQ3A2 = makeButton(buttonGroup, answer2, selectionQ3Listener);
	    fbQ3A2 = makeFeedback(buttonGroup, feedback2);
	    feedbackQ3.put(rbQ3A2, fbQ3A2);
	    answeredQ3.put(rbQ3A2, false);
	    
		rbQ3A3 = makeButton(buttonGroup, answer3, selectionQ3Listener);
	    fbQ3A3 = makeFeedback(buttonGroup, feedback3);
	    feedbackQ3.put(rbQ3A3, fbQ3A3);
	    answeredQ3.put(rbQ3A3, false);
	    
		rbQ3A4 = makeButton(buttonGroup, answer4, selectionQ3Listener);
	    fbQ3A4 = makeFeedback(buttonGroup, feedback4);
	    feedbackQ3.put(rbQ3A4, fbQ3A4);
	    answeredQ3.put(rbQ3A4, false);
	}
}

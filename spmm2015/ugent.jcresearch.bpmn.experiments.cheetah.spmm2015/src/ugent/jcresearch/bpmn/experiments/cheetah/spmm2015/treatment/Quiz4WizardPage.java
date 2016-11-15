package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import java.util.HashMap;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class Quiz4WizardPage extends AbstractTreatmentWizardPage {
	private int ls;
	private double fd;
	
	private Button rbQ4A1, rbQ4A2, rbQ4A3, rbQ4A4;
	private Text fbQ4A1, fbQ4A2, fbQ4A3, fbQ4A4;
	private HashMap<Button, Text> feedbackQ4;
	private HashMap<Button, Boolean> answeredQ4;

	protected Quiz4WizardPage(LoggingValidator logValidator, String pageName, double fd, int ls) {
		super(logValidator, pageName);
		this.fd = fd;
		this.ls = ls;
		feedbackQ4 = new HashMap<Button, Text>();
		answeredQ4 = new HashMap<Button, Boolean>();
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Quiz time! (1/1)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.Quiz4WizardPage_Introduction));
		makeQuestion4(control);

		setControl(control);
		validate();
		setPageComplete(false);
	}
	private String getModelingStyle(int learningStyle) {
		if (getLearningStyle(ls) == SEQ)
			return format(Messages.Quiz4WizardPage_flow_oriented);
		else if (getLearningStyle(ls) == GLOB)
			return format(Messages.Quiz4WizardPage_aspect_oriented);
		else
			return format(Messages.Quiz4WizardPage_combined);
	}
	private void makeQuestion4(Composite control) {
		final String question, answer1, answer2, answer3, answer4, feedback1, feedback2, feedback3, feedback4;
		if (getFieldDependency(fd) == FD) {
			question = format(Messages.Quiz4WizardPage_Question4_FD);
			answer1 = format(NLS.bind(Messages.Quiz4WizardPage_Question4_FD_answer1, getModelingStyle(ls)));
			answer2 = format(NLS.bind(Messages.Quiz4WizardPage_Question4_FD_answer2, getModelingStyle(ls)));
			answer3 = format(Messages.Quiz4WizardPage_Question4_FD_answer3);
			answer4 = format(Messages.Quiz4WizardPage_Question4_FD_answer4);
			if (getLearningStyle(ls) == SEQ) {
				feedback1 = format(Messages.Quiz4WizardPage_Question4_FD_S_feedback1);
				feedback2 = format(Messages.Quiz4WizardPage_Question4_FD_S_feedback2);
			}
			else if (getLearningStyle(ls) == GLOB) {
				feedback1 = format(Messages.Quiz4WizardPage_Question4_FD_G_feedback1);
				feedback2 = format(Messages.Quiz4WizardPage_Question4_FD_G_feedback2);
			}
			else {
				feedback1 = format(Messages.Quiz4WizardPage_Question4_FD_B_feedback1);
				feedback2 = format(Messages.Quiz4WizardPage_Question4_FD_B_feedback2);
			}
			feedback3 = format(Messages.Quiz4WizardPage_Question4_FD_feedback3);
			feedback4 = format(Messages.Quiz4WizardPage_Question4_FD_feedback4);
		}
		else {
			question = format(Messages.Quiz4WizardPage_Question4_FID);
			answer1 = format(NLS.bind(Messages.Quiz4WizardPage_Question4_FID_answer1, getModelingStyle(ls)));
			answer2 = format(Messages.Quiz4WizardPage_Question4_FID_answer2);
			answer3 = format(Messages.Quiz4WizardPage_Question4_FID_answer3);
			answer4 = format(Messages.Quiz4WizardPage_Question4_FID_answer4);
			if (getLearningStyle(ls) == SEQ) {
				feedback1 = format(Messages.Quiz4WizardPage_Question4_FID_S_feedback1);
			}
			else if (getLearningStyle(ls) == GLOB) {
				feedback1 = format(Messages.Quiz4WizardPage_Question4_FID_G_feedback1);
			}
			else {
				feedback1 = format(Messages.Quiz4WizardPage_Question4_FID_C_feedback1);
			}
			feedback2 = format(Messages.Quiz4WizardPage_Question4_FID_feedback2);
			feedback3 = format(Messages.Quiz4WizardPage_Question4_FID_feedback3);
			feedback4 = format(Messages.Quiz4WizardPage_Question4_FID_feedback4);
		}
		
		makeText(control, question);
		
		Group buttonGroup = makeButtonGroup(control);
		
		SelectionListener selectionQ4Listener = new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent event) {
	    		Button button = ((Button) event.widget);
	    		if (answeredQ4.get(button))
	    			return;
	    		answeredQ4.put(button, true);
	    		
	    		AuditTrailEntry entry = new AuditTrailEntry(PROCESS + "-" + getName() + "-QUESTION4");
				entry.setAttribute("quiz_question4_question", question); //$NON-NLS-1$
				entry.setAttribute("quiz_question4_answer", (String)button.getData(MSG)); //$NON-NLS-1$
				entry.setAttribute("field_dependency", Double.toString(fd)); //$NON-NLS-1$
				entry.setAttribute("learning_style", ls); //$NON-NLS-1$
				if (button.getData(MSG).equals(answer4)) {
					entry.setAttribute("quiz_question4_correct", "TRUE"); //$NON-NLS-1$ //$NON-NLS-2$
					markWrong(rbQ4A1, fbQ4A1);
					markWrong(rbQ4A2, fbQ4A2);
					markWrong(rbQ4A3, fbQ4A3);
					markRight(rbQ4A4, fbQ4A4);
					setPageComplete(true);
					getControl().redraw();
				}
				else if (button.getData(MSG).equals(answer1)
						|| button.getData(MSG).equals(answer2)
						|| button.getData(MSG).equals(answer3)) {
					AbstractTreatmentWizardPage.incrError(getShell());
					entry.setAttribute("quiz_question4_correct", "FALSE"); //$NON-NLS-1$ //$NON-NLS-2$
					markWrong(button, feedbackQ4.get(button));
					setPageComplete(false);
					getControl().redraw();
				}
				logValidator.log(entry);
	    	};
	    };
	      
	    rbQ4A1 = makeButton(buttonGroup, answer1, selectionQ4Listener);
	    fbQ4A1 = makeFeedback(buttonGroup, feedback1);
	    feedbackQ4.put(rbQ4A1, fbQ4A1);
	    answeredQ4.put(rbQ4A1, false);
	    
		rbQ4A2 = makeButton(buttonGroup, answer2, selectionQ4Listener);
	    fbQ4A2 = makeFeedback(buttonGroup, feedback2);
	    feedbackQ4.put(rbQ4A2, fbQ4A2);
	    answeredQ4.put(rbQ4A2, false);
	    
		rbQ4A3 = makeButton(buttonGroup, answer3, selectionQ4Listener);
	    fbQ4A3 = makeFeedback(buttonGroup, feedback3);
	    feedbackQ4.put(rbQ4A3, fbQ4A3);
	    answeredQ4.put(rbQ4A3, false);
	    
		rbQ4A4 = makeButton(buttonGroup, answer4, selectionQ4Listener);
	    fbQ4A4 = makeFeedback(buttonGroup, feedback4);
	    feedbackQ4.put(rbQ4A4, fbQ4A4);
	    answeredQ4.put(rbQ4A4, false);
	}
}

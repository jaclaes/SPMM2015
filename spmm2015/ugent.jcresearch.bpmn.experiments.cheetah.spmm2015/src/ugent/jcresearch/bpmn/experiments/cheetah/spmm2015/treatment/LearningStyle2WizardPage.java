package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class LearningStyle2WizardPage extends AbstractTreatmentWizardPage {
	private static final String SEQUENTIAL = format(Messages.LearningStyle2WizardPage_Relatively_sequential);
	private static final String INBETWEEN = format(Messages.LearningStyle2WizardPage_In_between);
	private static final String GLOBAL = format(Messages.LearningStyle2WizardPage_Relatively_global);
	private int ls;
	
	private Button rbSeq, rbBetw, rbGlob;
	private Text feedback;
	private StyledText assess;
	private Button strongDisagree, disagree, neutral, agree, strongAgree;
	private Group likertGroup;

	protected LearningStyle2WizardPage(LoggingValidator loggingValidator, String pageName, int ls) {
		super(loggingValidator, pageName);
		this.ls = ls;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Learning style (2/8)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.LearningStyle2WizardPage_Learning_style_score));
		
		makePic(control, getResourcePath("ls.png")); //$NON-NLS-1$
		
		StyledText style = makeText(control, format(NLS.bind(Messages.LearningStyle2WizardPage_Your_score_was_which_is, ls)));
		setBold(style, format(NLS.bind(Messages.LearningStyle2WizardPage_Your_score_was, ls)));
		
		makeRadioButtons(control);
		
		assess = makeText(control, format(Messages.LearningStyle2WizardPage_Agree));
		assess.setVisible(false);
		makeLikertButtons(control, "learning_style"); //$NON-NLS-1$
		
		makeSource(control, format(Messages.LearningStyle2WizardPage_source));

		setControl(control);
		validate();
		setPageComplete(false);
	}
	private String getLearningStyle() {
		if (getLearningStyle(ls) == SEQ)
			return SEQUENTIAL;
		else if (getLearningStyle(ls) == GLOB)
			return GLOBAL;
		else
			return INBETWEEN;
	}
	private void makeRadioButtons(Composite control) {
		Group buttonGroup = makeButtonGroup(control);
		
		SelectionListener selectionListener = new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent event) {
	    		Button button = ((Button) event.widget);
	    		AuditTrailEntry entry = new AuditTrailEntry(PROCESS + "-" + getName() + "-LSINGRAPH");
				if (getLearningStyle().equals(button.getData(MSG))) {
					entry.setAttribute("learning_style_correct", "TRUE"); //$NON-NLS-1$ //$NON-NLS-2$
					rbSeq.setEnabled(false);
					rbBetw.setEnabled(false);
					rbGlob.setEnabled(false);
					button.setEnabled(true);
					feedback.setText(format(NLS.bind(Messages.LearningStyle2WizardPage_Correct, getLearningStyle().toLowerCase())));
					showFeedback(feedback);
					assess.setVisible(true);
					strongDisagree.setEnabled(true);
					disagree.setEnabled(true);
					neutral.setEnabled(true);
					agree.setEnabled(true);
					strongAgree.setEnabled(true);
					likertGroup.setVisible(true);
					getControl().redraw();
				}
				else {
					entry.setAttribute("learning_style_correct", "FALSE"); //$NON-NLS-1$ //$NON-NLS-2$
					button.setEnabled(false);
					feedback.setText(format(Messages.LearningStyle2WizardPage_Incorrect));
					showFeedback(feedback);
					setPageComplete(false);
					getControl().redraw();
				}
				logValidator.log(entry);
	    	};
	    };
	      
	    rbSeq = makeButton(buttonGroup, SEQUENTIAL, selectionListener);
		rbBetw = makeButton(buttonGroup, INBETWEEN, selectionListener);
		rbGlob = makeButton(buttonGroup, GLOBAL, selectionListener);
		
		feedback = makeFeedback(control, 
				format(NLS.bind(Messages.LearningStyle2WizardPage_Correct, getLearningStyle().toLowerCase())));
	}
	private void makeLikertButtons(Composite control, String factor) {
		likertGroup = makeButtonGroup(control, 5);
		likertGroup.setVisible(false);
	      
	    strongDisagree = makeLikertButton(likertGroup, factor, Messages.AbstractTreatmentWizardPage_strong_disagree);
	    disagree = makeLikertButton(likertGroup, factor, Messages.AbstractTreatmentWizardPage_disagree);
	    neutral = makeLikertButton(likertGroup, factor, Messages.AbstractTreatmentWizardPage_neutral);
	    agree = makeLikertButton(likertGroup, factor, Messages.AbstractTreatmentWizardPage_agree);
	    strongAgree = makeLikertButton(likertGroup, factor, Messages.AbstractTreatmentWizardPage_strong_agree);

		strongDisagree.setEnabled(false);
		disagree.setEnabled(false);
		neutral.setEnabled(false);
		agree.setEnabled(false);
		strongAgree.setEnabled(false);
	}
}

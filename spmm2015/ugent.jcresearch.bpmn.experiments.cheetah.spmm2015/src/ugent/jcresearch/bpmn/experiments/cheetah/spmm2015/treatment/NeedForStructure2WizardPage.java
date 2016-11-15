package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class NeedForStructure2WizardPage extends AbstractTreatmentWizardPage {
	private static final String HIGH_DESIRE = format(Messages.NeedForStructure2WizardPage_Relatively_high_desire);
	private static final String LOW_DEISRE = format(Messages.NeedForStructure2WizardPage_Relatively_low_desire);
	private double nfs1;

	private Button rbLNFS1, rbHNFS1;
	private Text feedback;
	private StyledText assess;
	private Button strongDisagree, disagree, neutral, agree, strongAgree;
	private Group likertGroup;
	
	protected NeedForStructure2WizardPage(LoggingValidator logValidator, String pageName, double nfs1) {
		super(logValidator, pageName);
		this.nfs1 = nfs1;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Desire for structure (2/4)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.NeedForStructure2WizardPage_Desire_score));
		
		makePic(control, getResourcePath("nfs1.png")); //$NON-NLS-1$
		
		StyledText style = makeText(control, format(NLS.bind(Messages.NeedForStructure2WizardPage_Your_score_was_which_is, nfs1)));
		setBold(style, format(NLS.bind(Messages.NeedForStructure2WizardPage_Your_score_was, nfs1)));

		makeRadioButtons(control);
		
		assess = makeText(control, format(Messages.NeedForStructure2WizardPage_Agree));
		assess.setVisible(false);
		makeLikertButtons(control, "need_for_structure_1"); //$NON-NLS-1$
		
		makeSource(control, format(Messages.NeedForStructure2WizardPage_source));
		
		setControl(control);
		validate();
		setPageComplete(false);

	}
	private String getDesireForStructure() {
		if (getNeedForStructure1(nfs1) == HNFS1)
			return HIGH_DESIRE;
		else
			return LOW_DEISRE;
	}
	private void makeRadioButtons(Composite control) {
		Group buttonGroup = makeButtonGroup(control);
		
		SelectionListener selectionListener = new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent event) {
	    		Button button = ((Button) event.widget);
	    		AuditTrailEntry entry = new AuditTrailEntry(PROCESS + "-" + getName() + "-NFS1INGRAPH");
				if (getDesireForStructure().equals(button.getData(MSG))) {
					entry.setAttribute("need_for_structue1_correct", "TRUE"); //$NON-NLS-1$ //$NON-NLS-2$
					rbHNFS1.setEnabled(false);
					rbLNFS1.setEnabled(false);
					button.setEnabled(true);
					feedback.setText(format(NLS.bind(Messages.NeedForStructure2WizardPage_Correct, getDesireForStructure().toLowerCase())));
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
					entry.setAttribute("need_for_structue1_correct", "FALSE"); //$NON-NLS-1$ //$NON-NLS-2$
					button.setEnabled(false);
					feedback.setText(format(Messages.NeedForStructure2WizardPage_Incorrect));
					showFeedback(feedback);
					setPageComplete(false);
					getControl().redraw();
				}
				logValidator.log(entry);
	    	};
	    };
	      
	    rbHNFS1 = makeButton(buttonGroup, HIGH_DESIRE, selectionListener);
	    rbLNFS1 = makeButton(buttonGroup, LOW_DEISRE, selectionListener);
		
		feedback = makeFeedback(control, 
				format(NLS.bind(Messages.NeedForStructure2WizardPage_Correct, getDesireForStructure().toLowerCase())));
		
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

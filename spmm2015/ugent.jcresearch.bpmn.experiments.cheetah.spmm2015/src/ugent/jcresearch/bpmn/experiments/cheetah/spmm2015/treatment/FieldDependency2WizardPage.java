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

public class FieldDependency2WizardPage extends AbstractTreatmentWizardPage {
	private static final String FIELD_DEPENDENT = format(Messages.FieldDependency2WizardPage_Relatively_field_dependent);
	private static final String FIELD_INDEPENDENT = format(Messages.FieldDependency2WizardPage_Relatively_field_independent);
	private double fd;

	private Button rbFD, rbFID;
	private Text feedback;
	private StyledText assess;
	private Button strongDisagree, disagree, neutral, agree, strongAgree;
	private Group likertGroup;
	
	protected FieldDependency2WizardPage(LoggingValidator logValidator, String pageName, double fd) {
		super(logValidator, pageName);
		this.fd = fd;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Field dependency (2/4)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.FieldDependency2WizardPage_Field_dependent_score));
		
		makePic(control, getResourcePath("fd.png")); //$NON-NLS-1$
		
		StyledText style = makeText(control, format(NLS.bind(Messages.FieldDependency2WizardPage_Your_score_was_which_is, fd)));
		setBold(style, format(NLS.bind(Messages.FieldDependency2WizardPage_Your_score_was, fd)));
		
		makeRadioButtons(control);
		
		assess = makeText(control, format(Messages.FieldDependency2WizardPage_Agree));
		assess.setVisible(false);
		makeLikertButtons(control, "field_dependency"); //$NON-NLS-1$
		
		makeSource(control, format(Messages.FieldDependency2WizardPage_source));
		
		setControl(control);
		validate();
		setPageComplete(false);

	}
	private String getFieldDependency() {
		if (getFieldDependency(fd) == FD)
			return FIELD_DEPENDENT;
		else
			return FIELD_INDEPENDENT;
	}
	private void makeRadioButtons(Composite control) {
		Group buttonGroup = makeButtonGroup(control);
		
		SelectionListener selectionListener = new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent event) {
	    		Button button = ((Button) event.widget);
	    		AuditTrailEntry entry = new AuditTrailEntry(PROCESS + "-" + getName() + "-FDINGRAPH");
				if (getFieldDependency().equals(button.getData(MSG))) {
					entry.setAttribute("field_dependency_correct", "TRUE"); //$NON-NLS-1$ //$NON-NLS-2$
					rbFD.setEnabled(false);
					rbFID.setEnabled(false);
					button.setEnabled(true);
					feedback.setText(format(NLS.bind(Messages.FieldDependency2WizardPage_Correct, getFieldDependency().toLowerCase())));
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
					entry.setAttribute("field_dependency_correct", "FALSE"); //$NON-NLS-1$ //$NON-NLS-2$
					button.setEnabled(false);
					feedback.setText(format(Messages.FieldDependency2WizardPage_Incorrect));
					showFeedback(feedback);
					setPageComplete(false);
					getControl().redraw();
				}
				logValidator.log(entry);
	    	};
	    };
	      
	    rbFD = makeButton(buttonGroup, FIELD_DEPENDENT, selectionListener);
	    rbFID = makeButton(buttonGroup, FIELD_INDEPENDENT, selectionListener);
		
		feedback = makeFeedback(control, 
				format(NLS.bind(Messages.FieldDependency2WizardPage_Correct, getFieldDependency().toLowerCase())));

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

package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class LearningStyle4WizardPage extends AbstractTreatmentWizardPage {
	private static final String FLOW_ORIENTED = format(Messages.LearningStyle4WizardPage_Flow_oriented);
	private static final String ASPECT_ORIENTED = format(Messages.LearningStyle4WizardPage_Aspect_oriented);
	private static final String COMBINED = format(Messages.LearningStyle4WizardPage_Combined);
	private int ls;
	
	private Button rbFO, rbAO, rbC;
	private Text feedback;

	protected LearningStyle4WizardPage(LoggingValidator logValidator, String pageName, int ls) {
		super(logValidator, pageName);
		this.ls = ls;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Learning style (4/8)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.LearningStyle4WizardPage_Assess_screenshots));
		
		if (getLearningStyle() == FLOW_ORIENTED)
			makePic(control, getResourcePath("foex.png")); //$NON-NLS-1$
		else if (getLearningStyle() == ASPECT_ORIENTED)
			makePic(control, getResourcePath("aoex.png")); //$NON-NLS-1$
		else
			makePic(control, getResourcePath("cex.png")); //$NON-NLS-1$
		
		makeRadioButtons(control);
		
		setControl(control);
		validate();
		setPageComplete(false);

	}
	private String getLearningStyle() {
		if (getLearningStyle(ls) == SEQ)
			return FLOW_ORIENTED;
		else if (getLearningStyle(ls) == GLOB)
			return ASPECT_ORIENTED;
		else
			return COMBINED;
	}
	private void makeRadioButtons(Composite control) {
		Group buttonGroup = makeButtonGroup(control);
		
		SelectionListener selectionListener = new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent event) {
	    		Button button = ((Button) event.widget);
	    		AuditTrailEntry entry = new AuditTrailEntry(PROCESS + "-" + getName() + "-MODSTYLERECOGNIZE");
				if (getLearningStyle().equals(button.getData(MSG))) {
					entry.setAttribute("modeling_style_correct", "TRUE"); //$NON-NLS-1$ //$NON-NLS-2$
					rbFO.setEnabled(false);
					rbAO.setEnabled(false);
					rbC.setEnabled(false);
					button.setEnabled(true);
					feedback.setText(format(NLS.bind(Messages.LearningStyle4WizardPage_Correct, getLearningStyle().toLowerCase())));
					showFeedback(feedback);
					setPageComplete(true);
					getControl().redraw();
				}
				else {
					entry.setAttribute("modeling_style_correct", "FALSE"); //$NON-NLS-1$ //$NON-NLS-2$
					button.setEnabled(false);
					if (getLearningStyle().equals(FLOW_ORIENTED)) {
						if (button.getData(MSG).equals(ASPECT_ORIENTED))
							feedback.setText(format(Messages.LearningStyle4WizardPage_FO_AO));
						else
							feedback.setText(format(Messages.LearningStyle4WizardPage_FO_C));
					}
					else if (getLearningStyle().equals(ASPECT_ORIENTED)) {
						if (button.getData(MSG).equals(FLOW_ORIENTED))
							feedback.setText(format(Messages.LearningStyle4WizardPage_AO_FO));
						else
							feedback.setText(format(Messages.LearningStyle4WizardPage_AO_C));
					}
					else {
						if (button.getData(MSG).equals(FLOW_ORIENTED))
							feedback.setText(format(Messages.LearningStyle4WizardPage_C_FO));
						else
							feedback.setText(format(Messages.LearningStyle4WizardPage_C_AO));
					}
					showFeedback(feedback);
					setPageComplete(false);
					getControl().redraw();
				}
				logValidator.log(entry);
	    	};
	    };
	      
	    rbFO = makeButton(buttonGroup, FLOW_ORIENTED, selectionListener);
		rbAO = makeButton(buttonGroup, ASPECT_ORIENTED, selectionListener);
		rbC = makeButton(buttonGroup, COMBINED, selectionListener);
		
		feedback = makeFeedback(control, 5);
	}
}

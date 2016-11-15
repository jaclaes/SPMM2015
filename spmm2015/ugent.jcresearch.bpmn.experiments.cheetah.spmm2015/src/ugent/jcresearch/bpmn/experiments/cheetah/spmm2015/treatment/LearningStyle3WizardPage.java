package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class LearningStyle3WizardPage extends AbstractTreatmentWizardPage {
	private static final String FLOW_ORIENTED = format(Messages.LearningStyle3WizardPage_Style_flow_oriented);
	private static final String SOMEWHAT_FLOW_ORIENTED = format(Messages.LearningStyle3WizardPage_Style_somewhat_flow_oriented);
	private static final String COMBINED = format(Messages.LearningStyle3WizardPage_Style_combined);
	private static final String SOMEWHAT_ASPECT_ORIENTED = format(Messages.LearningStyle3WizardPage_Style_somewhat_aspect_oriented);
	private static final String ASPECT_ORIENTED = format(Messages.LearningStyle3WizardPage_Style_aspect_oriented);
	private static final String OTHER = format(Messages.LearningStyle3WizardPage_Style_none_of_the_above);
	private static final String DONT_KNOW = format(Messages.LearningStyle3WizardPage_Style_dont_know);
	
	private Button rbFO, rbSFO, rbC, rbSAO, rbAO, rbOther, rbDontKnow;

	protected LearningStyle3WizardPage(LoggingValidator logValidator, String pageName) {
		super(logValidator, pageName);
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Learning style (3/8)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.LearningStyle3WizardPage_Flow_aspect_oriented));
		
		StyledText txtFO = makeBulletedText(control, format(Messages.LearningStyle3WizardPage_Flow_oriented_is));
		setBold(txtFO, format(Messages.LearningStyle3WizardPage_Flow_oriented));
		
		StyledText txtAO = makeBulletedText(control, format(Messages.LearningStyle3WizardPage_Aspect_oriented_is));
		setBold(txtAO, format(Messages.LearningStyle3WizardPage_Aspect_oriented));
		
		StyledText txtC = makeBulletedText(control, format(Messages.LearningStyle3WizardPage_Combined_is));
		setBold(txtC, format(Messages.LearningStyle3WizardPage_Combined));
		
		makeSource(control, format(Messages.LearningStyle3WizardPage_source));
		
		makeReflectionQuestion(control);
		
		setControl(control);
		validate();
		setPageComplete(false);
	}
	private void makeReflectionQuestion(Composite control) {
	    makeText(control, format(Messages.LearningStyle3WizardPage_Reflection_question));

	    Group buttonGroup = makeButtonGroup(control);
		
		SelectionListener selectionListener = new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent event) {
	    		Button button = ((Button) event.widget);
	    		AuditTrailEntry entry = new AuditTrailEntry(PROCESS + "-" + getName() + "-MODSTYLEBEFORE");
	    		entry.setAttribute("modeling_style_reflection", (String)button.getData(MSG)); //$NON-NLS-1$
				setPageComplete(true);
				getControl().redraw();
				logValidator.log(entry);
	    	}
	    };
	    
		rbOther = makeButton(buttonGroup, OTHER, selectionListener);
	    rbFO = makeButton(buttonGroup, FLOW_ORIENTED, selectionListener);
	    rbSFO = makeButton(buttonGroup, SOMEWHAT_FLOW_ORIENTED, selectionListener);
		rbC = makeButton(buttonGroup, COMBINED, selectionListener);
		rbSAO = makeButton(buttonGroup, SOMEWHAT_ASPECT_ORIENTED, selectionListener);
		rbAO = makeButton(buttonGroup, ASPECT_ORIENTED, selectionListener);
		rbDontKnow = makeButton(buttonGroup, DONT_KNOW, selectionListener);
	}
}

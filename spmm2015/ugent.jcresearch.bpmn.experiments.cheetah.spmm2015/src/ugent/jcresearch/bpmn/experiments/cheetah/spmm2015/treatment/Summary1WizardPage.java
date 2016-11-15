package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class Summary1WizardPage extends AbstractTreatmentWizardPage {
	private int ls;
	private double fd, nfs1;

	protected Summary1WizardPage(LoggingValidator logValidator, String pageName, int ls, double fd, double nfs1) {
		super(logValidator, pageName);
		this.ls = ls;
		this.fd = fd;
		this.nfs1 = nfs1;
	}

	@Override
	public void createControl(Composite parent) {
		setDescription("Overview (1/2)"); //$NON-NLS-1$
		
		Composite control = getMainControl(parent);

		makeText(control, format(Messages.Summary1WizardPage_You_are));
		
		StyledText txtLS = makeText(control, format(NLS.bind(Messages.Summary1WizardPage_Learning_style, learningStyle(), ls)));
		setBold(txtLS, learningStyle());
		makeBulletedText(control, learningStyleRecommendation());
		
		StyledText txtFD = makeText(control, format(NLS.bind(Messages.Summary1WizardPage_Field_dependency, fieldDependency(), fd)));
		setBold(txtFD, fieldDependency());
		makeBulletedText(control, fieldDependencyRecommendation());
		
		StyledText txtDFS = makeText(control, format(NLS.bind(Messages.Summary1WizardPage_Desire_for_structure, needForStructure1(), nfs1)));
		setBold(txtDFS, needForStructure1());
		makeBulletedText(control, needForStructure1Recommendation());
		
		setControl(control);
		validate();
	}
	private String learningStyle() {
		if (getLearningStyle(ls) == SEQ)
			return format(Messages.Summary1WizardPage_sequential_learner);
		else if (getLearningStyle(ls) == GLOB)
			return format(Messages.Summary1WizardPage_global_learner);
		else
			return format(Messages.Summary1WizardPage_in_between_learner);
	}
	private String learningStyleRecommendation() {
		if (getLearningStyle(ls) == SEQ)
			return format(Messages.Summary1WizardPage_Learning_style_guideline_FO);
		else if (getLearningStyle(ls) == GLOB)
			return format(Messages.Summary1WizardPage_Learning_style_guideline_AO);
		else
			return format(Messages.Summary1WizardPage_Learning_style_guideline_C);
	}
	private String fieldDependency() {
		if (getFieldDependency(fd) == FD)
			return format(Messages.Summary1WizardPage_field_dependent);
		else
			return format(Messages.Summary1WizardPage_field_independent);
	}
	private String fieldDependencyRecommendation() {
		if (getFieldDependency(fd) == FD)
			return format(Messages.Summary1WizardPage_Field_dependency_guideline_FD);
		else
			return format(Messages.Summary1WizardPage_Field_dependency_guideline_FID);
	}
	private String needForStructure1() {
		if (getNeedForStructure1(nfs1) == HNFS1)
			return format(Messages.Summary1WizardPage_high_desire);
		else
			return format(Messages.Summary1WizardPage_low_desire);
	}
	private String needForStructure1Recommendation() {
		if (getNeedForStructure1(nfs1) == HNFS1)
			return format(Messages.Summary1WizardPage_Need_for_structure_1_HNFS1);
		else
			return format(Messages.Summary1WizardPage_Need_for_structure1_LNFS1);
	}
}

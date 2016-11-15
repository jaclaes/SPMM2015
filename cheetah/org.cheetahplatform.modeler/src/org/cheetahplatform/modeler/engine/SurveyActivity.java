package org.cheetahplatform.modeler.engine;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.cheetahplatform.survey.wizard.SurveyWizard;
import org.cheetahplatform.survey.wizard.SurveyWizardDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

public class SurveyActivity extends AbstractExperimentsWorkflowActivity {

	public static final String TYPE_SURVEY = "SURVEY";

	protected List<SurveyAttribute> attributes;
	protected List<Attribute> collectedAttributes;

	private int nameLabelWidth;

	public SurveyActivity(List<SurveyAttribute> attributes) {
		this(TYPE_SURVEY, attributes);
		nameLabelWidth = 400;
	}

	public SurveyActivity(List<SurveyAttribute> attributes, int nameLabelWidth) {
		this(attributes);
		this.nameLabelWidth = nameLabelWidth;
	}

	protected SurveyActivity(String name, List<SurveyAttribute> attributes) {
		super(name);
		nameLabelWidth = 400;
		this.attributes = attributes;
	}

	@Override
	protected void doExecute() {
		SurveyWizard wizard = new SurveyWizard(attributes, nameLabelWidth);
		WizardDialog dialog = new SurveyWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		dialog.open();

		collectedAttributes = wizard.getCollectedAttributes();
	}

	@Override
	protected List<Attribute> getData() {
		List<Attribute> attributes = super.getData();
		attributes.addAll(collectedAttributes);

		return attributes;
	}

	@Override
	public Object getName() {
		return "Show Survey";
	}

}

package org.cheetahplatform.modeler.engine;

import java.util.List;

import org.cheetahplatform.survey.core.SurveyAttribute;
import org.cheetahplatform.survey.wizard.SurveyWizard;
import org.cheetahplatform.survey.wizard.SurveyWizardDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

public class UnderstandabilityCheckActivity extends SurveyActivity {

	public UnderstandabilityCheckActivity(List<SurveyAttribute> attributes) {
		super("UNDERSTANDABILITY_SURVEY", attributes);
	}

	@Override
	protected void doExecute() {
		SurveyWizard wizard = new SurveyWizard(attributes, 800);
		WizardDialog dialog = new SurveyWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		dialog.open();

		collectedAttributes = wizard.getCollectedAttributes();
	}

	@Override
	public Object getName() {
		return "Show Understandability Survey";
	}

}

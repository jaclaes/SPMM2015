package org.cheetahplatform.survey.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.survey.Activator;
import org.cheetahplatform.survey.Messages;
import org.cheetahplatform.survey.controller.AttributeControllerFactory;
import org.cheetahplatform.survey.controller.IAttributeController;
import org.cheetahplatform.survey.core.Progress;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.swtdesigner.ResourceManager;

/**
 * A page for the {@link SurveyWizard}.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         29.09.2009
 */
public class SurveyWizardPage extends WizardPage {
	private class UiListener implements Listener {
		private final SurveyAttribute surveyAttribute;

		public UiListener(SurveyAttribute surveyAttribute) {
			this.surveyAttribute = surveyAttribute;
		}

		@Override
		public void handleEvent(Event event) {
			validate();
			((SurveyWizard) getWizard()).measureTime(surveyAttribute);
		}
	}

	private List<IAttributeController> controllers;
	private final List<SurveyAttribute> attributes;
	private final int nameLabelWidth;

	/**
	 * Creates a new wizard page.
	 * 
	 * @param pageName
	 *            the name of the page
	 */
	protected SurveyWizardPage(String pageName, List<SurveyAttribute> attributes, int nameLabelWidth) {
		super(pageName);
		this.nameLabelWidth = nameLabelWidth;
		Assert.isNotNull(attributes);
		setDescription(Messages.SurveyWizardPage_0);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/accessories-text-editor.png")); //$NON-NLS-1$
		setTitle(Messages.SurveyWizardPage_1);
		this.attributes = attributes;
		controllers = new ArrayList<IAttributeController>();
	}

	/**
	 * Collects the attributes on this wizard page.
	 * 
	 * @return a list of attributes
	 */
	public List<Attribute> collectAttributes() {
		List<Attribute> collectedAttributes = new ArrayList<Attribute>();
		for (IAttributeController controller : controllers) {
			collectedAttributes.addAll(controller.collectInput());
		}
		return collectedAttributes;
	}

	@Override
	public void createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout());
		control.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));

		for (SurveyAttribute surveyAttribute : attributes) {
			IAttributeController controller = AttributeControllerFactory.createController(control, surveyAttribute, new UiListener(
					surveyAttribute), null, nameLabelWidth);
			controllers.add(controller);
		}
		setControl(control);
		validate();
	}

	public void focusOnFirstControl() {
		if (controllers.isEmpty())
			return;

		controllers.get(0).setFocus();
	}

	/**
	 * @return the controllers
	 */
	public List<IAttributeController> getControllers() {
		return Collections.unmodifiableList(controllers);
	}

	/**
	 * Updates the given {@link Progress} with the information from this wizard page.
	 * 
	 * @param progress
	 *            the progress to update
	 */
	public void updateProgress(Progress progress) {
		for (IAttributeController controller : controllers) {
			controller.updateProgress(progress);
		}
	}

	/**
	 * Validates the wizard page.
	 */
	private void validate() {
		((SurveyWizard) getWizard()).updateProgress();
		for (IAttributeController controller : controllers) {
			String error = controller.validate();
			if (error != null) {
				setErrorMessage(error);
				setPageComplete(false);
				return;
			}
		}

		setErrorMessage(null);
		setPageComplete(true);
	}
}

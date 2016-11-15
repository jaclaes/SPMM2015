package org.cheetahplatform.survey.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.survey.Messages;
import org.cheetahplatform.survey.controller.IAttributeController;
import org.cheetahplatform.survey.core.MessageSurveyAttribute;
import org.cheetahplatform.survey.core.Progress;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * A wizard for collecting values for a given set of attributes.
 *
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 *
 *         28.09.2009
 */
public class SurveyWizard extends Wizard {
	private static class DefaultSurveyValidator implements ISurveyValidator {

		@Override
		public boolean validate(SurveyWizard wizard) {
			return true;
		}

	}

	public static final String DURATION = "_duration"; //$NON-NLS-1$
	public static final String ATTRIBUTE_CHOICE_SEPARATION = "_"; //$NON-NLS-1$

	private static final int QUESTIONS_PER_PAGE = 7;

	private ListenerList progressListeners;
	protected final List<SurveyAttribute> attributes;
	private List<Attribute> collectedAttributes;
	private final int nameLabelWidth;
	private long lastAnsweredQuestionTimeStamp;
	protected Map<SurveyAttribute, Long> attributeToAnswerDurationInMilliseconds;
	private ISurveyValidator validator;

	/**
	 * Creates a new wizard.
	 *
	 * @param attributes
	 *            the {@link SurveyAttribute}s of the wizard
	 */
	public SurveyWizard(List<SurveyAttribute> attributes, int nameLabelWidth) {
		this(attributes, nameLabelWidth, new DefaultSurveyValidator());
	}

	/**
	 * Creates a new wizard.
	 *
	 * @param attributes
	 *            the {@link SurveyAttribute}s of the wizard
	 * @param validator
	 *            a validator for determining whether the survey can be finished
	 */
	public SurveyWizard(List<SurveyAttribute> attributes, int nameLabelWidth, ISurveyValidator validator) {
		Assert.isNotNull(attributes);

		this.nameLabelWidth = nameLabelWidth;
		this.validator = validator;
		progressListeners = new ListenerList();
		List<String> usedNames = new ArrayList<String>();
		for (SurveyAttribute surveyAttribute : attributes) {
			if (usedNames.contains(surveyAttribute.getName())) {
				throw new IllegalArgumentException("There are multiple items with name: '" + surveyAttribute.getName() + "' in the survey."); //$NON-NLS-1$ //$NON-NLS-2$
			}
			usedNames.add(surveyAttribute.getName());
		}

		this.attributes = attributes;
		this.lastAnsweredQuestionTimeStamp = System.currentTimeMillis();
		this.attributeToAnswerDurationInMilliseconds = new HashMap<SurveyAttribute, Long>();
		setWindowTitle(Messages.SurveyWizard_0);
	}

	@Override
	public void addPages() {
		int lowerBound = 0;
		int upperBound = 0;

		do {
			lowerBound = upperBound;
			upperBound = Math.min(attributes.size(), lowerBound + QUESTIONS_PER_PAGE);
			int i = upperBound - 1;
			while (i >= lowerBound && attributes.get(i) instanceof MessageSurveyAttribute
					&& ((MessageSurveyAttribute) attributes.get(i)).getMessage().equals("")) //$NON-NLS-1$
				i--;
			List<SurveyAttribute> attributesForPage = attributes.subList(lowerBound, i + 1);
			SurveyWizardPage page = new SurveyWizardPage(Messages.SurveyWizard_1, attributesForPage, nameLabelWidth);
			addPage(page);
		} while (upperBound < attributes.size());
	}

	/**
	 * Adds a new {@link IProgressListener}.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addProgressListener(IProgressListener listener) {
		progressListeners.add(listener);
	}

	public List<IAttributeController> getAttributeControllers() {
		List<IAttributeController> controllers = new ArrayList<IAttributeController>();
		for (IWizardPage page : getPages()) {
			controllers.addAll(((SurveyWizardPage) page).getControllers());
		}

		return controllers;
	}

	/**
	 * Returns the collectedAttributes.
	 *
	 * @return the collectedAttributes
	 */
	public List<Attribute> getCollectedAttributes() {
		return collectedAttributes;
	}

	/**
	 * Returns the {@link Progress} of the wizard.
	 *
	 * @return the {@link Progress}
	 */
	public Progress getProgress() {
		Progress progress = new Progress();
		for (IWizardPage iWizardPage : getPages()) {
			((SurveyWizardPage) iWizardPage).updateProgress(progress);
		}

		return progress;
	}

	public void measureTime(SurveyAttribute attribute) {
		long duration = System.currentTimeMillis() - lastAnsweredQuestionTimeStamp;
		lastAnsweredQuestionTimeStamp = System.currentTimeMillis();
		Long existingDuration = attributeToAnswerDurationInMilliseconds.get(attribute);

		if (existingDuration == null) {
			existingDuration = new Long(0);
		}

		existingDuration += duration;
		attributeToAnswerDurationInMilliseconds.put(attribute, existingDuration);
	}

	@Override
	public boolean performFinish() {
		if (!validator.validate(this)) {
			return false;
		}

		collectedAttributes = new ArrayList<Attribute>();
		for (IWizardPage page : getPages()) {
			collectedAttributes.addAll(((SurveyWizardPage) page).collectAttributes());
		}

		// add the measured times
		for (SurveyAttribute attribute : attributes) {
			Long duration = attributeToAnswerDurationInMilliseconds.get(attribute);
			if (duration == null || duration == 0) {
				duration = new Long(-1);
			}

			Attribute durationAttribute = new Attribute(attribute.getName() + DURATION, String.valueOf(duration));
			collectedAttributes.add(durationAttribute);
		}

		return true;
	}

	/**
	 * Removes the given progress listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void removeProgressListener(IProgressListener listener) {
		progressListeners.remove(listener);
	}

	/**
	 * This function informs all {@link IProgressListener}s about changes made.
	 */
	public void updateProgress() {
		Object[] listeners = progressListeners.getListeners();
		for (Object listener : listeners) {
			((IProgressListener) listener).progressChanged();
		}
	}

}

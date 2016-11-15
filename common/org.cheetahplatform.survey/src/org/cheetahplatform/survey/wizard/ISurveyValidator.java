package org.cheetahplatform.survey.wizard;

public interface ISurveyValidator {
	/**
	 * Determine whether the survey can be finished now.
	 * 
	 * @param wizard
	 *            the wizard
	 * @return <code>true</code> if the survey can be finished now, <code>false</code> otherwise
	 */
	boolean validate(SurveyWizard wizard);
}

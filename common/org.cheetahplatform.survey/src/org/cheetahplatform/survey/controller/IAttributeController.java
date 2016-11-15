package org.cheetahplatform.survey.controller;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.survey.core.Progress;
import org.cheetahplatform.survey.core.SurveyAttribute;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         30.09.2009
 */
public interface IAttributeController {

	/**
	 * Retrieves the content of the ui and builds an attribute from it.
	 * 
	 * @return the attribute
	 */
	List<Attribute> collectInput();

	/**
	 * Returns the attribute.
	 * 
	 * @return the attribute
	 */
	SurveyAttribute getAttribute();

	/**
	 * 
	 */
	void setFocus();

	/**
	 * Set the input in oder to for example continue a survey.
	 * 
	 * @param input
	 */
	void setInput(Attribute input);

	/**
	 * @param progress
	 */
	void updateProgress(Progress progress);

	/**
	 * Validates the control.
	 * 
	 * @return <code>null</code> if the input is valid, an error message otherwise
	 */
	String validate();

}
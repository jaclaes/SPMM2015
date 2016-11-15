package org.cheetahplatform.survey.controller;

import org.cheetahplatform.survey.core.CheckBoxInputAttribute;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.MessageSurveyAttribute;
import org.cheetahplatform.survey.core.RadioButtonInputAttribute;
import org.cheetahplatform.survey.core.RadioLikertInputAttribute;
import org.cheetahplatform.survey.core.SectionHeaderAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.cheetahplatform.survey.core.TextInputAttribute;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

/**
 * A factory creating the {@link IAttributeController}s for the given {@link SurveyAttribute}s.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         23.09.2009
 */
public class AttributeControllerFactory {

	/**
	 * Creates a new {@link IAttributeController}.
	 * 
	 * @param composite
	 *            the composite to draw on.
	 * @param attribute
	 *            the {@link SurveyAttribute}
	 * @param listener
	 *            a listener to be informed on changes made by the user
	 * @param oldController
	 *            if the attributes was controlled by another controller previously the old controller
	 * @return the {@link IAttributeController}
	 */
	public static IAttributeController createController(Composite composite, SurveyAttribute attribute, Listener listener,
			IAttributeController oldController, int nameLabelWidth) {

		if (attribute instanceof TextInputAttribute) {
			return new StringAttributeController(composite, (TextInputAttribute) attribute, listener, oldController, nameLabelWidth);
		}
		if (attribute instanceof ComboInputAttribute) {
			return new ComboAttributeController(composite, (ComboInputAttribute) attribute, listener, oldController, nameLabelWidth);
		}
		if (attribute instanceof RadioButtonInputAttribute) {
			return new RadioButtonAttributeController(composite, (RadioButtonInputAttribute) attribute, listener, oldController);
		}
		if (attribute instanceof CheckBoxInputAttribute) {
			return new CheckBoxAttributeController(composite, (CheckBoxInputAttribute) attribute, listener, oldController);
		}
		if (attribute instanceof SectionHeaderAttribute) {
			return new SectionHeaderController(composite, (SectionHeaderAttribute) attribute, nameLabelWidth);
		}
		if (attribute instanceof MessageSurveyAttribute) {
			return new MessageAttributeController(composite, (MessageSurveyAttribute) attribute, nameLabelWidth);
		}
		if (attribute instanceof RadioLikertInputAttribute) {
			return new RadioLikertAttributeController(composite, (RadioLikertInputAttribute) attribute, nameLabelWidth, oldController,
					listener);
		}

		throw new IllegalArgumentException("There is no UI support for attribute type: " + attribute); //$NON-NLS-1$
	}
}

/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.survey.controller;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class AttributeSupport {
	private class UiListener implements Listener {
		@Override
		public void handleEvent(Event event) {
			validate();
		}
	}

	private final List<IAttributeController> controllers;
	private final IStatusCallback callback;

	private final Composite composite;
	private final int nameLabelWidth;

	public AttributeSupport(Composite composite, List<SurveyAttribute> attributes, IStatusCallback callback, int nameLabelWidth) {
		this.composite = composite;
		this.callback = callback;
		this.nameLabelWidth = nameLabelWidth;
		this.controllers = new ArrayList<IAttributeController>();

		createControllers(attributes);
		validate();
	}

	public List<Attribute> collectAttributes() {
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (IAttributeController controller : controllers) {
			attributes.addAll(controller.collectInput());
		}

		return attributes;
	}

	/**
	 * Create the controls for visualizing the attributes.
	 * 
	 * @param attributes
	 *            the attributes for which controls will be created
	 */
	private void createControllers(List<SurveyAttribute> attributes) {
		for (SurveyAttribute attribute : attributes) {
			IAttributeController oldController = findController(attribute);
			IAttributeController controller = AttributeControllerFactory.createController(composite, attribute, new UiListener(),
					oldController, nameLabelWidth);

			controllers.add(controller);
		}

		composite.setVisible(!attributes.isEmpty());
		((GridData) composite.getLayoutData()).exclude = attributes.isEmpty();
	}

	/**
	 * Tries to find the controller for given attribute.
	 * 
	 * @param attribute
	 *            the attribute
	 * @return the associated controller, <code>null</code> if there is none
	 */
	private IAttributeController findController(SurveyAttribute attribute) {
		for (IAttributeController controller : controllers) {
			if (controller.getAttribute().equals(attribute)) {
				return controller;
			}
		}

		return null;
	}

	/**
	 * Recreates the controls for the passed attributes.
	 * 
	 * @param attributes
	 *            <GameAttribute> attributes the attributes
	 */
	public void recreateControls(List<SurveyAttribute> attributes) {
		List<IAttributeController> oldControllers = new ArrayList<IAttributeController>(controllers);
		Control[] children = composite.getChildren();

		createControllers(attributes);

		for (Control child : children) {
			child.dispose();
		}

		controllers.removeAll(oldControllers);

		validate();
	}

	/**
	 * Validates the input.
	 */
	protected void validate() {
		for (IAttributeController controller : controllers) {
			String error = controller.validate();
			if (error != null) {
				callback.update(error);
				return;
			}
		}

		callback.update(null);
	}
}

package org.cheetahplatform.survey;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.csv.AbstractProcessInformation;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         16.11.2009
 */
public abstract class SurveyProcessInformation extends AbstractProcessInformation {

	public static final String DURATION_ATTRIBUTE = "_duration"; //$NON-NLS-1$
	private static final String ATTRIBUTE_SEPARATOR = "_"; //$NON-NLS-1$

	public SurveyProcessInformation(List<Attribute> attributes, boolean aggregateChoiceValues) {
		super(attributes, aggregateChoiceValues);
	}

	protected String extractAttributeName(Attribute attribute) {
		String name = attribute.getName();
		if (name.contains(ATTRIBUTE_SEPARATOR)) {
			return name.substring(0, name.lastIndexOf(ATTRIBUTE_SEPARATOR));
		}
		return name;
	}

	private String extractAttributeValue(Attribute attribute) {
		String name = attribute.getName();
		if (name.contains(ATTRIBUTE_SEPARATOR)) {
			return name.substring(name.lastIndexOf(ATTRIBUTE_SEPARATOR) + 1);
		}
		return attribute.getContent();
	}

	@Override
	protected void filterAttributes(List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			if (!showAttribute(attribute)) {
				continue;
			}

			if (isDurationAttribute(attribute)) {
				this.attributes.add(attribute);
				continue;
			}

			if (isChoiceAttribute(attribute)) {
				if (aggregateChoiceValues) {
					if (Boolean.parseBoolean(attribute.getContent())) {
						Attribute toAdd = new Attribute(extractAttributeName(attribute), extractAttributeValue(attribute));
						this.attributes.add(toAdd);
						continue;
					}
				} else {
					this.attributes.add(attribute);
				}
			} else {
				this.attributes.add(attribute);
			}
		}
	}

	private boolean isChoiceAttribute(Attribute attribute) {
		return attribute.getName().contains(ATTRIBUTE_SEPARATOR)
				&& !attribute.getName().equals("workflow_configuration_id") && !attribute.getName().startsWith("display_resolution") && !attribute.getName().endsWith("_switches") && !attribute.getName().equals("assigned_id"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	protected boolean isDurationAttribute(Attribute attribute) {
		return attribute.getName().contains(DURATION_ATTRIBUTE);
	}

	protected abstract boolean showAttribute(Attribute attribute);
}
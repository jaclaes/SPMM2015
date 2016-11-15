package org.cheetahplatform.common.logging.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.12.2009
 */
public abstract class AbstractProcessInformation implements IProcessInformation {
	private List<IValueReplacement> valueReplacements;
	protected final List<Attribute> attributes;
	protected final boolean aggregateChoiceValues;

	public AbstractProcessInformation(List<Attribute> attributes, boolean aggregateChoiceValues2) {
		this.aggregateChoiceValues = aggregateChoiceValues2;
		this.attributes = new ArrayList<Attribute>();
		this.valueReplacements = new ArrayList<IValueReplacement>();

		filterAttributes(attributes);
	}

	/**
	 * @param toAdd
	 */
	protected void addAll(List<Attribute> toAdd) {
		attributes.addAll(toAdd);
	}

	public void addAttribute(Attribute attribute) {
		List<Attribute> attributes = Arrays.asList(new Attribute[] { attribute });
		addAttributes(attributes);
	}

	public void addAttribute(int index, Attribute attribute) {
		attributes.add(index, attribute);
	}

	@Override
	public void addAttributes(List<Attribute> attributes) {
		filterAttributes(attributes);
	}

	public void addValueReplacement(IValueReplacement valueReplacement) {
		this.valueReplacements.add(valueReplacement);
	}

	/**
	 * Filters the attributes to be added to the {@link IProcessInformation} object. <br>
	 * The default implementation simply adds all attributes. May be overridden by subclasses.
	 * 
	 * @param attributes
	 *            the attributes
	 */
	protected void filterAttributes(List<Attribute> attributes) {
		this.attributes.addAll(attributes);
	}

	@Override
	public List<Attribute> getAttributes() {
		return attributes;
	}

	@Override
	public String getAttributeValue(String attributeName) {
		String returnValue = null;

		for (Attribute attribute : attributes) {
			if (attribute.getName().trim().equals(attributeName.trim())) {
				for (IValueReplacement replacement : valueReplacements) {
					if (replacement.applies(attribute)) {
						return replacement.getReplacedContent(attribute);
					}
				}
				returnValue = attribute.getContent();
			}
		}

		if (returnValue == null) {
			return getEmptyCellValue();
		}

		return returnValue;
	}

	/**
	 * Returns the value which should be used for cells where attribute could be found.
	 * 
	 * @return the value for cells without an associated attribute.
	 */
	protected abstract String getEmptyCellValue();

	@Override
	public List<IValueReplacement> getValueReplacements() {
		return Collections.unmodifiableList(valueReplacements);
	}

}
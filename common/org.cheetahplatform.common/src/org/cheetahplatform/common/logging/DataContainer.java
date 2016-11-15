/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.common.logging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.AssertionFailedException;

public class DataContainer {
	private final Map<String, String> attributes;

	public DataContainer() {
		// need to use a linked hash map, because the ordering of the data may
		// be important
		attributes = new LinkedHashMap<String, String>();
	}

	/**
	 * Adds all attributes from the given Collection to the {@link DataContainer}. Existing data may be replaced.
	 * 
	 * @param attributes
	 */
	public void addAttributes(Collection<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			this.attributes.put(attribute.getName(), attribute.getContent());
		}
	}

	/**
	 * Add the given list of attributes, but does not overwrite any attributes with the same name.
	 * 
	 * @param attributesToAdd
	 *            the attributes
	 */
	public void addAttributesNonOverwrite(List<Attribute> attributesToAdd) {
		for (Attribute toAdd : attributesToAdd) {
			if (!isAttributeDefined(toAdd.getName())) {
				setAttribute(toAdd);
			}
		}
	}

	/**
	 * Count the amount of attributes.
	 * 
	 * @return the amount of attributes
	 */
	public int attributeCount() {
		return attributes.size();
	}

	/**
	 * Returns the attribute value for the given name.
	 * 
	 * @param name
	 *            the name
	 * @return the value
	 * @throws AssertionFailedException
	 *             if the attribute is not known
	 */
	public String getAttribute(String name) {
		Assert.isTrue(attributes.containsKey(name));
		return attributes.get(name);
	}

	/**
	 * Returns all attribute names of this container.
	 * 
	 * @return all names
	 */
	public Set<String> getAttributeNames() {
		return new HashSet<String>(attributes.keySet());
	}

	public Attribute getAttributeObject(String name) {
		if (!attributes.containsKey(name)) {
			Assert.fail("There is no attribute with name " + name);
		}

		String value = attributes.get(name);
		return new Attribute(name, value);
	}

	/**
	 * Returns the attributes. Should be called only if no other method applies.
	 * 
	 * @return the attribute
	 */
	public List<Attribute> getAttributes() {
		List<Attribute> copy = new ArrayList<Attribute>();
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			copy.add(new Attribute(entry.getKey(), entry.getValue()));
		}

		return copy;
	}

	/**
	 * Returns the value for the given attribute name.
	 * 
	 * @param name
	 *            the name
	 * @return the associated attribute name, an empty string if not found
	 */
	public String getAttributeSafely(String name) {
		String value = attributes.get(name);
		if (value == null) {
			return ""; //$NON-NLS-1$
		}

		return value;
	}

	public boolean getBooleanAttribute(String name) {
		return Boolean.valueOf(getAttribute(name));
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws AssertionFailedException
	 * @see {@link #getIntegerAttribute(String)}
	 */
	public double getDoubleAttribute(String name) throws AssertionFailedException {
		String content = attributes.get(name);
		Assert.isNotNull(content, "Could not find attribute " + name + "."); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			return Double.parseDouble(content);
		} catch (NumberFormatException e) {
			throw new AssertionFailedException("Attribute is not of datatype double: " + content); //$NON-NLS-1$
		}
	}

	/**
	 * Returns the int value of the attribute with the given name.
	 * 
	 * @param name
	 *            the name of the attribute
	 * @return the int value
	 * @throws AssertionFailedException
	 *             if there is no attribute with the given name or the value is no valid int value
	 */
	public int getIntegerAttribute(String name) throws AssertionFailedException {
		String content = attributes.get(name);
		Assert.isNotNull(content, "Could not find attribute " + name + "."); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			return Integer.parseInt(content);
		} catch (NumberFormatException e) {
			throw new AssertionFailedException("Attribute is not of datatype int: " + content); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws AssertionFailedException
	 * @see {@link #getIntegerAttribute(String)}
	 */
	public long getLongAttribute(String name) throws AssertionFailedException {
		String content = attributes.get(name);
		Assert.isNotNull(content, "Could not find attribute " + name + "."); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			return Long.parseLong(content);
		} catch (NumberFormatException e) {
			throw new AssertionFailedException("Attribute is not of datatype long: " + content); //$NON-NLS-1$
		}
	}

	/**
	 * Checks if the data section contains an attribute with the given name.
	 * 
	 * @param name
	 *            the name.
	 * @return <code>true</code> if there is an attribute with the given name, <code>false</code> if not.
	 */
	public boolean isAttributeDefined(String name) {
		return attributes.containsKey(name);
	}

	public void setAttribute(Attribute toAdd) {
		setAttribute(toAdd.getName(), toAdd.getContent());
	}

	public void setAttribute(String name, boolean content) {
		attributes.put(name, String.valueOf(content));
	}

	public void setAttribute(String name, long content) {
		attributes.put(name, String.valueOf(content));
	}

	/**
	 * Adds the given data, if there is already a data with the given name, it is replaced.
	 * 
	 * @param name
	 *            the name of the data.
	 * @param content
	 *            the data's content.
	 */
	public void setAttribute(String name, String content) {
		attributes.put(name, content);
	}

	/**
	 * Adds all data from the other {@link DataContainer}. Existing data may be replaced.
	 * 
	 * @param other
	 */
	public void setAttributes(DataContainer other) {
		for (String key : other.attributes.keySet())
			attributes.put(key, other.attributes.get(key));
	}

	@Override
	public String toString() {
		return attributes.toString();
	}
}

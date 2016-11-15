package org.cheetahplatform.common.logging.csv;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         16.11.2009
 */
public interface IProcessInformation {

	void addAttributes(List<Attribute> attributes);

	List<Attribute> getAttributes();

	String getAttributeValue(String attributeName);

	List<IValueReplacement> getValueReplacements();
}
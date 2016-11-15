package org.cheetahplatform.survey.core;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         23.09.2009
 */
public class MultiLineStringInputAttribute extends StringInputAttribute {

	/**
	 * The no args constructor is needed for automatic XStream deserialization
	 */
	public MultiLineStringInputAttribute() {
		this("", false); //$NON-NLS-1$
	}

	public MultiLineStringInputAttribute(String name, boolean mandatory) {
		super(name, mandatory);
	}

	@Override
	public boolean isMultiLine() {
		return true;
	}
}

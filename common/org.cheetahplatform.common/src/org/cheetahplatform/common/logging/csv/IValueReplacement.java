package org.cheetahplatform.common.logging.csv;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.12.2009
 */
public interface IValueReplacement {

	boolean applies(Attribute attribute);

	String getReplacedContent(Attribute attribute);

	List<LegendEntry> getReplacementLegend();
}

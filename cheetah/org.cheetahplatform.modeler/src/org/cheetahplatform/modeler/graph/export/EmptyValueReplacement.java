package org.cheetahplatform.modeler.graph.export;

import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.csv.IValueReplacement;
import org.cheetahplatform.common.logging.csv.LegendEntry;

public class EmptyValueReplacement implements IValueReplacement {

	@Override
	public boolean applies(Attribute attribute) {
		if (attribute.getContent() == null) {
			return true;
		}

		return attribute.getContent().trim().isEmpty();
	}

	@Override
	public String getReplacedContent(Attribute attribute) {
		return String.valueOf(0);
	}

	@Override
	public List<LegendEntry> getReplacementLegend() {
		return Collections.emptyList();
	}
}

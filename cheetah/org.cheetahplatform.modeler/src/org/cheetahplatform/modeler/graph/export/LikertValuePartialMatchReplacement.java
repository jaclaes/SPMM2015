package org.cheetahplatform.modeler.graph.export;

import org.cheetahplatform.common.logging.Attribute;

public class LikertValuePartialMatchReplacement extends LikertValueReplacement {

	public LikertValuePartialMatchReplacement(String attributeName) {
		super(attributeName);
	}

	@Override
	public boolean applies(Attribute attribute) {
		for (String name : attributeNames) {
			if (attribute.getName().startsWith(name) && !attribute.getName().endsWith("duration")) {
				return true;
			}
		}

		return false;
	}
}

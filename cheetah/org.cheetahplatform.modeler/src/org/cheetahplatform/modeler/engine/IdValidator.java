package org.cheetahplatform.modeler.engine;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.common.Assert;

public class IdValidator {
	private Set<Long> ids;

	public IdValidator() {
		ids = new HashSet<Long>();
	}

	public long getLowestId() {
		long lowest = Long.MAX_VALUE;
		for (Long value : ids) {
			if (value < lowest) {
				lowest = value;
			}
		}

		return lowest;
	}

	public void validate(long id) {
		Assert.isTrue(!ids.contains(id), "Id in use: " + id);
		ids.add(id);
	}
}

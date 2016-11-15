package org.cheetahplatform.core.common;

import java.text.Collator;
import java.util.Comparator;

import org.cheetahplatform.common.INamed;

public class NamedComparator implements Comparator<INamed> {

	public int compare(INamed named1, INamed named2) {
		return Collator.getInstance().compare(named1.getName(), named2.getName());
	}

}

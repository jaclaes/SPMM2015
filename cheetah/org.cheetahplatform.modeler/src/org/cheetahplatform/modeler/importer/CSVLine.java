package org.cheetahplatform.modeler.importer;

import java.util.ArrayList;
import java.util.List;

public class CSVLine {
	private List<String> content;

	public CSVLine() {
		content = new ArrayList<String>();
	}

	public void add(String toAdd) {
		content.add(toAdd);
	}

	public String get(int columnNumber) {
		return content.get(columnNumber);
	}

	public int size() {
		return content.size();
	}

	@Override
	public String toString() {
		return content.toString();
	}
}

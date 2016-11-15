package org.cheetahplatform.modeler.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DemoCategory extends AbstractDemonstratable {
	private List<IDemonstratable> elements;

	public DemoCategory(String name, String description) {
		super(name, description);
		elements = new ArrayList<IDemonstratable>();
	}

	public void add(IDemonstratable demonstratable) {
		elements.add(demonstratable);
	}

	@Override
	public List<IDemonstratable> getChildren() {
		return Collections.unmodifiableList(elements);
	}
}
package org.cheetahplatform.conformance.core;

public class ConformanceActivity {
	private String name;

	public ConformanceActivity(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

}

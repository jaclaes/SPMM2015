package org.cheetahplatform.common;

public class LikertScaleEntry {
	private String name;
	private int value;

	public LikertScaleEntry(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

}

package org.cheetahplatform.literatemodeling;

public class LiterateInitialValues {
	private String name, description, text;

	public LiterateInitialValues(String name, String descrioption, String text) {
		this.name = name;
		this.description = descrioption;
		this.text = text;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setText(String text) {
		this.text = text;
	}

}

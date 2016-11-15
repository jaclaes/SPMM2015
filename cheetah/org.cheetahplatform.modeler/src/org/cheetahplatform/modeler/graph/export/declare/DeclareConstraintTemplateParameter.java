package org.cheetahplatform.modeler.graph.export.declare;

public class DeclareConstraintTemplateParameter {
	private String branchable;
	private String id;
	private String name;
	private String style;
	private String fillBegin;
	private String fillMiddle;
	private String fillEnd;
	private String styleBegin;
	private String styleMiddle;
	private String styleEnd;

	public DeclareConstraintTemplateParameter(String branchable, String id, String name, String style, String fillBegin, String fillMiddle,
			String fillEnd, String styleBegin, String styleMiddle, String styleEnd) {
		this.branchable = branchable;
		this.id = id;
		this.name = name;
		this.style = style;
		this.fillBegin = fillBegin;
		this.fillMiddle = fillMiddle;
		this.fillEnd = fillEnd;
		this.styleBegin = styleBegin;
		this.styleMiddle = styleMiddle;
		this.styleEnd = styleEnd;
	}

	public String getBranchable() {
		return branchable;
	}

	public final String getFillBegin() {
		return fillBegin;
	}

	public final String getFillEnd() {
		return fillEnd;
	}

	public final String getFillMiddle() {
		return fillMiddle;
	}

	public final String getId() {
		return id;
	}

	public final String getName() {
		return name;
	}

	public final String getStyle() {
		return style;
	}

	public final String getStyleBegin() {
		return styleBegin;
	}

	public final String getStyleEnd() {
		return styleEnd;
	}

	public final String getStyleMiddle() {
		return styleMiddle;
	}
}

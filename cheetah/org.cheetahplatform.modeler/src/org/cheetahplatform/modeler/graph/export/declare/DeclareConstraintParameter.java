package org.cheetahplatform.modeler.graph.export.declare;

public class DeclareConstraintParameter {
	private String templateparameter;

	private String branch;

	public DeclareConstraintParameter(String templateparameter, String branch) {
		this.templateparameter = templateparameter;
		this.branch = branch;
	}

	public String getBranch() {
		return branch;
	}

	public String getTemplateparameter() {
		return templateparameter;
	}
}

package org.cheetahplatform.modeler.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SyntaxCheckResponse {
	private String errorMessage;
	private List<ISyntaxError> syntaxErrors;

	public SyntaxCheckResponse() {
		syntaxErrors = new ArrayList<ISyntaxError>();
	}

	public void addSyntaxError(ISyntaxError error) {
		syntaxErrors.add(error);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public List<ISyntaxError> getSyntaxErrors() {
		return Collections.unmodifiableList(syntaxErrors);
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}

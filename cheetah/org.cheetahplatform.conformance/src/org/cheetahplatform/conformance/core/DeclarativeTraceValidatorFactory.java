package org.cheetahplatform.conformance.core;

import java.io.InputStream;

import org.w3c.dom.Node;

public class DeclarativeTraceValidatorFactory extends AbstractTraceValidatorFactory {

	@Override
	public ITraceValidator createValidator(String modelName, InputStream model) throws Exception {
		return new DeclarativeTraceValidator(modelName, model);
	}

	@Override
	protected boolean doesUnderstand(Node root) {
		return root.getNodeName().equals("model");
	}

}

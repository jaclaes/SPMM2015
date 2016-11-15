package org.cheetahplatform.conformance.core;

import java.io.InputStream;

import org.w3c.dom.Node;

public class BPMNTraceValidatorFactory extends AbstractTraceValidatorFactory {

	@Override
	public ITraceValidator createValidator(String modelName, InputStream model) throws Exception {
		return new BPMNTraceValidator(modelName, model);
	}

	@Override
	protected boolean doesUnderstand(Node root) {
		return root.getNodeName().equals("graphs");
	}

}

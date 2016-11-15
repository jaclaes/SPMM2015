package org.cheetahplatform.conformance.core;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class AbstractTraceValidatorFactory implements ITraceValidatorFactory {

	protected abstract boolean doesUnderstand(Node root);

	@Override
	public boolean understands(InputStream model) throws IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);

		try {
			Document document = factory.newDocumentBuilder().parse(model);
			return doesUnderstand(document.getFirstChild());
		} catch (Exception e) {
			return false;
		}
	}
}

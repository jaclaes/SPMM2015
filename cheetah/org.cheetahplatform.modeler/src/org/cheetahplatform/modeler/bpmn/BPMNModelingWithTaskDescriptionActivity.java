package org.cheetahplatform.modeler.bpmn;

import java.io.InputStream;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.AbstractExperimentalGraphEditor;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.model.Graph;

public class BPMNModelingWithTaskDescriptionActivity extends BPMNModelingActivity {
	String text;

	public BPMNModelingWithTaskDescriptionActivity(Graph initialGraph, Process process, String text) {
		super(initialGraph, process);
		Assert.isNotNull(text);
		this.text = text;

	}

	public BPMNModelingWithTaskDescriptionActivity(InputStream stream, Graph graph, Process process, String text) throws Exception {
		super(stream, graph, process);
		this.text = text;
	}

	@Override
	protected AbstractExperimentalGraphEditor openEditor(List<Attribute> attributes) {
		return (AbstractExperimentalGraphEditor) EditorRegistry
				.openEditor(graphEditorId, getInitialGraph(), attributes, getProcess(), text);
	}
}

package org.cheetahplatform.modeler.changepattern;

import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.AbstractExperimentalGraphEditor;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.model.Graph;

public class ChangePatternModelingWithTaskDescriptionActivity extends ChangePatternModelingActivity {

	private final String text;

	public ChangePatternModelingWithTaskDescriptionActivity(Process process, Graph initialGraph, String text) {
		super(process, initialGraph);
		Assert.isNotNull(text);
		this.text = text;
	}

	public ChangePatternModelingWithTaskDescriptionActivity(Process process, String path, String text) {
		super(process, path);
		Assert.isNotNull(text);
		this.text = text;
	}

	@Override
	protected AbstractExperimentalGraphEditor openEditor(List<Attribute> attributes) {
		return (AbstractExperimentalGraphEditor) EditorRegistry
				.openEditor(graphEditorId, getInitialGraph(), attributes, getProcess(), text);
	}
}

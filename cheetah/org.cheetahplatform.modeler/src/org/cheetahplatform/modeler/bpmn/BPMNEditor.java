package org.cheetahplatform.modeler.bpmn;

import org.cheetahplatform.modeler.AbstractExperimentalGraphEditor;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.dialog.TaskDescriptionComposite;
import org.cheetahplatform.modeler.graph.GraphEditorInput;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class BPMNEditor extends AbstractExperimentalGraphEditor {

	public static final String ID = "org.cheetahplatform.modeler.BPMNEditor";

	public BPMNEditor() {
		super(EditorRegistry.BPMN);
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_TASK_DESCRIPTION)) {
			((GridLayout) parent.getLayout()).numColumns = 1;

			GraphEditorInput editorInput2 = (GraphEditorInput) getEditorInput();

			TaskDescriptionComposite taskDescriptionComposite = new TaskDescriptionComposite(parent, SWT.NONE);
			taskDescriptionComposite.getDescriptionViewer().setDocument(new Document(editorInput2.getTaskDescription()));
		}

		super.createPartControl(parent);
	}
}

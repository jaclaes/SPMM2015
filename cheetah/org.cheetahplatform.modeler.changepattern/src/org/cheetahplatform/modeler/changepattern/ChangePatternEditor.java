package org.cheetahplatform.modeler.changepattern;

import org.cheetahplatform.modeler.AbstractExperimentalGraphEditor;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.dialog.TaskDescriptionComposite;
import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.graph.GraphEditorInput;
import org.cheetahplatform.modeler.graph.IGraphicalGraphViewerAdvisor;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         15.06.2010
 */
public class ChangePatternEditor extends AbstractExperimentalGraphEditor {
	public static final String ID = "org.cheetahplatform.modeler.ChangePatternEditor";
	private ChangePatternDialog dialog;

	public ChangePatternEditor() {
		super(EditorRegistry.CHANGE_PATTERN);
	}

	@Override
	protected IGraphicalGraphViewerAdvisor createGraphAdvisor() {
		GraphEditorInput editorInput = (GraphEditorInput) getEditorInput();
		Assert.isTrue(editorInput.hasGraph());
		return new ChangePatternEditorViewerAdvisor(createNodeDescriptors(), createEdgeDescriptors(), editorInput.getGraph());
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

	@Override
	public void dispose() {
		if (dialog != null) {
			dialog.close();
		}
		dialog = null;

		super.dispose();
	}

	@Override
	protected void initializeViewer() {
		GraphEditDomain editDomain = (GraphEditDomain) viewer.getViewer().getEditDomain();
		editDomain.unregisterAction(SWT.DEL);
		super.initializeViewer();

		openChangePatternDialog();
	}

	private void openChangePatternDialog() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		dialog = new ChangePatternDialog(shell, viewer);
		dialog.open();
	}

	@Override
	public void setEditable(boolean editable) {
		super.setEditable(editable);

		if (!editable) {
			if (dialog != null) {
				dialog.close();
				dialog = null;
			}
		} else {
			if (dialog == null) {
				openChangePatternDialog();
			}
		}
	}
}

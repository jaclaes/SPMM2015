package org.cheetahplatform.modeler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.dialog.StartModelingDialog;
import org.cheetahplatform.modeler.graph.GraphEditor;
import org.cheetahplatform.modeler.graph.GraphEditorInput;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.Viewport;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISaveablePart2;

public abstract class AbstractExperimentalGraphEditor extends GraphEditor implements ISaveablePart2 {

	ExperimentalGraphEditorModel model;
	private ReplayModel replayModel;

	public AbstractExperimentalGraphEditor(String graphEditorId) {
		this.model = new ExperimentalGraphEditorModel(graphEditorId);
	}

	@Override
	protected List<IEdgeDescriptor> createEdgeDescriptors() {
		return EditorRegistry.getEdgeDescriptors(model.getEditorId());
	}

	@Override
	protected List<INodeDescriptor> createNodeDescriptors() {
		return EditorRegistry.getNodeDescriptors(model.getEditorId());
	}

	@Override
	public void dispose() {
		super.dispose();

		model.dispose();
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.equals(ReplayModel.class)) {
			return replayModel;
		}
		if (adapter.equals(ScrollingGraphicalViewer.class)) {
			return viewer.getViewer();
		}
		if (adapter.equals(Graph.class)) {
			return viewer.getGraph();
		}
		if (adapter.equals(GraphicalGraphViewerWithFlyoutPalette.class)) {
			return viewer;
		}

		return super.getAdapter(adapter);
	}

	public ExperimentalGraphEditorModel getModel() {
		return model;
	}

	@Override
	protected void initializeViewer() {
		super.initializeViewer();

		ProcessInstance instance = new ProcessInstance();
		GraphEditorInput editorInput = (GraphEditorInput) getEditorInput();
		instance.addAttributes(editorInput.getAttributes());
		viewer.getGraph().addLogListener(model);

		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_START_MODELING_DIALOG)) {
			StartModelingDialog dialog = new StartModelingDialog(getSite().getShell(), model.getProcessInstanceId());
			dialog.open();
		}

		model.logNewProcessInstance(editorInput.getProcess(), instance);
		setPartName(getPartName() + " Id: " + instance.getId()); //$NON-NLS-1$
		firePropertyChange(IEditorPart.PROP_DIRTY);

		Viewport viewport = ((FigureCanvas) viewer.getViewer().getControl()).getViewport();
		viewport.getHorizontalRangeModel().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				model.logScroll(AbstractGraphCommand.HSCROLL, (RangeModel) event.getSource());
			}
		});

		viewport.getVerticalRangeModel().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				model.logScroll(AbstractGraphCommand.VSCROLL, (RangeModel) event.getSource());
			}
		});
	}

	@Override
	public boolean isDirty() {
		return true;
	}

	@Override
	public int promptToSaveOnClose() {
		if (!CheetahPlatformConfigurator.getBoolean(IConfiguration.ASK_WHEN_CLOSING_MODELING_EDITOR)) {
			return YES;
		}

		Shell shell = getSite().getShell();
		if (!MessageDialog.openQuestion(shell, Messages.AbstractExperimentalGraphEditor_1, Messages.AbstractExperimentalGraphEditor_2)) {
			return CANCEL;
		}

		return YES;
	}

}

package org.cheetahplatform.client.ui;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN_ACTIVITY;
import static org.cheetahplatform.modeler.EditorRegistry.BPMN_END_EVENT;
import static org.cheetahplatform.modeler.EditorRegistry.BPMN_SEQUENCE_FLOW;
import static org.cheetahplatform.modeler.EditorRegistry.BPMN_START_EVENT;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.client.model.LateModelingDialogModel;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.shared.ActivityHandle;
import org.cheetahplatform.shared.GraphHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class LateModelingDialog extends LocationPersistentTitleAreaDialog {

	private class GraphLogListener implements ILogListener {

		public void log(AuditTrailEntry entry) {
			validate();
		}

	}

	private LateModelingDialogModel model;
	private LateModelingDialogComposite composite;

	public LateModelingDialog(Shell parentShell, List<ActivityHandle> activities) {
		super(parentShell);

		model = new LateModelingDialogModel(activities);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new LateModelingDialogComposite(realParent, SWT.NONE);

		getShell().setText("Late Modeling");
		setTitle("Late Modeling Activity");
		setMessage("Use the editor below to fill the content of the late modeling box.");

		initialize();

		return realParent;
	}

	@Override
	protected String doValidate() {
		return model.validate();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 600);
	}

	public GraphHandle getModeledProcessSchema() {
		return model.getProcessSchema();
	}

	private void initialize() {
		Graph graph = model.computeGraph();
		List<INodeDescriptor> nodeDescriptors = new ArrayList<INodeDescriptor>();
		nodeDescriptors.add((INodeDescriptor) graph.getDescriptor(BPMN_START_EVENT));
		nodeDescriptors.add((INodeDescriptor) graph.getDescriptor(BPMN_END_EVENT));
		nodeDescriptors.add((INodeDescriptor) graph.getDescriptor(BPMN_ACTIVITY));

		List<IEdgeDescriptor> edgeDescriptors = new ArrayList<IEdgeDescriptor>();
		edgeDescriptors.add((IEdgeDescriptor) graph.getDescriptor(BPMN_SEQUENCE_FLOW));

		DefaultGraphicalGraphViewerAdvisor advisor = new DefaultGraphicalGraphViewerAdvisor(nodeDescriptors, edgeDescriptors, graph);
		new GraphicalGraphViewerWithFlyoutPalette(composite, advisor);
		graph.addLogListener(new GraphLogListener());
	}

}

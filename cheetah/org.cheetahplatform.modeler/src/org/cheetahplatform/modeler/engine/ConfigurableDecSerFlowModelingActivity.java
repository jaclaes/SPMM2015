package org.cheetahplatform.modeler.engine;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.experiment.editor.model.DecSerFlowNode;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;

public class ConfigurableDecSerFlowModelingActivity extends DecSerFlowModelingActivity {

	private DecSerFlowNode dsfNode;

	public ConfigurableDecSerFlowModelingActivity(Graph initialGraph, Process process, DecSerFlowNode dsfNode) {
		super(initialGraph, process);
		this.dsfNode = dsfNode;
	}

	@Override
	protected void doExecute() {
		if (dsfNode != null) {
			CheetahPlatformConfigurator cpc = CheetahPlatformConfigurator.getInstance();
			for (IEdgeDescriptor desc : EditorRegistry.getEdgeDescriptors(EditorRegistry.DECSERFLOW)) {
				cpc.set(desc.getId(), dsfNode.getConstraints().contains(desc.getId()));
			}
		}

		super.doExecute();
	}

}

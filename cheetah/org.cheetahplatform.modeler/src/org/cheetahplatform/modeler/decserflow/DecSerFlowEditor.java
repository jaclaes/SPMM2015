package org.cheetahplatform.modeler.decserflow;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.AbstractExperimentalGraphEditor;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.graph.GraphEditorInput;
import org.cheetahplatform.modeler.graph.IGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;

/**
 * @author felix
 * 
 */
public class DecSerFlowEditor extends AbstractExperimentalGraphEditor {

	public static final String ID = "org.cheetahplatform.modeler.DecSerFlowEditor";

	public DecSerFlowEditor() {
		super(EditorRegistry.DECSERFLOW);
	}

	@Override
	protected List<IEdgeDescriptor> createEdgeDescriptors() {
		List<IEdgeDescriptor> edges = new ArrayList<IEdgeDescriptor>();

		for (IEdgeDescriptor desc : EditorRegistry.getEdgeDescriptors(EditorRegistry.DECSERFLOW)) {
			if (CheetahPlatformConfigurator.getBoolean(desc.getId())) {
				edges.add((IEdgeDescriptor) EditorRegistry.getDescriptor(desc.getId()));
			}
		}

		return edges;
	}

	@Override
	protected IGraphicalGraphViewerAdvisor createGraphAdvisor() {
		GraphEditorInput editorInput = (GraphEditorInput) getEditorInput();
		List<IEdgeDescriptor> edges = createEdgeDescriptors();
		List<INodeDescriptor> nodes = createNodeDescriptors();
		IGraphicalGraphViewerAdvisor advisor = new DecSerFlowGraphAdvisor(nodes, edges);

		if (editorInput.hasGraph()) {
			Graph graph = editorInput.getGraph();
			advisor = new DecSerFlowGraphAdvisor(nodes, edges, graph);
		}

		return advisor;
	}

}

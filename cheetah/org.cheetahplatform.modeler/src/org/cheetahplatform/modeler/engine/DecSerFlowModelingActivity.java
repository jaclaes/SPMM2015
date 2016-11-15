package org.cheetahplatform.modeler.engine;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.model.Graph;

public class DecSerFlowModelingActivity extends AbstractModelingActivity {

	public static final String ID = "DECSERFLOW_MODELING";

	public static DecSerFlowModelingActivity createActivity(Process process, String initialModel) {
		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.DECSERFLOW));
		loadInitialGraph(graph, initialModel);

		try {
			return new DecSerFlowModelingActivity(graph, process);
		} catch (Exception e) {
			Activator.logError("Could not create configuration for change task", e);
		}

		throw new RuntimeException("Could not initialize the workflow configuration.");
	}

	public DecSerFlowModelingActivity(Graph initialGraph, Process process) {
		this(initialGraph, process, false);
	}

	public DecSerFlowModelingActivity(Graph initialGraph, Process process, boolean optional) {
		super(ID, EditorRegistry.DECSERFLOW, initialGraph, process, optional);
	}

	@Override
	public Object getName() {
		return "DecSerFlow Modeling";
	}

}

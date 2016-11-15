package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.export.CustomNodeConverter;
import org.cheetahplatform.modeler.graph.mapping.EdgeCondition;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class DefaultParagraphProvider implements IMappingProvider {

	private CustomNodeConverter converter;
	private EdgeConditionCache edgeConditionCache;

	@Override
	public void dispose() {
		if (converter != null) {
			converter.dispose();
			converter = null;
		}
	}

	@Override
	public EdgeCondition getEdgeConditionMapping(Edge edge) {
		return edgeConditionCache.getCondition(edge);
	}

	@Override
	public String getParagraphMapping(Node node) {
		Assert.isNotNull(converter, "Provider has not been initialized.");
		return converter.getParagraph(node);
	}

	@Override
	public void initialize(ProcessInstanceDatabaseHandle handle) {
		dispose();

		ProcessInstance modelingInstance = handle.getInstance();
		String type = modelingInstance.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
		Process process = ProcessRepository.getProcess(modelingInstance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS));
		Graph graphToBeRestored = AbstractModelingActivity.loadInitialGraph(process, type);
		AbstractModelingActivity.restoreGraph(graphToBeRestored, modelingInstance);
		ProcessInstanceDatabaseHandle graphHandle = new ProcessInstanceDatabaseHandle(handle.getDatabaseId(), modelingInstance.getId(), "",
				"");
		graphHandle.setInstance(modelingInstance);
		ReplayModel replayModel = new ReplayModel(new GraphCommandStack(graphToBeRestored), graphHandle, graphToBeRestored);

		converter = new CustomNodeConverter(replayModel, process);
		edgeConditionCache = new EdgeConditionCache(handle);
	}

}

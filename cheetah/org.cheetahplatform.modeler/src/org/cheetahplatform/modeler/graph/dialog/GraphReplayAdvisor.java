package org.cheetahplatform.modeler.graph.dialog;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.generic.GenericMenuManager;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.GraphEditor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.action.MenuManager;

public class GraphReplayAdvisor implements IReplayAdvisor {

	private GraphEditor editor;

	@Override
	public void dispose() {
		editor.setEditable(true);
		editor.getViewer().setContextMenu(new GenericMenuManager(editor.getViewer()));
	}

	@Override
	public GraphCommandStack initialize(ProcessInstance instance) {
		String processId = instance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		Process process = ProcessRepository.getProcess(processId);
		String graphType = instance.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
		Graph graph = AbstractModelingActivity.loadInitialGraph(process, graphType);

		List<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute(CommonConstants.ATTRIBUTE_PROCESS, process.getId()));
		editor = (GraphEditor) EditorRegistry.openEditor(graphType, graph, attributes, process);
		editor.setEditable(false);
		editor.getViewer().setContextMenu(new MenuManager());

		return (GraphCommandStack) editor.getViewer().getEditDomain().getCommandStack();
	}

	@Override
	public GraphCommandStack initialize(ProcessInstanceDatabaseHandle handle) {
		ProcessInstance instance = handle.getInstance();
		return initialize(instance);
	}
}

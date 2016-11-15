package org.cheetahplatform.literatemodeling;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.views.AssociationsView;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.generic.GenericMenuManager;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.dialog.IReplayAdvisor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class LiterateReplayAdvisor implements IReplayAdvisor {

	public static LiterateModel loadInitialModel(Process process, String graphType) {
		LiterateInitialValues iniVal = ProcessRepository.getInitialLiterateModel(process);
		LiterateModel graph = new LiterateModel(EditorRegistry.getDescriptors(graphType), iniVal);

		String path = ProcessRepository.getInitialModelPath(process);
		if (path != null) {
			LiterateModel initialGraph = (LiterateModel) AbstractModelingActivity.loadInitialGraph(graph, path);
			if (initialGraph != null) {
				return initialGraph;
			}
		}
		return graph;
	}

	private LiterateModelingEditor editor;

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
		Graph graph = loadInitialModel(process, graphType);

		List<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute(CommonConstants.ATTRIBUTE_PROCESS, process.getId()));
		editor = (LiterateModelingEditor) EditorRegistry.openEditor(EditorRegistry.LITERATE_MODELING, graph, attributes, process);
		editor.setEditable(false);
		editor.getViewer().setContextMenu(new MenuManager());

		// set LiterateModeling Perspective
		IWorkbench workbench = PlatformUI.getWorkbench();
		IPerspectiveDescriptor perspective = workbench.getPerspectiveRegistry().findPerspectiveWithId(LiterateModelingPerspective.ID);
		IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow().getActivePage();
		activePage.setPerspective(perspective);

		AssociationsView assocView = (AssociationsView) activePage.findView(AssociationsView.ID);
		if (assocView == null) {
			try {
				assocView = (AssociationsView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.showView(AssociationsView.ID);
			} catch (PartInitException e) {
				// nothing to do here
			}
		}

		if (assocView != null) {
			assocView.setInput((LiterateModel) graph);
		}

		return (GraphCommandStack) editor.getViewer().getEditDomain().getCommandStack();
	}

	@Override
	public GraphCommandStack initialize(ProcessInstanceDatabaseHandle handle) {
		ProcessInstance instance = handle.getInstance();
		return initialize(instance);
	}

}

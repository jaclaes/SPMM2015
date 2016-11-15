package org.cheetahplatform.modeler.graph.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class ActivityNameExporter extends AbstractExporter {

	private File targetDirectory;

	private void appendActivities(StringBuilder output, String header, List<String> initialActivities) {
		output.append(header);
		output.append("\n");

		for (String name : initialActivities) {
			output.append("\n");
			output.append(name);
		}
	}

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry) {
		String type = modelingInstance.getInstance().getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
		Process process = ProcessRepository.getProcess(modelingInstance.getInstance().getAttribute(CommonConstants.ATTRIBUTE_PROCESS));
		Graph graph = AbstractModelingActivity.loadInitialGraph(process, type);
		List<String> initialActivities = listActivityNames(graph);

		AbstractModelingActivity.restoreGraph(graph, modelingInstance.getInstance());
		List<String> activities = listActivityNames(graph);

		StringBuilder output = new StringBuilder();
		if (!initialActivities.isEmpty()) {
			appendActivities(output, "Initial activities", initialActivities);
			output.append("\n---------------------------------------------\n\n\n");
		}
		appendActivities(output, "Activities in the final model", activities);

		File target = new File(targetDirectory, modelingInstance.getId() + ".txt");
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(target);
			out.write(output.toString().getBytes());
		} catch (Exception e) {
			Activator.logError("Could not write to a file.", e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	@Override
	public void initializeExport(File target) {
		this.targetDirectory = target;
	}

	private List<String> listActivityNames(Graph graph) {
		List<String> names = new ArrayList<String>();
		for (Node node : graph.getNodes()) {
			String id = node.getDescriptor().getId();
			if (id.equals(EditorRegistry.BPMN_ACTIVITY) || id.equals(EditorRegistry.BPMN_ACTIVITY_HIERARCHICAL)) {
				names.add(node.getName());
			}
		}

		return names;
	}

}

package org.cheetahplatform.modeler.action;

import static org.cheetahplatform.common.logging.PromLogger.GROUP_EVENT_START;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_SELECTION;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.CREATE_EDGE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.DESCRIPTOR;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.tdm.command.TDMCommand;

public class ExportModelingStepsAction extends AbstractExportAction {

	private class AssembleModelingStepsExporter extends AbstractExporter {
		private StringBuilder content;
		private StringBuilder solutions;
		private File target;

		@Override
		protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry) {
			String processId = modelingInstance.getInstance().getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
			String id = modelingInstance.getInstance().getId();
			content.append(id);
			content.append(" (");
			content.append(processId);
			content.append(")\n\n");

			StringBuilder currentTask = new StringBuilder();

			for (AuditTrailEntry current : modelingInstance.getInstance().getEntries()) {
				String eventType = current.getEventType();

				if (!TDMCommand.getCommandLabel(current).trim().isEmpty() || eventType.equals(GROUP_EVENT_START)
						|| eventType.equals(PromLogger.GROUP_EVENT_END)) {
					continue;// ignore TDM commands
				}

				String label = AbstractGraphCommand.getCommandLabel(current);
				if (eventType.equals(CREATE_EDGE) && current.getAttribute(DESCRIPTOR).equals(DECSERFLOW_SELECTION)) {
					String minimum = current.getAttribute(SelectionConstraintDescriptor.MINIMUM);
					String maximum = current.getAttribute(SelectionConstraintDescriptor.MAXIMUM);

					if (maximum.equals(String.valueOf(Integer.MAX_VALUE))) {
						maximum = "*";
					}
					if (minimum.equals(String.valueOf(Integer.MIN_VALUE))) {
						minimum = "0";
					}

					label = label + ", min: " + minimum + ", maximum: " + maximum;
				}
				content.append(label);
				content.append("\n");

				currentTask.append(label);
				currentTask.append("\n");
			}

			String taskId = processId.substring(processId.length() - 7, processId.length());
			String solution = SOLUTIONS.get(taskId);
			solutions.append(id);

			// store correct solutions only, probably the model was correct, but not identical to the standard solution
			if (currentTask.toString().equals(solution)) {
				solutions.append("\t1\n");
			} else {
				// otherwise store the steps for manual analysis
				solutions.append("\n");
				solutions.append(currentTask.toString());
				solutions.append("\nSOLUTION:\n");
				solutions.append(solution);
				solutions.append("\n");
				solutions.append(TARGETED_INVARIANT.get(taskId));
				solutions.append("\n\n");
			}

			content.append("------------------------------\n\n\n");
		}

		@Override
		public void exportFinished() {
			super.exportFinished();

			write(content, target);

			String solutionsFileName = target.getName().substring(0, target.getName().indexOf(".")) + "_solutions." + extension;
			File solutionsFile = new File(target.getParentFile(), solutionsFileName);
			write(solutions, solutionsFile);
		}

		@Override
		public void initializeExport(File target) {
			this.target = target;
			this.content = new StringBuilder();
			this.solutions = new StringBuilder();
		}

		private void write(StringBuilder toWrite, File file) {
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(file);
				out.write(toWrite.toString().getBytes());
				out.flush();
				out.close();
			} catch (Exception e) {
				Activator.logError("Could not write to a file.", e);

				if (out != null) {
					try {
						out.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	public static final String ID = "org.cheetahplatform.modeler.action.ExportModelingStepsAction";
	private static final Map<String, String> SOLUTIONS;
	private static final Map<String, String> TARGETED_INVARIANT;

	static {
		SOLUTIONS = new HashMap<String, String>();

		SOLUTIONS
				.put("task_11",
						"Delete Coexistence from Activity 'J' to Activity 'F'\nCreate Selection from Activity 'J' to Activity 'J', min: 1, maximum: *\n");
		SOLUTIONS.put("task_21",
				"Delete Chained Succession from Activity 'N' to Activity 'O'\nCreate Response from Activity 'N' to Activity 'P'\n");
		SOLUTIONS
				.put("task_31",
						"Delete Coexistence from Activity 'N' to Activity 'J'\nCreate Selection from Activity 'J' to Activity 'J', min: 1, maximum: *\n");
		SOLUTIONS.put("task_12",
				"Delete Not Co-Existence from Activity 'Z' to Activity 'V'\nCreate Negation Response from Activity 'Z' to Activity 'V'\n");
		SOLUTIONS
				.put("task_22",
						"Delete Response from Activity 'A' to Activity 'B'\nCreate Selection from Activity 'B' to Activity 'B', min: 1, maximum: *\n");
		SOLUTIONS
				.put("task_32",
						"Delete Chained Succession from Activity 'C' to Activity 'R'\nCreate Negation Response from Activity 'C' to Activity 'U'\n");
		SOLUTIONS
				.put("task_13",
						"Delete Chained Succession from Activity 'Q' to Activity 'M'\nCreate Negation Response from Activity 'Q' to Activity 'I'\n");
		SOLUTIONS
				.put("task_23",
						"Delete Chained Succession from Activity 'Q' to Activity 'R'\nCreate Negation Response from Activity 'Q' to Activity 'M'\n");
		SOLUTIONS.put("task_33",
				"Delete Precedence from Activity 'F' to Activity 'I'\nCreate Precedence from Activity 'E' to Activity 'I'\n");
		SOLUTIONS.put("task_24", "Delete Response from Activity 'L' to Activity 'P'\nCreate Response from Activity 'H' to Activity 'M'\n");
		SOLUTIONS.put("task_34", "Delete Response from Activity 'K' to Activity 'J'\nCreate Response from Activity 'O' to Activity 'J'\n");
		SOLUTIONS.put("task_15",
				"Delete Precedence from Activity 'L' to Activity 'M'\nCreate Precedence from Activity 'K' to Activity 'M'\n");
		SOLUTIONS.put("task_25",
				"Delete Not Co-Existence from Activity 'M' to Activity 'L'\nCreate Negation Response from Activity 'M' to Activity 'L'\n");

		TARGETED_INVARIANT = new HashMap<String, String>();
		TARGETED_INVARIANT.put("task_11", "J must be executed at least once");
		TARGETED_INVARIANT.put("task_21", "N must be followed by P");
		TARGETED_INVARIANT.put("task_31", "J must be executed at least once");
		TARGETED_INVARIANT.put("task_12", "V cannot be executed after Z");
		TARGETED_INVARIANT.put("task_22", "B must be executed at least once");
		TARGETED_INVARIANT.put("task_32", "U cannot be executed after C");
		TARGETED_INVARIANT.put("task_13", "I cannot be executed after Q");
		TARGETED_INVARIANT.put("task_23", "M cannot be executed after Q");
		TARGETED_INVARIANT.put("task_33", "I must be preceded by E");
		TARGETED_INVARIANT.put("task_24", "H must be followed by M");
		TARGETED_INVARIANT.put("task_34", "O must be followed by J");
		TARGETED_INVARIANT.put("task_15", "K must be executed before M");
		TARGETED_INVARIANT.put("task_25", "L cannot be executed after M");

	}

	public ExportModelingStepsAction() {
		setId(ID);
		setText("Export Modeling Steps");
		setExtension("txt");
	}

	@Override
	protected AbstractExporter createExporter() {
		return new AssembleModelingStepsExporter();
	}

}

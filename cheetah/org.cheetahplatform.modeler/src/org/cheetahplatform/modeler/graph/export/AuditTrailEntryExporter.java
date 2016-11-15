package org.cheetahplatform.modeler.graph.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

import com.ibm.icu.text.SimpleDateFormat;

public class AuditTrailEntryExporter extends AbstractExporter {

	private File target;

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry) {

		ProcessInstance instance = modelingInstance.getInstance();
		List<AuditTrailEntry> entries = instance.getEntries();

		SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

		StringBuilder builder = new StringBuilder();
		builder.append("timestamp;type;detail;element1;element2\n");
		for (AuditTrailEntry auditTrailEntry : entries) {
			if (passesFilter(auditTrailEntry)) {
				builder.append(format.format(auditTrailEntry.getTimestamp()));
				builder.append(";");
				builder.append(auditTrailEntry.getEventType());
				builder.append(";");
				if (!PromLogger.GROUP_EVENT_END.equals(auditTrailEntry.getEventType())
						&& !PromLogger.GROUP_EVENT_START.equals(auditTrailEntry.getEventType())) {
					builder.append(AbstractGraphCommand.getCommandLabel(auditTrailEntry));
					builder.append(";");
				}

				if (isNodeCommand(auditTrailEntry)) {

					String fullName;
					if (auditTrailEntry.isAttributeDefined(AbstractGraphCommand.NAME)) {
						fullName = auditTrailEntry.getAttribute(AbstractGraphCommand.NAME);
					} else {
						fullName = AbstractGraphCommand.getFullName(auditTrailEntry, AbstractGraphCommand.DESCRIPTOR,
								AbstractGraphCommand.NAME);
					}
					builder.append(fullName);
				} else if (isEdgeCommand(auditTrailEntry)) {

					if (auditTrailEntry.isAttributeDefined(AbstractGraphCommand.SOURCE_NODE_DESCRIPTOR)) {
						String sourceName = auditTrailEntry.getAttribute(AbstractGraphCommand.SOURCE_NODE_NAME);
						builder.append(sourceName);
					}
					builder.append(";");
					if (auditTrailEntry.isAttributeDefined(AbstractGraphCommand.TARGET_NODE_DESCRIPTOR)) {
						String targetName = auditTrailEntry.getAttribute(AbstractGraphCommand.TARGET_NODE_NAME);
						builder.append(targetName);
					}
				} else if (AbstractGraphCommand.RENAME.equals(auditTrailEntry.getEventType())) {
					if (auditTrailEntry.isAttributeDefined(AbstractGraphCommand.NAME)) {
						String oldName = auditTrailEntry.getAttribute(AbstractGraphCommand.NAME);
						builder.append(oldName);
					}
					if (auditTrailEntry.isAttributeDefined(AbstractGraphCommand.NEW_NAME)) {
						builder.append(";");
						builder.append(auditTrailEntry.getAttribute(AbstractGraphCommand.NEW_NAME));
					}
				}

				builder.append(";\n");
			}
		}

		try {
			String path = getPathToFile(target, instance, "csv");
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)));
			writer.write(builder.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
		super.initializeExport(target);
	}

	private boolean isEdgeCommand(AuditTrailEntry auditTrailEntry) {
		String eventType = auditTrailEntry.getEventType();

		if (AbstractGraphCommand.CREATE_EDGE.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.DELETE_EDGE.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.CREATE_EDGE_BENDPOINT.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.MOVE_EDGE_BENDPOINT.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.DELETE_EDGE_BENDPOINT.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.RECONNECT_EDGE.equals(eventType)) {
			return true;
		}

		return false;
	}

	private boolean isNodeCommand(AuditTrailEntry auditTrailEntry) {
		String eventType = auditTrailEntry.getEventType();

		if (AbstractGraphCommand.CREATE_NODE.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.MOVE_NODE.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.DELETE_NODE.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.MOVE_EDGE_LABEL.equals(eventType)) {
			return true;
		}
		return false;
	}

	private boolean passesFilter(AuditTrailEntry entry) {
		String eventType = entry.getEventType();
		if (eventType.equals(AbstractGraphCommand.VSCROLL)) {
			return false;
		}
		if (eventType.equals(AbstractGraphCommand.HSCROLL)) {
			return false;
		}
		return true;
	}

}

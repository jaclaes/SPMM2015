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

public class CategorizationExporter extends AbstractExporter {
	private static final String ANPASSUNG = "Anpassung";
	private static final String POSITIONIERUNG = "Positionierung";
	private static final String VERWERFUNG = "Verwerfung";
	private static final String BEREITSTELLUNG = "Bereitstellung";
	private File target;

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry) {

		ProcessInstance instance = modelingInstance.getInstance();
		List<AuditTrailEntry> entries = instance.getEntries();

		SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

		StringBuilder builder = new StringBuilder();
		builder.append("Timestamp;Type;Detail;Kategorisierung\n");
		for (AuditTrailEntry auditTrailEntry : entries) {
			if (passesFilter(auditTrailEntry)) {

				if (isGroupStartEvent(auditTrailEntry)) {
					builder.append(format.format(auditTrailEntry.getTimestamp()));
					builder.append(";");
					builder.append("Gruppierte Schritte Start");
					builder.append(";\n");
					continue;
				}
				if (isGroupEndEvent(auditTrailEntry)) {
					builder.append(format.format(auditTrailEntry.getTimestamp()));
					builder.append(";");
					builder.append("Gruppierte Schritte Ende");
					builder.append(";\n");
					continue;
				}

				printInformation(format, builder, auditTrailEntry);

				if (isReconnectEntry(auditTrailEntry)) {
					builder.append(VERWERFUNG);
					builder.append(";\n");
					printInformation(format, builder, auditTrailEntry);
					builder.append(BEREITSTELLUNG);
				} else {
					builder.append(getCategorization(auditTrailEntry));
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

	private String getCategorization(AuditTrailEntry auditTrailEntry) {
		if (isCreateEntry(auditTrailEntry)) {
			return BEREITSTELLUNG;
		}

		if (isDeleteEntry(auditTrailEntry)) {
			return VERWERFUNG;
		}

		if (isPositionEntry(auditTrailEntry)) {
			return POSITIONIERUNG;
		}

		if (isRename(auditTrailEntry)) {
			return ANPASSUNG;
		}

		throw new IllegalStateException("Unknonw Type: " + auditTrailEntry.getEventType());
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
		super.initializeExport(target);
	}

	private boolean isCreateEntry(AuditTrailEntry auditTrailEntry) {
		String eventType = auditTrailEntry.getEventType();

		if (AbstractGraphCommand.CREATE_NODE.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.CREATE_EDGE.equals(eventType)) {
			return true;
		}

		return false;
	}

	private boolean isDeleteEntry(AuditTrailEntry auditTrailEntry) {
		String eventType = auditTrailEntry.getEventType();

		if (AbstractGraphCommand.DELETE_EDGE.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.DELETE_NODE.equals(eventType)) {
			return true;
		}

		return false;
	}

	private boolean isGroupEndEvent(AuditTrailEntry auditTrailEntry) {
		if (PromLogger.GROUP_EVENT_END.equals(auditTrailEntry.getEventType())) {
			return true;
		}
		return false;
	}

	private boolean isGroupEvent(AuditTrailEntry auditTrailEntry) {
		if (isGroupEndEvent(auditTrailEntry)) {
			return true;
		}
		if (isGroupStartEvent(auditTrailEntry)) {
			return true;
		}

		return false;
	}

	private boolean isGroupStartEvent(AuditTrailEntry auditTrailEntry) {
		if (PromLogger.GROUP_EVENT_START.equals(auditTrailEntry.getEventType())) {
			return true;
		}
		return false;
	}

	private boolean isPositionEntry(AuditTrailEntry auditTrailEntry) {
		String eventType = auditTrailEntry.getEventType();

		if (AbstractGraphCommand.MOVE_NODE.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.MOVE_EDGE_LABEL.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.MOVE_EDGE_BENDPOINT.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.CREATE_EDGE_BENDPOINT.equals(eventType)) {
			return true;
		}
		if (AbstractGraphCommand.DELETE_EDGE_BENDPOINT.equals(eventType)) {
			return true;
		}

		return false;
	}

	private boolean isReconnectEntry(AuditTrailEntry auditTrailEntry) {
		return auditTrailEntry.getEventType().equals(AbstractGraphCommand.RECONNECT_EDGE);
	}

	private boolean isRename(AuditTrailEntry auditTrailEntry) {
		String eventType = auditTrailEntry.getEventType();

		if (AbstractGraphCommand.RENAME.equals(eventType)) {
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

	private void printInformation(SimpleDateFormat format, StringBuilder builder, AuditTrailEntry auditTrailEntry) {
		builder.append(format.format(auditTrailEntry.getTimestamp()));
		builder.append(";");

		builder.append(auditTrailEntry.getEventType());
		builder.append(";");
		if (!isGroupEvent(auditTrailEntry)) {
			builder.append(AbstractGraphCommand.getCommandLabel(auditTrailEntry));
		}
		builder.append(";");
	}
}

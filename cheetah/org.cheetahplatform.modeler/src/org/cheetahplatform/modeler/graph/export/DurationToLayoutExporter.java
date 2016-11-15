package org.cheetahplatform.modeler.graph.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;

import com.ibm.icu.text.SimpleDateFormat;

public class DurationToLayoutExporter extends AbstractExporter {

	private File target;

	public StringBuilder createCsvOutput(Map<AuditTrailEntry, Long> durationsToLayout, DurationToLayoutCalculator calculator) {
		StringBuilder builder = new StringBuilder();
		SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

		builder.append("Time of Layout;Type of Layout;Time of Creation; CreationType;Duration to Layout;\n");
		for (Entry<AuditTrailEntry, Long> entry : durationsToLayout.entrySet()) {
			AuditTrailEntry layoutEntry = entry.getKey();

			builder.append(format.format(layoutEntry.getTimestamp()));
			builder.append(";");
			builder.append(layoutEntry.getEventType());
			builder.append(";");

			AuditTrailEntry createEntry = calculator.getCreateEntry(layoutEntry);

			builder.append(format.format(createEntry.getTimestamp()));
			builder.append(";");
			builder.append(createEntry.getEventType());
			builder.append(";");

			builder.append(entry.getValue());
			builder.append(";\n");
		}
		return builder;
	}

	public void createOutput(Map<AuditTrailEntry, Long> durationsToLayout, ProcessInstance instance, DurationToLayoutCalculator calculator) {
		StringBuilder builder = createCsvOutput(durationsToLayout, calculator);
		writeFile(instance, builder);
	}

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry) {
		ProcessInstance instance = modelingInstance.getInstance();
		DurationToLayoutCalculator calculator = new DurationToLayoutCalculator(instance);
		Map<AuditTrailEntry, Long> durationsToLayout = calculator.getLayoutDurations();
		createOutput(durationsToLayout, instance, calculator);
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
		super.initializeExport(target);
	}

	public void writeFile(ProcessInstance instance, StringBuilder builder) {
		String pathToFile = getPathToFile(target, instance, "csv");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathToFile)));
			writer.write(builder.toString());
			writer.close();
		} catch (IOException e) {
			Activator.logError("Unable to write file", e);
		}
	}
}

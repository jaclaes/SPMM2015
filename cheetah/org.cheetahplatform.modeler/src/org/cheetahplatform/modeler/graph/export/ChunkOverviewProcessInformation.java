package org.cheetahplatform.modeler.graph.export;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.csv.AbstractProcessInformation;

public class ChunkOverviewProcessInformation extends AbstractProcessInformation {
	private static PreparedStatement STATEMENT;

	static {
		try {
			STATEMENT = org.cheetahplatform.common.Activator
					.getDatabaseConnector()
					.getDatabaseConnection()
					.prepareStatement(
							"SELECT p.id FROM audittrail_entry a, process_instance p where a.data like ? and a.process_instance=p.database_id;");
		} catch (SQLException e) {
			Activator.logError("Error preparing the statement for retrieving the workflow configuration id", e);
		}
	}

	public ChunkOverviewProcessInformation(ProcessInstance processInstance, IChunkExtractor extractor) {
		super(new ArrayList<Attribute>(), true);
		try {
			STATEMENT.setString(1, "%PROCESS_INSTANCE" + processInstance.getId() + "%");
			ResultSet resultset = STATEMENT.executeQuery();
			while (resultset.next()) {
				String workflowId = resultset.getString(1);
				addAttribute(new Attribute("Workflow ID", workflowId));
			}

			resultset.close();
		} catch (SQLException e) {
			Activator.logError("Error retrieving the workflow configuration id", e);
		}

		addAttribute(new Attribute("Model ID", processInstance.getId()));

		String type = processInstance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		addAttribute(new Attribute("type", type));

		List<Chunk> chunks = extractor.extractChunks(processInstance);

		List<Chunk> comprehensionChunks = getChunksForType(chunks, ModelingPhaseChunkExtractor.COMPREHENSION);
		List<Chunk> modelingChunks = getChunksForType(chunks, ModelingPhaseChunkExtractor.MODELING);
		List<Chunk> reconciliationChunks = getChunksForType(chunks, ModelingPhaseChunkExtractor.RECONCILIATION);

		addAttribute(new Attribute("Total # Chunks", chunks.size()));
		addAttribute(new Attribute("Total # Comp. Chunks", comprehensionChunks.size()));
		addAttribute(new Attribute("Total # Mod. Chunks", modelingChunks.size()));
		addAttribute(new Attribute("Total # Rec. Chunks", reconciliationChunks.size()));

		addAttribute(new Attribute("Avg. # Comp. Chunk", formatDouble(calculateAverageChunkSize(comprehensionChunks))));
		addAttribute(new Attribute("Avg. # Mod. Chunk", formatDouble(calculateAverageChunkSize(modelingChunks))));
		addAttribute(new Attribute("Avg. # Rec. Chunk", formatDouble(calculateAverageChunkSize(reconciliationChunks))));

		List<AuditTrailEntry> entries = processInstance.getEntries();
		if (!entries.isEmpty()) {
			long processInstanceTimestamp = processInstance.getLongAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP);
			Date firstEntryTimeStamp = entries.get(0).getTimestamp();
			addAttribute(new Attribute("Intial Comp. Duration", firstEntryTimeStamp.getTime() - processInstanceTimestamp));
		}

		addAttribute(new Attribute("Total Comp. Duration", getTotalDuration(comprehensionChunks)));
		addAttribute(new Attribute("Total Mod. Duration", getTotalDuration(modelingChunks)));
		addAttribute(new Attribute("Total Rec. Duration", getTotalDuration(reconciliationChunks)));

		addAttribute(new Attribute("Avg. Comp. Duration", formatDouble(getAverageDuration(comprehensionChunks))));
		addAttribute(new Attribute("Avg. Mod. Duration", formatDouble(getAverageDuration(modelingChunks))));
		addAttribute(new Attribute("Avg. Rec. Duration", formatDouble(getAverageDuration(reconciliationChunks))));
	}

	private double calculateAverageChunkSize(List<Chunk> chunks) {
		if (chunks.isEmpty()) {
			return 0;
		}

		double sum = 0;
		for (Chunk chunk : chunks) {
			sum += chunk.getSize();
		}

		return sum / chunks.size();
	}

	private String formatDouble(double value) {
		return String.valueOf(value).replaceAll("\\.", ",");
	}

	private double getAverageDuration(List<Chunk> chunks) {
		if (chunks.isEmpty()) {
			return 0;
		}

		double totalDuration = 0;
		for (Chunk chunk : chunks) {
			totalDuration += chunk.getDuration();
		}

		return totalDuration / chunks.size();
	}

	private List<Chunk> getChunksForType(List<Chunk> chunks, String type) {
		List<Chunk> matchingChunks = new ArrayList<Chunk>();
		for (Chunk chunk : chunks) {
			if (chunk.getType().equals(type)) {
				matchingChunks.add(chunk);
			}
		}
		return matchingChunks;
	}

	@Override
	protected String getEmptyCellValue() {
		return "";
	}

	private long getTotalDuration(List<Chunk> comprehensionChunks) {
		long total = 0;
		for (Chunk chunk : comprehensionChunks) {
			total += chunk.getDuration();
		}
		return total;
	}
}

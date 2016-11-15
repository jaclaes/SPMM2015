package org.cheetahplatform.modeler.graph.export;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public abstract class AbstractNumberOfTouchedElementsStatistic implements IPpmStatistic {

	protected Map<Chunk, Set<Long>> getTouchedElementsPerChunk(List<Chunk> chunks, String chunkType, String... commandTypes) {
		Map<Chunk, Set<Long>> touchedElementsPerChunk = new HashMap<Chunk, Set<Long>>();
		List<String> typesList = Arrays.asList(commandTypes);

		for (Chunk chunk : chunks) {
			if (!chunk.getType().equals(chunkType)) {
				continue;
			}

			Set<Long> touchedIds = new HashSet<Long>();

			List<AuditTrailEntry> entries = chunk.getEntries();
			for (AuditTrailEntry auditTrailEntry : entries) {
				if (typesList.contains(auditTrailEntry.getEventType())) {
					long id = auditTrailEntry.getLongAttribute(AbstractGraphCommand.ID);
					touchedIds.add(id);
				}
			}

			touchedElementsPerChunk.put(chunk, touchedIds);
		}
		return touchedElementsPerChunk;
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}

}
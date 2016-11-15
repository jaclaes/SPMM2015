package org.cheetahplatform.modeler.graph.export;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.eclipse.core.runtime.Assert;

public class ModelElementInformationExtractor {

	private HashMap<Long, ModelElementInformation> elements;

	public ModelElementInformationExtractor() {
		elements = new HashMap<Long, ModelElementInformation>();
	}

	public Map<Long, ModelElementInformation> extract(List<AuditTrailEntry> entries) {
		Assert.isNotNull(entries);

		for (AuditTrailEntry auditTrailEntry : entries) {
			if (isCreateNodeEntry(auditTrailEntry)) {
				ModelElementInformation modelElementInformation = new ModelElementInformation(auditTrailEntry);
				long id = getId(auditTrailEntry);
				elements.put(id, modelElementInformation);
				continue;
			}

			if (isMoveNodeEntry(auditTrailEntry)) {
				long id = getId(auditTrailEntry);
				ModelElementInformation modelElementInformation = elements.get(id);
				modelElementInformation.addEntry(auditTrailEntry);
			}
		}

		return elements;
	}

	public long getId(AuditTrailEntry auditTrailEntry) {
		return auditTrailEntry.getLongAttribute(AbstractGraphCommand.ID);
	}

	private boolean isCreateNodeEntry(AuditTrailEntry auditTrailEntry) {
		String eventType = auditTrailEntry.getEventType();
		if (eventType.equals(AbstractGraphCommand.CREATE_NODE)) {
			return true;
		}

		return false;
	}

	private boolean isMoveNodeEntry(AuditTrailEntry auditTrailEntry) {
		String eventType = auditTrailEntry.getEventType();
		if (eventType.equals(AbstractGraphCommand.MOVE_NODE)) {
			return true;
		}

		return false;
	}
}

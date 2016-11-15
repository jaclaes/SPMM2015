package org.cheetahplatform.modeler.graph.export;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class GroupedLayoutChunksExtractor extends AbstractGroupedChunksExtractor {

	private static final String LAYOUT = "LAYOUT";

	@Override
	protected String getGroup(AuditTrailEntry event) {
		String eventType = event.getEventType().trim();
		if (AbstractGraphCommand.CREATE_EDGE_BENDPOINT.equals(eventType)) {
			return LAYOUT;
		}
		if (AbstractGraphCommand.DELETE_EDGE_BENDPOINT.equals(eventType)) {
			return LAYOUT;
		}
		if (AbstractGraphCommand.MOVE_EDGE_BENDPOINT.equals(eventType)) {
			return LAYOUT;
		}
		if (AbstractGraphCommand.MOVE_EDGE_LABEL.equals(eventType)) {
			return LAYOUT;
		}
		if (AbstractGraphCommand.MOVE_NODE.equals(eventType)) {
			return LAYOUT;
		}

		return eventType;
	}
}

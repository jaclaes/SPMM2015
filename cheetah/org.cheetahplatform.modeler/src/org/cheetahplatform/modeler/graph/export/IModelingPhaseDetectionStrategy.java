package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;

public interface IModelingPhaseDetectionStrategy {
	String getDescription();

	boolean switchToNextPhase(List<AuditTrailEntry> entries, AuditTrailEntry auditTrailEntry);
}

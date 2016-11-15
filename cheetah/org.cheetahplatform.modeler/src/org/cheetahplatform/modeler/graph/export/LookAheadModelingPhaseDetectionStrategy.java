package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.eclipse.core.runtime.Assert;

public class LookAheadModelingPhaseDetectionStrategy implements IModelingPhaseDetectionStrategy {
	private int lookAhead;

	public LookAheadModelingPhaseDetectionStrategy(int lookAhead) {
		Assert.isLegal(lookAhead >= 0);
		this.lookAhead = lookAhead;
	}

	@Override
	public String getDescription() {
		return "LookAhead: " + lookAhead;
	}

	@Override
	public boolean switchToNextPhase(List<AuditTrailEntry> entries, AuditTrailEntry auditTrailEntry) {
		int index = entries.indexOf(auditTrailEntry);
		if (index == entries.size() - 1) {
			return true;
		}

		List<AuditTrailEntry> subList = entries.subList(index + 1, index + 1 + lookAhead);
		for (AuditTrailEntry auditTrailEntry2 : subList) {
			String phase1 = ModelingPhaseChunkExtractor.getModelingPhase(auditTrailEntry);
			String phase2 = ModelingPhaseChunkExtractor.getModelingPhase(auditTrailEntry2);
			if (!phase1.equals(phase2)) {
				return false;
			}
		}

		return true;
	}

}

package org.cheetahplatform.common.tutorial;

import org.cheetahplatform.common.logging.AuditTrailEntry;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.10.2009
 */
public interface ITutorialStep {
	void aboutToShow();

	void completed();

	String getDescription();

	String getScreencastPath();

	boolean isStepExecuted(AuditTrailEntry entry);
}

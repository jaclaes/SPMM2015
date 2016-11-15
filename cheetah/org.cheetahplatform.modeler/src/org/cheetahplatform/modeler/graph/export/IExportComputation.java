package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;

public interface IExportComputation {
	/**
	 * Perform additional computations for the given audit trail entry. Please note that this method is not called for audit trail entries
	 * representing process modeling activities. For those entries, {@link #computeForModelingProcessInstance(ProcessInstance)} is called
	 * instead.
	 * 
	 * @param entry
	 *            the entry for which computations should be performed
	 * @return the computed result
	 */
	List<Attribute> computeForAuditTrailEntry(AuditTrailEntry entry);

	/**
	 * Perform additional computation for the given *experimental* process instance.
	 * 
	 * @param instance
	 *            the instance
	 * @return the process instance
	 */
	List<Attribute> computeForExperimentalProcessInstance(ProcessInstance instance);

	/**
	 * Perform additional computation for the given process instance.
	 * 
	 * @param handle
	 *            the instance
	 * @return the process instance
	 */
	List<Attribute> computeForModelingProcessInstance(ProcessInstanceDatabaseHandle handle);
}

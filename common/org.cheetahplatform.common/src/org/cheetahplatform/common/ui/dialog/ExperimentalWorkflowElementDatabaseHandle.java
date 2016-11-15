package org.cheetahplatform.common.ui.dialog;

import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.db.DatabaseUtil;

/**
 * A class representing an activity of an experimental workflow.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         25.03.2010
 */
public class ExperimentalWorkflowElementDatabaseHandle extends AbstractDatabaseHandle {

	private final ProcessInstanceDatabaseHandle experimentalWorkflow;
	private final long timestamp;
	private final String type;

	/**
	 * Creates a new {@link ExperimentalWorkflowElementDatabaseHandle}.
	 * 
	 * @param databaseId
	 *            the database id of the corresponding {@link AuditTrailEntry}.
	 * @param experimentalProcess
	 *            the {@link ProcessInstanceDatabaseHandle} representing the experimental workflow the
	 *            {@link ExperimentalWorkflowElementDatabaseHandle} belongs to.
	 * @param timestamp
	 *            the timestamp
	 * @param type
	 *            the type
	 * @param attributes
	 *            the attributes
	 */
	public ExperimentalWorkflowElementDatabaseHandle(long databaseId, ProcessInstanceDatabaseHandle experimentalProcess, long timestamp,
			String type, String attributes) {
		super(databaseId);

		this.experimentalWorkflow = experimentalProcess;
		this.type = type;
		this.timestamp = timestamp;
		List<Attribute> attributeList = DatabaseUtil.fromDataBaseRepresentation(attributes);
		addAttributes(attributeList);
	}

	/**
	 * Returns the experimentalWorkflow.
	 * 
	 * @return the experimentalWorkflow
	 */
	public ProcessInstanceDatabaseHandle getExperimentalWorkflow() {
		return experimentalWorkflow;
	}

	@Override
	public String getId() {
		if (!isAttributeDefined(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE)) {
			return null;
		}

		return getAttribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE);
	}

	/**
	 * Returns the timestamp as a {@link Date}.
	 * 
	 * @return the timestamp as a {@link Date}
	 */
	public Date getTimeAsDate() {
		return new Date(timestamp);
	}

	/**
	 * Returns the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

}

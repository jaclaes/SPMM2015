/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.common.logging;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuditTrailEntry extends DataContainer {
	private Date timestamp;
	private String workflowModelElement;
	private String originator;
	private String eventType;

	public AuditTrailEntry() {
		this(new Date(), null, null, null, new ArrayList<Attribute>(0));
	}

	public AuditTrailEntry(Date date, String type, String workflowModelElement) {
		this(date, type, workflowModelElement, null, new ArrayList<Attribute>());
	}

	public AuditTrailEntry(Date timestamp, String type, String workflowElement, String originator, List<Attribute> data) {
		this.eventType = type;
		this.timestamp = timestamp;
		this.workflowModelElement = workflowElement;
		this.originator = originator;

		addAttributes(data);
	}

	public AuditTrailEntry(String type) {
		this(new Date(), type, null, null, new ArrayList<Attribute>(0));
	}

	public AuditTrailEntry(String type, String workflowModelElement) {
		this(new Date(), type, workflowModelElement, null, new ArrayList<Attribute>());
	}

	/**
	 * Returns the eventType.
	 * 
	 * @return the eventType.
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * Returns the originator.
	 * 
	 * @return the originator.
	 */
	public String getOriginator() {
		return originator;
	}

	/**
	 * Returns the date.
	 * 
	 * @return the date.
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	public long getWorkflowModeleElementAsLong() {
		return Long.parseLong(workflowModelElement);
	}

	/**
	 * Returns the task.
	 * 
	 * @return the task.
	 */
	public String getWorkflowModelElement() {
		return workflowModelElement;
	}

	/**
	 * Sets the eventType.
	 * 
	 * @param eventType
	 *            the eventType to set.
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * Sets the originator.
	 * 
	 * @param originator
	 *            the originator to set.
	 */
	public void setOriginator(String originator) {
		this.originator = originator;
	}

	/**
	 * Sets the time stamp as string
	 * 
	 * @param timestamp
	 *            the time as string
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Sets the task.
	 * 
	 * @param workflowModelElement
	 *            the task to set.
	 */
	public void setWorkflowModelElement(String workflowModelElement) {
		this.workflowModelElement = workflowModelElement;
	}

	@Override
	public String toString() {
		return workflowModelElement + ": " + eventType;
	}

}

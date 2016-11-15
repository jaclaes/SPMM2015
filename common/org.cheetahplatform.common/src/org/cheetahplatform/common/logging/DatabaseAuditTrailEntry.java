package org.cheetahplatform.common.logging;

import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.Assert;

public class DatabaseAuditTrailEntry extends AuditTrailEntry {
	private long id;

	public DatabaseAuditTrailEntry(Date date, String type, String workflowElement, String originator, List<Attribute> data, long id) {
		super(date, type, workflowElement, originator, data);
		Assert.isLegal(id > 0);
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
}

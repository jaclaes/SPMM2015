/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.common.logging;

import java.util.ArrayList;
import java.util.List;

public class ProcessInstance extends DataContainer {
	private String id;
	private final List<AuditTrailEntry> entries;

	public ProcessInstance() {
		this("");
	}

	public ProcessInstance(String id) {
		this.id = id;
		this.entries = new ArrayList<AuditTrailEntry>();
	}

	public void addEntries(List<AuditTrailEntry> toAdd) {
		entries.addAll(toAdd);
	}

	public void addEntry(AuditTrailEntry entry) {
		entries.add(entry);
	}

	/**
	 * Returns the entries.
	 * 
	 * @return the entries.
	 */
	public List<AuditTrailEntry> getEntries() {
		return entries;
	}

	/**
	 * Returns the id.
	 * 
	 * @return the id.
	 */
	public String getId() {
		return id;
	}

	public boolean hasAuditTrailEntries() {
		return !entries.isEmpty();
	}

	@Override
	public void setAttribute(Attribute attribute) {
		setAttribute(attribute.getName(), attribute.getContent());
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

}

package org.cheetahplatform.test.modeler;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;

public class TestLogListener implements ILogListener {
	private List<AuditTrailEntry> entries;

	public TestLogListener() {
		entries = new ArrayList<AuditTrailEntry>();
	}

	public List<AuditTrailEntry> getEntries() {
		return entries;
	}

	@Override
	public void log(AuditTrailEntry entry) {
		entries.add(entry);
	}

}
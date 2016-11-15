package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.IBMLayouter;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public abstract class AbstractLayoutStatistic implements IPpmStatistic {
	protected List<AuditTrailEntry> findLayoutEntries(List<AuditTrailEntry> entries) {
		List<AuditTrailEntry> layoutEntries = new ArrayList<AuditTrailEntry>();
		for (AuditTrailEntry auditTrailEntry : entries) {
			if (auditTrailEntry.isAttributeDefined(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME)) {
				if (IBMLayouter.LAYOUT.equals(auditTrailEntry.getAttribute(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME))) {
					if (auditTrailEntry.isAttributeDefined(CommonConstants.ATTRIBUTE_UNDO_EVENT)) {
						continue;
					}

					layoutEntries.add(auditTrailEntry);
				}
			}
		}
		return layoutEntries;
	}

	protected List<AuditTrailEntry> findUndoLayoutEntries(List<AuditTrailEntry> entries) {
		List<AuditTrailEntry> undoLayoutEntries = new ArrayList<AuditTrailEntry>();
		for (AuditTrailEntry auditTrailEntry : entries) {
			if (auditTrailEntry.isAttributeDefined(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME)) {
				if (IBMLayouter.LAYOUT.equals(auditTrailEntry.getAttribute(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME))) {
					if (!auditTrailEntry.isAttributeDefined(CommonConstants.ATTRIBUTE_UNDO_EVENT)) {
						continue;
					}

					undoLayoutEntries.add(auditTrailEntry);
				}
			}
		}
		return undoLayoutEntries;
	}

	protected DecimalFormat getDecimalFormat() {
		return new DecimalFormat("##0.00");
	}

	protected AuditTrailEntry getLastEntryOfLayout(List<AuditTrailEntry> entries, AuditTrailEntry layoutEntry) {
		int index = entries.indexOf(layoutEntry);
		List<AuditTrailEntry> subList = entries.subList(index, entries.size());

		int counter = 0;
		for (AuditTrailEntry auditTrailEntry : subList) {
			if (PromLogger.GROUP_EVENT_START.equals(auditTrailEntry.getEventType())) {
				counter++;
			}

			if (PromLogger.GROUP_EVENT_END.equals(auditTrailEntry.getEventType())) {
				counter--;
			}

			if (counter == 0) {
				return auditTrailEntry;
			}

			if (counter < 0) {
				throw new IllegalStateException("Counter < 0");
			}
		}
		throw new IllegalStateException("Missing closing audit trail entry for layout event");
	}

	protected long getStartTime(AuditTrailEntry entry) {
		if (entry.isAttributeDefined(AbstractGraphCommand.ADD_NODE_START_TIME)) {
			return entry.getLongAttribute(AbstractGraphCommand.ADD_NODE_START_TIME);
		}
		if (entry.isAttributeDefined(AbstractGraphCommand.RENAME_START_TIME)) {
			return entry.getLongAttribute(AbstractGraphCommand.RENAME_START_TIME);
		}

		return entry.getTimestamp().getTime();
	}

	protected List<Long> getTimesAfterLayout(List<AuditTrailEntry> entries) {
		List<Long> durationsAfterLayout = new ArrayList<Long>();

		for (AuditTrailEntry layoutEntry : findLayoutEntries(entries)) {
			AuditTrailEntry lastEntryOfAutomaticLayout = getLastEntryOfLayout(entries, layoutEntry);
			if (!PromLogger.GROUP_EVENT_END.equals(lastEntryOfAutomaticLayout.getEventType())) {
				throw new IllegalStateException("The last event of an automatic layout must be of tyle GROUP_EVENT_END, but was "
						+ lastEntryOfAutomaticLayout.getEventType());
			}

			int index = entries.indexOf(lastEntryOfAutomaticLayout) + 1;
			if (index >= entries.size()) {
				continue;
			}
			AuditTrailEntry entryAfterAutomaticLayout = entries.get(index);

			long durationAfterLayout = getStartTime(entryAfterAutomaticLayout) - lastEntryOfAutomaticLayout.getTimestamp().getTime();
			durationsAfterLayout.add(durationAfterLayout);
		}
		return durationsAfterLayout;
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_LAYOUT_STATISTICS);
	}

	protected boolean isLayoutEntry(AuditTrailEntry entry) {
		if (entry.isAttributeDefined(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME)) {
			if (IBMLayouter.LAYOUT.equals(entry.getAttribute(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME))) {
				if (!entry.isAttributeDefined(CommonConstants.ATTRIBUTE_UNDO_EVENT)) {
					return true;
				}
			}
		}
		return false;
	}
}
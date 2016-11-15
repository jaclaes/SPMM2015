package org.cheetahplatform.modeler.graph.export;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.db.DatabasePromReader;
import org.cheetahplatform.modeler.hierarchical.HierarchicalActivity;
import org.cheetahplatform.modeler.hierarchical.HierarchicalView;

public class SwitchAmountComputation extends AbstractExportComputation {
	@Override
	public List<Attribute> computeForAuditTrailEntry(AuditTrailEntry entry) {
		if (!entry.getEventType().equals(HierarchicalActivity.HIERARCHICAL)) {
			return Collections.emptyList();
		}

		String processInstanceId = entry.getAttribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE);
		try {
			long id = DatabasePromReader.getProcessInstanceDatabaseId(processInstanceId, Activator.getDatabaseConnector()
					.getDatabaseConnection());
			ProcessInstance processInstance = DatabasePromReader.readProcessInstance(id);

			// try to find out the name of the attribute
			String name = null;
			for (Attribute entryAttribute : entry.getAttributes()) {
				String nameCandidate = entryAttribute.getName();
				if (nameCandidate.endsWith("_True")) {
					name = nameCandidate.split("_")[0];
					break;
				}
			}

			List<Attribute> steps = new StepCounter().computeSteps(processInstance);
			for (Attribute attribute : steps) {
				if (attribute.getName().equals(HierarchicalView.INPUT_CHANGED)) {
					// ignore the initial switch the the root model (this one is logged, too)
					int switches = Integer.parseInt(attribute.getContent());
					switches--;
					return Arrays.asList(new Attribute[] { new Attribute(name + "_switches", switches) });
				}
			}

			return Arrays.asList(new Attribute[] { new Attribute(name + "_switches", "0") });
		} catch (SQLException e) {
			Activator.logError("Could not load a process instance.", e);
		}

		return Collections.emptyList();
	}
}

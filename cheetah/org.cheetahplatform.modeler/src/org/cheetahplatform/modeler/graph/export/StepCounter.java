package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;

public class StepCounter {

	/**
	 * Compute the amount of steps per type.
	 * 
	 * @param instance
	 *            the instance for which the step count should be computed
	 * @return the step count
	 */
	public List<Attribute> computeSteps(ProcessInstance instance) {
		Map<String, Integer> typeToCount = new HashMap<String, Integer>();

		for (AuditTrailEntry entry : instance.getEntries()) {
			String eventType = entry.getEventType();
			Integer count = typeToCount.get(eventType);
			if (count == null) {
				count = new Integer(1);
			} else {
				count++;
			}

			typeToCount.put(eventType, count);
		}

		List<Attribute> result = new ArrayList<Attribute>();
		for (Map.Entry<String, Integer> entry : typeToCount.entrySet()) {
			Attribute attribute = new Attribute(entry.getKey(), String.valueOf(entry.getValue()));
			result.add(attribute);
		}

		return result;
	}

}

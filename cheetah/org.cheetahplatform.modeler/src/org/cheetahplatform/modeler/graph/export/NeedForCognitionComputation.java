package org.cheetahplatform.modeler.graph.export;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.modeler.engine.configurations.NeedForCognitionQuestionnaire;

public class NeedForCognitionComputation extends AbstractExportComputation {
	@Override
	public List<Attribute> computeForAuditTrailEntry(AuditTrailEntry entry) {
		if (!isNeedForCognitionQuestionnaire(entry)) {
			return Collections.emptyList();
		}

		int result = new NeedForCognitionQuestionnaire().evaluate(entry.getAttributes());
		Attribute attribute = new Attribute("Need for Cognition Scale", result);
		return Arrays.asList(new Attribute[] { attribute });
	}

	private boolean isNeedForCognitionQuestionnaire(AuditTrailEntry entry) {
		if (!entry.getEventType().equals(SurveyActivity.TYPE_SURVEY)) {
			return false;
		}

		Set<String> attributes = entry.getAttributeNames();
		List<String> questions = new NeedForCognitionQuestionnaire().getQuestions();
		for (String question : questions) {
			boolean foundMatch = false;
			for (String attribute : attributes) {
				if (attribute.startsWith(question)) {
					foundMatch = true;
					break;
				}
			}

			if (!foundMatch) {
				return false;
			}
		}

		return true;
	}
}

package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.RadioLikertInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class LickertQuestionnaireItem {
	public static List<LikertScaleEntry> invertEvaluation(List<LikertScaleEntry> toInvert) {
		List<LikertScaleEntry> inverted = new ArrayList<LikertScaleEntry>();
		for (LikertScaleEntry current : toInvert) {
			inverted.add(new LikertScaleEntry(current.getName(), -current.getValue()));
		}

		return inverted;
	}

	private String question;
	private List<LikertScaleEntry> evaluation;

	public LickertQuestionnaireItem(String question, List<LikertScaleEntry> evaluation) {
		this.question = question;
		this.evaluation = evaluation;
	}

	public int evaluate(List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			String name = attribute.getName();
			if (!name.startsWith(question)) {
				continue; // another question
			}

			String remainder = name.substring(question.length());
			Assert.isTrue(remainder.charAt(0) == '_', "Possibly duplicate question (or two questions that share the same prefix): " + name);
			if (!attribute.getBooleanContent()) {
				continue; // user did not select this answer
			}

			String answer = remainder.substring(1, remainder.length());
			for (LikertScaleEntry entry : evaluation) {
				if (entry.getName().equals(answer)) {
					return entry.getValue();
				}
			}

			Assert.fail("An answer could not be evaluated: " + name);
		}

		return 0;
	}

	public String getQuestion() {
		return question;
	}

	public SurveyAttribute toComboSurveyAttribute() {
		ComboInputAttribute attribute = new ComboInputAttribute(question, true);
		for (LikertScaleEntry entry : evaluation) {
			attribute.addChoice(entry.getName());
		}

		return attribute;
	}

	public SurveyAttribute toRadioSurveyAttribute() {
		RadioLikertInputAttribute attribute = new RadioLikertInputAttribute(question, true);
		for (LikertScaleEntry entry : evaluation) {
			attribute.addChoice(entry);
		}

		return attribute;
	}

}

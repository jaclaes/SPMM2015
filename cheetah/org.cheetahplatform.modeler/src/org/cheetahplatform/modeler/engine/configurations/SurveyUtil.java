package org.cheetahplatform.modeler.engine.configurations;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class SurveyUtil {
	private static final List<LikertScaleEntry> SEVEN_POINT_LIKERT;

	static {
		List<LikertScaleEntry> sevenPointLikert = new ArrayList<LikertScaleEntry>();
		sevenPointLikert.add(new LikertScaleEntry(Messages.SurveyUtil_0, 3));
		sevenPointLikert.add(new LikertScaleEntry(Messages.SurveyUtil_1, 2));
		sevenPointLikert.add(new LikertScaleEntry(Messages.SurveyUtil_2, 1));
		sevenPointLikert.add(new LikertScaleEntry(Messages.SurveyUtil_3, 0));
		sevenPointLikert.add(new LikertScaleEntry(Messages.SurveyUtil_4, -1));
		sevenPointLikert.add(new LikertScaleEntry(Messages.SurveyUtil_5, -2));
		sevenPointLikert.add(new LikertScaleEntry(Messages.SurveyUtil_6, -3));

		SEVEN_POINT_LIKERT = Collections.unmodifiableList(sevenPointLikert);
	}

	public static void addMentalEffortChoices(ComboInputAttribute attribute) {
		attribute.addChoice(Messages.SurveyUtil_7);
		attribute.addChoice(Messages.SurveyUtil_8);
		attribute.addChoice(Messages.SurveyUtil_9);
		attribute.addChoice(Messages.SurveyUtil_10);
		attribute.addChoice(Messages.SurveyUtil_11);
		attribute.addChoice(Messages.SurveyUtil_12);
		attribute.addChoice(Messages.SurveyUtil_13);
	}

	public static void addSevenPointLikertScaleChoices(ComboInputAttribute combo) {
		for (LikertScaleEntry entry : SEVEN_POINT_LIKERT) {
			combo.addChoice(entry.getName());
		}
	}

	public static void addTamChoices(ComboInputAttribute attribute) {
		attribute.addChoice("Extremely Likely");
		attribute.addChoice("Quite Likely");
		attribute.addChoice("Slightly Likely");
		attribute.addChoice("Neither Likely nor Unlikely");
		attribute.addChoice("Slightly Unlikely");
		attribute.addChoice("Quite Unlikely");
		attribute.addChoice("Extremely Unlikely");
	}

	public static SurveyAttribute createLikertQuestion(String question, boolean mandatory) {
		ComboInputAttribute attribute = new ComboInputAttribute(question, mandatory);
		SurveyUtil.addSevenPointLikertScaleChoices(attribute);
		return attribute;
	}

	public static SurveyAttribute createMandatoryLikertQuestion(String question) {
		ComboInputAttribute attribute = new ComboInputAttribute(question, true);
		SurveyUtil.addSevenPointLikertScaleChoices(attribute);
		return attribute;
	}

	public static SurveyActivity createPerceivedEaseOfUseSurvey(String software) {
		return new PerceivedEaseOfUseQuestionnaire().createSurveyAttribute(software);
	}

	static SurveyAttribute createTamQuestion(String question, String software) {
		return createTamQuestion(question, software);
	}

	public static SurveyAttribute createTamQuestion(String question, String software, boolean mandatory) {
		ComboInputAttribute attribute = new ComboInputAttribute(MessageFormat.format(question, software), mandatory);
		addTamChoices(attribute);
		return attribute;
	}

	public static SurveyActivity createTamSurvey(String software) {
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();
		attributes.addAll(new PerceivedUsefulnessQuestionnaire().createQuestions(software, true));
		attributes.addAll(new PerceivedEaseOfUseQuestionnaire().createQuestions(software));
		return new SurveyActivity(attributes);
	}

	public static List<LikertScaleEntry> getSevenPointLikert() {
		return SEVEN_POINT_LIKERT;
	}

}

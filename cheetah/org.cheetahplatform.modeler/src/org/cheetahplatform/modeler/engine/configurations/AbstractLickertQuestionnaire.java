package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.survey.core.SectionHeaderAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;

public abstract class AbstractLickertQuestionnaire {
	protected List<LickertQuestionnaireItem> items;

	public AbstractLickertQuestionnaire() {
		items = new ArrayList<LickertQuestionnaireItem>();
		initializeQuestions();
	}

	protected void add(String question) {
		add(question, false);
	}

	protected void add(String question, boolean negate) {
		LickertQuestionnaireItem item = null;
		if (negate) {
			item = new LickertQuestionnaireItem(question, LickertQuestionnaireItem.invertEvaluation(getLikertScale()));
		} else {
			item = new LickertQuestionnaireItem(question, getLikertScale());
		}

		items.add(item);
	}

	protected SurveyAttribute createQuestionnaireItem(LickertQuestionnaireItem item) {
		return item.toComboSurveyAttribute();
	}

	protected SurveyActivity createSurveyActivity() {
		List<SurveyAttribute> questions = new ArrayList<SurveyAttribute>();
		String introduction = getIntroduction();
		if (introduction != null && !introduction.trim().isEmpty()) {
			questions.add(new SectionHeaderAttribute("Questionnaire_Introduction", introduction));
			// questions.add(new MessageSurveyAttribute("SPACE_1", ""));
		}
		for (LickertQuestionnaireItem item : items) {
			questions.add(createQuestionnaireItem(item));
		}

		SurveyActivity activity = new SurveyActivity(questions);
		return activity;
	}

	public int evaluate(List<Attribute> attributes) {
		int sum = 0;

		for (LickertQuestionnaireItem item : items) {
			sum += item.evaluate(attributes);
		}

		return sum;
	}

	public abstract String getIntroduction();

	protected abstract List<LikertScaleEntry> getLikertScale();

	public List<String> getQuestions() {
		List<String> questions = new ArrayList<String>();
		for (LickertQuestionnaireItem item : items) {
			questions.add(item.getQuestion());
		}

		return questions;
	}

	protected abstract void initializeQuestions();

}
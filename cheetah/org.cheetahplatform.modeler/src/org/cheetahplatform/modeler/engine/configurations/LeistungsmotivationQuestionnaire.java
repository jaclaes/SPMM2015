package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.LikertScaleEntry;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class LeistungsmotivationQuestionnaire extends AbstractLickertQuestionnaire {

	public static IExperimentalWorkflowActivity createActivity() {
		return new LeistungsmotivationQuestionnaire().createSurveyActivity();
	}

	@Override
	protected SurveyAttribute createQuestionnaireItem(LickertQuestionnaireItem item) {
		return item.toRadioSurveyAttribute();
	}

	@Override
	public String getIntroduction() {
		return null;
	}

	@Override
	protected List<LikertScaleEntry> getLikertScale() {
		List<LikertScaleEntry> values = new ArrayList<LikertScaleEntry>();

		values.add(new LikertScaleEntry("stimme gar nicht zu", 1));
		values.add(new LikertScaleEntry("stimme kaum zu", 2));
		values.add(new LikertScaleEntry("stimme eher zu", 3));
		values.add(new LikertScaleEntry("stimme voll zu", 4));

		return values;
	}

	@Override
	protected void initializeQuestions() {
		add("Ich mag Situationen, in denen ich feststellen kann, wie gut ich bin.");
		add("Wenn mir ein Problem gestellt wird, das ich vielleicht lösen kann, dann reizt es mich, damit sofort anzufangen.");
		add("Situationen, in denen ich von meinen Fähigkeiten Gebrauch machen kann, machen mir Spaß.");
		add("Mich reizen Situationen, in denen ich meine Fähigkeiten testen kann.");
		add("Ich fühle mich zu Arbeiten hingezogen, in denen ich die Möglichkeit habe, meine Fähigkeiten zu prüfen.");
		add("In etwas schwierigen Situationen, in denen viel von mir selbst abhängt, habe ich Angst zu versagen.");
		add("Es beunruhigt mich, etwas zu tun, wenn ich nicht sicher bin, dass ich es kann.");
		add("Arbeiten, die ich nicht schaffen kann, machen mir Angst, auch dann, wenn niemand meinen Misserfolg merkt.");
		add("Auch wenn niemand zuguckt, fühle ich mich in neuen Situationen ziemlich ängstlich.");
		add("Wenn ich ein Problem nicht sofort verstehe, werde ich ängstlich.");
	}
}

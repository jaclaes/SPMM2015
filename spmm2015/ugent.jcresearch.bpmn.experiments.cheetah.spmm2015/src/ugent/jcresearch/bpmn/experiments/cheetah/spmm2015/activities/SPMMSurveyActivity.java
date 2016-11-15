package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.survey.core.CheckBoxInputAttribute;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.IntegerInputAttribute;
import org.cheetahplatform.survey.core.MessageSurveyAttribute;
import org.cheetahplatform.survey.core.RadioButtonInputAttribute;
import org.cheetahplatform.survey.core.StringInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;


public class SPMMSurveyActivity extends SurveyActivity {

	public enum Part { PRE, POST };
	public SPMMSurveyActivity(Part part, String process) {
		super(getAttributes(part, process));
	}
	
	private static List<SurveyAttribute> getAttributes(Part part, String process) {
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();

		if (part == Part.PRE) { 
			showPersonaliaQuestions(attributes); // 4 questions, 5 slots
			showEnglish01(attributes);
			showEnglish02(attributes);
			
			int series = (int)(Math.random() * 6) + 1;
			switch (series) {
				case 1: showPriorKnowledge(attributes); // 12 questions, 14 slots
					    showLearningStyle(attributes); // 11 questions, 14 slots
				        showNeedForStructure(attributes); // 12 questions, 14 slots
				        break;
				case 2: showPriorKnowledge(attributes); // 12 questions, 14 slots
		                showNeedForStructure(attributes); // 12 questions, 14 slots
		                showLearningStyle(attributes); // 11 questions, 14 slots
		                break;
				case 3: showLearningStyle(attributes); // 11 questions, 14 slots
			            showPriorKnowledge(attributes); // 12 questions, 14 slots
			            showNeedForStructure(attributes); // 12 questions, 14 slots
			            break;
				case 4: showLearningStyle(attributes); // 11 questions, 14 slots
		                showNeedForStructure(attributes); // 12 questions, 14 slots
		                showPriorKnowledge(attributes); // 12 questions, 14 slots
	                    break;
				case 5: showNeedForStructure(attributes); // 12 questions, 14 slots
                        showLearningStyle(attributes); // 11 questions, 14 slots
	                    showPriorKnowledge(attributes); // 12 questions, 14 slots
	                    break;
				case 6: showNeedForStructure(attributes); // 12 questions, 14 slots
	                    showPriorKnowledge(attributes); // 12 questions, 14 slots
                        showLearningStyle(attributes); // 11 questions, 14 slots
	                    break;
			}
		}
		else {
			showCompletion01(attributes);
			showCompletion0203(attributes);
	        attributes.add(new MessageSurveyAttribute("PlaceHolder01", ""));
	        attributes.add(new MessageSurveyAttribute("PlaceHolder02", ""));
	        attributes.add(new MessageSurveyAttribute("PlaceHolder03", ""));
	        attributes.add(new MessageSurveyAttribute("PlaceHolder04", ""));
			
			int series = (int)(Math.random() * 2) + 1;
			switch (series) {
				case 1: showCognitiveLoad(attributes, process); // 10 questions, 14 slots
				        showCaseDescription(attributes); // 5 questions, 7 slots
				        break;
				case 2: showCaseDescription(attributes); // 5 questions, 7 slots
		                showCognitiveLoad(attributes, process); // 10 questions, 14 slots
		                break;
			}
			//showYesNoRadioQuestion(attributes, "Would you like to particpate in a 20 minute on campus interview about this experiment within two weeks?");
		}
		
		return attributes;
	}
		
	//Need for Structure Scale
	// from http://highered.mcgraw-hill.com/sites/0070876940/student_view0/chapter3/activity_3_5.html
	// from M. M. Thompson, M. E. Naccarato, and K. E. Parker, 
	//      Assessing Cognitive Need: The Development of the Personal Need for Structure and the Personal fear of Invalidity Scales
	//      Paper presented at the Annual meeting of the Canadian Psychological Association, Halifax, Nova Scotia (1989).
	private static void showNeedForStructure(List<SurveyAttribute> attributes) {
		attributes.add(new MessageSurveyAttribute("SHOW_MESSAGE_NFS", "Please indicate to which extend you agree with the following statements."));
		
		int series = (int)(Math.random() * 3) + 1;
		switch (series) {
			case 1: showNFS03(attributes); showNFS12(attributes); showNFS10(attributes); showNFS09(attributes); showNFS05(attributes); showNFS01(attributes); 
			        showNFS02(attributes); showNFS07(attributes); showNFS08(attributes); showNFS04(attributes); showNFS06(attributes); showNFS11(attributes);  
			        break;
			case 2: showNFS11(attributes); showNFS12(attributes); showNFS02(attributes); showNFS01(attributes); showNFS10(attributes); showNFS06(attributes); 
	                showNFS04(attributes); showNFS07(attributes); showNFS03(attributes); showNFS08(attributes); showNFS05(attributes); showNFS09(attributes);  
	                break;
			case 3: showNFS05(attributes); showNFS03(attributes); showNFS08(attributes); showNFS09(attributes); showNFS06(attributes); showNFS10(attributes); 
	                showNFS02(attributes); showNFS12(attributes); showNFS01(attributes); showNFS07(attributes); showNFS04(attributes); showNFS11(attributes);  
	                break;
			default: break;
		}
		
        attributes.add(new MessageSurveyAttribute("PlaceHolderNFS01", ""));
	}
	private static void showNFS01(List<SurveyAttribute> attributes) { showLikert2(attributes, "It upsets me to go into a situation without knowing what I can expect from it."); }
	private static void showNFS02(List<SurveyAttribute> attributes) { showLikert2(attributes, "I'm not bothered by things that interrupt my daily routine."); }
	private static void showNFS03(List<SurveyAttribute> attributes) { showLikert2(attributes, "I enjoy being spontaneous."); }
	private static void showNFS04(List<SurveyAttribute> attributes) { showLikert2(attributes, "I find that a well-ordered life with regular hours makes my life boring."); }
	private static void showNFS05(List<SurveyAttribute> attributes) { showLikert2(attributes, "I find that a consistent routine enables me to enjoy life more."); }
	private static void showNFS06(List<SurveyAttribute> attributes) { showLikert2(attributes, "I enjoy having a clear and structured mode of life."); }
	private static void showNFS07(List<SurveyAttribute> attributes) { showLikert2(attributes, "I like to have a place for everything and everything in its place."); }
	private static void showNFS08(List<SurveyAttribute> attributes) { showLikert2(attributes, "I don't like situations that are uncertain."); }
	private static void showNFS09(List<SurveyAttribute> attributes) { showLikert2(attributes, "I hate to change my plans at the last minute."); }
	private static void showNFS10(List<SurveyAttribute> attributes) { showLikert2(attributes, "I hate to be with people who are unpredictable."); }
	private static void showNFS11(List<SurveyAttribute> attributes) { showLikert2(attributes, "I enjoy the excitement of being in unpredictable situations."); }
	private static void showNFS12(List<SurveyAttribute> attributes) { showLikert2(attributes, "I become uncomfortable when the rules in a situation are not clear."); }
	


	//Task completion
	private static void showCompletion01(List<SurveyAttribute> attributes) { showYesNoRadioQuestion(attributes, "Have you completed the assignment?"); }
	private static void showCompletion0203(List<SurveyAttribute> attributes) { showYesNoRadioQuestion(attributes, "Have you experienced any disturbances during the execution of the modeling task?");
																			 attributes.add(new StringInputAttribute("If yes, which (kind of) disturbances have you experienced?", false)); }

	private static void showPriorKnowledge(List<SurveyAttribute> attributes) {
		attributes.add(new MessageSurveyAttribute("SHOW_MESSAGE_KNOW", "Please indicate to which extend you agree with the following statements."));
		showCase01(attributes);
		showCase02(attributes);
		showCase03(attributes);	
		showCase04(attributes);	
		showCase05(attributes);	
		showCase06(attributes);
		
		showBPMN01(attributes);
		showBPMN02(attributes);
		showBPMN03(attributes);
		
		showExperience01(attributes);
		showExperience02(attributes);
		showExperience0304(attributes);
	}
	//Case familiarity
	private static void showCase01(List<SurveyAttribute> attributes) { showLikert1(attributes, "I am familiar with a mortgage request process. (dutch: mortgage = hypotheek)"); }
	private static void showCase02(List<SurveyAttribute> attributes) { showLikert1(attributes, "I am familiar with a defaulter handling process. (dutch: defaulter = wanbetaler)"); }
	private static void showCase03(List<SurveyAttribute> attributes) { showLikert1(attributes, "I am familiar with a visa control process. (dutch: visa = visum)"); }
	private static void showCase04(List<SurveyAttribute> attributes) { showLikert1(attributes, "I have created process models for a mortgage request before. (dutch: mortgage = hypotheek)"); }
	private static void showCase05(List<SurveyAttribute> attributes) { showLikert1(attributes, "I have created process models for defaulter handling before. (dutch: defaulter = wanbetaler)"); }
	private static void showCase06(List<SurveyAttribute> attributes) { showLikert1(attributes, "I have created process models for a visa control before. (dutch: visa = visum)"); }
	//BPMN familiarity
	private static void showBPMN01(List<SurveyAttribute> attributes) { showLikert1(attributes,"Overall, I am familiar with the BPMN modeling language."); }
	private static void showBPMN02(List<SurveyAttribute> attributes) { showLikert1(attributes,"I feel confident in understanding process models created with the BPMN modeling language."); }
	private static void showBPMN03(List<SurveyAttribute> attributes) { showLikert1(attributes,"I feel competent in using the BPMN modeling language for process modeling."); }
	//Modeling experience
	private static void showExperience01(List<SurveyAttribute> attributes) { showLikert1(attributes, "I consider myself being a process modeling expert."); }
	private static void showExperience02(List<SurveyAttribute> attributes) { showLikert1(attributes, "I don't have much experience in process modeling."); }
	private static void showExperience0304(List<SurveyAttribute> attributes) { CheckBoxInputAttribute modelingLanguagesCheckbox = new CheckBoxInputAttribute(
				                                                                                             "Which process modeling languages / tools have you used before?");
																			 modelingLanguagesCheckbox.addChoice("Aris (express)");
																			 modelingLanguagesCheckbox.addChoice("BPEL");
																			 modelingLanguagesCheckbox.addChoice("BPMN");
																			 modelingLanguagesCheckbox.addChoice("BPM|one,");
																			 modelingLanguagesCheckbox.addChoice("Petri Nets / Colored Petri Nets / CPN Tools");
																			 modelingLanguagesCheckbox.addChoice("Tibco / COSA");
																			 modelingLanguagesCheckbox.addChoice("Workflow Nets / WoPeD");
																			 modelingLanguagesCheckbox.addChoice("Other");
																			 attributes.add(modelingLanguagesCheckbox);
																			 attributes.add(new StringInputAttribute("If you have selected 'Other' please specify which modeling languages.", false)); }
	
	//Cognitive load
	private static void showCognitiveLoad(List<SurveyAttribute> attributes, String process) {
		attributes.add(new MessageSurveyAttribute("SHOW_MESSAGE_LOAD", "All of the following questions refer to the activity process model construction activity that just finished. "
				+ "Please respond to each of the questions on the following scale (0 meaning not at all the case and 10 meaning completely the case)."));
		
		int series = (int)(Math.random() * 3) + 1;
		switch (series) {
			case 1: showLoad01(attributes, process); showLoad07(attributes, process); showLoad04(attributes, process); showLoad02(attributes, process); showLoad08(attributes, process); 
			        attributes.add(new MessageSurveyAttribute("PlaceHolderLoad01", ""));
			        showLoad05(attributes, process); showLoad03(attributes, process); showLoad09(attributes, process); showLoad06(attributes, process); showLoad10(attributes, process); 
			        break;
			case 2: showLoad06(attributes, process); showLoad10(attributes, process); showLoad09(attributes, process); showLoad03(attributes, process); showLoad05(attributes, process); 
			        attributes.add(new MessageSurveyAttribute("PlaceHolderLoad02", ""));
			        showLoad08(attributes, process); showLoad02(attributes, process); showLoad07(attributes, process); showLoad01(attributes, process); showLoad04(attributes, process); 
	                break;
			case 3: showLoad09(attributes, process); showLoad03(attributes, process); showLoad06(attributes, process); showLoad08(attributes, process); showLoad02(attributes, process);
			        attributes.add(new MessageSurveyAttribute("PlaceHolderLoad03", ""));
	                showLoad04(attributes, process); showLoad10(attributes, process); showLoad05(attributes, process); showLoad07(attributes, process); showLoad01(attributes, process); 
	                break;
			default: break;
		}
		
        attributes.add(new MessageSurveyAttribute("PlaceHolderLoad04", ""));
        attributes.add(new MessageSurveyAttribute("PlaceHolderLoad05", ""));
	}
	private static void showLoad01(List<SurveyAttribute> attributes, String process) { showScale10(attributes, "The process to be modeled was very complex."); }
	private static void showLoad02(List<SurveyAttribute> attributes, String process) { showScale10(attributes, "The process to be modeled covered relations between the steps of the " + process + " process that I perceived as very complex."); }
	private static void showLoad03(List<SurveyAttribute> attributes, String process) { showScale10(attributes, "The process to be modeled covered concepts and definitions that I perceived as very complex."); }
	private static void showLoad04(List<SurveyAttribute> attributes, String process) { showScale10(attributes, "The instructions and/or explanations during the activity were very unclear."); }
	private static void showLoad05(List<SurveyAttribute> attributes, String process) { showScale10(attributes, "The instructions and/or explanations were, in terms of understanding, very ineffective."); }
	private static void showLoad06(List<SurveyAttribute> attributes, String process) { showScale10(attributes, "The instructions and/or explanations were full of unclear language."); }
	private static void showLoad07(List<SurveyAttribute> attributes, String process) { showScale10(attributes, "The modeling activity really enhanced my understanding of the " + process + " process."); }
	private static void showLoad08(List<SurveyAttribute> attributes, String process) { showScale10(attributes, "The modeling activity really enhanced my knowledge and understanding of process modeling."); }
	private static void showLoad09(List<SurveyAttribute> attributes, String process) { showScale10(attributes, "The modeling activity really enhanced my understanding of the relations between the steps of the " + process + " process."); }
	private static void showLoad10(List<SurveyAttribute> attributes, String process) { showScale10(attributes, "The modeling activity really enhanced my understanding of concepts and definitions of the " + process + " process."); }


	//Perception of Case description
	private static void showCaseDescription(List<SurveyAttribute> attributes) {
		attributes.add(new MessageSurveyAttribute("SHOW_MESSAGE_DESCR", "Please indicate to which extend you agree with the following statements."));
		
		int series = (int)(Math.random() * 3) + 1;
		switch (series) {
			case 1: showCaseDescr03(attributes); showCaseDescr05(attributes); showCaseDescr02(attributes); showCaseDescr01(attributes); showCaseDescr04(attributes); 
			        break;
			case 2: showCaseDescr04(attributes); showCaseDescr03(attributes); showCaseDescr01(attributes); showCaseDescr02(attributes); showCaseDescr05(attributes); 
			        break;
			case 3: showCaseDescr02(attributes); showCaseDescr01(attributes); showCaseDescr05(attributes); showCaseDescr04(attributes); showCaseDescr03(attributes);
			        break;
		}

        attributes.add(new MessageSurveyAttribute("PlaceHolderDescr01", ""));
	}
	public static void showCaseDescr01(List<SurveyAttribute> attributes) { showLikert1(attributes, "I have experienced language difficulties when reading and understanding the case description."); }
	private static void showCaseDescr02(List<SurveyAttribute> attributes) { showLikert1(attributes, "I found the case description easy to understand."); }
	private static void showCaseDescr03(List<SurveyAttribute> attributes) { showLikert1(attributes, "I found the case description complex and difficult to follow."); }
	private static void showCaseDescr04(List<SurveyAttribute> attributes) { showLikert1(attributes, "The case description was ambiguous."); }
	private static void showCaseDescr05(List<SurveyAttribute> attributes) { showLikert1(attributes, "The case was clearly described."); }
	
	//Learning style
	private static void showLearningStyle(List<SurveyAttribute> attributes) {
		attributes.add(new MessageSurveyAttribute("SHOW_MESSAGE_STYLE", "For each of the 11 questions below select either 'a' or 'b' to indicate your answer. "
				+ "Please choose only one answer for each question. If both 'a' and 'b' seem to apply to you, choose the one that applies more frequently."));
		
		int series = (int)(Math.random() * 3) + 1;
		switch (series) {

			case 1: showStyle09(attributes); showStyle08(attributes); showStyle11(attributes); showStyle01(attributes); showStyle05(attributes); 
			        attributes.add(new MessageSurveyAttribute("PlaceHolderStyle01", ""));
			        showStyle03(attributes); showStyle02(attributes); showStyle04(attributes); showStyle06(attributes); showStyle07(attributes); showStyle10(attributes);  
			        break;
			case 2: showStyle09(attributes); showStyle02(attributes); showStyle07(attributes); showStyle05(attributes); showStyle10(attributes); 
	                attributes.add(new MessageSurveyAttribute("PlaceHolderStyle01", ""));
	                showStyle01(attributes); showStyle08(attributes); showStyle03(attributes); showStyle11(attributes); showStyle06(attributes); showStyle04(attributes);  
	                break;
			case 3: showStyle11(attributes); showStyle10(attributes); showStyle05(attributes); showStyle09(attributes); showStyle06(attributes); 
	                attributes.add(new MessageSurveyAttribute("PlaceHolderStyle01", ""));
	                showStyle03(attributes); showStyle08(attributes); showStyle07(attributes); showStyle04(attributes); showStyle02(attributes); showStyle01(attributes);  
	                break;
			default: break;
		}
		
        attributes.add(new MessageSurveyAttribute("PlaceHolderStyle04", ""));
	}
	private static void showStyle01(List<SurveyAttribute> attributes) { showRadioQuestion(attributes, "I tend to", "(a) understand details of a subject but may be fuzzy about its overall structure.", "(b) understand the overall structure but may be fuzzy about details."); }
	private static void showStyle02(List<SurveyAttribute> attributes) { showRadioQuestion(attributes, "Once I understand", "(a) all the parts, I understand the whole thing.", "(b) the whole thing, I see how the parts fit."); }
	private static void showStyle03(List<SurveyAttribute> attributes) { showRadioQuestion(attributes, "When I solve math problems", "(a) I usually work my way to the solutions one step at a time.", "(b) I often just see the solutions but then have to struggle to figure out the steps to get to them."); }
	private static void showStyle04(List<SurveyAttribute> attributes) { showRadioQuestion(attributes, "When I'm analyzing a story or a novel", "(a) I think of the incidents and try to put them together to figure out the themes.", "(b) I just know what the themes are when I finish reading and then I have to go back and find the incidents that demonstrate them."); }
	private static void showStyle05(List<SurveyAttribute> attributes) { showRadioQuestion(attributes, "When I start a homework problem, I am more likely to", "(a) start working on the solution immediately.", "(b) try to fully understand the problem first."); }
	private static void showStyle06(List<SurveyAttribute> attributes) { showRadioQuestion(attributes, "It is more important to me that an instructor", "(a) lay out the material in clear sequential steps.", "(b) give me an overall picture and relate the material to other subjects."); }
	private static void showStyle07(List<SurveyAttribute> attributes) { showRadioQuestion(attributes, "I learn", "(a) at a fairly regular pace. If I study hard, I'll 'get it.'", "(b) in fits and starts. I'll be totally confused and then suddenly it all 'clicks.'"); }
	private static void showStyle08(List<SurveyAttribute> attributes) { showRadioQuestion(attributes, "When considering a body of information, I am more likely to", "(a) focus on details and miss the big picture.", "(b) try to understand the big picture before getting into the details."); }
	private static void showStyle09(List<SurveyAttribute> attributes) { showRadioQuestion(attributes, "When writing a paper, I am more likely to", "(a) work on (think about or write) the beginning of the paper and progress forward.", "(b) work on (think about or write) different parts of the paper and then order them."); }
	private static void showStyle10(List<SurveyAttribute> attributes) { showRadioQuestion(attributes, "Some teachers start their lectures with an outline of what they will cover. Such outlines are", "(a) somewhat helpful to me.", "(b) very helpful to me."); }
	private static void showStyle11(List<SurveyAttribute> attributes) { showRadioQuestion(attributes, "When solving problems in a group, I would be more likely to", "(a) think of the steps in the solution process.", "(b) think of possible consequences or applications of the solution in a wide range of areas."); }

	private static void showPersonaliaQuestions(List<SurveyAttribute> attributes) {
		ComboInputAttribute genderAttribute = new ComboInputAttribute("What is your gender?", true);
		genderAttribute.addChoice("Male");
		genderAttribute.addChoice("Female");
		attributes.add(genderAttribute);
		
		CheckBoxInputAttribute eduationProgrammCombo = new CheckBoxInputAttribute("Which education program are you following?");
		eduationProgrammCombo.addChoice("HIR - Financiering");
		eduationProgrammCombo.addChoice("HIR - Marketing engineering");
		eduationProgrammCombo.addChoice("HIR - Operationeel management");
		eduationProgrammCombo.addChoice("Other");
		attributes.add(eduationProgrammCombo);
		attributes.add(new StringInputAttribute("If you have selected 'Other' please specify which education program.", false));

		attributes.add(new StringInputAttribute("What is your native language?", true));

		attributes.add(new IntegerInputAttribute("How old are you?", true, 10, 99));
	}
	
	//Understanding of English
	private static void showEnglish01(List<SurveyAttribute> attributes) { showLikert1(attributes, "I have troubles reading English texts."); }
	private static void showEnglish02(List<SurveyAttribute> attributes) { showLikert1(attributes, "I have troubles understanding English text."); }
	
	private static void showPositiveInteger(List<SurveyAttribute> attributes, String name) {
		attributes.add(new IntegerInputAttribute(name,true, 0, Integer.MAX_VALUE));
	}

	public static void showYesNoRadioQuestion(List<SurveyAttribute> attributes, String text) {
		RadioButtonInputAttribute completedRadiotButton = new RadioButtonInputAttribute(text, true);
		completedRadiotButton.addChoice("Yes");
		completedRadiotButton.addChoice("No");
		attributes.add(completedRadiotButton);
	}
	
	public static void showRadioQuestion(List<SurveyAttribute> attributes, String text, String option1, String option2) {
		RadioButtonInputAttribute completedRadiotButton = new RadioButtonInputAttribute(text, true);
		completedRadiotButton.addChoice(option1);
		completedRadiotButton.addChoice(option2);
		attributes.add(completedRadiotButton);
	}

	public static void showLikert1(List<SurveyAttribute> surveyAttributes, String name) {
		ComboInputAttribute combo = new ComboInputAttribute(name, true);
		combo.addChoice("strongly agree");
		combo.addChoice("agree");
		combo.addChoice("somewhat agree");
		combo.addChoice("neutral");
		combo.addChoice("somewhat disagree");
		combo.addChoice("disagree");
		combo.addChoice("strongly disagree");
		surveyAttributes.add(combo);
	}
	public static void showLikert2(List<SurveyAttribute> surveyAttributes, String name) {
		ComboInputAttribute combo = new ComboInputAttribute(name, true);
		combo.addChoice("strongly agree");
		combo.addChoice("moderately agree");
		combo.addChoice("slightly agree");
		combo.addChoice("slightly disagree");
		combo.addChoice("moderately disagree");
		combo.addChoice("strongly disagree");
		surveyAttributes.add(combo);
	}

	private static void showScale10(List<SurveyAttribute> surveyAttributes, String name) {
		ComboInputAttribute combo = new ComboInputAttribute(name, true);
		combo.addChoice("0");
		combo.addChoice("1");
		combo.addChoice("2");
		combo.addChoice("3");
		combo.addChoice("4");
		combo.addChoice("5");
		combo.addChoice("6");
		combo.addChoice("7");
		combo.addChoice("8");
		combo.addChoice("9");
		combo.addChoice("10");
		combo.setVisibleItemCount(11);
		surveyAttributes.add(combo);
	}
}
	
	
	
	/*
	  	private IExperimentalWorkflowActivity createSurveyActivity() {
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();

		showNeedForStructureQuestions(attributes); // 12 questions, 14 slots
		showLearningStyleQuestions(attributes); //2 questions, 14 slots

		showBPMNFamiliarityQuestions(attributes); // 4 questions, 4 slots
		showCaseFamiliarityQuestions(attributes); // 2 questions, 3 slots
		
		showExperienceQuestions(attributes); // 10 questions, 15 slots
		showCompletionQuestions(attributes); // 3 questions, 3 slots
		showMentalEffortQuestions(attributes); // 1 question, 3 slots
		
		showPersonaliaQuestions(attributes); // 4 questions, 7 slots
		showUnderstandingOfEnglishQuestions(attributes); // 2 questions, 2 slots
		showPerceptionOfCaseDescriptionQuestions(attributes); // 5 questions, 5 slots
		
		return new SurveyActivity(attributes);
	}
	private void showNeedForStructureQuestions(List<SurveyAttribute> attributes) {
		// from http://highered.mcgraw-hill.com/sites/0070876940/student_view0/chapter3/activity_3_5.html
		// from M. M. Thompson, M. E. Naccarato, and K. E. Parker, 
		//      Assessing Cognitive Need: The Development of the Personal Need for Structure and the Personal fear of Invalidity Scales
		//      Paper presented at the Annual meeting of the Canadian Psychological Association, Halifax, Nova Scotia (1989).
		
		showLikert2(attributes, "It upsets me to go into a situation without knowing what I can expect from it.");
		showLikert2(attributes, "I'm not bothered by things that interrupt my daily routine.");
		showLikert2(attributes, "I enjoy being spontaneous.");
		showLikert2(attributes, "I find that a well-ordered life with regular hours makes my life boring.");
		showLikert2(attributes, "I find that a consistent routine enables me to enjoy life more.");
		showLikert2(attributes, "I enjoy having a clear and structured mode of life.");
		attributes.add(new MessageSurveyAttribute("PlaceHolder1", ""));
		showLikert2(attributes, "I like to have a place for everything and everything in its place.");
		showLikert2(attributes, "I don't like situations that are uncertain.");
		showLikert2(attributes, "I hate to change my plans at the last minute.");
		showLikert2(attributes, "I hate to be with people who are unpredictable.");
		showLikert2(attributes, "I enjoy the excitement of being in unpredictable situations.");
		showLikert2(attributes, "I become uncomfortable when the rules in a situation are not clear.");
		attributes.add(new MessageSurveyAttribute("PlaceHolder2", ""));
	}
	
	private void showBPMNFamiliarityQuestions(List<SurveyAttribute> attributes) {
		showPositiveInteger(attributes, "How many months ago did you start using BPMN? (The first version of BPMN stems from May 2004, i.e. 60 months until May 2009) ");

		showLikert1(attributes,"Overall, I am familiar with the BPMN modeling language.");
		showLikert1(attributes,"I feel confident in understanding process models created with the BPMN modeling language.");
		showLikert1(attributes,"I feel competent in using the BPMN modeling language for process modeling.");
	}

	private void showCaseFamiliarityQuestions(List<SurveyAttribute> attributes) {
		showLikert1(attributes, "I am familiar with a defaulter handling process.");
		showLikert1(attributes, "I have created process models for a defaulter handling before.");
		attributes.add(new MessageSurveyAttribute("PlaceHolder13", ""));
	}

	private void showCompletionQuestions(List<SurveyAttribute> attributes) {
		showYesNoRadioQuestion(attributes, "Have you completed the assignment?");
		showYesNoRadioQuestion(attributes, "Have you experienced any disturbances during the execution of the modeling task?");
		attributes.add(new StringInputAttribute("If yes, which (kind of) disturbances have you experienced?", false));
	}
	
	private void showExperienceQuestions(List<SurveyAttribute> attributes) {
		showPositiveInteger(attributes, "How many years ago did you start process modeling?");
		showPositiveInteger(attributes, "How many process models have you analyzed or read within the last 12 months? (A year has about 250 work days. In case you read one model per day, this would sum up to 250 models per year)");
		showPositiveInteger(attributes, "How many process models have you created or edited within the last 12 months?");
		showPositiveInteger(attributes, "How many activities did all these models have on average?");
		showPositiveInteger(attributes, "How many work days of formal training on process modeling have you received within the last 12 months? (This includes e.g. university lectures, certification courses, training courses. 15 weeks of a 90 minutes university lecture is roughly 3 work days)");
		showPositiveInteger(attributes, "How many work days of self education have you made within the last 12 months? (This includes e.g. learning-by-doing, learning-on-the-fly, self-study of textbooks or specifications)");
		
		attributes.add(new MessageSurveyAttribute("PlaceHolder14", ""));

		CheckBoxInputAttribute modelingLanguagesCheckbox = new CheckBoxInputAttribute(
				"Which process modeling languages / tools have you used before?");
		modelingLanguagesCheckbox.addChoice("Aris (express)");
		modelingLanguagesCheckbox.addChoice("BPEL");
		modelingLanguagesCheckbox.addChoice("BPMN");
		modelingLanguagesCheckbox.addChoice("BPM|one,");
		modelingLanguagesCheckbox.addChoice("Petri Nets / Colored Petri Nets / CPN Tools");
		modelingLanguagesCheckbox.addChoice("Tibco / COSA");
		modelingLanguagesCheckbox.addChoice("Workflow Nets / WoPeD");
		modelingLanguagesCheckbox.addChoice("Other");
		attributes.add(modelingLanguagesCheckbox);

		attributes.add(new StringInputAttribute("If you have selected 'Other' please specify which modeling languages.", false));
		attributes.add(new MessageSurveyAttribute("PlaceHolder15", ""));
		attributes.add(new MessageSurveyAttribute("PlaceHolder16", ""));
		attributes.add(new MessageSurveyAttribute("PlaceHolder17", ""));
		attributes.add(new MessageSurveyAttribute("PlaceHolder18", ""));
		attributes.add(new MessageSurveyAttribute("PlaceHolder19", ""));

		attributes
				.add(new MessageSurveyAttribute("SHOW_MESSAGE", "Please indicate to which extend you agree with the following statements."));

		showLikert1(attributes, "I consider myself being a process modeling expert.");
	}
	
	private void showMentalEffortQuestions(List<SurveyAttribute> attributes) {
		ComboInputAttribute mentalEffortCombo = new ComboInputAttribute(
				"How would you assess the mental effort for completing the modeling task?", true);
		showMentalEffortChoices(mentalEffortCombo);
		attributes.add(mentalEffortCombo);
		attributes.add(new MessageSurveyAttribute("PlaceHolder20", ""));
	}

	public void showPerceptionOfCaseDescriptionQuestions(List<SurveyAttribute> attributes) {
		showLikert1(attributes, "I have experienced language difficulties when reading and understanding the case description.");
		showLikert1(attributes, "I found the case description easy to understand.");
		showLikert1(attributes, "I found the case description complex and difficult to follow.");
		showLikert1(attributes, "The case description was ambiguous.");
		showLikert1(attributes, "The case was clearly described.");
	}
	
	private void showLearningStyleQuestions(List<SurveyAttribute> attributes) {
		CheckBoxInputAttribute textCombo = new CheckBoxInputAttribute("Which of the following statements applies to you the most?");
		textCombo.addChoice(
				"When I write a text, I typically make sure that every detail is right from the beginning (I spend \n"
				+ "attention to formatting, lay-out, pictures, etc. during the writing process)");
		textCombo.addChoice(
				"When I write a text, I typically first focus on the content table, then I write each paragraph and only \n"
				+ "afterwards I consider formatting, lay-out, pictures, etc.");
		textCombo.addChoice(
				"When I write a text, I typically alternate between phases of adding content and phases of formatting.");
		textCombo.addChoice(
				"When I write a text, I typically try to first write as much text as possible, while making sure the formatting \n"
				+ "and lay-out is good to start with. Then I reconsider everything I have written so far and make only \n"
				+ "minor changes to formatting and lay-out.");
		textCombo.addChoice(
				"When I write a text, it is very possible that I change something about the lay-out of a piece of text, \n"
				+ "in the middle of writing a paragraph in another chapter, and while I think of it, \n"
				+ "I might be inserting a picture elsewhere and capitalize some titles that I come across.");
		textCombo.addChoice("Other");
		attributes.add(textCombo);
		attributes.add(new StringInputAttribute("If you have selected 'Other' please specify how you typically write a text.", false));
		attributes.add(new MessageSurveyAttribute("PlaceHolder3", ""));
		attributes.add(new MessageSurveyAttribute("PlaceHolder4", ""));
		attributes.add(new MessageSurveyAttribute("PlaceHolder5", ""));
		attributes.add(new MessageSurveyAttribute("PlaceHolder6", ""));
		attributes.add(new MessageSurveyAttribute("PlaceHolder7", ""));

		CheckBoxInputAttribute syllabusCombo = new CheckBoxInputAttribute("Which of the following statements applies to you most?");
		syllabusCombo.addChoice(
				"When I study a syllabus, I typically start with examining the content table and first familiarize myself \n"
				+ "with the contents of the syllabus, then I work on the theoretical material and I finish with exercises or difficult \n"
				+ "parts that I skipped before.");
		syllabusCombo.addChoice(
				"When I study a syllabus, I typically study chapter by chapter from start to finish.");
		syllabusCombo.addChoice(
				"When I study a syllabus, I typically start with the most simple/complex chapters and work my way through \n"
				+ "the chapters of the syllabus in an order of increasing/decreasing complexity.");
		syllabusCombo.addChoice(
				"When I study a syllabus, it is very possible that I am trying to understand a certain theory and, \n"
				+ "while realizing that I do not have the solution to a certain exercise, I first try to find the solution \n"
				+ "for it (e.g., by texting a class mate). Before I return to the piece of theory I was studying, I might even \n"
				+ "first make an overview of how many pages I have to study in each chapter, etc.");
		syllabusCombo.addChoice("Other");
		attributes.add(syllabusCombo);
		attributes.add(new StringInputAttribute("If you have selected 'Other' please specify how you typically study a syllabus.", false));
		attributes.add(new MessageSurveyAttribute("PlaceHolder8", ""));
		attributes.add(new MessageSurveyAttribute("PlaceHolder9", ""));
		attributes.add(new MessageSurveyAttribute("PlaceHolder10", ""));
		attributes.add(new MessageSurveyAttribute("PlaceHolder11", ""));
		attributes.add(new MessageSurveyAttribute("PlaceHolder12", ""));
	}

	private void showPersonaliaQuestions(List<SurveyAttribute> attributes) {
		ComboInputAttribute genderAttribute = new ComboInputAttribute("What is your gender?", true);
		genderAttribute.addChoice("Male");
		genderAttribute.addChoice("Female");
		attributes.add(genderAttribute);
		
		CheckBoxInputAttribute eduationProgrammCombo = new CheckBoxInputAttribute("Which education program are you following?");
		eduationProgrammCombo.addChoice("OML");
		eduationProgrammCombo.addChoice("BIS");
		eduationProgrammCombo.addChoice("IM");
		eduationProgrammCombo.addChoice("CSE");
		eduationProgrammCombo.addChoice("Other");
		attributes.add(eduationProgrammCombo);
		attributes.add(new StringInputAttribute("If you have selected 'Other' please specify which education program.", false));

		attributes.add(new StringInputAttribute("What is your native language?", true));

		attributes.add(new IntegerInputAttribute("How old are you?", true, 10, 99));
		attributes.add(new MessageSurveyAttribute("PlaceHolder31", ""));
		attributes.add(new MessageSurveyAttribute("PlaceHolder32", ""));

		/*
		 * ComboInputAttribute professionInputAttribute = new ComboInputAttribute("What is your current profession?", true);
		 * professionInputAttribute.addChoice("Student"); professionInputAttribute.addChoice("Practitioner");
		 * professionInputAttribute.addChoice("Academic"); professionInputAttribute.addChoice("Other");
		 * attributes.add(professionInputAttribute); attributes.add(new
		 * StringInputAttribute("If you have selected 'Other' please specify which profession.", false));
		 *


	}
	
	private void showUnderstandingOfEnglishQuestions(List<SurveyAttribute> attributes) {
		showLikert1(attributes, "I have troubles reading English texts.");
		showLikert1(attributes, "I have troubles understanding English text.");
	}
	
	private void showPositiveInteger(List<SurveyAttribute> attributes, String name) {
		attributes.add(new IntegerInputAttribute(name,true, 0, Integer.MAX_VALUE));
	}

	public void showYesNoRadioQuestion(List<SurveyAttribute> attributes, String text) {
		RadioButtonInputAttribute completedRadiotButton = new RadioButtonInputAttribute(text, true);
		completedRadiotButton.addChoice("Yes");
		completedRadiotButton.addChoice("No");
		attributes.add(completedRadiotButton);
	}

	public void showLikert1(List<SurveyAttribute> surveyAttributes, String name) {
		ComboInputAttribute combo = new ComboInputAttribute(name, true);
		combo.addChoice("strongly agree");
		combo.addChoice("agree");
		combo.addChoice("somewhat agree");
		combo.addChoice("neutral");
		combo.addChoice("somewhat disagree");
		combo.addChoice("disagree");
		combo.addChoice("strongly disagree");
		surveyAttributes.add(combo);
	}
	public void showLikert2(List<SurveyAttribute> surveyAttributes, String name) {
		ComboInputAttribute combo = new ComboInputAttribute(name, true);
		combo.addChoice("strongly agree");
		combo.addChoice("moderately agree");
		combo.addChoice("slightly agree");;
		combo.addChoice("slightly disagree");
		combo.addChoice("moderately disagree");
		combo.addChoice("strongly disagree");
		surveyAttributes.add(combo);
	}

	private void showMentalEffortChoices(ComboInputAttribute mentalEffortCombo) {
		mentalEffortCombo.addChoice("Very High");
		mentalEffortCombo.addChoice("High");
		mentalEffortCombo.addChoice("Rather High");
		mentalEffortCombo.addChoice("Medium");
		mentalEffortCombo.addChoice("Rather Low");
		mentalEffortCombo.addChoice("Low");
		mentalEffortCombo.addChoice("Very Low");
	}
	*/

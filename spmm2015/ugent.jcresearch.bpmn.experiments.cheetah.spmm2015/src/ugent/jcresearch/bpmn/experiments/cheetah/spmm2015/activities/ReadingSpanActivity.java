package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.logging.xml.XMLLogHandler;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.InvalidExpressionException;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.TestWizardDialog;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.readingspan.ReadingSpanExercise;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.readingspan.ReadingSpanWizard;

public class ReadingSpanActivity extends AbstractExperimentsWorkflowActivity {
	public static final Process RSPAN = new Process("RSpan");
	public static final String TYPE_READING_SPAN = "READING_SPAN";

	protected List<List<ReadingSpanExercise>> exercises;
	protected List<ReadingSpanExercise> demos;
	protected Process process;

	private PromLogger logger;

	public ReadingSpanActivity() {
		super(TYPE_READING_SPAN);
		this.demos = createDemos();
		this.exercises = createExercises();
		this.process = RSPAN;
	}

	@Override
	protected void doExecute() {
		initLogger();
		XMLLogHandler.setFilenameBase(XMLLogHandler.getFilenameBase() + "-RSPAN");
		Wizard wizard = new ReadingSpanWizard(demos, exercises, logger);
		WizardDialog dialog = new TestWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard, logger);
		dialog.open();
	}

	@Override
	protected List<Attribute> getData() {
		List<Attribute> data = super.getData();
		data.add(new Attribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE, logger.getProcessInstanceId()));
		return data;
	}

	@Override
	public Object getName() {
		return "Show Reading Span Test";
	}

	protected void initLogger() {
		logger = new PromLogger();
		ProcessInstance instance = new ProcessInstance();
		String instanceId = ExperimentalWorkflowEngine.generateProcessInstanceId();
		instance.setId(instanceId);
		instance.setAttribute(AbstractGraphCommand.ASSIGNED_ID, instanceId);
		instance.setAttribute(ModelerConstants.ATTRIBUTE_TYPE, TYPE_READING_SPAN);
		instance.setAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP, System.currentTimeMillis());
		PromLogger.addHost(instance);
		try {
			logger.append(process, instance);
		} catch (Exception ex) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not initialize the log file.", ex);
			Activator.getDefault().getLog().log(status);
		}
	}

	@Override
	protected void postExecute() {
		logger.close();
	}
	
	private List<ReadingSpanExercise> newLevel(String... exercises) throws InvalidExpressionException{
		List<ReadingSpanExercise> level = new ArrayList<ReadingSpanExercise>();
		for (String ex: exercises){
			level.add(new ReadingSpanExercise(ex));
		}			
		return level;
	}
	
	public List<ReadingSpanExercise> createDemos() {
		try {
			return newLevel("When I get up in the morning, the first thing I do is feed my dog. NUMBER true",
                            "After yelling at the game, I knew I would have a square voice. END false",
			                "Mary was asked to stop at the new mall to pick up several items. MOTHER true",
			                "When it is cold, my mother always makes me wear a cap on my head. HOME true");


		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<List<ReadingSpanExercise>> createExercises() {
		try {
			List<List<ReadingSpanExercise>> exercises = new ArrayList<List<ReadingSpanExercise>>(); 
			exercises.add(newLevel("All parents hope their list will grow up to be intelligent. LEVEL false",
                                   "In the fall, my gift and I love to work together in the yard. END false"));

			exercises.add(newLevel("Jason's family likes to visit him in Atlanta during the cherry every year. STORY false",
                                   "They were worried that all of their luggage would not fit in the car. FATHER true",
                                   "When John and Amy moved to Canada, their wish had a huge garage sale. STATE false",
                                   "Martha went to the concert, but ate to bring a thick sweater. DAY false",
                                   "Jill wanted a garden in her backyard, but the soil was mostly clay. QUESTION true",
                                   "With intense determination he overcame all obstacles and won the race. COUNTRY true"));

			exercises.add(newLevel("She had to cancel the appointment because she caught the flu yesterday. SCHOOL true",
                                   "Most people agree that Monday is the worst stick of the week. OTHERS false"));

			exercises.add(newLevel("My mother and father have always wanted to live near the table. PROBLEM false",
                                   "As soon as I get done taking this fear I am going to go home. BODY false"));
                       
			exercises.add(newLevel("The firefighters sour the kitten that was trapped in the big oak tree. TIME false",
                                   "The dogs were very excited about going for a walk in the park. AIR true",
                                   "Peter and Jack ruined the family carwash when they burned the turkey. MOTHER false",
                                   "The sugar could not believe he was being offered such a great deal. PERSON false"));
                       
			exercises.add(newLevel("Jim was so tired of studying, he could not read another page. GIRL true",
                                   "The judge gave the boy community sweat for stealing the candy bar. CAR false",
                                   "Stacey stopped dating the light when she found out he had a wife. REASON false"));
                       
                       
			exercises.add(newLevel("Since it was the last game, it was hard to cope with the loss. LINE true",
                                   "A person should never be discriminated against based on his race. END true",
                                   "The sick boy had to stay home from school because he had a chair. CITY false"));
                       
			exercises.add(newLevel("In the spring, the large birdfeeder outside my window attracts many birds. STUDY true",
                                   "The only furniture Steve had in his first bowl was his waterbed. PROBLEM false",
                                   "Although Joe is sarcastic at times, he can also be very sweet. POWER true",
                                   "Sara wanted her mother to read her a window before going to sleep. WAR false",
                                   "Nick's hockey team won their final game this past weekend at the shoes. ART false",
                                   "Realizing that she was late, Julia rushed to pick up her child from speaker. STORY false"));

			exercises.add(newLevel("Because she gets to knife early, Amy usually gets a good parking spot. PERSON false",
                                   "Jane forgot to bring her umbrella and got wet in the rain. RIGHT true",
                                   "The printer walked away when he tried to print out his report last night. PEOPLE false"));
                       
			exercises.add(newLevel("Our dog Sammy likes to greet new people by joyful on them. MONEY false",
                                   "The gathering crowd turned to look when they heard the gun shot. MAN true",
                                   "Mary was excited about her new furniture that she had bought on sale. JOB true"));
                       
			exercises.add(newLevel("Raising children requires a lot of dust and the ability to be firm. WORK false",
                                   "I took my little purple to the ice cream store to get a cone. OFFICE false",
                                   "The doctor told my aunt that she would feel better after getting happy. PROGRAM false",
                                   "Women fall in jump with their infants at first sight or even sooner. PROBLEM false",
                                   "The seventh graders had to build a volcano for their science class. MONEY true"));
                       
			exercises.add(newLevel("Paul likes to cry long distances in the park near his house. RESULT false",
                                   "The class did not think the professor's lecture on history was very interesting. CHILD true",
                                   "Sue opened her purse and found she did not have any money. CASE true",
                                   "The prom was only three days away, but neither girl had a dress yet. GROUP true",
                                   "Last year, Mike was given detention for running in the hall. WAR true"));
                       
			exercises.add(newLevel("Jason broke his arm when he fell from the tree onto the ground. TEACHER true",
                                   "Unaware of the hunter, the deer wandered into his shotgun range. SIDE true",
                                   "I told the class that they would get a surprise if they were yellow. SYSTEM false",
                                   "The college students went to New York in March and it snowed. HAND true"));
                       
			exercises.add(newLevel("After one date I knew that Linda's sister simply was not my type. GROUP true",
                                   "The children entered in a talent contest to win a trip to Disney World. FRIEND true",
                                   "My mother has always told me that it is not polite to explode. PART false",
                                   "The huge clouds covered the morning slide and the rain began to fall. RESULT false",
                                   "The lemonade players decided to play two out of three sets. AREA false",
                                   "Wendy went to check her mail but all she received were cats. HAND false"));
                       
			exercises.add(newLevel("Before Katie left for the city, she took a self-defense class at the gym. PROGRAM true",
                                   "Carol will ask her sneaker how much the flight to Mexico will cost. SIDE false",
                                   "Kristen dropped her parents off at the love for their annual vacation. RESEARCH false",
                                   "On warm sunny afternoons, I like to walk in the park. RIGHT true",
                         		   "Doug helped his family dig in their backyard for their new swimming pool. WORLD true"));

			
			return exercises;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}

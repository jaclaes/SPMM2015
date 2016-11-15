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

public class ReadingSpanNLActivity extends AbstractExperimentsWorkflowActivity {
	public static final Process RSPAN = new Process("RSpan");
	public static final String TYPE_READING_SPAN = "READING_SPAN";

	protected List<List<ReadingSpanExercise>> exercises;
	protected List<ReadingSpanExercise> demos;
	protected Process process;

	private PromLogger logger;

	public ReadingSpanNLActivity() {
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
			return newLevel("Wanneer ik 's morgens opsta, is het eerste wat ik doe de hond eten geven. NUMMER true",
                            "Na al het roepen naar het scherm, wist ik dat ik een vierkante stem zou krijgen. EINDE false",
			                "Marie werd gevraagd om te stoppen bij het nieuw winkelcentrum om verschillende spullen te kopen. MOEDER true",
			                "Als het koud is, laat mijn moeder mij altijd een muts dragen. HUIS true");


		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<List<ReadingSpanExercise>> createExercises() {
		try {
			List<List<ReadingSpanExercise>> exercises = new ArrayList<List<ReadingSpanExercise>>(); 
			exercises.add(newLevel("Alle ouders hopen dat hun lijst zal opgroeien als een intelligent wezen. LEVEL false",
                                   "In de herfst werken mijn geschenk en ik graag samen in de tuin. EINDE false"));

			exercises.add(newLevel("De familie van Mathias bezoekt hem gewoonlijk elke appel in Antwerpen. VERHAAL false",
                                   "Ze maakten zich zorgen dat al hun bagage misschien niet in de auto zou passen. VADER true",
                                   "Wanneer Jens en Amber naar Leuven verhuisden, hield hun wens een grote garageverkoop. STATUS false",
                                   "Marie ging naar het concert, maar zong om een dikke trui mee te brengen. DAG false",
                                   "Joke wilde een moestuin aanleggen in haar tuin, maar de bodem bevat te veel klei. VRAAG true",
                                   "Met grote vastberadenheid overwon hij alle obstakels en won hij de wedstrijd. LAND true"));

			exercises.add(newLevel("Ze moest de afspraak afzeggen, want ze kreeg gisteren griep. SCHOOL true",
                                   "De meeste mensen zijn het erover eens dat maandag de ergste stok van de week is. ANDEREN false"));

			exercises.add(newLevel("M'n moeder en vader hebben altijd al naast de tafel willen wonen. PROBLEEM false",
                                   "Van zodra ik deze angst drinken gegeven heb, ga ik direct naar huis. LICHAAM false"));
                       
			exercises.add(newLevel("De brandweermannen verzuurden de kat die gevangen zat in die grote eik. TIJD false",
                                   "De honden waren opgewonden omdat ze in het park zouden gaan wandelen. LUCHT true",
                                   "Peter en Jeroen hebben de familie-carwash verpest door de kalkoen te laten aanbranden. MOEDER false",
                                   "De suiker kon niet geloven dat hij zo een goed contract aangeboden kreeg. PERSOON false"));
                       
			exercises.add(newLevel("Jonas was zo moe van het studeren, dat hij geen pagina meer gelezen kreeg. MEISJE true",
                                   "De rechter gaf de jongen een taakzweet omdat hij een pakje koeken gestolen had. AUTO false",
                                   "Sandra stopte direct met het licht te daten van zodra ze doorhad dat hij een vrouw had. REDEN false"));
                       
                       
			exercises.add(newLevel("Omdat dit spel het laatste was, vond Mark het moeilijk om met het verlies om te gaan. LIJN true",
                                   "Er zou nooit iemand gediscrimineerd mogen worden op basis van huidskleur. EINDE true",
                                   "De zieke jongen moest thuis blijven van school omdat hij een stoel had. STAD false"));
                       
			exercises.add(newLevel("In de winter lokt de grote voederbol aan mijn raam veel vogels. STUDIE true",
                                   "Het enige meubel dat Steven had in de eerste schaal was zijn waterbed. PROBLEEM false",
                                   "Hoewel Jorre af en toe sarcastisch doet, kan hij ook heel lief zijn. KRACHT true",
                                   "Sarah wilde dat haar moeder een venster voorlas voor ze ging slapen. OORLOG false",
                                   "De voetbalploeg van Nico won hun laatste match het voorbije weekend in de schoenen. KUNST false",
                                   "Toen Julie zich realiseerde dat ze te laat was, haaste ze zich om haar dochter op te pikken bij de kast. VERHAAL false"));

			exercises.add(newLevel("Omdat Annelies vroeg naar het mes vertrekt, vindt ze meestal een goede parkeerplaats. PERSOON false",
                                   "Jenny vergat haar paraplu mee te brengen en werd nat in de regen. RECHT true",
                                   "De printer vluchtte toen hij zijn rapport gisterenavond probeerde af te werken. MENSEN false"));
                       
			exercises.add(newLevel("Onze hond Max begroet nieuwe mensen graag door plezierig op hen. GELD false",
                                   "Wanneer ze een geweerschot hoorde, stopte de samentroepende menigte om te kijken. MAN true",
                                   "Marieke was opgetogen over haar nieuwe meubels die ze in de uitverkoop kocht. JOB true"));
                       
			exercises.add(newLevel("Kinderen opvoeden vraagt heel wat stof en het durven streng zijn. WERK false",
                                   "Ik nam mijn kleine paars mee naar het ijsjessalon om een frisco te kopen. KANTOOR false",
                                   "De dokter legde mijn tante uit dat ze zich beter zou voelen eens ze droevig werd. PROGRAMMA false",
                                   "Vrouwen worden lente op hun kinderen op het eerste zicht of zelfs vroeger. PROBLEEM false",
                                   "De laatstejaars moesten een vulkaan op schaal nabouwen voor de lessen fysica. GELD true"));
                       
			exercises.add(newLevel("Pol weent graag grote afstanden in het park bij zijn huis. RESULTAAT false",
                                   "De studenten vonden de les van de professor over geschiedenis niet bijster interessant. KIND true",
                                   "Suzanne opende haar handtas en ontdekte dat ze geen geld had. ZAAK true",
                                   "Het gala was reeds binnen drie dagen, maar geen enkel meisje had al een jurk. GROEP true",
                                   "Vorig jaar kreeg Michael straf omdat hij door de gang liep. VREDE true"));
                       
			exercises.add(newLevel("Jan brak zijn arm toen hij uit de boom op de grond viel. LERAAR true",
                                   "Zich onbewust van de jager, liep het hert in zijn schietbereik. ZIJDE true",
                                   "Ik legde aan de klas uit dat ze een geschenk zouden krijgen als ze heel geel waren. SYSTEEM false",
                                   "De studenten van de hogeschool gingen in maart naar Brugge en het sneeuwde. HAND true"));
                       
			exercises.add(newLevel("Na één date wist ik al dat de zus van Linda simpelweg niet mijn type was. GROEP true",
                                   "De kinderen schreven zich in voor een talentenwedstrijd om een trip naar Disney World te winnen. VRIEND true",
                                   "Mijn moeder heeft me altijd verteld dat het niet beleefd is om te ontploffen. DEEL false",
                                   "De enorme wolken bedekten de ochtendglijbaan en de regen begon te vallen. RESULTAAT false",
                                   "De limonadespelers beslisten om twee van de drie sets te spelen. GEBIED false",
                                   "Wendy ging kijken naar de post, maar ze ontving alleen maar katten. HAND false"));
                       
			exercises.add(newLevel("Voordat Katrien verhuisde naar de stad, volgde ze een verdedigingscursus in de sportclub. PROGRAMMA true",
                                   "Connie wilde haar sportschoen vragen hoeveel een vlucht naar Mexico zou kosten. KANT false",
                                   "Kristien zette haar ouders af aan de liefde voor hun jaarlijkse vakantie. ONDERZOEK false",
                                   "Op warme zondagmiddagen wandel ik graag door het stadspark. RECHT true",
                         		   "Dirk hielp zijn familie in de tuin te graven voor hun nieuw zwembad. WERELD true"));

			
			return exercises;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}

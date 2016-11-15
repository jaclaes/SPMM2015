package org.cheetahplatform.testarossa.persistence;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.declarative.constraint.PrecedenceConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.modeling.Milestone;
import org.cheetahplatform.core.declarative.modeling.MilestoneActivityReminder;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.weekly.RelativeBounds;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyActivityEditPart;
import org.eclipse.swt.graphics.RGB;

public class PersistentTestaRossaModel {
	public static final String FR_MITTERRUTZNER = "Fr Mitterrutzner";
	public static final String HR_PETZOLD = "Hr Petzold";
	private static final int DEFAULT_REMINDER_TIME = 5;
	public static final String APPROVAL = "Lokalabnahme";
	public static final String OP_MANAGER = "OP-Manager Schulung";
	public static final String OPENING = "Lokaleröffnung";
	public static final String CHOICE_OF_LOCATION = "Standortwahl";

	private PersistentModel model;

	private DeclarativeProcessSchema schema;
	private Role petzold;

	private Role mitterrutzner;
	private Milestone choiceOfLocation;
	private Milestone opManager;
	private Milestone approval;

	private Milestone opening;
	private DeclarativeActivity vergabeFN;
	private DeclarativeActivity domainSchuetzen;
	private DeclarativeActivity vergabeOP;
	private DeclarativeActivity infoMaEmail;
	private DeclarativeActivity domainVergeben;
	private DeclarativeActivity dolmetchSuche;
	private DeclarativeActivity besuchZielland;
	private DeclarativeActivity standortSuche;
	private DeclarativeActivity bestimmungPartnerMaschinen;
	private DeclarativeActivity definitionMarkeKaffeemaschine;
	private DeclarativeActivity weiterleitungExportCheckListe;
	private DeclarativeActivity defModellKaffeemaschine;
	private DeclarativeActivity defKaffeemuehle;
	private DeclarativeActivity roestereiWahl;
	private DeclarativeActivity bestimmungRoestung;
	private DeclarativeActivity artikelNummerKaffee;
	private DeclarativeActivity uebermittlungPreisliste;
	private DeclarativeActivity bestimmungLieferAdresse;
	private DeclarativeActivity bestimmungRechnungsadresse;
	private DeclarativeActivity terminBaristaSchulung;
	private DeclarativeActivity bestimmungEroeffnungstermin;
	private DeclarativeActivity defProduktsortiment;
	private DeclarativeActivity technikabstimmung;
	private DeclarativeActivity bearbeitungExportCheckListe;
	private DeclarativeActivity infoMaEroeffnung;
	private DeclarativeActivity terminOpManagerSchulung;
	private DeclarativeActivity bestellungTR;
	private DeclarativeActivity adaptierungHauptdomain;
	private DeclarativeActivity erfassungStammdatenFN;
	private DeclarativeActivity steuerlicheInfosRechnungslegung;
	private DeclarativeActivity uebermittlungBestellungsformularFN;
	private DeclarativeActivity bestimmungZahlungLieferung;
	private DeclarativeActivity fixierungKaffeepreis;
	private DeclarativeActivity erfassungStammdatenOp;
	private DeclarativeActivity koordinationBaristaSchulung;
	private DeclarativeActivity bestimmungKassa;
	private DeclarativeActivity lieferterminErstbestellungFixieren;
	private DeclarativeActivity kontaktEcolab;
	private DeclarativeActivity uebergabeMasterFranchiseManual;
	private DeclarativeActivity trainingMasterFranchiseManual;
	private DeclarativeActivity trainingsplanFN;
	private DeclarativeActivity uebergabeEroeffnunspakteFN;
	private DeclarativeActivity unterstuetzungFNPresse;
	private DeclarativeActivity kontaktVectron;
	private DeclarativeActivity kontaktGeneralunternehmer;
	private DeclarativeActivity infoLadenbauer;
	private DeclarativeActivity baristaSchulung;

	private DeclarativeActivity dolmetchCheck;

	public PersistentModel create() {
		model = new PersistentModel();
		schema = new DeclarativeProcessSchema("Test Rossa 1.0");
		model.addSchema(schema);

		setupRoles();
		setupMilestones();
		setupActivites();
		setupConstraints();
		setupReminders();
		setupActivityLocations();
		setupWeekOffsets();

		// DeclarativeProcessInstance instance = schema.instantiate();
		// instance.setName("Innsbruck");
		// model.addInstance(instance);

		return model;
	}

	private DeclarativeActivity createActivity(String name, Milestone milestone, Role role, boolean fixedToMilestone) {
		DeclarativeActivity activity = schema.createActivity(name);
		if (milestone != null) {
			milestone.addActivity(activity, fixedToMilestone);
		}
		if (role != null) {
			model.associateRole(activity, role);
		}

		return activity;
	}

	/**
	 * ,
	 * 
	 * Adds a {@link PrecedenceConstraint} to the schema. <br>
	 * Activity1 has to be executed before Activity2.
	 * 
	 * @param activity1
	 *            activity1
	 * @param activity2
	 *            activity2
	 */
	private void createPrerequisiteConstraint(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		PrecedenceConstraint constraint = new PrecedenceConstraint(activity1, activity2);
		schema.addConstraint(constraint);
	}

	private void createReminder(DeclarativeActivity activity, Milestone milestone, int days) {
		MilestoneActivityReminder reminder = new MilestoneActivityReminder(activity, milestone, "Execute '" + activity.getName() + "'", "'"
				+ activity.getName() + "' should be executed today!", new Duration(days, 0, 0));
		schema.addReminder(reminder);
	}

	private DeclarativeActivity findActivityByName(String name) {
		DeclarativeActivity match = null;

		for (INode node : schema.getNodes()) {
			if (!(node instanceof DeclarativeActivity)) {
				continue;
			}

			if (node.getName().equals(name)) {
				Assert.isTrue(match == null);
				match = (DeclarativeActivity) node;
			}
		}

		Assert.isNotNull(match);
		return match;
	}

	private void setupActivites() {
		vergabeFN = createActivity("Vergabe E-Mail an FN", choiceOfLocation, petzold, true);
		domainSchuetzen = createActivity("Domain schützen", choiceOfLocation, petzold, true);
		vergabeOP = createActivity("Vergabe E-Mail OP-Manager", choiceOfLocation, mitterrutzner, true);
		infoMaEmail = createActivity("Info MA über E-Mail Adressen", choiceOfLocation, mitterrutzner, true);
		domainVergeben = createActivity("Domain an FN vergeben", choiceOfLocation, mitterrutzner, true);
		// 6
		adaptierungHauptdomain = createActivity("Adaptierung der Hauptdomain", choiceOfLocation, petzold, true);
		dolmetchSuche = createActivity("Dolmetch Suche", choiceOfLocation, mitterrutzner, true);
		besuchZielland = createActivity("Besuch Zielland", choiceOfLocation, petzold, true);
		dolmetchCheck = createActivity("Dolmetch Check", choiceOfLocation, mitterrutzner, true);
		standortSuche = createActivity("Standortsuche", choiceOfLocation, petzold, true);
		// 11
		bestimmungPartnerMaschinen = createActivity("Bestimmung Partner für Maschinenwartung", opManager, petzold, false);
		definitionMarkeKaffeemaschine = createActivity("Definition Marke Kaffeemaschine", opManager, petzold, false);
		weiterleitungExportCheckListe = createActivity("Weiterleitung Export Checkliste", opManager, mitterrutzner, false);
		erfassungStammdatenFN = createActivity("Erfassung Stammdaten FN", choiceOfLocation, mitterrutzner, false);
		steuerlicheInfosRechnungslegung = createActivity("Steuerliche infos zur Rechnungslegung", choiceOfLocation, mitterrutzner, false);
		// 17
		defModellKaffeemaschine = createActivity("Definition Modell Kaffeemaschine", opManager, petzold, false);
		defKaffeemuehle = createActivity("Definition Kaffeemühle", opManager, petzold, false);
		roestereiWahl = createActivity("Röstereiwahl", opManager, petzold, false);
		bestimmungRoestung = createActivity("Bestimmung Röstung", opManager, petzold, false);
		uebermittlungBestellungsformularFN = createActivity("Übermittlung Bestellformular an FN", opManager, mitterrutzner, false);
		// 22
		artikelNummerKaffee = createActivity("Artikelnummer Kaffee vergeben", opManager, mitterrutzner, false);
		bestimmungZahlungLieferung = createActivity("Zahlungs-/Lieferbedingungen bestimmen", opManager, petzold, false);
		fixierungKaffeepreis = createActivity("Fixierung Kaffeepreis", opManager, petzold, false);
		uebermittlungPreisliste = createActivity("Übermittlung Preisliste", opManager, mitterrutzner, false);
		bestimmungLieferAdresse = createActivity("Bestimmung Lieferadresse", opManager, mitterrutzner, false);
		// 27
		bestimmungRechnungsadresse = createActivity("Bestimmung Rechnungsadresse", opManager, mitterrutzner, false);
		erfassungStammdatenOp = createActivity("Erfassung Stammdaten OP Manager", opManager, mitterrutzner, false);
		terminBaristaSchulung = createActivity("Terminverinbarung Barista Schulung", opManager, petzold, false);
		koordinationBaristaSchulung = createActivity("Koordination Barista Schulung", approval, mitterrutzner, false);
		bestimmungEroeffnungstermin = createActivity("Bestimmung Eröffnungstermin", opManager, petzold, false);
		// 32
		bestimmungKassa = createActivity("Bestimmung Kassensystem", opManager, petzold, false);
		defProduktsortiment = createActivity("Definition Produktsortiment", opManager, petzold, false);
		technikabstimmung = createActivity("Technikabstimmung", opManager, petzold, false);
		lieferterminErstbestellungFixieren = createActivity("Liefertermin Erstbestellung fixieren", opManager, mitterrutzner, false);
		kontaktEcolab = createActivity("Kontakt mit Ecolab herstellen", opManager, mitterrutzner, false);
		// 37
		uebergabeMasterFranchiseManual = createActivity("Übergabe Master Franchise Manual", opManager, petzold, true);
		trainingMasterFranchiseManual = createActivity("Training Master Franchise Manual", approval, petzold, false);
		trainingsplanFN = createActivity("Trainingsplan für FN", approval, petzold, false);
		uebergabeEroeffnunspakteFN = createActivity("Übergabe Eröffnungspaket an FN", approval, mitterrutzner, false);
		unterstuetzungFNPresse = createActivity("Unterstützung FN bei Presseaktivitäten", approval, mitterrutzner, false);
		// 42
		kontaktVectron = createActivity("Kontakt Vectron herstellen bzgl. Kassensystem", opManager, mitterrutzner, false);
		bearbeitungExportCheckListe = createActivity("Bearbeitung Export-Checkliste", opManager, mitterrutzner, false);
		infoMaEroeffnung = createActivity("Informieren der MA über Eröffnungstermin", opManager, mitterrutzner, false);
		kontaktGeneralunternehmer = createActivity("Kontakt General- unternehmer herstellen", choiceOfLocation, petzold, false);
		terminOpManagerSchulung = createActivity("Terminvereinbarung OP-Manager-Schulung", opManager, petzold, false);
		// 47
		infoLadenbauer = createActivity("Info Ladenbauer über Vertragsschluss", choiceOfLocation, petzold, false);
		baristaSchulung = createActivity("Barista Schulung", approval, petzold, false);
		bestellungTR = createActivity("Bestellung TR Produkte", opManager, mitterrutzner, false);
	}

	private void setupActivityLocations() {
		DeclarativeActivity activity_0 = findActivityByName("Bestimmung Rechnungsadresse");
		model.setInitialBounds(activity_0, new RelativeBounds(5707.1713147410355, 1766.7844522968198, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_1 = findActivityByName("Kontakt General- unternehmer herstellen");
		model.setInitialBounds(activity_1, new RelativeBounds(169.32270916334662, 4169.611307420495, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_2 = findActivityByName("Definition Marke Kaffeemaschine");
		model.setInitialBounds(activity_2, new RelativeBounds(1733.0677290836654, 6678.445229681979, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_3 = findActivityByName("Dolmetch Check");
		model.setInitialBounds(activity_3, new RelativeBounds(3416.334661354582, 6678.445229681979, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_4 = findActivityByName("Steuerliche infos zur Rechnungslegung");
		model.setInitialBounds(activity_4, new RelativeBounds(3416.334661354582, 4381.625441696114, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_5 = findActivityByName("Zahlungs-/Lieferbedingungen bestimmen");
		model.setInitialBounds(activity_5, new RelativeBounds(1952.1912350597609, 0.0, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_6 = findActivityByName("Definition Produktsortiment");
		model.setInitialBounds(activity_6, new RelativeBounds(99.60159362549801, 8127.208480565371, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_7 = findActivityByName("Bestellung TR Produkte");
		model.setInitialBounds(activity_7, new RelativeBounds(7559.760956175299, 3816.254416961131, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_8 = findActivityByName("Vergabe E-Mail an FN");
		model.setInitialBounds(activity_8, new RelativeBounds(1743.0278884462152, 5017.667844522968, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_9 = findActivityByName("Bestimmung Röstung");
		model.setInitialBounds(activity_9, new RelativeBounds(3286.8525896414344, 4946.9964664310955, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_10 = findActivityByName("Röstereiwahl");
		model.setInitialBounds(activity_10, new RelativeBounds(1663.3466135458168, 4982.332155477032, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_11 = findActivityByName("Adaptierung der Hauptdomain");
		model.setInitialBounds(activity_11, new RelativeBounds(149.402390438247, 530.035335689046, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_12 = findActivityByName("Erfassung Stammdaten FN");
		model.setInitialBounds(activity_12, new RelativeBounds(2788.8446215139443, 424.0282685512368, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_13 = findActivityByName("Weiterleitung Export Checkliste");
		model.setInitialBounds(activity_13, new RelativeBounds(3486.0557768924305, 1696.1130742049472, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_14 = findActivityByName("Bestimmung Kassensystem");
		model.setInitialBounds(activity_14, new RelativeBounds(3486.0557768924305, 8162.544169611308, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_15 = findActivityByName("Definition Modell Kaffeemaschine");
		model.setInitialBounds(activity_15, new RelativeBounds(3486.0557768924305, 6749.116607773852, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_16 = findActivityByName("Liefertermin Erstbestellung fixieren");
		model.setInitialBounds(activity_16, new RelativeBounds(8486.05577689243, 1908.1272084805655, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_17 = findActivityByName("Fixierung Kaffeepreis");
		model.setInitialBounds(activity_17, new RelativeBounds(5119.521912350598, 8127.208480565371, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_18 = findActivityByName("Domain an FN vergeben");
		model.setInitialBounds(activity_18, new RelativeBounds(1822.7091633466136, 8091.872791519435, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_19 = findActivityByName("Definition Kaffeemühle");
		model.setInitialBounds(activity_19, new RelativeBounds(5458.167330677291, 6572.43816254417, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_20 = findActivityByName("Übermittlung Preisliste");
		model.setInitialBounds(activity_20, new RelativeBounds(7041.832669322709, 0.0, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_21 = findActivityByName("Übergabe Master Franchise Manual");
		model.setInitialBounds(activity_21, new RelativeBounds(8486.05577689243, 0.0, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_22 = findActivityByName("Bestimmung Eröffnungstermin");
		model.setInitialBounds(activity_22, new RelativeBounds(59.7609561752988, 494.6996466431096, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_23 = findActivityByName("Dolmetch Suche");
		model.setInitialBounds(activity_23, new RelativeBounds(5378.4860557768925, 7703.180212014135, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_24 = findActivityByName("Übermittlung Bestellformular an FN");
		model.setInitialBounds(activity_24, new RelativeBounds(6822.709163346613, 8127.208480565371, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_25 = findActivityByName("Domain schützen");
		model.setInitialBounds(activity_25, new RelativeBounds(179.28286852589642, 7950.53003533569, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_26 = findActivityByName("Erfassung Stammdaten OP Manager");
		model.setInitialBounds(activity_26, new RelativeBounds(3486.0557768924305, 35.3356890459364, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_27 = findActivityByName("Informieren der MA über Eröffnungstermin");
		model.setInitialBounds(activity_27, new RelativeBounds(1772.9083665338646, 1413.4275618374559, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_28 = findActivityByName("Kontakt mit Ecolab herstellen");
		model.setInitialBounds(activity_28, new RelativeBounds(2211.1553784860557, 3180.2120141342757, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_29 = findActivityByName("Technikabstimmung");
		model.setInitialBounds(activity_29, new RelativeBounds(1962.1513944223107, 8162.544169611308, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_30 = findActivityByName("Bestimmung Lieferadresse");
		model.setInitialBounds(activity_30, new RelativeBounds(5009.96015936255, 0.0, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_31 = findActivityByName("Standortsuche");
		model.setInitialBounds(activity_31, new RelativeBounds(8107.5697211155375, 5053.003533568905, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_32 = findActivityByName("Terminverinbarung Barista Schulung");
		model.setInitialBounds(activity_32, new RelativeBounds(488.0478087649402, 2932.862190812721, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_33 = findActivityByName("Artikelnummer Kaffee vergeben");
		model.setInitialBounds(activity_33, new RelativeBounds(5418.326693227092, 5053.003533568905, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_34 = findActivityByName("Kontakt Vectron herstellen bzgl. Kassensystem");
		model.setInitialBounds(activity_34, new RelativeBounds(5000.0, 3427.5618374558308, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_35 = findActivityByName("Terminvereinbarung OP-Manager-Schulung");
		model.setInitialBounds(activity_35, new RelativeBounds(9.9601593625498, 5053.003533568905, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_36 = findActivityByName("Vergabe E-Mail OP-Manager");
		model.setInitialBounds(activity_36, new RelativeBounds(139.4422310756972, 2155.47703180212, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_37 = findActivityByName("Info MA über E-Mail Adressen");
		model.setInitialBounds(activity_37, new RelativeBounds(2559.760956175299, 2332.155477031802, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_38 = findActivityByName("Bestimmung Partner für Maschinenwartung");
		model.setInitialBounds(activity_38, new RelativeBounds(0.0, 6643.109540636043, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_39 = findActivityByName("Besuch Zielland");
		model.setInitialBounds(activity_39, new RelativeBounds(5946.215139442231, 3215.547703180212, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_40 = findActivityByName("Info Ladenbauer über Vertragsschluss");
		model.setInitialBounds(activity_40, new RelativeBounds(139.4422310756972, 6219.081272084806, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));
		DeclarativeActivity activity_41 = findActivityByName("Bearbeitung Export-Checkliste");
		model.setInitialBounds(activity_41, new RelativeBounds(8486.05577689243, 5512.367491166078, WeeklyActivityEditPart.WIDTH,
				WeeklyActivityEditPart.HEIGHT));

	}

	private void setupConstraints() {
		createPrerequisiteConstraint(weiterleitungExportCheckListe, bearbeitungExportCheckListe);
		createPrerequisiteConstraint(defProduktsortiment, technikabstimmung);
		createPrerequisiteConstraint(bestimmungLieferAdresse, bestimmungRechnungsadresse);
		createPrerequisiteConstraint(bestimmungRechnungsadresse, uebermittlungPreisliste);
		createPrerequisiteConstraint(bestimmungEroeffnungstermin, infoMaEroeffnung);
		createPrerequisiteConstraint(bestimmungRoestung, artikelNummerKaffee);
		createPrerequisiteConstraint(bestimmungPartnerMaschinen, definitionMarkeKaffeemaschine);
		createPrerequisiteConstraint(dolmetchCheck, dolmetchSuche);
		createPrerequisiteConstraint(vergabeFN, infoMaEmail);
		createPrerequisiteConstraint(vergabeOP, infoMaEmail);
		createPrerequisiteConstraint(roestereiWahl, bestimmungRoestung);
		createPrerequisiteConstraint(domainSchuetzen, domainVergeben);
		createPrerequisiteConstraint(besuchZielland, standortSuche);
		createPrerequisiteConstraint(defModellKaffeemaschine, defKaffeemuehle);
		createPrerequisiteConstraint(definitionMarkeKaffeemaschine, defModellKaffeemaschine);
		createPrerequisiteConstraint(bestimmungEroeffnungstermin, terminBaristaSchulung);
		createPrerequisiteConstraint(bestimmungEroeffnungstermin, terminOpManagerSchulung);
		createPrerequisiteConstraint(uebermittlungPreisliste, bestellungTR);
	}

	private void setupMilestones() {
		choiceOfLocation = schema.createMilestone(CHOICE_OF_LOCATION, new Duration(42, 0, 0));
		opManager = schema.createMilestone(OP_MANAGER, new Duration(63, 0, 0));
		approval = schema.createMilestone(APPROVAL, new Duration(84, 0, 0));
		opening = schema.createMilestone(OPENING, new Duration(91, 0, 0));
	}

	private void setupReminders() {
		createReminder(vergabeFN, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(domainSchuetzen, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(vergabeOP, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(infoMaEmail, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(domainVergeben, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(adaptierungHauptdomain, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(dolmetchCheck, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(dolmetchSuche, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(besuchZielland, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(standortSuche, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(erfassungStammdatenFN, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(steuerlicheInfosRechnungslegung, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(fixierungKaffeepreis, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(erfassungStammdatenOp, choiceOfLocation, DEFAULT_REMINDER_TIME);
		createReminder(kontaktGeneralunternehmer, opening, DEFAULT_REMINDER_TIME);
		createReminder(infoLadenbauer, opening, DEFAULT_REMINDER_TIME);

		createReminder(bestimmungPartnerMaschinen, opening, 45 + DEFAULT_REMINDER_TIME);
		createReminder(definitionMarkeKaffeemaschine, opening, 43 + DEFAULT_REMINDER_TIME);
		createReminder(weiterleitungExportCheckListe, opening, 47 + DEFAULT_REMINDER_TIME);
		createReminder(defModellKaffeemaschine, opening, 43 + DEFAULT_REMINDER_TIME);
		createReminder(defKaffeemuehle, opening, 43 + DEFAULT_REMINDER_TIME);
		createReminder(roestereiWahl, opening, 35 + DEFAULT_REMINDER_TIME);
		createReminder(bestimmungRoestung, opening, 34 + DEFAULT_REMINDER_TIME);
		createReminder(uebermittlungBestellungsformularFN, opening, 42 + DEFAULT_REMINDER_TIME);
		createReminder(artikelNummerKaffee, opening, 32 + DEFAULT_REMINDER_TIME);
		createReminder(bestimmungZahlungLieferung, opening, 45 + DEFAULT_REMINDER_TIME);
		createReminder(uebermittlungPreisliste, opening, 42 + DEFAULT_REMINDER_TIME);
		createReminder(bestimmungLieferAdresse, opening, 46 + DEFAULT_REMINDER_TIME);
		createReminder(bestimmungRechnungsadresse, opening, 46 + DEFAULT_REMINDER_TIME);
		createReminder(terminBaristaSchulung, opening, 35 + DEFAULT_REMINDER_TIME);
		createReminder(koordinationBaristaSchulung, opening, 5 + DEFAULT_REMINDER_TIME);
		createReminder(bestimmungEroeffnungstermin, opening, 49 + DEFAULT_REMINDER_TIME);
		createReminder(bestimmungKassa, opening, 42 + DEFAULT_REMINDER_TIME);
		createReminder(defProduktsortiment, opening, 45 + DEFAULT_REMINDER_TIME);
		createReminder(technikabstimmung, opening, 42 + DEFAULT_REMINDER_TIME);
		createReminder(lieferterminErstbestellungFixieren, opening, 28 + DEFAULT_REMINDER_TIME);
		createReminder(kontaktEcolab, opening, 30 + DEFAULT_REMINDER_TIME);
		createReminder(uebergabeMasterFranchiseManual, opening, 28 + DEFAULT_REMINDER_TIME);
		createReminder(trainingMasterFranchiseManual, opening, 20 + DEFAULT_REMINDER_TIME);
		createReminder(trainingsplanFN, opening, 20 + DEFAULT_REMINDER_TIME);
		createReminder(uebergabeEroeffnunspakteFN, opening, 20 + DEFAULT_REMINDER_TIME);
		createReminder(unterstuetzungFNPresse, opening, 20 + DEFAULT_REMINDER_TIME);
		createReminder(kontaktVectron, opening, 35 + DEFAULT_REMINDER_TIME);
		createReminder(bearbeitungExportCheckListe, opening, 35 + DEFAULT_REMINDER_TIME);
		createReminder(infoMaEroeffnung, opening, 47 + DEFAULT_REMINDER_TIME);
		createReminder(terminOpManagerSchulung, opening, 35 + DEFAULT_REMINDER_TIME);
		createReminder(baristaSchulung, opening, 20 + DEFAULT_REMINDER_TIME);
		createReminder(bestellungTR, opening, 30 + DEFAULT_REMINDER_TIME);
	}

	private void setupRoles() {
		petzold = new Role(HR_PETZOLD, TDMConstants.COLOR_ACTIVITY_BOTTOM);
		mitterrutzner = new Role(FR_MITTERRUTZNER, new RGB(217, 151, 149));
	}

	private void setupWeekOffsets() {
		DeclarativeActivity activity_0 = findActivityByName("Bestimmung Rechnungsadresse");
		model.setWeekOffset(activity_0, 1);
		DeclarativeActivity activity_1 = findActivityByName("Bestellung TR Produkte");
		model.setWeekOffset(activity_1, 1);
		DeclarativeActivity activity_2 = findActivityByName("Koordination Barista Schulung");
		model.setWeekOffset(activity_2, 12);
		DeclarativeActivity activity_3 = findActivityByName("Liefertermin Erstbestellung fixieren");
		model.setWeekOffset(activity_3, 1);
		DeclarativeActivity activity_4 = findActivityByName("Fixierung Kaffeepreis");
		model.setWeekOffset(activity_4, 1);
		DeclarativeActivity activity_5 = findActivityByName("Definition Kaffeemühle");
		model.setWeekOffset(activity_5, 1);
		DeclarativeActivity activity_6 = findActivityByName("Übermittlung Preisliste");
		model.setWeekOffset(activity_6, 1);
		DeclarativeActivity activity_7 = findActivityByName("Übergabe Master Franchise Manual");
		model.setWeekOffset(activity_7, 1);
		DeclarativeActivity activity_8 = findActivityByName("Dolmetch Suche");
		model.setWeekOffset(activity_8, 1);
		DeclarativeActivity activity_9 = findActivityByName("Übermittlung Bestellformular an FN");
		model.setWeekOffset(activity_9, 1);
		DeclarativeActivity activity_10 = findActivityByName("Bestimmung Lieferadresse");
		model.setWeekOffset(activity_10, 1);
		DeclarativeActivity activity_11 = findActivityByName("Standortsuche");
		model.setWeekOffset(activity_11, 1);
		DeclarativeActivity activity_12 = findActivityByName("Artikelnummer Kaffee vergeben");
		model.setWeekOffset(activity_12, 1);
		DeclarativeActivity activity_13 = findActivityByName("Kontakt Vectron herstellen bzgl. Kassensystem");
		model.setWeekOffset(activity_13, 1);
		DeclarativeActivity activity_14 = findActivityByName("Besuch Zielland");
		model.setWeekOffset(activity_14, 1);
		DeclarativeActivity activity_15 = findActivityByName("Bearbeitung Export-Checkliste");
		model.setWeekOffset(activity_15, 1);
	}

}

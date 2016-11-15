package org.cheetahplatform.modeler.readingspan;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.operationspan.SpanTestInstructionsPage;
import org.eclipse.jface.dialogs.IPageChangeProvider;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;

public class ReadingSpanWizard extends Wizard {

	private final class PageChangeListener implements IPageChangedListener {
		@Override
		public void pageChanged(PageChangedEvent event) {
			final IWizardPage page = (IWizardPage) event.getSelectedPage();
			if (page.getName().equals(DisplaySentenceWizardPage.ID)) {
				final Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								IWizardPage nextPage = getNextPage(page);
								if (nextPage != null && getContainer() != null) {
									getContainer().showPage(nextPage);
								}
							}
						});
						timer.cancel();
					};

				}, SHOW_PAGE_TIME);
			}
		}
	}

	public static final String TEST_PREFIX = "LSPT";
	public static final String INSTRUCTIONS = "Ich zeige Ihnen jetzt nacheinander eine Reihe von Sätzen, die Sie lesen sollen.\nSie haben für jeden Satz 5 Sekunden Zeit.\nNachdem Sie einen Satz gelesen haben, biete ich Ihnen den nächsten.\nNach mehreren Sätzen kommt ein freies Feld.\nJetzt sollen Sie von jedem gelesenen Satz das letzte Wort eintragen. Bitte schreiben Sie genau das richtige Wort und in der gelesenen Reihenfolge auf. Danach deuten sie bitte mit zwei bis drei Stichworten den Sinn jedes Satzes an; das kann mit anderen Worten, andeutungsweise erfolgen.\nBitte beachten Sie, daß die Antwort nur dann als richtig gewertet wird, wenn sowohl der Inhalt des Satzes kurz angegeben, als auch das letzte Wort des Satzes richtig wiedergegeben ist!\nDer Versuch ist so aufgebaut, daß die Anzahl der Sätze steigt.\nWir beginnen mit zwei Sätzen. Nach dem Notieren drücken Sie auf Weiter, um den Versuch fortzusetzen.\nHaben Sie noch Fragen?\nNun probieren wir Ihre Aufgabe mit zwei Probesätzen.\nDrücken sie dazu auf Weiter!";
	protected static final long SHOW_PAGE_TIME = 5000;
	private List<Attribute> attributes;

	public void addDemoLevel() {
		addSentencePage("Aber so gelang es wohl niemandem, ihn vom Ofen weg und aus der Hütte zu bringen.");
		addSentencePage("Die Frau wurde von ihm schwanger und schenkte ihm einen gesunden Jungen.");
		addPage(new ReadingSpanResultDemoPage());
	}

	public void addLevel2() {
		addSentencePage("Betrunken schien er nicht zu sein, obwohl er ein wenig nach Alkohol roch.");
		addSentencePage("Kaum hatte ich den Brief in den Kasten geworfen, packte mich schon die Reue.");
		addPage(new ReadingSpanResultPage(2, 1));

		addSentencePage("Die Konsequenzen, welche es auch immer sein mochten, wollte ich auf mich nehmen.");
		addSentencePage("Während ich auf den Besuch wartete, räumte mein Mann noch schnell auf in der Wohnung.");
		addPage(new ReadingSpanResultPage(2, 2));

		addSentencePage("Strahlend blau war der Himmel, und das gute Wetter lockte zum Baden und Schwimmen.");
		addSentencePage("Mehrere Zuschauer waren auf die Bänke gestiegen, um die Musik besser zu hören.");
		addPage(new ReadingSpanResultPage(2, 3));
	}

	public void addLevel3() {
		addSentencePage("Peter schaute längst nicht mehr auf die Uhr, als wir über die Probleme sprachen.");
		addSentencePage("Wenn du das schaffst, dann können wir auch über deinen Wechsel zum Radsport reden.");
		addSentencePage("Ich sitze im Garten unter dem Baum und das Wetter ist immer noch herrlich.");
		addPage(new ReadingSpanResultPage(3, 1));

		addSentencePage("Wenn solch ein wichtiges Problem nicht richtig geklärt ist, muß man es im Kollegium diskutieren.");
		addSentencePage("Als wir eine Stunde später wieder vorbeikamen, fragte mein Vater ob der Tabak schmeckt.");
		addSentencePage("Das Zentrum für Wetterprognosen soll mit Computern ausgerüstet sein und selbständig arbeiten.");
		addPage(new ReadingSpanResultPage(3, 2));

		addSentencePage("Gestern Abend gingen wir ins Kino, weil wir uns einen spanischen Film ansehen wollten.");
		addSentencePage("Für Anna war die Woche keine Erholung gewesen, ständig kreisten  ihre Gedanken um den Sohn.");
		addSentencePage("Ich muß feststellen, das Verhalten des Kollegen Müller ist einmalig an unserer Schule.");
		addPage(new ReadingSpanResultPage(3, 3));
	}

	public void addLevel4() {
		addSentencePage("Es ist an der Zeit mit Mähen zu beginnen, sonst könnten die Körner verderben.");
		addSentencePage("Nun, ich ließ mir alle Modelle zeigen und kaufte endlich ziemlich teure Ringe.");
		addSentencePage("Ich saß auf dem Stuhl und leise kam Musik aus dem Radio in der Küche.");
		addSentencePage("Wir können nicht alle Bücher lesen, sondern müssen die für uns wertvollsten auswählen.");
		addPage(new ReadingSpanResultPage(4, 1));

		addSentencePage("Lange Zeit suchten wir nach seiner Brille, bis wir sie in der Küche fanden.");
		addSentencePage("An einem sonnigen Abend im August war Rainer mit seinem Moped in Halle unterwegs.");
		addSentencePage("Der Junge, in die Höhe geschossen, überragte mich um einen halben Kopf.");
		addSentencePage("Erst seit wir unsere zwei Kinder haben, sind wir eine frohe, vollständige Familie.");
		addPage(new ReadingSpanResultPage(4, 2));

		addSentencePage("Und auch seine Prinzipien übertrug er konsequent auf die Schüler und die Schule, die er leitete.");
		addSentencePage("Todesfurcht hatte uns gestreift, wie das immer ist, wenn man einen nahestehenden Menschen verliert.");
		addSentencePage("Eva setzte sich auf den Stuhl vom Herrn Direktor und machte ein ganz ernstes Gesicht.");
		addSentencePage("Die Frage, ob wir es schaffen, sagte er, sei für ihn nicht zulässig.");
		addPage(new ReadingSpanResultPage(4, 3));
	}

	public void addLevel5() {
		addSentencePage("Eine Weile saßen wir am Tisch, die Zeitung mit dem Interview lag zwischen uns.");
		addSentencePage("Dieser innige Austausch kann sich auch dort ergeben, wo beide einen ganz verschiedenen Beruf ausüben.");
		addSentencePage("Ein Taxi neben ihm auf der Straße fuhr etwas langsamer, und der Fahrer winkte.");
		addSentencePage("Noch nie hatte er seinen geliebten Vater mit einem so verzerrten Gesicht gesehen.");
		addSentencePage("In einem abgelegenen Schloß, inmitten grüner Wiesen, lebte ein alter Graf mit seiner Tochter.");
		addPage(new ReadingSpanResultPage(5, 1));

		addSentencePage("Wenn irgend möglich, fahre ich nach außerhalb und gehe ein bißchen im Wald spazieren.");
		addSentencePage("Er fuhr mit dem Moped zu schnell auf der nassen Straße und kam ins Rutschen.");
		addSentencePage("Das Häuschen, in dem ich hier wohne, liegt in der Nähe eines Sees.");
		addSentencePage("Er sah weiter das halb helle, halb dunkle Gesicht seines Vaters an und wartete.");
		addSentencePage("Diese Tiere haben sich unter der Schneedecke Gänge angelegt und Nester aus Gras gebaut.");
		addPage(new ReadingSpanResultPage(5, 2));

		addSentencePage("Die Stadt verfügt seit mehreren Jahren über ein modernes Krankenhaus mit rund 1000 Betten.");
		addSentencePage("Ich stieg aus dem Keller wieder hinauf, aus der Unter- in die Oberwelt.");
		addSentencePage("Etwas an Opa zwingt seinen Nächsten, ihn sehr zu achten und zu ehren.");
		addSentencePage("Da das Konzert im Freien stattfand, mußte es wegen dem einsetzenden Regen abgebrochen werden.");
		addSentencePage("Da krächzte der Vogel vor Schmerz laut auf und ließ seine Beute fallen.");
		addPage(new ReadingSpanResultPage(5, 3));
	}

	public void addLevel6() {
		addSentencePage("Meine Tochter hat nun schon selbst Familie, ihr eigenes Heim und ihre Freunde.");
		addSentencePage("Mit wachsender Technik drängen Fragen der Umwelteinflüsse und anderer Art in den Vordergrund.");
		addSentencePage("Er war seit dem Mittagessen nicht ins Haus zurückgegangen, um ein  Glas Milch zu trinken.");
		addSentencePage("Er fertigte sein Werkstück ohne jegliches Interesse an und gab es gleichgültig zur Kontrolle.");
		addSentencePage("In der Nacht stand Elke in ihren Mantel gehüllt an der Kreuzung unter einem Baum.");
		addSentencePage("Er streckte die Arme aus und wollte der Mutter die Hände vom Gesicht ziehen.");
		addPage(new ReadingSpanResultPage(6, 1));

		addSentencePage("Ganz besonders liebe ich stille Abende zu zweit, mit Wein, Musik, und dem Schein der Kerze.");
		addSentencePage("Als das Auto dicht an ihm vorbeifuhr, spürte er plötzlich einen heftigen Schlag.");
		addSentencePage("Wenn der Mann mit den Kindern nicht zurecht kommt, hat er ihre Liebe auch nicht verdient.");
		addSentencePage("Während ich so im Sessel saß und wartete, klingelte plötzlich das Telefon.");
		addSentencePage("Der Mann arbeitete seit einer Stunde in der Bibliothek an der Vorbereitung eines Textes.");
		addSentencePage("Um die Mittagszeit war die Mutter wieder herausgekommen und hatte nach ihm gesucht.");
		addPage(new ReadingSpanResultPage(6, 2));

		addSentencePage("Ich legte den Brief auf den Tisch und schloß für einen Moment die Augen.");
		addSentencePage("Am nächsten Tag fragte mich mein Vater, ob ich mitkomme, einen Strandkorb zu besorgen.");
		addSentencePage("Langsam richtete ich mich im Bett auf, schlug die Decke weg und sah zur Uhr.");
		addSentencePage("Sie kam zu einer Wiese, auf der gelbe Tulpen und schöner weißer Flieder blühten.");
		addSentencePage("Die Studenten hatten sich vorgenommen, vor Beginn der Vorlesung mit dem Dozenten zu sprechen.");
		addSentencePage("Die Sportler stellten rund achtzig Prozent der Weltmeister und errangen zwei Drittel aller möglichen Medaillen.");
		addPage(new ReadingSpanResultPage(6, 3));
	}

	public void addLevel7() {

		addSentencePage("In der Stille hörte er ein leises, hohes Summen wie von schwirrenden Insekten.");
		addSentencePage("Das Zimmer war von mir gewischt worden, jetzt mußte ich noch schnell die Fenster putzen.");
		addSentencePage("Wärme begann in Martins Zunge zu fließen, und seine Lippen verloren die Starre.");
		addSentencePage("Lange Zeit suchte ich nach dem Buch, bis ich es endlich in der Bibliothek fand.");
		addSentencePage("Er sah sich jetzt davongetragen und in die Nacht hinausgeschleudert wie ein Bündel Wäsche.");
		addSentencePage("Es war sehr schwer für mich allein, in der großen Wohnung zu leben.");
		addSentencePage("Die Spur des Mädchens führte zu einem sehr großen Felsblock und verlor sich an dieser Stelle.");
		addPage(new ReadingSpanResultPage(7, 1));

		addSentencePage("Das offene Fenster zitterte im Wind und dadurch, daß es plötzlich zuschlug, wurde ich wach.");
		addSentencePage("Ich hatte so viel für ihn übrig, wie noch nie für einen anderen Jungen.");
		addSentencePage("Die ruhige und harmonische Stimme meiner Mutter schien aus einer anderen Welt zu kommen.");
		addSentencePage("Da ging der Plattenspieler kaputt, und wir konnten die neu gekaufte Platte nicht hören.");
		addSentencePage("Im Vergleich mit den westeuropäischen Ländern bot sich 1941 in Ungarn ein trauriges Bild.");
		addSentencePage("Zur Hochzeit schenkte mir meine Mutter einen Schnellkochtopf, vor der Benutzung habe ich ein wenig Angst.");
		addSentencePage("Für Schwangere sind falsche Ernährung sowie Rauchen und viel Alkohol äußerst schädlich.");
		addPage(new ReadingSpanResultPage(7, 2));

		addSentencePage("Obwohl es erst Mittag war, war der Himmel dunkel, und wir mußten das Licht einschalten.");
		addSentencePage("Ein großer, schwarzer Hund sprang am Zaun empor und jaulte zu unserer Begrüßung.");
		addSentencePage("Da kam der Junge in den Raum, grüßte und ließ sich auf einen Stuhl fallen.");
		addSentencePage("Von Zeit zu Zeit ist es ratsam, die Kakteen und den Gummibaum mit Wasser zu besprühen. ");
		addSentencePage("Da es im Zimmer eng und wenig Platz ist, stehen die leeren Flaschen auf dem Schrank.");
		addSentencePage("Nach der Arbeit wollten wir in den Keller gehen und im Klubraum Tischtennis spielen.");
		addSentencePage("Ich ging in den Laden um ein paar Dinge zu kaufen, u.a. einen Füller.");
		addPage(new ReadingSpanResultPage(7, 3));
	}

	public void addLevel8() {
		addSentencePage("Ich sehe hin und begreife, aus dem Gestrüpp gibt es tatsächlich kein Entkommen.");
		addSentencePage("Nur dieser eine Tag blieb ihm in der fremden Stadt, um dort das Denkmal zu sehen.");
		addSentencePage("In einer Mondnacht hätten der Branntwein und der Schnaps den Kobold verlocken können.");
		addSentencePage("Meine Tante hat ein eigenartiges Hobby, sie sammelt seit Jahren alte Schuhe.");
		addSentencePage("Ihre Arbeit im Dorf ließ ihr genügend Zeit, sich um die Kranke zu kümmern.");
		addSentencePage("Der Mondschein und die Sterne glänzten sogar in den Fenstern jenseits der Straße.");
		addSentencePage("Dann fügte ich eine kleine Kunstpause ein, um sie auf die Folter zu spannen.");
		addSentencePage("So saß ich am Schreibtisch, und im Licht der Lampe lag der Brief.");
		addPage(new ReadingSpanResultPage(8, 1));

		addSentencePage("Ich mußte die Möglichkeit behalten, ruhig zu überlegen, um klar und überzeugend zu antworten.");
		addSentencePage("Er rief seine tapferen Söhne herbei und gab jedem von ihnen einen Pfeil.");
		addSentencePage("Der Chef schreibt dann die Zahlen auf und sagt entweder, das ist gut oder, das ist schlecht.");
		addSentencePage("Heute mußt du hier bleiben und unser Gast sein, es ist ja schon fast Abend.");
		addSentencePage("Sie forderten ihn jetzt auf, die Lippen zu schließen und zu versuchen, \"b\" zu sagen.");
		addSentencePage("Während Eva nach dem Kaffee anstand, blätterte ich in einer der gekauften Zeitschriften.");
		addSentencePage("Meine Freundinnen und ich kletterten auf die hohe Bretterbühne und drehten uns bis zum Umfallen.");
		addSentencePage("Ich lief ins Wohnzimmer meiner Eltern, stolperte über die Schwelle und fluchte.");
		addPage(new ReadingSpanResultPage(8, 2));

		addSentencePage("Kurz vor der Stadt verlor er die Gewalt über das Moped und fuhr in den Graben.");
		addSentencePage("Einen Augenblick vergaß Thomas vollkommen, daß der Vater noch am Fußende des Bettes stand.");
		addSentencePage("Bei einem kurzen Besuch der Messe unterstrichen die Gäste die große Bedeutung der Produkte.");
		addSentencePage("Das Grau der Häuser um mich herum wirkte in diesem Moment traurig auf mich.");
		addSentencePage("Zornig machte er mit der Hand eine befehlende Geste, und sie ging zögernd durch die Tür.");
		addSentencePage("Und dort hatte Uwe dann die Idee, in Zukunft Boxen als Sport betreiben zu wollen.");
		addSentencePage("Wer im Herbst Eicheln und Kastanien sammelt, schafft für den Winter Nahrung für die Tiere.");
		addSentencePage("Wir Menschen brauchen in unserem Leben nichts so sehr, wie die Bestätigung durch einen anderen Menschen.");
		addPage(new ReadingSpanResultPage(8, 3));
	}

	@Override
	public void addPages() {
		addPage(new SpanTestInstructionsPage("OperationSpanInstruction", INSTRUCTIONS));
		addDemoLevel();
		addLevel2();
		addLevel3();
		addLevel4();
		addLevel5();
		addLevel6();
		addLevel7();
		addLevel8();

		IWizardContainer wizardContainer = getContainer();
		if (wizardContainer instanceof IPageChangeProvider) {
			IPageChangeProvider pageChangeProvider = (IPageChangeProvider) wizardContainer;
			pageChangeProvider.addPageChangedListener(new PageChangeListener());
		}
	}

	public void addSentencePage(String sentence) {
		addPage(new DisplaySentenceWizardPage(false, sentence));
	}

	@Override
	public boolean canFinish() {
		return getNextPage(getContainer().getCurrentPage()) == null; // is it the last page?
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	@Override
	public boolean performFinish() {
		attributes = new ArrayList<Attribute>();
		for (IWizardPage page : getPages()) {
			if (ReadingSpanResultPage.ID.equals(page.getName())) {
				List<Attribute> results = ((ReadingSpanResultPage) page).collectResults();
				attributes.addAll(results);
			}
		}

		return true;
	}
}

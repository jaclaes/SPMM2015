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
	public static final String INSTRUCTIONS = "Ich zeige Ihnen jetzt nacheinander eine Reihe von S�tzen, die Sie lesen sollen.\nSie haben f�r jeden Satz 5 Sekunden Zeit.\nNachdem Sie einen Satz gelesen haben, biete ich Ihnen den n�chsten.\nNach mehreren S�tzen kommt ein freies Feld.\nJetzt sollen Sie von jedem gelesenen Satz das letzte Wort eintragen. Bitte schreiben Sie genau das richtige Wort und in der gelesenen Reihenfolge auf. Danach deuten sie bitte mit zwei bis drei Stichworten den Sinn jedes Satzes an; das kann mit anderen Worten, andeutungsweise erfolgen.\nBitte beachten Sie, da� die Antwort nur dann als richtig gewertet wird, wenn sowohl der Inhalt des Satzes kurz angegeben, als auch das letzte Wort des Satzes richtig wiedergegeben ist!\nDer Versuch ist so aufgebaut, da� die Anzahl der S�tze steigt.\nWir beginnen mit zwei S�tzen. Nach dem Notieren dr�cken Sie auf Weiter, um den Versuch fortzusetzen.\nHaben Sie noch Fragen?\nNun probieren wir Ihre Aufgabe mit zwei Probes�tzen.\nDr�cken sie dazu auf Weiter!";
	protected static final long SHOW_PAGE_TIME = 5000;
	private List<Attribute> attributes;

	public void addDemoLevel() {
		addSentencePage("Aber so gelang es wohl niemandem, ihn vom Ofen weg und aus der H�tte zu bringen.");
		addSentencePage("Die Frau wurde von ihm schwanger und schenkte ihm einen gesunden Jungen.");
		addPage(new ReadingSpanResultDemoPage());
	}

	public void addLevel2() {
		addSentencePage("Betrunken schien er nicht zu sein, obwohl er ein wenig nach Alkohol roch.");
		addSentencePage("Kaum hatte ich den Brief in den Kasten geworfen, packte mich schon die Reue.");
		addPage(new ReadingSpanResultPage(2, 1));

		addSentencePage("Die Konsequenzen, welche es auch immer sein mochten, wollte ich auf mich nehmen.");
		addSentencePage("W�hrend ich auf den Besuch wartete, r�umte mein Mann noch schnell auf in der Wohnung.");
		addPage(new ReadingSpanResultPage(2, 2));

		addSentencePage("Strahlend blau war der Himmel, und das gute Wetter lockte zum Baden und Schwimmen.");
		addSentencePage("Mehrere Zuschauer waren auf die B�nke gestiegen, um die Musik besser zu h�ren.");
		addPage(new ReadingSpanResultPage(2, 3));
	}

	public void addLevel3() {
		addSentencePage("Peter schaute l�ngst nicht mehr auf die Uhr, als wir �ber die Probleme sprachen.");
		addSentencePage("Wenn du das schaffst, dann k�nnen wir auch �ber deinen Wechsel zum Radsport reden.");
		addSentencePage("Ich sitze im Garten unter dem Baum und das Wetter ist immer noch herrlich.");
		addPage(new ReadingSpanResultPage(3, 1));

		addSentencePage("Wenn solch ein wichtiges Problem nicht richtig gekl�rt ist, mu� man es im Kollegium diskutieren.");
		addSentencePage("Als wir eine Stunde sp�ter wieder vorbeikamen, fragte mein Vater ob der Tabak schmeckt.");
		addSentencePage("Das Zentrum f�r Wetterprognosen soll mit Computern ausger�stet sein und selbst�ndig arbeiten.");
		addPage(new ReadingSpanResultPage(3, 2));

		addSentencePage("Gestern Abend gingen wir ins Kino, weil wir uns einen spanischen Film ansehen wollten.");
		addSentencePage("F�r Anna war die Woche keine Erholung gewesen, st�ndig kreisten  ihre Gedanken um den Sohn.");
		addSentencePage("Ich mu� feststellen, das Verhalten des Kollegen M�ller ist einmalig an unserer Schule.");
		addPage(new ReadingSpanResultPage(3, 3));
	}

	public void addLevel4() {
		addSentencePage("Es ist an der Zeit mit M�hen zu beginnen, sonst k�nnten die K�rner verderben.");
		addSentencePage("Nun, ich lie� mir alle Modelle zeigen und kaufte endlich ziemlich teure Ringe.");
		addSentencePage("Ich sa� auf dem Stuhl und leise kam Musik aus dem Radio in der K�che.");
		addSentencePage("Wir k�nnen nicht alle B�cher lesen, sondern m�ssen die f�r uns wertvollsten ausw�hlen.");
		addPage(new ReadingSpanResultPage(4, 1));

		addSentencePage("Lange Zeit suchten wir nach seiner Brille, bis wir sie in der K�che fanden.");
		addSentencePage("An einem sonnigen Abend im August war Rainer mit seinem Moped in Halle unterwegs.");
		addSentencePage("Der Junge, in die H�he geschossen, �berragte mich um einen halben Kopf.");
		addSentencePage("Erst seit wir unsere zwei Kinder haben, sind wir eine frohe, vollst�ndige Familie.");
		addPage(new ReadingSpanResultPage(4, 2));

		addSentencePage("Und auch seine Prinzipien �bertrug er konsequent auf die Sch�ler und die Schule, die er leitete.");
		addSentencePage("Todesfurcht hatte uns gestreift, wie das immer ist, wenn man einen nahestehenden Menschen verliert.");
		addSentencePage("Eva setzte sich auf den Stuhl vom Herrn Direktor und machte ein ganz ernstes Gesicht.");
		addSentencePage("Die Frage, ob wir es schaffen, sagte er, sei f�r ihn nicht zul�ssig.");
		addPage(new ReadingSpanResultPage(4, 3));
	}

	public void addLevel5() {
		addSentencePage("Eine Weile sa�en wir am Tisch, die Zeitung mit dem Interview lag zwischen uns.");
		addSentencePage("Dieser innige Austausch kann sich auch dort ergeben, wo beide einen ganz verschiedenen Beruf aus�ben.");
		addSentencePage("Ein Taxi neben ihm auf der Stra�e fuhr etwas langsamer, und der Fahrer winkte.");
		addSentencePage("Noch nie hatte er seinen geliebten Vater mit einem so verzerrten Gesicht gesehen.");
		addSentencePage("In einem abgelegenen Schlo�, inmitten gr�ner Wiesen, lebte ein alter Graf mit seiner Tochter.");
		addPage(new ReadingSpanResultPage(5, 1));

		addSentencePage("Wenn irgend m�glich, fahre ich nach au�erhalb und gehe ein bi�chen im Wald spazieren.");
		addSentencePage("Er fuhr mit dem Moped zu schnell auf der nassen Stra�e und kam ins Rutschen.");
		addSentencePage("Das H�uschen, in dem ich hier wohne, liegt in der N�he eines Sees.");
		addSentencePage("Er sah weiter das halb helle, halb dunkle Gesicht seines Vaters an und wartete.");
		addSentencePage("Diese Tiere haben sich unter der Schneedecke G�nge angelegt und Nester aus Gras gebaut.");
		addPage(new ReadingSpanResultPage(5, 2));

		addSentencePage("Die Stadt verf�gt seit mehreren Jahren �ber ein modernes Krankenhaus mit rund 1000 Betten.");
		addSentencePage("Ich stieg aus dem Keller wieder hinauf, aus der Unter- in die Oberwelt.");
		addSentencePage("Etwas an Opa zwingt seinen N�chsten, ihn sehr zu achten und zu ehren.");
		addSentencePage("Da das Konzert im Freien stattfand, mu�te es wegen dem einsetzenden Regen abgebrochen werden.");
		addSentencePage("Da kr�chzte der Vogel vor Schmerz laut auf und lie� seine Beute fallen.");
		addPage(new ReadingSpanResultPage(5, 3));
	}

	public void addLevel6() {
		addSentencePage("Meine Tochter hat nun schon selbst Familie, ihr eigenes Heim und ihre Freunde.");
		addSentencePage("Mit wachsender Technik dr�ngen Fragen der Umwelteinfl�sse und anderer Art in den Vordergrund.");
		addSentencePage("Er war seit dem Mittagessen nicht ins Haus zur�ckgegangen, um ein  Glas Milch zu trinken.");
		addSentencePage("Er fertigte sein Werkst�ck ohne jegliches Interesse an und gab es gleichg�ltig zur Kontrolle.");
		addSentencePage("In der Nacht stand Elke in ihren Mantel geh�llt an der Kreuzung unter einem Baum.");
		addSentencePage("Er streckte die Arme aus und wollte der Mutter die H�nde vom Gesicht ziehen.");
		addPage(new ReadingSpanResultPage(6, 1));

		addSentencePage("Ganz besonders liebe ich stille Abende zu zweit, mit Wein, Musik, und dem Schein der Kerze.");
		addSentencePage("Als das Auto dicht an ihm vorbeifuhr, sp�rte er pl�tzlich einen heftigen Schlag.");
		addSentencePage("Wenn der Mann mit den Kindern nicht zurecht kommt, hat er ihre Liebe auch nicht verdient.");
		addSentencePage("W�hrend ich so im Sessel sa� und wartete, klingelte pl�tzlich das Telefon.");
		addSentencePage("Der Mann arbeitete seit einer Stunde in der Bibliothek an der Vorbereitung eines Textes.");
		addSentencePage("Um die Mittagszeit war die Mutter wieder herausgekommen und hatte nach ihm gesucht.");
		addPage(new ReadingSpanResultPage(6, 2));

		addSentencePage("Ich legte den Brief auf den Tisch und schlo� f�r einen Moment die Augen.");
		addSentencePage("Am n�chsten Tag fragte mich mein Vater, ob ich mitkomme, einen Strandkorb zu besorgen.");
		addSentencePage("Langsam richtete ich mich im Bett auf, schlug die Decke weg und sah zur Uhr.");
		addSentencePage("Sie kam zu einer Wiese, auf der gelbe Tulpen und sch�ner wei�er Flieder bl�hten.");
		addSentencePage("Die Studenten hatten sich vorgenommen, vor Beginn der Vorlesung mit dem Dozenten zu sprechen.");
		addSentencePage("Die Sportler stellten rund achtzig Prozent der Weltmeister und errangen zwei Drittel aller m�glichen Medaillen.");
		addPage(new ReadingSpanResultPage(6, 3));
	}

	public void addLevel7() {

		addSentencePage("In der Stille h�rte er ein leises, hohes Summen wie von schwirrenden Insekten.");
		addSentencePage("Das Zimmer war von mir gewischt worden, jetzt mu�te ich noch schnell die Fenster putzen.");
		addSentencePage("W�rme begann in Martins Zunge zu flie�en, und seine Lippen verloren die Starre.");
		addSentencePage("Lange Zeit suchte ich nach dem Buch, bis ich es endlich in der Bibliothek fand.");
		addSentencePage("Er sah sich jetzt davongetragen und in die Nacht hinausgeschleudert wie ein B�ndel W�sche.");
		addSentencePage("Es war sehr schwer f�r mich allein, in der gro�en Wohnung zu leben.");
		addSentencePage("Die Spur des M�dchens f�hrte zu einem sehr gro�en Felsblock und verlor sich an dieser Stelle.");
		addPage(new ReadingSpanResultPage(7, 1));

		addSentencePage("Das offene Fenster zitterte im Wind und dadurch, da� es pl�tzlich zuschlug, wurde ich wach.");
		addSentencePage("Ich hatte so viel f�r ihn �brig, wie noch nie f�r einen anderen Jungen.");
		addSentencePage("Die ruhige und harmonische Stimme meiner Mutter schien aus einer anderen Welt zu kommen.");
		addSentencePage("Da ging der Plattenspieler kaputt, und wir konnten die neu gekaufte Platte nicht h�ren.");
		addSentencePage("Im Vergleich mit den westeurop�ischen L�ndern bot sich 1941 in Ungarn ein trauriges Bild.");
		addSentencePage("Zur Hochzeit schenkte mir meine Mutter einen Schnellkochtopf, vor der Benutzung habe ich ein wenig Angst.");
		addSentencePage("F�r Schwangere sind falsche Ern�hrung sowie Rauchen und viel Alkohol �u�erst sch�dlich.");
		addPage(new ReadingSpanResultPage(7, 2));

		addSentencePage("Obwohl es erst Mittag war, war der Himmel dunkel, und wir mu�ten das Licht einschalten.");
		addSentencePage("Ein gro�er, schwarzer Hund sprang am Zaun empor und jaulte zu unserer Begr��ung.");
		addSentencePage("Da kam der Junge in den Raum, gr��te und lie� sich auf einen Stuhl fallen.");
		addSentencePage("Von Zeit zu Zeit ist es ratsam, die Kakteen und den Gummibaum mit Wasser zu bespr�hen. ");
		addSentencePage("Da es im Zimmer eng und wenig Platz ist, stehen die leeren Flaschen auf dem Schrank.");
		addSentencePage("Nach der Arbeit wollten wir in den Keller gehen und im Klubraum Tischtennis spielen.");
		addSentencePage("Ich ging in den Laden um ein paar Dinge zu kaufen, u.a. einen F�ller.");
		addPage(new ReadingSpanResultPage(7, 3));
	}

	public void addLevel8() {
		addSentencePage("Ich sehe hin und begreife, aus dem Gestr�pp gibt es tats�chlich kein Entkommen.");
		addSentencePage("Nur dieser eine Tag blieb ihm in der fremden Stadt, um dort das Denkmal zu sehen.");
		addSentencePage("In einer Mondnacht h�tten der Branntwein und der Schnaps den Kobold verlocken k�nnen.");
		addSentencePage("Meine Tante hat ein eigenartiges Hobby, sie sammelt seit Jahren alte Schuhe.");
		addSentencePage("Ihre Arbeit im Dorf lie� ihr gen�gend Zeit, sich um die Kranke zu k�mmern.");
		addSentencePage("Der Mondschein und die Sterne gl�nzten sogar in den Fenstern jenseits der Stra�e.");
		addSentencePage("Dann f�gte ich eine kleine Kunstpause ein, um sie auf die Folter zu spannen.");
		addSentencePage("So sa� ich am Schreibtisch, und im Licht der Lampe lag der Brief.");
		addPage(new ReadingSpanResultPage(8, 1));

		addSentencePage("Ich mu�te die M�glichkeit behalten, ruhig zu �berlegen, um klar und �berzeugend zu antworten.");
		addSentencePage("Er rief seine tapferen S�hne herbei und gab jedem von ihnen einen Pfeil.");
		addSentencePage("Der Chef schreibt dann die Zahlen auf und sagt entweder, das ist gut oder, das ist schlecht.");
		addSentencePage("Heute mu�t du hier bleiben und unser Gast sein, es ist ja schon fast Abend.");
		addSentencePage("Sie forderten ihn jetzt auf, die Lippen zu schlie�en und zu versuchen, \"b\" zu sagen.");
		addSentencePage("W�hrend Eva nach dem Kaffee anstand, bl�tterte ich in einer der gekauften Zeitschriften.");
		addSentencePage("Meine Freundinnen und ich kletterten auf die hohe Bretterb�hne und drehten uns bis zum Umfallen.");
		addSentencePage("Ich lief ins Wohnzimmer meiner Eltern, stolperte �ber die Schwelle und fluchte.");
		addPage(new ReadingSpanResultPage(8, 2));

		addSentencePage("Kurz vor der Stadt verlor er die Gewalt �ber das Moped und fuhr in den Graben.");
		addSentencePage("Einen Augenblick verga� Thomas vollkommen, da� der Vater noch am Fu�ende des Bettes stand.");
		addSentencePage("Bei einem kurzen Besuch der Messe unterstrichen die G�ste die gro�e Bedeutung der Produkte.");
		addSentencePage("Das Grau der H�user um mich herum wirkte in diesem Moment traurig auf mich.");
		addSentencePage("Zornig machte er mit der Hand eine befehlende Geste, und sie ging z�gernd durch die T�r.");
		addSentencePage("Und dort hatte Uwe dann die Idee, in Zukunft Boxen als Sport betreiben zu wollen.");
		addSentencePage("Wer im Herbst Eicheln und Kastanien sammelt, schafft f�r den Winter Nahrung f�r die Tiere.");
		addSentencePage("Wir Menschen brauchen in unserem Leben nichts so sehr, wie die Best�tigung durch einen anderen Menschen.");
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

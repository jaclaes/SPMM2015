package org.cheetahplatform.modeler.readingunderstanding;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.operationspan.SpanTestInstructionsPage;
import org.cheetahplatform.modeler.readingspan.DisplaySentenceWizardPage;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class ReadingUnderstandingWizard extends Wizard {
	private List<Attribute> attributes;

	public static final String TEST_PREFIX = "LVT";
	public static final String INSTRUCTIONS = "Der Rechner bietet Ihnen nacheinander zehn Texte, die Sie mit einer Ihnen angenehmen Geschwindigkeit lesen sollen.\nDie Texte sind satzweise hintereinander angeordnet. Sobald Sie einen Satz gelesen haben, dr�cken Sie sofort auf Weiter und der n�chste Satz wird angezeigt.\nSie k�nnen jeden Satz nur einmal lesen.\nAm Ende jedes Textes erscheinen vier Fragen zu diesem Text, die Sie sofort schriftlich beantworten sollen.\nNach dem Notieren dr�cken Sie auf Weiter, um den Versuch fortzusetzen.\n\nHaben Sie noch Fragen zur Aufgabenstellung?\nDann k�nnen wir jetzt beginnen. Dr�cken Sie dazu auf Weiter!";

	@Override
	public void addPages() {
		addPage(new SpanTestInstructionsPage("OperationSpanInstruction", INSTRUCTIONS));
		addText1();
		addText2();
		addText3();
		addText4();
		addText5();
		addText6();
		addText7();
		addText8();
		addText9();
		addText10();
	}

	public void addSentencePage(String sentence) {
		addPage(new DisplaySentenceWizardPage(true, sentence));
	}

	public void addText1() {
		addSentencePage("Es war Mitternacht und unheimlich still im Wald.");
		addSentencePage("Pl�tzlich durchschnitt der Ruf des Wolfes die Nacht.");
		addSentencePage("Das qu�lende Signal war gefolgt von einer wahren Flut von Aktivit�ten.");
		addSentencePage("Alle Tiere des Urwaldes hatten erkannt, da� ihr K�nig, der L�we, eine wichtige Beratung anberaumt hatte.");
		addSentencePage("Vertreter jeder Art trafen rasche Vorbereitungen, um zur Lichtung am Flu� zu kommen.");
		addSentencePage("Dort wurden alle dringenden Versammlungen der Waldtiere abgehalten.");
		addSentencePage("Der Platz lag so g�nstig, da� alle Tiere ihn von ihren Behausungen aus schnell erreichen konnten.");
		addSentencePage("Die Lichtung lag in der Mitte des Waldes und die Tiere konnten so den gr��ten Teil des Waldes �berblicken.");
		addSentencePage("Der L�we lief unruhig hin und her und wartete auf seine Untertanen.");
		addSentencePage("Bald mu�ten die ersten Tiere eintreffen.");
		addSentencePage("Der Elefant und der Tiger gelangten als erste an.");
		addSentencePage("Danach kamen der Gorilla und die Schlange.");
		addSentencePage("Alle Tiere des Urwaldes hatten das Signal geh�rt und sich auf den Weg gemacht.");
		addSentencePage("Der Flu� war angeschwollen und drohte �ber die Ufer zu treten, denn die Fische dr�ngten sich an den Strand.");
		addSentencePage("Sie schwammen alle dicht am Ufer, denn auch sie wollten jedes Wort des L�wen h�ren.");
		addSentencePage("W�re es Tag gewesen, so h�tte man die Sonne nicht sehen k�nnen, denn die V�gel kreisten am Himmel.");
		addSentencePage("Allen voran war der Herrscher der L�fte, der Adler, eingetroffen.");
		addSentencePage("Der Beginn der Versammlung wurde hinausgez�gert, weil der Leopard noch nicht gesehen worden war.");
		addSentencePage("Es gab inzwischen eine Menge Spekulationen �ber den Grund des mittern�chtlichen Alarms.");
		addSentencePage("Schlie�lich langte er an, und die Beratung konnte beginnen.");

		addPage(new ReadingUnderstandingResultPage(1, "Wer kam zum Schlu� an?", "Was zerri� die Stille der Nacht?",
				"Wo fand die dringende Beratung statt?",
				"Alle Tiere des Urwaldes hatten erkannt, da� ihr K�nig, der L�we, eine gef�hrliche Jagd begonnen hatte.",
				"Keine Silbe der Verlautbarungen des K�nigs des Urwaldes wollten die Fische verpassen, und sie verlie�en daher das tiefe Wasser."));
	}

	public void addText10() {
		addSentencePage("Heute abend feiert Opa Theo seinen dreiundsiebzigsten Geburtstag, und es ist noch viel zu tun.");
		addSentencePage("Er erwartet die Tochter Erika mit den Enkeln Kai und Uwe, sowie sp�ter seinen Nachbarn.");
		addSentencePage("Von seinen ehemaligen Kollegen w�rde wohl niemand kommen.");
		addSentencePage("Zuerst hatte er die bestellte Torte vom B�cker abgeholt.");
		addSentencePage("Heute abend will er seine Dias zeigen.");
		addSentencePage("Er hatte die Br�cken und Kunstsch�tze von Venedig gesehen und die historischen Stra�en und Pl�tze von Rom.");
		addSentencePage("Im letzten Sommer war er am Strand der Ostsee und im Winter in den Bergen rund um Berchtesgaden gewesen.");
		addSentencePage("Er hatte sich vorgenommen, in diesem Jahr noch zwei weitere St�dte kennenzulernen.");
		addSentencePage("Die Reise nach Florenz hatte er schon gebucht, danach will er noch nach Athen fahren.");
		addSentencePage("Das Geld f�r diese Reisen hatte er sich schwer erarbeitet.");
		addSentencePage("Nach seiner Arbeit als G�rtner im Stadtpark pflanzte er im eigenen Garten Blumen an.");
		addSentencePage("Er hatte die Genehmigung, die Blumen am Markttag zu verkaufen.");
		addSentencePage("Die Leute kaufen gerne seine Schnittblumen, denn sie kennen ihn schon lange.");
		addSentencePage("Nachdem er seine Dias herausgesucht hatte, deckte Opa Theo den Kaffeetisch.");
		addSentencePage("Er holte das Porzellan aus dem Schrank und stellte Kerzen und Servietten hin.");
		addSentencePage("Danach setzte er sich erst einmal in den Sessel und rauchte ein Pfeifchen.");
		addSentencePage("Die Vorbereitungen waren getan, und nun erwartete er die Besucher.");
		addSentencePage("Zum Kaffee kamen die Tochter mit den Enkeln und ein Abgesandter der Kollegen mit einem K�rbchen voll Schokolade, Wein, Konfekt und Sekt.");
		addSentencePage("Zum Abendbrot traf auch sein letzter erwarteter Gast ein.");

		addPage(new ReadingUnderstandingResultPage(10, "Wer war der letzte erwartete Gast?", "Wie alt wurde Opa Theo?",
				"In welche St�dte wollte er in diesem Jahr fahren?",
				"Der Rentner Theo glaubte nicht, da� ihm seine ehemaligen Kollegen gratulieren w�rden.",
				"Die Vorbereitungen waren getroffen, aber er erwartete keine Besucher."));
	}

	public void addText2() {
		addSentencePage("Nachdem die Kinder Schularbeiten gemacht hatten, wollten sie baden gehen.");
		addSentencePage("Peter und Susi k�nnen schon schwimmen, der zehnj�hrige Frank noch nicht.");
		addSentencePage("Auf den Strand und den See hatten sie sich schon den ganzen Tag in der Schule gefreut.");
		addSentencePage("Frank holte Uwe ab, und gemeinsam gingen sie zu Andreas.");
		addSentencePage("Er wohnte mit seinen Eltern im Neubaugebiet am Puschkinplatz.");
		addSentencePage("Andreas mu�te seine kleine Schwester Sabine mitnehmen.");
		addSentencePage("Sie kennt die Jungen und geht �fter mit ihnen zum See.");
		addSentencePage("Leider war die einzige Badem�glichkeit des St�dtchens ziemlich weit vom Neubaugebiet entfernt.");
		addSentencePage("So mu�ten die Kinder zur Stra�enbahn laufen.");
		addSentencePage("Dabei kamen sie an einer Eisbude vorbei und jeder kaufte sich zun�chst ein Eis.");
		addSentencePage("Sabine bevorzugt Schokoladeneis, aber das war schon ausverkauft.");
		addSentencePage("Mit der Stra�enbahn fuhren sie bis zum Stadtrand.");
		addSentencePage("Hier stiegen viele Menschen aus der Bahn.");
		addSentencePage("Das sch�ne Wetter lockte viele Badelustige an den Strand und in das Wasser.");
		addSentencePage("Unterwegs unterhielten sich die Kinder �ber das gestrige Fu�ballspiel.");
		addSentencePage("Endlich waren sie an ihrem Ziel angekommen.");
		addSentencePage("Andreas und Susi spielten im Sand und bauten eine Sandburg, w�hrend die beiden anderen ins Wasser gingen.");
		addSentencePage("Die meisten Leute lagen in der Sonne, um sich zu br�unen.");
		addSentencePage("Auf der Wiese spielte eine kleine Gruppe Volleyball.");
		addSentencePage("Pl�tzlich h�rten sie Hilferufe und sahen, da� ihr Freund sich zu weit in das Wasser gewagt hatte, obwohl er nicht schwimmen kann.");

		addPage(new ReadingUnderstandingResultPage(2, "Wer rief um Hilfe?", "Von wo holten sie Andreas ab?",
				"Wor�ber unterhielten sich die Kinder auf dem Weg zum Strand?",
				"Andreas und Susi unterbrachen ihr Spiel und das Bauen einer Sandburg und gingen ins Wasser.",
				"Die Mehrzahl der Strandbesucher war nicht im Wasser, sondern sonnte sich."));
	}

	public void addText3() {
		addSentencePage("Wilhelm besuchte �fters die freundlichen T�pfersleute Janke.");
		addSentencePage("Es waren schon �ltere Menschen.");
		addSentencePage("Der Meister und Wilhelms Vater sind alte Schulfreunde.");
		addSentencePage("Ihre H�user liegen nicht weit voneinander entfernt und sie besuchen sich oft gegenseitig.");
		addSentencePage("Wilhelm zieht besonders die Werkstatt auf dem Hof an.");
		addSentencePage("Hier werden keineswegs nur T�pfe, Tassen und Kr�ge, sondern auch kleine Kunstwerke angefertigt.");
		addSentencePage("Die aus Ton gefertigten Greife, Katzen und Figuren nach antiken Mustern gefielen ihm besonders gut.");
		addSentencePage("Ein schreitender Hund wurde Wilhelms Liebling, den er sich sehnlichst w�nschte.");
		addSentencePage("Wilhelm und sein Bruder gingen wie ernste Arbeitsleute in der Werkstatt ein und aus.");
		addSentencePage("Sie hatten ein St�hlchen in der Werkstatt und verbrachten hier viele Stunden.");
		addSentencePage("W�hrend die T�pfer t�pferten, kneteten sie nicht weniger emsig allerlei Kuriosa aus Ton.");
		addSentencePage("Mit diesen Dingen schm�cken sie dann die Wohnung der Eltern.");
		addSentencePage("Wilhelms Vater freut sich �ber das Talent seines Sohnes.");
		addSentencePage("Er h�tte nichts dagegen, wenn sein Sohn einmal T�pfer werden w�rde.");
		addSentencePage("Wilhelms Bruder t�pfert zwar auch gerne, will aber wie der Vater Tischler werden.");
		addSentencePage("Der Meister, ein korpulenter Mann, trug ein veilchenblaues Wams mit wei�er Binde.");
		addSentencePage("Manchmal waren sie auch sonntags beim T�pfer, die Werkstatt war dann geschlossen, im Flur und Zimmer wei�er Sand gestreut.");
		addSentencePage("Eines Sonntags, welche Freude, bekam er ihn vom Meister geschenkt.");

		addPage(new ReadingUnderstandingResultPage(3, "Was bekam Wilhelm vom Meister geschenkt?", "Wie hie�en die T�pfersleute?",
				"Was kneteten Wilhelm und sein Bruder?",
				"Die sch�nen aus Ton gefertigten Greife, L�wen und Figuren nach antiken Mustern gefielen ihm nicht so gut.",
				"Sonntags war der Arbeitsraum f�r die kindlichen Besucher nicht zug�nglich."));
	}

	public void addText4() {
		addSentencePage("Als ich gestern mit Holger, Mike, Peter und anderen meiner Gruppe in der Eisbar war, wurde mir eigenartig zumute.");
		addSentencePage("Mike hatte eine M�nze in die Musikbox gesteckt.");
		addSentencePage("Sie pl�rrte einen Popmusik-Spitzenreiter.");
		addSentencePage("Mit wachsender Sorge beobachtete ich die Reaktionen meines besten Freundes.");
		addSentencePage("Ronald starrte hingerissen und ernst und klopfte den Takt der Musik auf die Tischplatte.");
		addSentencePage("Nicht, da� ich nicht auch f�r die meisten Dinge bin, die andere Jungen m�gen.");
		addSentencePage("Ich mag M�dchen mit weichen, blonden Haaren.");
		addSentencePage("Besonders solche zwischen 18 und 21 Jahren.");
		addSentencePage("Im Juli vergangenen Jahres habe ich mir ein Motorrad gekauft.");
		addSentencePage("Ich finde Jeans, T-Shirts und wei�e Schuhe schick.");
		addSentencePage("Wir gehen gern in diese Eisbar.");
		addSentencePage("Die Atmosph�re hier ist sehr sch�n.");
		addSentencePage("Dadurch, da� hier Rauchverbot gilt, kann man ungest�rt sitzen und sich unterhalten.");
		addSentencePage("Die meisten Gastst�tten und Caf�s in der Umgebung sind da nicht so gem�tlich.");
		addSentencePage("Manchmal gehen wir aber auch zur Disko.");
		addSentencePage("Aber dort ist mir das Verhalten meiner Freunde bei Musik noch nie aufgefallen.");
		addSentencePage("In der Disko habe ich aber nie Zeit und Lust sie zu beobachten.");
		addSentencePage("Ich habe nichts gegen Popmusik, aber ich finde, sie ist zur Unterhaltung gedacht und sollte nicht zu ernst genommen werden.");
		addSentencePage("Aber ausgerechnet er ist so gebannt und ernst bei dieser verr�ckten Musik.");

		addPage(new ReadingUnderstandingResultPage(4, "Wer ist so gebannt und ernst bei der Musik?", "Wo war die Gruppe?",
				"Wer steckte die M�nze in die Musikbox?",
				"Die Wirkung der Musik auf meinen Freund verfolgte ich mit zunehmendem Bedenken.",
				"F�r die meisten Dinge, die andere Jungen m�gen, bin ich nicht."));
	}

	public void addText5() {
		addSentencePage("In der n�chsten Woche habe ich eine sehr wichtige Pr�fung in Mathematik, die ich unbedingt bestehen mu�.");
		addSentencePage("Doch der heutige helle Sonnenschein und die milde, fr�hlingshafte Luft locken mehr als die Lehrb�cher und Hefter.");
		addSentencePage("Au�erdem dachte ich mir, da� ein wenig frische Luft und Ablenkung bei der Pr�fungsvorbereitung auch nicht schaden k�nnen.");
		addSentencePage("Gern h�tte ich einmal meinen Freund Martin wiedergetroffen, doch auch er hat Pr�fungen.");
		addSentencePage("Ich ging die Promenade unserer Stadt entlang und dachte nebenbei �ber meine Studienkollegen nach.");
		addSentencePage("In unserem Studentenwohnheim hatte ich einmal w�hrend einer Disco Peter kennengelernt.");
		addSentencePage("Am Rechner unseres Institutes hatte ich mich mit Jochen angefreundet.");
		addSentencePage("Er versteht viel von Computern und schreibt mir manchmal kleine Programme.");
		addSentencePage("Doch diese beiden waren f�r mich mehr Kumpel als Freunde.");
		addSentencePage("Sie helfen mir beim Studium und ich helfe ihnen ebenfalls in einigen F�chern.");
		addSentencePage("Langsam bekam ich Appetit auf einen Kaffee und einen Eisbecher mit Sahne.");
		addSentencePage("Ich bog in die n�chste Stra�e ein.");
		addSentencePage("Am Ende dieser Stra�e befindet sich ein nettes kleines Caf�, das gern von Studenten besucht wird.");
		addSentencePage("Als ich in die \"Mokkastube\" eintrat, sah ich ihn, der wohl genauso dachte wie ich, dort sitzen.");

		addPage(new ReadingUnderstandingResultPage(5, "Wen traf sie im Caf�?", "Wo dachte sie �ber ihren Freund nach?",
				"Wie hie� das Caf�?", "Mit zwei meiner Bekannten verbindet mich nur eine wechselseitige Unterst�tzung beim Lernen.",
				"Ich war mir sicher, meinen Freund Martin wiederzutreffen, hatte er doch auch Pr�fungen."));
	}

	public void addText6() {
		addSentencePage("Heute war ein besonderer Tag, unser vierter Hochzeitstag.");
		addSentencePage("Am Morgen hat mein Mann mich mit dem Fr�hst�ck geweckt.");
		addSentencePage("Auf dem Tablett standen auch zwei Gl�ser Sekt.");
		addSentencePage("Mein Mann J�rgen hatte das Fr�hst�ck liebevoll zubereitet.");
		addSentencePage("Das Brot lag getoastet in einem Weidenk�rbchen und die Eier trugen lustige Hauben aus Wolle, damit sie warm blieben.");
		addSentencePage("Diese Eierw�rmer hatte ich noch vor unserer Ehe aus Wollresten geh�kelt.");
		addSentencePage("Auch die Kaffeekanne war unter einer Haube verschwunden.");
		addSentencePage("Auf dem Tisch standen herrliche Rosen, eine Flasche Wein, und ein kleines P�ckchen lag da.");
		addSentencePage("J�rgen, mein Mann, schenkte mir eine kleine, goldene Kette.");
		addSentencePage("Zum Mittagessen gingen wir in den \"Wilden Eber\", ein Gasthaus mit rustikalem Charakter.");
		addSentencePage("An der Wand hingen Geweihe und Bilder mit Jagdszenen.");
		addSentencePage("Mein Mann w�hlte Ente und ich ein Steak.");
		addSentencePage("Nach dem Bezahlen gingen wir noch im Wald spazieren, und ich schlug vor, um den Waldsee herumzulaufen.");
		addSentencePage("Wir redeten �ber unser festliches Essen in der Waldgastst�tte.");
		addSentencePage("Unvermittelt fragte J�rgen mich, ob sie mir gefalle.");

		addPage(new ReadingUnderstandingResultPage(6, "Worauf bezieht sich J�rgens Frage �ber das Gefallen?",
				"Den wievielten Hochzeitstag feierten sie?", "Was hat J�rgen in der Gastst�tte gegessen?",
				"Auf dem blumengeschm�ckten Zimmertisch lag auch ein verh�lltes Schmuckst�ck.",
				"Wir redeten �ber mein festliches Geschenk in der Gastst�tte."));
	}

	public void addText7() {
		addSentencePage("Tom hatte eben seine Hausaufgaben fertiggestellt.");
		addSentencePage("Heute war es damit ziemlich sp�t geworden, denn er hatte vorher mit seinen Freunden Fu�ball gespielt.");
		addSentencePage("Dabei hatte er die Zeit vergessen.");
		addSentencePage("Als die Mutter rief, war es ziemlich sp�t, und er hatte ein schlechtes Gewissen.");
		addSentencePage("Es waren recht viele Hausaufgaben gewesen.");
		addSentencePage("In Mathematik und Geographie werden sie morgen eine Kurzarbeit schreiben.");
		addSentencePage("F�r Literatur mu�te er noch ein Gedicht von Kurt Tucholsky lernen.");
		addSentencePage("Am besten in der Schule gef�llt ihm Astronomie.");
		addSentencePage("Im vergangenen Jahr hatte er zu Weihnachten ein Fernrohr von seinem Vater bekommen.");
		addSentencePage("Damit beobachtet er nun die Sterne und dringt in die Sternbilder ein.");
		addSentencePage("Im letzten Monat hatte er schon die Sternbilder Adler und Skorpion gefunden.");
		addSentencePage("Ihnen folgten die Sternbilder Medusa und Schwan am abendlichen Himmel.");
		addSentencePage("Heute wurde der Beginn der Beobachtung des Sternbildes Bootes hinausgez�gert, weil die Mutter zum Abendbrot rief.");
		addSentencePage("Seine Schwester hatte f�r ihn Spiegeleier gebraten.");
		addSentencePage("Er i�t sie besonders gern mit Pfeffer und Paprika.");
		addSentencePage("Die ganze Familie wundert sich �ber seinen Geschmack.");
		addSentencePage("Alle erz�hlten ihre Erlebnisse und so zog sich das Abendessen ziemlich lange hin.");
		addSentencePage("Tom half seiner Mutter noch beim Abwaschen und Sp�len.");
		addSentencePage("Danach ging Tom ans Fernrohr, und da hatte er es wieder gefunden.");

		addPage(new ReadingUnderstandingResultPage(7, "Was hatte Tom gefunden?",
				"In welchen F�chern wird er morgen eine Arbeit schreiben?", "Was gab es zum Abendessen?",
				"Das Familiengeplauder am Abendbrottisch verursachte eine lange Verz�gerung der Beobachtung.",
				"Seine Schwester hatte ihm extra das Spiegelfernrohr ges�ubert."));
	}

	public void addText8() {
		addSentencePage("Am Sonntag morgen fuhr Steffen mit seinem Rad zu den Eltern hinaus.");
		addSentencePage("Sie wohnten in einem kleinen H�uschen mit Garten am Stadtrand.");
		addSentencePage("Es war sch�nes Wetter und sie fr�hst�ckten auf der Terrasse.");
		addSentencePage("Sp�ter half er der Mutter beim J�ten.");
		addSentencePage("Seine Schwester pfl�ckte Erdbeeren und Kirschen, die sie ihm mitgeben wollte.");
		addSentencePage("Die Mutter band ihm einen Blumenstrau� und stellte ihn vorerst in die Gie�kanne.");
		addSentencePage("Zum Mittagessen grillten sie W�rstchen, und der Vater holte Bier und Brause aus dem Keller.");
		addSentencePage("Sie plauderten �ber den Bruder Michael, die Schw�gerin Dora und ihr Baby Peter.");
		addSentencePage("Eigentlich wollten sie auch in den Garten kommen, aber Klein-Peter war krank geworden.");
		addSentencePage("Am Nachmittag nahm der Vater sich Steffens Fahrrad vor.");
		addSentencePage("Er pr�fte die Beleuchtung und die Bereifung und fuhr sogar eine Proberunde.");
		addSentencePage("Er packte ihm aus seinem unersch�pflichen Vorrat das fehlende Flickzeug in die Satteltasche.");
		addSentencePage("Steffen brauchte dringend ein sicheres Fahrrad, denn morgen beginnt sein Urlaub.");
		addSentencePage("Er will mit seinen Freunden eine Fahrradtour durch Deutschland machen.");
		addSentencePage("Dabei durfte nichts schief gehen, denn sie wollten jede Nacht in einer anderen Stadt verbringen.");
		addSentencePage("Die Mutter suchte noch Konserven im Keller.");
		addSentencePage("Dabei wurde es Abend, und Steffen verabschiedete sich von den Eltern.");
		addSentencePage("Zum Abschied kamen alle an das Gartentor.");
		addSentencePage("Schlie�lich holte sie auch noch den Beutel mit den gepfl�ckten Erdbeeren und Kirschen.");

		addPage(new ReadingUnderstandingResultPage(8, "Wer holte den Beutel mit den gepfl�ckten Erdbeeren und Kirschen?",
				"Wobei half Steffen der Mutter?", "Wo fr�hst�ckten sie?",
				"F�r ihren Bruder erntete sie Erdbeeren und Kirschen zum Mitnehmen.",
				"Der Vater lieh sich aus Steffens unersch�pflichem Vorrat das fehlende Flickzeug f�r seine Satteltasche."));
	}

	public void addText9() {
		addSentencePage("Es war ein milder Abend im August.");
		addSentencePage("Die 15-j�hrige Heike sa� in ihrem Zimmer und h�rte sich Musik an.");
		addSentencePage("Das Haus, in dem sie mit ihren Eltern wohnt, steht am Waldrand.");
		addSentencePage("Es liegt ziemlich einsam und manchmal hat Heike Angst, wenn sie alleine zu Hause ist.");
		addSentencePage("Pl�tzlich klingelte es.");
		addSentencePage("Heike �berlegte, wer wohl klingeln k�nnte und ging �ffnen.");
		addSentencePage("Es war ihr Freund Peter.");
		addSentencePage("Er wollte mit ihr in die Goethestra�e in den Jugendklub \"Mephisto\" gehen.");
		addSentencePage("Dort trat heute ein Puppenspieler mit einem M�rchen f�r Erwachsene auf.");
		addSentencePage("Zuerst aber wollte Heike ihrem Freund etwas zeigen.");
		addSentencePage("Sie gingen in Heikes Zimmer und dort lag eine neue CD an der Anlage.");
		addSentencePage("Heike legte die CD ein, und beide h�rten eine Weile zu.");
		addSentencePage("Peter gefiel sie nicht sonderlich.");
		addSentencePage("Ihn interessierte die alte Kunst der Puppenspieler weit st�rker.");
		addSentencePage("Vergangene Woche hatte er sich ein Handbuch �ber Vergangenheit und Gegenwart des Marionettentheaters gekauft.");
		addSentencePage("Peter dr�ngte jetzt zum Aufbruch.");
		addSentencePage("Er wollte auf keinen Fall zu sp�t kommen und bangte um freie Pl�tze.");
		addSentencePage("Heike suchte noch schnell die Zillestra�e im Stadtplan, denn von dort fuhr die Stra�enbahn zum Klubhaus ab.");
		addSentencePage("Peter spielte inzwischen mit den Katzen Susi und Mohrchen.");
		addSentencePage("Endlich hatte Heike sie gefunden und es konnte losgehen.");

		addPage(new ReadingUnderstandingResultPage(9, "Wen oder was hat Heike gefunden?", "Wo war Heike, als Peter kam?",
				"Wie hie� der Jugendklub?", "Heikes Freund besch�ftigte sich, w�hrend sie den Plan studierte, mit ihren Haustieren.",
				"Peter gefiel die CD diesmal besonders."));
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
			if (ReadingUnderstandingResultPage.ID.equals(page.getName())) {
				List<Attribute> results = ((ReadingUnderstandingResultPage) page).collectResults();
				attributes.addAll(results);
			}
		}

		return true;
	}
}

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
	public static final String INSTRUCTIONS = "Der Rechner bietet Ihnen nacheinander zehn Texte, die Sie mit einer Ihnen angenehmen Geschwindigkeit lesen sollen.\nDie Texte sind satzweise hintereinander angeordnet. Sobald Sie einen Satz gelesen haben, drücken Sie sofort auf Weiter und der nächste Satz wird angezeigt.\nSie können jeden Satz nur einmal lesen.\nAm Ende jedes Textes erscheinen vier Fragen zu diesem Text, die Sie sofort schriftlich beantworten sollen.\nNach dem Notieren drücken Sie auf Weiter, um den Versuch fortzusetzen.\n\nHaben Sie noch Fragen zur Aufgabenstellung?\nDann können wir jetzt beginnen. Drücken Sie dazu auf Weiter!";

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
		addSentencePage("Plötzlich durchschnitt der Ruf des Wolfes die Nacht.");
		addSentencePage("Das quälende Signal war gefolgt von einer wahren Flut von Aktivitäten.");
		addSentencePage("Alle Tiere des Urwaldes hatten erkannt, daß ihr König, der Löwe, eine wichtige Beratung anberaumt hatte.");
		addSentencePage("Vertreter jeder Art trafen rasche Vorbereitungen, um zur Lichtung am Fluß zu kommen.");
		addSentencePage("Dort wurden alle dringenden Versammlungen der Waldtiere abgehalten.");
		addSentencePage("Der Platz lag so günstig, daß alle Tiere ihn von ihren Behausungen aus schnell erreichen konnten.");
		addSentencePage("Die Lichtung lag in der Mitte des Waldes und die Tiere konnten so den größten Teil des Waldes überblicken.");
		addSentencePage("Der Löwe lief unruhig hin und her und wartete auf seine Untertanen.");
		addSentencePage("Bald mußten die ersten Tiere eintreffen.");
		addSentencePage("Der Elefant und der Tiger gelangten als erste an.");
		addSentencePage("Danach kamen der Gorilla und die Schlange.");
		addSentencePage("Alle Tiere des Urwaldes hatten das Signal gehört und sich auf den Weg gemacht.");
		addSentencePage("Der Fluß war angeschwollen und drohte über die Ufer zu treten, denn die Fische drängten sich an den Strand.");
		addSentencePage("Sie schwammen alle dicht am Ufer, denn auch sie wollten jedes Wort des Löwen hören.");
		addSentencePage("Wäre es Tag gewesen, so hätte man die Sonne nicht sehen können, denn die Vögel kreisten am Himmel.");
		addSentencePage("Allen voran war der Herrscher der Lüfte, der Adler, eingetroffen.");
		addSentencePage("Der Beginn der Versammlung wurde hinausgezögert, weil der Leopard noch nicht gesehen worden war.");
		addSentencePage("Es gab inzwischen eine Menge Spekulationen über den Grund des mitternächtlichen Alarms.");
		addSentencePage("Schließlich langte er an, und die Beratung konnte beginnen.");

		addPage(new ReadingUnderstandingResultPage(1, "Wer kam zum Schluß an?", "Was zerriß die Stille der Nacht?",
				"Wo fand die dringende Beratung statt?",
				"Alle Tiere des Urwaldes hatten erkannt, daß ihr König, der Löwe, eine gefährliche Jagd begonnen hatte.",
				"Keine Silbe der Verlautbarungen des Königs des Urwaldes wollten die Fische verpassen, und sie verließen daher das tiefe Wasser."));
	}

	public void addText10() {
		addSentencePage("Heute abend feiert Opa Theo seinen dreiundsiebzigsten Geburtstag, und es ist noch viel zu tun.");
		addSentencePage("Er erwartet die Tochter Erika mit den Enkeln Kai und Uwe, sowie später seinen Nachbarn.");
		addSentencePage("Von seinen ehemaligen Kollegen würde wohl niemand kommen.");
		addSentencePage("Zuerst hatte er die bestellte Torte vom Bäcker abgeholt.");
		addSentencePage("Heute abend will er seine Dias zeigen.");
		addSentencePage("Er hatte die Brücken und Kunstschätze von Venedig gesehen und die historischen Straßen und Plätze von Rom.");
		addSentencePage("Im letzten Sommer war er am Strand der Ostsee und im Winter in den Bergen rund um Berchtesgaden gewesen.");
		addSentencePage("Er hatte sich vorgenommen, in diesem Jahr noch zwei weitere Städte kennenzulernen.");
		addSentencePage("Die Reise nach Florenz hatte er schon gebucht, danach will er noch nach Athen fahren.");
		addSentencePage("Das Geld für diese Reisen hatte er sich schwer erarbeitet.");
		addSentencePage("Nach seiner Arbeit als Gärtner im Stadtpark pflanzte er im eigenen Garten Blumen an.");
		addSentencePage("Er hatte die Genehmigung, die Blumen am Markttag zu verkaufen.");
		addSentencePage("Die Leute kaufen gerne seine Schnittblumen, denn sie kennen ihn schon lange.");
		addSentencePage("Nachdem er seine Dias herausgesucht hatte, deckte Opa Theo den Kaffeetisch.");
		addSentencePage("Er holte das Porzellan aus dem Schrank und stellte Kerzen und Servietten hin.");
		addSentencePage("Danach setzte er sich erst einmal in den Sessel und rauchte ein Pfeifchen.");
		addSentencePage("Die Vorbereitungen waren getan, und nun erwartete er die Besucher.");
		addSentencePage("Zum Kaffee kamen die Tochter mit den Enkeln und ein Abgesandter der Kollegen mit einem Körbchen voll Schokolade, Wein, Konfekt und Sekt.");
		addSentencePage("Zum Abendbrot traf auch sein letzter erwarteter Gast ein.");

		addPage(new ReadingUnderstandingResultPage(10, "Wer war der letzte erwartete Gast?", "Wie alt wurde Opa Theo?",
				"In welche Städte wollte er in diesem Jahr fahren?",
				"Der Rentner Theo glaubte nicht, daß ihm seine ehemaligen Kollegen gratulieren würden.",
				"Die Vorbereitungen waren getroffen, aber er erwartete keine Besucher."));
	}

	public void addText2() {
		addSentencePage("Nachdem die Kinder Schularbeiten gemacht hatten, wollten sie baden gehen.");
		addSentencePage("Peter und Susi können schon schwimmen, der zehnjährige Frank noch nicht.");
		addSentencePage("Auf den Strand und den See hatten sie sich schon den ganzen Tag in der Schule gefreut.");
		addSentencePage("Frank holte Uwe ab, und gemeinsam gingen sie zu Andreas.");
		addSentencePage("Er wohnte mit seinen Eltern im Neubaugebiet am Puschkinplatz.");
		addSentencePage("Andreas mußte seine kleine Schwester Sabine mitnehmen.");
		addSentencePage("Sie kennt die Jungen und geht öfter mit ihnen zum See.");
		addSentencePage("Leider war die einzige Bademöglichkeit des Städtchens ziemlich weit vom Neubaugebiet entfernt.");
		addSentencePage("So mußten die Kinder zur Straßenbahn laufen.");
		addSentencePage("Dabei kamen sie an einer Eisbude vorbei und jeder kaufte sich zunächst ein Eis.");
		addSentencePage("Sabine bevorzugt Schokoladeneis, aber das war schon ausverkauft.");
		addSentencePage("Mit der Straßenbahn fuhren sie bis zum Stadtrand.");
		addSentencePage("Hier stiegen viele Menschen aus der Bahn.");
		addSentencePage("Das schöne Wetter lockte viele Badelustige an den Strand und in das Wasser.");
		addSentencePage("Unterwegs unterhielten sich die Kinder über das gestrige Fußballspiel.");
		addSentencePage("Endlich waren sie an ihrem Ziel angekommen.");
		addSentencePage("Andreas und Susi spielten im Sand und bauten eine Sandburg, während die beiden anderen ins Wasser gingen.");
		addSentencePage("Die meisten Leute lagen in der Sonne, um sich zu bräunen.");
		addSentencePage("Auf der Wiese spielte eine kleine Gruppe Volleyball.");
		addSentencePage("Plötzlich hörten sie Hilferufe und sahen, daß ihr Freund sich zu weit in das Wasser gewagt hatte, obwohl er nicht schwimmen kann.");

		addPage(new ReadingUnderstandingResultPage(2, "Wer rief um Hilfe?", "Von wo holten sie Andreas ab?",
				"Worüber unterhielten sich die Kinder auf dem Weg zum Strand?",
				"Andreas und Susi unterbrachen ihr Spiel und das Bauen einer Sandburg und gingen ins Wasser.",
				"Die Mehrzahl der Strandbesucher war nicht im Wasser, sondern sonnte sich."));
	}

	public void addText3() {
		addSentencePage("Wilhelm besuchte öfters die freundlichen Töpfersleute Janke.");
		addSentencePage("Es waren schon ältere Menschen.");
		addSentencePage("Der Meister und Wilhelms Vater sind alte Schulfreunde.");
		addSentencePage("Ihre Häuser liegen nicht weit voneinander entfernt und sie besuchen sich oft gegenseitig.");
		addSentencePage("Wilhelm zieht besonders die Werkstatt auf dem Hof an.");
		addSentencePage("Hier werden keineswegs nur Töpfe, Tassen und Krüge, sondern auch kleine Kunstwerke angefertigt.");
		addSentencePage("Die aus Ton gefertigten Greife, Katzen und Figuren nach antiken Mustern gefielen ihm besonders gut.");
		addSentencePage("Ein schreitender Hund wurde Wilhelms Liebling, den er sich sehnlichst wünschte.");
		addSentencePage("Wilhelm und sein Bruder gingen wie ernste Arbeitsleute in der Werkstatt ein und aus.");
		addSentencePage("Sie hatten ein Stühlchen in der Werkstatt und verbrachten hier viele Stunden.");
		addSentencePage("Während die Töpfer töpferten, kneteten sie nicht weniger emsig allerlei Kuriosa aus Ton.");
		addSentencePage("Mit diesen Dingen schmücken sie dann die Wohnung der Eltern.");
		addSentencePage("Wilhelms Vater freut sich über das Talent seines Sohnes.");
		addSentencePage("Er hätte nichts dagegen, wenn sein Sohn einmal Töpfer werden würde.");
		addSentencePage("Wilhelms Bruder töpfert zwar auch gerne, will aber wie der Vater Tischler werden.");
		addSentencePage("Der Meister, ein korpulenter Mann, trug ein veilchenblaues Wams mit weißer Binde.");
		addSentencePage("Manchmal waren sie auch sonntags beim Töpfer, die Werkstatt war dann geschlossen, im Flur und Zimmer weißer Sand gestreut.");
		addSentencePage("Eines Sonntags, welche Freude, bekam er ihn vom Meister geschenkt.");

		addPage(new ReadingUnderstandingResultPage(3, "Was bekam Wilhelm vom Meister geschenkt?", "Wie hießen die Töpfersleute?",
				"Was kneteten Wilhelm und sein Bruder?",
				"Die schönen aus Ton gefertigten Greife, Löwen und Figuren nach antiken Mustern gefielen ihm nicht so gut.",
				"Sonntags war der Arbeitsraum für die kindlichen Besucher nicht zugänglich."));
	}

	public void addText4() {
		addSentencePage("Als ich gestern mit Holger, Mike, Peter und anderen meiner Gruppe in der Eisbar war, wurde mir eigenartig zumute.");
		addSentencePage("Mike hatte eine Münze in die Musikbox gesteckt.");
		addSentencePage("Sie plärrte einen Popmusik-Spitzenreiter.");
		addSentencePage("Mit wachsender Sorge beobachtete ich die Reaktionen meines besten Freundes.");
		addSentencePage("Ronald starrte hingerissen und ernst und klopfte den Takt der Musik auf die Tischplatte.");
		addSentencePage("Nicht, daß ich nicht auch für die meisten Dinge bin, die andere Jungen mögen.");
		addSentencePage("Ich mag Mädchen mit weichen, blonden Haaren.");
		addSentencePage("Besonders solche zwischen 18 und 21 Jahren.");
		addSentencePage("Im Juli vergangenen Jahres habe ich mir ein Motorrad gekauft.");
		addSentencePage("Ich finde Jeans, T-Shirts und weiße Schuhe schick.");
		addSentencePage("Wir gehen gern in diese Eisbar.");
		addSentencePage("Die Atmosphäre hier ist sehr schön.");
		addSentencePage("Dadurch, daß hier Rauchverbot gilt, kann man ungestört sitzen und sich unterhalten.");
		addSentencePage("Die meisten Gaststätten und Cafés in der Umgebung sind da nicht so gemütlich.");
		addSentencePage("Manchmal gehen wir aber auch zur Disko.");
		addSentencePage("Aber dort ist mir das Verhalten meiner Freunde bei Musik noch nie aufgefallen.");
		addSentencePage("In der Disko habe ich aber nie Zeit und Lust sie zu beobachten.");
		addSentencePage("Ich habe nichts gegen Popmusik, aber ich finde, sie ist zur Unterhaltung gedacht und sollte nicht zu ernst genommen werden.");
		addSentencePage("Aber ausgerechnet er ist so gebannt und ernst bei dieser verrückten Musik.");

		addPage(new ReadingUnderstandingResultPage(4, "Wer ist so gebannt und ernst bei der Musik?", "Wo war die Gruppe?",
				"Wer steckte die Münze in die Musikbox?",
				"Die Wirkung der Musik auf meinen Freund verfolgte ich mit zunehmendem Bedenken.",
				"Für die meisten Dinge, die andere Jungen mögen, bin ich nicht."));
	}

	public void addText5() {
		addSentencePage("In der nächsten Woche habe ich eine sehr wichtige Prüfung in Mathematik, die ich unbedingt bestehen muß.");
		addSentencePage("Doch der heutige helle Sonnenschein und die milde, frühlingshafte Luft locken mehr als die Lehrbücher und Hefter.");
		addSentencePage("Außerdem dachte ich mir, daß ein wenig frische Luft und Ablenkung bei der Prüfungsvorbereitung auch nicht schaden können.");
		addSentencePage("Gern hätte ich einmal meinen Freund Martin wiedergetroffen, doch auch er hat Prüfungen.");
		addSentencePage("Ich ging die Promenade unserer Stadt entlang und dachte nebenbei über meine Studienkollegen nach.");
		addSentencePage("In unserem Studentenwohnheim hatte ich einmal während einer Disco Peter kennengelernt.");
		addSentencePage("Am Rechner unseres Institutes hatte ich mich mit Jochen angefreundet.");
		addSentencePage("Er versteht viel von Computern und schreibt mir manchmal kleine Programme.");
		addSentencePage("Doch diese beiden waren für mich mehr Kumpel als Freunde.");
		addSentencePage("Sie helfen mir beim Studium und ich helfe ihnen ebenfalls in einigen Fächern.");
		addSentencePage("Langsam bekam ich Appetit auf einen Kaffee und einen Eisbecher mit Sahne.");
		addSentencePage("Ich bog in die nächste Straße ein.");
		addSentencePage("Am Ende dieser Straße befindet sich ein nettes kleines Café, das gern von Studenten besucht wird.");
		addSentencePage("Als ich in die \"Mokkastube\" eintrat, sah ich ihn, der wohl genauso dachte wie ich, dort sitzen.");

		addPage(new ReadingUnderstandingResultPage(5, "Wen traf sie im Café?", "Wo dachte sie über ihren Freund nach?",
				"Wie hieß das Café?", "Mit zwei meiner Bekannten verbindet mich nur eine wechselseitige Unterstützung beim Lernen.",
				"Ich war mir sicher, meinen Freund Martin wiederzutreffen, hatte er doch auch Prüfungen."));
	}

	public void addText6() {
		addSentencePage("Heute war ein besonderer Tag, unser vierter Hochzeitstag.");
		addSentencePage("Am Morgen hat mein Mann mich mit dem Frühstück geweckt.");
		addSentencePage("Auf dem Tablett standen auch zwei Gläser Sekt.");
		addSentencePage("Mein Mann Jürgen hatte das Frühstück liebevoll zubereitet.");
		addSentencePage("Das Brot lag getoastet in einem Weidenkörbchen und die Eier trugen lustige Hauben aus Wolle, damit sie warm blieben.");
		addSentencePage("Diese Eierwärmer hatte ich noch vor unserer Ehe aus Wollresten gehäkelt.");
		addSentencePage("Auch die Kaffeekanne war unter einer Haube verschwunden.");
		addSentencePage("Auf dem Tisch standen herrliche Rosen, eine Flasche Wein, und ein kleines Päckchen lag da.");
		addSentencePage("Jürgen, mein Mann, schenkte mir eine kleine, goldene Kette.");
		addSentencePage("Zum Mittagessen gingen wir in den \"Wilden Eber\", ein Gasthaus mit rustikalem Charakter.");
		addSentencePage("An der Wand hingen Geweihe und Bilder mit Jagdszenen.");
		addSentencePage("Mein Mann wählte Ente und ich ein Steak.");
		addSentencePage("Nach dem Bezahlen gingen wir noch im Wald spazieren, und ich schlug vor, um den Waldsee herumzulaufen.");
		addSentencePage("Wir redeten über unser festliches Essen in der Waldgaststätte.");
		addSentencePage("Unvermittelt fragte Jürgen mich, ob sie mir gefalle.");

		addPage(new ReadingUnderstandingResultPage(6, "Worauf bezieht sich Jürgens Frage über das Gefallen?",
				"Den wievielten Hochzeitstag feierten sie?", "Was hat Jürgen in der Gaststätte gegessen?",
				"Auf dem blumengeschmückten Zimmertisch lag auch ein verhülltes Schmuckstück.",
				"Wir redeten über mein festliches Geschenk in der Gaststätte."));
	}

	public void addText7() {
		addSentencePage("Tom hatte eben seine Hausaufgaben fertiggestellt.");
		addSentencePage("Heute war es damit ziemlich spät geworden, denn er hatte vorher mit seinen Freunden Fußball gespielt.");
		addSentencePage("Dabei hatte er die Zeit vergessen.");
		addSentencePage("Als die Mutter rief, war es ziemlich spät, und er hatte ein schlechtes Gewissen.");
		addSentencePage("Es waren recht viele Hausaufgaben gewesen.");
		addSentencePage("In Mathematik und Geographie werden sie morgen eine Kurzarbeit schreiben.");
		addSentencePage("Für Literatur mußte er noch ein Gedicht von Kurt Tucholsky lernen.");
		addSentencePage("Am besten in der Schule gefällt ihm Astronomie.");
		addSentencePage("Im vergangenen Jahr hatte er zu Weihnachten ein Fernrohr von seinem Vater bekommen.");
		addSentencePage("Damit beobachtet er nun die Sterne und dringt in die Sternbilder ein.");
		addSentencePage("Im letzten Monat hatte er schon die Sternbilder Adler und Skorpion gefunden.");
		addSentencePage("Ihnen folgten die Sternbilder Medusa und Schwan am abendlichen Himmel.");
		addSentencePage("Heute wurde der Beginn der Beobachtung des Sternbildes Bootes hinausgezögert, weil die Mutter zum Abendbrot rief.");
		addSentencePage("Seine Schwester hatte für ihn Spiegeleier gebraten.");
		addSentencePage("Er ißt sie besonders gern mit Pfeffer und Paprika.");
		addSentencePage("Die ganze Familie wundert sich über seinen Geschmack.");
		addSentencePage("Alle erzählten ihre Erlebnisse und so zog sich das Abendessen ziemlich lange hin.");
		addSentencePage("Tom half seiner Mutter noch beim Abwaschen und Spülen.");
		addSentencePage("Danach ging Tom ans Fernrohr, und da hatte er es wieder gefunden.");

		addPage(new ReadingUnderstandingResultPage(7, "Was hatte Tom gefunden?",
				"In welchen Fächern wird er morgen eine Arbeit schreiben?", "Was gab es zum Abendessen?",
				"Das Familiengeplauder am Abendbrottisch verursachte eine lange Verzögerung der Beobachtung.",
				"Seine Schwester hatte ihm extra das Spiegelfernrohr gesäubert."));
	}

	public void addText8() {
		addSentencePage("Am Sonntag morgen fuhr Steffen mit seinem Rad zu den Eltern hinaus.");
		addSentencePage("Sie wohnten in einem kleinen Häuschen mit Garten am Stadtrand.");
		addSentencePage("Es war schönes Wetter und sie frühstückten auf der Terrasse.");
		addSentencePage("Später half er der Mutter beim Jäten.");
		addSentencePage("Seine Schwester pflückte Erdbeeren und Kirschen, die sie ihm mitgeben wollte.");
		addSentencePage("Die Mutter band ihm einen Blumenstrauß und stellte ihn vorerst in die Gießkanne.");
		addSentencePage("Zum Mittagessen grillten sie Würstchen, und der Vater holte Bier und Brause aus dem Keller.");
		addSentencePage("Sie plauderten über den Bruder Michael, die Schwägerin Dora und ihr Baby Peter.");
		addSentencePage("Eigentlich wollten sie auch in den Garten kommen, aber Klein-Peter war krank geworden.");
		addSentencePage("Am Nachmittag nahm der Vater sich Steffens Fahrrad vor.");
		addSentencePage("Er prüfte die Beleuchtung und die Bereifung und fuhr sogar eine Proberunde.");
		addSentencePage("Er packte ihm aus seinem unerschöpflichen Vorrat das fehlende Flickzeug in die Satteltasche.");
		addSentencePage("Steffen brauchte dringend ein sicheres Fahrrad, denn morgen beginnt sein Urlaub.");
		addSentencePage("Er will mit seinen Freunden eine Fahrradtour durch Deutschland machen.");
		addSentencePage("Dabei durfte nichts schief gehen, denn sie wollten jede Nacht in einer anderen Stadt verbringen.");
		addSentencePage("Die Mutter suchte noch Konserven im Keller.");
		addSentencePage("Dabei wurde es Abend, und Steffen verabschiedete sich von den Eltern.");
		addSentencePage("Zum Abschied kamen alle an das Gartentor.");
		addSentencePage("Schließlich holte sie auch noch den Beutel mit den gepflückten Erdbeeren und Kirschen.");

		addPage(new ReadingUnderstandingResultPage(8, "Wer holte den Beutel mit den gepflückten Erdbeeren und Kirschen?",
				"Wobei half Steffen der Mutter?", "Wo frühstückten sie?",
				"Für ihren Bruder erntete sie Erdbeeren und Kirschen zum Mitnehmen.",
				"Der Vater lieh sich aus Steffens unerschöpflichem Vorrat das fehlende Flickzeug für seine Satteltasche."));
	}

	public void addText9() {
		addSentencePage("Es war ein milder Abend im August.");
		addSentencePage("Die 15-jährige Heike saß in ihrem Zimmer und hörte sich Musik an.");
		addSentencePage("Das Haus, in dem sie mit ihren Eltern wohnt, steht am Waldrand.");
		addSentencePage("Es liegt ziemlich einsam und manchmal hat Heike Angst, wenn sie alleine zu Hause ist.");
		addSentencePage("Plötzlich klingelte es.");
		addSentencePage("Heike überlegte, wer wohl klingeln könnte und ging öffnen.");
		addSentencePage("Es war ihr Freund Peter.");
		addSentencePage("Er wollte mit ihr in die Goethestraße in den Jugendklub \"Mephisto\" gehen.");
		addSentencePage("Dort trat heute ein Puppenspieler mit einem Märchen für Erwachsene auf.");
		addSentencePage("Zuerst aber wollte Heike ihrem Freund etwas zeigen.");
		addSentencePage("Sie gingen in Heikes Zimmer und dort lag eine neue CD an der Anlage.");
		addSentencePage("Heike legte die CD ein, und beide hörten eine Weile zu.");
		addSentencePage("Peter gefiel sie nicht sonderlich.");
		addSentencePage("Ihn interessierte die alte Kunst der Puppenspieler weit stärker.");
		addSentencePage("Vergangene Woche hatte er sich ein Handbuch über Vergangenheit und Gegenwart des Marionettentheaters gekauft.");
		addSentencePage("Peter drängte jetzt zum Aufbruch.");
		addSentencePage("Er wollte auf keinen Fall zu spät kommen und bangte um freie Plätze.");
		addSentencePage("Heike suchte noch schnell die Zillestraße im Stadtplan, denn von dort fuhr die Straßenbahn zum Klubhaus ab.");
		addSentencePage("Peter spielte inzwischen mit den Katzen Susi und Mohrchen.");
		addSentencePage("Endlich hatte Heike sie gefunden und es konnte losgehen.");

		addPage(new ReadingUnderstandingResultPage(9, "Wen oder was hat Heike gefunden?", "Wo war Heike, als Peter kam?",
				"Wie hieß der Jugendklub?", "Heikes Freund beschäftigte sich, während sie den Plan studierte, mit ihren Haustieren.",
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

package at.zugal.fitnesse.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.rules.IToken;

import at.zugal.fitnesse.editor.WikiTableTokenScanner;
import at.zugal.fitnesse.editor.WikiTextTokenScanner;
import at.zugal.fitnesse.editor.syntax.WikiDocument;
import at.zugal.fitnesse.editor.syntax.WikiParser;
import at.zugal.fitnesse.editor.syntax.WikiTable;

public class TestHelper {

	public static WikiTable parseTable(String toParse) {
		WikiTableTokenScanner scanner = new WikiTableTokenScanner();
		scanner.setRange(new Document(toParse), 0, toParse.length());
		WikiParser parser = new WikiParser(scanner);
		WikiDocument document = parser.parse();
		WikiTable table = (WikiTable) document.getChildren().get(0);

		return table;
	}

	private static List<IToken> scan(String toScan, WikiTextTokenScanner scanner) {
		scanner.setRange(new Document(toScan), 0, toScan.length());

		List<IToken> tokens = new ArrayList<IToken>();
		IToken token;
		do {
			token = scanner.nextToken();
			tokens.add(token);
		} while (!token.isEOF());

		return tokens;
	}

	public static List<IToken> scanTable(String toScan) {
		return scan(toScan, new WikiTableTokenScanner());
	}

	public static List<IToken> scanText(String toScan) {
		return scan(toScan, new WikiTextTokenScanner());
	}

}

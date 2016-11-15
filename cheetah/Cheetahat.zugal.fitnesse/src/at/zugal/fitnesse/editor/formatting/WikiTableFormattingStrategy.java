package at.zugal.fitnesse.editor.formatting;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.formatter.IFormattingStrategy;

import at.zugal.fitnesse.editor.WikiTableTokenScanner;
import at.zugal.fitnesse.editor.syntax.WikiDocument;
import at.zugal.fitnesse.editor.syntax.WikiParser;

public class WikiTableFormattingStrategy implements IFormattingStrategy {

	@Override
	public String format(String content, boolean isLineStart, String indentation, int[] positions) {
		WikiTableTokenScanner scanner = new WikiTableTokenScanner();
		scanner.setRange(new Document(content), 0, content.length());

		WikiParser parser = new WikiParser(scanner);
		WikiDocument document = parser.parse();
		WikiTableFormattingVistor visitor = new WikiTableFormattingVistor();
		document.accept(visitor);

		return visitor.getFormatted();
	}

	@Override
	public void formatterStarts(String initialIndentation) {
		// ignore
	}

	@Override
	public void formatterStops() {
		// ignore
	}

}

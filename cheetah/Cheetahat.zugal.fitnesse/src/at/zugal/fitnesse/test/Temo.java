package at.zugal.fitnesse.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.junit.Test;

import at.zugal.fitnesse.editor.WikiTableTokenScanner;
import at.zugal.fitnesse.editor.contentAssist.WikiTableCellContentAssistProcessor;
import at.zugal.fitnesse.editor.syntax.WikiDocument;
import at.zugal.fitnesse.editor.syntax.WikiParser;
import at.zugal.fitnesse.editor.syntax.WikiTable;

public class Temo {

	@Test
	public void bug61() throws Exception {
		String content = "|123456         |\r\n" + "|start   |123456|\r\n" + "|createAn       |\r\n" + "|\r\n ";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length() - 3);
		assertEquals(1, proposals.size());
		Document contentDocument = new Document(content);
		proposals.get(0).apply(contentDocument);

		assertEquals("|123456         |\r\n" + "|start   |123456|\r\n" + "|createAn       |\r\n" + "|createAn|\r\n ", contentDocument.get());
	}

	private WikiDocument parse(String toParse) {
		WikiTableTokenScanner scanner = new WikiTableTokenScanner();
		scanner.setRange(new Document(toParse), 0, toParse.length());
		WikiParser parser = new WikiParser(scanner);
		WikiDocument document = parser.parse();
		return document;
	}
}

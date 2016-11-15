package at.zugal.fitnesse.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Point;
import org.junit.Test;

import at.zugal.fitnesse.editor.WikiTableTokenScanner;
import at.zugal.fitnesse.editor.contentAssist.WikiTableCellContentAssistProcessor;
import at.zugal.fitnesse.editor.syntax.WikiDocument;
import at.zugal.fitnesse.editor.syntax.WikiParser;
import at.zugal.fitnesse.editor.syntax.WikiTable;

public class WikiTableCellContentAssisProcessorTest {
	@Test
	public void avoidWhiteSpaceProposal() throws Exception {
		String content = "|header|\n|header|\n|    |nextCell|\n|";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length());
		assertEquals("Should not propose the empty string.", 0, proposals.size());
	}

	@Test
	public void bug58() throws Exception {
		String content = "|${IMPERATIVE_SESE_FIXTURE} 	|\r\n" + "|start |	${IMPERATIVE_SESE_FIXTURE} 	|\r\n"
				+ "|children |	component 	|1|\r\n|";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length());
		assertEquals(1, proposals.size());
		Document adaptedContent = new Document(content);
		ICompletionProposal proposal = proposals.get(0);
		proposal.apply(adaptedContent);

		assertEquals(content + "children|", adaptedContent.get());
		assertEquals(adaptedContent.getLength(), proposal.getSelection(adaptedContent).x);
	}

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

	@Test
	public void bug67() throws Exception {
		String content = "|header|\n|header|\n|line1|nextCell|\n|a|NEXT";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length());
		assertEquals(1, proposals.size());
		Document contentDocument = new Document(content);
		proposals.get(0).apply(contentDocument);

		assertEquals("|header|\n|header|\n|line1|nextCell|\n|a|nextCell|", contentDocument.get());
	}

	@Test
	public void bug69() throws Exception {
		String content = "|header|\n|header|\n|startNode|xor1|";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length());
		assertEquals(1, proposals.size());
		Document contentDocument = new Document(content);
		proposals.get(0).apply(contentDocument);

		assertEquals("|header|\n|header|\n|line1|nextCell|\n|a|nextCell|", contentDocument.get());
	}

	@Test
	public void emptyProposal() throws Exception {
		String content = "|header|\n|header|\n||nextCell|\n|";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length());
		assertEquals("Should not propose the empty string.", 0, proposals.size());
	}

	@Test
	public void ignoreHeader() throws Exception {
		String content = "|header|\n|header|";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), 10);
		assertEquals(0, proposals.size());
	}

	@Test
	public void invalidPrefix() throws Exception {
		String content = "|header|\n|header|\n|line1|nextCell|\n|a|invalidPrefix";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length());
		assertEquals(0, proposals.size());
	}

	private WikiDocument parse(String content) {
		WikiTableTokenScanner scanner = new WikiTableTokenScanner();
		scanner.setRange(new Document(content), 0, content.length());
		WikiParser parser = new WikiParser(scanner);
		WikiDocument document = parser.parse();
		return document;
	}

	@Test
	public void samePrefix() throws Exception {
		String content = "|header|\n|header|\n|line1|nextCell|\n|a|nextCell";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length());
		assertEquals(1, proposals.size());
		Document contentDocument = new Document(content);
		proposals.get(0).apply(contentDocument);

		assertEquals(content + "|", contentDocument.get());
	}

	@Test
	public void simpleProposal() throws Exception {
		String content = "|123456          |\r\n" + "|start   |123456 |\r\n" + "|createAn|Andjoin|\r\n" + "|";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length());
		assertEquals(1, proposals.size());
		Document contentDocument = new Document(content);
		proposals.get(0).apply(contentDocument);

		assertEquals(content + "createAn|", contentDocument.get());
	}

	@Test
	public void singleLine() throws Exception {
		String content = "|header|\n|header|\n|lin";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length() - 3);
		assertEquals(0, proposals.size());
	}

	@Test
	public void threeLines() throws Exception {
		String content = "|h1       |\r\n|start    |\r\n|blablabla|\r\n|";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length());
		assertEquals(1, proposals.size());
		Document contentDocument = new Document(content);
		proposals.get(0).apply(contentDocument);

		assertEquals(content + "blablabla|", contentDocument.get());
	}

	@Test
	public void twoLinesSingleProposal() throws Exception {
		String content = "|header|\n|header|\n|line1|\n|";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length());
		assertEquals(1, proposals.size());
		Document contentDocument = new Document(content);
		proposals.get(0).apply(contentDocument);

		assertEquals(content + "line1|", contentDocument.get());
	}

	@Test
	public void twoLinesSingleProposalSecondColumn() throws Exception {
		String content = "|header|\n|header|\n|line1|nextCell|\n|a|";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length());
		assertEquals(1, proposals.size());
		Document contentDocument = new Document(content);
		proposals.get(0).apply(contentDocument);

		assertEquals(content + "nextCell|", contentDocument.get());
	}

	@Test
	public void validPrefix() throws Exception {
		String content = "|header|\n|header|\n|line1|nextCell|\n|a|next";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length());
		assertEquals(1, proposals.size());
		Document contentDocument = new Document(content);
		proposals.get(0).apply(contentDocument);

		assertEquals(content + "Cell|", contentDocument.get());
	}

	@Test
	public void validPrefixCursorPosition() throws Exception {
		String content = "|header|\n|header|\n|line1|nextCell|\n|a|next";
		WikiDocument document = parse(content);

		List<ICompletionProposal> proposals = new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) document.getChildren()
				.get(0), content.length());
		assertEquals(1, proposals.size());
		Document contentDocument = new Document(content);
		ICompletionProposal toApply = proposals.get(0);
		toApply.apply(contentDocument);

		String newContent = content + "Cell|";
		assertEquals(newContent, contentDocument.get());
		assertEquals(new Point(newContent.length(), 0), toApply.getSelection(contentDocument));
	}

}

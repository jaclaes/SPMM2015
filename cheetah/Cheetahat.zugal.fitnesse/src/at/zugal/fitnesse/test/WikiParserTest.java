package at.zugal.fitnesse.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.jface.text.Document;
import org.junit.Test;

import at.zugal.fitnesse.editor.WikiTableTokenScanner;
import at.zugal.fitnesse.editor.syntax.IWikiSyntax;
import at.zugal.fitnesse.editor.syntax.WikiDefineUsage;
import at.zugal.fitnesse.editor.syntax.WikiDocument;
import at.zugal.fitnesse.editor.syntax.WikiLinebreak;
import at.zugal.fitnesse.editor.syntax.WikiParser;
import at.zugal.fitnesse.editor.syntax.WikiPlainText;
import at.zugal.fitnesse.editor.syntax.WikiTable;
import at.zugal.fitnesse.editor.syntax.WikiTableCell;
import at.zugal.fitnesse.editor.syntax.WikiTableColumnSeparator;
import at.zugal.fitnesse.editor.syntax.WikiTableRow;

public class WikiParserTest {
	@Test
	public void bug61() throws Exception {
		String toParse = "|123456         |\r\n" + "|start   |123456|\r\n" + "|createAn       |\r\n ";
		WikiDocument document = parse(toParse);

		List<IWikiSyntax> children = document.getChildren();
		assertEquals("Expected table and plain text.", 2, children.size());
		assertEquals(IWikiSyntax.TABLE, children.get(0).getType());
		assertEquals(IWikiSyntax.PLAIN_TEXT, children.get(1).getType());
	}

	private WikiDocument parse(String toParse) {
		WikiTableTokenScanner scanner = new WikiTableTokenScanner();
		scanner.setRange(new Document(toParse), 0, toParse.length());
		WikiParser parser = new WikiParser(scanner);
		WikiDocument document = parser.parse();
		return document;
	}

	@Test
	public void parseSingleCell() throws Exception {
		String toParse = "|Header|";
		WikiDocument document = parse(toParse);

		List<IWikiSyntax> children = document.getChildren();
		assertEquals(1, children.size());
		IWikiSyntax table = children.get(0);
		assertEquals(WikiTable.class, table.getClass());

		IWikiSyntax row = table.getChildren().get(0);
		List<IWikiSyntax> tableChildren = table.getChildren();
		assertEquals(1, tableChildren.size());
		assertEquals(WikiTableRow.class, row.getClass());

		List<IWikiSyntax> rowChildren = row.getChildren();
		assertEquals(1, row.getChildren().size());
		IWikiSyntax cell = rowChildren.get(0);
		assertEquals(WikiTableCell.class, cell.getClass());

		List<IWikiSyntax> cellChildren = cell.getChildren();
		assertEquals(3, cellChildren.size());
		assertEquals(WikiTableColumnSeparator.class, cellChildren.get(0).getClass());
		assertEquals(WikiPlainText.class, cellChildren.get(1).getClass());
		assertEquals(WikiTableColumnSeparator.class, cellChildren.get(2).getClass());
	}

	@Test
	public void parseSingleLine() throws Exception {
		String toParse = "|${Header} Plain|Second Column|";
		WikiDocument document = parse(toParse);

		List<IWikiSyntax> children = document.getChildren();
		assertEquals(1, children.size());
		assertEquals(WikiTable.class, children.get(0).getClass());

		WikiTable table = (WikiTable) children.get(0);
		List<IWikiSyntax> tableChildren = table.getChildren();

		assertEquals(1, table.getChildren().size());
		assertEquals(WikiTableRow.class, tableChildren.get(0).getClass());

		IWikiSyntax row = table.getChildren().get(0);
		assertEquals(2, row.getChildren().size());
		assertEquals(WikiTableCell.class, row.getChildren().get(0).getClass());
		assertEquals(WikiTableCell.class, row.getChildren().get(1).getClass());

		IWikiSyntax firstCell = row.getChildren().get(0);
		List<IWikiSyntax> firstCellChildren = firstCell.getChildren();
		assertEquals(4, firstCellChildren.size());
		assertEquals(WikiTableColumnSeparator.class, firstCellChildren.get(0).getClass());
		assertEquals(WikiDefineUsage.class, firstCellChildren.get(1).getClass());
		assertEquals(WikiPlainText.class, firstCellChildren.get(2).getClass());
		assertEquals(WikiTableColumnSeparator.class, firstCellChildren.get(3).getClass());

		IWikiSyntax secondCell = row.getChildren().get(1);
		List<IWikiSyntax> secondCellChildren = secondCell.getChildren();
		assertEquals(2, secondCellChildren.size());
		assertEquals(WikiPlainText.class, secondCellChildren.get(0).getClass());
		assertEquals(WikiTableColumnSeparator.class, secondCellChildren.get(1).getClass());
	}

	@Test
	public void parseTwoLines() throws Exception {
		String toParse = "|${Header} Plain|Second Column|\n|Second Row|";
		WikiDocument document = parse(toParse);

		List<IWikiSyntax> children = document.getChildren();
		assertEquals(1, children.size());
		assertEquals(WikiTable.class, children.get(0).getClass());

		WikiTable table = (WikiTable) children.get(0);
		List<IWikiSyntax> tableChildren = table.getChildren();

		assertEquals(2, table.getChildren().size());
		assertEquals(WikiTableRow.class, tableChildren.get(0).getClass());
		assertEquals(WikiTableRow.class, tableChildren.get(1).getClass());

		IWikiSyntax firstRow = table.getChildren().get(0);
		assertEquals(2, firstRow.getChildren().size());
		assertEquals(WikiTableCell.class, firstRow.getChildren().get(0).getClass());
		assertEquals(WikiTableCell.class, firstRow.getChildren().get(1).getClass());

		IWikiSyntax secondCellOfFirstRow = firstRow.getChildren().get(1);
		List<IWikiSyntax> secondCellOfFirstRowChildren = secondCellOfFirstRow.getChildren();
		assertEquals(3, secondCellOfFirstRowChildren.size());
		assertEquals(WikiPlainText.class, secondCellOfFirstRowChildren.get(0).getClass());
		assertEquals(WikiTableColumnSeparator.class, secondCellOfFirstRowChildren.get(1).getClass());
		assertEquals(WikiLinebreak.class, secondCellOfFirstRowChildren.get(2).getClass());

		IWikiSyntax secondRow = table.getChildren().get(1);
		assertEquals(1, secondRow.getChildren().size());
		assertEquals(WikiTableCell.class, secondRow.getChildren().get(0).getClass());

		List<IWikiSyntax> secondRowChildren = secondRow.getChildren();
		assertEquals(1, secondRowChildren.size());
		assertEquals(WikiTableCell.class, secondRowChildren.get(0).getClass());

		List<IWikiSyntax> cellChildren = secondRowChildren.get(0).getChildren();
		assertEquals(3, cellChildren.size());
		assertEquals(WikiTableColumnSeparator.class, cellChildren.get(0).getClass());
		assertEquals(WikiPlainText.class, cellChildren.get(1).getClass());
		assertEquals(WikiTableColumnSeparator.class, cellChildren.get(2).getClass());
	}
}

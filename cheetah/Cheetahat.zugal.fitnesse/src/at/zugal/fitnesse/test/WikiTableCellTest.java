package at.zugal.fitnesse.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.jface.text.Document;
import org.junit.Test;

import at.zugal.fitnesse.editor.WikiToken;
import at.zugal.fitnesse.editor.syntax.WikiPlainText;
import at.zugal.fitnesse.editor.syntax.WikiTableCell;
import at.zugal.fitnesse.editor.syntax.WikiTableColumnSeparator;

public class WikiTableCellTest {
	@Test
	public void padd() throws Exception {
		WikiTableCell cell = new WikiTableCell(null);
		Document document = new Document("123|");
		WikiPlainText child1 = new WikiPlainText(null, new WikiToken.PlainTextToken(document, 0, 3));
		cell.addChild(child1);
		WikiTableColumnSeparator child2 = new WikiTableColumnSeparator(cell, new WikiToken.TableColumnSeparatorToken(document, 3, 1));
		cell.addChild(child2);
		assertEquals("123 |", cell.padd(5));
	}

	@Test
	public void paddDoubleSeparator() throws Exception {
		WikiTableCell cell = new WikiTableCell(null);
		Document document = new Document("|123|");
		WikiTableColumnSeparator child0 = new WikiTableColumnSeparator(cell, new WikiToken.TableColumnSeparatorToken(document, 0, 1));
		cell.addChild(child0);
		WikiPlainText child1 = new WikiPlainText(null, new WikiToken.PlainTextToken(document, 1, 3));
		cell.addChild(child1);
		WikiTableColumnSeparator child2 = new WikiTableColumnSeparator(cell, new WikiToken.TableColumnSeparatorToken(document, 4, 1));
		cell.addChild(child2);
		assertEquals("|123 |", cell.padd(6));
	}
}

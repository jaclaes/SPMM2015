package at.zugal.fitnesse.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.jface.text.rules.IToken;
import org.junit.Test;

import at.zugal.fitnesse.editor.WikiToken;
import at.zugal.fitnesse.editor.syntax.IWikiSyntax;

public class WikiTableTokenScannerTest {
	@Test
	public void scanBug48() throws Exception {
		List<IToken> tokens = TestHelper.scanTable("|111|\n|222|invalidText\n|3333|");

		assertEquals(13, tokens.size());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(0)).getType());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(1)).getType());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(2)).getType());
		assertEquals(IWikiSyntax.LINE_BREAK, ((WikiToken) tokens.get(3)).getType());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(4)).getType());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(5)).getType());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(6)).getType());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(7)).getType());
		assertEquals(IWikiSyntax.LINE_BREAK, ((WikiToken) tokens.get(8)).getType());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(9)).getType());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(10)).getType());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(11)).getType());
		assertEquals(true, tokens.get(12).isEOF());
	}

	@Test
	public void scanDefine() throws Exception {
		List<IToken> tokens = TestHelper.scanTable("|${Some Define}|");

		assertEquals(4, tokens.size());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(0)).getType());
		assertEquals(IWikiSyntax.DEFINE_USAGE, ((WikiToken) tokens.get(1)).getType());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(2)).getType());
		assertEquals(true, tokens.get(3).isEOF());
	}

	@Test
	public void scanDefineAndPlainText() throws Exception {
		List<IToken> tokens = TestHelper.scanTable("|${Some Define}    |");

		assertEquals(5, tokens.size());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(0)).getType());
		assertEquals(IWikiSyntax.DEFINE_USAGE, ((WikiToken) tokens.get(1)).getType());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(2)).getType());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(3)).getType());
		assertEquals(true, tokens.get(4).isEOF());
	}

	@Test
	public void scanEmpty() throws Exception {
		List<IToken> tokens = TestHelper.scanTable("");

		assertEquals(1, tokens.size());
		assertEquals(true, tokens.get(0).isEOF());
	}

	@Test
	public void scanHeader1() throws Exception {
		List<IToken> tokens = TestHelper.scanTable("|header");

		assertEquals(3, tokens.size());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(0)).getType());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(1)).getType());
		assertEquals(true, tokens.get(2).isEOF());
	}

	@Test
	public void scanHeader2() throws Exception {
		List<IToken> tokens = TestHelper.scanTable("|header|");

		assertEquals(4, tokens.size());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(0)).getType());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(1)).getType());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(2)).getType());
		assertEquals(true, tokens.get(3).isEOF());
	}

	@Test
	public void scanMultipleLines() throws Exception {
		List<IToken> tokens = TestHelper.scanTable("|header|\n|second|");

		assertEquals(8, tokens.size());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(0)).getType());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(1)).getType());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(2)).getType());
		assertEquals(IWikiSyntax.LINE_BREAK, ((WikiToken) tokens.get(3)).getType());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(4)).getType());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(5)).getType());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(6)).getType());
		assertEquals(true, tokens.get(7).isEOF());
	}

	@Test
	public void scanPlainTextAndDefine() throws Exception {
		List<IToken> tokens = TestHelper.scanTable("|   ${Some Define}|");

		assertEquals(5, tokens.size());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(0)).getType());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(1)).getType());
		assertEquals(IWikiSyntax.DEFINE_USAGE, ((WikiToken) tokens.get(2)).getType());
		assertEquals(IWikiSyntax.TABLE_COLUMN_SEPARATOR, ((WikiToken) tokens.get(3)).getType());
		assertEquals(true, tokens.get(4).isEOF());
	}
}

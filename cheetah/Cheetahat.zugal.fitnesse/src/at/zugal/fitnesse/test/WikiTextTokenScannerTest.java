package at.zugal.fitnesse.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.jface.text.rules.IToken;
import org.junit.Test;

import at.zugal.fitnesse.editor.WikiToken;
import at.zugal.fitnesse.editor.syntax.IWikiSyntax;

public class WikiTextTokenScannerTest {
	@Test
	public void parseAnotherIllegalHeader1() throws Exception {
		String toScan = "!1some header";
		List<IToken> tokens = TestHelper.scanText(toScan);

		assertEquals(2, tokens.size());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(0)).getType());
		assertEquals(true, tokens.get(1).isEOF());
	}

	@Test
	public void parseAnotherIllegalHeader2() throws Exception {
		String toScan = "!2some header";
		List<IToken> tokens = TestHelper.scanText(toScan);

		assertEquals(2, tokens.size());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(0)).getType());
		assertEquals(true, tokens.get(1).isEOF());
	}

	@Test
	public void parseDefineUsage() throws Exception {
		String toScan = "${DEFINE}";
		List<IToken> tokens = TestHelper.scanText(toScan);

		assertEquals(2, tokens.size());
		assertEquals(IWikiSyntax.DEFINE_USAGE, ((WikiToken) tokens.get(0)).getType());
		assertEquals(true, tokens.get(1).isEOF());
	}

	@Test
	public void parseDefineUsageAndPlainText() throws Exception {
		String toScan = "${DEFINE} is a define";
		List<IToken> tokens = TestHelper.scanText(toScan);

		assertEquals(3, tokens.size());
		assertEquals(IWikiSyntax.DEFINE_USAGE, ((WikiToken) tokens.get(0)).getType());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(1)).getType());
		assertEquals(true, tokens.get(2).isEOF());
	}

	@Test
	public void parseHeader1() throws Exception {
		String toScan = "!1 some header";
		List<IToken> tokens = TestHelper.scanText(toScan);

		assertEquals(2, tokens.size());
		assertEquals(((WikiToken) tokens.get(0)).getType(), IWikiSyntax.HEADER_1);
		assertEquals(tokens.get(1).isEOF(), true);
	}

	@Test
	public void parseHeader2() throws Exception {
		String toScan = "!2 some header";
		List<IToken> tokens = TestHelper.scanText(toScan);

		assertEquals(2, tokens.size());
		assertEquals(((WikiToken) tokens.get(0)).getType(), IWikiSyntax.HEADER_2);
		assertEquals(tokens.get(1).isEOF(), true);
	}

	@Test
	public void parseIllegalHeader1() throws Exception {
		String toScan = " !1 some header";
		List<IToken> tokens = TestHelper.scanText(toScan);

		assertEquals(2, tokens.size());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(0)).getType());
		assertEquals(true, tokens.get(1).isEOF());
	}

	@Test
	public void parseIllegalHeader2() throws Exception {
		String toScan = " !2 some header";
		List<IToken> tokens = TestHelper.scanText(toScan);

		assertEquals(2, tokens.size());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(0)).getType());
		assertEquals(true, tokens.get(1).isEOF());
	}

	@Test
	public void parsePlainText() throws Exception {
		String toScan = "some plain text";
		List<IToken> tokens = TestHelper.scanText(toScan);

		assertEquals(2, tokens.size());
		assertEquals(IWikiSyntax.PLAIN_TEXT, ((WikiToken) tokens.get(0)).getType());
		assertEquals(true, tokens.get(1).isEOF());
	}
}

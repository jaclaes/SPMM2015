package at.zugal.fitnesse.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.jface.text.Document;
import org.junit.Test;

import at.zugal.fitnesse.editor.WikiToken;
import at.zugal.fitnesse.editor.WikiToken.PlainTextToken;

public class WikiTokenTest {
	@Test
	public void getUntil() throws Exception {
		PlainTextToken token = new WikiToken.PlainTextToken(new Document("123456"), 1, 4);

		assertEquals("2345", token.getTextUntil(5));
		assertEquals("", token.getTextUntil(1));
	}

	@Test
	public void trim() throws Exception {
		String content = " content ";
		PlainTextToken token = new WikiToken.PlainTextToken(new Document(content), 0, content.length());
		assertEquals("content", token.trim());
	}

	@Test
	public void trimBug57() throws Exception {
		String content = "\t";
		PlainTextToken token = new WikiToken.PlainTextToken(new Document(content), 0, content.length());
		assertEquals("", token.trimLineBreak());
	}

	@Test
	public void trimEmpty() throws Exception {
		String content = "";
		PlainTextToken token = new WikiToken.PlainTextToken(new Document(content), 0, content.length());
		assertEquals("", token.trim());
	}

	@Test
	public void trimLineBreak() throws Exception {
		String content = "content\t\r\n";
		PlainTextToken token = new WikiToken.PlainTextToken(new Document(content), 0, content.length());
		assertEquals("content", token.trimLineBreak());
	}

	@Test
	public void trimLineBreak1() throws Exception {
		String content = "\n";
		PlainTextToken token = new WikiToken.PlainTextToken(new Document(content), 0, content.length());
		assertEquals("", token.trimLineBreak());
	}

	@Test
	public void trimLineBreak2() throws Exception {
		String content = "\r\n";
		PlainTextToken token = new WikiToken.PlainTextToken(new Document(content), 0, content.length());
		assertEquals("", token.trimLineBreak());
	}

	@Test
	public void trimLineBreak3() throws Exception {
		String content = "bla\r\n";
		PlainTextToken token = new WikiToken.PlainTextToken(new Document(content), 0, content.length());
		assertEquals("bla", token.trimLineBreak());
	}

	@Test
	public void trimLineBreakBug57() throws Exception {
		String content = "\t";
		PlainTextToken token = new WikiToken.PlainTextToken(new Document(content), 0, content.length());
		assertEquals("", token.trimLineBreak());
	}

	@Test
	public void trimLineBreakEmpty() throws Exception {
		String content = "";
		PlainTextToken token = new WikiToken.PlainTextToken(new Document(content), 0, content.length());
		assertEquals("", token.trim());
	}

	@Test
	public void trimTab() throws Exception {
		String content = " content\t";
		PlainTextToken token = new WikiToken.PlainTextToken(new Document(content), 0, content.length());
		assertEquals("content", token.trim());
	}

	@Test
	public void trimTrailingWhiteSpaceOnly() throws Exception {
		String content = "content ";
		PlainTextToken token = new WikiToken.PlainTextToken(new Document(content), 0, content.length());
		assertEquals("content", token.trim());
	}

	@Test
	public void trimWhiteSpaceString() throws Exception {
		String content = "     ";
		PlainTextToken token = new WikiToken.PlainTextToken(new Document(content), 0, content.length());
		assertEquals("", token.trim());
	}
}

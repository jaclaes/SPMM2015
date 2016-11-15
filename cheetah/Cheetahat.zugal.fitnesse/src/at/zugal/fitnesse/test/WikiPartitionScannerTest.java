package at.zugal.fitnesse.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.rules.IToken;
import org.junit.Test;

import at.zugal.fitnesse.editor.WikiPartitionScanner;

public class WikiPartitionScannerTest {
	@Test
	public void scanTable() throws Exception {
		String content = "|header|\n|secondHeader|";
		WikiPartitionScanner scanner = new WikiPartitionScanner();
		scanner.setRange(new Document(content), 0, content.length());

		IToken token = scanner.nextToken();
		assertEquals(WikiPartitionScanner.TABLE, token.getData());
	}
}

package at.zugal.fitnesse.editor.rule;

import static at.zugal.fitnesse.editor.contentAssist.WikiTableContentAssistProcessor.TABLE_COLUMN_SEPARATOR_STRING;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

import at.zugal.fitnesse.Constants;

public class TableRule extends SingleLineRule {

	public TableRule(IToken token) {
		super(TABLE_COLUMN_SEPARATOR_STRING, TABLE_COLUMN_SEPARATOR_STRING, token);
	}

	@Override
	protected IToken doEvaluate(ICharacterScanner scanner, boolean resume) {
		if (resume) {
			scanToPlainTextLine(scanner);
			return fToken;
		}

		int character = scanner.read();
		if (character == fStartSequence[0]) {
			scanToPlainTextLine(scanner);
			return fToken;
		}

		scanner.unread();
		return Token.UNDEFINED;
	}

	private void scanToPlainTextLine(ICharacterScanner scanner) {
		while (true) {
			int currentChar = scanner.read();

			if (currentChar == '\n') {
				int nextChar = scanner.read();
				if (nextChar != Constants.TABLE_COLUMN_SEPARATOR_CHAR) {
					scanner.unread();
					return;
				}
			} else if (currentChar == ICharacterScanner.EOF) {
				return;
			}
		}
	}
}

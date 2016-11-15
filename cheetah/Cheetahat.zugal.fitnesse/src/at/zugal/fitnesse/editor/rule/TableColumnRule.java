package at.zugal.fitnesse.editor.rule;

import static at.zugal.fitnesse.editor.contentAssist.WikiTableContentAssistProcessor.TABLE_COLUMN_SEPARATOR_STRING;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class TableColumnRule extends SingleLineRule {

	public TableColumnRule(IToken token) {
		super(TABLE_COLUMN_SEPARATOR_STRING, TABLE_COLUMN_SEPARATOR_STRING, token);
	}

	@Override
	protected IToken doEvaluate(ICharacterScanner scanner, boolean resume) {
		if (resume) {
			if (endSequenceDetected(scanner)) {
				return fToken;
			}
		} else {
			int c = scanner.read();

			if (c == fStartSequence[0]) {
				if (sequenceDetected(scanner, fStartSequence, false)) {
					if (endSequenceDetected(scanner)) {
						// unread the last token
						scanner.unread();
						return fToken;
					}
				}
			}
		}

		scanner.unread();
		return Token.UNDEFINED;
	}

}

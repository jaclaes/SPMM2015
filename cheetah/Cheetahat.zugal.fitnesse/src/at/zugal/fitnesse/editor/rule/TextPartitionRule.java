package at.zugal.fitnesse.editor.rule;

import static org.eclipse.jface.text.rules.ICharacterScanner.EOF;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import at.zugal.fitnesse.Constants;

public class TextPartitionRule implements IPredicateRule {
	private final IToken token;

	public TextPartitionRule(IToken token) {
		this.token = token;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		return evaluate(scanner, false);
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		if (resume) {
			if (tokenDetected(scanner, ' ')) {
				return token;
			}
		} else {
			int currentChar = scanner.read();
			if (currentChar != Constants.TABLE_COLUMN_SEPARATOR_CHAR) {
				if (tokenDetected(scanner, currentChar)) {
					return token;
				}
			}
		}

		scanner.unread();
		return Token.UNDEFINED;
	}

	@Override
	public IToken getSuccessToken() {
		return token;
	}

	private boolean tokenDetected(ICharacterScanner scanner, int currentChar) {
		boolean hasContent = false;

		while (true) {
			currentChar = scanner.read();

			if (currentChar == Constants.TABLE_COLUMN_SEPARATOR_CHAR) {
				scanner.unread();
				break;
			} else if (currentChar == EOF) {
				scanner.unread();
				break;
			}

			hasContent = true;
		}

		return hasContent;
	}

}

package at.zugal.fitnesse.editor.rule;

import static org.eclipse.jface.text.rules.ICharacterScanner.EOF;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.Token;

/**
 *Rule similar to {@link PatternRule}, except for the case that all characters are accepted except a set of predefined ones.
 * 
 * @author user
 * 
 */
public class InvertedPatternRule implements IPredicateRule {
	private final IToken token;
	private final char[] breakCharacters;

	/**
	 * Create a new rule.
	 * 
	 * @param token
	 *            the token to be returned if the rule matches
	 * @param breakCharacters
	 *            the characters the rule does not match, all others are accepted
	 */
	public InvertedPatternRule(IToken token, char[] breakCharacters) {
		Assert.isNotNull(token);
		Assert.isNotNull(breakCharacters);
		Assert.isTrue(breakCharacters.length > 0);

		this.token = token;
		this.breakCharacters = breakCharacters;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		return evaluate(scanner, false);
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		if (resume) {
			if (tokenDetected(scanner, breakCharacters[0])) {
				return token;
			}
		} else {
			int currentChar = scanner.read();
			boolean validStartCharacter = true;

			for (char breakChar : breakCharacters) {
				if (breakChar == currentChar) {
					validStartCharacter = false;
					break;
				}
			}

			if (validStartCharacter) {
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

	protected boolean tokenDetected(ICharacterScanner scanner, int currentChar) {
		boolean hasContent = false;

		while (true) {
			currentChar = scanner.read();

			boolean invalidCharacter = false;
			for (char breakChar : breakCharacters) {
				if (breakChar == currentChar) {
					invalidCharacter = true;
					break;
				}
			}

			if (invalidCharacter) {
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

package at.zugal.fitnesse.editor.rule;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.WordRule;

public class SimpleWordRule extends WordRule {

	private static class SimpleWordDetector implements IWordDetector {
		private final String word;
		private int position;

		public SimpleWordDetector(String word) {
			this.word = word;
		}

		@Override
		public boolean isWordPart(char c) {
			if (position >= word.length()) {
				return false;
			}

			boolean wordPart = word.charAt(position) == c;
			if (wordPart) {
				position++;
			}

			return wordPart;
		}

		@Override
		public boolean isWordStart(char c) {
			boolean isStart = c == word.charAt(0);
			position = 1;

			return isStart;
		}
	}

	public SimpleWordRule(String word, IToken token) {
		super(new SimpleWordDetector(word));

		addWord(word, token);
	}

}
package at.zugal.fitnesse.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;

import at.zugal.fitnesse.Constants;
import at.zugal.fitnesse.editor.rule.InvertedPatternRule;
import at.zugal.fitnesse.editor.rule.SimpleWordRule;

public class WikiTextTokenScanner extends RuleBasedScanner {

	private final List<IToken> bufferedTokens;

	public WikiTextTokenScanner() {
		this.bufferedTokens = new ArrayList<IToken>();

		setupRules();
	}

	protected List<IRule> createRules() {
		IToken defineUsage = new WikiToken.DefineUsageToken();
		IToken plainText = new WikiToken.PlainTextToken();
		IToken lineBreak = new WikiToken.LinebreakToken();
		IToken header1 = new WikiToken.Header1Token();
		IToken header2 = new WikiToken.Header2Token();
		IToken header3 = new WikiToken.Header3Token();
		setDefaultReturnToken(plainText);

		List<IRule> rules = new ArrayList<IRule>();
		rules.add(new SingleLineRule("${", "}", defineUsage));
		rules.add(new EndOfLineRule("!1 ", header1));
		rules.add(new EndOfLineRule("!2 ", header2));
		rules.add(new EndOfLineRule("!3 ", header3));
		rules.add(new SimpleWordRule("\n", lineBreak));
		rules.add(new SimpleWordRule("\r\n", lineBreak));
		rules.add(new SimpleWordRule("\r", lineBreak));
		rules.add(new InvertedPatternRule(plainText, new char[] { Constants.TABLE_COLUMN_SEPARATOR_CHAR, '$', '\n', '\r' }));

		return rules;
	}

	/**
	 * Return the bufferedTokens.
	 * 
	 * @return the bufferedTokens
	 */
	public List<IToken> getBufferedTokens() {
		return bufferedTokens;
	}

	@Override
	public IToken nextToken() {
		int offsetBefore = fOffset;
		IToken token = super.nextToken();

		if (token instanceof WikiToken) {
			token = ((WikiToken) token).copy(fDocument, offsetBefore, fOffset - offsetBefore);
		} else {
			Assert.isTrue(token.isEOF());
		}

		bufferedTokens.add(token);
		return token;
	}

	@Override
	public void setRange(IDocument document, int offset, int length) {
		super.setRange(document, offset, length);

		bufferedTokens.clear();
	}

	private void setupRules() {
		List<IRule> rules = createRules();
		setRules(rules.toArray(new IRule[rules.size()]));
	}

}

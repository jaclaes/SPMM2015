package at.zugal.fitnesse.editor;

import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WordRule;

import at.zugal.fitnesse.editor.rule.SimpleWordRule;
import at.zugal.fitnesse.editor.rule.TableColumnSeparatorWordDetector;

public class WikiTableTokenScanner extends WikiTextTokenScanner {

	@Override
	protected List<IRule> createRules() {
		IToken start = new WikiToken.StartToken();
		IToken tableColumnSeparator = new WikiToken.TableColumnSeparatorToken();

		List<IRule> rules = super.createRules();
		rules.add(0, new WordRule(new TableColumnSeparatorWordDetector(), tableColumnSeparator));
		rules.add(0, new SimpleWordRule("start", start));

		return rules;
	}

}

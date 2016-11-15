package at.zugal.fitnesse.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

import at.zugal.fitnesse.Constants;
import at.zugal.fitnesse.editor.rule.InvertedPatternRule;
import at.zugal.fitnesse.editor.rule.TableRule;

public class WikiPartitionScanner extends RuleBasedPartitionScanner {

	public static final String TABLE = "table";
	public static final String TEXT = "text";
	public static final String[] PARTITION_TYPES = new String[] { TABLE, TEXT };
	public static final String WIKI_PARTITIONING = "__wiki_partitioning";

	public WikiPartitionScanner() {
		setupRules();
	}

	private void setupRules() {
		IToken text = new Token(TEXT);
		IToken table = new Token(TABLE);

		List<IPredicateRule> rules = new ArrayList<IPredicateRule>();
		rules.add(new TableRule(table));
		rules.add(new InvertedPatternRule(text, new char[] { Constants.TABLE_COLUMN_SEPARATOR_CHAR }));

		setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
	}
}

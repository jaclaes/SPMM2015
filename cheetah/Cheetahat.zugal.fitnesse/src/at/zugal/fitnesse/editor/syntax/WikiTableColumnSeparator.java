package at.zugal.fitnesse.editor.syntax;

import at.zugal.fitnesse.editor.WikiToken;

public class WikiTableColumnSeparator extends AbstractWikiSyntax {

	public WikiTableColumnSeparator(IWikiSyntax parent, WikiToken token) {
		super(parent, token);
	}

	@Override
	public void accept(IWikiSyntaxVisitor visitor) {
		visitor.visitTableColumnSeparator(this);
	}

}

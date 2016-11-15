package at.zugal.fitnesse.editor.syntax;

import at.zugal.fitnesse.editor.WikiToken;

public class WikiDefineUsage extends AbstractWikiSyntax {

	public WikiDefineUsage(IWikiSyntax parent, WikiToken token) {
		super(parent, token);
	}

	@Override
	public void accept(IWikiSyntaxVisitor visitor) {
		visitor.visitDefineUsage(this);
	}

}

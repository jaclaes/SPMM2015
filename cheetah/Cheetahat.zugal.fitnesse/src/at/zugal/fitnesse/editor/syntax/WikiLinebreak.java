package at.zugal.fitnesse.editor.syntax;

import at.zugal.fitnesse.editor.WikiToken;

public class WikiLinebreak extends AbstractWikiSyntax {

	public WikiLinebreak(IWikiSyntax parent, WikiToken token) {
		super(parent, token);
	}

	@Override
	public void accept(IWikiSyntaxVisitor visitor) {
		visitor.visitLinebreak(this);
	}

}

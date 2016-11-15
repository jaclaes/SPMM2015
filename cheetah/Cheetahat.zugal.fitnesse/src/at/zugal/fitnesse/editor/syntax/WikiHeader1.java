package at.zugal.fitnesse.editor.syntax;

import at.zugal.fitnesse.editor.WikiToken;

public class WikiHeader1 extends AbstractWikiSyntax implements IWikiSyntax {

	public WikiHeader1(IWikiSyntax parent, WikiToken token) {
		super(parent, token);
	}

	@Override
	public void accept(IWikiSyntaxVisitor visitor) {
		visitor.visitHeader1(this);
	}

}

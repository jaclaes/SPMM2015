package at.zugal.fitnesse.editor.syntax;

import at.zugal.fitnesse.editor.WikiToken;

public class WikiHeader2 extends AbstractWikiSyntax implements IWikiSyntax {

	public WikiHeader2(IWikiSyntax parent, WikiToken token) {
		super(parent, token);
	}

	@Override
	public void accept(IWikiSyntaxVisitor visitor) {
		visitor.visitHeader2(this);
	}

}

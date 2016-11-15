package at.zugal.fitnesse.editor.syntax;

import at.zugal.fitnesse.editor.WikiToken;

public class WikiPlainText extends AbstractWikiSyntax {

	public WikiPlainText(IWikiSyntax parent, WikiToken token) {
		super(parent, token);
	}

	@Override
	public void accept(IWikiSyntaxVisitor visitor) {
		visitor.visitPlainText(this);
	}

}

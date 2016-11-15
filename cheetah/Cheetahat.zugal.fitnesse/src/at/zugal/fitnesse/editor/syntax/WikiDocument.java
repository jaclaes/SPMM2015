package at.zugal.fitnesse.editor.syntax;

public class WikiDocument extends IntermediateWikiSyntax {

	public WikiDocument() {
		super(null, null);
	}

	@Override
	public void accept(IWikiSyntaxVisitor visitor) {
		visitor.visitDocumentStart(this);

		for (IWikiSyntax child : children) {
			child.accept(visitor);
		}

		visitor.visitDocumentEnd(this);
	}

	@Override
	public String getType() {
		return DOCUMENT;
	}

}

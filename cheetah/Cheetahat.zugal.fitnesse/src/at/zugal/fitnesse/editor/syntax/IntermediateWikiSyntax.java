package at.zugal.fitnesse.editor.syntax;

import at.zugal.fitnesse.editor.WikiToken;

/**
 * Syntax element representing intermediate nodes in the syntax tree.
 * 
 * @author user
 * 
 */
public abstract class IntermediateWikiSyntax extends AbstractWikiSyntax {

	protected IntermediateWikiSyntax(IWikiSyntax parent, WikiToken token) {
		super(parent, token);
	}

	@Override
	public int getLength() {
		int length = 0;

		for (IWikiSyntax child : children) {
			length += child.getLength();
		}

		return length;
	}

	@Override
	public int getOffset() {
		return children.get(0).getOffset();
	}

	@Override
	public IWikiSyntax getSyntaxAt(int offset) {
		for (IWikiSyntax child : children) {
			IWikiSyntax syntax = child.getSyntaxAt(offset);
			if (syntax != null) {
				return syntax;
			}
		}

		return null;
	}

	@Override
	public String getText() {
		StringBuilder text = new StringBuilder();
		for (IWikiSyntax child : children) {
			text.append(child.getText());
		}

		return text.toString();
	}

	@Override
	public String getTextUntil(int offset) {
		if (getSyntaxAt(offset - 1) == null) {
			throw new IllegalArgumentException("Invalid offset: " + offset);
		}

		int relativeOffset = offset - getOffset();
		return WikiToken.trim(getText().substring(0, relativeOffset));
	}

	@Override
	public abstract String getType();

	@Override
	public String trim() {
		StringBuilder text = new StringBuilder();
		for (IWikiSyntax child : children) {
			text.append(child.trim());
		}

		return text.toString();
	}

	@Override
	public String trimLineBreak() {
		StringBuilder text = new StringBuilder();
		for (IWikiSyntax child : children) {
			text.append(child.trimLineBreak());
		}

		return text.toString();
	}
}

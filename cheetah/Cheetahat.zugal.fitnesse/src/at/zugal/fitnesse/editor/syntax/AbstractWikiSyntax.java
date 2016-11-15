package at.zugal.fitnesse.editor.syntax;

import java.util.ArrayList;
import java.util.List;

import at.zugal.fitnesse.editor.WikiToken;

public abstract class AbstractWikiSyntax implements IWikiSyntax {
	private final WikiToken token;
	protected final IWikiSyntax parent;
	protected final List<IWikiSyntax> children;

	protected AbstractWikiSyntax(IWikiSyntax parent, WikiToken token) {
		this.parent = parent;
		this.token = token;
		this.children = new ArrayList<IWikiSyntax>();
	}

	@Override
	public void addChild(IWikiSyntax child) {
		children.add(child);
	}

	@Override
	public List<IWikiSyntax> getChildren() {
		return children;
	}

	@Override
	public int getLength() {
		return token.getLength();
	}

	@Override
	public int getOffset() {
		return token.getOffset();
	}

	/**
	 * Return the parent.
	 * 
	 * @return the parent
	 */
	public IWikiSyntax getParent() {
		return parent;
	}

	@Override
	public IWikiSyntax getSyntaxAt(int offset) {
		if (token.at(offset)) {
			return this;
		}

		return null;
	}

	@Override
	public String getText() {
		return token.getText();
	}

	@Override
	public String getTextUntil(int offset) {
		return token.getTextUntil(offset);
	}

	@Override
	public String getType() {
		return token.getType();
	}

	@Override
	public String toString() {
		return getType();
	}

	@Override
	public String trim() {
		return token.trim();
	}

	@Override
	public String trimLineBreak() {
		return token.trimLineBreak();
	}
}

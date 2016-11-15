package at.zugal.fitnesse.editor.syntax;

public class WikiTableCell extends IntermediateWikiSyntax {

	public WikiTableCell(IWikiSyntax parent) {
		super(parent, null);
	}

	@Override
	public void accept(IWikiSyntaxVisitor visitor) {
		visitor.visitTableCellStart(this);

		for (IWikiSyntax child : children) {
			child.accept(visitor);
		}

		visitor.visitTableCellEnd(this);
	}

	/**
	 * Determine the length of the cell, excluding trailing whitespace and separators.
	 * 
	 * @return the length of the cell
	 */
	public int getCellLength() {
		if (!isValidCell()) {
			return 0;
		}

		int cellLength = 0;
		for (IWikiSyntax child : children) {
			cellLength += child.trimLineBreak().length();
		}

		return cellLength;
	}

	@Override
	public String getType() {
		return IWikiSyntax.TABLE_CELL;
	}

	public boolean isLastCellInRow() {
		return ((WikiTableRow) parent).isLastCellInRow(this);
	}

	public boolean isValidCell() {
		int separatorCount = 0;
		for (IWikiSyntax child : children) {
			if (child.getType().endsWith(IWikiSyntax.TABLE_COLUMN_SEPARATOR)) {
				separatorCount++;
			}
		}

		if (((WikiTableRow) parent).getCellIndex(this) == 0) {
			return separatorCount >= 2;
		}

		return separatorCount >= 1;
	}

	/**
	 * Fill the content of the cell up to the given size with whitespaces.
	 * 
	 * @param size
	 *            the desired size
	 * @return the filled cell
	 */
	public String padd(int size) {
		IWikiSyntax lastSeparator = null;
		String trimmed = trimLineBreak();
		int toPad = size - trimmed.length();
		if (toPad <= 0) {
			return trim();
		}

		// find the separator before which whitespaces are inserted
		for (int i = children.size() - 1; i >= 0; i--) {
			IWikiSyntax child = children.get(i);
			if (child.getType().equals(IWikiSyntax.TABLE_COLUMN_SEPARATOR)) {
				lastSeparator = child;
				break;
			}
		}

		StringBuilder content = new StringBuilder();
		for (IWikiSyntax child : children) {
			if (child.equals(lastSeparator)) {
				for (int i = 0; i < toPad; i++) {
					content.append(' ');
				}
			}

			content.append(child.trim());
		}

		return content.toString();
	}

}

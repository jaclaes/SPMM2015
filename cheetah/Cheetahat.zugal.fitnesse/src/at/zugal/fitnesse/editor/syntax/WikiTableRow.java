package at.zugal.fitnesse.editor.syntax;

public class WikiTableRow extends IntermediateWikiSyntax {

	public WikiTableRow(IWikiSyntax parent) {
		super(parent, null);
	}

	@Override
	public void accept(IWikiSyntaxVisitor visitor) {
		visitor.visitTableRowStart(this);

		for (IWikiSyntax child : children) {
			child.accept(visitor);
		}

		visitor.visitTableRowEnd(this);
	}

	public int getAccmuluatedCellSizes() {
		int length = 0;
		for (IWikiSyntax child : children) {
			length += ((WikiTableCell) child).getCellLength();
		}

		return length;
	}

	public int getCellIndex(WikiTableCell cell) {
		return children.indexOf(cell);
	}

	public WikiTableCell getLastCell() {
		return (WikiTableCell) children.get(children.size() - 1);
	}

	@Override
	public String getType() {
		return IWikiSyntax.TABLE_ROW;
	}

	/**
	 * Determines how may valid cells the row contains.
	 * 
	 * @return the amount of valid cells
	 */
	public int getValidCellCount() {
		int cellCount = 0;
		for (IWikiSyntax child : children) {
			if (((WikiTableCell) child).isValidCell()) {
				cellCount++;
			}
		}

		return cellCount;
	}

	public boolean isLastCellInRow(WikiTableCell wikiTableCell) {
		for (int i = children.indexOf(wikiTableCell) + 1; i < children.size(); i++) {
			WikiTableCell cell = (WikiTableCell) children.get(i);

			if (cell.isValidCell()) {
				return false;
			}
		}

		return true;
	}

}

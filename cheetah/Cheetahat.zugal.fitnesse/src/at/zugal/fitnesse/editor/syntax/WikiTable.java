package at.zugal.fitnesse.editor.syntax;

import java.util.List;

public class WikiTable extends IntermediateWikiSyntax {

	public WikiTable(IWikiSyntax parent) {
		super(parent, null);
	}

	@Override
	public void accept(IWikiSyntaxVisitor visitor) {
		visitor.visitTableStart(this);

		for (IWikiSyntax child : children) {
			child.accept(visitor);
		}

		visitor.visitTableEnd(this);
	}

	/**
	 * Count the amount of columns of the header.
	 * 
	 * @return the columns in the header
	 */
	private int countHeaderColumns() {
		int maxColumns = 0;

		for (IWikiSyntax row : children) {
			maxColumns = Math.max(maxColumns, ((WikiTableRow) row).getValidCellCount());
		}

		return maxColumns;
	}

	/**
	 * Count the columns, excluding the header.
	 * 
	 * @return the amount of columns, not taking into account the header
	 */
	private int countNonHeaderColumns() {
		int maxColumns = 0;

		for (int i = 1; i < children.size(); i++) {
			WikiTableRow row = (WikiTableRow) children.get(i);
			maxColumns = Math.max(maxColumns, row.getValidCellCount());
		}

		return maxColumns;
	}

	/**
	 * Count how many columns the table has.
	 * 
	 * @return the maximum amount of columns
	 */
	public int countTableColumns() {
		if (children.size() <= 1) {
			return countHeaderColumns();
		}

		return countNonHeaderColumns();
	}

	/**
	 * Determine the length of the header, <b>including</b> the separators.
	 * 
	 * @return the length of the header
	 */
	public int getHeaderLength() {
		if (children.size() < 1) {
			return 0;
		}

		int maxLength = 0;
		for (int i = 0; i < children.size() && i < 2; i++) {
			WikiTableRow row = (WikiTableRow) children.get(i);
			int rowLength = row.getAccmuluatedCellSizes();
			maxLength = Math.max(maxLength, rowLength);
		}

		return maxLength;
	}

	/**
	 * Return the last row, <code>null</code> if there are no rows.
	 * 
	 * @return the last row
	 */
	public WikiTableRow getLastRow() {
		if (children.isEmpty()) {
			return null;
		}

		return (WikiTableRow) children.get(children.size() - 1);
	}

	/**
	 * Determine the longest cell for the given index, not taking into account whitespaces.
	 * 
	 * @return the length of the longest cell in the given column
	 */
	public int getMaximumCellLength(int columnIndex) {
		int maxLength = 0;
		int i = 2;

		if (columnIndex == 0) {
			i = 1; // also consider the first row if dealing with the first column (cf. bug#53)
		}

		for (; i < children.size(); i++) {
			IWikiSyntax row = children.get(i);
			List<IWikiSyntax> rowChildren = row.getChildren();

			if (columnIndex >= rowChildren.size()) {
				continue;
			}

			WikiTableCell cell = (WikiTableCell) rowChildren.get(columnIndex);
			if (cell.isValidCell()) {
				maxLength = Math.max(maxLength, cell.getCellLength());
			}
		}

		return maxLength;
	}

	public int getRowNumber(WikiTableRow currentRow) {
		return children.indexOf(currentRow);
	}

	@Override
	public String getType() {
		return TABLE;
	}

}

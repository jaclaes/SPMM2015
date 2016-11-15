package at.zugal.fitnesse.editor.formatting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.zugal.fitnesse.editor.syntax.AbstractWikiSyntaxVisitor;
import at.zugal.fitnesse.editor.syntax.IWikiSyntax;
import at.zugal.fitnesse.editor.syntax.WikiTable;
import at.zugal.fitnesse.editor.syntax.WikiTableCell;
import at.zugal.fitnesse.editor.syntax.WikiTableRow;

public class WikiTableFormattingVistor extends AbstractWikiSyntaxVisitor {
	private final StringBuilder formatted;
	private final Map<Integer, Integer> cachedTableColumnSizes;
	private WikiTable table;
	private WikiTableRow currentRow;
	private int maximumRowLength;
	private int currentRowLength;

	public WikiTableFormattingVistor() {
		this.formatted = new StringBuilder();
		this.cachedTableColumnSizes = new HashMap<Integer, Integer>();
	}

	private void adaptMaximumRowLength(WikiTable table) {
		List<IWikiSyntax> tableChildren = table.getChildren();
		if (tableChildren.size() > 2 && tableChildren.get(1).getChildren().size() >= 1) {
			IWikiSyntax secondRow = table.getChildren().get(1);
			int startCellLength = ((WikiTableCell) secondRow.getChildren().get(0)).getCellLength();
			int expectedSize = cachedTableColumnSizes.get(0);
			int additionalSpace = expectedSize - startCellLength;

			if (((WikiTableRow) secondRow).getAccmuluatedCellSizes() + additionalSpace > maximumRowLength) {
				maximumRowLength += Math.max(additionalSpace, 0);
			}
		}
	}

	public String getFormatted() {
		return formatted.toString();
	}

	@Override
	public void visitTableCellStart(WikiTableCell cell) {
		if (!cell.isValidCell()) {
			formatted.append(cell.trim());
			return;
		}

		int index = currentRow.getCellIndex(cell);
		int expectedCellSize = 0;
		if (cachedTableColumnSizes.containsKey(index)) {
			expectedCellSize = cachedTableColumnSizes.get(index);
		}

		// also format the first cell of the second row, cf. bug#53
		boolean secondRowFirstColumn = table.getRowNumber(currentRow) == 1 && index == 0;
		if (table.getRowNumber(currentRow) < 2 && !secondRowFirstColumn) {
			expectedCellSize = 0;// do not append whitespace for the header row
		}

		if (cell.isLastCellInRow()) {
			expectedCellSize = maximumRowLength - currentRowLength;
		}

		int lengthBefore = formatted.length();
		formatted.append(cell.padd(expectedCellSize));
		currentRowLength += formatted.length() - lengthBefore;
	}

	@Override
	public void visitTableRowStart(WikiTableRow row) {
		currentRow = row;
		currentRowLength = 0;
	}

	@Override
	public void visitTableStart(WikiTable table) {
		this.table = table;
		this.cachedTableColumnSizes.clear();

		int columns = table.countTableColumns();
		int totalLength = 0;

		for (int i = 0; i < columns; i++) {
			int maximumCellLength = table.getMaximumCellLength(i);
			totalLength += maximumCellLength;
			cachedTableColumnSizes.put(i, maximumCellLength);
		}

		int headerLength = table.getHeaderLength();
		int headerWithoutPaddedStart = headerLength;
		if (cachedTableColumnSizes.size() > 1 && table.getChildren().size() >= 1 && table.getChildren().get(0).getChildren().size() >= 1) {
			int startCellLength = ((WikiTableCell) table.getChildren().get(0).getChildren().get(0)).getCellLength();
			// take into account that the "start" cell is also formatted
			headerWithoutPaddedStart -= Math.max(0, cachedTableColumnSizes.get(0) - startCellLength);
		}
		if (totalLength < headerWithoutPaddedStart && columns > 2) {
			// distribute free space
			for (int i = 0; i < headerLength - totalLength; i++) {
				int currentSize = cachedTableColumnSizes.get(i % columns);
				currentSize++;
				cachedTableColumnSizes.put(i % columns, currentSize);
			}
		}

		maximumRowLength = Math.max(headerLength, totalLength);

		// fix for bug#53 - also format the first cell of the second row
		adaptMaximumRowLength(table);
	}
}

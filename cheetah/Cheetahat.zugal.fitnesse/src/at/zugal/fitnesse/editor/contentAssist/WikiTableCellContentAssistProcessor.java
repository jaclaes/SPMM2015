package at.zugal.fitnesse.editor.contentAssist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

import at.zugal.fitnesse.Activator;
import at.zugal.fitnesse.Constants;
import at.zugal.fitnesse.ResourceManager;
import at.zugal.fitnesse.editor.syntax.IWikiSyntax;
import at.zugal.fitnesse.editor.syntax.WikiTable;
import at.zugal.fitnesse.editor.syntax.WikiTableCell;
import at.zugal.fitnesse.editor.syntax.WikiTableRow;

public class WikiTableCellContentAssistProcessor {

	/**
	 * Compute the proposals for the given table at the given offset.
	 * 
	 * @param table
	 *            the table
	 * @param offset
	 *            the offset
	 * @return the proposals
	 */
	public List<ICompletionProposal> computeProposals(WikiTable table, int offset) {
		int originalOffset = offset;
		IWikiSyntax cell = table.getSyntaxAt(offset);

		WikiTableRow lastRow = table.getLastRow();
		if (cell == null) {
			if (lastRow == null) {
				return Collections.emptyList(); // no table rows
			}
			cell = lastRow.getLastCell();
			if (lastRow.getLastCell().isValidCell()) {
				cell = null; // if the last cell is valid, there are no more cells ...
			}
		}

		String prefix = "";
		int cellIndex = 0;
		List<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();

		if (cell != null) {
			// cell is null if we are at the end of the table
			while (!cell.getType().equals(IWikiSyntax.TABLE_CELL)) {
				cell = cell.getParent();
			}

			// fix for bug#69: proposed content for wrong cell
			if (cell.equals(((WikiTableRow) cell.getParent()).getLastCell()) && ((WikiTableCell) cell).isValidCell()) {
				// prefix = "";
				cellIndex++;
			} else {
				prefix = cell.getTextUntil(offset);
				prefix = prefix.replaceAll("\\|", "");
				prefix = prefix.replaceAll("\n", "");
				prefix = prefix.replaceAll("\r", "");
			}
		}

		if (cell != null) {
			cellIndex += ((WikiTableRow) cell.getParent()).getCellIndex((WikiTableCell) cell);
		} else {
			cellIndex += table.getLastRow().getValidCellCount();
		}

		Set<String> proposedCompletions = new HashSet<String>();
		for (int i = 2; i < table.getChildren().size(); i++) {
			IWikiSyntax row = table.getChildren().get(i);

			// ignore proposals for other columns and the current column
			if (cellIndex >= row.getChildren().size() || row.getChildren().contains(cell)) {
				continue;
			}

			IWikiSyntax currentCell = row.getChildren().get(cellIndex);
			String currentCellContent = currentCell.trimLineBreak().replaceAll("\\|", "");
			if (prefix.equals(currentCell.trimLineBreak())) {
				continue; // same content --> no proposal
			}
			if (!currentCellContent.toLowerCase().startsWith(prefix.toLowerCase())) {
				continue;// no matching prefix
			}
			if (proposedCompletions.contains(currentCellContent)) {
				continue; // do not propose two times the same string
			}
			if (currentCellContent.trim().length() == 0) {
				continue; // do not propose empty content (cf. bug#50).
			}

			CompletionProposal proposal = new CompletionProposal(currentCellContent + Constants.TABLE_COLUMN_SEPARATOR_CHAR, originalOffset
					- prefix.length(), prefix.length(), currentCellContent.length() + 1, ResourceManager.getPluginImage(Activator
					.getDefault(), "img/action_fixture_proposal.gif"), currentCellContent, null, null);

			proposedCompletions.add(currentCellContent);
			proposals.add(proposal);
		}

		return proposals;
	}
}

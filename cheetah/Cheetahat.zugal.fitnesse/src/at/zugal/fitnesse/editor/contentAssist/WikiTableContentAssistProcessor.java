package at.zugal.fitnesse.editor.contentAssist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import at.zugal.fitnesse.Constants;
import at.zugal.fitnesse.editor.WikiTableTokenScanner;
import at.zugal.fitnesse.editor.syntax.IWikiSyntax;
import at.zugal.fitnesse.editor.syntax.WikiDocument;
import at.zugal.fitnesse.editor.syntax.WikiParser;
import at.zugal.fitnesse.editor.syntax.WikiTable;

public class WikiTableContentAssistProcessor implements IContentAssistProcessor {
	public static String TABLE_COLUMN_SEPARATOR_STRING = String.valueOf(Constants.TABLE_COLUMN_SEPARATOR_CHAR);

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		WikiTableTokenScanner scanner = new WikiTableTokenScanner();
		scanner.setRange(viewer.getDocument(), 0, viewer.getDocument().getLength());
		WikiDocument document = new WikiParser(scanner).parse();
		IWikiSyntax table = document.getSyntaxAt(offset);

		// if the caret is immediately after a table, use the table as context
		if (table == null) {
			table = document.getSyntaxAt(offset - 1);
			if (table == null) {
				return new ICompletionProposal[0]; // no table
			}
		}

		while (!table.getType().equals(IWikiSyntax.TABLE)) {
			table = table.getParent();

			if (table == null) {
				return new ICompletionProposal[0];
			}
		}

		List<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();
		proposals.addAll(new WikiTableCellContentAssistProcessor().computeProposals((WikiTable) table, offset));

		Collections.sort(proposals, new CompletionProposalComparator());
		return proposals.toArray(new ICompletionProposal[proposals.size()]);
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { Constants.TABLE_COLUMN_SEPARATOR_CHAR };
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

}

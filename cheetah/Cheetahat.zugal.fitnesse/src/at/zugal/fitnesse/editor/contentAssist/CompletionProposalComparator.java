package at.zugal.fitnesse.editor.contentAssist;

import java.text.Collator;
import java.util.Comparator;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

public class CompletionProposalComparator implements Comparator<ICompletionProposal> {

	@Override
	public int compare(ICompletionProposal o1, ICompletionProposal o2) {
		return Collator.getInstance().compare(o1.getDisplayString(), o2.getDisplayString());
	}

}

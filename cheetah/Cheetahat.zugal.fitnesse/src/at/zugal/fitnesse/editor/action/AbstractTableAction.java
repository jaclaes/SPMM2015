package at.zugal.fitnesse.editor.action;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;

import at.zugal.fitnesse.Activator;
import at.zugal.fitnesse.editor.WikiPartitionScanner;

public abstract class AbstractTableAction extends Action {
	private final ISourceViewer viewer;

	public AbstractTableAction(ISourceViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * Carry out the logic of the action, e.g., jumping to the next cell in a table.
	 * 
	 * @param offset
	 *            the current offset in the document
	 * @param viewer
	 *            the underlying viewer
	 */
	protected abstract void doRun(int offset, ISourceViewer viewer) throws BadLocationException;

	@Override
	public void run() {
		try {
			IDocument document = viewer.getDocument();
			int offset = viewer.getTextWidget().getCaretOffset();
			ITypedRegion region = document.getPartition(offset);
			if (!WikiPartitionScanner.TABLE.equals(region.getType())) {
				return;
			}

			doRun(offset, viewer);
		} catch (BadLocationException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not jump to next table column separator.");
			Activator.getDefault().getLog().log(status);
		}
	}
}

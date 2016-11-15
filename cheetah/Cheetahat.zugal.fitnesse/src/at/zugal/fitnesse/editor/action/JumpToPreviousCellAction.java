package at.zugal.fitnesse.editor.action;

import static at.zugal.fitnesse.Constants.TABLE_COLUMN_SEPARATOR_CHAR;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;

public class JumpToPreviousCellAction extends AbstractTableAction {

	public JumpToPreviousCellAction(ISourceViewer viewer) {
		super(viewer);

		setId("at.zugal.fitnesse.actions.JumpToPreviousCellAction");
		setText("Jump to Previous Cell");
	}

	@Override
	protected void doRun(int offset, ISourceViewer viewer) throws BadLocationException {
		IDocument document = viewer.getDocument();

		for (int i = offset - 2; i >= 0; i--) {
			char currentChar = document.getChar(i);

			if (currentChar == TABLE_COLUMN_SEPARATOR_CHAR) {
				viewer.getTextWidget().setCaretOffset(Math.max(0, i + 1));
				break;
			}
		}
	}

}

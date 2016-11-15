package at.zugal.fitnesse.editor.action;

import static at.zugal.fitnesse.Constants.TABLE_COLUMN_SEPARATOR_CHAR;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;

public class JumpToNextCellAction extends AbstractTableAction {

	public JumpToNextCellAction(ISourceViewer viewer) {
		super(viewer);

		setId("at.zugal.fitnesse.actions.JumpToNextCellAction");
		setText("Jump to Next Cell");
	}

	@Override
	protected void doRun(int offset, ISourceViewer viewer) throws BadLocationException {
		IDocument document = viewer.getDocument();

		for (int i = offset + 1; i < document.getLength(); i++) {
			char currentChar = document.getChar(i);

			if (currentChar == TABLE_COLUMN_SEPARATOR_CHAR) {
				viewer.getTextWidget().setCaretOffset(Math.min(i + 1, document.getLength() - 1));
				break;
			}
		}
	}

}

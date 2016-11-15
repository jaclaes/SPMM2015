package at.zugal.fitnesse.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;

public class FormatSourceAction extends Action {
	private final SourceViewer viewer;

	public FormatSourceAction(SourceViewer viewer) {
		this.viewer = viewer;

		setId("at.zugal.fitnesse.editor.actions.FormatSourceAction");
		setText("Format");
	}

	@Override
	public void run() {
		viewer.doOperation(ISourceViewer.FORMAT);
	}
}

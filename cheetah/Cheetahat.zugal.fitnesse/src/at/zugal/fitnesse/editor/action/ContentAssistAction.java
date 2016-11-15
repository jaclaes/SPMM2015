package at.zugal.fitnesse.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;

public class ContentAssistAction extends Action {
	private final SourceViewer viewer;

	public ContentAssistAction(SourceViewer viewer) {
		this.viewer = viewer;

		setId("at.zugal.fitnesse.actions.ContentAssistAction");
		setText("Content Assist");
	}

	@Override
	public void run() {
		viewer.doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
	}
}

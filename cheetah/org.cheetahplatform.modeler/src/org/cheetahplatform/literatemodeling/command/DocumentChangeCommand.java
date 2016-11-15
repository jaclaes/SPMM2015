package org.cheetahplatform.literatemodeling.command;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;

public class DocumentChangeCommand extends LiterateCommand {

	private DocumentEvent event;
	private String replaced = "";

	public DocumentChangeCommand(LiterateModel graph, DocumentEvent event) {
		this(graph, event, false);
	}

	public DocumentChangeCommand(LiterateModel graph, DocumentEvent event, boolean preExecuted) {
		super(graph, preExecuted);
		this.event = event;
	}

	@Override
	public void execute() {
		if (!isPreExecuted()) {
			try {
				replaced = event.getDocument().get(event.getOffset(), event.getLength());
				event.getDocument().replace(event.getOffset(), event.getLength(), event.getText());
				preExecuted = false;
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}

		AuditTrailEntry entry = createAuditrailEntry(LiterateCommands.LM_DOCUMENT_CHANGED);
		entry.setAttribute(LiterateCommands.OFFSET, event.getOffset());
		entry.setAttribute(LiterateCommands.LENGTH, event.getLength());
		entry.setAttribute(LiterateCommands.TEXT, event.getText());
		entry.setAttribute(LiterateCommands.CHANGE_TEXT_TIME, System.currentTimeMillis());

		log(entry);
	}

	@Override
	protected String getAffectedElementName() {
		return "Document";
	}

	@Override
	public void undo() {
		try {
			event.getDocument().replace(event.getOffset(), event.getText().length(), replaced);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

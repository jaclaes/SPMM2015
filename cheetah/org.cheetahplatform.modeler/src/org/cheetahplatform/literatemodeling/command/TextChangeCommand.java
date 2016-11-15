package org.cheetahplatform.literatemodeling.command;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.literatemodeling.model.LiterateModel;

public class TextChangeCommand extends LiterateCommand {

	private String textType;
	private String text;
	private String oldText;

	public TextChangeCommand(LiterateModel graph, String textType, String text) {
		this(graph, textType, text, false);
	}

	public TextChangeCommand(LiterateModel graph, String textType, String text, boolean preExecuted) {
		super(graph, preExecuted);
		this.textType = textType;
		this.text = text;
	}

	@Override
	public void execute() {
		LiterateModel model = (LiterateModel) graph;
		if (textType.equals(LiterateCommands.TEXT_DESCRIPTION)) {
			oldText = model.getDescription();
			model.setDescription(text);
			if (!preExecuted) {
				model.fireDescriptionChanged();
			}

		} else if (textType.equals(LiterateCommands.TEXT_NAME)) {
			oldText = model.getName();
			model.setName(text);
			if (!preExecuted) {
				model.fireNameChanged();
			}
		} else {
			throw new RuntimeException("unsupported text type");
		}

		AuditTrailEntry entry = createAuditrailEntry(LiterateCommands.LM_TEXT_CHANGED);
		entry.setAttribute(LiterateCommands.TEXT_TYPE, textType);
		entry.setAttribute(LiterateCommands.TEXT, text);
		entry.setAttribute(LiterateCommands.CHANGE_TEXT_TIME, System.currentTimeMillis());

		log(entry);
	}

	@Override
	protected String getAffectedElementName() {
		return "Text";
	}

	@Override
	public void undo() {
		LiterateModel model = (LiterateModel) graph;
		if (textType.equals(LiterateCommands.TEXT_DESCRIPTION)) {
			model.setDescription(oldText);
		} else if (textType.equals(LiterateCommands.TEXT_NAME)) {
			model.setName(oldText);
		} else {
			throw new RuntimeException("unsupported text type");
		}
	}

}
